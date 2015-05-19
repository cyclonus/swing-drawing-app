package net.faraya.swing.core;

import net.faraya.swing.core.layer.AbstractLayer;
import java.util.Collection;
import java.awt.geom.Point2D;

/**
 * User: Fabrizzio
 * Date: 08-Jan-2006
 * Time: 13:57:34
 * To change this template use File | Settings | File Templates.
 */
public interface IContext {

    public Collection<AbstractLayer> getLayers();

    public void removeSelection();

    public AbstractLayer newLayer( Class clss );

    public AbstractLayer newLayer( );

    public Collection <AbstractLayer> getSelectedLayers();

    public void paint( AbstractLayer layer );

    public void addContextListener( ContextListener l );

    public void removeContextListener( ContextListener l );

    public double getScale();

    public void zoomTo( int percentage );

    public Point2D pointToView( Point2D p );

    public void snap(Point2D p);

    public void snap( AbstractLayer layer );

    public void undo();

    public void redo();

    public boolean canUndo();

    public boolean canRedo();

    public void repaint();    

    public void setCurrentLayerClass(Class clss);

}
