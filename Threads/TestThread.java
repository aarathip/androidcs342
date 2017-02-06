//Modified version of xample from tutorialspoint.com
class MyThread extends Thread {
    private Thread t;
    private String threadName;
   
    MyThread( String name) {
	threadName = name;
	System.out.println("Creating " +  threadName );
    }
   
    public void run() {
	System.out.println("Running " +  threadName );
	try {
	    for(int i = 4; i > 0; i--) {
		System.out.println("Thread: " + threadName + " at " + i);
		// Let the thread sleep for a while.
		Thread.sleep(60);
	    }
	}catch (InterruptedException e) {
	    System.out.println("Thread " +  threadName + " interrupted.");
	}
	System.out.println("Thread " +  threadName + " exiting.");
    }
   
    public void start () {
	System.out.println("Starting " +  threadName );
	if (t == null) {
	    t = new Thread (this, threadName);
	    t.start ();
	}
    }
}

public class TestThread {

    public static void main(String args[]) {
	MyThread T1 = new MyThread( "Countdown 1");
	T1.start();
      
	MyThread T2 = new MyThread( "Countdown 2");
	T2.start();

	MyThread T3 = new MyThread( "Countdown 3");
        T3.start();

    }   
}