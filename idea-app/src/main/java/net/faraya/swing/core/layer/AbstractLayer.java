package net.faraya.swing.core.layer;
//TODO: Check out usage of this  -Dsun.java2d.opengl=true.
/*
 * BasicStroke stroke = new BasicStroke(25f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
 */
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.GeneralPath;
import java.util.*;
import java.io.Serializable;
import net.faraya.swing.core.IContext;
import net.faraya.swing.core.layer.event.HandleEvent;
import net.faraya.swing.core.layer.math.AABB;
import net.faraya.swing.core.layer.math.Vect2D;
import net.faraya.swing.core.layer.math.Utils;
import net.faraya.swing.core.layer.math.geom.Topology;
import net.faraya.swing.core.layer.handle.HandleListener;
import net.faraya.swing.core.layer.handle.IHandle;
import net.faraya.swing.core.layer.handle.RectHandle;



/**
 * User: Fabrizzio
 * Date: 4-Dec-2005
 * Time: 10:50:31 PM 
 */
public abstract class AbstractLayer implements Serializable, Comparable, Cloneable
{


    private IContext context;

    //protected PropertyChangeSupport changeSupport = new PropertyChangeSupport( this );

    private String name;

    private String presentationName;

    private boolean drawName = false;

    private boolean selected = false;

    private boolean visible = true;

    private Point2D lastMouseLocation = new Point();

    private Color color = Color.darkGray;

    private boolean dragging = false;

    private AABB aabb = new AABB();

    protected Topology topology = new Topology();

    protected IHandle handle = null;

    private final HandleListener handleListener = new HandleListener(){
        public void dragHandle(HandleEvent e){
          int index = e.getId();
          System.out.println(  index );
          topology.setVectAt( index, e.getX(), e.getY() );
          repaint();
        }
     };

    private static final long serialVersionUID = -27971967657919028L;

    protected int width = 1;

    public void setWidth(int width){ this.width = width; }

    public int getWidth(){ return width; }

    public void setPropertyWidth(String width){
        this.width = Integer.valueOf(width);
        getContext().paint( this );
    }

    public int getPropertyWidth(){ return width; }


    public GeneralPath getGeneralPath() {
      return new GeneralPath( topology );
    }

    public Topology getTopology(){
      return topology;
    }

    public void setTopology( Topology t ){
     this.topology = t;
    }

    /**
     * addPoint creates a poligon to store the Layer geom
     * @param x
     * @param y
     * @param draggable
     * if draggable a handle object is associated to the point
     * @return int
     * position of the new point the polygon
     */

    public int addVect(float x, float y, boolean draggable){
      if(!draggable)
       return topology.addVect( x, y );
       else{
        IHandle hnd = new RectHandle( x, y );
        int indx = topology.addVect( x , y, hnd );
         hnd.addHandleListener( handleListener );
         return indx;
       }
    }

    /**
     * sets a
     * @param i
     * @param x
     * @param y
     */
    public void setVectAt(int i, float x, float y,  boolean draggable){
        Vect2D v = topology.getVectAt( i );
        v.x = x;
        v.y = y;
        IHandle h = topology.getHandle( v );
        if(h != null){
         h.setLocation( v.x, v.y );
         h.setId(i);
        }else{
          if( draggable ){
            h = new RectHandle( x, y , i);
            topology.setVectAt( i, x,y , h );
            h.addHandleListener( handleListener );
          }
        }
    }

    /**
     * Assosiates the Componete that owns this layer
     * @param o
     */
    public void setContext(IContext o){ context = o;  }

    /**
     * returns the the Component that owns the layer
     * @return IContext
     */
    public IContext getContext(){ return context; }

    /**
     * returns the name associated to the Layer
     * @return String
     */
    public String getName(){ return this.name;}


    public String getPropertyName(){ return getName(); }
    /**
     * Assign a name to the Layer
     * @param name
     */
    public void setName( String name ){ this.name = name;}

    public void setPropertyName( String name ){ setName( name ); }    

    public String getPresentationName(){
        return presentationName;
    };

    public void setPresentationName( String value ){
      presentationName = value;
    };

    /**
     * returns the Selection flag
     * @return boolan
     */
    public boolean isSelected(){ return  selected; }

    /**
     * Marks the Layer as Selected
     * Propagates the selection Action
     * to the Owner making the handles visible
     * @param value
     */
    public void setSelected( boolean value ){
        if( value == selected )
         return;
        selected = value;
        // TODO: firePropertyChange
        if( selected ){
        }
       // repaint();
    }

    /**
     * returns the visibility flag
     * @return boolean
     */
    public boolean isVisible(){ return visible; }

