package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import view.catView;

/**
 * This class is model, all the methods inside this class control the field
 * 
 * @author ianfang
 *
 */
public class catModel extends Observable {
	// This field controls each slot in view
	private char[][] field;
	// This char represents seed, the state before harvestable
	private static final char seed = 's';
	// This char represents empty slot, this slot is available for planting new seed
	private static final char empty = 'e';
	// This char represents grown catnip, this state means harvestable
	private static final char grown = 'g';
	// This keeps tracking the total money;
	private static int money;
	// This keeps tracking the remaining catnip
	private static int catnipRemaining;
	// This is the legacy
	private int legacy = 1000;
	// This parameter checks if money is enough for buying new land
	private boolean buyable = false;
	// This parameter checks if harvest is called
	private boolean harvestCalled = false;
	// These parameter represent spring, summer, fall, winter
	private boolean spring, summer, fall, winter;
	// This contains timertask for each slot
	private List<TimerTask> slottask;
	// This contains timetask
	private TimerTask timetask;
	// This contains timeline
	private Timer timer_season;
	// this contains flag of initializing
	private boolean initializing;
	// this contains timer for field
	private Timer timer_slot;
	// this contains total land available
	private int land;

	/**
	 * Constructor, each time when model is initialized it retrives game state from
	 * file to accomplish background running
	 */
	public catModel(catView view) {
		field = new char[5][10];
		slottask = new ArrayList<TimerTask>();
		timer_season = new Timer();
		timer_slot = new Timer();
		initializing = true;
		this.addObserver(view);
		try {
			RetriveState();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initializing = false;
		startTimer();
	}

	/**
	 * this method starts a timer to keep tracking current season
	 */
	private void startTimer() {
		// TODO Auto-generated method stub
		timetask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getSeason();
			}
		};
		timer_season.schedule(timetask, 1000, 1000); // The task will execute after one second later, then repeat per
														// second.
	}

	/**
	 * this method will be called when user clicks exit button
	 * 
	 * /** The player quits the client, so all of TimerTask(s) should be terminated.
	 */
	public void exitGame() {
		timer_season.cancel();
		timer_slot.cancel();
	}

	/**
	 * This private method calls three functions to retrive three different parts of
	 * the game state
	 * 
	 * @throws IOException
	 */
	private void RetriveState() throws IOException {
		getSeason();
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 10; j++) {
				field[i][j] = empty;
			}
		}
		getPlantMonitor();
		getFieldStates();
		getMoneyNRemaining();
		gettotalland();
	}

	/**
	 * check current available land
	 */
	private void gettotalland() {
		// TODO Auto-generated method stub
		try {
			File landFile = new File("land.txt");
			Scanner read_land = new Scanner(landFile);
			while (read_land.hasNext()) {
				land = Integer.parseInt(read_land.nextLine().trim());
			}
			read_land.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * returns total available land
	 * 
	 * @return land
	 */
	public int getLand() {
		return land;
	}

	/**
	 * this method reads data.txt which contains money and remaining catnip
	 * information
	 */
	private void getMoneyNRemaining() {
		// TODO Auto-generated method stub
		int lineCounter = 0;
		File file_data = new File("data.txt");
		Scanner reader_data;
		try {
			reader_data = new Scanner(file_data);
			while (reader_data.hasNextLine()) {
				if (lineCounter == 0) {
					money = Integer.parseInt(reader_data.nextLine().trim());
				} else {
					catnipRemaining = Integer.parseInt(reader_data.nextLine().trim());
				}
				lineCounter++;
			}
			reader_data.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method reads time.txt file which contains the state and plant state of
	 * each field slot and the coordinates of each slot If harvest is not called,
	 * then grown plant will not be removed If harvest is called, harvested plant
	 * information will be removed from the time.txt file which allows user to place
	 * a new seed on empty field
	 */
	private void getPlantMonitor() {
		// TODO Auto-generated method stub
		File file_catnip = new File("time.txt");
		Scanner reader_catnip;
		try {
			reader_catnip = new Scanner(file_catnip);
			List<String> line = new ArrayList<String>();
			while (reader_catnip.hasNextLine()) {
				String string = reader_catnip.nextLine().trim();
				String[] filecontent = string.split(" ", 3);
				long time = Long.parseLong(filecontent[0]);
				int row = Integer.parseInt(filecontent[1]);
				int col = Integer.parseInt(filecontent[2]);
				long duration = System.currentTimeMillis() - time;
				System.out.println("time elapsed for this catnip: ");
				System.out.println(duration / 1000 / 60);
				// different season have requires different amount of time
				// for catnip to grow
				if (duration / 1000 / 60 >= 5 && spring) {
					grown(row, col);
					line.add(string);
				} else if (duration / 1000 / 60 >= 3 && summer) {
					grown(row, col);
					line.add(string);
				} else if (duration / 1000 / 60 >= 1 && fall) {
					grown(row, col);
					line.add(string);
				} else if (duration / 1000 / 60 >= 10 && winter) {
					grown(row, col);
					line.add(string);
				} else {
					boolean fromFile = true;
					plantCatnip(row, col, fromFile);
				}
			}
			reader_catnip.close();

			// if a slot is havested, remove the slot information from file
			// and update the file
			reader_catnip = new Scanner(file_catnip);
			FileWriter timeTemp = new FileWriter("timeTemp.txt");
			boolean flag = false;
			if (line.size() > 0) {
				while (reader_catnip.hasNextLine()) {
					String currentLine = reader_catnip.nextLine().trim();
					for (String lineToRemove : line) {
						if (lineToRemove.compareTo(currentLine) == 0) {
							flag = true;
						}
					}
					if (!flag) {
						timeTemp.write(currentLine);
						timeTemp.write("\n");
					}
					flag = false;
				}
				timeTemp.close();
				reader_catnip.close();
			}
			line.clear();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method reads field.txt, and reset the field to it's latest state
	 */
	private void getFieldStates() {
		// TODO Auto-generated method stub
		File file_field = new File("field.txt");
		Scanner reader_field;
		try {
			reader_field = new Scanner(file_field);
			int i = 0;
			while (reader_field.hasNextLine()) {
				String string = reader_field.nextLine().trim();
				for (int j = 0; j < string.length(); j++) {
					if (string.charAt(i) == empty) {
						field[i][j] = string.charAt(j);
					}
				}
				i++;
			}
			reader_field.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method controls season change method uses user local time and divides
	 * one hour into four parts, each season occupies 15 minutes
	 */
	public void getSeason() {
		// TODO Auto-generated method stub
		long elapsed = System.currentTimeMillis();
		if ((elapsed / 1000 / 60) % 60 >= 0 && (elapsed / 1000 / 60) % 60 < 15) {
			spring = true;
			summer = false;
			fall = false;
			winter = false;
		} else if ((elapsed / 1000 / 60) % 60 >= 15 && (elapsed / 1000 / 60) % 60 < 30) {
			spring = false;
			summer = true;
			fall = false;
			winter = false;
		} else if ((elapsed / 1000 / 60) % 60 >= 30 && (elapsed / 1000 / 60) % 60 < 45) {
			spring = false;
			summer = false;
			fall = true;
			winter = false;
		} else if ((elapsed / 1000 / 60) % 60 >= 45 && (elapsed / 1000 / 60) % 60 <= 59) {
			spring = false;
			summer = false;
			fall = false;
			winter = true;
		}
		if (initializing != true) {
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * this method checks if harvest is called
	 * 
	 * @return harvestCalled
	 */
	private boolean harvestIsCalled() {
		// TODO Auto-generated method stub
		return harvestCalled;
	}

	/**
	 * This method places one catnip seed in the target slot
	 * 
	 * @param i
	 * @param j
	 */
	public void plantCatnip(int i, int j, boolean fromFile) {
		if (field[i][j] == empty) {
			field[i][j] = seed;
			if (!fromFile) {
				updatePlantTime(i, j);
			}
			updateField(field);
			if (spring) {
				TimerTask task = new TimerTask() {
					int sec = 300;
					boolean changed = false;

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (summer && !changed) {
							sec -= 120;
							changed = true;
						}
						sec--;
						if (sec <= 0) {
							grown(i, j);
							this.cancel();
						}
					}
				};
				timer_slot.schedule(task, 1000, 1000);
			} else if (summer) {
				TimerTask task = new TimerTask() {
					int sec = 180;
					boolean changed = false;

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (fall && !changed) {
							sec -= 120;
							changed = true;
						}
						sec--;
						if (sec <= 0) {
							grown(i, j);
							this.cancel();
						}
					}
				};
				timer_slot.schedule(task, 1000, 1000);
			} else if (fall) {
				TimerTask task = new TimerTask() {
					int sec = 60;
					boolean changed = false;

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (winter && !changed) {
							sec += 540;
							changed = true;
						}
						sec--;
						if (sec <= 0) {
							grown(i, j);
							this.cancel();
						}
					}
				};
				timer_slot.schedule(task, 1000, 1000);
			} else if (winter) {
				TimerTask task = new TimerTask() {
					int sec = 600;
					boolean changed = false;

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (spring && !changed) {
							sec -= 300;
							changed = true;
						}
						sec--;
						if (sec <= 0) {
							grown(i, j);
							this.cancel();
						}
					}
				};
				timer_slot.schedule(task, 1000, 1000);
			}
		}
	}

	/**
	 * This private method updates all the slots when plant, harvest or grown takes
	 * place
	 * 
	 * @param field
	 */
	private void updateField(char[][] field) {
		// TODO Auto-generated method stub
		FileWriter fieldfile;
		try {
			fieldfile = new FileWriter("field.txt");
			for (char[] innerArray : field) {
				for (char slot : innerArray) {
					fieldfile.write(Character.toString(slot));
				}
				fieldfile.write("\n");
			}
			fieldfile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This private method write the time of each catnip has been placed back to the
	 * file and mark the slot
	 * 
	 * @param i
	 * @param j
	 */
	private void updatePlantTime(int i, int j) {
		// TODO Auto-generated method stub
		FileWriter timefile;
		try {
			timefile = new FileWriter("time.txt", true);
			timefile.write(
					String.valueOf(System.currentTimeMillis() + " " + String.valueOf(i) + " " + String.valueOf(j)));
			timefile.write("\n");
			timefile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method harvest the grown catnip update the remaining catnip and update
	 * the view directly
	 */
	public void harvest() {
		harvestCalled = true;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 10; j++) {
				if (field[i][j] == grown) {
					field[i][j] = empty;
					catnipRemaining += 1;
					updateTimefile();
					syncMoneyNip();
					if (catnipRemaining >= legacy) {
						legacy = catnipRemaining;
					}
				}
			}
		}
		updateField(field);
		setChanged();
		notifyObservers();
	}

	/**
	 * This method replace the time.txt if harvest is called if harvest is called,
	 * this method removes all the grown catnip info from the file, and keeps those
	 * has grown yet
	 */
	private void updateTimefile() {
		// TODO Auto-generated method stub
		if (harvestIsCalled()) {
			File modifyTime = new File("timeTemp.txt");
			Scanner reader_timeTemp;
			FileWriter timefile;
			try {
				reader_timeTemp = new Scanner(modifyTime);
				timefile = new FileWriter("time.txt");
				while (reader_timeTemp.hasNextLine()) {
					timefile.write(reader_timeTemp.nextLine());
					timefile.write("\n");
				}
				reader_timeTemp.close();
				timefile.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method synchronize money and remaining catnip update newest money and
	 * remaining catnip to file
	 */
	public void syncMoneyNip() {
		// TODO Auto-generated method stub
		FileWriter nipNmoney;
		try {
			nipNmoney = new FileWriter("data.txt");
			nipNmoney.write(String.valueOf(money) + "\n");
			nipNmoney.write(String.valueOf(catnipRemaining));
			nipNmoney.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method changes the state of catnip to grown
	 * 
	 * @param i
	 * @param j
	 */
	public void grown(int i, int j) {
		field[i][j] = grown;
		updateField(field);
		if (initializing != true) {
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * This method will be called when user sells catnip
	 * 
	 * @param amount
	 */
	public void sold(int amount) {
		catnipRemaining -= amount;
		money += Math.round(amount / 10);
		syncMoneyNip();
	}

	/**
	 * This method checks if user is eligible buying land
	 * 
	 * @return
	 */
	public boolean checkBuyable() {
		if (money >= 100) {
			buyable = true;
		}
		return buyable;
	}

	/**
	 * This method unlocks a new land for user and deduct the money by 100
	 */
	public void buyLand() {
		money -= 100;
		land += 1;
		if (money < 100) {
			buyable = false;
		}
		try {
			FileWriter land_w = new FileWriter("land.txt");
			land_w.write(String.valueOf(land));
			land_w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		syncMoneyNip();
	}

	public void consume() {
		// check system clock
		// catnipRemaining will be deducted by 5 per minute
	}

	/**
	 * this method returns the remaining catnip
	 * 
	 * @return catnipRemaining
	 */
	public int getCatNipRemaining() {
		return catnipRemaining;
	}

	/**
	 * this method returns the legacy amount
	 * 
	 * @return legacy
	 */
	public int getLegacy() {
		return legacy;
	}

	/**
	 * this method returns how much money user owns
	 * 
	 * @return money
	 */
	public int getMoney() {
		return money;
	}

	/**
	 * This is passed to controller for updating view this method returns the newest
	 * model state
	 * 
	 * @return field
	 */
	public char[][] getModel() {
		return field;
	}

	/**
	 * This method retrive the current season
	 * 
	 * @return one of the four seasons
	 */
	public String returnSeason() {
		if (spring) {
			return "Spring";
		} else if (summer) {
			return "Summer";
		} else if (fall) {
			return "Fall";
		} else {
			return "Winter";
		}
	}

	public void setMoney(int amount) {
		money += amount;
	}

}
