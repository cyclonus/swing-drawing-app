package net.faraya.swing.core.layer.event;
import java.awt.*;

/**
 * User: Fabrizzio
 * Date: 01-Jan-2006
 * Time: 22:23:08
 * To change this template use File | Settings | File Templates.
 */
public class LayerEvent extends AWTEvent {
    /**
     * Action Event Suppport
     * Allows the use of Action Listeners
     */

    public static final int LAYER_EVENT = AWTEvent.RESERVED_ID_MAX + 1800;

    public static final int LAYER_CREATED             = LAYER_EVENT + 1;
    public static final int LAYER_SELECTED            = LAYER_EVENT + 2;
    public static final int LAYER_DESELECT_LAYERS     = LAYER_EVENT + 3;
    public static final int LAYER_RESIZED             = LAYER_EVENT + 4;
    public static final int LAYER_BEGIN_DRAG          = LAYER_EVENT + 5;
    public static final int LAYER_END_DRAG            = LAYER_EVENT + 6;
    public static final int LAYER_MOVED               = LAYER_EVENT + 7;
    public static final int LAYER_DELETED             = LAYER_EVENT + 8;
    public static final int LAYER_TRANSLATE_SELECTION = LAYER_EVENT + 9;

    public LayerEvent(Object source, int id) {
        super( source, id);
    }


}
