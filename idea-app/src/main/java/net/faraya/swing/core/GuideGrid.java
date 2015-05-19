package net.faraya.swing.core;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * User: Fabrizzio
 * Date: 11-Jan-2006
 * Time: 23:08:32
 * To change this template use File | Settings | File Templates.
 */
public class GuideGrid implements Serializable {

    int spacing  = 16;
    Color color  = IViewerConstants.DEFAULT_GRID_COLOR;
    int dotSize  = 1;
    int gridSize = spacing;
    int style = 2;
    boolean paintLines = false;
    boolean paintDots  = true;
    private static final long serialVersionUID = 5113470596642084766L;

    public void paint(Graphics2D g){
       if( paintLines )
        paintLines(g, false );
       if( paintDots )
        paintDots( g, false );
    }

    public void paint(Graphics2D g , boolean paintBackground){
       if( paintLines )
        paintLines(g, paintBackground );
       if( paintDots )
        paintDots( g, paintBackground );
    }

    private void paintDots(Graphics2D g, boolean paintBackground)
        {
            Rectangle clip = g.getClipBounds();
            if(paintBackground)
            {
                g.setColor(g.getBackground());
                g.fillRect(clip.x, clip.y, clip.width, clip.height);
            }
            int x = (clip.x / spacing) * spacing - spacing;
            int y = (clip.y / spacing) * spacing - spacing;
            int right = clip.x + clip.width;
            int bot = clip.y + clip.height;
            g.setColor( color.darker() );
            for(; x <= right; x += spacing)
                for(y = 0; y <= bot; y += spacing)
                    g.fillRect( x, y, dotSize, dotSize );
        }


   private void paintLines(Graphics2D g, boolean paintBackground)
    {
        java.awt.Rectangle clip = g.getClipBounds();
        if(paintBackground)
        {
            g.setColor(g.getBackground());
            g.fillRect(clip.x, clip.y, clip.width, clip.height);
        }
        int x = (clip.x / spacing) * spacing - spacing;
        int y = (clip.y / spacing) * spacing - spacing;
        int stepsX = clip.width / spacing + 2;
        int stepsY = clip.height / spacing + 2;
        int right = clip.x + clip.width;
        int bot = clip.y + clip.height;
        g.setColor( color.brighter() );
        for(; stepsX > 0; stepsX--)
        {
            g.drawLine(x, 0, x, bot);
            x += spacing;
        }

        for(; stepsY > 0; stepsY--)
        {
            g.drawLine(0, y, right, y);
            y += spacing;
        }

    }

    public void snap(Point2D p)
    {
        double x = gridSize * (Math.round(p.getX() / ((float) gridSize)));
        double y = gridSize * (Math.round(p.getY() / ((float) gridSize)));
        p.setLocation( x, y );
    }

    public void adjust()
    {
        if(gridSize >= 32)
            gridSize = 4;
        else
            gridSize *= 2;

       spacing = gridSize;
    }



    public int getStyle(){ return style; }

    public void switchStyle()
        {
            style = (style + 1) % 5;

            switch(style)
            {
            case LINES_DOTS_16:
                paintLines = true;
                paintDots = true;
                spacing = 16;
                gridSize = spacing;
                break;

            case LINES_DOTS_8:
                paintLines = true;
                paintDots = true;
                spacing = 8;
                gridSize = spacing;
                break;

            case NO_LINES_DOTS_16:
                paintLines = false;
                paintDots = true;
                spacing = 16;
                gridSize = spacing;
                break;

            case NO_LINES_DOTS_32:
                paintLines = false;
                paintDots = true;
                spacing = 32;
                gridSize = spacing;
                break;

            case NO_LINES_NO_DOTS:
                paintLines = false;
                paintDots = false;
                break;
            }

        }

      public static final int LINES_DOTS_16 = 0;
      public static final int LINES_DOTS_8  = 1;
      public static final int NO_LINES_DOTS_16 = 2;
      public static final int NO_LINES_DOTS_32 = 3;
      public static final int NO_LINES_NO_DOTS = 4;

      public void setSpacing(int value){
         this.spacing  = value;
         this.gridSize = this.spacing;
      }

      public int getSpacing( ){ return this.spacing; }


}
