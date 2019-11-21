package ui;

import threads.*;
import model.*;

import java.io.IOException;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Controller {
	
	@FXML
	private Pane boardPane;
	
	@FXML
	private Label informationLabel;
	
	private List<GUIBall> shapes;
	
	private List<Ball> bll;
	
	private List<MovingThread> mts;
	
	private FileManager sfm;
	
	private boolean runningGame;
	
	
	@FXML
	void initialize() {
		shapes = new ArrayList<GUIBall>();
		bll = new ArrayList<Ball>();
		try {
			sfm = new FileManager(this);
		}catch(IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		mts = new ArrayList<MovingThread>();
		runningGame = false;
		GUIUpdaterThread gut = new GUIUpdaterThread(this);
		gut.setDaemon(true);
		gut.start();
	}
	
	
	@FXML
	void newGame(ActionEvent e) {
		try{
			runningGame = true;
			startGame();
		}catch(IllegalArgumentException ex) {
			Alert t = new Alert(AlertType.INFORMATION, "There's currently a game on board.");
			t.setContentText("Please finish the current game first.");
			t.show();
		}
	}
	
	
	public void startGame() {
		runningGame = true;
		Random rnd = new Random();
		//Change this number to modify the number of Balls in screen.
		int numberOfBall = 10;
		for(int i = 0; i<numberOfBall; i++) {
			double size = 40.0+(Math.random()*20);
			double posX = ((Math.random()*640));
			double posY = ((Math.random()*400));
			int dir = rnd.nextInt(3);
			Directions dire;
			if(dir == 1) {
				dire = Directions.BACKWARD;
			}else if(dir == 2) {
				dire = Directions.FORWARD;
			}else if(dir == 3) {
				dire = Directions.UPWARD;
			}else {
				dire = Directions.DOWNWARD;
			}
			int id = i;
			long sleepTime = 30 + (long)(Math.random()*10);
			Ball b = new Ball(dire, posX, posY, size, id, sleepTime);
			bll.add(b);
			MovingThread mt = new MovingThread(b, this);
			mts.add(mt);
			mt.setDaemon(true);
			mt.start();
			GUIBall gb = new GUIBall(b);
			shapes.add(gb);
		}
		for(GUIBall g : shapes) {
			boardPane.getChildren().add(g.getBody());
		}
	}
	
	
	public void startGame(ArrayList<Ball> bl) {
		finishGame();
		runningGame = true;
		bll = bl;
		for(Ball b : bll) {
			MovingThread mt = new MovingThread(b, this);
			mts.add(mt);
			mt.setDaemon(true);
			mt.start();
			GUIBall gb = new GUIBall(b);
			shapes.add(gb);
		}
		for(int i = 0; i<shapes.size(); i++) {
			if(!bll.get(i).isCaught()) {
			boardPane.getChildren().add(shapes.get(i).getBody());
			}
		}
	}
	
	
	@FXML
	void saveGame(ActionEvent e) {
		try {
			sfm.saveGame();
			runningGame = false;
			finishGame();
		}catch(IOException ex) {
			Alert t = new Alert(AlertType.ERROR, ex.getLocalizedMessage());
			t.setContentText("An error ocurred during the saving of the game.");
			t.show();
		}
	}
	
	
	@FXML
	void loadGame(ActionEvent e) {
		try {
			runningGame = true;
			sfm.loadGame();
		}catch(IOException ex) {
			Alert t = new Alert(AlertType.ERROR, ex.getLocalizedMessage());
			t.setContentText("An error ocurred during the loading of the game.");
			t.show();
		}
	}
	
	
	@FXML
	void showScores(ActionEvent e) {
		runningGame = false;
			try {
				String hof = sfm.getHOF();
				Stage scoresWindow = new Stage();
				VBox content = new VBox();
				content.getChildren().add(new Text(hof));
				content.setPadding(new Insets(14, 14, 14, 14));
				Scene scoreSc = new Scene(content, 200,200);
				scoresWindow.setScene(scoreSc);
				scoresWindow.setTitle("Hall of Fame");
				scoresWindow.show();
			}catch(IndexOutOfBoundsException ex) {
				Alert t = new Alert(AlertType.ERROR, "asdasd");
				t.setContentText("There are no registered scores.");
				t.show();	
			}
	}
	
	
	public void updateShapes() {
		for(GUIBall g : shapes) {
			g.move();
		}
	}
	
	
	public double getBoardHeight() {
		return boardPane.getHeight();
	}
	
	
	public double getBoardWidth() {
		return boardPane.getWidth();
	}
	
	
	public List<Ball> getBll(){
		return bll;
	}
	
	
	public void setPacmen(ArrayList<Ball> bl) {
		bll = bl;
	}
	
	
	public void checkCrash(Ball ball) {
		for(int i = 0; i<bll.size(); i++) {
			Ball b = bll.get(i);
			if(!ball.isCaught() && !b.isCaught()) {
				double distance= Math.sqrt((b.getX() - ball.getX())*(b.getX() - ball.getX())+(b.getY() - ball.getY())*(b.getY() - ball.getY()));
				if (distance < (b.getSize()/2)+(ball.getSize()/2)) {
					b.changeDirection();
					ball.changeDirection();
					b.addBounce();
				}
			}
		}
	}
	
	
	public boolean checkFinished() {
		boolean finished = false;
		int counter = 0;
		for(Ball b : bll) {
			if(b.isCaught()) {
				counter++;
			}
		}
		if(counter == bll.size()) {
			finished = true;
			finishGame();
		}
		return finished;
	}
	
	
	public void finishGame() {
		boardPane.getChildren().clear();
		bll.clear();
		shapes.clear();
		informationLabel.setText("Catch the Ball!");
		for(MovingThread mt : mts) {
			mt.deactivate();
		}
	}
	
	public void registerScore() {
		runningGame = false;
		int totalBounces = 0;
		for(Ball b : bll) {
			totalBounces += b.getBounces();
		}
		if(sfm.mayBeAdded(totalBounces)) {
			try{
				TextInputDialog tid = new TextInputDialog("Your name here");
				tid.setContentText("Please type your name to add you in the Hall of Fame.");
				tid.show();
				String name = tid.getEditor().getText();
				sfm.addScore(new Score(totalBounces, name));
			}catch(IOException ex) {
				Alert t = new Alert(AlertType.ERROR, ex.getLocalizedMessage());
				t.setContentText("Error: There are some missing files.");
				t.show();
			}
		}
	}
	
	public boolean isRunning() {
		return runningGame;
	}
	
}
