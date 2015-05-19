package net.faraya.swing.core.layer;

import net.faraya.swing.core.layer.math.geom.Topology;
import net.faraya.swing.core.layer.math.Vect2D;
import java.awt.geom.Point2D;
import java.awt.geom.PathIterator;
import java.awt.*;
import java.util.Iterator;

/**
 * User: Fabrizzio
 * Date: 09/05/2006
 * Time: 11:48:59 PM
 */
public class PolyLineLayer extends AbstractLayer{

    public void setLastVect( float x, float y, boolean add ){      
      if( add ){
        addVect( x, y , true);
      }
       else
      {
        int i = topology.getSize() - 1;
        setVectAt( i, x, y , true);
      }
    }

    public void saveLastVect( float x, float y ){
        int i =  topology.getSize()-1;
        topology.setVectAt(i , x, y);
    }



    protected  void paintLayer( Graphics2D g ){
      BasicStroke stroke = new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
      g.setStroke( stroke );
      g.setColor( getColor() );
      g.draw( topology );
      Iterator <Vect2D> i = topology.iterator();
      int pos = 0;
      while(i.hasNext()){
       Vect2D v = i.next();   
       g.drawString( "id=" + pos++ + v.toString(), v.x, v.y );
      }
      Vect2D v = topology.getVectAt( 0 );
      g.drawString( "Size: " + Integer.toString(topology.getSize()), v.x + 20, v.y - 20 );
    }

    protected int segmentId = -1;

    public  boolean doHitTest(double x, double y){
      PathIterator i = topology.getPathIterator( null );
      float[] coords = new float[2];
      Vect2D v1, v2;  v1 = v2 = null;
      int seg = 0;
       while(!i.isDone()){
          i.currentSegment( coords );
          i.next();
          Vect2D v = new Vect2D( coords[0], coords[1] );
           if(v1 == null)
            v1 = v;
           else
            if((v2 == null))
             v2 = v;
          if( v1 != null && v2 != null ){
           if (LineLayer.distFromLine((float)x, (float)y, v1, v2) < 4 ){
             this.segmentId = seg;
             return true;
           }
           seg++;
           v1 = v2; v2 = null;
          }
       }
      this.segmentId = -1;
      return false;
    }

    protected void layerDragged( float x, float y ){
     Point2D lp = getLastMouseLocation();
           if(lp != null){
             float dx = (float)(x - lp.getX());
             float dy = (float)(y - lp.getY());
             moveBy( dx, dy );
           }
    }


    public Object clone(){
          PolyLineLayer l = new PolyLineLayer( );
          l.setTopology((Topology)topology.clone());
          l.setColor(getColor());
          l.setSelected(isSelected());
          l.setVisible(isVisible());
          l.setContext(getContext());
          l.setLastMouseLocation(getLastMouseLocation());
          return l;
        }


}
