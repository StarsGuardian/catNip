package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;

import controller.catController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class catView extends Application implements Observer{
	private catController controller;
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		controller = new catController(this);
		primaryStage.setTitle("Katten Idle Game");
		BorderPane window = new BorderPane();
		window.setTop(firstRowOfBoard());
		Scene scene = new Scene(window, 750, 600);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@SuppressWarnings("static-access")
	private HBox firstRowOfBoard() {
		// TODO Auto-generated method stub
		HBox hb_firstRow = new HBox();
		HBox hb_money = new HBox();
		HBox hb_grass = new HBox();
		HBox hb_button = new HBox();
		Label totalMoney = new Label();
		Label catNip = new Label();
		FileInputStream moneyBag;
		FileInputStream grass;
		try {
			moneyBag = new FileInputStream("src/money.jpg");
			grass = new FileInputStream("src/grass.jpg");
			Image money = new Image(moneyBag);
			Image grassImage = new Image(grass);
			ImageView showMoney = new ImageView();
			ImageView showGrass = new ImageView();
			showMoney.setImage(money);
			showGrass.setImage(grassImage);
			totalMoney.setText(String.valueOf(controller.getMoney()));
			catNip.setText(String.valueOf(controller.getCatnip()) + "/" + String.valueOf(controller.getLegacy()));
			hb_money.getChildren().addAll(showMoney, totalMoney);
			hb_money.setAlignment(Pos.CENTER);
			hb_money.setMargin(showMoney, new Insets(20,20,20,20));
			hb_grass.getChildren().addAll(showGrass, catNip);
			hb_grass.setAlignment(Pos.CENTER);
			hb_grass.setMargin(showGrass, new Insets(20,20,20,40));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Button collect = new Button("Collect");
		collect.setOnMouseClicked((event) -> {
			controller.harvest();
			catNip.setText(String.valueOf(controller.getCatnip()) + "/" + String.valueOf(controller.getLegacy()));
		});
		Button sell = new Button("Sell");
		sell.setOnMouseClicked((event) -> {
			PopWindow pop = new PopWindow(controller, totalMoney, catNip);
			pop.display();
		});
		hb_button.getChildren().addAll(collect, sell);
		hb_button.setAlignment(Pos.CENTER);
		hb_button.setMargin(collect, new Insets(20,20,20,40));
		hb_button.setMargin(sell, new Insets(20,20,20,20));
		hb_firstRow.getChildren().addAll(hb_money, hb_grass, hb_button);
		hb_firstRow.setAlignment(Pos.CENTER_LEFT);
		return hb_firstRow;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}

class PopWindow extends Stage {
	private catController control;
	private Label newMoney;
	private Label grass;
	
	public PopWindow(catController controller, Label totalMoney, Label catNip) {
		control = controller;
		newMoney = totalMoney;
		grass = catNip;
	}
	
	@SuppressWarnings("static-access")
	public void display() {
		Button confirm = new Button("Confirm");
		Button cancel = new Button("Cancel");
		Label amount = new Label("Enter amount: ");
		TextField enter = new TextField();
		Stage popStage = new Stage();
		GridPane popup = new GridPane();
		Scene newScene = new Scene(popup, 350, 100);
		HBox labelNtext = new HBox();
		labelNtext.getChildren().addAll(amount, enter);
		labelNtext.setMargin(amount, new Insets(30,30,20,30));
		labelNtext.setAlignment(Pos.CENTER);
		HBox buttons = new HBox();
		buttons.getChildren().addAll(confirm, cancel);
		buttons.setMargin(confirm, new Insets(0,30,30,50));
		buttons.setMargin(cancel, new Insets(0,30,30,0));
		buttons.setAlignment(Pos.CENTER);
		confirm.setOnMouseClicked((event) -> {
			control.sellCatnip(Integer.parseInt(enter.getText()));
			popStage.close();
			newMoney.setText(String.valueOf(control.getMoney()));
			grass.setText(String.valueOf(control.getCatnip()) + "/" + String.valueOf(control.getLegacy()));
		});
		cancel.setOnMouseClicked((event) -> {
			popStage.close();
		});
		VBox setAll = new VBox();
		setAll.getChildren().addAll(labelNtext, buttons);
		popup.add(setAll, 3, 3);
		popStage.setScene(newScene);
		popStage.show();
	}
}
