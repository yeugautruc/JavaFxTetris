package de.proglabor.aufgabe6;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainTetris {
	public static void main(String[] in) throws ArrayIndexOutOfBoundsException {
		try {
			try {
				checkInput(in);
			} catch (UnknownParameterException e) {
				e.printStackTrace();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			UnknownParameterException e1 = new UnknownParameterException("null");
			e1.printStackTrace();
		}

	}

	public static void checkInput(String[] in) throws UnknownParameterException {
		if (in[0].equals("-gui") || in[0].equals("-help") || in[0].equals("-load")) {
			switch (in[0]) {
			case "-gui":
				new Thread() {
					@Override
					public void run() {
						javafx.application.Application.launch(GUI.class);
					}
				}.start();
				break;
			case "-load":
				Console.main(in);
				break;
			case "-help":
				BufferedReader reader = null;
				String line = "ok";
				try {
					reader = new BufferedReader(new FileReader("readmeTetris.txt"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				try {
					while (line != null) {
						line = reader.readLine();
						if (line != null) {
							System.out.println(line);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
		} else {
			throw new UnknownParameterException(in[0].toString());
		}
	}
}
