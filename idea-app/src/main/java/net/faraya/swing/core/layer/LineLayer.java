package net.faraya.swing.core.layer;

import net.faraya.swing.core.layer.math.VMath;
import net.faraya.swing.core.layer.math.Vect2D;
import net.faraya.swing.core.layer.math.AABB;
import net.faraya.swing.core.layer.math.Utils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;

/**
 * User: Fabrizzio
 * Date: 31-Jan-2006
 * Time: 23:42:20
 * To change this template use File | Settings | File Templates.
 */
public class LineLayer extends AbstractLayer{

    final Vect2D vpos = new Vect2D();

    public LineLayer(){
        setPresentationName( "Line" );
       // addVect( 60,60, true );
       // addVect( 260,260, true );
    }

    public LineLayer( Vect2D p1, Vect2D p2 ){
        setPresentationName( "Line" );
        addVect( (float)p1.x, (float)p1.y, true );
        addVect( (float)p2.x, (float)p2.y, true );
    }

    public Object clone(){
      Vect2D v1 = new Vect2D( topology.getVectAt(0) );
      Vect2D v2 = new Vect2D( topology.getVectAt(1) );
      LineLayer l = new LineLayer( v1, v2);
      l.setColor(getColor());
      l.setSelected(isSelected());
      l.setVisible(isVisible());
      l.setContext(getContext());
      l.setLastMouseLocation(getLastMouseLocation());
      //l.handle is this also needed ???
      return l;
    }

    protected void layerDragged( float x, float y ){

      Point2D lp = getLastMouseLocation();

       if(lp != null){
         float dx = (float)(x - lp.getX());
         float dy = (float)(y - lp.getY());
         moveBy( dx, dy );
       }

    }


    protected void paintLayer( Graphics2D g ){
       BasicStroke stroke = new BasicStroke(width, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER);
       g.setStroke( stroke ); 
       g.setColor( getColor() );
       Vect2D v1 = topology.getVectAt( 0 );
       Vect2D v2 = topology.getVectAt( 1 );
       g.drawLine( (int)v1.x, (int)v1.y, (int)v2.x, (int)v2.y);       
    }

    public  boolean doHitTest(double x , double y){
       vpos.setPos( x, y );
       Vect2D v1 = topology.getVectAt( 0 );
       Vect2D v2 = topology.getVectAt( 1 );
       boolean hit = (( v1.distance( vpos ) < 8 ) || ( v2.distance( vpos ) < 8 ));
       if( hit )
         return  true;
        else{
         return (distFromLine( vpos.x, vpos.y, v1, v2 ) < 4 );
       }
    }

    /**
         * returns Double.POSITIVE_INFINITY if closest point to line is not contained
         * in line segment
         */
        public static double distFromLine( float x, float y, Vect2D v1, Vect2D v2 ) {

                float ax = v1.x, ay = v1.y;
                float bx = v2.x, by = v2.y;

                if ( ax != bx || ay != by ) {
                        double denom = VMath.square( bx - ax ) + VMath.square( by - ay );

                        double u = ( x - ax ) * ( bx - ax ) + ( y - ay ) * ( by - ay );
                        u /= denom;
                        /*
                        if ( segment ) {
                        */
                                if ( u < 0 || u > 1 ) {
                                        return Double.POSITIVE_INFINITY;
                                }
                       /* } */

                        double[] test = new double[] {
                                ax + u * ( bx - ax ),
                                ay + u * ( by - ay )
                        };

                        return VMath.distance( test[0], test[1], x, y );
                }
                else {
                        return VMath.distance( x, y, ax, ay);
                }
        }

}
