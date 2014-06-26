package net.collaud.fablab.door.io;

/**
 *
 * @author gaetan
 */
public interface DoorInputListener {
	void buttonOpenDoorShortlyPressed();
	void buttonOpenDoorPressed();
	void buttonCloseDoorPressed();
	void buttonExitPressed();
}
