package controller;

import model.catModel;
import view.catView;

public class catController {
private catModel model;

	public catController(catView view) {
		model = new catModel();
		model.addObserver(view);
	}
	
	public void buyLand() {
		if (model.checkBuyable()) {
			model.buyLand();
		}
		else {
			System.out.println("Insufficient Funds!");
		}
	}
	
	public void sellCatnip(int amount) {
		if (amount >= 10) {
			model.sold(amount);
		}
		else {
			System.out.println("Minimum of 10 catnips are required to complete selling process!");
		}
	} 
	
	public void plantCatnip(int i, int j) {
		model.plantCatnip(i, j);
	}
	
	public void harvest() {
		model.harvest();
	}
	
	public void consumeCatnip() {
		model.consume();
	}
	
	public char[][] getNewFieldState(){
		return model.getModel();
	}
}
