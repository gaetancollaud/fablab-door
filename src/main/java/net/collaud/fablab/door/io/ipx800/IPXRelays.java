package net.collaud.fablab.door.io.ipx800;

/**
 *
 * @author gaetan
 */
public interface IPXRelays {
	
	public enum Relay{
		RELAY_1(1),
		RELAY_2(2),
		RELAY_3(3),
		RELAY_4(4),
		RELAY_5(5),
		RELAY_6(6),
		RELAY_7(7),
		RELAY_8(8);
		
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
