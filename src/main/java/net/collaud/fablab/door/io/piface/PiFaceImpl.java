package net.collaud.fablab.door.io.piface;

import com.pi4j.device.piface.PiFace;
import java.util.Optional;
import net.collaud.fablab.door.io.IOSystem;

/**
 *
 * @author Gaetan Collaud <gaetancollaud@gmail.com>
 */
public class PiFaceImpl extends IOSystem{
	
	public static final int ALARM_PIN_ON = 0;
	public static final int ALARM_PIN_OFF = 1;
	public static final int DOOR_PIN_OPEN = 2;
	public static final int STATUS_PIN_DOOR_OPEN = 5;
	public static final int STATUS_PIN_DOOR_CLOSE = 6;
	public static final int STATUS_PIN_ALARM = 7;
	
	private Optional<PiFace> piface;

	public PiFaceImpl() {
		piface = PiFaceFactory.getPiface();
	}

	@Override
	public void setDoorOpen(boolean open) {
		piface.ifPresent(pf->pf.getOutputPin(DOOR_PIN_OPEN).setState(open));
	}

	@Override
	public void setAlarmOnPressed(boolean pressed) {
		piface.ifPresent(pf->pf.getOutputPin(ALARM_PIN_ON).setState(pressed));
	}

	@Override
	public void setAlarmOffPressed(boolean pressed) {
		piface.ifPresent(pf->pf.getOutputPin(ALARM_PIN_OFF).setState(pressed));
	}

	@Override
	public void setLedAlarmOn(boolean on) {
		piface.ifPresent(pf->pf.getOutputPin(STATUS_PIN_ALARM).setState(on));
	}

	@Override
	public void setLedDoorOpen(boolean on) {
		piface.ifPresent(pf->pf.getOutputPin(STATUS_PIN_DOOR_OPEN).setState(on));
	}

	@Override
	public void setLedDoorClose(boolean on) {
		piface.ifPresent(pf->pf.getOutputPin(STATUS_PIN_DOOR_CLOSE).setState(on));
	}
	
}
