package net.collaud.fablab.door.io;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import net.collaud.fablab.common.ws.exception.WebServiceException;
import net.collaud.fablab.door.file.ConfigFileHelper;
import net.collaud.fablab.door.file.FileHelperFactory;
import net.collaud.fablab.door.io.ipx800.IPX800;
import net.collaud.fablab.door.io.piface.PiFaceImpl;
import net.collaud.fablab.door.io.piface.PiFaceInput;
import net.collaud.fablab.door.ws.client.DoorClient;
import org.apache.log4j.Logger;

/**
 *
 * @author gaetan
 */
public class IOManager implements DoorInputListener {

	public static final int ALARM_ON_OFF_DELAY = 200;//in ms

	private static final Logger LOG = Logger.getLogger(IOManager.class);

	private static IOManager instance;

	private final DoorClient doorWs;

	public static final IOManager getInstance() {
		if (instance == null) {
			instance = new IOManager();
		}
		return instance;
	}

	private boolean alarmOn = false;
	private boolean doorOpen = true;
	private boolean doorOpenShortly = false;

	private final Timer timerFast;
	private final Timer timerWebService;
	private Optional<TimerTask> currentTask;

	private List<IOSystem> outputSystems;

	private IOManager() {
		LOG.trace("Init IOManager");

		LOG.trace("Init output systems");
		outputSystems = new ArrayList<>();
		outputSystems.add(new PiFaceImpl());
		outputSystems.add(new IPX800());


		LOG.trace("Init Timer");
		timerFast = new Timer();
		timerFast.schedule(new LedStateTask(), 0, 100);

		timerWebService = new Timer();
		timerWebService.schedule(new WebserviceStateTask(), 0, 1000);

		LOG.trace("Init listeners");
		PiFaceInput.addListener(this);
		
		LOG.trace("Init webservice");
		doorWs = new DoorClient();
	}

	@Override
	synchronized public void buttonOpenDoorShortlyPressed() {
		alarmOff();
		if (doorOpen()) {
			//set to true, only if door just open
			doorOpenShortly = true;
			
			//door is now open, close it after the delay
			createOpenDoorShortlyTask();
		}
	}

	@Override
	synchronized public void buttonOpenDoorPressed() {
		doorOpenShortly = false;
		alarmOff();
		doorOpen();
	}

	@Override
	synchronized public void buttonCloseDoorPressed() {
		alarmOff();
		doorClose();
	}

	@Override
	synchronized public void buttonExitPressed() {
		doorClose();
		alarmOn();
	}

	synchronized public boolean isAlarmOn() {
		return alarmOn;
	}

	synchronized public boolean isDoorOpen() {
		return doorOpen;
	}

	private void alarmOn() {
		if (!alarmOn) {
			timerFast.schedule(new OnOffWithDelay(() -> {
				outputSystems.forEach(out -> out.setAlarmOnPressed(true));
			}, ALARM_ON_OFF_DELAY, () -> {
				outputSystems.forEach(out -> out.setAlarmOnPressed(false));
			}), 0);
			alarmOn = true;
		}
	}

	private void alarmOff() {
		if (alarmOn) {
			timerFast.schedule(new OnOffWithDelay(() -> {
				outputSystems.forEach(out -> out.setAlarmOffPressed(true));
			}, ALARM_ON_OFF_DELAY, () -> {
				outputSystems.forEach(out -> out.setAlarmOffPressed(false));
			}), 0);
			alarmOn = false;
		}

	}

	private boolean doorOpen() {
		if (!doorOpen) {
			outputSystems.forEach(out -> out.setDoorOpen(true));

			doorOpen = true;
			return true;
		}
		return false;
	}

	private boolean doorClose() {
		if (doorOpen) {
			outputSystems.forEach(out -> out.setDoorOpen(false));
			doorOpen = false;
			return true;
		}
		return false;
	}

	/**
	 * Create a task to close the door in a few seconds.
	 *
	 * @see Constants for delay
	 */
	private void createOpenDoorShortlyTask() {
		cancelOpenDoorShortlyTask();
		currentTask = Optional.of(new OpenDoorShortlyTask());
		timerFast.schedule(currentTask.get(), FileHelperFactory.getConfig().getAsInt(ConfigFileHelper.OPEN_DOOR_SHORTLY_DELAY));

	}

	/**
	 * Cancel the task, if one was present
	 */
	private void cancelOpenDoorShortlyTask() {
		currentTask.ifPresent(task -> task.cancel());
	}

	private class OpenDoorShortlyTask extends TimerTask {

		@Override
		public void run() {
			doorClose();
		}
	}

	protected interface ExecuteOnOrOff {

		public void run();
	}

	protected class OnOffWithDelay extends TimerTask {

		private final int delayOff;
		private final ExecuteOnOrOff onAction;
		private final ExecuteOnOrOff offAction;

		public OnOffWithDelay(ExecuteOnOrOff onAction, int delayOff, ExecuteOnOrOff offAction) {
			this.onAction = onAction;
			this.offAction = offAction;
			this.delayOff = delayOff;
		}

		@Override
		public void run() {
			try {
				onAction.run();
				Thread.sleep(delayOff);
				offAction.run();
				currentTask = Optional.empty();
			} catch (InterruptedException ex) {
				LOG.warn("OnOffWithDelay interrupted", ex);
			}
		}

	}

	protected class LedStateTask extends TimerTask {

		private boolean lastState = true;

		@Override
		public void run() {

			if (alarmOn && lastState) {
				outputSystems.forEach(out -> out.setLedAlarmOn(true));
			} else {
				outputSystems.forEach(out -> out.setLedAlarmOn(false));
			}

			if (doorOpen) {
				if (doorOpenShortly) {
					outputSystems.forEach(out -> out.setLedDoorOpen(lastState));
				} else {
					outputSystems.forEach(out -> out.setLedDoorOpen(true));
				}
				outputSystems.forEach(out -> out.setLedDoorClose(false));
			} else {
				outputSystems.forEach(out -> {
					out.setLedDoorOpen(false);
					out.setLedDoorClose(true);
				});
			}
			lastState = !lastState;
		}

	}

	protected class WebserviceStateTask extends TimerTask {

		private boolean lastAlarmOn = alarmOn;
		private boolean lastDoorOpen = doorOpen;

		@Override
		public void run() {
			if (lastAlarmOn != alarmOn || lastDoorOpen != doorOpen) {
				lastAlarmOn = alarmOn;
				lastDoorOpen = doorOpen;
				try {
					doorWs.doorStatus(doorOpen, alarmOn, null);
				} catch (WebServiceException ex) {
					LOG.error("Cannot contact main server to indicate door status", ex);
				}
			}
		}
	}

}
