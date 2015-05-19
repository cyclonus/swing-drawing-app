package net.faraya.swing.ui.view.property.dialog;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;

/**
 * User: Fabrizzio
 * Date: 26-Mar-2006
 * Time: 13:18:11
 * To change this template use File | Settings | File Templates.
 */
public class AlphaColorChooser extends JDialog implements ChangeListener
{
    private JSlider slider_;
    private JColorChooser color_chooser_;
    private ChangeListener change_listener_;
    public static AlphaColorChooser instance_ = null;

    //----------------------------------------------------------------------------
    /** Creates a new instance of AlphaColorChooser */
    private AlphaColorChooser(String title, Color color)
    {
        super();
        setTitle(title);
        setModal(true);
        change_listener_ = null;
        color_chooser_ = new JColorChooser(color);
        color_chooser_.getSelectionModel().addChangeListener(this);
        getContentPane().add(color_chooser_, BorderLayout.CENTER);
        slider_ = new JSlider(JSlider.HORIZONTAL, 0, 255, color.getAlpha());
        slider_.addChangeListener(this);
        slider_.setMajorTickSpacing(85);
        slider_.setMinorTickSpacing(17);
        slider_.setPaintTicks(true);
        slider_.setPaintLabels(true);
        slider_.setBorder(BorderFactory.createTitledBorder("Alpha"));
        getContentPane().add(slider_, BorderLayout.SOUTH);
        pack();
    }

    public static AlphaColorChooser getInstance()
    {
        if( instance_ == null )
        {
            instance_ = new AlphaColorChooser("Choose Color",Color.RED);
        }

        return instance_;
    }

    //----------------------------------------------------------------------------
    /**
     * Invoked when the target of the listener has changed its state.
     *
     * @param e  a ChangeEvent object
     */
    public void stateChanged(ChangeEvent e)
    {
        if (change_listener_ != null)
        {
            color_chooser_.setColor( getColor() );
            change_listener_.stateChanged( new ChangeEvent(this) );
        }
    }

    //----------------------------------------------------------------------------
    public void setChangeListener(ChangeListener change_listener)
    {
        change_listener_ = change_listener;
    }

    //----------------------------------------------------------------------------
    public void setColor(Color new_color)
    {
        color_chooser_.setColor(new_color);
        slider_.setValue( new_color.getAlpha() );
    }

    //----------------------------------------------------------------------------
    public Color getColor()
    {
        Color chooser_color = color_chooser_.getColor();
        return new Color(chooser_color.getRed(), chooser_color.getGreen(), chooser_color.getBlue(), slider_.getValue());
    }
}

