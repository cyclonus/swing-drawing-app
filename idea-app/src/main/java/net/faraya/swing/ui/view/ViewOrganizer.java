package net.faraya.swing.ui.view;

import javax.swing.*;
import java.awt.*;

/**
 * User: Fabrizzio
 * Date: 28-Feb-2006
 * Time: 23:30:56
 * To change this template use File | Settings | File Templates.
 */
public class ViewOrganizer extends JPanel {

    private JTabbedPane tabs = new JTabbedPane(JTabbedPane.BOTTOM);

    private ObjectsView objectsView = new ObjectsView();
    private CommandBar commandBar = new CommandBar();

    public ViewOrganizer(){
      init();
    }

    private void init(){
      setPreferredSize(new Dimension(200, 160 * 6));
      setLayout(new BorderLayout());
      tabs.addTab("Commands",commandBar);
      tabs.addTab("Objects",objectsView);
      add(tabs,BorderLayout.CENTER);
    }

    public ObjectsView getObjectsView(){
       return objectsView;
    }

    public CommandBar getCommandBar(){ return commandBar;  }
}
