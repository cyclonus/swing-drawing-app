package net.faraya.swing.ui.view;

import net.faraya.swing.core.layer.AbstractLayer;
import net.faraya.swing.ui.UIImages;
import net.faraya.swing.ui.view.property.PropertiesView;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

/**
 * User: Fabrizzio
 * Date: 25-Feb-2006
 * Time: 11:47:39
 * To change this template use File | Settings | File Templates.
 */
public class ObjectsView extends Container {

    //protected static Logger log = Logger.getLogger(ObjectsView.class);

    private PropertiesView propertiesView = new PropertiesView( this );
    private Toolkit toolkit = Toolkit.getDefaultToolkit();
    private JTree tree;
    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel treeModel;

    public DefaultMutableTreeNode getRootNode(){ return rootNode; }

    private void createTree(){
          rootNode = new DefaultMutableTreeNode( "" );
          treeModel = new DefaultTreeModel( rootNode );

           treeModel.addTreeModelListener(new TreeModelListener(){
             public void treeNodesChanged(TreeModelEvent e) {
                DefaultMutableTreeNode node;
                node = (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());
                /*
                 * If the event lists children, then the changed
                 * node is the child of the node we've already
                 * gotten.  Otherwise, the changed node and the
                 * specified node are the same.
                 */
                try {
                    int index = e.getChildIndices()[0];
                    node = (DefaultMutableTreeNode)(node.getChildAt(index));
                } catch (NullPointerException exc) {}
                System.out.println("The user has finished editing the node.");
                System.out.println("New value: " + node.getUserObject());
            }
            public void treeNodesInserted(TreeModelEvent e) {
            }
            public void treeNodesRemoved(TreeModelEvent e) {
            }
            public void treeStructureChanged(TreeModelEvent e) {
            }
           });

          tree = new JTree( treeModel );
          DefaultTreeCellRenderer renderer =(DefaultTreeCellRenderer) tree.getCellRenderer();

          tree.setEditable(true);
          tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
          tree.setShowsRootHandles(true);
          tree.addTreeSelectionListener( new TreeSelectionListener(){
           public void valueChanged(TreeSelectionEvent e){

              TreePath currentSelection = tree.getSelectionPath();
              if (currentSelection != null) {
                 DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                         (currentSelection.getLastPathComponent());
                  Object o = currentNode.getUserObject();
                  if(o instanceof AbstractLayer)
                  {
                    AbstractLayer l = (AbstractLayer)o;
                    l.setSelected( true );
                  }
                 }
              }
           } );

           /*
           DefaultTreeCellRenderer renderer3 = new DefaultTreeCellRenderer();
           renderer3.setOpenIcon(customOpenIcon);
           renderer3.setClosedIcon(customClosedIcon);
           renderer3.setLeafIcon(customLeafIcon);
           tree.setCellRenderer(renderer3);
           */
    }

    public void reload( Collection items ){
        clear();
        if(items != null){
         for( Object o : items){
          addObject(rootNode, o);
         }
         tree.expandRow(0);
        }
      }

    public void select( Object obj ){
       //Get the enumeration
       Enumeration en = rootNode.breadthFirstEnumeration();
       while(en.hasMoreElements()){
         DefaultMutableTreeNode node = (DefaultMutableTreeNode)en.nextElement();
         Object o = node.getUserObject();

          if( obj.equals( o )){
             TreeNode[] nodes = treeModel.getPathToRoot(node);
             TreePath path = new TreePath(nodes);
             tree.scrollPathToVisible(path);
             tree.setSelectionPath(path);
             try{
                /*
                 Variable v = new Variable(obj.getClass(), "this", obj);
                 ObjectTreeModel model = new ObjectTreeModel();
                 model.setRoot(v);
                 treeTable.setModel(model);
                 */
                 propertiesView.setDrawObject( obj );
             }catch(Exception e){
              e.printStackTrace();
             }
             break;
          }
       }
    }
   /*
    public void setNewObject(Object o){
      Variable v = new Variable(o.getClass(), "this", o);
      ObjectTreeModel model = new ObjectTreeModel();
      model.setRoot(v);
    }*/

  /** Remove all nodes except the root node. */
    public void clear() {
        rootNode.removeAllChildren();
        treeModel.reload();
    }

