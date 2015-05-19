package net.faraya.swing.core.util;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * User: Fabrizzio
 * Date: 03/05/2006
 * Time: 11:21:15 PM
 */
public class ShapeUtils {

     /*
      * See diva.jar For Furhter information
      */


       public static boolean isOrthogonal(AffineTransform at)
       {
           int t = at.getType();
           return (t & 0x38) == 0;
       }

    public static Rectangle2D transformBounds(Rectangle2D rect, AffineTransform at)
    {
        if(at.isIdentity())
            return rect.getBounds2D();
        if(!isOrthogonal(at))
            return at.createTransformedShape(rect).getBounds2D();
        if(rect instanceof Rectangle2D.Double)
        {
            java.awt.geom.Rectangle2D.Double r = (Rectangle2D.Double)rect;
            double coords[] = new double[4];
            coords[0] = r.x;
            coords[1] = r.y;
            coords[2] = r.x + r.width;
            coords[3] = r.y + r.height;
            at.transform(coords, 0, coords, 0, 2);
            return new Rectangle2D.Double(coords[0], coords[1], coords[2] - coords[0], coords[3] - coords[1]);
        }
        if(rect instanceof Rectangle2D.Float)
        {
            Rectangle2D.Float r = (Rectangle2D.Float)rect;
            float coords[] = new float[4];
            coords[0] = r.x;
            coords[1] = r.y;
            coords[2] = r.x + r.width;
            coords[3] = r.y + r.height;
            at.transform(coords, 0, coords, 0, 2);
            return new Rectangle2D.Float(coords[0], coords[1], coords[2] - coords[0], coords[3] - coords[1]);
        } else
        {
            double coords[] = new double[4];
            coords[0] = rect.getX();
            coords[1] = rect.getY();
            coords[2] = coords[0] + rect.getWidth();
            coords[3] = coords[1] + rect.getHeight();
            at.transform(coords, 0, coords, 0, 2);
            return new Rectangle2D.Double(coords[0], coords[1], coords[2] - coords[0], coords[3] - coords[1]);
        }
    }



}
