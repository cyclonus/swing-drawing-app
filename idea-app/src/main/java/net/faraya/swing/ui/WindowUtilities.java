package net.faraya.swing.ui;

import javax.swing.*;
import java.awt.*;

/**
 * User: Fabrizzio
 * Date: 01-Jan-2006
 * Time: 11:47:25
 * To change this template use File | Settings | File Templates.
 */
/** A few utilities that simplify using windows in Swing.
 *  1998-99 Marty Hall, http://www.apl.jhu.edu/~hall/java/
 */

public class WindowUtilities {

  /** Tell system to use native look and feel, as in previous
   *  releases. Metal (Java) LAF is the default otherwise.
   */

  public static void setNativeLookAndFeel() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch(Exception e) {
      System.out.println("Error setting native LAF: " + e);
    }
  }

  public static void setJavaLookAndFeel() {
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch(Exception e) {
      System.out.println("Error setting Java LAF: " + e);
    }
  }

   public static void setMotifLookAndFeel() {
    try {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
    } catch(Exception e) {
      System.out.println("Error setting Motif LAF: " + e);
    }
  }

  /** A simplified way to see a JPanel or other Container.
   *  Pops up a JFrame with specified Container as the content pane.
   */

  public static JFrame openInJFrame(Container content,
                                    int width,
                                    int height,
                                    String title,
                                    Color bgColor) {
    JFrame frame = new JFrame(title);
    frame.setBackground(bgColor);
    content.setBackground(bgColor);
    frame.setSize(width, height);
    frame.setContentPane(content);
    frame.addWindowListener(new ExitListener());
    frame.setVisible(true);
    return(frame);
  }

  /** Uses Color.white as the background color. */

  public static JFrame openInJFrame(Container content,
                                    int width,
                                    int height,
                                    String title) {
    return(openInJFrame(content, width, height, title, Color.white));
  }

  /** Uses Color.white as the background color, and the
   *  name of the Container's class as the JFrame title.
   */

  public static JFrame openInJFrame(Container content,
                                    int width,
                                    int height) {
    return(openInJFrame(content, width, height,
                        content.getClass().getName(),
                        Color.white));
  }

  public static void center(JFrame frame) {
          GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
          Point center = ge.getCenterPoint();
          Rectangle bounds = ge.getMaximumWindowBounds();
          int w = Math.max(bounds.width/2, Math.min(frame.getWidth(), bounds.width));
          int h = Math.max(bounds.height/2, Math.min(frame.getHeight(), bounds.height));
          int x = center.x - w/2, y = center.y - h/2;
          frame.setBounds(x, y, w, h);
          if (w == bounds.width && h == bounds.height)
              frame.setExtendedState(Frame.MAXIMIZED_BOTH);
          frame.validate();
      }


}

