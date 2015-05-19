package net.faraya.swing.core.layer.math;

import net.faraya.swing.core.layer.math.geom.Topology;
import net.faraya.swing.core.IViewerConstants;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.*;

/**
 * User: Fabrizzio
 * Date: 30-Jan-2006
 * Time: 23:23:31
 * To change this template use File | Settings | File Templates.
 */
public class AABB implements Comparable{

    public float xmax, xmin, ymax, ymin;

    public int compareTo(Object o){
       return (int)(xmin-((AABB)o).xmin);
    }

    public Rectangle2D toRect2D(){
      return new Rectangle2D.Double((int)xmin,(int)ymin,(int)(xmax - xmin),(int)(ymax-ymin));
      //TODO: Add missing Validations
    }


    public Rectangle2D getAdjustedRect(){
     final int adjustment = IViewerConstants.BOUNDINGBOX_ADJUSTMENT_VALUE;
     return new Rectangle2D.Double((int)xmin - adjustment ,(int)ymin - adjustment,((int)(xmax - xmin) + adjustment * 2),((int)(ymax-ymin) + adjustment * 2));
    }

    public AABB(  ){

    }

    public AABB computeAABB( Topology polygon ){
    // Calculate the dimension of the 2D model
     final int nvertices = polygon.getSize()-1;
     Vect2D v = polygon.getVectAt( 0 );
     xmax = xmin =  v.x;
     ymax = ymin =  v.y;
        for (int i=0; i <= nvertices; i++) {
          v = polygon.getVectAt( i );
          xmin = Math.min(xmin, v.x);
          ymin = Math.min(ymin, v.y);
          xmax = Math.max(xmax, v.x);
          ymax = Math.max(ymax, v.y);
        }
       return this;
    }

    public boolean contains( Point2D p ){
     return ( p.getX() >= xmin  &&  p.getX() <= xmax && p.getY() >= ymin && p.getY() <= ymax );
    }


    public static AABB computeAABB( Vect2D[] vect ){
     final int nvertices = vect.length-1;
     Vect2D v = vect[0];
     float xmax, xmin, ymax, ymin;
     xmax = xmin =  v.x;
     ymax = ymin =  v.y;
        for (int i=0; i <= nvertices; i++) {
          v = vect[ i ];
          xmin = Math.min(xmin, v.x);
          ymin = Math.min(ymin, v.y);
          xmax = Math.max(xmax, v.x);
          ymax = Math.max(ymax, v.y);
        }

       AABB aabb = new AABB();
       aabb.xmax = xmax;
       aabb.xmin = xmin;
       aabb.ymax = ymax;
       aabb.ymin = ymin;
       return aabb;
    }

    public void paint( Graphics g ){
        g.drawString( new Double( xmin ).toString() , (int)xmin, (int)ymin );
        g.drawRect((int)xmin,(int)ymin,(int)(xmax - xmin),(int)(ymax-ymin));
    }

    public boolean intersectsWith(AABB box){
      return intersect(this, box, null);
    }

    public static boolean intersect(AABB box1, AABB box2, AABB boxIntersect){
     // Check for no overlap

        if (box1.xmin > box2.xmax) return false;
        if (box1.xmax < box2.xmin) return false;
        if (box1.ymin > box2.ymax) return false;
        if (box1.ymax < box2.ymin) return false;

        // We have overlap.  Compute AABB of intersection, if they want it

	if (boxIntersect != null) {
		boxIntersect.xmin = Math.max(box1.xmin, box2.xmin);
		boxIntersect.xmax = Math.min(box1.xmax, box2.xmax);
		boxIntersect.ymin = Math.max(box1.ymin, box2.ymin);
		boxIntersect.ymax = Math.min(box1.ymax, box2.ymax);
	}

	// They intersected

	return true;

    }

    public double rayIntersect(
	Vect2D rayOrg,          // orgin of the ray
	Vect2D rayDelta,        // length and direction of the ray
	Vect2D returnNormal)    // optionally, the normal is returned
    {
     // We'll return this huge number if no intersection
	 final float kNoIntersection = 1e30f;
	 // Check for point inside box, trivial reject, and determine parametric
	 // distance to each front face
	 boolean inside = true;
     double xt, xn;
        if (rayOrg.x < xmin) {
            xt = xmin - rayOrg.x;
            if (xt > rayDelta.x) return kNoIntersection;
            xt /= rayDelta.x;
            inside = false;
            xn = -1.0f;
        } else if (rayOrg.x > xmax) {
            xt = xmax - rayOrg.x;
            if (xt < rayDelta.x) return kNoIntersection;
            xt /= rayDelta.x;
            inside = false;
            xn = 1.0f;
        } else {
            xt = -1.0f;
        }

        double yt, yn;
        if (rayOrg.y < ymin) {
            yt = ymin - rayOrg.y;
            if (yt > rayDelta.y) return kNoIntersection;
            yt /= rayDelta.y;
            inside = false;
            yn = -1.0f;
        } else if (rayOrg.y > ymax) {
            yt = ymax - rayOrg.y;
            if (yt < rayDelta.y) return kNoIntersection;
            yt /= rayDelta.y;
            inside = false;
            yn = 1.0f;
        } else {
            yt = -1.0f;
        }

// Inside box?

        if (inside) {
            if (returnNormal != null) {
                returnNormal.add( -rayDelta.x, -rayDelta.y );
                returnNormal.normalize();
            }
            return 0.0f;
        }

        // Select farthest plane - this is
        // the plane of intersection.

        int which = 0;
        double t = xt;
        if (yt > t) {
            which = 1;
            t = yt;
        }

			double x = rayOrg.x + rayDelta.x*t;
			if (x < xmin || x > xmax) return kNoIntersection;
			double y = rayOrg.y + rayDelta.y*t;
			if (y < ymin || y > ymax) return kNoIntersection;

			if (returnNormal != null) {
				returnNormal.x = 0.0f;
				returnNormal.y = 0.0f;
			}
       // Return parametric point of intersection

	   return t;

    }


    // AABB3::closestPointTo
//
// Return the closest point on this box to another point

   public Vect2D closestPointTo( Vect2D p){

	// "Push" p into the box, on each dimension

	Vect2D r = new Vect2D();

	if (p.x < xmin) {
		r.x = xmin;
	} else if (p.x > xmax) {
		r.x = xmax;
	} else {
		r.x = p.x;
	}

	if (p.y < ymin) {
		r.y = ymin;
	} else if (p.y > ymax) {
		r.y = ymax;
	} else {
		r.y = p.y;
	}

	// Return it

	return r;
 }

   public boolean contains(Vect2D p){

	// Check for overlap on each axis
	return((p.x >= xmin) && (p.x <= xmax) && (p.y >= ymin) && (p.y <= ymax));
    }

    public String toString(){
       return new StringBuffer().append("[xmin=").append(xmin).
               append(" ymin=").append(ymin).
               append(" xmax=").append(xmax).
               append(" ymax=").append(ymax).
               append("]").toString();
    }
}

