package net.collaud.fablab.door.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author gaetan
 */
public class IPX800 implements RelayManager {

	private static final Logger LOG = Logger.getLogger(IPX800.class);

	protected String addr;
	protected int port;
	protected Socket socket;
	protected BufferedWriter writer;
	protected BufferedReader reader;

	protected IPX800(String addr, int port) {
		this.addr = addr;
		this.port = port;
		connect();
	}

	private void connect() {
		try {
			socket = new Socket(InetAddress.getByName(addr), port);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException ex) {
			LOG.error("Cannot connect to IPX800 at " + addr + ":" + port, ex);
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
	
	protected void writeCmd(String cmd, int port, boolean on){
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
		try {
			writer.write(sb.toString());
			writer.newLine();
			writer.flush();
			if(LOG.isDebugEnabled()){
				LOG.debug("Write cmd "+sb.toString());
			}
		} catch (IOException ex) {
			LOG.error("Cannot write command", ex);
		}
	}

}
