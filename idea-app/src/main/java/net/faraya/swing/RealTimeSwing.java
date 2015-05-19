package net.faraya.swing;

import java.util.Map;
import java.util.HashMap;

/**
 * User: Fabrizzio
 * Date: 18-Feb-2006
 * Time: 23:43:12
 * To change this template use File | Settings | File Templates.
 */
public class RealTimeSwing {

   /**
      Use this method for handling events in realtime,
      when the events may be generated more quickly than the
      the handler can complete.  Call this method from the
      appropriate Listener.

      Executes Runnables inside and outside the Swing Thread.
      Returns immediately.
      If this method is called again with the same id after less
      than the specified pause, then the first call will be
      ignored.  The most recent call with a given id
      will not be allowed to start until previous call finishes.
      When previous call finishes, only the most recent call with
      same id will run.  Others will be discarded.

      @param id This string uniquely identifies this task.
      If this method is called again with the same id within
      the specified number of milliseconds, then the first call
      will not be executed.  If this id is already executing,
      then it will wait until either the previous execution
      finishes, or until a later call with the same id.

      @param milliseconds  Wait this long in a separate thread
      before executing Runnables.  You can safely set this to zero.
      Set the time less than the expected time to execute the
      Runnables.  If you set to 0, then a series of calls will
      be executed at least twice.  If you set to greater than 0,
      then the first call may be ignored if followed quickly by
      another call.

      @param worker This Runnable will be executed first outside
      the Swing thread.  Set to null to skip this step.

      @param refresher This Runnable will be executed inside the
      Swing thread after the worker has completed.  Activity
      that uses or changes the state of Swing widgets should be
      included here.   Set to null to skip this step.
   */
  public static void invokeOnce(String id,
                                final long milliseconds,
                                final Runnable worker,
                                final Runnable refresher) {
    synchronized (s_timestamps) {
      if (!s_timestamps.containsKey(id)) {  // call once for each id
        s_timestamps.put(id,new Latest());
      }
    }
    final Latest latest = (Latest)s_timestamps.get(id);
    final long time = System.currentTimeMillis();
    latest.time = time;

    (new Thread("Invoke once "+id) {public void run() {
      if (milliseconds > 0) {
        try {Thread.sleep(milliseconds);}
        catch (InterruptedException e) {return;}
      }
      synchronized (latest.running) { // can't start until previous finishes
        if (latest.time != time) return; // only most recent gets to run
        if (worker != null) worker.run(); // outside Swing thread
        //if (refresher != null) invokeNow(refresher); // inside Swing thread
      }
    }}).start();
  }

  private static Map s_timestamps = new HashMap();

  private static class Latest {
    public long time=0;
    public Object running = new Object();
  }

}
