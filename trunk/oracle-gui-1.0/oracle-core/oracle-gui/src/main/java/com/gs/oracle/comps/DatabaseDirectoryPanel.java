/**
 * 
 */
package com.gs.oracle.comps;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.gs.oracle.OracleGuiConstants;
import com.gs.oracle.command.GuiCommandConstants;
import com.gs.oracle.connection.ConnectionProperties;
import com.gs.oracle.dlg.TableDataExportDialog;
import com.gs.oracle.grabber.OracleDbGrabber;
import com.gs.oracle.iframe.DatabaseViewerInternalFrame;
import com.gs.oracle.model.Database;
import com.gs.oracle.model.Table;
import com.gs.oracle.util.DisplayUtils;
import com.gs.oracle.util.MenuBarUtil;
import com.gs.oracle.enums.*;

/**
 * @author sabuj.das
 *
 */
public class DatabaseDirectoryPanel extends JPanel implements ActionListener,
		OracleGuiConstants, TreeSelectionListener, MouseListener{

	private static final String CONTENT_TEXT = "Content of "; 
	
	private static final ImageIcon EXPAND_IMAGE = new ImageIcon(DatabaseDirectoryPanel.class
			.getResource(OracleGuiConstants.IMAGE_PATH
					+ "expand.gif"));
	private static final ImageIcon EXPAND_ALL_IMAGE = new ImageIcon(DatabaseDirectoryPanel.class
			.getResource(OracleGuiConstants.IMAGE_PATH
					+ "expandall.gif"));
	private static final ImageIcon COLLAPSE_IMAGE = new ImageIcon(DatabaseDirectoryPanel.class
			.getResource(OracleGuiConstants.IMAGE_PATH
					+ "collapse.gif"));
	private static final ImageIcon COLLAPSE_ALL_IMAGE = new ImageIcon(DatabaseDirectoryPanel.class
			.getResource(OracleGuiConstants.IMAGE_PATH
					+ "collapseall.gif"));
	
	private JComponent parentComponent;
	private JFrame parentFrame;
	private DatabaseDirectoryTree databaseDirectoryTree;
	protected TreePath mouseClickedTreePath;
	private JPopupMenu dbDirectoryTreePopupMenu, commonPopupMenu, columnPopupMenu, tablePopupMenu,
						folderPopupMenu, schemaPopupMenu;
	private JMenuItem expandCollaspMenuItem, renameObjectMenuItem, viewTableDetailsMenuItem,
						dropObjectMenuItem, commentObjectMenuItem;
	
	private JMenuItem renameColumnMenuItem, dropColumnMenuItem, commentColumnMenuItem;
	
	private ConnectionProperties connectionProperties;
	
	private JMenuItem expandCollaspTableMenuItem, renameTableMenuItem,
		dropTableMenuItem, commentTableMenuItem, openTableDetailsMenuItem, showTableContentMenuItem,
		importDataToTableMenuItem, modifyTableMenuItem, truncateTableMenuItem,
		copyTableMenuItem, exportDataToInsertScriptMenuItem, exportDataToLoaderMenuItem,
		exportDataToCsvMenuItem, exportDataToHtmlMenuItem, exportDataToTextMenuItem,
		exportDataToExcelMenuItem, exportDataToXmlMenuItem, exportDdlToFileMenuItem,
		exportDdlToClipBoardMenuItem, exportDdlToSqlTabMenuItem;
	private JMenu editTableMenu, exportTableDataMenu, exportTableDdlMenu;
	
	private JMenuItem addTableMenuItem, expandCollapseFolderMenuItem, expandCollapseSchemaMenuItem,
		expandCollapseAllFolderMenuItem, expandCollapseAllSchemaMenuItem, exportTableStructureMenuItem;
	
	
	public DatabaseDirectoryPanel(JFrame parent, DatabaseDirectoryTree tree, ConnectionProperties connectionProperties) {
		if(tree == null)
			throw new IllegalArgumentException("DatabaseDirectoryTree cannot be NULL");
		this.parentFrame = parent;
		this.databaseDirectoryTree = tree;
		this.databaseDirectoryTree.addTreeSelectionListener(this);
		this.databaseDirectoryTree.addMouseListener(this);
		this.connectionProperties = connectionProperties;
		addMouseListener(this);
		initComponents();
	}
	
	private void initComponents(){
		refreshTreeButton = new JButton();
		databaseDirectoryToolBar = new JToolBar();
		dbDirectoryTreePopupMenu = new JPopupMenu();
		
		
// menu items		
		expandCollaspMenuItem = new JMenuItem("Expand");
		expandCollaspMenuItem.setIcon(EXPAND_IMAGE);
		expandCollaspMenuItem.addActionListener(this);
		
		viewTableDetailsMenuItem = new JMenuItem("Table Details");
		viewTableDetailsMenuItem.addActionListener(this);
        viewTableDetailsMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "sampleContents.gif")));
        
		renameObjectMenuItem = new JMenuItem("Rename");
		renameObjectMenuItem.addActionListener(this);
		renameObjectMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "correction_linked_rename.gif")));
		
		dropObjectMenuItem = new JMenuItem("Drop");
		dropObjectMenuItem.addActionListener(this);
		dropObjectMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "drop.gif")));
		
		commentObjectMenuItem = new JMenuItem("Comment");
		commentObjectMenuItem.addActionListener(this);
		commentObjectMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "comments.gif")));
		
		renameColumnMenuItem = new JMenuItem("Rename");
		renameColumnMenuItem.addActionListener(this);
		renameColumnMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "correction_linked_rename.gif")));
		
		dropColumnMenuItem = new JMenuItem("Drop");
		dropColumnMenuItem.addActionListener(this);
		dropColumnMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "drop.gif")));
		
		commentColumnMenuItem = new JMenuItem("Comment");
		commentColumnMenuItem.addActionListener(this);
		commentColumnMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "comments.gif")));
		
		columnPopupMenu = new JPopupMenu();
		columnPopupMenu.add(renameColumnMenuItem);
		columnPopupMenu.add(dropColumnMenuItem);
		columnPopupMenu.add(new JSeparator());
		columnPopupMenu.add(commentColumnMenuItem);
		
		expandCollaspTableMenuItem = new JMenuItem("Expand");
		expandCollaspTableMenuItem.addActionListener(this);
		expandCollaspTableMenuItem.setIcon(EXPAND_IMAGE);
		
		openTableDetailsMenuItem = new JMenuItem("Open");
		openTableDetailsMenuItem.addActionListener(this);
		openTableDetailsMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "openTable.gif")));
		showTableContentMenuItem = new JMenuItem("Show Content");
		showTableContentMenuItem.addActionListener(this);
		showTableContentMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "sampleContents.gif")));
		importDataToTableMenuItem = new JMenuItem("Import Data");
		importDataToTableMenuItem.addActionListener(this);
		importDataToTableMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "appclient_import_wiz.gif")));
		modifyTableMenuItem = new JMenuItem("Modify");
		modifyTableMenuItem.addActionListener(this);
		modifyTableMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "edit.gif")));
		renameTableMenuItem = new JMenuItem("Rename");
		renameTableMenuItem.addActionListener(this);
		renameTableMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "correction_linked_rename.gif")));
		
		dropTableMenuItem = new JMenuItem("Drop");
		dropTableMenuItem.addActionListener(this);
		dropTableMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "drop.gif")));
		
		commentTableMenuItem = new JMenuItem("Comment");
		commentTableMenuItem.addActionListener(this);
		commentTableMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "comments.gif")));
		truncateTableMenuItem = new JMenuItem("Truncate");
		truncateTableMenuItem.addActionListener(this);
		/*truncateTableMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "edit.gif")));*/
		copyTableMenuItem = new JMenuItem("Copy");
		copyTableMenuItem.addActionListener(this);
		copyTableMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "copy_edit.gif")));
		
		exportDataToInsertScriptMenuItem = new JMenuItem("Insert Script");
		exportDataToInsertScriptMenuItem.addActionListener(this);
		exportDataToInsertScriptMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "insert_table_row.png")));
		
		exportDataToLoaderMenuItem = new JMenuItem("Sql Loader");
		exportDataToLoaderMenuItem.addActionListener(this);
		
		exportDataToCsvMenuItem = new JMenuItem("CSV");
		exportDataToCsvMenuItem.addActionListener(this);
		exportDataToCsvMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "document-excel-csv.png")));
		
		exportDataToHtmlMenuItem = new JMenuItem("HTML");
		exportDataToHtmlMenuItem.addActionListener(this);
		exportDataToHtmlMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "html.png")));
		
		exportDataToTextMenuItem = new JMenuItem("Text");
		exportDataToTextMenuItem.addActionListener(this);
		exportDataToTextMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "Text.png")));
		
		exportDataToExcelMenuItem = new JMenuItem("Excel");
		exportDataToExcelMenuItem.addActionListener(this);
		exportDataToExcelMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "page_excel.png")));
		
		exportDataToXmlMenuItem = new JMenuItem("Xml");
		exportDataToXmlMenuItem.addActionListener(this);
		exportDataToXmlMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "xml.png")));
		
		exportDdlToFileMenuItem = new JMenuItem("To File");
		exportDdlToFileMenuItem.addActionListener(this);
		
		exportDdlToClipBoardMenuItem = new JMenuItem("To Clipboard");
		exportDdlToClipBoardMenuItem.addActionListener(this);
		
		exportDdlToSqlTabMenuItem = new JMenuItem("To Sql Tab");
		exportDdlToSqlTabMenuItem.addActionListener(this);
		
		exportTableStructureMenuItem = new JMenuItem("Structure to Excel");
		exportTableStructureMenuItem.addActionListener(this);
		
		editTableMenu = new JMenu("Edit");
		editTableMenu.add(modifyTableMenuItem);
		editTableMenu.add(new JSeparator());
		editTableMenu.add(renameTableMenuItem);
		editTableMenu.add(dropTableMenuItem);
		editTableMenu.add(truncateTableMenuItem);
		editTableMenu.add(new JSeparator());
		editTableMenu.add(commentTableMenuItem);
		editTableMenu.add(copyTableMenuItem);

		exportTableDataMenu = new JMenu("Export Data");
		exportTableDataMenu.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "export_wiz.gif")));
		exportTableDataMenu.add(exportDataToInsertScriptMenuItem);
		exportTableDataMenu.add(exportDataToLoaderMenuItem);
		exportTableDataMenu.add(new JSeparator());
		exportTableDataMenu.add(exportDataToCsvMenuItem);
		exportTableDataMenu.add(exportDataToHtmlMenuItem);
		exportTableDataMenu.add(exportDataToTextMenuItem);
		exportTableDataMenu.add(exportDataToExcelMenuItem);
		exportTableDataMenu.add(new JSeparator());
		exportTableDataMenu.add(exportDataToXmlMenuItem);
		
		exportTableDdlMenu = new JMenu("Export DDL");
		exportTableDdlMenu.add(exportDdlToFileMenuItem);
		exportTableDdlMenu.add(exportDdlToClipBoardMenuItem);
		exportTableDdlMenu.add(new JSeparator());
		exportTableDdlMenu.add(exportDdlToSqlTabMenuItem);
		
		
		tablePopupMenu = new JPopupMenu();
		tablePopupMenu.add(expandCollaspTableMenuItem);
		tablePopupMenu.add(new JSeparator());
		tablePopupMenu.add(openTableDetailsMenuItem);
		tablePopupMenu.add(showTableContentMenuItem);
		tablePopupMenu.add(new JSeparator());
		tablePopupMenu.add(editTableMenu);
		tablePopupMenu.add(new JSeparator());
		tablePopupMenu.add(importDataToTableMenuItem);
		tablePopupMenu.add(new JSeparator());
		tablePopupMenu.add(exportTableDataMenu);
		tablePopupMenu.add(exportTableDdlMenu);
		tablePopupMenu.add(exportTableStructureMenuItem);
		
		
		expandCollapseFolderMenuItem = new JMenuItem("Expand");
		expandCollapseFolderMenuItem.setIcon(EXPAND_IMAGE);
		expandCollapseFolderMenuItem.addActionListener(this);
		expandCollapseAllFolderMenuItem = new JMenuItem("Expand All");
		expandCollapseAllFolderMenuItem.setIcon(EXPAND_ALL_IMAGE);
		expandCollapseAllFolderMenuItem.addActionListener(this);
		addTableMenuItem = new JMenuItem("Add Table");
		addTableMenuItem.setIcon(new ImageIcon(getClass()
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "add_obj.gif")));
		addTableMenuItem.addActionListener(this);
		
		folderPopupMenu  = new JPopupMenu();
		folderPopupMenu.add(expandCollapseFolderMenuItem);
		folderPopupMenu.add(expandCollapseAllFolderMenuItem);
		folderPopupMenu.add(new JSeparator());
		folderPopupMenu.add(addTableMenuItem);
		
		//getDatabaseDirectoryTree().add(dbDirectoryTreePopupMenu);
		//getDatabaseDirectoryTree().add(columnPopupMenu);
		//getDatabaseDirectoryTree().add(tablePopupMenu);
		
		
        
		databaseDirectoryToolBar.setFloatable(false);
		setLayout(new GridBagLayout());
		Icon image = null;
		GridBagConstraints gridBagConstraints = null;
		
		refreshTreeButton.setFocusable(false);
		image = new ImageIcon(MenuBarUtil.class
				.getResource(OracleGuiConstants.IMAGE_PATH
						+ "reload_green.png"));
		refreshTreeButton.setIcon(image);
		refreshTreeButton.addActionListener(this);
		databaseDirectoryToolBar.add(refreshTreeButton);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		add(databaseDirectoryToolBar, gridBagConstraints);
		
		JScrollPane jScrollPane1 = new JScrollPane();
		jScrollPane1.setViewportView(getDatabaseDirectoryTree());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		add(jScrollPane1, gridBagConstraints);
	}
	
	private JButton refreshTreeButton;
	private JToolBar databaseDirectoryToolBar;
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(GuiCommandConstants.EXPAND_PATH_ACTION.equalsIgnoreCase(e.getActionCommand())){
			if (mouseClickedTreePath == null) {
                return;
            }
            if (!getDatabaseDirectoryTree().isExpanded(mouseClickedTreePath)) {
                getDatabaseDirectoryTree().expandPath(mouseClickedTreePath);
            }
		}else if(GuiCommandConstants.COLLAPSE_PATH_ACTION.equalsIgnoreCase(e.getActionCommand())){
			if (mouseClickedTreePath == null) {
                return;
            }
            if (getDatabaseDirectoryTree().isExpanded(mouseClickedTreePath)) {
            	getDatabaseDirectoryTree().collapsePath(mouseClickedTreePath);
            }
		}else if(e.getSource().equals(refreshTreeButton)){
			reloadDatabaseTree();
		}else if(e.getSource().equals(openTableDetailsMenuItem)){
			openTableFromTreePath(mouseClickedTreePath);
		}else if(e.getSource().equals(showTableContentMenuItem)){
			showTableContentFromTreePath(mouseClickedTreePath);
		}else if(e.getSource().equals(exportDataToInsertScriptMenuItem)){
			exportTableData(mouseClickedTreePath, TableDataExportTypeEnum.INSERT_STATEMENT);
		}else if(e.getSource().equals(exportDataToLoaderMenuItem)){
			exportTableData(mouseClickedTreePath, TableDataExportTypeEnum.SQL_LOADER);
		}else if(e.getSource().equals(exportDataToCsvMenuItem)){
			exportTableData(mouseClickedTreePath, TableDataExportTypeEnum.CSV);
		}else if(e.getSource().equals(exportDataToHtmlMenuItem)){
			exportTableData(mouseClickedTreePath, TableDataExportTypeEnum.HTML);
		}else if(e.getSource().equals(exportDataToTextMenuItem)){
			exportTableData(mouseClickedTreePath, TableDataExportTypeEnum.TEXT);
		}else if(e.getSource().equals(exportDataToExcelMenuItem)){
			exportTableData(mouseClickedTreePath, TableDataExportTypeEnum.EXCEL);
		}else if(e.getSource().equals(exportDataToXmlMenuItem)){
			exportTableData(mouseClickedTreePath, TableDataExportTypeEnum.XML);
		}
	}
	
	public void exportTableData(TreePath clickedPath,
			TableDataExportTypeEnum exportTypeEnum) {
		Table table = getTableFromTreePath(clickedPath);
		if(table != null){
			TableDataExportDialog dataExportDialog = new TableDataExportDialog(
					getParentFrame(), table, exportTypeEnum, connectionProperties
				);
			dataExportDialog.setVisible(true);
		}
	}

	public void showTableContentFromTreePath(TreePath clickedPath){
		DefaultMutableTreeNode node = getDatabaseDirectoryTree().getTreeNode(clickedPath);
        if(node == null)
            return;
        DatabaseNode dbNode = getDatabaseDirectoryTree().getDatabaseNode(node);
        if (dbNode == null) {
            return;
        }
        if(dbNode instanceof TableNode){
        	Table table = ((TableNode)dbNode).getTable();
			if(table != null){
				TableContentPanel contentPanel = new TableContentPanel(
					table.getSchemaName(), table.getModelName(), connectionProperties	
				);
				DatabaseViewerInternalFrame iFrame = (DatabaseViewerInternalFrame) getParentComponent();
				iFrame.getDbDetailsTabbedPane().addTab(CONTENT_TEXT + table.getModelName(), contentPanel);
				int n = iFrame.getDbDetailsTabbedPane().getTabCount();
				iFrame.getDbDetailsTabbedPane().setTabComponentAt(n - 1,
		                new ButtonTabComponent(iFrame.getDbDetailsTabbedPane(), new ImageIcon(DatabaseViewerInternalFrame.class
		        				.getResource(OracleGuiConstants.IMAGE_PATH + "sampleContents.gif"))));
				iFrame.getDbDetailsTabbedPane().setSelectedIndex(n-1);
				iFrame.getDbDetailsTabbedPane().updateUI();
			}
        }
	}
	
	private Table getTableFromTreePath(TreePath clickedPath){
		DefaultMutableTreeNode node = getDatabaseDirectoryTree().getTreeNode(clickedPath);
        if(node == null)
            return null;
        DatabaseNode dbNode = getDatabaseDirectoryTree().getDatabaseNode(node);
        if (dbNode == null) {
            return null;
        }
        if(dbNode instanceof TableNode){
        	Table table = ((TableNode)dbNode).getTable();
			if(table != null){
				return table;
			}
        }
        return null;
	}
	
	public void openTableFromTreePath(TreePath clickedPath){
		DefaultMutableTreeNode node = getDatabaseDirectoryTree().getTreeNode(clickedPath);
        if(node == null)
            return;
        DatabaseNode dbNode = getDatabaseDirectoryTree().getDatabaseNode(node);
        if (dbNode == null) {
            return;
        }
        if(dbNode instanceof TableNode){
        	Table table = ((TableNode)dbNode).getTable();
			if(table != null){
				openTableDetails(table);
			}
        }
	}

	public void reloadDatabaseTree(){
		try {
			Database db = new OracleDbGrabber().grabDatabase(
					connectionProperties.getDataSource().getConnection(), 
					connectionProperties.getDatabaseName());
			databaseDirectoryTree.reload(db);
			if(mouseClickedTreePath != null){
				databaseDirectoryTree.expandPath(mouseClickedTreePath);
				databaseDirectoryTree.updateUI();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public DatabaseDirectoryTree getDatabaseDirectoryTree() {
		return databaseDirectoryTree;
	}

	@Override
	public void valueChanged(TreeSelectionEvent event) {
		DefaultMutableTreeNode node = getDatabaseDirectoryTree().getTreeNode(event.getPath());
		DatabaseNode<Table> tableNode =  getDatabaseDirectoryTree().getDatabaseNode(node);
		if(tableNode == null)
			return;
		if(tableNode instanceof TableNode){
			Table t = ((TableNode)tableNode).getTable();
			if(t != null){
				String tableName = t.getModelName();
				
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource().equals(getDatabaseDirectoryTree())){
            TreePath p = getDatabaseDirectoryTree().getSelectionPath();
            if(p==null)
                return;
            mouseClickedTreePath = p;
            DefaultMutableTreeNode node = getDatabaseDirectoryTree().getTreeNode(p);
            if(node == null)
                return;
            DatabaseNode dbNode = getDatabaseDirectoryTree().getDatabaseNode(node);
            if (dbNode == null) {
                return;
            }
            if(MouseEvent.BUTTON1 == e.getButton()){
                if(dbNode instanceof TableNode){
                    if(e.getClickCount() == 2){
                    	Table table = ((TableNode)dbNode).getTable();
            			if(table != null){
            				openTableDetails(table);
            			}
                    }
                }
            }
        }
	}

	private void openTableDetails(Table table) {
		DatabaseViewerInternalFrame iFrame = (DatabaseViewerInternalFrame) getParentComponent();
		if(iFrame != null){
			boolean tableOpened = false;
			int selectedTabIndex = -1;
			int tabCount = iFrame.getDbDetailsTabbedPane().getTabCount();
			for (int i = 0; i < tabCount; i++) {
				Component tabComponent = iFrame.getDbDetailsTabbedPane().getComponentAt(i);
				if(tabComponent instanceof TableDetailsPanel){
					TableDetailsPanel ti = (TableDetailsPanel) tabComponent;
					if(ti != null){
						if(table.getModelName().equals(ti.getTableName())){
							tableOpened = true;
							selectedTabIndex = i;
							break;
						}
					}
				}
			}
			if(!tableOpened){
				TableDetailsPanel panel = new TableDetailsPanel(getParentFrame(), table, iFrame.getConnectionProperties());
				iFrame.getDbDetailsTabbedPane().addTab(table.getModelName(), panel);
				int n = iFrame.getDbDetailsTabbedPane().getTabCount();
				iFrame.getDbDetailsTabbedPane().setTabComponentAt(n - 1,
		                new ButtonTabComponent(iFrame.getDbDetailsTabbedPane(), new ImageIcon(DatabaseViewerInternalFrame.class
		        				.getResource(OracleGuiConstants.IMAGE_PATH + "table.gif"))));
				selectedTabIndex = iFrame.getDbDetailsTabbedPane().getTabCount() - 1;
			}else{
				// call the refresh method of this tab
			}
			if(selectedTabIndex > -1){
				iFrame.getDbDetailsTabbedPane().setSelectedIndex(selectedTabIndex);
				iFrame.getDbDetailsTabbedPane().updateUI();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.isPopupTrigger()){
			int x = e.getX();
            int y = e.getY();
            TreePath path = getDatabaseDirectoryTree().getPathForLocation(x, y);
            if (path == null) {
                return;
            }
            mouseClickedTreePath = path;
            DefaultMutableTreeNode node = getDatabaseDirectoryTree().getTreeNode(path);
            if (node == null) {
                return;
            }
            DatabaseNode dbNode = getDatabaseDirectoryTree().getDatabaseNode(node);
            if(dbNode == null){
            	return;
            } else {
            	getDatabaseDirectoryTree().remove(tablePopupMenu);
            	getDatabaseDirectoryTree().remove(columnPopupMenu);
            	if(dbNode instanceof TableNode){
            		if (getDatabaseDirectoryTree().isExpanded(path)) {
            			expandCollaspTableMenuItem.setText("Collapse");
            			expandCollaspTableMenuItem.setIcon(COLLAPSE_IMAGE);
            			expandCollaspTableMenuItem.setActionCommand(GuiCommandConstants.COLLAPSE_PATH_ACTION);
                    } else {
                    	expandCollaspTableMenuItem.setText("Expand");
                    	expandCollaspTableMenuItem.setIcon(EXPAND_IMAGE);
                    	expandCollaspTableMenuItem.setActionCommand(GuiCommandConstants.EXPAND_PATH_ACTION);
                    }
            		tablePopupMenu.show(getDatabaseDirectoryTree(), x, y);
            	} else if(dbNode instanceof ColumnNode){
            		columnPopupMenu.show(getDatabaseDirectoryTree(), x, y);
            	} else if(dbNode instanceof FolderNode){
            		if (getDatabaseDirectoryTree().isExpanded(path)) {
            			expandCollapseFolderMenuItem.setText("Collapse");
            			expandCollapseFolderMenuItem.setIcon(COLLAPSE_IMAGE);
            			expandCollapseFolderMenuItem.setActionCommand(GuiCommandConstants.COLLAPSE_PATH_ACTION);
                    } else {
                    	expandCollapseFolderMenuItem.setText("Expand");
                    	expandCollapseFolderMenuItem.setIcon(EXPAND_IMAGE);
                    	expandCollapseFolderMenuItem.setActionCommand(GuiCommandConstants.EXPAND_PATH_ACTION);
                    }
            		folderPopupMenu.show(getDatabaseDirectoryTree(), x, y);
            	}
            }
		}
	}
	
	private void directoryTreePopupPopupMenuWillBecomeVisible(PopupMenuEvent evt) {                                                              
        if(evt.getSource().equals(getDatabaseDirectoryTree())){
            TreePath p = getDatabaseDirectoryTree().getSelectionPath();
            if(p==null)
                return;
            DefaultMutableTreeNode node = getDatabaseDirectoryTree().getTreeNode(p);
            if(node == null)
                return;
            /*FileNode fnode = directoryTree.getFileNode(node);
            if (fnode == null) {
                return;
            }*/
            
        }
    }                                                             

    private void directoryTreePopupComponentShown(ComponentEvent evt) {                                                  
        if(evt.getSource().equals(getDatabaseDirectoryTree())){
            TreePath p = getDatabaseDirectoryTree().getSelectionPath();
            if(p==null)
                return;
            DefaultMutableTreeNode node = getDatabaseDirectoryTree().getTreeNode(p);
            if(node == null)
                return;
            /*FileNode fnode = directoryTree.getFileNode(node);
            if (fnode == null) {
                return;
            }*/

        }
    }
    
    
    
    
    class PopupTrigger extends MouseAdapter {

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                int x = e.getX();
                int y = e.getY();
                TreePath path = getDatabaseDirectoryTree().getPathForLocation(x, y);
                if (path != null) {
                    if (getDatabaseDirectoryTree().isExpanded(path)) {
                        //m_action.putValue(Action.NAME, "Collapse");
                    } else {
                        //m_action.putValue(Action.NAME, "Expand");
                    }
                    //m_popup.show(m_tree, x, y);
                    //m_clickedPath = path;
                }
            }
        }
    }




	public JComponent getParentComponent() {
		return parentComponent;
	}

	public void setParentComponent(JComponent parentComponent) {
		this.parentComponent = parentComponent;
	}

	/**
	 * @return the parentFrame
	 */
	public JFrame getParentFrame() {
		return parentFrame;
	}

	/**
	 * @param parentFrame the parentFrame to set
	 */
	public void setParentFrame(JFrame parentFrame) {
		this.parentFrame = parentFrame;
	}

}