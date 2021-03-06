package de.proglabor.aufgabe6;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighScoreList {
	public List<HighScore> highScoreListe;
	private Comparator<HighScore> compareBy;
	private String file;

	//
	public HighScoreList(String file) throws IOException {
		highScoreListe = new ArrayList<HighScore>();
		compareBy = (HighScore o1, HighScore o2) -> Long.compare(o1.getPoints(), o2.getPoints());
		this.file = file;
//		this.add(new HighScore("", "", 0));
		loadScoreFromFile(file);
	}

	public HighScoreList() {
		highScoreListe = new ArrayList<HighScore>();
		compareBy = (HighScore o1, HighScore o2) -> Long.compare(o1.getPoints(), o2.getPoints());
	}

	//
	public void saveScoreIntoFile(String file) {
		try {
			try {
				File txtFile = new File("textHighScore.csv");
				if (txtFile.exists() && txtFile.isFile()) {
					txtFile.delete();
				}
				txtFile.createNewFile();
			} catch (IOException e) {
			}
			BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
			for (HighScore highScore2 : highScoreListe) {
				output.append(
						"" + highScore2.getTime() + "," + highScore2.getName() + "," + highScore2.getPoints() + "\n");
			}
			output.close();
			System.out.println("size of list after save in file: " + highScoreListe.size());
		} catch (IOException ex1) {
			System.out.printf("ERROR writing score to file: %s\n", ex1);
		}
	}

	public void loadScoreFromFile(String txtFile) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(txtFile));
		String line = reader.readLine();
		while (line != null) // read the score file line by line
		{
			try {
				if (file != null) {
					while (line != null) {
						add(new HighScore(line));
						line = reader.readLine();
					}
				}
			} catch (IOException e) {
				System.out.println("IOException");
			}
		}
		reader.close();
		System.out.println("size of list after load from file is: " + highScoreListe.size());
	}

	public void add(HighScore e) {
		if (highScoreListe.size() < 10) {
			highScoreListe.add(new HighScore(e.getName(), e.getTime(), e.getPoints()));
			highScoreListe.stream().sorted((HighScore a, HighScore b) -> Long.compare(a.getPoints(), b.getPoints()));
//			Collections.sort(highScoreListe, compareBy);
		} else {
			if (e.getPoints() > highScoreListe.get(0).getPoints()) {
				highScoreListe.add(new HighScore(e.getName(), e.getTime(), e.getPoints()));
				Collections.sort(highScoreListe, compareBy);
				highScoreListe.remove(highScoreListe.get(0));
			}
		}

	}

// getter and setter
	public long getHighScore() {
		long point = 0;
		if (highScoreListe.size() != 0) {
			point = highScoreListe.get(highScoreListe.size() - 1).getPoints();
		}
		return point;
	}

	public String getHighScoreName() {
		String out = "no";
		if (highScoreListe.size() != 0) {
			out = highScoreListe.get(highScoreListe.size() - 1).getName();
		}
		return out;
	}

	public String getHighScoreTime() {
		String out = "no";
		if (highScoreListe.size() != 0) {
			out = highScoreListe.get(highScoreListe.size() - 1).getTime();
		}
		return out;
	}

	public static List<HighScore> getHighScoreListe(List<HighScore> highScoreListe) {
		return highScoreListe;
	}

	public List<HighScore> getHighScoreListe() {
		int count = highScoreListe.size();
		for (HighScore highScore : highScoreListe) {
			highScore.setNo(count--);
		}
		return highScoreListe;
	}

	public void setHighScoreListe(List<HighScore> highScoreListe) {
		this.highScoreListe = highScoreListe;
	}
}
