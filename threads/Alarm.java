package nachos.threads;
import java.util.PriorityQueue;
import nachos.machine.*;

/**
 * Uses the hardware timer to provide preemption, and to allow threads to sleep
 * until a certain time.
 */
public class Alarm {
    /**
     * Allocate a new Alarm. Set the machine's timer interrupt handler to this
     * alarm's callback.
     *
     * <p><b>Note</b>: Nachos will not function correctly with more than one
     * alarm.
     */
    public Alarm() {
        Machine.timer().setInterruptHandler(new Runnable() {
            public void run() { timerInterrupt(); }
	    });
    }

    /**
     * The timer interrupt handler. This is called by the machine's timer
     * periodically (approximately every 500 clock ticks). Causes the current
     * thread to yield, forcing a context switch if there is another thread
     * that should be run.
     */
    public void timerInterrupt() {
        long currentTime = Machine.timer().getTime();
        //System.out.println(currentTime);
        Machine.interrupt().disable();
        while (!waitQueue.isEmpty() && waitQueue.peek().wakeTime <= currentTime){
            Waiting threadTime = waitQueue.poll();
            KThread thread = threadTime.thread;
            if (thread != null){
                //System.out.println(currentTime);
                //System.out.println(Machine.timer().getTime());
                thread.ready();

            }
        }
        Machine.interrupt().enable();
        KThread.yield();
    }

    /**
     * Put the current thread to sleep for at least <i>x</i> ticks,
     * waking it up in the timer interrupt handler. The thread must be
     * woken up (placed in the scheduler ready set) during the first timer
     * interrupt where
     *
     * <p><blockquote>
     * (current time) >= (WaitUntil called time)+(x)
     * </blockquote>
     *
     * @param	x	the minimum number of clock ticks to wait.
     *
     * @see	nachos.machine.Timer#getTime()
     */
    public void waitUntil(long x) {

        Machine.interrupt().disable();
        //tiempo en el que entra el thread mas x segundos
        long wakeTime = Machine.timer().getTime() + x;
        waitQueue.add(new Waiting(KThread.currentThread(), wakeTime));
        //System.out.println(wakeTime);
       // System.out.println(Machine.timer().getTime());
        KThread.sleep();
        Machine.interrupt().enable();
    }


    // Clase para tiempo exacto del thread
    // Comparable interface. nos va servir para comparar los disintos tiempos del thread de manera ordenada
    private class Waiting implements Comparable<Waiting> {
        //Variables de la clase el thread y el tiempo
        private KThread thread;
        private long wakeTime;
        Waiting(KThread thread, long wakeTime) {
            this.thread = thread;
            this.wakeTime = wakeTime;
        }
        //Obtener el tiempo por medio del compareTo que se usa para comparar dos cadenas
        public int compareTo(Waiting that) {
            //Tiempo actual
            //Long.signum si es menor devulve -1, si es mayor devuelve 1, si es igual 0
            return Long.signum(wakeTime - that.wakeTime);
            // nos sirve para que la priority sepa donde meter al thread
        }
    }
    //priority Queue
    private PriorityQueue<Waiting> waitQueue = new PriorityQueue<Waiting>();

}