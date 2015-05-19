package net.faraya.swing.core.util.tree;

/*
 * @(#)QuadTree.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	 by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */





import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.awt.geom.Area;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.io.Serializable;

import net.faraya.swing.core.layer.AbstractLayer;
import net.faraya.swing.core.layer.math.geom.Polygonal;

/**
 * @author WMG (INIT Copyright (C) 2000 All rights reserved)
 * @version <$CURRENT_VERSION$>
 */
public class QuadTree implements Serializable {



    //_________________________________________________________VARIABLES

    protected Rectangle2D _absoluteBoundingRectangle2D = new Rectangle2D.Double();
    protected int         _nMaxTreeDepth;
    protected Hashtable <AbstractLayer, Rectangle2D>  _theHashtable = new Hashtable <AbstractLayer, Rectangle2D> ();
    protected Hashtable <AbstractLayer, Rectangle2D>  _outsideHashtable = new Hashtable <AbstractLayer, Rectangle2D>();
    protected QuadTree  _nwQuadTree;
    protected QuadTree  _neQuadTree;
    protected QuadTree  _swQuadTree;
    protected QuadTree  _seQuadTree;

    //______________________________________________________CONSTRUCTORS

    public QuadTree(Rectangle2D absoluteBoundingRectangle2D) {
        this(2, absoluteBoundingRectangle2D);
    }

    public QuadTree(int nMaxTreeDepth, Rectangle2D absoluteBoundingRectangle2D) {
        _init(nMaxTreeDepth, absoluteBoundingRectangle2D);
    }

    //____________________________________________________PUBLIC METHODS

    public void add(AbstractLayer anObject, Rectangle2D absoluteBoundingRectangle2D) {
        if (_nMaxTreeDepth == 1) {
            if (absoluteBoundingRectangle2D.intersects(_absoluteBoundingRectangle2D)) {
                _theHashtable.put(anObject, absoluteBoundingRectangle2D);
            }
            else {
                _outsideHashtable.put(anObject, absoluteBoundingRectangle2D);
            }
            return;
        }

        boolean bNW = absoluteBoundingRectangle2D.intersects(
            _nwQuadTree.getAbsoluteBoundingRectangle2D());

        boolean bNE = absoluteBoundingRectangle2D.intersects(
            _neQuadTree.getAbsoluteBoundingRectangle2D());

        boolean bSW = absoluteBoundingRectangle2D.intersects(
            _swQuadTree.getAbsoluteBoundingRectangle2D());

        boolean bSE = absoluteBoundingRectangle2D.intersects(
            _seQuadTree.getAbsoluteBoundingRectangle2D());

        int nCount = 0;

        if ( bNW ) {
            nCount++;
        }
        if ( bNE ) {
            nCount++;
        }
        if ( bSW ) {
            nCount++;
        }
        if ( bSE ) {
            nCount++;
        }

        if (nCount > 1) {
            _theHashtable.put(anObject, absoluteBoundingRectangle2D);
            return;
        }
        if (nCount == 0) {
            _outsideHashtable.put(anObject, absoluteBoundingRectangle2D);
            return;
        }

        if ( bNW ) {
            _nwQuadTree.add(anObject, absoluteBoundingRectangle2D);
        }
        if ( bNE ) {
            _neQuadTree.add(anObject, absoluteBoundingRectangle2D);
        }
        if ( bSW ) {
            _swQuadTree.add(anObject, absoluteBoundingRectangle2D);
        }
        if ( bSE ) {
            _seQuadTree.add(anObject, absoluteBoundingRectangle2D);
        }
    }

    public Object remove(AbstractLayer anObject) {
        Object returnObject = _theHashtable.remove(anObject);
        if (returnObject != null) {
            return returnObject;
        }

        if (_nMaxTreeDepth > 1) {
            returnObject = _nwQuadTree.remove(anObject);
            if (returnObject != null) {
                return returnObject;
            }

            returnObject = _neQuadTree.remove(anObject);
            if (returnObject != null) {
                return returnObject;
            }

            returnObject = _swQuadTree.remove(anObject);
            if (returnObject != null) {
                return returnObject;
            }

            returnObject = _seQuadTree.remove(anObject);
            if (returnObject != null) {
                return returnObject;
            }
        }

        returnObject = _outsideHashtable.remove(anObject);
        if (returnObject != null) {
            return returnObject;
        }

        return null;
    }

