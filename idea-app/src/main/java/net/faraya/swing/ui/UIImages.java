package net.faraya.swing.ui;

import com.google.common.io.ByteStreams;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;

/**
 * User: Fabrizzio
 * Date: 26-Dec-2005
 * Time: 12:42:14
 * To change this template use File | Settings | File Templates.
 */
public class UIImages {

     public static final String baseDir ="";

     public static final String SPLASH = "images/RedRain.jpg";

     public static final String  TREE_OPEN    =  "images/icons/16x16/Open.gif";
     public static final String  TREE_CLOSE   =  "images/icons/16x16/Folder.gif";
     public static final String  TREE_LEAF    =  "images/icons/16x16/BlueCircle.gif";

     public static final String  ADD_16      =  "images/icons/16x16/Document.gif";
     public static final String  LINE_16     =  "images/icons/16x16/DocumentDraw.gif";
     public static final String  DEL_16      =  "images/icons/16x16/DeleteDocument.gif";
     public static final String  UNDO_16     =  "images/icons/16x16/Undo.gif";
     public static final String  REDO_16     =  "images/icons/16x16/Redo.gif";
     public static final String  REPAINT_16  =  "images/icons/16x16/Exclamation.gif";
     public static final String  REMOVE_16   =  "images/icons/16x16/DeleteDocument.gif";
     public static final String  EXCLAMATION =  "images/icons/16x16/Exclamation.gif";
     public static final String  DOC_16      =  "images/icons/16x16/Document.gif";
     public static final String  TRASH_16    =  "images/icons/16x16/Trashcan.gif";

    /**
     * Commands
     */

     public static final String  PAINT_20     =  "images/icons/20x20/Paint.gif";
     public static final String  PALETTE_20   =  "images/icons/20x20/Palette.gif";
     public static final String  OBJECT_20    =  "images/icons/20x20/Object.gif";
     public static final String  LINEGRAPH_20 =  "images/icons/20x20/LineGraph.gif";
     public static final String  FLOWGRAPH_20 =  "images/icons/20x20/FlowGraph.gif";
     public static final String  DRAW_20      =  "images/icons/20x20/Draw.gif";
     public static final String  DOCDRAW_20   =  "images/icons/20x20/DocumentDraw.gif";
     public static final String  DIAGRAM_20   =  "images/icons/20x20/DocumentDiagram.gif";
     public static final String  ARROW_20     =  "images/icons/20x20/UpLeft.gif";

    

     private static Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon>();


     public static ImageIcon getIcon( String fileKey ){
         ImageIcon i = iconsCache.get( fileKey );

         if( i != null){
           return i;
         }

        try {
            InputStream is = UIImages.class.getClassLoader().getResourceAsStream(fileKey);
            byte[] bytes = ByteStreams.toByteArray(is);
            i = new ImageIcon(bytes);
            iconsCache.put(fileKey, i);
        }catch (IOException e){
            e.printStackTrace();
        }   

         return i;
     }



     private UIImages(){
     }

}
