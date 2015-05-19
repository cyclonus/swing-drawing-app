package net.faraya.swing.ui.view.property;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import java.awt.*;

/**
 * User: Fabrizzio
 * Date: 26-Mar-2006
 * Time: 13:10:08
 * To change this template use File | Settings | File Templates.
 */
public class DrawObjectTableCellRenderer extends DefaultTableCellRenderer
{

    //----------------------------------------------------------------------------
    /** Creates a new instance of DrawObjectTableCellRenderer */
    public DrawObjectTableCellRenderer()
    {
        super();
    }

    //----------------------------------------------------------------------------
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column )
    {
        //        System.out.println("value = " + value);

        if ( value instanceof Color )
        {
            setBackground((Color)value);
            setText( "" );
        }
        else
        {
            setBackground( Color.WHITE );
            setValue( value );
        }

        if (hasFocus)
        {
            setBorder( UIManager.getBorder("Table.focusCellHighlightBorder") );

            if (table.isCellEditable(row, column))
            {
                super.setForeground( UIManager.getColor("Table.focusCellForeground") );
            }
        }
        else
        {
            setBorder(noFocusBorder);
        }

        return this;
    }
}