    public void removeAll(Collection <AbstractLayer> layers){
      for( AbstractLayer  l : layers ){
          remove( l );
      }
    }

    public void clear() {
        _theHashtable.clear();
        _outsideHashtable.clear();
        if (_nMaxTreeDepth > 1) {
            _nwQuadTree.clear();
            _neQuadTree.clear();
            _swQuadTree.clear();
            _seQuadTree.clear();
        }
    }

    public boolean contains(AbstractLayer anObject) {
        boolean found = _theHashtable.containsKey(anObject);
        if (found) {
            return true;
        }

        if (_nMaxTreeDepth > 1) {
            found = _nwQuadTree.contains(anObject);
            if (found) {
                return true;
            }

            found = _neQuadTree.contains(anObject);
            if (found) {
                return true;
            }

            found = _swQuadTree.contains(anObject);
            if (found) {
                return true;
            }

            found = _seQuadTree.contains(anObject);
            if (found) {
                return true;
            }
        }

        found = _outsideHashtable.contains(anObject);
        if (found) {
            return true;
        }

        return false;
    }


    public int getMaxTreeDepth() {
        return _nMaxTreeDepth;
    }

	public List <AbstractLayer> getAll() {
		List <AbstractLayer> l = new ArrayList <AbstractLayer> ();
		l.addAll(_theHashtable.keySet());
		l.addAll(_outsideHashtable.keySet());

		if (_nMaxTreeDepth > 1) {
			l.addAll(_nwQuadTree.getAll());
			l.addAll(_neQuadTree.getAll());
			l.addAll(_swQuadTree.getAll());
			l.addAll(_seQuadTree.getAll());
		}

		return  l;
	}

    public List <AbstractLayer> getAllWithin(Rectangle2D r) {
        List <AbstractLayer>l = new ArrayList <AbstractLayer>( );
        for (AbstractLayer layer : _outsideHashtable.keySet()) {
            Rectangle2D itsAbsoluteBoundingRectangle2D = (Rectangle2D)
            _outsideHashtable.get(layer);
            if (itsAbsoluteBoundingRectangle2D.intersects(r)) {
                l.add(layer);
            }
        }

        if (_absoluteBoundingRectangle2D.intersects(r)) {
            for(AbstractLayer layer : _theHashtable.keySet()) {
                Rectangle2D itsAbsoluteBoundingRectangle2D = (Rectangle2D)
                _theHashtable.get( layer );
                if (itsAbsoluteBoundingRectangle2D.intersects(r)) {
                    l.add( layer );
                }
            }

            if (_nMaxTreeDepth > 1) {
                l.addAll(_nwQuadTree.getAllWithin(r));
                l.addAll(_neQuadTree.getAllWithin(r));
                l.addAll(_swQuadTree.getAllWithin(r));
                l.addAll(_seQuadTree.getAllWithin(r));
            }
        }

        return l;
    }

    public List <AbstractLayer> getAllAt(Point2D p) {
     List <AbstractLayer>l = new ArrayList <AbstractLayer>( );
     if (_absoluteBoundingRectangle2D.contains( p )) {
           for(AbstractLayer layer : _theHashtable.keySet()) {
             Rectangle2D itsAbsoluteBoundingRectangle2D = (Rectangle2D)
             _theHashtable.get(layer);
             if (itsAbsoluteBoundingRectangle2D.contains(p)) {
              l.add(layer);
             }
            // log.debug( layer );
           }

            if (_nMaxTreeDepth > 1) {
                l.addAll(_nwQuadTree.getAllAt(p));
                l.addAll(_neQuadTree.getAllAt(p));
                l.addAll(_swQuadTree.getAllAt(p));
                l.addAll(_seQuadTree.getAllAt(p));
            }
        }
        return l;
    }