    /** Remove the currently selected node. */
    public void removeCurrentNode() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                         (currentSelection.getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode)(currentNode.getParent());
            if (parent != null) {
                treeModel.removeNodeFromParent(currentNode);
                return;
            }
        }

        // Either there was no selection, or the root was selected.
        toolkit.beep();

    }

     public TreePath getPath(TreeNode node) {
        java.util.List <TreeNode> list = new ArrayList<TreeNode> ();

        // Add all nodes to list
        while (node != null) {
            list.add(node);
            node = node.getParent();
        }
        Collections.reverse(list);

        // Convert array of nodes to TreePath
        return new TreePath(list.toArray());
    }

    /** Add child to the currently selected node. */
    public DefaultMutableTreeNode addObject(Object child) {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();

        if (parentPath == null) {
            parentNode = rootNode;
        } else {
            parentNode = (DefaultMutableTreeNode)
                         (parentPath.getLastPathComponent());
        }

        return addObject(parentNode, child, true);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child) {
        return addObject(parent, child, false);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child, boolean shouldBeVisible) {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
        if (parent == null) {
            parent = rootNode;
        }
        treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
        //Make sure the user can see the lovely new node.
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }



    private void init(){
      createTree();
      try{
       /*
       Variable v = new Variable(getClass(), "this", this);
       ObjectTreeModel model = new ObjectTreeModel();
       model.setRoot(null);
       treeTable = new JTreeTable( model );
       DefaultTreeCellRenderer renderer =(DefaultTreeCellRenderer) treeTable.getTree().getCellRenderer();
       renderer.setOpenIcon(UIImages.getIcon(UIImages.TREE_OPEN));
       renderer.setClosedIcon(UIImages.getIcon(UIImages.TREE_CLOSE));
       renderer.setLeafIcon(UIImages.getIcon(UIImages.TREE_LEAF));
       */

       JScrollPane jScrollPane1 = new JScrollPane(tree);
       //JScrollPane jScrollPane2 = new JScrollPane(treeTable);
       JScrollPane jScrollPane2 = new JScrollPane(propertiesView);

       JSplitPane splitPanel = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
       setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ));
       add( splitPanel );
       splitPanel.add( jScrollPane1 );
       splitPanel.add( jScrollPane2 );
      }catch(Exception e){
        //log.error( this,e );
      }
    }


    public ObjectsView(){
       init();
    }


}

/*
class ObjectTreeModel extends AbstractTreeTableModel implements TreeTableModel {

  // Names of the columns.
    static protected String[]  cNames = {"Name", "Value"};

    // Types of the columns.
    static protected Class[]  cTypes = {TreeTableModel.class, Object.class};

  public ObjectTreeModel() {
    super(null);
    root = null;
  }

  public void setRoot(Variable v) {
    Variable oldRoot = v;
    root = v;
    fireTreeStructureChanged(oldRoot);
  }

  public Object getRoot() {
    return root;
  }

  public int getColumnCount() {
	return cNames.length;
    }

  public String getColumnName(int column) {
    return cNames[column];
  }

  public Class getColumnClass(int column) {
	return cTypes[column];
  }

 int index = 0;

  public Object getValueAt(Object parent, int column) {
      switch(column) {
	    case 0: return ((Variable) parent).getName();
	    case 1: return ((Variable) parent).getValue();
        default: return null;
      }
  }

  public int getChildCount(Object parent) {
    return ((Variable) parent).getFields().size();
  }


  public Object getChild(Object parent, int index) {
    ArrayList fields = ((Variable) parent).getFields();
    Field f = (Field) fields.get(index);
    Object parentValue = ((Variable) parent).getValue();
    this.index = index;
    try {
      return new Variable(f.getType(), f.getName(), f.get(parentValue));
    } catch (IllegalAccessException e) {
      return null;
    }
  }

  public int getIndexOfChild(Object parent, Object child) {
    int n = getChildCount(parent);
    for (int i = 0; i < n; i++)
      if (getChild(parent, i).equals(child))
        return i;
    return -1;
  }

  public boolean isLeaf(Object node) {
    return getChildCount(node) == 0;
  }

  public void valueForPathChanged(TreePath path, Object newValue) {
  }

  public void addTreeModelListener(TreeModelListener l) {
    listenerList.add(TreeModelListener.class, l);
  }

  public void removeTreeModelListener(TreeModelListener l) {
    listenerList.remove(TreeModelListener.class, l);
  }

  protected void fireTreeStructureChanged(Object oldRoot) {
    TreeModelEvent event = new TreeModelEvent(this,
        new Object[] { oldRoot });
    Object[] listeners = listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2)
      ((TreeModelListener) listeners[i + 1]).treeStructureChanged(event);
  }

  private Variable root;

  private EventListenerList listenerList = new EventListenerList();
}
*/
/*
class Variable {
  public Variable(Class aType, String aName, Object aValue) {
    type = aType;
    name = aName;
    value = aValue;
    fields = new ArrayList();

     //
     // find all fields if we have a class type except we don't expand
     // strings and null values
     //

    if (!type.isPrimitive() && !type.isArray()
        && !type.equals(String.class) && value != null) { // get fields
                                  // from the
                                  // class and
                                  // all
                                  // superclasses
      for (Class c = value.getClass(); c != null; c = c.getSuperclass()) {
        Field[] f = c.getDeclaredFields();
        AccessibleObject.setAccessible(f, true);

        // get all nonstatic fields
        for (int i = 0; i < f.length; i++)
          if ((f[i].getModifiers() & Modifier.STATIC) == 0)
            fields.add(f[i]);
      }
    }
  }

  public String getName() {
    return name;
  }

  public Object getValue() {
    return value;
  }

  public ArrayList getFields() {
    return fields;
  }

  public String toString() {
    String r = type + " " + name;
    if (type.isPrimitive())
      r += "=" + value;
    else if (type.equals(String.class))
      r += "=" + value;
    else if (value == null)
      r += "=null";
    return r;
  }

  private Class type;

  private String name;

  private Object value;

  private ArrayList fields;
}
*/