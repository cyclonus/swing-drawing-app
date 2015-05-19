package net.faraya.swing.core.layer.undo;

import net.faraya.swing.core.layer.AbstractLayer;
import net.faraya.swing.core.IContext;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;

/**
 * User: Fabrizzio
 * Date: 15-Feb-2006
 * Time: 22:37:49
 * To change this template use File | Settings | File Templates.
 */
public class AddEdit extends AbstractUndoableEdit {

  private IContext ctx;
  private AbstractLayer src;

  public AddEdit(IContext ctx, AbstractLayer src ){
   this.ctx = ctx;
   this.src = src;
  }

  public void undo() throws CannotUndoException {
    this.ctx.getLayers().remove( src );
    this.ctx.repaint();
  }

  public void redo() throws CannotRedoException {
     this.ctx.getLayers().add( src );
     this.ctx.repaint();
  }

  public boolean canUndo() {
     return true;
  }

  public boolean canRedo() {
     return true;
  }

  public String getPresentationName() { return "Add"; }

}
