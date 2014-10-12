package net.collaud.fablab.door.ws;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import net.collaud.fablab.common.ws.data.DoorAction;
import net.collaud.fablab.common.ws.exception.WebServiceException;
import net.collaud.fablab.door.ws.client.DoorClient;
import org.apache.log4j.Logger;

/**
 *
 * @author gaetan
 */
public class DoorEventNotifier extends Thread {

	private static final Logger LOG = Logger.getLogger(DoorEventNotifier.class);

	protected class DoorEvent {

		protected String rfid;
		protected DoorAction action;

		public DoorEvent(String rfid, DoorAction action) {
			this.rfid = rfid;
			this.action = action;
		}
	}

	private static DoorEventNotifier instance;

	synchronized public static DoorEventNotifier getInstance() {
		if (instance == null) {
			instance = new DoorEventNotifier();
		}
		return instance;
	}

	private final DoorClient doorWs;
	private final BlockingQueue<DoorEvent> queue;

	private DoorEventNotifier() {
		super(DoorEventNotifier.class.getSimpleName());
		doorWs = new DoorClient();
		queue = new LinkedBlockingQueue<>();
		start();
	}

	public void notifyEvent(String rfid, DoorAction action) {
		LOG.info("add event rfid=" + rfid + " action=" + action);
		queue.add(new DoorEvent(rfid, action));
	}

	@Override
	public void run() {
		while (true) {
			try {
				//Wait, take and remove
				DoorEvent event = queue.take();
				try {
					doorWs.doorEvent(event.rfid, event.action);
				} catch (WebServiceException ex) {
					LOG.error("Cannot send event " + event.rfid, ex);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ex1) {
					}
				}

			} catch (InterruptedException ex) {
				//Nothing to do here
			}

		}
	}

}
