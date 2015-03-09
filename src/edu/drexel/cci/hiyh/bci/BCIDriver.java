package edu.drexel.cci.hiyh.bci;

public class BCIDriver {

	public static void main(String[] args) {
		BCICollector b = new BCICollector();
		b.start();
		while(true){
			try {
				b.getSignalWhenTrue();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("signal");
		}
		
	}

}
