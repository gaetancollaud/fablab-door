package net.collaud.fablab.door.serial;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author Gaetan Collaud <gaetancollaud@gmail.com>
 */
public class SerialFileReader extends SerialInterface {

	private static final Logger LOG = Logger.getLogger(SerialFileReader.class);

	protected BufferedReader in;
	protected String fileNamePrefix;
	protected ReaderThread reader;

	public SerialFileReader(String fileName) {
		this.fileNamePrefix = fileName;
		reader = new ReaderThread();
		reader.start();
	}

	private boolean connect(int portnumber) {
		try {
			in = new BufferedReader(new FileReader(fileNamePrefix+portnumber));
			return true;
		} catch (FileNotFoundException ex) {
			LOG.error("Cannot connect to port " + portnumber, ex);
		}
		return false;
	}

	protected class ReaderThread extends Thread {

		public ReaderThread() {
			super("Serial reader on " + fileNamePrefix);
		}

		@Override
		public void run() {
			while (true) {
				for (int i = 0; i < 4; i++) {
					if (connect(i)) {
						LOG.info("Starting Reader");
						try {
							String line;
							while ((line = in.readLine()) != null) {
								if (LOG.isDebugEnabled()) {
									LOG.debug("Read line : " + line);
								}
								setChanged();
								notifyObservers(line);
							}
							LOG.warn("End of reader");
						} catch (IOException ex) {
							LOG.error("Error while reading on serial", ex);
						}
					}
					try {
						sleep(1000);
					} catch (InterruptedException ex) {
					}
				}
			}
		}
	}
}
