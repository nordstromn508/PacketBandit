import javax.swing.*;

public abstract class CaptureThread {

    //Globals
    private Object value;
    private Thread thread;

    //Nested class maintains ref to current thread under sync control
    private static class ThreadVar {

        private Thread thread;
        ThreadVar(Thread t) {thread = t; }
        synchronized Thread get() {return thread;}
        synchronized void clear() {thread = null;}

    }

    private ThreadVar threadVar;

    //accessor methods
    protected synchronized Object getValue() {return value; }
    private synchronized void setValue(Object x) {value = x; }

    //compute value to be returned. Abstract so MUST be implemented.
    public abstract Object construct();

    //called on even dispatching thread (not on the worker thread)
    public void finished() {}
    public void interrupt() {

        Thread t = threadVar.get();

        if(t != null) {
            t.interrupt();
        }

        threadVar.clear();
    }

    //return value created by the constructing thread, null if
    //constructing or current thread have been interrupted
    public Object get() {
        while (true) {
            Thread t = threadVar.get();

            if(t == null) {
                return getValue();
            }

            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); //propagate
                return null;
            }
        }
    }

    //start a thread that will call constructor and exit
    public CaptureThread() {

        final Runnable doFinished = new Runnable() {
            public void run() { finished(); }
        };

        Runnable doConstruct = new Runnable() {
            public void run() {
                try{
                    setValue(construct());
                }
                finally {
                    threadVar.clear();
                }

                SwingUtilities.invokeLater(doFinished);
            }
        };

        Thread t = new Thread(doConstruct);
        threadVar = new ThreadVar(t);
    }

    public void start() {
        Thread t = threadVar.get();

        if(t != null) {
            t.start();
        }
    }
}
