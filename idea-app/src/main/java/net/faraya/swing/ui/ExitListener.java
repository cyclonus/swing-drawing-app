package net.faraya.swing.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * User: Fabrizzio
 * Date: 01-Jan-2006
 * Time: 11:49:41
 * To change this template use File | Settings | File Templates.
 */
public class ExitListener extends WindowAdapter {
  public void windowClosing(WindowEvent event) {
    System.exit(0);
  }
}
