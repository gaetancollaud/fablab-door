package net.collaud.fablab.door.io;

/**
 *
 * @author gaetan
 */
public interface RelayManager {
	
	public enum Relay{
		RELAY_DOOR(1),
		OUTPUT_2(2),
		RELAY_3(3),
		OUTPUT_4(4),
		OUTPUT_5(5),
		OUTPUT_6(6),
		OUTPUT_7(7),
		OUTPUT_8(8);
		
		private final int number;
		
		private Relay(int number){
			this.number = number;
		}
		
		public int getOutputNumber(){
			return number;
		}
		
		
	}
	
	public void setRelay(Relay output, boolean on);
	
	public void setRelayImpulsion(Relay output, boolean on);
}
