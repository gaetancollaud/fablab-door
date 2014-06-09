package net.collaud.fablab.door.io;

import com.pi4j.component.switches.SwitchListener;
import com.pi4j.component.switches.SwitchState;
import com.pi4j.component.switches.SwitchStateChangeEvent;
import com.pi4j.device.piface.PiFace;
import com.pi4j.device.piface.PiFaceSwitch;
import com.pi4j.device.piface.impl.PiFaceDevice;
import com.pi4j.wiringpi.Spi;
import java.io.IOException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import net.collaud.fablab.common.file.FileHelper;
import net.collaud.fablab.common.ws.exception.WebServiceException;
import net.collaud.fablab.door.file.ConfigFileHelper;
import net.collaud.fablab.door.file.FileHelperFactory;
import net.collaud.fablab.door.ws.client.DoorClient;
import org.apache.log4j.Logger;

/**
 *
 * @author gaetan
 */
public class IOManager {

	public static final int IPX_ON_OFF_DELAY = 1000;//in ms
	public static final int ALARM_ON_OFF_DELAY = 200;//in ms
	public static final int ALARM_PIN_ON = 0;
	public static final int ALARM_PIN_OFF = 1;
	public static final int DOOR_PIN_OPEN = 2;
	public static final int STATUS_PIN_DOOR = 6;
	public static final int STATUS_PIN_ALARM = 7;

	private static final Logger LOG = Logger.getLogger(IOManager.class);

	private static IOManager instance;

	private DoorClient doorWs;

	public static final IOManager getInstance() {
		if (instance == null) {
			instance = new IOManager();
		}
		return instance;
	}

	private boolean alarmOn = false;
	private boolean doorOpen = true;

	private final IPX800 ipx800;
	private final Optional<PiFace> piface;

	private final Timer timerFast;
	private final Timer timerWebService;
	private Optional<TimerTask> currentTask;

	private IOManager() {
		FileHelper<ConfigFileHelper> config = FileHelperFactory.getConfig();
		ipx800 = new IPX800(config.get(ConfigFileHelper.IPX800_ADDR), config.getAsInt(ConfigFileHelper.IPX800_PORT));
		PiFace pf = null;
		try {
			pf = new PiFaceDevice(PiFace.DEFAULT_ADDRESS, Spi.CHANNEL_0);
			addButtonListeners(pf);
		} catch (IOException ex) {
			LOG.error("Cannot instanciate piface !", ex);
		}

		doorWs = new DoorClient();

		piface = Optional.ofNullable(pf);
		
		timerFast = new Timer();
		timerFast.schedule(new LedStateTask(), 0, 100);
		
		timerWebService = new Timer();
		timerWebService.schedule(new WebserviceStateTask(), 0, 1000);

	}

	private void addButtonListeners(PiFace pf) {
		pf.getSwitch(PiFaceSwitch.S1).addListener((SwitchListener) (SwitchStateChangeEvent event) -> {
			if (event.getNewState() == SwitchState.ON) {
				LOG.info("Button rfid pressed");
				openDoorShortly();
			}
		});
		pf.getSwitch(PiFaceSwitch.S2).addListener((SwitchListener) (SwitchStateChangeEvent event) -> {
			if (event.getNewState() == SwitchState.ON) {
				LOG.info("Button open door pressed");
				openDoorPermanently();
			}
		});
		pf.getSwitch(PiFaceSwitch.S3).addListener((SwitchListener) (SwitchStateChangeEvent event) -> {
			if (event.getNewState() == SwitchState.ON) {
				LOG.info("Button close door pressed");
				closeDoorPermanenlty();
			}
		});
		pf.getSwitch(PiFaceSwitch.S4).addListener((SwitchListener) (SwitchStateChangeEvent event) -> {
			if (event.getNewState() == SwitchState.ON) {
				LOG.info("Button exit pressed");
				closeDoorAndActivateAlarm();
			}
		});
	}

	synchronized public boolean isAlarmOn() {
		return alarmOn;
	}

	synchronized public boolean isDoorOpen() {
		return doorOpen;
	}

	synchronized public void openDoorShortly() {
		alarmOff();
		if (doorOpen()) {
			//door is now open, close it after the delay
			createOpenDoorShortlyTask();
		}
	}

	synchronized public void openDoorPermanently() {
		alarmOff();
		doorOpen();
	}

	synchronized public void closeDoorPermanenlty() {
		alarmOff();
		doorClose();
	}

	synchronized public void closeDoorAndActivateAlarm() {
		doorClose();
		alarmOn();
	}

	private void alarmOn() {
		if (!alarmOn) {
			timerFast.schedule(new OnOffWithDelay(() -> {
				piface.ifPresent(pf -> pf.getOutputPin(ALARM_PIN_ON).high());
			}, ALARM_ON_OFF_DELAY, () -> {
				piface.ifPresent(pf -> pf.getOutputPin(ALARM_PIN_ON).low());
			}), 0);
			alarmOn = true;
		}
	}

	private void alarmOff() {
		if (alarmOn) {
			timerFast.schedule(new OnOffWithDelay(() -> {
				piface.ifPresent(pf -> pf.getOutputPin(ALARM_PIN_OFF).high());
			}, ALARM_ON_OFF_DELAY, () -> {
				piface.ifPresent(pf -> pf.getOutputPin(ALARM_PIN_OFF).low());
			}), 0);
			alarmOn = false;
		}

	}

	private boolean doorOpen() {
		if (!doorOpen) {
			ipx800.setRelay(RelayManager.Relay.RELAY_DOOR, true);
			piface.ifPresent(pf -> pf.getOutputPin(DOOR_PIN_OPEN).low());
			doorOpen = true;
			return true;
		}
		return false;
	}

	private boolean doorClose() {
		if (doorOpen) {
			ipx800.setRelay(RelayManager.Relay.RELAY_DOOR, false);
			piface.ifPresent(pf -> pf.getOutputPin(DOOR_PIN_OPEN).high());
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

			piface.ifPresent(pf -> {
				if (alarmOn && lastState) {
					pf.getOutputPin(STATUS_PIN_ALARM).high();
				} else {
					pf.getOutputPin(STATUS_PIN_ALARM).low();
				}

				if (doorOpen) {
					pf.getOutputPin(STATUS_PIN_DOOR).low();
				} else {
					pf.getOutputPin(STATUS_PIN_DOOR).high();
				}
			});
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
