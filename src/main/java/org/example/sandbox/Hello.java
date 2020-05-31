package org.example.sandbox;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Hello {

	public static void main(String[] args) {
		String currentDirectory = System.getProperty("user.dir");
		System.out.println("hello, I am running from "+currentDirectory);

		// Try to load some data into a list
		List<String> moodList = new ArrayList<>();
		File f = new File(currentDirectory, "src/main/resources/lists/moods.txt");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line = reader.readLine().trim();
			while (line != null) {
				// Only add the line to the list if it is not empty
				if (line.length() > 0) {
					moodList.add(line);
				}
				line = reader.readLine();
			}
		} catch (FileNotFoundException fnfe) {
			System.err.println("the file "+f.getAbsolutePath()+" does not exist: "+fnfe.getMessage());
		} catch (IOException ioe) {
			System.err.println("error reading "+f.getAbsolutePath()+": "+ioe.getMessage());
		}

		if (moodList.size() == 0) {
			System.err.println("No lines were read from the file");
			return;
		} else {
			System.out.println("The mood list has "+moodList.size()+" items");
		}

		// Make a random number generator so we can pick a mood
		Random rnd = new Random();
		// Get a random index to choose a mood from the list.
		// The nextInt() function takes an upper bound and chooses
		// a number between zero (inclusive) and that number (exclusive).
		int index = rnd.nextInt(moodList.size());
		String mood = moodList.get(index);
		System.out.println("My current mood is "+mood);
	}

}
