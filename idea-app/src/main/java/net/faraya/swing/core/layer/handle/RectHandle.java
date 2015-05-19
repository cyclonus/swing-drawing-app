package net.faraya.swing.core.layer.handle;

import net.faraya.swing.core.IViewerConstants;
import net.faraya.swing.core.layer.event.HandleEvent;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: Fabrizzio
 * Date: 13-Dec-2005
 * Time: 11:47:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class RectHandle extends Rectangle implements IHandle {

    private Dimension dimension = IViewerConstants.DEFAULT_HANDLE_SIZE;

    public static Color color = IViewerConstants.DEFAULT_HANDLE_COLOR;

    private Collection <HandleListener> listeners = null;

    private int id = -1;

    public RectHandle( float x, float y ){
     super( );
     setLocation( x, y );
    }

    public RectHandle(Point2D.Float v){
     super( );
     setLocation(v);
    }

    public RectHandle( float x, float y, int id ){
     super( );
     setLocation( x, y );
     this.id = id;
    }

    public void setLocation(int x, int y){
     setLocation( (float)x, (float)y );
    }

    public void setLocation(float x, float y){
      // TODO: remove type cast if Rectangle2D.Float is supported again
      Dimension d = getDimension();
      this.x = (int)x;
      this.y = (int)y;
      this.x = (this.x - d.width  / 2);
      this.y = (this.y - d.height / 2);
      this.width  = d.width;
      this.height = d.height;
    }


    public void setLocation(Point2D.Float v){
      setLocation( v.x, v.y );
    }

    public void paint(Graphics2D g){      
      g.setColor( color );
      g.fill3DRect( (int)x,(int)y, (int)width, (int)height, true );
    }

    public void addHandleListener( HandleListener l ){
      if( listeners == null )
       listeners = new ArrayList <HandleListener> ();
      listeners.add(l);
    }

    public void removeHandleListener( HandleListener l ){
      if( listeners != null )
       listeners.remove( l );
    }

    public void processDragEvent(float x, float y){
      // This Point should ALLWAYS BE MAPPED to the context SCALE
      HandleEvent event = new HandleEvent( this, x, y );
      Iterator i = listeners.iterator();
      while( i.hasNext() ){
         HandleListener l = (HandleListener) i.next();
         l.dragHandle( event );
      }
      setLocation( x, y );
    }

     public boolean contains(float x, float y){
      return super.contains( x, y );
     }

    public int getId(){ return this.id; }

    public void setId(int id){
      this.id = id;
    }

    public String toString(){
       return super.toString() + " id: "+id;
    }

    public Object clone(){
      RectHandle h = new RectHandle( x, y, id );
      if(listeners != null){                 
       for(HandleListener hl : listeners)
        h.addHandleListener(hl);
      }
     return h;
    }

    public Dimension getDimension(){
      return dimension;
    }

}
