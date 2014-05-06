package net.collaud.fablab.raspberry.serial;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.apache.log4j.Logger;

/**
 *
 * @author gaetan
 */
public class SerialRXTX extends SerialInterface{

	private static final Logger LOG = Logger.getLogger(SerialRXTX.class);

	protected BufferedWriter out;
	protected BufferedReader in;
	protected boolean connected = false;
	protected String portName;
	protected ReaderThread reader;

	public SerialRXTX(String portName) {
		this.portName = portName;
		connect();
	}

	private void connect() {
		try {
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
			if (portIdentifier.isCurrentlyOwned()) {
				LOG.error("Port is currently in use");
			} else {
				CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

				if (commPort instanceof SerialPort) {
					SerialPort serialPort = (SerialPort) commPort;
					serialPort.setSerialPortParams(1000000, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

					in = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
					out = new BufferedWriter(new OutputStreamWriter(serialPort.getOutputStream()));

					connected = true;
					reader = new ReaderThread();
					reader.start();

				} else {
					LOG.error("Error: Only serial ports are handled.");
				}
			}
		} catch (NoSuchPortException ex) {
			LOG.error("Cannot find port " + portName);
		} catch (PortInUseException ex) {
			LOG.error("Port already used");
		} catch (UnsupportedCommOperationException | IOException ex) {
			LOG.error("Cannot connect to port", ex);
		}
	}

	protected class ReaderThread extends Thread {

		public ReaderThread() {
			super("Serial reader on " + portName);
		}

		@Override
		public void run() {
			try {
				String line;
				while ((line = in.readLine()) != null) {
					setChanged();
					notifyObservers(line);
				}
			} catch (IOException ex) {
				LOG.error("Error while reading on serial", ex);
			}
		}
	}
}
