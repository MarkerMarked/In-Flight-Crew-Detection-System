
public class TriangulationAlgorithm 
{
	double xDistanceFt;
	double yDistanceFt;
	
	public static void main(String[] args) throws Exception
	  {
		TriangulationAlgorithm tri = new TriangulationAlgorithm(2); 
	  }
	
	public TriangulationAlgorithm(int numReaders) throws Exception
	{
		 Reader reader1 = new Reader("192.168.1.10", 10001);
		//Reader reader2 = new Reader("192.168.1.11", 10002);
			
			//System.out.println(xDistanceFt + " " + yDistanceFt);
			
			while(true)
			{
				xDistanceFt = Math.abs(xDistance(reader1.rssiValue, 120, 8.0));
				yDistanceFt = Math.abs(yDistance(reader1.rssiValue, 120, 8.0));
				System.out.println(xDistanceFt + " " + yDistanceFt);
				//reader1.flag = 0;
			
			}
			
		
	}
	
	public double xDistance(double rssi1, double rssi2 , double fixedDistance)
	{
		double dist1 = ((rssi1) - 112.58)/1.1873;
		double dist2 = ((rssi2) - 112.58)/1.1873;
		double rssi1Sq = Math.pow(dist1, 2);
		double rssi2Sq = Math.pow(dist2, 2);
		double fixedSq = Math.pow(fixedDistance, 2);
		double angle = Math.acos((fixedSq + rssi2Sq - rssi1Sq)/(2*fixedDistance*dist2))*(180/Math.PI);
		angle = 90 - angle;
		return (Math.sin(angle)*dist2);
	}
	
	public double yDistance(double rssi1, double rssi2, double fixedDistance)
	{
		double dist1 = ((rssi1) - 112.58)/1.1873;
		double dist2 = ((rssi2) - 112.58)/1.1873;
		double rssi1Sq = Math.pow(dist1, 2);
		double rssi2Sq = Math.pow(dist2, 2);
		double fixedSq = Math.pow(fixedDistance, 2);
		double angle = Math.acos((fixedSq + rssi2Sq - rssi1Sq)/(2*fixedDistance*dist2))*(180/Math.PI);
		angle = 90 - angle;
		return (Math.cos(angle)*dist2);
	}

	
}
