package ui;

import model.*;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;


public class GUIBall {
	
	
	private Circle body;
	
	private Ball ball;

	
	public GUIBall(Ball b) {
		ball = b;
		double x = b.getX();
		double y = b.getY();
		double s = b.getSize();
		body = new Circle();
		body.setCenterX(x+(s/2));
		body.setCenterY(y+(s/2));
		body.setRadius(s/2);
		body.setFill(Color.YELLOW);
		body.setStroke(Color.BLACK);
		body.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				ball.catchIt();
				body.setVisible(false);
			}
		});
		}
	
	public void move() {
		body.setCenterX(ball.getX());
		body.setCenterY(ball.getY());
		}
	
	public Circle getBody() {
		return body;
	}
}
