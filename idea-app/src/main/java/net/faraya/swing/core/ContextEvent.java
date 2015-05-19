package net.faraya.swing.core;

import net.faraya.swing.core.layer.AbstractLayer;

import java.awt.*;

/**
 * User: Fabrizzio
 * Date: 09-Jan-2006
 * Time: 22:16:09
 * To change this template use File | Settings | File Templates.
 */
public class ContextEvent extends AWTEvent {

   public static final int CONTEXT_EVENT = AWTEvent.RESERVED_ID_MAX + 1200;

   public static final int CONTEXT_SCALED = CONTEXT_EVENT + 1;

   public static final int CONTEXT_LAYER_SELECTED = CONTEXT_EVENT + 2;

   private double scale = 1.0;

   private AbstractLayer layer;

   public ContextEvent(Object source, double scale ) {
       super( source, CONTEXT_SCALED );
       this.scale = scale;
   }

   public ContextEvent(Object source, AbstractLayer layer) {
       super( source, CONTEXT_LAYER_SELECTED );
       this.layer = layer;
   }

   public AbstractLayer getSelectedLayer(){ return layer; } 

   public double getScale(){ return scale; }
}
