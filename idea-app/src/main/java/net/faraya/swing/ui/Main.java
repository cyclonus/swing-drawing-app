package net.faraya.swing.ui;

import net.faraya.swing.core.layer.AbstractLayer;
import net.faraya.swing.core.Viewer;
import net.faraya.swing.core.ContextListener;
import net.faraya.swing.core.ContextEvent;
import net.faraya.swing.core.IContext;
import net.faraya.swing.ui.view.ViewOrganizer;
import net.faraya.swing.ui.view.CommandBar;
import net.faraya.swing.ui.view.ObjectsView;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


public class Main extends JFrame implements ContextListener {

  //Logger Log = Logger.getLogger(Main.class);
  

  private static int DEFAUL_WIDTH  = 900;
  private static int DEFAUL_HEIGHT = 700;


  private Viewer viewer;
  private ViewOrganizer organizer = new ViewOrganizer();
  //private PropertiesView view = organizer.getPropertiesView();
  private ObjectsView view = organizer.getObjectsView();

  private JPanel contentPane;
  private BorderLayout borderLayout1 = new BorderLayout();

  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu jMenuFile = new JMenu();
  JMenuItem jMenuFileExit = new JMenuItem();
  JMenu jMenuHelp = new JMenu();
  JMenuItem jMenuHelpAbout = new JMenuItem();
  JMenu jMenuLAF = new JMenu();

  JTabbedPane tabbedPanel = new JTabbedPane();

  JPanel panel1 = new JPanel();
  JPanel panel2 = new JPanel();

  JPanel mainPanel = new JPanel();

  BoxLayout boxLayout1;
  JToolBar mainToolBar = new JToolBar();
  JPanel statusBar = new JPanel();

  JButton contextButton = new JButton( UIImages.getIcon( UIImages.ADD_16 ));
  JButton removeButton = new JButton( UIImages.getIcon( UIImages.REMOVE_16 ));
  JButton undoButton = new JButton( UIImages.getIcon( UIImages.UNDO_16 ));
  JButton redoButton = new JButton( UIImages.getIcon( UIImages.REDO_16 ));

  //-----------------------------------------------------------------------
  JPopupMenu jPopMenu = new JPopupMenu();


  // Construct the frame
  public Main() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      init();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

    // Component initialization
    private void init() throws Exception {

      createMainMenu();

      createToolBar();

      createStatusBar();

      initContainers();

      createPopupMenus();

      pack();

      //VerboseRepaintManager.install();
    }


    private void initContainers(){
      contentPane = (JPanel) this.getContentPane();
      contentPane.setLayout( borderLayout1 );
      setTitle( "idea" );
      JSplitPane splitPane11 = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
      splitPane11.add( organizer );
      
      Dimension d = new Dimension( DEFAUL_WIDTH, DEFAUL_HEIGHT );
      setSize( d );
      setPreferredSize( d );

      tabbedPanel.setTabPlacement(JTabbedPane.BOTTOM);
      mainPanel.setLayout(new BorderLayout());
      mainPanel.add(tabbedPanel,BorderLayout.CENTER);
      splitPane11.add(mainPanel);
      contentPane.add(splitPane11);

      contentPane.add(mainToolBar, BorderLayout.NORTH);
      contentPane.add(statusBar, BorderLayout.SOUTH);

      tabbedPanel.addChangeListener(new ChangeListener(){
        public void stateChanged(ChangeEvent e){

           if(viewer != null){
            viewer.getDrawingContext().removeContextListener( Main.this );
           }

           viewer = (Viewer) tabbedPanel.getSelectedComponent();
           view.reload( viewer.getDrawingContext().getLayers() );
           viewer.getDrawingContext().addContextListener( Main.this );
        }
      });

     tabbedPanel.addMouseListener(new MouseAdapter(){
        public void mouseClicked(MouseEvent e) {
            System.out.println(e.getSource());
        }
     });
    }

   private void createPopupMenus(){
    JMenuItem drawingAreaItem = new JMenuItem("Drawing Area");
    jPopMenu.add(drawingAreaItem);

   }