    /**
     * Makes The layer Visible or not
     * propagates the visibility Action
     * to the Owner making the Layer visible or invisible
     * @param value
     */
    public void setVisible( boolean value ){
       if( value == visible )
         return;
        visible = value;
        // TODO: firePropertyChange
        if( visible ){
         // getContext().dispatchEvent( new LayerEvent(this, LayerEvent.LAYER_SELECTED));
          // add the layer to the selection list
        }
    }

    /**
     * returns the last coordinate of the maouse
     * @return Float
     */
    public Point2D getLastMouseLocation(){ return lastMouseLocation; }

    /**
     * saves the mosue coordinate
     * @param value
     */
    public void setLastMouseLocation( Point2D value ){ lastMouseLocation = value; }

    /**
     * saves the mosue coordinate
     * @param x
     * @param y
     */
    protected void setLastMouseLocation( float x, float y ){ lastMouseLocation.setLocation( x, y ); }

    /**
     * returns the Color used to render the layer
     * @return Color
     */
    public Color getColor(){ return this.color; }

    public Color getPropertyColor(){
      return getColor();
    }

    /**
     * sets the Color used to render the layer
     * and propagats the change
     * @param color
     */
    public void setColor(Color color){
      this.color = color;
      repaint();
    }

    public void setPropertyColor( Color color ){
      setColor( color );
    }

    /**
     * returs the dragging flag
     * @return boolean
     */
    public boolean isDragging(){ return this.dragging; }

    /**
     * Hit test
     * @param p
     * @return true if the descendent class contains the point
     */
    public  boolean doHitTest(Point2D p){
      return doHitTest( p.getX(), p.getY() );
    }

    /**
     * Hit test
     * take in count scale when implementing this method
     * @param x
     * @param y
     * @return true if the descendent class contains the point
     */
    public abstract boolean doHitTest(double x , double y);

    /**
     * Renders the Layer content
     * paint the handels is selected
     * @param g
     */
    public void paint( Graphics2D g ){
      if( visible ){
        paintLayer( g );
        if( selected ){
          paintHandles( g );
        }
      }
    }

    /**
     * handles paint method
     * @param g
     */
    protected void paintHandles( Graphics2D g ){
      Collection <IHandle> handles = topology.getHandles();
      if( handles != null ){
       for(IHandle h: handles){
         h.paint( g );
       }
      }
    }

    protected void paintName(Graphics2D g, Vect2D v1, Vect2D v2){
       Point2D mp = Utils.middlePoint(v1, v2);
       g.setPaint( Color.black );
       g.drawString( getName(),(int)mp.getX(), (int)mp.getY() );
    }

    /**
     * render the layer
     * @param g
     */
    protected abstract void paintLayer( Graphics2D g );

    public void moveBy( float dx, float dy ){
       for(Vect2D v : topology){
        v.add( dx, dy );
         IHandle h = ((IHandle)topology.getHandle( v ));
         if( h != null )
             h.setLocation( (float)v.x, (float)v.y );
       }
    }


    /**
     * Please invoke after any transformation
     */
    public AABB updateBounds(){
     aabb = aabb.computeAABB( topology );     
     return aabb;   
    }

    /**
     *  return the current Bounds
     */
    public AABB getAABB( ){
      return aabb;
    }

    public void processDragEvent( float x, float y ){ layerDragged( x, y ); }

    protected abstract void layerDragged(  float x, float y  );

    /**
     * by calling the method render on the drawing context
     * render takes care of rendering only this Layer
     * no full repaint is done
     */
    public void repaint(){
       getContext().paint( this );
    }

    /**
     * Explicit full Repaint
     */
    public void repaintAll(){
       getContext().paint( null );
    }    

    public int hashCode(){
       return hash.intValue();
    }
    
    private Integer hash = new Integer((int) (System.currentTimeMillis() * 623 ) >>  10);
    
    /*
    public boolean equals(Object o){
       if ( o instanceof AbstractLayer ) {
          AbstractLayer other = (AbstractLayer) o;
          return (
            other.vertex.getSignedArea() == this.vertex.getSignedArea()
             && // remove this condition if it turns to be slow
            other.vertex.centroid().equals( this.vertex.centroid() )
          );
       }
        return super.equals( o );
    }

    public AbstractLayer copyInstance() throws Exception{

    }
    */

    public abstract Object clone();

    public String toString(){
       return ( name );
    }

   /*
    public void addPropertyChangeListener( PropertyChangeListener l ){
     changeSupport.addPropertyChangeListener( l );
    }

    public void removePropertyChangeListener(  PropertyChangeListener l ){
     changeSupport.removePropertyChangeListener( l );
    } */

    public int compareTo(Object o){
       AbstractLayer l = (AbstractLayer)o;
       AABB box = l.getAABB();
       return aabb.compareTo( box );

    }

}
