package threads;

import model.*;
import ui.*;

public class MovingThread extends Thread {
	
	private Ball ball;
	
	private Controller gui;
	
	private boolean active;
	
	public MovingThread(Ball bl, Controller gui) {
		ball = bl;
		this.gui = gui;
		active = true;
	}
	
	private void move() {
		double maxX = gui.getBoardWidth();
		double maxY = gui.getBoardHeight();
		ball.move(maxX, maxY);
	}
	
	public void deactivate() {
		active = false;
	}
	
	public void run() {
		while(active) {
			try{
				gui.checkCrash(ball);
				move();
				Thread.sleep(ball.getSleep());
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}