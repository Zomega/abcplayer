package player;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Main entry point of your application.
 * 
 * @version alpha
 */
public class Main {

	/**
	 * Open a file and return it's contents.
	 * 
	 * @param pathname
	 *            the path to the file.
	 * @return The contents of the file as a String.
	 * @throws IOException
	 *             if something goes horribly wrong.
	 */
	private static String readFile(String pathname) throws IOException {

		File file = new File(pathname);
		StringBuilder fileContents = new StringBuilder((int) file.length());
		Scanner scanner = new Scanner(file);
		String lineSeparator = System.getProperty("line.separator");

		try {
			while (scanner.hasNextLine()) {
				fileContents.append(scanner.nextLine() + lineSeparator);
			}
			return fileContents.toString();
		} finally {
			scanner.close();
		}
	}

	/**
	 * Plays the input file using Java MIDI API and displays header information
	 * to the standard output stream.
	 * 
	 * (Your code should not exit the application abnormally using
	 * System.exit().)
	 * 
	 * @param file
	 *            the name of input abc file
	 * @throws Exception
	 */
	public static void play(String file) throws Exception {
		new Player(Main.readFile(file)).play();
	}

	/**
	 * Play a few random abc files.
	 */
	public static void main(String[] args) throws Exception {
		Main.play("sample_abc/scale.abc");

		// CALL play() HERE
	}

}
