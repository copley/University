package Reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public abstract class Reader<E> {

	private File file;
	protected String content;

	public Reader(File f) {
		file = f;

		StringBuilder b = new StringBuilder();
		// Read the content in the file
		try (BufferedReader in = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
			String line = null;

			while ((line = in.readLine()) != null) {
				b.append(line + "\n");
			}
		} catch (IOException e) {e.printStackTrace();}
		
		content = b.toString();
	}

	/**
	 * Parses the file into list of objects
	 * 
	 * @return List of the parsed objects
	 */
	public abstract E read();
}
