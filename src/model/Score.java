package model;

import java.io.Serializable;

public class Score implements Serializable{
	
	private static final long serialVersionUID = 1L;

	
	private int bounces;
	private String name;
	
	public Score(int b, String n) {
		bounces = b;
		name = n;
	}
	
	public int getBounces() {
		return bounces;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name + "\t" + bounces;
	}
}
