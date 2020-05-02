package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;

import com.sun.prism.paint.Color;

import controller.catController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.catModel;

/**
 * This class is catView, all the methods included in this class creates the
 * game board for user
 * 
 * @author ianfang
 *
 */
public class catView extends Application implements Observer {
	private catController controller;
	private ImageView[][] imageBoard;

	/**
	 * For test purpose
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * This displays game board imageBoard is a 2D array which contains ImageView
	 * object in each slot This game board is a borderpane contains a vbox which has
	 * three hbox in it first hbox holds money, catnipremaining and seed picture, as
	 * well as collect cancel button second hbox holds cat image, third hbox holds
	 * gridpane with 100 ImageView object inside the gridpane
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		controller = new catController(this); // Initialize controller and pass view as a parameter
		imageBoard = new ImageView[10][10]; // Initialize imageBoard 2D array which holds 100 ImageView object
		primaryStage.setTitle("Katten Idle Game"); // Set window name
		BorderPane window = new BorderPane(); // window is BorderPane
		window.setTop(RowsOfBoard()); // vbox is set at the top of borderpane
		window.setStyle("-fx-background-color:white"); // set background color to white
		Scene scene = new Scene(window, 750, 600);
		// enable drag and drop on this scene
		scene.setOnDragOver(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				// TODO Auto-generated method stub
				event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				event.consume();
			}
		});
		DrawBoard(); // support background running
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * This function divided the game board into three rows each row is contained by
	 * a hbox all three hboxes are wrapped into the vbox
	 * 
	 * @return vbox
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("static-access")
	private VBox RowsOfBoard() throws FileNotFoundException {
		// TODO Auto-generated method stub
		VBox gameBoard = new VBox(); // outter vbox
		HBox hb_firstRow = new HBox(); // first hbox
		HBox hb_money = new HBox(); // hbox inside first hbox contains moneybag image
		HBox hb_grass = new HBox(); // hbox inside first hbox contains catnip image
		HBox hb_seed = new HBox(); // hbox inside first hbox contains seed image
		HBox hb_button = new HBox(); // hbox inside first hbox contains two buttons
		Label totalMoney = new Label(); // label displays total amount of money
		Label catNip = new Label(); // label displays remaining catnip
		FileInputStream moneyBag;
		FileInputStream grass;
		FileInputStream seeds;
		try {
			moneyBag = new FileInputStream("src/money.jpg"); // get money image
			grass = new FileInputStream("src/grass.jpg"); // get catnip image
			seeds = new FileInputStream("src/seed.jpg"); // get seed image
			Image money = new Image(moneyBag);
			Image grassImage = new Image(grass);
			Image seedsImage = new Image(seeds);
			ImageView showMoney = new ImageView();
			ImageView showGrass = new ImageView();
			ImageView showSeeds = new ImageView();
			showMoney.setImage(money);
			showGrass.setImage(grassImage);
			showSeeds.setImage(seedsImage);
			totalMoney.setFont(Font.font("Verdana", 15)); // set font
			totalMoney.setText(String.valueOf(controller.getMoney())); // get label content
			catNip.setFont(Font.font("Verdana", 15));
			catNip.setText(String.valueOf(controller.getCatnip()) + "/" + String.valueOf(controller.getLegacy())); // get
																													// label
																													// content
			hb_money.getChildren().addAll(showMoney, totalMoney);
			hb_money.setAlignment(Pos.CENTER);
			hb_money.setMargin(showMoney, new Insets(20, 20, 20, 20));
			hb_grass.getChildren().addAll(showGrass, catNip);
			hb_grass.setAlignment(Pos.CENTER);
			hb_grass.setMargin(showGrass, new Insets(20, 20, 20, 40));
			hb_seed.getChildren().add(showSeeds);
			hb_seed.setMargin(showSeeds, new Insets(20, 20, 20, 40));
			seedDrag(showSeeds); // set drag on seed image
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// set click event on button collect
		Button collect = new Button("Collect");
		collect.setOnMouseClicked((event) -> {
			controller.harvest();
			// update the remaining label
			catNip.setText(String.valueOf(controller.getCatnip()) + "/" + String.valueOf(controller.getLegacy()));
		});
		// set click event on cancel button
		Button sell = new Button("  Sell  ");
		sell.setOnMouseClicked((event) -> {
			PopWindow pop = new PopWindow(controller, totalMoney, catNip);
			pop.display();
		});
		hb_button.getChildren().addAll(collect, sell);
		hb_button.setAlignment(Pos.CENTER);
		hb_button.setMargin(collect, new Insets(20, 20, 20, 40));
		hb_button.setMargin(sell, new Insets(20, 20, 20, 20));
		hb_firstRow.getChildren().addAll(hb_money, hb_grass, hb_seed, hb_button);
		hb_firstRow.setAlignment(Pos.CENTER_LEFT);
		// second hbox for second row
		HBox hb_secondRow = new HBox();
		FileInputStream getCat;
		HBox hb_cat = new HBox();
		try {
			getCat = new FileInputStream("src/cat.jpg"); // get cat image
			Image catImage = new Image(getCat);
			ImageView cat = new ImageView(catImage);
			hb_cat.getChildren().add(cat);
			hb_cat.setMargin(cat, new Insets(20, 20, 20, 20));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		hb_secondRow.getChildren().addAll(hb_cat);
		// third hbox for third row
		HBox hb_thirdRow = new HBox();
		GridPane grid = new GridPane(); // gridpane for the field
		grid.setGridLinesVisible(false);
		grid.setHgap(20);
		grid.setVgap(20);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				FileInputStream blank = new FileInputStream("src/plants.jpg");
				Image slot = new Image(blank);
				imageBoard[i][j] = new ImageView();
				imageBoard[i][j].setImage(slot);
				int row = i, col = j;
				grid.add(imageBoard[i][j], j, i); // each slot is an imageview object
				// set drag drop event on imageview object inside gridpane
				imageBoard[i][j].setOnDragDropped(new EventHandler<DragEvent>() {

					@Override
					public void handle(DragEvent event) {
						// TODO Auto-generated method stub
						boolean success = false;
						Dragboard db = event.getDragboard();
						if (db.hasImage()) {
							imageBoard[row][col].setImage(db.getImage());
							controller.plantCatnip(row, col);
							success = true;
						}
						event.setDropCompleted(success);
						event.consume();
					}
				});
			}
		}
		hb_thirdRow.getChildren().addAll(grid);
		hb_thirdRow.setMargin(grid, new Insets(20, 20, 20, 20));
		// adding all three hbox into vbox
		gameBoard.getChildren().addAll(hb_firstRow, hb_secondRow, hb_thirdRow);
		return gameBoard;
	}

	// set drag event on seed image
	private void seedDrag(ImageView seed) {
		// TODO Auto-generated method stub
		seed.setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				Dragboard db = seed.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				content.putImage(seed.getImage());
				db.setContent(content);
				event.consume();
			}
		});
	}

	/**
	 * This method will be called by model if any update was made
	 */
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		DrawBoard();
	}

	/**
	 * This method will re-draw the gridpane to display the latest update
	 */
	private void DrawBoard() {
		// TODO Auto-generated method stub
		char[][] newboard = controller.getNewFieldState();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (newboard[i][j] == 'g') {
					FileInputStream blank;
					try {
						blank = new FileInputStream("src/plants.jpg");
						Image slot = new Image(blank);
						imageBoard[i][j].setImage(slot);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (newboard[i][j] == 's') {
					FileInputStream update;
					try {
						update = new FileInputStream("src/seed.jpg");
						Image slot = new Image(update);
						imageBoard[i][j].setImage(slot);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}

/**
 * This is the pop up window class This will be called if user wants to sell
 * 
 * @author ianfang
 *
 */
class PopWindow extends Stage {
	private catController control;
	private Label newMoney;
	private Label grass;

	public PopWindow(catController controller, Label totalMoney, Label catNip) {
		control = controller;
		newMoney = totalMoney;
		grass = catNip;
	}

	/**
	 * draw the pop up window
	 */
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
		labelNtext.setMargin(amount, new Insets(30, 30, 20, 30));
		labelNtext.setAlignment(Pos.CENTER);
		HBox buttons = new HBox();
		buttons.getChildren().addAll(confirm, cancel);
		buttons.setMargin(confirm, new Insets(0, 30, 30, 50));
		buttons.setMargin(cancel, new Insets(0, 30, 30, 0));
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
