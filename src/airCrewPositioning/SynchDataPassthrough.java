package airCrewPositioning;

public class SynchDataPassthrough {
	private ReceivedDataPacket packet;
	
    // True if receiver should wait
    // False if sender should wait
    private boolean transfer = true;
  
    public synchronized void send(ReceivedDataPacket packet) {
        while (!transfer) {
        	System.out.println("DATA: SEND");
            try { 
                wait();
            } catch (InterruptedException e)  {
                Thread.currentThread().interrupt(); 
            }
        }
        transfer = false;
         
        this.packet = packet;
        notifyAll();
    }
  
    public synchronized ReceivedDataPacket receive() {
        while (transfer) {
        	System.out.println("DATA: RECEIVE");
            try {
                wait();
            } catch (InterruptedException e)  {
                Thread.currentThread().interrupt(); 
            }
        }
        transfer = true;
 
        notifyAll();
        return packet;
    }
}
