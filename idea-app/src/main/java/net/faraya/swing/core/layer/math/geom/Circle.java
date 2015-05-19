package net.faraya.swing.core.layer.math.geom;

import java.awt.geom.Ellipse2D;

/**
 * User: Fabrizzio
 * Date: 04-Feb-2006
 * Time: 00:17:58
 * To change this template use File | Settings | File Templates.
 */
class Circle extends Ellipse2D.Double {
  public Circle(double centerX, double centerY, double radius) {
    super(centerX - radius, centerY - radius, 2.0*radius, 2.0*radius);
  }
}
