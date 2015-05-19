package net.faraya.swing.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: Fabrizzio
 * Date: 07-Apr-2006
 * Time: 22:15:10
 * To change this template use File | Settings | File Templates.
 */
public class MemoryTrackBar extends JComponent implements Runnable {

    private JProgressBar progressBar;
    private JButton btn;
    private Thread thread;

    public MemoryTrackBar(){
     init();
    }

    private void init(){

      progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
      progressBar.setStringPainted(true);
      btn = new JButton( UIImages.getIcon( UIImages.TRASH_16) );
      btn.addMouseListener(new MouseAdapter(){
       public void mousePressed(MouseEvent e) {
          Runtime.getRuntime().gc();
       }
      });
      setLayout(new BorderLayout());
      add(progressBar,BorderLayout.CENTER);
      add(btn,BorderLayout.EAST);
      start();
    }

    private void step(){
        /*
            <td class="multidata" colspan="2">
            <p>
        -      JVM memory size : <%=(Runtime.getRuntime().totalMemory()*10/1048576)/10.0%> Mb (<%=(Runtime.getRuntime().totalMemory()*10/1024)/10.0%> Kb)<br />
        +      JVM memory size : <%=(Runtime.getRuntime().totalMemory()*10/1048576)/10.0%> Mb (<%=(Runtime.getRuntime().totalMemory()*10/1024)/10.0%> Kb)
        +   </p>
        +   <p>
               JVM free memory : <%=(Runtime.getRuntime().freeMemory()*10/1048576)/10.0%> Mb (<%=(Runtime.getRuntime().freeMemory()*10/1024)/10.0%> Kb)
            </p>
        +   <p>
     */
        float freeMem   = (float)((Runtime.getRuntime().freeMemory() *10/1048576)/10.0);
        float totalMem  = (float)((Runtime.getRuntime().totalMemory()*10/1048576)/10.0);
        float used =  totalMem - freeMem;

        StringBuilder sb = new StringBuilder();
        sb.append((int)used).append("M of ").append((int)totalMem).append("M");
        progressBar.setString(sb.toString());
        progressBar.setMaximum((int)totalMem);
        progressBar.setMinimum(0);
        progressBar.setValue((int)used);
    }


    public void start() {
       thread = new Thread(this);
       thread.setPriority(Thread.MIN_PRIORITY);
       thread.setName("MemoryMonitor");
       thread.start();
    }


    public synchronized void stop() {
      thread = null;
      notify();
    }


    public void run() {

    Thread me = Thread.currentThread();

    while (thread == me && !isShowing() || getSize().width == 0) {
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) { thread = null; return; }
    }
    while (thread == me && isShowing()) {
      step();
      try {
        thread.sleep(999);
      } catch (InterruptedException e) { break; }
    }
    thread = null;
    }

     public Dimension getMinimumSize() {
      return getPreferredSize();
    }

    public Dimension getMaximumSize() {
      return getPreferredSize();
    }

    public Dimension getPreferredSize() {
      return new Dimension( 200, 20 );
    }
}
