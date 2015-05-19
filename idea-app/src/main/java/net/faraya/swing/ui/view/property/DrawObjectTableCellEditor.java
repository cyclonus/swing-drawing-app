package net.faraya.swing.ui.view.property;

import net.faraya.swing.ui.view.property.dialog.FontDialog;
import net.faraya.swing.ui.view.property.dialog.AlphaColorChooser;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.util.Hashtable;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DrawObjectTableCellEditor extends DefaultCellEditor
{
    protected Hashtable delegates;
    protected Hashtable editors;

    //----------------------------------------------------------------------------
    /** Creates a new instance of DrawObjectTableModel */
    public DrawObjectTableCellEditor()
    {
        super( new JTextField() );

        editors   = new Hashtable();
        delegates = new Hashtable();

        editors.put("String", editorComponent);
        delegates.put("String", delegate);

        JButton button = new JButton();
        editors.put("Color", button );
        ColorDelegate color_delegate = new ColorDelegate( button );
        button.addActionListener(color_delegate);
        delegates.put("Color", color_delegate);
        /*
        button = new JButton();
        editors.put("DrawSWFFont", button );
        DrawSWFFontDelegate font_delegate = new DrawSWFFontDelegate( button );
        button.addActionListener(font_delegate);
        delegates.put("DrawSWFFont", font_delegate);
        */
    }

    //----------------------------------------------------------------------------
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column )
    {
        //        System.out.println("value = " + value);

        if ( value instanceof Color )
        {
            editorComponent = (JButton) editors.get("Color");
            editorComponent.setBackground((Color) value);
            delegate = (EditorDelegate) delegates.get("Color");
            ( (ColorDelegate) delegate ).setTableModel(table.getModel(), row, column);
            delegate.setValue(value);
        }
        /*
        else if( value instanceof DrawSWFFont )
        {
            editorComponent = (JButton) editors.get("DrawSWFFont");
            ((JButton) editorComponent).setText(value.toString());
            delegate = (EditorDelegate) delegates.get("DrawSWFFont");
            ( (DrawSWFFontDelegate) delegate ).setTableModel(table.getModel(), row, column);
            delegate.setValue( value );
        } */
        else
        {
            editorComponent = (JTextField) editors.get("String");
            delegate = (EditorDelegate) delegates.get("String");
            delegate.setValue(value);
        }

        return editorComponent;
    }

    //----------------------------------------------------------------------------
    protected class DrawSWFFontDelegate extends EditorDelegate implements ChangeListener
    {
        private TableModel model_;
        private FontDialog font_dialog_;
        private int row_;
        private int column_;
        private JButton button_;

        //----------------------------------------------------------------------------
        public DrawSWFFontDelegate( JButton button )
        {
            model_ = null;
            font_dialog_ = new FontDialog( null );
            font_dialog_.removeAllChangeListeners();
            font_dialog_.addChangeListener( this );
            font_dialog_.setTextInputEnabled( false );
            button_ = button;
        }

        //----------------------------------------------------------------------------
        public void setValue(Object value)
        {
            font_dialog_.setFont( (Font)value );
        }

        //----------------------------------------------------------------------------
        public Object getCellEditorValue()
        {
            return font_dialog_.getFont();
        }

        //----------------------------------------------------------------------------
        public void actionPerformed(ActionEvent e)
        {
            font_dialog_.show();
        }

        //----------------------------------------------------------------------------
        public void setTableModel(TableModel table_model, int row, int column)
        {
            model_  = table_model;
            row_    = row;
            column_ = column;
        }

        //----------------------------------------------------------------------------
        public void stateChanged(ChangeEvent e)
        {
            if (model_ != null)
            {
                model_.setValueAt( font_dialog_.getFont(), row_, column_);
            }

            button_.setText( font_dialog_.getFont().toString() );
        }
    }

    //----------------------------------------------------------------------------
    protected class ColorDelegate extends EditorDelegate implements ChangeListener
    {
        private int row_;
        private int column_;
        private TableModel model_;
        private JButton button_;
        private Color color_;

        //----------------------------------------------------------------------------
        public ColorDelegate( JButton button )
        {
            AlphaColorChooser color_chooser = AlphaColorChooser.getInstance();
            color_chooser.setTitle("Edit Color");
            color_chooser.setChangeListener(this);
            color_chooser.setColor(Color.black);
            button_ = button;
            model_ = null;
        }

        //----------------------------------------------------------------------------
        public void setValue(Object value)
        {
            color_ = (Color) value;
            AlphaColorChooser color_chooser = AlphaColorChooser.getInstance();
            color_chooser.setTitle("Edit Color");
            color_chooser.setChangeListener(this);
            color_chooser.setColor(color_);
        }

        //----------------------------------------------------------------------------
        public Object getCellEditorValue()
        {
            return color_;
        }

        //----------------------------------------------------------------------------
        public void actionPerformed(ActionEvent e)
        {
            AlphaColorChooser color_chooser = AlphaColorChooser.getInstance();
            color_chooser.setTitle("Edit Color");
            color_chooser.setChangeListener(this);
            color_chooser.setColor(color_);
            color_chooser.show();
        }

        //----------------------------------------------------------------------------
        public void setTableModel(TableModel table_model, int row, int column)
        {
            model_  = table_model;
            row_    = row;
            column_ = column;
        }

        //----------------------------------------------------------------------------
        public void stateChanged(ChangeEvent e)
        {
            AlphaColorChooser color_chooser = AlphaColorChooser.getInstance();
            color_  = color_chooser.getColor();

            if (model_ != null)
            {

                model_.setValueAt(color_, row_, column_);
            }

            if( button_ != null )
            {
                button_.setBackground( color_ );
            }
        }
    }
}

