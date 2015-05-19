package net.faraya.swing.core.layer.event;

import net.faraya.swing.core.layer.handle.RectHandle;

import java.util.EventObject;
import java.awt.geom.Point2D;

/**
 * User: Fabrizzio
 * Date: 18-Jan-2006
 * Time: 23:55:04
 * To change this template use File | Settings | File Templates.
 */
public class HandleEvent extends EventObject {

    private int id;
    private float x;
    private float y;

    public HandleEvent(RectHandle source, float x, float y ) {
        super( source );
        this.id = source.getId();
        this.x = x;
        this.y = y;
    }

    public Integer getId(){ return this.id;  }

    public  Point2D.Float getPoint(){ return new Point2D.Float( x,y );  }

    public  float getX(){ return x; }

    public  float getY(){ return y; }
}
