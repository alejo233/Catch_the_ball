package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import ui.Controller;

public class FileManager {

	private static final String SAVE_PATH = "data/savegame.txt";
	private static final String SCORES_PATH = "data/scores.dat";
	
	
	private Controller gui;
	private List<Score> scores;
	
	
	public FileManager(Controller gui) throws IOException, ClassNotFoundException{
		this.gui = gui;
		scores = new ArrayList<Score>();
		loadScores();
	}
	
	
	public void loadGame() throws IOException{
		File f = new File(SAVE_PATH);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = br.readLine();
		ArrayList<Ball> bll = new ArrayList<Ball>();
		String[] parts = null;
		while(line != null) {
			if(line.charAt(0) == '#') {
				line = br.readLine();
			}else {
				parts = line.split("\t");
				Ball b = new Ball(parts);
				bll.add(b);
				line = br.readLine();
			}
		}
		gui.startGame(bll);
		
		br.close();
	}
	
	public void saveGame() throws IOException {
		List<Ball> bll = gui.getBll();
		String save = "#Ball\n";
		save += "#X\tY\tDIRECTION\tSLEEP\tSIZE\tID\tBOUNCES\tCAUGHT\n";
		for(Ball b : bll) {
			save += b.getX() + "\t" + b.getY() + "\t" + b.getDirection().toString() + "\t" + b.getSleep() + "\t" + b.getSize() + "\t" + b.getId() + "\t" + b.getBounces() + "\t" + b.isCaught() + "\t" + "\n";
		}
		PrintWriter pw = new PrintWriter(new File(SAVE_PATH));
		pw.write(save);
		pw.close();
	}
	
	public void saveScores() throws IOException{
		File f = new File(SCORES_PATH);
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
		oos.writeObject(scores);
		oos.close();
	}
	
	@SuppressWarnings("unchecked")
	public void loadScores() throws IOException, ClassNotFoundException{
		File f = new File(SCORES_PATH);
		if(f.exists()) {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
			scores = (ArrayList<Score>)ois.readObject();
			ois.close();
		}
	}
	
	public boolean mayBeAdded(int c) {
		boolean added = false;	
		if(!scores.isEmpty() && scores.size()<10) {
			for (int i = 0; i < 10 && !added; i++) {
				if(!added) {
					if(scores.get(i).getBounces() < c) {
						added = true;
					}
				}
			}
		}else {
			added = true;
		}
		return added;
	}
	
	public void addScore(Score c) throws IOException{
		boolean added = false;
		if(scores.isEmpty()) {
			scores.add(c);
			added = true;
		}
		for (int i = 0; i < scores.size() && !added; i++) {
			if(added) {
				if(scores.get(i).getBounces() > c.getBounces()) {
					added = true;
					scores.add(i, c);
				}
			}
		}
		saveScores();
	}
	
	public String getHOF() {
		String msg = "Hall Of Fame:\n";
		if(scores.size() < 10) {
			for(int i = 0; i<scores.size(); i++) {
				msg += scores.get(i).toString() + "\n";
			}
		}else {
			for(int i = 0; i<10; i++) {
				msg += scores.get(i).toString() + "\n";
			}
		}
		return msg;
	}
}
