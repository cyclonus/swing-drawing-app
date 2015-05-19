package net.faraya.swing.core;

import java.awt.*;

/**
 * User: Fabrizzio
 * Date: 07-Jan-2006
 * Time: 10:08:05
 * To change this template use File | Settings | File Templates.
 */
public interface IViewerConstants {

      //public static final double ZOOM_FACTOR = 0.1;

      public static final double ZOOM_PERCENTAGE = 25D;

      public static final int DEFAULT_WIDTH  = 800;
    
      public static final int DEFAULT_HEIGHT = 600;

      public static final double UNIT_SCALE = 1.0;

      public static final int BOUNDINGBOX_ADJUSTMENT_VALUE = 10;

      public static final double DOTS_PER_CENTIMETER = 2.54;

      public static final Dimension minimumSize   = new Dimension( DEFAULT_WIDTH, DEFAULT_HEIGHT );

      public static final Dimension maximumSize   = new Dimension( DEFAULT_WIDTH, DEFAULT_HEIGHT );

      //public static final Dimension preferredSize = new Dimension( DEFAULT_WIDTH, DEFAULT_HEIGHT );

      public static final Color DEFAULT_RULE_COLOR = new Color(243,243,242);   //new Color(228, 239, 252);

      public static final Color DEFAULT_CONTEXT_COLOR = Color.black;

      public static final Color DEFAULT_GRID_COLOR = Color.lightGray;   // new Color(145, 145, 145);

      public static final Color DEFAULT_HANDLE_COLOR = Color.white;

      public static final Dimension DEFAULT_HANDLE_SIZE = new Dimension( 7 , 7 );
}
