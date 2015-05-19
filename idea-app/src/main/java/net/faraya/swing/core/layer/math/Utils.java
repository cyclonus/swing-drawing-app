package net.faraya.swing.core.layer.math;

import java.awt.*;
import java.awt.geom.Point2D;


/**
 * Created by IntelliJ IDEA.
 * User: Fabrizzio
 * Date: 7-Dec-2005
 * Time: 10:58:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    public static boolean IsPointInLine( Point2D p, Point2D p1, Point2D p2, int tolerance){
      return (pointLineSide (p, p1, p2, tolerance) == 0);
    }

   public static int pointLineSide( Point2D p, Point2D p1, Point2D p2, double tolerance ){
        // -1 if left of p1->p2, 1 if right of p1-p2, 0 if on the line
       double d = distPointToLineSigned(p, p1, p2);
       if (d <- tolerance)
        return -1;
       else
         if (d > tolerance)
          return 1;
         else
          return 0;
        }

    public static double distPointToLineSigned(Point2D p, Point2D p1, Point2D p2){
   // P pour le point P1-P2 pour le segment de droite
     double len = Math.sqrt(sqr(p2.getX()-p1.getX())+sqr(p2.getY()-p1.getY()));
     if (len == 0)  // segement length is 0 -> dist = dist from point to point
      return  Math.sqrt(sqr(p.getX()-p1.getX())+sqr(p.getY()-p1.getY()));
      else
       return ((p2.getX()-p1.getX())*(p.getY()-p1.getY())-(p2.getY()-p1.getY())*(p.getX()-p1.getX())) / len;
    }

    private static double sqr(double d){
      return ( d * d );
    }

    private static int sqr(int i){
      return ( i * i );
    }

    public static boolean IsPointInsidePolygon(Point v, Polygon polygon){
     boolean result = false;
     Point P1, P2;   Polygon p;
     if(polygon.npoints <= 2) return false;
     int n = polygon.npoints;
     Point pOld = new Point( polygon.xpoints[n], polygon.xpoints[n] );
      for (int i = 0; i <= n; i++){
         Point pNew = new Point( polygon.xpoints[i], polygon.xpoints[i] );
         if(pNew.x > pOld.x){
           P1 = pOld;
           P2 = pNew;
         }else{
           P1 = pNew;
           P2 = pOld;
         }
        if ((v.x > P1.x) && (v.x <= P2.x) && // straddle test: count right edge,but not left one
         ( (v.y-P1.y)*(P2.x-P1.x) < (P2.y-P1.y)*(v.x-P1.x) ))  // north test
          result = ! result; // one border crossed: if we were outside, then were are inside and vice et verca...
       pOld = pNew;
      }
     return result;
    }


public static int IsPolygonInPolygon(Polygon Polygon1, Polygon Polygon2, int tolerance){
      int [] SegmentPositions = new int[]{0,0,0,0,0,0,0,0};
      int n = Polygon1.npoints;
      Point pOld = new Point( Polygon1.xpoints[n],Polygon1.ypoints[n] );
        for(int i=0; i <= n; i++){
           int q = Polygon2.npoints;
           Point pNew = new Point( Polygon1.xpoints[q],Polygon1.ypoints[q] );
           int SegmentPosition = IsSegmentInPolygon(pOld,pNew,Polygon2,tolerance);
           if (SegmentPosition == POLY_OUTSIDEINSIDE ) {
              return POLY_OUTSIDEINSIDE;
           }
           SegmentPositions[SegmentPosition]++;
           pOld = pNew;
        }
          int SegPosOutside = SegmentPositions[POLY_OUTSIDEINSIDE] + SegmentPositions[POLY_OUTSIDE] +
          SegmentPositions[POLY_OUTSIDE_TOUCH] + SegmentPositions[POLY_OUTSIDE_EDGE];

          int SegPosInside = SegmentPositions[POLY_OUTSIDEINSIDE] + SegmentPositions[POLY_INSIDE] +
              SegmentPositions[POLY_INSIDE_TOUCH] + SegmentPositions[POLY_INSIDE_EDGE];

          if ((SegPosOutside==0) && (SegPosInside==0))
            return POLY_EDGE;
          else if (SegPosOutside==0 )
            {
            if (SegmentPositions[POLY_INSIDE_EDGE] > 0 )
                return POLY_INSIDE_EDGE;
            else if (SegmentPositions[POLY_INSIDE_TOUCH] > 0)
                 return POLY_INSIDE_TOUCH;
                  else
                   return POLY_INSIDE;
            }
           else
            if (SegPosInside == 0 )
            {
             if (SegmentPositions[POLY_OUTSIDE_EDGE] > 0 )
                 return POLY_OUTSIDE_EDGE;
            else if( SegmentPositions[POLY_OUTSIDE_TOUCH] > 0 )
                  return POLY_OUTSIDE_TOUCH;
                 else
                  return POLY_OUTSIDE;
            }
            else //if (SegPosOutside>0) and (SegPosInside>0) then
              return POLY_OUTSIDEINSIDE;
 }

public static boolean IsPointInSegment(Point2D v , Point2D P1, Point2D P2,int tolerance)
// true if point is in segment p1-p2, inclusive edges
{
 return ( ( (P1.getX()<=P2.getX()) && (v.getX()>=P1.getX()-tolerance) && (v.getX()<=P2.getX()+tolerance) ) ||
            ( (P2.getX()<=P1.getX()) && (v.getX()>=P2.getX()-tolerance) && (v.getX()<=P1.getX()+tolerance)
 ) ) &&
           ( ( (P1.getY()<=P2.getY()) && (v.getY()>=P1.getY()-tolerance) && (v.getY()<=P2.getY()+tolerance) ) ||
             ( (P2.getY()<=P1.getY()) && (v.getY()>=P2.getY()-tolerance) && (v.getY()<=P1.getY()+tolerance)
 ) ) &&
           IsPointInLine(v,P1,P2,tolerance);
}

 public static int IsPointInsidePolygon2( Point2D v, Polygon polygon,int tolerance){
  int result = -1;
  Point2D P1, P2;
     if(polygon.npoints <= 2) return result;
     int n = polygon.npoints;
     Point pOld = new Point( polygon.xpoints[n], polygon.xpoints[n] );
     for (int i = 0; i <= n; i++){
       Point pNew = new Point( polygon.xpoints[i], polygon.xpoints[i] );
       if(pNew.x > pOld.x){
           P1 = pOld;
           P2 = pNew;
         }else{
           P1 = pNew;
           P2 = pOld;
         }
        if(IsPointInSegment(v,P1,P2,tolerance)){
         result = 0; break;
        }
        if ((v.getX() > P1.getX()) && (v.getX() <= P2.getX()) && // straddle test: count right edge,but not left one
         ( (v.getY()-P1.getY())*(P2.getX()-P1.getX()) < (P2.getY()-P1.getY())*(v.getX()-P1.getX()) ))  // north test
          result=-result; // one border crossed: if we were outside, then were are inside and vice et verca...
        pOld = pNew;
     }
  return result;
 }

private static boolean PointsEqual(Point2D P1,Point2D P2, int tolerance){

  return (Math.abs(P2.getX()-P1.getX()) <= tolerance) && (Math.abs(P2.getY()-P1.getY()) <= tolerance);

}

public static  Point2D middlePoint(Point2D P1, Point2D P2)
{
  return new Point.Float( ((float)(P1.getX()+P2.getY()) / 2), ((float)(P1.getY()+P2.getY()) / 2 ));
}

 private static boolean between(double a, double a1, double a2, double tolerance){
  double inf,sup;
  if (a1 > a2) { inf = a2; sup = a1; }
   else { inf = a1; sup = a2; }
     return (a >= inf-tolerance) && (a <= sup+tolerance);
 }

 public static boolean LineIntersection(Point2D A1,Point2D A2,Point2D B1,Point2D B2,
                                        Point IntersectionPoint, boolean InSegments, boolean InSegmentA,boolean InSegmentB,
                                        double tolerance){
   double dx1, dx2, dy1, dy2, det, k1, k2, x, y;

   dx1 = A2.getX()-A1.getX();
   dy1 = A2.getY()-A1.getY();
   dx2 = B2.getX()-B1.getX();
   dy2 = B2.getY()-B1.getY();
   det = dx1*dy2-dx2*dy1;
   boolean Result = Math.abs(det) > tolerance;
   if (Result)  { // lines not parallel
     k1=A1.getX()*dy1-A1.getY()*dx1;
     k2=B1.getX()*dy2-B1.getY()*dx2;
     x=(dx1*k2-dx2*k1)/det;
     y=(dy1*k2-dy2*k1)/det;

       IntersectionPoint = new Point((int)Math.round(x),(int)Math.round(y));
       InSegmentA = between( x, A1.getX(), A2.getX() ,tolerance) && between( y, A1.getY(), A2.getY() ,tolerance);
       InSegmentB = between( x, B1.getX(), B2.getX() ,tolerance) && between( y, B1.getY(), B2.getY() ,tolerance);
   }else{
     InSegmentA = false;
     InSegmentB = false;
   }
  InSegments = InSegmentA && InSegmentB;
  return Result;
 }

  public static int IsSegmentInPolygon(Point2D P1,Point2D P2, Polygon polygon, int tolerance){
     int InsideP1 = IsPointInsidePolygon2(P1,polygon,tolerance);
     int InsideP2 = IsPointInsidePolygon2(P2,polygon,tolerance);
     boolean InSegment, InSegmentA, InSegmentB;
     InSegment = InSegmentA = InSegmentB = false;
     Point IntersectionPoint = new Point();
     if ((InsideP1 != 0 ) && (InsideP1 == -InsideP2))
      return POLY_OUTSIDEINSIDE;
     int n = polygon.npoints;
     Point pOld = new Point( polygon.xpoints[n], polygon.xpoints[n] );
     for (int i = 0; i <= n; i++){
       Point pNew = new Point( polygon.xpoints[i], polygon.xpoints[i] );
       boolean Intersect = LineIntersection(P1,P2, pOld,pNew, IntersectionPoint,
                                            InSegment, InSegmentA, InSegmentB, tolerance);
        if((Intersect && InSegment) &&
         !PointsEqual(P1,IntersectionPoint,tolerance) &&
         !PointsEqual(P2,IntersectionPoint,tolerance) &&
         !PointsEqual(pOld,IntersectionPoint,tolerance) &&
         !PointsEqual(pNew,IntersectionPoint,tolerance)){
         return POLY_OUTSIDEINSIDE;
        }
      pOld = pNew;
     }
     // arrive here only if not inside and outside -> complete inside, complete outside or touch edge
     if ((InsideP1==0) && (InsideP2==0)) {
      Point2D M = middlePoint(P1,P2);
      int InsideM = IsPointInsidePolygon2(M, polygon, tolerance);
      if (InsideM == 1) { return POLY_INSIDE_TOUCH; }
      else if (InsideM == -1 ){ return POLY_OUTSIDE_TOUCH; }
            else return POLY_EDGE; // this is wrong, but enough for the actual purpose
     }else{
       if ((InsideP1 == 0) && (InsideP2 == 1) || (InsideP1 == 1) && (InsideP2 == 0))
         return POLY_INSIDE_TOUCH;
       else if ((InsideP1==0) && (InsideP2 ==-1) || (InsideP1 == -1) && (InsideP2 == 0))
        return POLY_OUTSIDE_TOUCH;
       else if ((InsideP1 == 1) && (InsideP2 == 1))
         return POLY_INSIDE;
       else if ((InsideP1 == -1) && (InsideP2 == -1))
         return POLY_OUTSIDE;
       else
        return POLY_OUTSIDEINSIDE;
     }
  }

   public static final int POLY_OUTSIDEINSIDE = 0;
   public static final int POLY_OUTSIDE = 1;
   public static final int POLY_OUTSIDE_TOUCH = 2;
   public static final int POLY_OUTSIDE_EDGE  = 3;
   public static final int POLY_EDGE = 4;
   public static final int POLY_INSIDE_EDGE = 4;
   public static final int POLY_INSIDE_TOUCH = 5;
   public static final int POLY_INSIDE = 6;


   public static double pitagoras( Vect2D p1, Vect2D p2 ){
     double y = Math.pow(p2.y  - p1.y, 2); //Alto
     double x = Math.pow(p2.x  - p1.x, 2); //Ancho
     double d = (Math.sqrt( x + y ));      //Diagonal
     return (d == 0 ? 1 : d );
   }

   public static double computeN(Vect2D p1, Vect2D p2, int separation){
      double y = Math.pow(p2.y  - p1.y ,2);  //Alto
      double x = Math.pow(p2.x  - p1.x, 2);  //Ancho
      double d = (Math.sqrt( x + y ));       //Diagonal
      d = (d == 0  ? 1 : d );
      return (separation / (d * 2));
   }

   public static double computeWX(Vect2D p1, Vect2D p2, double n){
      return  n * (p2.y - p1.y);
   }

   public static double computeWY(Vect2D p1, Vect2D p2, double n){
     return  n * (p2.x  - p1.x);
   }



}
