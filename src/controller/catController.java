package controller;

import java.io.FileWriter;
import java.io.IOException;

import model.catModel;
import view.catView;

/**
 * This class is catController, all the methods in this class will be called by
 * view to pass data to the model
 * 
 * @author ianfang
 *
 */
public class catController {
	private catModel model;

	/**
	 * constructor, this takes the view as parameter and give it to model model will
	 * be initialized here
	 * 
	 * @param view
	 */
	public catController(catView view) {
		model = new catModel(view);
	}

	/**
	 * this method will be called if user tries to buy land
	 */
	public void buyLand() {
		if (checkBuyable()) {
			model.buyLand();
		} else {
			System.out.println("Insufficient Funds!");
		}
	}

	public boolean checkBuyable() {
		// TODO Auto-generated method stub
		return model.checkBuyable();
	}

	/**
	 * this method will be called if user sells catnip
	 * 
	 * @param amount
	 */
	public void sellCatnip(int amount) {
		if (amount >= 10) {
			model.sold(amount);
		} else {
			System.out.println("Minimum of 10 catnips are required to complete selling process!");
		}
	}

	/**
	 * this method will be called if user place a seed into empty field
	 * 
	 * @param i
	 * @param j
	 */
	public void plantCatnip(int i, int j, boolean fromFile) {
		model.plantCatnip(i, j, fromFile);
	}

	/**
	 * this method will be called if user clicks collect button to harvest
	 */
	public void harvest() {
		model.harvest();
	}

	/**
	 * this method will be called automatically every minute to accomplish the
	 * consumption made by cat
	 */
	public void consumeCatnip() {
		model.consume();
	}

	/**
	 * this method will get the newest model state
	 * 
	 * @return
	 */
	public char[][] getNewFieldState() {
		return model.getModel();
	}

	/**
	 * this method will get the remaining catnip
	 * 
	 * @return catnip amount
	 */
	public int getCatnip() {
		return model.getCatNipRemaining();
	}

	/**
	 * this method will get money
	 * 
	 * @return money
	 */
	public int getMoney() {
		return model.getMoney();
	}

	/**
	 * this method will get legacy
	 * 
	 * @return legacy
	 */
	public int getLegacy() {
		return model.getLegacy();
	}

	/**
	 * this method gets current season
	 * 
	 * @return
	 */
	public String getSeason() {
		return model.returnSeason();
	}

	/**
	 * this method updates money
	 */
	public void updateMoney(int amount) {
		model.setMoney(amount);
		model.syncMoneyNip();
	}

	/**
	 * exit the game
	 */
	public void exitGame() {
		model.exitGame();
	}

	/**
	 * this method gets total number of available field
	 * 
	 * @return
	 */
	public int getAvailableLand() {
		return model.getLand();
	}

	public void paySpeed(int amount) {
		model.spendMoney(amount);
		model.syncMoneyNip();
	}

	/**
	 * this method will be called if user clicked restart game, this method reset
	 * all the game data to default
	 */
	public void resetGame() {
		// TODO Auto-generated method stub
		try {
			FileWriter field = new FileWriter("field.txt");
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 10; j++) {
					field.write("e");
				}
				field.write("\n");
			}
			field.close();
			FileWriter time = new FileWriter("time.txt");
			time.write("");
			time.close();
			FileWriter data = new FileWriter("data.txt");
			data.write("0 " + String.valueOf(System.currentTimeMillis()) + "\n");
			data.write("100 " + String.valueOf(System.currentTimeMillis()));
			data.close();
			FileWriter land = new FileWriter("land.txt");
			land.write("25");
			land.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
