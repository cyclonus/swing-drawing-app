package net.faraya.swing.ui.view;

import net.faraya.swing.ui.UIImages;

import javax.swing.*;
import java.awt.*;

/**
 * User: Fabrizzio
 * Date: 02-Mar-2006
 * Time: 22:59:57
 * To change this template use File | Settings | File Templates.
 */
public class CommandBar extends JPanel {

  ButtonGroup cmdsGroup = new ButtonGroup();
  GridLayout gridLayout1 = new GridLayout();
  JToggleButton selectToolButton = new JToggleButton( UIImages.getIcon( UIImages.ARROW_20 ));
  JToggleButton drawLineButton   = new JToggleButton( UIImages.getIcon( UIImages.PAINT_20 ));
  JToggleButton drawRectLineButton = new JToggleButton( UIImages.getIcon( UIImages.PALETTE_20 ));
  JToggleButton drawPolyLineButton = new JToggleButton( UIImages.getIcon( UIImages.LINEGRAPH_20 ));
  JToggleButton jToggleButton5   = new JToggleButton( UIImages.getIcon( UIImages.DIAGRAM_20 ));
  JToggleButton jToggleButton6   = new JToggleButton( UIImages.getIcon( UIImages.DOCDRAW_20 ));
  JToggleButton jToggleButton7   = new JToggleButton( UIImages.getIcon( UIImages.OBJECT_20 ));
  JToggleButton jToggleButton8   = new JToggleButton( UIImages.getIcon( UIImages.FLOWGRAPH_20 ));
  JToggleButton jToggleButton9   = new JToggleButton( UIImages.getIcon( UIImages.PAINT_20 ));
  JToggleButton jToggleButton10  = new JToggleButton(UIImages.getIcon( UIImages.PAINT_20 ));
  JToggleButton jToggleButton11  = new JToggleButton( UIImages.getIcon( UIImages.PAINT_20 ));
  JToggleButton jToggleButton12  = new JToggleButton(UIImages.getIcon( UIImages.PAINT_20 ));

    public CommandBar(){
        init();
    }

    private void init(){
    setLayout(new BorderLayout());
    JPanel panel = new JPanel();
    add(panel,BorderLayout.NORTH);
    panel.setLayout(gridLayout1);
    gridLayout1.setColumns(3);
    gridLayout1.setRows(4);

      /*
      setMinimumSize(new Dimension(25, 25));
            editButtons[i].setPreferredSize(new Dimension(25, 25));
      */
    selectToolButton.setMinimumSize(new Dimension(25, 25));
    selectToolButton.setPreferredSize(new Dimension(25, 25));

    drawLineButton.setMinimumSize(new Dimension(25, 25));
    drawLineButton.setPreferredSize(new Dimension(25, 25));

    drawRectLineButton.setMinimumSize(new Dimension(25, 25));
    drawRectLineButton.setPreferredSize(new Dimension(25, 25));

    drawPolyLineButton.setMinimumSize(new Dimension(25, 25));
    drawPolyLineButton.setPreferredSize(new Dimension(25, 25));

    jToggleButton5.setMinimumSize(new Dimension(25, 25));
    jToggleButton5.setPreferredSize(new Dimension(25, 25));

    jToggleButton6.setMinimumSize(new Dimension(25, 25));
    jToggleButton6.setPreferredSize(new Dimension(25, 25));

    jToggleButton7.setMinimumSize(new Dimension(25, 25));
    jToggleButton7.setPreferredSize(new Dimension(25, 25));

    jToggleButton8.setMinimumSize(new Dimension(25, 25));
    jToggleButton8.setPreferredSize(new Dimension(25, 25));

    jToggleButton9.setMinimumSize(new Dimension(25, 25));
    jToggleButton9.setPreferredSize(new Dimension(25, 25));

    jToggleButton10.setMinimumSize(new Dimension(25, 25));
    jToggleButton10.setPreferredSize(new Dimension(25, 25));

    jToggleButton11.setMinimumSize(new Dimension(25, 25));
    jToggleButton11.setPreferredSize(new Dimension(25, 25));

    jToggleButton12.setMinimumSize(new Dimension(25, 25));
    jToggleButton12.setPreferredSize(new Dimension(25, 25));

    selectToolButton.setEnabled(true);
    drawLineButton.setEnabled(true);
    selectToolButton.setSelected(true);
    drawRectLineButton.setEnabled(true);
    drawPolyLineButton.setEnabled(true);

    jToggleButton5.setEnabled(false);
    jToggleButton9.setEnabled(false);
    jToggleButton6.setEnabled(false);
    jToggleButton7.setEnabled(false);
    jToggleButton8.setEnabled(false);
    jToggleButton10.setEnabled(false);
    jToggleButton11.setEnabled(false);
    jToggleButton12.setEnabled(false);

    panel.add(selectToolButton, null);
    panel.add(drawLineButton, null);
    panel.add(drawRectLineButton, null);
    panel.add(drawPolyLineButton, null);
    panel.add(jToggleButton5, null);
    panel.add(jToggleButton9, null);
    panel.add(jToggleButton6, null);
    panel.add(jToggleButton7, null);
    panel.add(jToggleButton8, null);
    panel.add(jToggleButton10, null);
    panel.add(jToggleButton11, null);
    panel.add(jToggleButton12, null);

    cmdsGroup.add(selectToolButton);
    cmdsGroup.add(drawLineButton);
    cmdsGroup.add(drawRectLineButton);
    cmdsGroup.add(drawPolyLineButton);
    cmdsGroup.add(jToggleButton5);
    cmdsGroup.add(jToggleButton9);
    cmdsGroup.add(jToggleButton6);
    cmdsGroup.add(jToggleButton7);
    cmdsGroup.add(jToggleButton8);
    cmdsGroup.add(jToggleButton9);
    cmdsGroup.add(jToggleButton11);
    cmdsGroup.add(jToggleButton12);

    }

    public JToggleButton getSelctionToolButton(){ return selectToolButton; }
    public JToggleButton getLineButton(){ return drawLineButton; }
    public JToggleButton getRectLineButton(){ return drawRectLineButton; }
    public JToggleButton getPolyLineButton(){ return drawPolyLineButton; }
}
