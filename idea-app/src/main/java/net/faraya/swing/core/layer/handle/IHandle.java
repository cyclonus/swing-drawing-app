package net.faraya.swing.core.layer.handle;

import java.awt.*;

/**
 * User: Fabrizzio
 * Date: 04-Feb-2006
 * Time: 00:10:39
 * To change this template use File | Settings | File Templates.
 */
public interface IHandle {

    public Dimension getDimension();

    public void paint(Graphics2D g);

    public void addHandleListener( HandleListener l );

    public void removeHandleListener( HandleListener l );

    public void processDragEvent(float x, float y);

    public void setLocation(float x, float y);

    public boolean contains(float x, float y);

    public void setId(int id);

    public int getId();

    public Object clone();
}
