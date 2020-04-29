package model;

import java.util.Observable;

public class catModel extends Observable{
private char[][] field;
private static final char seed = 's';
private static final char empty = 'e';
private static final char grown = 'g';
private int money;
private int catnipRemaining;
private static int legacy = 1000;
private static boolean buyable = false;
	
	public catModel() {
		money = 0;
		catnipRemaining = 0;
		field = new char[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				field[i][j] = empty;
			}
		}
		printOutField();
	}
	
	private void printOutField() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 10; i++) {
			System.out.println();
			for (int j = 0; j < 10; j++) {
				System.out.print(field[i][j]);
			}
		}
	}

	public void plantCatnip(int i, int j) {
		if (field[i][j] == empty) {
			field[i][j] = seed;
			setChanged();
			notifyObservers();
		}
	}
	
	public void harvest(int i, int j) {
		if (field[i][j] == grown) {
			field[i][j] = empty;
			catnipRemaining += 1;
			setChanged();
			notifyObservers();
		}
		if (catnipRemaining >= legacy) {
			legacy = catnipRemaining;
		}
	}
	
	public void sold(int amount) {
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
