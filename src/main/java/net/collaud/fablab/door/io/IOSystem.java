package net.collaud.fablab.door.io;

/**
 *
 * @author gaetan
 */
abstract public class IOSystem {
	
	/**
	 * Open or close the gate
	 * @param open 
	 */
	abstract public void setDoorOpen(boolean open);
	
	/**
	 * Emulate the alarm On button
	 * @param pressed 
	 */
	abstract public void setAlarmOnPressed(boolean pressed);
	
	/**
	 * Emulate the alarm Off button
	 * @param pressed 
	 */
	abstract public void setAlarmOffPressed(boolean pressed);
	
	/**
	 * Set the alarm on led (red led)
	 * @param on 
	 */
	abstract public void setLedAlarmOn(boolean on);
	
	/**
	 * Set the door open led (green of green/red led)
	 * @param on 
	 */
	abstract public void setLedDoorOpen(boolean on);
	
	/**
	 * Set the door close led (red of green/red led)
	 * @param on 
	 */
	abstract public void setLedDoorClose(boolean on);
	
}
