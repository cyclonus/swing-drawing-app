package net.faraya.swing.core.layer.event;

import java.util.EventListener;

/**
 * User: Fabrizzio
 * Date: 15-Feb-2006
 * Time: 22:06:34
 * To change this template use File | Settings | File Templates.
 */
public interface LayerListener extends EventListener {

    public void layerBeginDrag( LayerEvent e );

    public void layerEndDrag( LayerEvent e );

    public void layerCreated( LayerEvent e );

    public void layerDeleted( LayerEvent e );

    public void layerMoved( LayerEvent e );

    public void layerDragged( LayerEvent e );


}
