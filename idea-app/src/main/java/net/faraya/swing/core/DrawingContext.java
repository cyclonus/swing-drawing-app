package net.faraya.swing.core;

import net.faraya.swing.core.layer.AbstractLayer;
import net.faraya.swing.core.layer.PolyLineLayer;
import net.faraya.swing.core.layer.handle.IHandle;
import net.faraya.swing.core.layer.math.geom.Topology;
import net.faraya.swing.core.layer.math.Vect2D;
import net.faraya.swing.core.layer.undo.AddEdit;
import net.faraya.swing.core.layer.undo.RemoveEdit;
import net.faraya.swing.core.layer.undo.TranslateEdit;
import net.faraya.swing.core.util.tree.QuadTree;
import net.faraya.swing.core.util.ShapeUtils;

import javax.swing.*;
import javax.swing.event.UndoableEditListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.io.Serializable;



/**
 * User: Fabrizzio
 * Date: 4-Dec-2005
 * Time: 10:49:50 PM
 */
public class DrawingContext extends JComponent implements
                                               Serializable,
                                               IContext,
                                               MouseListener,
                                               MouseMotionListener,
                                               MouseWheelListener,
                                               UndoableEditListener
{
   // protected static Logger log = Logger.getLogger( DrawingContext.class );

    private static final long serialVersionUID = -6999410423251662622L;

    private BufferedImage offBuffer = null;

    private GuideGrid guideGrid = new GuideGrid();

    private double scale = IViewerConstants.UNIT_SCALE;

    private double zoomPercentage = IViewerConstants.ZOOM_PERCENTAGE / 100D;

    private boolean repaintAll = false;

    private ArrayList <AbstractLayer> selectedLayers;

    private Collection <ContextListener> contextListeners;

    private QuadTree qtree;

    private AffineTransform at;

    private Class currentLayerClass;

    public Collection <AbstractLayer> getLayers(){ return qtree.getAll(); }

    private Point2D mousePos;

    private int addLayer(AbstractLayer layer){
        layer.setContext( this );
        // record the effect
        undoSupport.postEdit( new AddEdit(this, layer) );
        return 0;
    }

    public void setCurrentLayerClass(Class clss){
     currentLayerClass = clss;
     polyLine = null;
    }

    public DrawingContext(  ){
      Dimension sz = getDrawingAreaSize();
      selectedLayers = new ArrayList <AbstractLayer> ();
      qtree = new QuadTree( 3, new Rectangle( sz.width, sz.height ));
      contextListeners = new ArrayList <ContextListener>();
      //setDebugGraphicsOptions( DebugGraphics.LOG_OPTION );
      at = new AffineTransform();
      at.scale( scale, scale );
      mousePos = new Point();
      setOpaque( false );
      //setDoubleBuffered( false ); should this be turned off cuz  made double buffer by my self
      addMouseListener( this );
      addMouseMotionListener( this );
      addMouseWheelListener( this );
      addUndoableEditListener( this );
    }


   public Dimension getPreferredSize() {

      Dimension d = getDrawingAreaSize();

      return new Dimension((int)((d.getWidth() + (d.getWidth() * (scale - 1)))),
                           (int)((d.getHeight() + (d.getHeight() * (scale -1 )))));

   }



   public void paintComponent(Graphics g){
         super.paintComponent( g );
         Graphics2D gd = (Graphics2D) g;
         /*
          * Its ok scaling Graphics created by the offscreenbuffer
          * but not the component graphics that would only cause image degeneration
          */
         if ( offBuffer == null ) {
            initBuffers( gd );
         }else{
           paintLayers( gd );
         }

         gd.drawImage( offBuffer, 0, 0, this);
         gd.dispose();
   }

   private Dimension getDrawingAreaSize(){ return new Dimension( 1024, 768 ); }

   private void initBuffers(Graphics2D g){

     Dimension sz = getDrawingAreaSize();
     offBuffer = (BufferedImage)createImage( sz.width * 2, sz.height * 2 );
     Graphics2D bg = offBuffer.createGraphics( );
     bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
     bg.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
     // Cleans up the image background
     bg.setPaint( Color.gray );
     bg.fill( new Rectangle( sz.width, sz.height));
     bg.scale( scale, scale );
     Rectangle clip = new Rectangle( sz.width, sz.height );
     bg.fill( clip );
     bg.setClip( clip );
     bg.setPaint( getBackground() );
     guideGrid.paint( bg, true );
     qtree.paint( bg );
     bg.dispose();
   } 

   private void paintLayers(Graphics2D g){
     if( repaintAll ){
       Dimension sz = getDrawingAreaSize();
       Rectangle2D clip = new Rectangle( sz.width * 2, sz.height * 2);
       // the whole buffer Size should be used as clipping rect, so it will paint even the areas that are not visible (scrollbars)
       Graphics2D bg = offBuffer.createGraphics();
       bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       bg.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
       // Cleans up the image background
       // Dimension sz = getDrawingAreaSize();
       bg.setPaint( Color.gray );
       bg.fill( new Rectangle( sz ));

       bg.scale( scale, scale );
       bg.setClip( clip );
       bg.setPaint( getBackground() );

       guideGrid.paint( bg, true );
       qtree.paint( bg );
       qtree.paintLayes( bg );
       bg.dispose();
       repaintAll = false;
     }
   }

   private void repaintAll(){
     repaintAll = true;
     repaint();
   }

   public void paint( AbstractLayer layer ){
       // TODO: Handle Intersections
       Graphics2D g = (Graphics2D)getGraphics();
      // g.scale( scale , scale );
       // prepare to render
       Graphics2D bg = (Graphics2D)offBuffer.createGraphics();
       bg.scale( scale , scale );
       Rectangle2D rect = layer.getAABB().getAdjustedRect();
       bg.setClip( rect );
       bg.setPaint( getBackground() );
       bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       bg.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
       // first recreate previous content at the BB where the layer was located

       guideGrid.paint( bg,true );
       qtree.paint( bg );       
       List<AbstractLayer> intersections = qtree.getAllWithin( rect );
       for( AbstractLayer l : intersections ){
         if( l != layer ){
           bg.setColor( l.getColor() );
           l.paint( bg );
         }
       }

       // update the layer's BB where it actually lays
       rect = layer.updateBounds().getAdjustedRect();
       // update Clipping area
       bg.setClip( rect );
       // and finally paint
       layer.paint( bg );
       // release context and swap buffers
       bg.dispose();
       g.drawImage( offBuffer, 0, 0, this);
       g.dispose();

   }


   public AbstractLayer newLayer( Class clazz ){
      try{
        AbstractLayer layer = (AbstractLayer)clazz.newInstance();
        int i = addLayer( layer );
        layer.setName(layer.getClass().getName() + "-" + i );
        return layer;
      }catch(Exception e){
        e.printStackTrace();
      }
      return null;
   }

   public AbstractLayer newLayer( ){
     return (currentLayerClass == null ?  null :  newLayer( currentLayerClass ) );
   }

   public void removeSelection(){
       qtree.removeAll(  selectedLayers );
       undoSupport.postEdit( new RemoveEdit(this, selectedLayers ) );
       repaint( );
   }

   public void mouseMoved(MouseEvent e){

        mousePos.setLocation( e.getX()  , e.getY() );
        try{
          mousePos = pointToView( mousePos );
          if( mouseLabel != null ){
            String value = new StringBuilder(" Component (").append((int)e.getX()).append(",").append((int)e.getY()).append(")").
                      append(" View (").append((int)mousePos.getX()).append(",").append((int)mousePos.getY()).append(")").
                      append(" scale  ").append( scale ).append(" zoom ").append( getZoomedTo() ).append(" % ").
                      toString();
            mouseLabel.setText( value );
          }
        }catch(Exception ex){
          ex.printStackTrace();
        }

        if( polyLine != null ){
           polyLine.setLastVect(e.getX(), e.getY(), false);
           paint( polyLine );
        }
        if(selectedLayers != null){
           for( AbstractLayer l : selectedLayers ){
             IHandle h = l.getTopology().getSelectedHandle( mousePos );
             if( h != null){
               setCursor( Cursor.getPredefinedCursor( Cursor.MOVE_CURSOR ));
               break;
             }else{
               setCursor( Cursor.getDefaultCursor() );
             }
           }
        }
   }

   private IHandle handle = null;
   private AbstractLayer layer = null;
   private PolyLineLayer polyLine = null;
   private boolean dragging = false;
   private JLabel mouseLabel = null;

   public void setMouseLabel(JLabel value){
     mouseLabel = value;
   }

   public void mousePressed(MouseEvent e){
     Point2D p = e.getPoint(); 
     p = pointToView( p );       
     if ( polyLine != null ) {
      updateQuadTree( polyLine );   
      if( e.getButton() == MouseEvent.BUTTON1 )
      {
        snap( p );
        polyLine.setLastVect((float)p.getX()  , (float)p.getY(), false); // updates the last point        
        polyLine.setLastVect((float)p.getX()  , (float)p.getY(), true);  // adds up a new one

         //Put this into an InvokeLater
         tmp = polyLine.getTopology();
         undoSupport.postEdit( new TranslateEdit(this, tmp, polyLine) ); //Save Undo,Redo Object

      }else
      {
        polyLine = null;
      }
      return;
     }

     /**
      * if a Layer Class is Set ( != null ) then create an instance of it
      */
     if(( layer = newLayer() ) != null )
     {
       snap( p );
       layer.addVect((float)p.getX(),(float)p.getY(), true);       
       layer.addVect((float)p.getX() + 1 ,(float)p.getY() + 1, true);
       layer.setSelected( true );
     }

     if((layer != null)){
      layer.setLastMouseLocation( p );
      handle = layer.getTopology().getSelectedHandle( p );
      return;
     }

      List <AbstractLayer>layers = qtree.getAllAt( p );
      if(!layers.isEmpty()){
        for (AbstractLayer l : layers) {
           if (l.doHitTest( p )) {
              layer = l;
              handle = layer.getTopology().getSelectedHandle( p );
              layer.setLastMouseLocation( p );
              performSelection( e.isControlDown(), layer );
              if( e.isControlDown( ) ) // when ctrl is pressed then multi dragging is on
               break;
           }   // TODO: missing repaint
        }
      }
   }


   public void mouseDragged(MouseEvent e){ // TODO The Rectangle needs to updated on the qTree
     Point2D p = e.getPoint();
     p = pointToView( p );
        if( layer != null ){
          if(!dragging){
           // On Begin Drag
            tmp = new Topology( layer.getTopology() ); // Save a copy for Undo,Redo
            dragging = true;
          }

          if( handle != null ){
              handle.processDragEvent( (float)p.getX(), (float)p.getY() );
              layer.setLastMouseLocation( p );
              paint( layer );
              System.out.println("HID:" + handle.getId());
              return;
          }else{
            layer.processDragEvent( (float)p.getX(), (float)p.getY() );
          }
          layer.setLastMouseLocation( p );
          paint( layer );
        }
   }

   public void mouseReleased(MouseEvent e){
       if( layer != null && dragging )
       {
         if(handle != null)
         {
          Vect2D p = layer.getTopology().getVectAt( handle.getId() );
          snap( p );
           IHandle h = layer.getTopology().getHandle( p );
           if( h!= null )
             h.setLocation((float)p.x,(float)p.y);
         }else{
           snap( layer );
         }
        // end dragging Layer
        // TODO: Do Something to preserve correctly the Selection List
        paint( layer );
        undoSupport.postEdit( new TranslateEdit(this, tmp, layer) ); //Save Undo,Redo Object
        updateQuadTree( layer );

         if((layer instanceof PolyLineLayer)&&(currentLayerClass!=null))
         {
           polyLine = (PolyLineLayer)layer;  // We Created a instance of PolyLine
         }
       }
       dragging = false;
       layer  = null;
       handle = null;
   }

   public void mouseClicked(MouseEvent e){}

   public void mouseEntered(MouseEvent e){}

   public void mouseExited(MouseEvent e){}

   public void mouseWheelMoved(MouseWheelEvent e){

       if (e.getWheelRotation() == 1) {
            zoomOut();
        } else if (e.getWheelRotation() == -1) {
            zoomIn();
        }
   }

   public void setScale( double d ){
     scale = d;
     at.setToScale( scale, scale );
     revalidate();
     repaintAll();
     fireContextScaledEvent();
   }

   public double getScale(){ return scale; }

  /**
   * This method set the image to the original size
   * by setting the zoom factor to 1. i.e. 100%
   */
    public void originalSize(){
        setScale( 1.0 );
    }

    /**
     * This method increments the zoom factor with
     * the zoom percentage, to create the zoom in effect
     */
    public void zoomIn(){
       double d = scale;
        d += zoomPercentage;
        setScale( d );
        //TODO: IMPEDIR Q EL ZOOM CREZCA MAS ALLA DE LO DETERMINADO POR ESTA RELACION
        // Dimension sz = getDrawingAreaSize();
        // offBuffer = (BufferedImage)createImage( sz.width * 2, sz.height * 2 );
        // En este caso solo se puede permitir un zoom del 200%
    }

   /**
    * This method decrements the zoom factor with the
    * zoom percentage, to create the zoom out effect
    */
    public void zoomOut(){
        double d = scale;
        d -= zoomPercentage;
        /*
        if(d < zoomPercentage){
            if(zoomPercentage > 1.0){
                d = 1.0;
            }else{
                zoomIn();
                return;
            }
        } */
       setScale( d );
    }

    public void zoomTo( int percentage ){
           double d = ( percentage / 100D );
           setScale( d );
    }

    /**
     * This method returns the currently
     * zoomed percentage
     *
     * @return amount
     */
    public double getZoomedTo()
    {
        return scale * 100;
    }


   public synchronized void addContextListener( ContextListener l ){
      contextListeners.add( l );
   }

   public synchronized void removeContextListener( ContextListener l ){
      contextListeners.remove( l );
   }

   private void fireContextScaledEvent(){
    ContextEvent e = new ContextEvent( this , scale );
       for (ContextListener contextListener : contextListeners) {
           contextListener.contextScaled(e);
       }
   }

   private void fireContextSelectionEvent(){
    ContextEvent e = new ContextEvent( this , layer );
       for (ContextListener contextListener : contextListeners) {
           contextListener.layerSelected(e);
       }
   }

   private void updateQuadTree(AbstractLayer layer){
     if(qtree.contains( layer ))
         qtree.remove( layer );
      qtree.add( layer, layer.getAABB().getAdjustedRect() );
   }

    public void snap( AbstractLayer layer ){
        Topology t = layer.getTopology();
        for (Vect2D p : t.getDragableVects()) {
            IHandle h = t.getHandle( p );
            snap( p );
            h.setLocation((float) p.x, (float) p.y);
        }
    }

   public void snap(Point2D p){ guideGrid.snap( p ); }

   public Point2D pointToView( Point2D p ){
     try{
       final AffineTransform viewBoxTranfrom = at.createInverse();
       return convertPoint( p, viewBoxTranfrom );
     }catch(NoninvertibleTransformException e){
       e.printStackTrace();
     }
      return null;
   }

   private static Point2D convertPoint(Point2D point, AffineTransform  viewBoxTranfrom) {
     double[] matrix = new double [6] ;
     viewBoxTranfrom.getMatrix( matrix );
     return convertPoint( point, matrix );
   }

   private static Point2D convertPoint(Point2D point, double[] matrix) {
      Point2D.Float retP = new Point2D.Float();
      retP.x = (float) (matrix[0] * point.getX() + matrix[2] * point.getY() + matrix[4]);
      retP.y = (float) (matrix[1] * point.getX() + matrix[3] * point.getY() + matrix[5]);
      return retP;
   }

   private Rectangle2D transformBounds(Rectangle2D rect){
      return ShapeUtils.transformBounds(rect, at);
   }

   private void performSelection( boolean multiSelect , AbstractLayer layer ){
      layer.setSelected( true );
      paint( layer );  //
      if( multiSelect ){
        if(!selectedLayers.contains( layer ))
         selectedLayers.add( layer );
         return;
      } // deselect the rest
      Iterator i = selectedLayers.iterator();
      while(i.hasNext()){
          AbstractLayer l = (AbstractLayer)i.next();
          l.setSelected( false );
          i.remove();
      }
     selectedLayers.add( layer );
     fireContextSelectionEvent();
   }


   public Collection <AbstractLayer> getSelectedLayers(){ return selectedLayers; }

   //-------------------------------------------------------------------------------------------------------------------

  /**
   * undo redo support implementation   
   */

   public void undo(){
      undoManager.undo();
      repaintAll();
   }

   public void redo(){
      undoManager.redo();
      repaintAll();
   }

   public boolean canUndo(){ return undoManager.canUndo(); }

   public boolean canRedo(){ return undoManager.canRedo(); }

   public void addUndoableEditListener(UndoableEditListener listener) {
	  undoSupport.addUndoableEditListener( listener );
   }

   public void removeUndoableEditListener(UndoableEditListener listener){
      undoSupport.removeUndoableEditListener( listener );
   }

   private UndoManager undoManager = new UndoManager(); // history list

   private UndoableEditSupport undoSupport = new UndoableEditSupport( );  // event support

 /**
  * An undo/redo adpater. The adpater is notified when
  * an undo edit occur(e.g. add or remove from the list)
  * The adptor extract the edit from the event, add it
  * to the UndoManager, and refresh the GUI
  */

   public void undoableEditHappened (UndoableEditEvent evt) {
    UndoableEdit edit = evt.getEdit();
    undoManager.addEdit( edit );
   }

   private Topology tmp;


}
