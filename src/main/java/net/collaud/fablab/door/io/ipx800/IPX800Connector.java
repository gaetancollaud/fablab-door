package net.collaud.fablab.door.io.ipx800;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Semaphore;
import org.apache.log4j.Logger;

/**
 *
 * @author gaetan
 */
public class IPX800Connector implements IPXRelays {

	private static final Logger LOG = Logger.getLogger(IPX800Connector.class);

	protected String addr;
	protected int port;
	protected Socket socket;
	protected BufferedWriter writer;
	protected BufferedReader reader;
	protected Semaphore semWaitDisconnect;
	protected IPX800Reader readerThread;

	public IPX800Connector(String addr, int port) {
		this.addr = addr;
		this.port = port;
		semWaitDisconnect = new Semaphore(0);
	}

	public boolean connect() {
		try {
			LOG.trace("Trying to connect to "+addr+":"+port);
			socket = new Socket(InetAddress.getByName(addr), port);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			readerThread = new IPX800Reader();
			readerThread.start();
			LOG.debug("Connected to "+addr+":"+port);
			return true;
		} catch (IOException ex) {
			LOG.error("Cannot connect to IPX800 at " + addr + ":" + port+" reason "+ex.getMessage());
			semWaitDisconnect.release(10000000);
			return false;
		}
	}

	@Override
	public void setRelay(Relay output, boolean on) {
		writeCmd("Set", output.getOutputNumber(), on);
	}

	@Override
	public void setRelayImpulsion(Relay output, boolean on) {
		writeCmd("Set", output.getOutputNumber(), on, "p");
	}

	protected void writeCmd(String cmd, int port, boolean on) {
		writeCmd(cmd, port, on, "");
	}

	protected void writeCmd(String cmd, int port, boolean on, String suffix) {
		StringBuilder sb = new StringBuilder(cmd);
		if (port < 10) {
			sb.append("0");
		}
		sb.append(port);
		sb.append(on ? "1" : "0");
		sb.append(suffix);

		if (writer != null) {
			try {
				writer.write(sb.toString());
				writer.newLine();
				writer.flush();
				if (LOG.isDebugEnabled()) {
					LOG.debug("Write cmd " + sb.toString());
				}
			} catch (IOException ex) {
				LOG.error("Cannot write command", ex);
				try {
					reader.close();
					writer.close();
					socket.close();
				} catch (IOException ex1) {
				}
				semWaitDisconnect.release(100000);
			}
		} else {
			LOG.error("IPX800 not connected, cannot send command : " + sb.toString());
		}
	}

	void waitForDisconnect() throws InterruptedException {
		semWaitDisconnect.acquire();
	}

	public class IPX800Reader extends Thread {

		@Override
		public void run() {
			while (true) {
				try {
					String line;
					while ((line = reader.readLine()) != null) {
						LOG.debug("IPX800 sent " + line);
					}
				} catch (IOException ex) {
					LOG.error("Cannot read ipx800", ex);
					break;
				}
			}
			semWaitDisconnect.release(1000000);
		}
	}

}
