package net.faraya.swing.core.layer.math.geom;

import net.faraya.swing.core.layer.math.Vect2D;
import net.faraya.swing.core.layer.handle.IHandle;
import sun.awt.geom.Crossings;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.awt.geom.PathIterator;
import java.awt.geom.AffineTransform;
import java.awt.*;
import java.util.*;


/**
 * User: Fabrizzio
 * Date: 28-Jan-2006
 * Time: 12:45:25
 * To change this template use File | Settings | File Templates.
 */
public class Topology implements Shape , Cloneable, Iterable<Vect2D>{

    protected java.util.List <Vect2D> vertex;
    protected Rectangle2D.Double bounds;
    protected HashMap<Vect2D, IHandle> handles = new HashMap<Vect2D, IHandle>();
    protected boolean closed = false;

    /**
     * Creates an empty polygon.
     */
    public Topology() {
     vertex = new ArrayList<Vect2D>();
    }

    /**
     *
     * @param t
     */
    public Topology(Topology t){
       this();
       Iterator i = t.vertex.iterator();
       while(i.hasNext()){
         Vect2D v = (Vect2D)i.next();
          IHandle h = t.handles.get(v);
          if(h != null){
            IHandle newHnd = (IHandle)h.clone();
            addVect(v.x, v.y, newHnd);
          }else{
            addVect(v.x, v.y);
          }
       }
    }

    /**
     *
     * @param xpoints
     * @param ypoints
     * @param npoints
     */
    public Topology(float xpoints[], float ypoints[], int npoints) {
     vertex = new ArrayList <Vect2D>();
      for(int i=0; i < npoints; i++){
        vertex.add( new Vect2D( xpoints[i], ypoints[i] ) );
      }
    }

    /**
     *
     * @return polygon size
     */
    public int getSize(){ return vertex.size(); }

    /**
     *
     * @return number of assigned handles in the polygon
     */
    public int handlesCount(){ return handles.size(); }

    /**
     *
     * @param i
     * @return
     */
    public Vect2D getVectAt(int i){
      return vertex.get( i );
    }

    /**
     *
     * @param i
     * @param v
     */
    public void setVectAt( int i, Vect2D v ){
      Vect2D oldVect = getVectAt( i );
      IHandle h = handles.get( oldVect );
      if(h != null){
        h.setLocation( v.x, v.y );
        h.setId( i );
      }
      vertex.set( i, v );
    }

    /**
     *
     * @param i
     * @param x
     * @param y
     */
    public void setVectAt( int i, float x, float y ){
      Vect2D v = getVectAt( i );
      v.x = x;
      v.y = y;      
      IHandle h = handles.get( v );
      if(h != null){
       h.setLocation( v.x, v.y );
       h.setId( i );
      }
    }

    /**
     *
     * @param i
     * @param x
     * @param y
     * @param hnd
     */
    public void setVectAt( int i, float x, float y, IHandle hnd ){
      Vect2D v = getVectAt( i );
      v.x = x;
      v.y = y;
      if(hnd != null)
      {
        handles.put(v,hnd);
        hnd.setLocation( v.x, v.y );
        hnd.setId( i );
      }
    }

    /**
     *
     * @return
     */
    public Collection <IHandle> getHandles(){ return this.handles.values();  }


    /**
     *
     * @param v
     * @return
     */
    public IHandle getHandle( Vect2D v ){
      return handles.get( v );
    }

