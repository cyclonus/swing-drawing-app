package net.faraya.swing.core.layer;

import net.faraya.swing.core.layer.math.Utils;
import net.faraya.swing.core.layer.math.Vect2D;
import net.faraya.swing.core.layer.math.geom.Polygonal;
import net.faraya.swing.core.layer.math.geom.Topology;
import java.awt.geom.Point2D;
import java.awt.*;


/**
 * User: Fabrizzio
 * Date: 04-Mar-2006
 * Time: 15:03:44
 * To change this template use File | Settings | File Templates.
 */
public class RectLineLayer extends AbstractLayer implements Polygonal {

   public static final int DEFAULT_SEPARATION = 10;

   private Polygon polygon;

   private int separation = DEFAULT_SEPARATION;

   public void setSeparation(int value){ separation = value;  }

   public int getSeparation(){ return separation; }

   public void setPropertySeparation(int value){ setSeparation(value);  }

   public int getPropertySeparation(){ return getSeparation(); }

   protected void layerDragged(  float x, float y  ){
       Point2D lp = getLastMouseLocation();
         if(lp != null){
           float dx = (float)(x - lp.getX());
           float dy = (float)(y - lp.getY());
           moveBy( dx, dy );
         }
   }

   protected void paintLayer( Graphics2D g ){
      g.setColor( Color.DARK_GRAY );
      g.draw( computePolygon() );

   }

   public Polygon computePolygon( ){
     Vect2D v1 = topology.getVectAt( 0 );

     Vect2D v2 = topology.getVectAt( 1 );

     double n  = Utils.computeN( v1, v2, separation );

     double wx = Utils.computeWX( v1, v2, n );

     double wy = Utils.computeWY( v1, v2, n);

     double ax1 = (v1.x + wx);

     double ay1 = (v1.y - wy);

     double bx1 = (v2.x + wx);

     double by1 = (v2.y - wy);

     double ax2 = (v1.x - wx);

     double ay2 = (v1.y + wy);

     double bx2 = (v2.x - wx);

     double by2 = (v2.y + wy);

     polygon = new Polygon(new int[]{(int)ax1,(int)bx1,(int)bx2,(int)ax2},
                           new int[]{(int)ay1,(int)by1,(int)by2,(int)ay2}, 4 );

     return polygon;
   }

   public  boolean doHitTest(double x , double y){ return polygon.contains( x, y ); }

   public Object clone(){
    return null;
   }
}
