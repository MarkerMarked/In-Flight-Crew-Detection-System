package airCrewPositioning;

public class ReceivedDataPacket {
	public int tagID;
	public double r1;
	public double r2;
	public double r1Ft;
	public double r2Ft;
	public boolean valid = false;
	
    public ReceivedDataPacket(int ID, double rssi1, double rssi2) {
    	tagID = ID;
    	r1 = rssi1;
    	r2 = rssi2;
    	r1Ft = 0;
    	r2Ft = 0;
    }
    
    public ReceivedDataPacket() {
    	tagID = 0;
    	r1 = 0;
    	r2 = 0;
    	r1Ft = 0;
    	r2Ft = 0;
    }
    
    public void set(double rssi1, double rssi2) {
    	r1 = rssi1;
    	r2 = rssi2;
    }
    
}
