package net.faraya.swing.core.layer.math;

import java.awt.geom.Point2D;

/**
 * User: Fabrizzio
 * Date: 05-Jan-2006
 * Time: 21:33:47
 * To change this template use File | Settings | File Templates.
 */
public class Vect2D extends Point2D.Float {
       // public double x = 50;
       // public double y = 100;

        public double distance( Vect2D other ) {
                return distance( other.x, other.y );
        }

        public double distance( double px, double py ) {
                return VMath.distance( x, y, px, py );
        }

        public void setPos( java.awt.Point p ) {
                x = p.x;
                y = p.y;
        }

        public void setPos( Vect2D v ) {
                setPos( v.x, v.y );
        }

        public void setPos( double px, double py ) {
               // x = px;
               // y = py;
               setLocation(px,py);
        }

        public void add( double dx, double dy ) {
                x += dx;
                y += dy;
        }

        public Vect2D() {

        }

        public Vect2D( Vect2D v ) {
                setPos( v );
        }

        public Vect2D( Vect2D a, Vect2D b ) {
                x = a.x + b.x;
                y = a.y + b.y;
        }
        public Vect2D( double a, double b ) {
                setPos( a, b );
        }

        public String toString() {
                return "[" + x + ", " + y + "]";
        }

        public boolean equals( Object o ) {
                if ( o instanceof Vect2D ) {
                        Vect2D other = (Vect2D) o;
                        return ( other.x == x && other.y == y );
                }
                return super.equals( o );
        }

        public double dotProduct( Vect2D v ) {
                return v.x * x + v.y * y;
        }

        public double getLength() {
                return Math.sqrt( x * x + y * y );
        }

        public Vect2D normalize(){
            double l = getLength();
            return new Vect2D( x/l , y/l );
        }


        public int hashCode() {
          return hashcode.intValue();
        }        
        
        private Integer hashcode = new Integer(  (int)(System.currentTimeMillis()  * 1024) >> 10  );

       /*
        # "Whenever it is invoked on the same object more than once during an execution of a Java application,
          the hashCode() method must consistently return the same integer,
          provided no information used in equals comparisons on the object is modified.
          This integer need not remain consistent from one execution of an application to another execution of the same application.
        # If two objects are equal according to the equals(Object) method,
          then calling the hashCode() method on each of the two objects must produce the same integer result.
        # It is not required that if two objects are unequal according to the equals(java.lang.Object method,
          then calling the hashCode() method on each of the two objects must produce distinct integer results. However, the programmer should be aware that producing distinct integer results for unequal objects may improve the performance of hashtables."
        */

        /*
         According to the equals() Javadoc, the method must conform to the following rules:
         "The equals() method implements an equivalence relation:
        * It is reflexive: For any reference value x, x.equals(x) should return true
        * It is symmetric: For any reference values x and y, x.equals(y) should return true if and only if y.equals(x) returns true
        * It is transitive: For any reference values x, y, and z, if x.equals(y) returns true and y.equals(z) returns true, then x.equals(z) should return true
        * It is consistent: For any reference values x and y, multiple invocations of x.equals(y) consistently return true or consistently return false, provided no information used in equals comparisons on the object is modified
        * For any non-null reference value x, x.equals(null) should return false"
        */

}

