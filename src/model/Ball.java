package model;

public class Ball {

	private int id;
	private Directions direction;
	private double size;
	private double positionX;
	private double positionY;
	private int bounces;
	private boolean caught;
	private long sleepTime;
	
	
	public Ball(Directions dir, double x, double y, double s, int id, long st) {
		this.id = id;
		direction = dir;
		size = s;
		positionX = x;
		positionY = y;
		bounces = 0;
		caught = false;
		sleepTime = st;
	}
	
	public Ball(String[] parts) {
		positionX = Double.parseDouble(parts[0]);
		positionY = Double.parseDouble(parts[1]);
		direction = Directions.valueOf(parts[2]);
		sleepTime = Long.parseLong(parts[3]);
		size = Double.parseDouble(parts[4]);
		id = Integer.parseInt(parts[5]);
		bounces = Integer.parseInt(parts[6]);
		caught = Boolean.parseBoolean(parts[7]);
		
	}
	
	public void move(double maxX, double maxY) {
		double advance = 10;
		double radius = size/2;
		switch(direction) {
			case DOWNWARD:
				if(positionY+advance+radius>maxY) {
					direction = Directions.UPWARD;
					positionY = maxY-radius;
				}else {
					positionY = positionY+advance;					
				}
				break;
			case FORWARD:
				if(positionX+advance+radius>maxX) {
					direction = Directions.BACKWARD;
					positionX = maxX-radius;
				}else {
					positionX = positionX+advance;					
				}
				break;
			case UPWARD:
				if(positionY-advance-radius<0) {
					direction = Directions.DOWNWARD;
					positionY = radius;
				}else {
					positionY = positionY-advance;			
				}
				break;
			case BACKWARD:
				if(positionX-advance-radius<0) {
					direction = Directions.FORWARD;
					positionX = radius;
				}else {
					positionX = positionX-advance;			
				}
				break;
			}
		}
	
	
	public double getX() {
		return positionX;
	}
	
	public double getY() {
		return positionY;
	}
	
	public double getSize() {
		return size;
	}
	
	public void catchIt() {
		caught = true;
	}
	
	public boolean isCaught() {
		return caught;
	}
	
	public void addBounce() {
		bounces++;
	}
	
	public int getBounces() {
		return bounces;
	}
	
	public int getId() {
		return id;
	}
	
	public long getSleep() {
		return sleepTime;
	}
	
	public Directions getDirection() {
		return direction;
	}
	
	public void changeDirection() {
		switch(direction) {
		case DOWNWARD:
			direction=Directions.UPWARD;
			break;
		case UPWARD:
			direction=Directions.DOWNWARD;
			break;
		case FORWARD:
			direction=Directions.BACKWARD;
			break;
		case BACKWARD:
			direction=Directions.FORWARD;
			break;
		}
	}
}