    private void createMainMenu(){
      this.setJMenuBar( jMenuBar1 );
      jMenuFile.setText("File");
      jMenuFileExit.setText("Exit");
      jMenuFileExit.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          System.exit(0);
        }
      });


      jMenuFile.add(jMenuFileExit);

      ButtonGroup lafGroup = new ButtonGroup();
       //napkin.NapkinLookAndFeel
      UIManager.installLookAndFeel("napkinLAF","napkin.NapkinLookAndFeel");
      UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
      for(int i = 0; i < lafs.length; i++) {
	    JRadioButtonMenuItem rb = new JRadioButtonMenuItem(lafs[i].getName());
	    jMenuLAF.add(rb);
	    rb.setSelected(UIManager.getLookAndFeel().getName().equals(lafs[i].getName()));
	    rb.putClientProperty("UIKey", lafs[i]);
	    rb.addItemListener(new ItemListener() {
	     public void itemStateChanged(ItemEvent ae) {
	     JRadioButtonMenuItem rb2 = (JRadioButtonMenuItem)ae.getSource();
	      if(rb2.isSelected()) {
		     UIManager.LookAndFeelInfo info =(UIManager.LookAndFeelInfo)rb2.getClientProperty("UIKey");
		    try {
		            UIManager.setLookAndFeel(info.getClassName());
		            SwingUtilities.updateComponentTreeUI( Main.this );
			}catch (Exception e) {
		      System.err.println("unable to set UI " +e.getMessage());
			}
	       }
	      }
	    });
	    lafGroup.add(rb);
	}



      jMenuLAF.setText("LAF");

      jMenuHelp.setText("Help");
      jMenuHelpAbout.setText("About");
      jMenuHelpAbout.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {

        }
      });

      jMenuHelp.add(jMenuHelpAbout);
      jMenuBar1.add(jMenuFile);
      jMenuBar1.add(jMenuLAF);
      jMenuBar1.add(jMenuHelp);
    }

    private void createToolBar(){
      mainToolBar.add(contextButton, null);
      mainToolBar.addSeparator();
      mainToolBar.add(removeButton,null);
      mainToolBar.add(undoButton,null);
      mainToolBar.add(redoButton,null);

      contextButton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
            addNewContext();
        }
      });

      removeButton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
           remove();
        }
      });

      undoButton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
           undo();
        }
      });

      redoButton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
           redo();
        }
      });

      contextButton.setToolTipText( Hints.NEW_CTX );

      undoButton.setToolTipText( Hints.UNDO );
      redoButton.setToolTipText( Hints.REDO );

      CommandBar commandBar = organizer.getCommandBar();
      commandBar.getLineButton().addActionListener( new ActionListener(){
         public void actionPerformed(ActionEvent e){
           drawLine();
         }
        });
      commandBar.getRectLineButton().addActionListener( new ActionListener(){
         public void actionPerformed(ActionEvent e){
           drawRectLine();
         }
        });

      commandBar.getPolyLineButton().addActionListener( new ActionListener(){
         public void actionPerformed(ActionEvent e){
           drawPolyLine();
         }
        });

      commandBar.getSelctionToolButton().addActionListener( new ActionListener(){
         public void actionPerformed(ActionEvent e){
           setNonCommand();
         }
        });
    }

    private void createStatusBar(){
      statusBar.setBorder(BorderFactory.createEtchedBorder());
      statusBar.setLayout( new BorderLayout() );
      statusBar.add(new MemoryTrackBar(),BorderLayout.EAST);
    }


  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }



  public static void main(String args[]){
    Main main = new Main();
    WindowUtilities.center( main );
    main.setVisible(true);
  }

  private void addRenderContext(Viewer ctx, String name){
    Icon i = UIImages.getIcon( UIImages.DOC_16 );
    tabbedPanel.addTab(name, i, ctx);
  }



    public void contextScaled( ContextEvent e ){

    }

    public void layerSelected( ContextEvent e ){
      AbstractLayer l = e.getSelectedLayer();
      view.select( l );
    }



    /**
     ***********************************************************************************************************
     */


  private void addNewContext(){
    addRenderContext( new Viewer() ," untitled-" + tabbedPanel.getTabCount() );

    // prepare tree
    //Enable drawing buttons

  }

  private void setNonCommand(){
    IContext ctx = viewer.getDrawingContext();
    ctx.setCurrentLayerClass( null );
  }

  private void drawLine(){
    IContext ctx = viewer.getDrawingContext();
    ctx.setCurrentLayerClass(  net.faraya.swing.core.layer.LineLayer.class );
  }

  private void drawRectLine(){
    IContext ctx = viewer.getDrawingContext();
    ctx.setCurrentLayerClass( net.faraya.swing.core.layer.RectLineLayer.class );
  }

  private void drawPolyLine(){
    IContext ctx = viewer.getDrawingContext();
    ctx.setCurrentLayerClass( net.faraya.swing.core.layer.PolyLineLayer.class );
  }

  private void remove(){
    if(viewer!=null){
      viewer.getDrawingContext().removeSelection();
      view.reload( viewer.getDrawingContext().getLayers() );
    }
  }

  private void undo(){
     if(viewer!=null)
      viewer.getDrawingContext().undo();
  }

  private void redo(){
     if(viewer!=null)
      viewer.getDrawingContext().redo();
  }


  static{
    // PropertyConfigurator.configure("./etc/log4j.properties");
  }

}



