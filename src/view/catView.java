package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;

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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.catModel;

/**
 * This class is View
 * @author ianfang
 *
 */
public class catView extends Application implements Observer{
	private catController controller;
	private ImageView[][] imageBoard;
	private catModel model;
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * This displays game board
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		controller = new catController(this);
		imageBoard = new ImageView[10][10];
		primaryStage.setTitle("Katten Idle Game");
		BorderPane window = new BorderPane();
		window.setTop(RowsOfBoard());
		Scene scene = new Scene(window, 750, 600);
		scene.setOnDragOver(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				// TODO Auto-generated method stub
				event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				event.consume();
			}
		});
		DrawBoard();
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * This function divided the game board into three rows
	 * Then use VBox to display these rows
	 * @return
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("static-access")
	private VBox RowsOfBoard() throws FileNotFoundException {
		// TODO Auto-generated method stub
		VBox gameBoard = new VBox();
		HBox hb_firstRow = new HBox();
		HBox hb_money = new HBox();
		HBox hb_grass = new HBox();
		HBox hb_seed = new HBox();
		HBox hb_button = new HBox();
		Label totalMoney = new Label();
		Label catNip = new Label();
		FileInputStream moneyBag;
		FileInputStream grass;
		FileInputStream seeds;
		try {
			moneyBag = new FileInputStream("src/money.jpg");
			grass = new FileInputStream("src/grass.jpg");
			seeds = new FileInputStream("src/seed.jpg");
			Image money = new Image(moneyBag);
			Image grassImage = new Image(grass);
			Image seedsImage = new Image(seeds);
			ImageView showMoney = new ImageView();
			ImageView showGrass = new ImageView();
			ImageView showSeeds = new ImageView();
			showMoney.setImage(money);
			showGrass.setImage(grassImage);
			showSeeds.setImage(seedsImage);
			totalMoney.setFont(Font.font ("Verdana", 15));
			totalMoney.setText(String.valueOf(controller.getMoney()));
			catNip.setFont(Font.font ("Verdana", 15));
			catNip.setText(String.valueOf(controller.getCatnip()) + "/" + String.valueOf(controller.getLegacy()));
			hb_money.getChildren().addAll(showMoney, totalMoney);
			hb_money.setAlignment(Pos.CENTER);
			hb_money.setMargin(showMoney, new Insets(20,20,20,20));
			hb_grass.getChildren().addAll(showGrass, catNip);
			hb_grass.setAlignment(Pos.CENTER);
			hb_grass.setMargin(showGrass, new Insets(20,20,20,40));
			hb_seed.getChildren().add(showSeeds);
			hb_seed.setMargin(showSeeds, new Insets(20,20,20,40));
			seedDrag(showSeeds);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Button collect = new Button("Collect");
		collect.setOnMouseClicked((event) -> {
			controller.harvest();
			catNip.setText(String.valueOf(controller.getCatnip()) + "/" + String.valueOf(controller.getLegacy()));
		});
		Button sell = new Button("  Sell  ");
		sell.setOnMouseClicked((event) -> {
			PopWindow pop = new PopWindow(controller, totalMoney, catNip);
			pop.display();
		});
		hb_button.getChildren().addAll(collect, sell);
		hb_button.setAlignment(Pos.CENTER);
		hb_button.setMargin(collect, new Insets(20,20,20,40));
		hb_button.setMargin(sell, new Insets(20,20,20,20));
		hb_firstRow.getChildren().addAll(hb_money, hb_grass, hb_seed, hb_button);
		hb_firstRow.setAlignment(Pos.CENTER_LEFT);
		HBox hb_secondRow = new HBox();
		FileInputStream getCat;
		HBox hb_cat = new HBox();
		try {
			getCat = new FileInputStream("src/cat.jpg");
			Image catImage = new Image(getCat);
			ImageView cat = new ImageView(catImage);
			hb_cat.getChildren().add(cat);
			hb_cat.setMargin(cat, new Insets(20,20,20,20));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		hb_secondRow.getChildren().addAll(hb_cat);
		HBox hb_thirdRow = new HBox();
		GridPane grid = new GridPane();
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
				grid.add(imageBoard[i][j], j, i);
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
		hb_thirdRow.setMargin(grid, new Insets(20,20,20,20));
		gameBoard.getChildren().addAll(hb_firstRow, hb_secondRow, hb_thirdRow);
		return gameBoard;
	}

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
	 * Observable
	 */
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		DrawBoard();
	}

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
				}
				else if(newboard[i][j] == 's') {
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
 * This is the pop up window class
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
