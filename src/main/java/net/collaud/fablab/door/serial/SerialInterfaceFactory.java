package net.collaud.fablab.door.serial;

/**
 *
 * @author Gaetan Collaud <gaetancollaud@gmail.com>
 */
abstract public class SerialInterfaceFactory {
	public static SerialInterface getBestInterface(String port){
		if(port.toUpperCase().contains("COM")){
			return new SerialRXTX(port);
		}else if(port.toLowerCase().contains("ttyusb")){
			return new SerialFileReader(port);
		}
		return null;
	}
}
