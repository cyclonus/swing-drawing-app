package net.faraya.swing;

import javax.swing.*;

/**
 * User: Fabrizzio
 * Date: 03-Apr-2006
 * Time: 22:29:01
 * To change this template use File | Settings | File Templates.
 */
public class VerboseRepaintManager extends RepaintManager {

    /**
        Installs the NullRepaintManager.
    */
    public static void install() {
        RepaintManager repaintManager = new VerboseRepaintManager();
        repaintManager.setDoubleBufferingEnabled(false);
        RepaintManager.setCurrentManager(repaintManager);
    }

    public void addInvalidComponent(JComponent c) {        
        System.out.println( "addInvalidComponent :"+ c.getClass() + (c.getName()==null ? "" : c.getName()));
        super.addInvalidComponent(c);
    }

    public void addDirtyRegion(JComponent c, int x, int y,
        int w, int h)
    {
       //System.out.println( "addDirtyRegion :" + c +" x= "+x+" y= "+y+" w= "+w+" h= "+h);
       System.out.println( "addDirtyRegion :" + c.getClass() + (c.getName()==null ? "" : c.getName()));        
       super.addDirtyRegion(c, x, y, w, h);
    }

    public void markCompletelyDirty(JComponent c) {
       System.out.println( "markCompletelyDirty :" + c.getClass() + (c.getName()==null ? "" : c.getName()));
       super.markCompletelyDirty(c);
    }

    public void paintDirtyRegions() {
        System.out.println( "paintDirtyRegions ");
        super.paintDirtyRegions();
    }

}
