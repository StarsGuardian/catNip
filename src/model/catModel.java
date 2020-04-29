package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;

public class catModel extends Observable{
private char[][] field;
private static final char seed = 's';
private static final char empty = 'e';
private static final char grown = 'g';
private static int money;
private static int catnipRemaining;
private int legacy = 1000;
private boolean buyable = false;
	
	public static void main(String[] args) throws IOException {
		catModel mo = new catModel();
		mo.plantCatnip(0, 0);
		mo.plantCatnip(1, 1);
		mo.plantCatnip(2, 2);
		mo.plantCatnip(3, 3);
		mo.harvest();
	}
	public catModel() {
		field = new char[10][10];
		try {
			checkState();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setChanged();
		notifyObservers();
	}

	private void checkState() throws IOException {
		// TODO Auto-generated method stub
		File file_field = new File("field.txt");
		File file_catnip = new File("time.txt");
		Scanner reader_field = new Scanner(file_field);
		Scanner reader_catnip = new Scanner(file_catnip);
		
		int i = 0;
		while(reader_field.hasNextLine()) {
			String string = reader_field.nextLine().trim();
			for (int j = 0; j < string.length(); j++) {
					field[i][j] = string.charAt(j);
			}
			i ++;
		}
		reader_field.close();
		
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
			if (duration / 1000 / 60 >= 1) {
				grown(row, col);
				line.add(string);
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
			File modifyTime = new File("timeTemp.txt");
			Scanner reader_timeTemp = new Scanner(modifyTime);
			FileWriter timefile = new FileWriter("time.txt");
			while (reader_timeTemp.hasNextLine()) {
				timefile.write(reader_timeTemp.nextLine());
				timefile.write("\n");
			}
			reader_timeTemp.close();
			timefile.close();
		}
	}

	public void plantCatnip(int i, int j) {
		if (field[i][j] == empty) {
			field[i][j] = seed;
			try {
				FileWriter timefile = new FileWriter("time.txt", true);
				FileWriter fieldfile = new FileWriter("field.txt");
				timefile.write(String.valueOf(System.currentTimeMillis() + " " + String.valueOf(i) + " " + String.valueOf(j)));
				timefile.write("\n");
				timefile.close();
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
			setChanged();
			notifyObservers();
		}
	}
	
	public void harvest() throws IOException {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (field[i][j] == grown) {
					field[i][j] = empty;
					catnipRemaining += 1;
					if (catnipRemaining >= legacy) {
						legacy = catnipRemaining;
					}
				}
			}
		}
		FileWriter fieldfile = new FileWriter("field.txt");
		for (char[] innerArray: field) {
			for (char slot: innerArray) {
				fieldfile.write(Character.toString(slot));
			}
			fieldfile.write("\n");
		}
		fieldfile.close();
		setChanged();
		notifyObservers();
	}
	
	public void grown(int i, int j) throws IOException {
		if (field[i][j] == seed) {
			field[i][j] = grown;
			FileWriter fieldfile = new FileWriter("field.txt");
			for (char[] innerArray: field) {
				for (char slot: innerArray) {
					fieldfile.write(Character.toString(slot));
				}
				fieldfile.write("\n");
			}
			fieldfile.close();
		}
	}
	
	public void sold(int amount) throws IOException {
		catnipRemaining -= amount;
		money += Math.round(amount / 10);
	}
	
	public boolean checkBuyable() {
		if (money >= 100) {
			buyable = true;
		}
		return buyable;
	}
	
	public void buyLand() {
		money -= 100;
		if (money < 100) {
			buyable = false;
		}
		setChanged();
		notifyObservers();
	}
	
	public void consume() {
		//check system clock
		//catnipRemaining will be deducted by 5 per minute
	}
	
}
