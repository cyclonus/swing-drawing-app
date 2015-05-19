package net.faraya.swing.ui.splash;

import net.faraya.swing.ui.UIImages;

import java.awt.*;

/*
 * @(#)Splasher.java  2.0  January 31, 2004
 *
 * Copyright (c) 2003-2004 Werner Randelshofer
 * Staldenmattweg 2, Immensee, CH-6405, Switzerland.
 * This software is in the public domain.
 */

/**
 *
 * @author  werni
 */
public class Splasher {
    /**
     * Shows the splash screen, launches the application and then disposes
     * the splash screen.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SplashWindow.splash(
               Toolkit.getDefaultToolkit().getImage(UIImages.SPLASH)
        );
        SplashWindow.invokeMain("net.faraya.swing.ui.Main", args);
        SplashWindow.disposeSplash();
    }

}
