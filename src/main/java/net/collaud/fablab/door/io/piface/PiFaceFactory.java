package net.collaud.fablab.door.io.piface;

import com.pi4j.device.piface.PiFace;
import com.pi4j.device.piface.impl.PiFaceDevice;
import com.pi4j.wiringpi.Spi;
import java.io.IOException;
import java.util.Optional;
import org.apache.log4j.Logger;

/**
 *
 * @author Gaetan Collaud <gaetancollaud@gmail.com>
 */
abstract public class PiFaceFactory {
	private static final Logger LOG = Logger.getLogger(PiFaceFactory.class);
	
	private static Optional<PiFace> piface;

	public static Optional<PiFace> getPiface() {
		if(piface==null) createPiFace();
		return piface;
	}
	
	protected static void createPiFace(){
		try {
			piface = Optional.of(new PiFaceDevice(PiFace.DEFAULT_ADDRESS, Spi.CHANNEL_0));
		} catch (IOException ex) {
			LOG.error("Cannot instanciate piface !", ex);
		}
	}
}
