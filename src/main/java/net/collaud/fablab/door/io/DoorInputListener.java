package net.collaud.fablab.door.io;

/**
 *
 * @author Gaetan Collaud <gaetancollaud@gmail.com>
 */
public interface DoorInputListener {
	void buttonOpenDoorShortlyPressed();
	void buttonOpenDoorPressed();
	void buttonCloseDoorPressed();
	void buttonExitPressed();
}
