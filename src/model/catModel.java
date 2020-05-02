package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;

import view.catView;

/**
 * This class is model
 * @author ianfang
 *
 */ 
public class catModel extends Observable{
	//This field controls each slot in view
	private char[][] field;
	//This char represents seed, the state before harvestable
	private static final char seed = 's';
	//This char represents empty slot, this slot is available for planting new seed
	private static final char empty = 'e';
	//This char represents grown catnip, this state means harvestable
	private static final char grown = 'g';
	//This keeps tracking the total money;
	private static int money;
	//This keeps tracking the remaining catnip
	private static int catnipRemaining;
	//This is the legacy
	private int legacy = 1000;
	//This parameter checks if money is enough for buying new land
	private boolean buyable = false;
	//This parameter checks if harvest is called
	private boolean harvestCalled = false;
	//These parameter represent spring, summer, fall, winter
	private boolean spring, summer, fall, winter;
	
	/**
	 * Constructor, each time when model is initialized
	 * it retrives game state from file to accomplish background running
	 */
	public catModel(catView view) {
		field = new char[10][10];
		this.addObserver(view);
		try {
			RetriveState();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This private method reads three file to retrive
	 * the game state
	 * @throws IOException
	 */
	private void RetriveState() throws IOException {
		getFieldStates();
		getSeason();
		getPlantMonitor();
		getMoneyNRemaining();
	}
	
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
				}
				else {
					catnipRemaining = Integer.parseInt(reader_data.nextLine().trim());
				}
				lineCounter ++;
			}
			reader_data.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getPlantMonitor() {
		// TODO Auto-generated method stub
		File file_catnip = new File("time.txt");
		Scanner reader_catnip;
		try {
			reader_catnip = new Scanner(file_catnip);
			List<String> line = new ArrayList<String>();
			while(reader_catnip.hasNextLine()) {
				String string = reader_catnip.nextLine().trim();
				String[] filecontent = string.split(" ", 3);
				long time = Long.parseLong(filecontent[0]);
				int row = Integer.parseInt(filecontent[1]);
				int col = Integer.parseInt(filecontent[2]);
				long duration = System.currentTimeMillis() - time;
				System.out.println(duration);
				System.out.println(duration / 1000 / 60);
				if (duration / 1000 / 60 >= 1 && spring) {
					grown(row, col);
					line.add(string);
				}
				else if (duration / 1000 / 60 >= 3 && summer) {
					grown(row, col);
					line.add(string);
				}
				else if (duration / 1000 / 60 >= 1 && fall) {
					grown(row, col);
					line.add(string);
				}
				else if (duration / 1000 / 60 >= 1 && winter) {
					grown(row, col);
					line.add(string);
				}
				else {
					plantCatnip(row, col);
				}
			}
			reader_catnip.close();
			
			reader_catnip = new Scanner(file_catnip);
			FileWriter timeTemp = new FileWriter("timeTemp.txt");
			boolean flag = false;
			if (line.size() > 0) {
				while (reader_catnip.hasNextLine()) {
					String currentLine = reader_catnip.nextLine().trim();
					for (String lineToRemove: line) {
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

	private void getFieldStates() {
		// TODO Auto-generated method stub
		File file_field = new File("field.txt");
		Scanner reader_field;
		try {
			reader_field = new Scanner(file_field);
			int i = 0;
			while(reader_field.hasNextLine()) {
				String string = reader_field.nextLine().trim();
				for (int j = 0; j < string.length(); j++) {
						field[i][j] = string.charAt(j);
				}
				i ++;
			}
			reader_field.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getSeason() {
		// TODO Auto-generated method stub
		long elapsed = System.currentTimeMillis();
		if ((elapsed / 1000 / 60) % 60 >= 0 && (elapsed / 1000 / 60) % 60 < 15) {
			spring = true;
			summer = false;
			fall = false;
			winter = false;
			System.out.println("spring");
		}
		else if ((elapsed / 1000 / 60) % 60 >= 15 && (elapsed / 1000 / 60) % 60 < 30) {
			spring = false;
			summer = true;
			fall = false;
			winter = false;
			System.out.println("summer");
		}
		else if ((elapsed / 1000 / 60) % 60 >= 30 && (elapsed / 1000 / 60) % 60 < 45) {
			spring = false;
			summer = false;
			fall = true;
			winter = false;
			System.out.println("fall");
		}
		else if ((elapsed / 1000 / 60) % 60 >= 45 && (elapsed / 1000 / 60) % 60 <= 59) {
			spring = false;
			summer = false;
			fall = false;
			winter = true;
			System.out.println("winter");
		}
	}

	private boolean harvestIsCalled() {
		// TODO Auto-generated method stub
		return harvestCalled;
	}

	/**
	 * This method places one catnip in the target slot 
	 * @param i
	 * @param j
	 */
	public void plantCatnip(int i, int j) {
		if (field[i][j] == empty) {
			field[i][j] = seed;
			updatePlantTime(i, j);
			updateField(field);
		}
	}

	/**
	 * This private method update all the slots when plant, harvest
	 * or grown takes place
	 * @param field
	 */
	private void updateField(char[][] field) {
		// TODO Auto-generated method stub
		FileWriter fieldfile;
		try {
			fieldfile = new FileWriter("field.txt");
			for (char[] innerArray: field) {
				for (char slot: innerArray) {
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
	 * This private method write the time of each catnip has been placed in the slot
	 * @param i
	 * @param j
	 */
	private void updatePlantTime(int i, int j) {
		// TODO Auto-generated method stub
		FileWriter timefile;
		try {
			timefile = new FileWriter("time.txt", true);
			timefile.write(String.valueOf(System.currentTimeMillis() + " " + String.valueOf(i) + " " + String.valueOf(j)));
			timefile.write("\n");
			timefile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method harvest the grown catnip
	 */
	public void harvest() {
		harvestCalled = true;
		for (int i = 0; i < 10; i++) {
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
	 * This method synchronize money and remaining catnip
	 */
	private void syncMoneyNip() {
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
	 * @param i
	 * @param j
	 */
	public void grown(int i, int j) {
		if (field[i][j] == seed) {
			field[i][j] = grown;
			updateField(field);
			setChanged();
			notifyObservers();
		}
	}
	
	/**
	 * This method will be called when user sells catnip
	 * @param amount
	 */
	public void sold(int amount) {
		catnipRemaining -= amount;
		money += Math.round(amount / 10);
		syncMoneyNip();
	}
	
	/**
	 * This method checks if user is eligible buying land
	 * @return
	 */
	public boolean checkBuyable() {
		if (money >= 100) {
			buyable = true;
		}
		return buyable;
	}
	
	/**
	 * This method unlocks a new land for user and deduct
	 * the money by 100
	 */
	public void buyLand() {
		money -= 100;
		if (money < 100) {
			buyable = false;
		}
		syncMoneyNip();
		setChanged();
		notifyObservers();
	}
	
	
	public void consume() {
		//check system clock
		//catnipRemaining will be deducted by 5 per minute
	}
	
	public int getCatNipRemaining() {
		return catnipRemaining;
	}
	
	public int getLegacy() {
		return legacy;
	}
	
	public int getMoney() {
		return money;
	}
	/**
	 * This is passed to controller for updating view
	 * @return
	 */
	public char[][] getModel(){
		return field;
	}
	
}
