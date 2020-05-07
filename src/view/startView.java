package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * 
 * @author Difeng Ye
 * A start view
 *
 */
public class startView extends Application{
	private VBox mainBox = new VBox();
	private Button button = new Button("Enter Game");
	private Button button1 = new Button("Intro");
	private Button button2 = new Button("Exit");
	public static void main(String[] args) {
		
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		mainBox.getChildren().addAll(button,button1,button2);
		mainBox.setPadding(new Insets(80, 300,300, 275));
		mainBox.setSpacing(30);
		mainBox.setPrefSize(200,50);
		button.setMinWidth(mainBox.getPrefWidth());
		button.setMinHeight(mainBox.getPrefHeight());
		button1.setMinWidth(mainBox.getPrefWidth());
		button1.setMinHeight(mainBox.getPrefHeight());
		button2.setMinWidth(mainBox.getPrefWidth());
		button2.setMinHeight(mainBox.getPrefHeight());
		FileInputStream getCat = new FileInputStream("src/catGirl.jpg"); // get catGirl image
		Image catImage = new Image(getCat);
		ImageView cat = new ImageView(catImage);
		
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color:white");
		root.setCenter(mainBox);
		root.setTop(cat);
		
		button.setOnMouseClicked(event->{
			
			catView Gview = new catView();
			try {
				Gview.start(new Stage());
				primaryStage.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		});
		
		button1.setOnMouseClicked(event->{
			IntroWindow introWindow  = new IntroWindow();
			introWindow.display();
		});
		
		button2.setOnMouseClicked(event->{
			primaryStage.close();
		});
		Scene scene = new Scene(root);
		FileInputStream geticon = new FileInputStream("src/icon.jpg"); 
		Image icon = new Image(geticon);
		primaryStage.getIcons().add(icon);
		primaryStage.setScene(scene);		
		primaryStage.setWidth(750);
		primaryStage.setHeight(700);
		primaryStage.centerOnScreen();
		primaryStage.setTitle("Katten Idle Game");
		primaryStage.show();
		
	}

}


class IntroWindow extends Stage {
	Stage popStage = new Stage();
	TextArea textArea = new TextArea("A wandering cat just lost his parents "
			+ "due to a car accident and his parents left him a legacy - "
			+ "one thousand catnips. The player must help the cat to "
			+ "survive and eventually help him to form a family."
			+ " As the game begins, the player will help the cat "
			+ "collect his parent's legacy, and sell partial catnips for money."
			+ " Once the player has $100, then farmland can be set up for producing"
			+ " catnips. A year divides into four seasons, the spring season can "
			+ "produce 10 catnips per min, and the summer season can produce 20 "
			+ "catnips, and autumn can produce 50 catnips while winter season "
			+ "produces nothing. The player must diligently harvest the "
			+ "farmland because if you don't harvest, the farmland will"
			+ " be not available to continue to produce catnips and it will "
			+ "eventually result in the catnips of deposit will decrease till zero, "
			+ "which means the cat will be dead due to no food (a cat eats 5 catnips "
			+ "per min). When the number of catnips reaches 800, the cat will have a girlfriend. "
			+ "If the player remains 800+ catnips for 10 mins, then you have successfully helped "
			+ "the cat to form a family, and your mission is completed.");
	public void display() {
		textArea.setEditable(false);
		textArea.setWrapText(true);
		BorderPane pane = new BorderPane();
		Scene newScene = new Scene(pane,500,250);
		pane.setStyle("-fx-background-color:white");
		pane.setCenter(textArea);
		FileInputStream geticon;
		try {
			geticon = new FileInputStream("src/icon.jpg");
			Image icon = new Image(geticon);
			popStage.getIcons().add(icon);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		popStage.setScene(newScene);
		popStage.show();
	}

}
