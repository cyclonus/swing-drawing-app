package net.faraya.swing.core;

import net.faraya.swing.VerboseRepaintManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * User: Fabrizzio
 * Date: 05-Jan-2006
 * Time: 21:54:23
 * To change this template use File | Settings | File Templates.
 */
public class Viewer extends JComponent {
    //private static Logger log = Logger.getLogger( Viewer.class );
    private JScrollPane sp;
    private Rule columnView;
    private Rule rowView;
    private DrawingContext drawingContext;
    private JToggleButton isMetric;
    public Viewer (){
      drawingContext = new DrawingContext();
      sp = new JScrollPane( drawingContext );
      /*
      columnView = new Rule(Rule.HORIZONTAL, true);
      columnView.setPreferredWidth(
             drawingContext.getWidth()
      );
      columnView.setOpaque( false );
      drawingContext.addContextListener( columnView );

      rowView = new Rule(Rule.VERTICAL, true);
      rowView.setPreferredHeight(
              drawingContext.getHeight()
      );
      rowView.setOpaque( false );
      drawingContext.addContextListener( rowView );

      JPanel buttonCorner = new JPanel(new BorderLayout());
      isMetric = new JToggleButton("[in]", true);
      isMetric.setFont(new Font("SansSerif", Font.PLAIN, 10));
      isMetric.addItemListener(new ItemListener(){
          public void itemStateChanged(ItemEvent e) {
            unitsItemStateChanged(e);
          }
      });
      isMetric.setMargin(new Insets(2, 2, 2, 2));
      buttonCorner.add(isMetric, BorderLayout.CENTER); //Use the default FlowLayout

      sp.setColumnHeaderView( columnView );
      sp.setRowHeaderView( rowView );
      //sp.setCorner(JScrollPane.UPPER_LEFT_CORNER, buttonCorner);
      sp.setCorner(JScrollPane.LOWER_LEFT_CORNER, new Corner());
      sp.setCorner(JScrollPane.UPPER_RIGHT_CORNER, new Corner());
      */
      sp.getHorizontalScrollBar().setUnitIncrement(25);
      sp.getHorizontalScrollBar().addAdjustmentListener(
              new AdjustmentListener(){
                public void adjustmentValueChanged(AdjustmentEvent e){

                }
              }
      );
      sp.getVerticalScrollBar().setUnitIncrement(25);
      sp.getVerticalScrollBar().addAdjustmentListener(
              new AdjustmentListener(){
                public void adjustmentValueChanged(AdjustmentEvent e){

                }
              }
      );

      JLabel label = new JLabel(" ");
      drawingContext.setMouseLabel(label);
      setLayout( new BorderLayout() );
      add( sp, BorderLayout.CENTER );
      add(label, BorderLayout.SOUTH );
    }

    private void unitsItemStateChanged(ItemEvent e){
      if (e.getStateChange() == ItemEvent.SELECTED) {
                // Turn it to metric.
                rowView.setIsMetric( true );
                columnView.setIsMetric( true );
            } else {
                // Turn it to inches.
                rowView.setIsMetric( false );
                columnView.setIsMetric( false );
            }
    }

    public IContext getDrawingContext(){ return drawingContext; }


    public static void main( String args[] ){
        Viewer viewer = new Viewer();
        JFrame f = new JFrame();
        f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
       // VerboseRepaintManager.install();

        f.getContentPane().setLayout( new BorderLayout() );
        f.getContentPane().add( viewer, BorderLayout.CENTER );
        f.setSize( new Dimension( 400, 300) );
        f.setVisible( true );

    }

}