    /**
     *
     * @return
     */
    public Collection <Vect2D> getDragableVects( ){
      return this.handles.keySet();
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public IHandle getSelectedHandle(double x, double y ){
           Iterator i = handles.values().iterator();
           while( i.hasNext() ){
               IHandle hnd = (IHandle)i.next();
               if( hnd.contains( (float)x, (float)y ) ){
                 return hnd;
               }
           }
           return null;
        }

    /**
     *
     * @param p
     * @return
     */
   public IHandle getSelectedHandle( Point2D p ){
     return getSelectedHandle( p.getX(), p.getY() );
   }

    /**
     *
     * @param x
     * @param y
     * @param handle
     * @return
     */
    public int addVect(double x, double y, IHandle handle) {
     if(handle != null){
       Vect2D v = new Vect2D( x, y );
       handles.put(v, handle );
       vertex.add( v );
       int index = vertex.size()-1;
       handle.setId( index );
       if (bounds != null) {
         updateBounds(x, y);
       }  
       return index;
     }else
       return addVect( x, y );
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public int addVect(double x, double y) {
      vertex.add( new Vect2D( x, y ) );
      if (bounds != null) {
         updateBounds(x, y);
      }
     return vertex.size()-1;
    }

    /**
     *
     * @param fromIndex
     * @param toIndex
     * @return
     */
    public Topology remove( int fromIndex,  int toIndex){
      Topology t = new Topology();
      Collection <Vect2D> subList = vertex.subList( fromIndex, toIndex );
      vertex.removeAll( subList );
      for ( Vect2D v: subList ){
       IHandle h = handles.get( v );
        if(h != null){
         handles.remove( v );
         t.addVect( v.x, v.y, h );
        }else{
         t.addVect( v.x, v.y);
        }
      }
      return t;
    }

    /**
     * the region substracted is always returned as the position 0
     * @param fromIndex
     * @param toIndex
     * @return
     */
    public Collection <Topology> substract( int fromIndex,  int toIndex){
     Collection <Topology> resultList  = new ArrayList <Topology> ();
       // El segmento se encuentra..
       if((fromIndex == 0 )&&(toIndex < vertex.size())){ // al principio
        resultList.add( remove( fromIndex, toIndex ) );
        resultList.add( this );
       }else
        if((fromIndex > 0 )&&(toIndex < vertex.size())){  // en medio
          resultList.add( remove( fromIndex, toIndex ) );
          resultList.add( remove( 0, fromIndex) );
          resultList.add( this );
        }else
         if((fromIndex > 0 )&&(toIndex == vertex.size())){  // al final
          resultList.add( remove( fromIndex, toIndex ) );
          resultList.add( this );
         }
      return resultList;
    }

    /**
     *
     * @param i
     * @param x
     * @param y
     */
    public void translateVectAt( int i, float x, float y ){
      Vect2D v = vertex.get( i );
      v.x += x;
      v.y += y;
      IHandle h = handles.get( v );
      if(h != null){
       h.setLocation( (float)v.x, (float)v.y );
       h.setId(i);
      }
    }

    /**
     *
     * @return
     */
    public Iterator <Vect2D> iterator(){ return vertex.iterator(); }

    /**
     *
     */
    public void reset() {
     vertex.clear();
     bounds = null;
    }

    /**
     *
     */
    public void invalidate() {
      bounds = null;
    }

    /**
     *
     * @param deltaX
     * @param deltaY
     */
    public void translate(float deltaX, float deltaY) {
     Iterator i = vertex.iterator();
       while(i.hasNext()) {
        Vect2D v = (Vect2D)i.next();
        v.x += deltaX;
        v.y += deltaY;
        IHandle h = handles.get( v );
        // TODO Add Translation Code to the handle   
       }
        if (bounds != null) {
            bounds.x += deltaX;
            bounds.y += deltaY;
        }
    }

    /**
     *
     */
    void calculateBounds() {
    double boundsMinX = Double.MAX_VALUE;
    double boundsMinY = Double.MAX_VALUE;
    double boundsMaxX = Double.MIN_VALUE;
    double boundsMaxY = Double.MIN_VALUE;

    for (Iterator i = vertex.iterator(); i.hasNext(); ) {
        Vect2D p = (Vect2D)i.next();
        double x = (double)p.x;
        boundsMinX = Math.min(boundsMinX, x);
        boundsMaxX = Math.max(boundsMaxX, x);
        double y = (double)p.y;
        boundsMinY = Math.min(boundsMinY, y);
        boundsMaxY = Math.max(boundsMaxY, y);
    }
        bounds = new Rectangle2D.Double(
                   boundsMinX, boundsMinY,
                   boundsMaxX - boundsMinX,
                   boundsMaxY - boundsMinY
        );
    }

    /*
     * Resizes the bounding box to accomodate the specified coordinates.
     * @param x,&nbsp;y the specified coordinates
     */
    void updateBounds(double x, double y) {
    if (x < bounds.x) {
        bounds.width = bounds.width + (bounds.x - x);
        bounds.x = x;
    }
    else {
        bounds.width = Math.max(bounds.width, x - bounds.x);
        // bounds.x = bounds.x;
    }

    if (y < bounds.y) {
        bounds.height = bounds.height + (bounds.y - y);
        bounds.y = y;
    }
    else {
        bounds.height = Math.max(bounds.height, y - bounds.y);
        // bounds.y = bounds.y;
    }
    }

    public Rectangle getBounds() {
    return getBoundingBox();
    }

    public Rectangle getBoundingBox() {
     int npoints = vertex.size();
     if (npoints == 0) {
        return new Rectangle();
     }
     if (bounds == null) {
        calculateBounds();
     }
     return bounds.getBounds();
    }

    public boolean contains(Point p) {
    return contains(p.x, p.y);
    }

    public boolean contains(int x, int y) {
    return contains((double) x, (double) y);
    }

    public boolean inside(int x, int y) {
    return contains((double) x, (double) y);
    }

    public Rectangle2D getBounds2D() {
    return getBounds();
    }

    public boolean contains(double x, double y) {
    int npoints = vertex.size();
    if (npoints <= 2 || !getBoundingBox().contains(x, y)) {
    return false;
    }
    int hits = 0;

    Vect2D lp = vertex.get(npoints - 1);
    double lastx = lp.x;
    double lasty = lp.y;
    double curx, cury;

    // Walk the edges of the polygon
    for (int i = 0; i < npoints; lastx = curx, lasty = cury, i++) {

        Vect2D p = vertex.get(i);
        curx = p.x;
        cury = p.y;

        if (cury == lasty) {
        continue;
        }

        double leftx;
        if (curx < lastx) {
        if (x >= lastx) {
            continue;
        }
        leftx = curx;
        } else {
        if (x >= curx) {
            continue;
        }
        leftx = lastx;
        }

        double test1, test2;
        if (cury < lasty) {
        if (y < cury || y >= lasty) {
            continue;
        }
        if (x < leftx) {
            hits++;
            continue;
        }
        test1 = x - curx;
        test2 = y - cury;
        } else {
        if (y < lasty || y >= cury) {
            continue;
        }
        if (x < leftx) {
            hits++;
            continue;
        }
        test1 = x - lastx;
        test2 = y - lasty;
        }

        if (test1 < (test2 / (lasty - cury) * (lastx - curx))) {
        hits++;
        }
    }

    return ((hits & 1) != 0);
    }

    private Crossings getCrossings(double xlo, double ylo,
                                   double xhi, double yhi)
    {
    Crossings cross = new Crossings.EvenOdd(xlo, ylo, xhi, yhi);
    int npoints = vertex.size();
    Vect2D lp = vertex.get(npoints - 1);
    double lastx = lp.x;
    double lasty = lp.y;
    double curx, cury;

    // Walk the edges of the polygon
    for (int i = 0; i < npoints; i++) {
        Vect2D p = vertex.get( i );
        curx = p.x;
        cury = p.y;
        if (cross.accumulateLine(lastx, lasty, curx, cury)) {
        return null;
        }
        lastx = curx;
        lasty = cury;
    }

    return cross;
    }

    public boolean contains(Point2D p) {
    return contains(p.getX(), p.getY());
    }

    public boolean intersects(double x, double y, double w, double h) {
    int npoints = vertex.size();
    if (npoints <= 0 || !getBoundingBox().intersects(x, y, w, h)) {
        return false;
    }

    Crossings cross = getCrossings(x, y, x+w, y+h);
    return (cross == null || !cross.isEmpty());
    }

    public boolean intersects(Rectangle2D r) {
    return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public boolean contains(double x, double y, double w, double h) {
    int npoints = vertex.size();
    if (npoints <= 0 || !getBoundingBox().intersects(x, y, w, h)) {
        return false;
    }
    Crossings cross = getCrossings(x, y, x+w, y+h);
    return (cross != null && cross.covers(y, y+h));
    }

    public void setClosed( boolean value ){
      closed = value;
    }

    public boolean isClosed(){ return closed; }

    public double getSignedArea(){
    java.awt.Polygon P;
    final int n = vertex.size();
    int i,j;
    double area = 0;

    for (i=0; i< n; i++) {
        j = (i + 1) % n;
        Vect2D v1 = vertex.get( i );
        Vect2D v2 = vertex.get( j );
        area += v1.x * v2.y;
        area -= v1.y * v2.x;
    }
    area /= 2.0;

    return(area);
    //return(area < 0 ? -area : area); for unsigned
    }

    public Vect2D centroid()
    {
    double cx = 0, cy = 0;
    double A = getSignedArea();
    final int n = vertex.size();
    int i,j;

    double factor = 0;
    for (i=0;i<n;i++) {
        j = (i + 1) % n;
        Vect2D vi = vertex.get( i );
        Vect2D vj = vertex.get( j );
        factor=(vi.x*vj.y-vj.x*vi.y);
        cx+=(vi.x+vj.x)*factor;
        cy+=(vi.y+vj.y)*factor;
    }
    A*=6.0f;
    factor=1/A;
    cx*=factor;
    cy*=factor;
    return new Vect2D( cx, cy );
    }


    public Vect2D [] toArray(){
      Vect2D [] d = new Vect2D [vertex.size()];
      Iterator i = vertex.iterator();
      int indx = 0;
      while(i.hasNext()){
       d[indx++] = (Vect2D)i.next();
      }
      return d;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        Iterator <Vect2D> i = iterator();
        while(i.hasNext()){
          Vect2D v = i.next();
          IHandle h = handles.get(v);
          sb.append( "(" ).
             append( v ).
             append( " h=" ).
             append( h ).
             append( ")" ).
             append( i.hasNext() ? "," : "" );
        }
        return sb.append("]").toString();
    }

    public boolean contains(Rectangle2D r) {
    return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public PathIterator getPathIterator(AffineTransform at) {
    return new TopologyPathIterator(this, at);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
    return getPathIterator(at);
    }

    public Object clone(){
       Topology p = new Topology();
       Iterator i = vertex.iterator();
       while(i.hasNext()){
         Vect2D v = (Vect2D)i.next();
          IHandle h = handles.get(v);
          if(h != null){
            IHandle newHnd = (IHandle)h.clone();
            p.addVect(v.x, v.y, newHnd);
          }else{
            p.addVect(v.x, v.y);
          }
       }
       return p;
    }

    class TopologyPathIterator implements PathIterator {
    Topology topology;
    AffineTransform transform;
    int index;

    public TopologyPathIterator(Topology pg, AffineTransform at) {
        topology = pg;
        transform = at;
        if (pg.getSize() == 0) {
        // Prevent a spurious SEG_CLOSE segment
        index = 1;
        }
    }

    /**
     * Returns the winding rule for determining the interior of the
     * path.
         * @return an integer representing the current winding rule.
     * @see PathIterator#WIND_NON_ZERO
     */
    public int getWindingRule() {
       if(topology.isClosed())
        return WIND_NON_ZERO;
         return WIND_EVEN_ODD;
    }

    /**
     * Tests if there are more points to read.
     * @return <code>true</code> if there are more points to read;
         *          <code>false</code> otherwise.
     */
    public boolean isDone() {
      if(topology.isClosed())
       return index > topology.getSize();
        return index >= topology.getSize();
    }

    /**
     * Moves the iterator forwards, along the primary direction of
         * traversal, to the next segment of the path when there are
     * more points in that direction.
     */
    public void next() {
        index++;
    }

    /**
     * Returns the coordinates and type of the current path segment in
     * the iteration.
     * The return value is the path segment type:
     * SEG_MOVETO, SEG_LINETO, or SEG_CLOSE.
     * A <code>float</code> array of length 2 must be passed in and
         * can be used to store the coordinates of the point(s).
     * Each point is stored as a pair of <code>float</code> x,&nbsp;y
         * coordinates.  SEG_MOVETO and SEG_LINETO types return one
         * point, and SEG_CLOSE does not return any points.
         * @param coords a <code>float</code> array that specifies the
         * coordinates of the point(s)
         * @return an integer representing the type and coordinates of the
         * 		current path segment.
     * @see PathIterator#SEG_MOVETO
     * @see PathIterator#SEG_LINETO
     * @see PathIterator#SEG_CLOSE
     */
    public int currentSegment(float[] coords) {
       if (isDone())
	    throw new NoSuchElementException("Topology iterator out of bounds");

        if(topology.isClosed())
          if(index >= topology.getSize())
           return SEG_CLOSE;

        Vect2D p = topology.getVectAt( index );
        coords[0] = (float)p.x;
        coords[1] = (float)p.y;
        if (transform != null) {
         transform.transform(coords, 0, coords, 0, 1);
        }
        return (index == 0 ? SEG_MOVETO : SEG_LINETO);
    }

    /**
     * Returns the coordinates and type of the current path segment in
     * the iteration.
     * The return value is the path segment type:
     * SEG_MOVETO, SEG_LINETO, or SEG_CLOSE.
     * A <code>double</code> array of length 2 must be passed in and
         * can be used to store the coordinates of the point(s).
     * Each point is stored as a pair of <code>double</code> x,&nbsp;y
         * coordinates.
     * SEG_MOVETO and SEG_LINETO types return one point,
     * and SEG_CLOSE does not return any points.
         * @param coords a <code>double</code> array that specifies the
         * coordinates of the point(s)
         * @return an integer representing the type and coordinates of the
         * 		current path segment.
     * @see PathIterator#SEG_MOVETO
     * @see PathIterator#SEG_LINETO
     * @see PathIterator#SEG_CLOSE
     */
    public int currentSegment(double[] coords) {
        if (isDone())
	    throw new NoSuchElementException("Topology iterator out of bounds");

        if(topology.isClosed())
         if(index >= topology.getSize())
          return SEG_CLOSE;

        Vect2D p = topology.getVectAt( index );
        coords[0] = p.x;
        coords[1] = p.y;
        if (transform != null) {
        transform.transform(coords, 0, coords, 0, 1);
        }
        return (index == 0 ? SEG_MOVETO : SEG_LINETO);
    }
    }
}

