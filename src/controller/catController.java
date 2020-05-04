package controller;

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
		if (model.checkBuyable()) {
			model.buyLand();
		} else {
			System.out.println("Insufficient Funds!");
		}
	}
	
	/**
	 * This method is the same as buyLand()
	 * Only difference is it calls a modified method that doesn't call observers for testing purposes
	 */
	public void buyLandForTest() {
		if (model.checkBuyable()) {
			model.buyLandForTest();
		} else {
			System.out.println("Insufficient Funds!");
		}
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
	public void plantCatnip(int i, int j) {
		model.plantCatnip(i, j);
	}

	/**
	 * this method will be called if user clicks collect button to harvest
	 */
	public void harvest() {
		model.harvest();
	}
	
	/**
	 * This method is the same as harvest()
	 * The only difference is it calls a modified method that doesn't call observers for testing purposes
	 */
	public void harvestForTest() {
		model.harvestForTest();
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
}
