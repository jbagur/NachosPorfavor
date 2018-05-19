package nachos.threads;

import nachos.machine.*;

/**
 * A <i>communicator</i> allows threads to synchronously exchange 32-bit
 * messages. Multiple threads can be waiting to <i>speak</i>,
 * and multiple threads can be waiting to <i>listen</i>. But there should never
 * be a time when both a speaker and a listener are waiting, because the two
 * threads can be paired off at this point.
 */
public class Communicator {
	/**
	 * Allocate a new communicator.
	 */
	public Communicator() {
		communicationLock = new Lock();
		currentSpeaker = new Condition2(communicationLock);
		currentListener = new Condition2(communicationLock);
		wordReady = false;
	}

	/**
	 * Wait for a thread to listen through this communicator, and then transfer
	 * <i>word</i> to the listener.
	 *
	 * <p>
	 * Does not return until this thread is paired up with a listening thread.
	 * Exactly one listener should receive <i>word</i>.
	 *
	 * @param	word	the integer to transfer.
	 */
	public void speak(int word) {
		System.out.println("Speaker initializing: " + KThread.currentThread().getName());
		communicationLock.acquire();
		speaker += 1;
		while (listener < 1 || wordReady){
			//System.out.println("No word or no listeners, speaker going to sleep");
			this.currentSpeaker.sleep();
		}
		//System.out.println("Listener found or word ready");
		this.msg = word;
		wordReady = true;
		//System.out.println("Word incoming, Waking up listener");
		currentListener.wake();
		speaker -= 1;
		//System.out.println("Speaker has finished");
		communicationLock.release();
	}

	/**
	 * Wait for a thread to speak through this communicator, and then return
	 * the <i>word</i> that thread passed to <tt>speak()</tt>.
	 *
	 * @return	the integer transferred.
	 */
	public int listen() {
		int word;
		communicationLock.acquire();
		listener += 1;
		System.out.println("Listener initializing: "+ KThread.currentThread().getName());
		while (wordReady == false){
			//System.out.println("The word is not ready going to send the listener to sleep and waking speaker");
			currentSpeaker.wake();
			this.currentListener.sleep();
		}
		word = this.msg;
		wordReady = false;
		listener -= 1;
		currentSpeaker.wake();
		communicationLock.release();
		return word;
	}

	private Lock communicationLock;
	private Condition2 currentSpeaker;
	private Condition2 currentListener;
	private int speaker = 0;
	private int listener = 0;
	private int msg;
	private boolean wordReady;


// Test un Speaker - un Listener

	public static void selfTest1(){
		final Communicator com = new Communicator();
		KThread thread1 = new KThread(new Runnable() {
			public void run() {
				System.out.println("Thread 1 -- Start/Speaking");
				com.speak(0);
				System.out.println("Thread 1 -- Said 0");
				System.out.println("Thread 1 -- Finish/Speaking");
			}
		});

		KThread thread2 = new KThread(new Runnable() {
			public void run() {
				System.out.println("Thread 2 -- Start/Listening");
				System.out.println("Thread 2 -- heard "+ com.listen());
				System.out.println("Thread 2 -- Finish/Listening");
			}
		});

		//Primero el speaker
		//thread1.fork();
		//thread1.join();
		//thread2.fork();
		//Primero el listener
		//thread2.fork();
		//thread1.fork();

	}


// muchos speaker muchos Listener


	public static void selfTest2(){

		final Communicator com = new Communicator();

		Runnable spw = new Runnable(){
			public void run() {
				System.out.println(KThread.currentThread().getName() + " -- Start/Speaking");
				com.speak(0);
				System.out.println(KThread.currentThread().getName() +" -- Said 0");
				System.out.println(KThread.currentThread().getName() +" -- Finish/Speaking");
			}
		};


		Runnable lsw = new Runnable(){
			public void run() {
				System.out.println(KThread.currentThread().getName() +"-- Start/Listening");
				System.out.println(KThread.currentThread().getName() +" -- heard "+ com.listen());
				System.out.println(KThread.currentThread().getName() +" -- Finish/Listening");
			}
		};

		KThread speak1 = new KThread(spw);
		KThread speak2 = new KThread(spw);
		KThread speak3 = new KThread(spw);

		KThread listen1 = new KThread(lsw);
		KThread listen2 = new KThread(lsw);
		KThread listen3 = new KThread(lsw);

		speak1.setName("SP1").fork();
		speak2.setName("SP2").fork();
		speak3.setName("SP3").fork();
		listen1.setName("LT1").fork();
		listen2.setName("LT2").fork();
		listen3.setName("LT3").fork();

	}


	//Test un Speaker muchos listeners

	public static void selfTest3() {

		final Communicator com = new Communicator();

		Runnable spw = new Runnable(){
			public void run() {

				for(int i = 0; i < 3; i++) {
					System.out.println(KThread.currentThread().getName() + " -- Start/Speaking");
					com.speak(i);
					System.out.println(KThread.currentThread().getName() +" -- Said"+i);
					System.out.println(KThread.currentThread().getName() +" -- Finish/Speaking");
				}
			}
		};

		Runnable lsw = new Runnable(){
			public void run() {
				System.out.println(KThread.currentThread().getName() +"-- Start/Listening");
				System.out.println(KThread.currentThread().getName() +" -- heard "+ com.listen());
				System.out.println(KThread.currentThread().getName() +" -- Finish/Listening");
			}
		};


		KThread speak1 = new KThread(spw);

		KThread listen1 = new KThread(lsw);
		KThread listen2 = new KThread(lsw);
		KThread listen3 = new KThread(lsw);


		speak1.setName("SP1").fork();
		listen1.setName("LT1").fork();
		listen2.setName("LT2").fork();
		listen3.setName("LT3").fork();

		speak1.join();
		listen1.join();
		listen2.join();
		listen3.join();

	}


}