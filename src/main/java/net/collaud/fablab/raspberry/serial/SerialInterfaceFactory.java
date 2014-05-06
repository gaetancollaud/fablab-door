package net.collaud.fablab.raspberry.serial;

/**
 *
 * @author gaetan
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
