package net.collaud.fablab.door.io.piface;

import com.pi4j.component.switches.SwitchListener;
import com.pi4j.component.switches.SwitchState;
import com.pi4j.component.switches.SwitchStateChangeEvent;
import com.pi4j.device.piface.PiFace;
import com.pi4j.device.piface.PiFaceSwitch;
import java.util.Optional;
import net.collaud.fablab.door.io.DoorInputListener;
import org.apache.log4j.Logger;

/**
 *
 * @author Gaetan Collaud <gaetancollaud@gmail.com>
 */
abstract public class PiFaceInput {

	private static final Logger LOG = Logger.getLogger(PiFaceInput.class);

	static public void addListener(DoorInputListener listener) {
		Optional<PiFace> opt = PiFaceFactory.getPiface();
		if (opt.isPresent()) {
			PiFace pf = opt.get();
			pf.getSwitch(PiFaceSwitch.S1).addListener((SwitchListener) (SwitchStateChangeEvent event) -> {
				if (event.getNewState() == SwitchState.ON) {
					listener.buttonOpenDoorShortlyPressed();
				}
			});
			pf.getSwitch(PiFaceSwitch.S2).addListener((SwitchListener) (SwitchStateChangeEvent event) -> {
				if (event.getNewState() == SwitchState.ON) {
					listener.buttonOpenDoorPressed();
				}
			});
			pf.getSwitch(PiFaceSwitch.S3).addListener((SwitchListener) (SwitchStateChangeEvent event) -> {
				if (event.getNewState() == SwitchState.ON) {
					listener.buttonCloseDoorPressed();
				}
			});
			pf.getSwitch(PiFaceSwitch.S4).addListener((SwitchListener) (SwitchStateChangeEvent event) -> {
				if (event.getNewState() == SwitchState.ON) {
					listener.buttonExitPressed();
				}
			});
			LOG.debug("Piface input listener registered");
		}else{
			LOG.error("Piface not present, cannot register input listener");
		}
	}

}