    public void paint( Graphics2D g ){
      g.draw(  _absoluteBoundingRectangle2D );
      if (_nMaxTreeDepth > 1) {
        _nwQuadTree.paint( g );
        _neQuadTree.paint( g );
        _swQuadTree.paint( g );
        _seQuadTree.paint( g );
      }
    }


    public Rectangle2D getAbsoluteBoundingRectangle2D() {
        return _absoluteBoundingRectangle2D;
    }


    public void setBoundingRectangle2D(Rectangle2D absoluteBoundingRectangle2D){
      _absoluteBoundingRectangle2D.setRect(absoluteBoundingRectangle2D);
      if (_nMaxTreeDepth > 1) {
            _nwQuadTree.setBoundingRectangle2D(
            _makeNorthwest(absoluteBoundingRectangle2D));
            _neQuadTree.setBoundingRectangle2D(
            _makeNortheast(absoluteBoundingRectangle2D));
            _swQuadTree.setBoundingRectangle2D(
            _makeSouthwest(absoluteBoundingRectangle2D));
            _seQuadTree.setBoundingRectangle2D(
            _makeSoutheast(absoluteBoundingRectangle2D));
        }
    }


    public void paintLayes( Graphics2D g ){
       Rectangle clip = g.getClipBounds();
       for(AbstractLayer layer :  _theHashtable.keySet() ) {
          Rectangle2D itsAbsoluteBoundingRectangle2D = (Rectangle2D)
                _theHashtable.get( layer );
           if(clip.intersects(itsAbsoluteBoundingRectangle2D)){
             g.setColor( layer.getColor() );
             layer.paint( g );
           }
       }
       if (_nMaxTreeDepth > 1) {
       _nwQuadTree.paintLayes( g );
       _neQuadTree.paintLayes( g );
       _swQuadTree.paintLayes( g );
       _seQuadTree.paintLayes( g );
       }
    }

    //___________________________________________________PRIVATE METHODS

    private void _init(int nMaxTreeDepth, Rectangle2D absoluteBoundingRectangle2D) {
        _absoluteBoundingRectangle2D.setRect(absoluteBoundingRectangle2D);
        _nMaxTreeDepth = nMaxTreeDepth;

        if (_nMaxTreeDepth > 1) {
            _nwQuadTree = new QuadTree(_nMaxTreeDepth-1,
            _makeNorthwest(absoluteBoundingRectangle2D));
            _neQuadTree = new QuadTree(_nMaxTreeDepth-1,
            _makeNortheast(absoluteBoundingRectangle2D));
            _swQuadTree = new QuadTree(_nMaxTreeDepth-1,
            _makeSouthwest(absoluteBoundingRectangle2D));
            _seQuadTree = new QuadTree(_nMaxTreeDepth-1,
            _makeSoutheast(absoluteBoundingRectangle2D));
        }
    }

    private Rectangle2D _makeNorthwest(Rectangle2D r) {
        return new Rectangle2D.Double(r.getX(), r.getY(), r.getWidth() / 2.0, r.getHeight() / 2.0);
    }

    private Rectangle2D _makeNortheast(Rectangle2D r) {
        return new Rectangle2D.Double(r.getX() + r.getWidth() / 2.0,
            r.getY(), r.getWidth() / 2.0, r.getHeight() / 2.0);
    }

    private Rectangle2D _makeSouthwest(Rectangle2D r) {
        return new Rectangle2D.Double(r.getX(), r.getY() + r.getHeight() / 2.0,
            r.getWidth() / 2.0, r.getHeight() / 2.0);
    }

    private Rectangle2D _makeSoutheast(Rectangle2D r) {
        return new Rectangle2D.Double(r.getX() + r.getWidth() / 2.0,
            r.getY() + r.getHeight() / 2.0, r.getWidth() / 2.0,
            r.getHeight() / 2.0);
    }

//_______________________________________________________________END

} //end of class QuadTree

