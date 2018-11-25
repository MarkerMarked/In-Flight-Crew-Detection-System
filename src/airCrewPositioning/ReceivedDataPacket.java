package airCrewPositioning;

public class ReceivedDataPacket {
	public int tagID;
	public double rssi1;
	public double rssi2;
	
    public ReceivedDataPacket(int ID, double r1, double r2) {
    	tagID = ID;
    	rssi1 = r1;
    	rssi2 = r2;
    }
    
    public ReceivedDataPacket() {
    	tagID = 0;
    	rssi1 = 0;
    	rssi2 = 0;
    }
    
}
