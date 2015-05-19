package net.faraya.swing.core.layer.math;

/**
 * User: Fabrizzio
 * Date: 05-Jan-2006
 * Time: 21:35:13
 * To change this template use File | Settings | File Templates.
 */
public class VMath {
        public static double square( double x ) { return x * x; }

        public static double distance( double x, double y, double ax, double ay ) {
                return Math.sqrt( square( x - ax ) + square( y - ay ) );
        }


        // Inputs: plane origin, plane normal, ray origin ray vector.
        // NOTE: both vectors are assumed to be normalized
        //
        // u = N * ( pO - rO )
        //     ---------------
        //     N * ( rV )

        public static double intersect( Vect2D pOrigin, Vect2D pNormal, Vect2D rOrigin, Vect2D rVector )
        {

                Vect2D mod_pOrigin = new Vect2D( pOrigin.x - rOrigin.x,
                                                 pOrigin.y - rOrigin.y );

                double numer = pNormal.dotProduct( mod_pOrigin );
                double denom = pNormal.dotProduct( rVector );

                return numer / denom;
        }
}

