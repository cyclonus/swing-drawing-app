package net.faraya.swing.core.layer.undo;

import net.faraya.swing.core.IContext;
import net.faraya.swing.core.layer.AbstractLayer;
import net.faraya.swing.core.layer.math.geom.Topology;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;

/**
 * User: Fabrizzio
 * Date: 15-Feb-2006
 * Time: 23:54:37
 * To change this template use File | Settings | File Templates.
 */
public class TranslateEdit  extends AbstractUndoableEdit {

    private IContext ctx;
    private Topology vertex1;
    private Topology vertex2;
    private AbstractLayer src;

    public TranslateEdit( IContext ctx, Topology vertex1 ,AbstractLayer l ){
       this.ctx = ctx;
       this.src = l;
       this.vertex1 = vertex1 ;
       this.vertex2 = new Topology( l.getTopology() );
    }

    public void undo() throws CannotUndoException {
        src.setTopology( this.vertex1 );
        ctx.snap( src );
        ctx.repaint();
      }

      public void redo() throws CannotRedoException {
        src.setTopology( this.vertex2 );
        ctx.snap( src );
        ctx.repaint();
      }

      public boolean canUndo() {
         return true;
      }

      public boolean canRedo() {
         return true;
      }

      public String getPresentationName() { return "Translate"; }


}
