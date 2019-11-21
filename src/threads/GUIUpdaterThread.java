package threads;

import ui.*;
import javafx.application.*;


public class GUIUpdaterThread extends Thread {
	
	private final static long UPDATE_SLEEP_TIME = 5;
	
	
	private Controller gui;
	
	private boolean active;
	
	public GUIUpdaterThread(Controller gui) {
		this.gui = gui;
		active = true;
	}
	
	public void move() {
		gui.updateShapes();
		if(gui.checkFinished() && gui.isRunning()) {
			gui.registerScore();
			gui.finishGame();
		}
	}
	
	public void run(){
		while(active){
			try {
				Platform.runLater(new Runnable() {
					public void run(){
						move();
					}
				});
				Thread.sleep(UPDATE_SLEEP_TIME);
			}catch(InterruptedException e) {
				e.printStackTrace();
				}
			}
		}

}