package de.proglabor.aufgabe6;

import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GameDataHandle {
	public String dateiName;

	public GameDataHandle(String dateiName) {
		this.dateiName = dateiName;
	}

	public void saveStringToCsv(String inputString) {
		try {
			try {
				File txtFile = new File(dateiName);
				if (txtFile.exists() && txtFile.isFile()) {
					txtFile.delete();
				}
				txtFile.createNewFile();
			} catch (IOException e) {
			}
			BufferedWriter output = new BufferedWriter(new FileWriter(dateiName, true));
			output.append(inputString);
			output.close();
		} catch (IOException ex1) {
		}
		System.out.println("Game saved!");
	}

	public List<String> loadStringFromCsv() throws IOException {
		List<String> csvString = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader(dateiName));
		String line = reader.readLine();
		while (line != null) {
			try {
				while (line != null) {
					csvString.add(line);
					line = reader.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		reader.close();
		return csvString;
	}

	public GameDataHandle getGameHandler() {
		return this;
	}
}
