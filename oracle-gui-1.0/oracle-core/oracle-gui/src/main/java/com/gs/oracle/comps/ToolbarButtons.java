/** ***************************************************************************
 *		Oracle GUI	
 *	
 *	File	: ToolbarButtons.java
 *	Type	: com.gs.oracle.comps.ToolbarButtons.java
 *	Date	: Aug 3, 2009	12:23:26 PM
 *
 *	Author	: Green Moon
 *
 *	
 *****************************************************************************/
package com.gs.oracle.comps;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import com.gs.oracle.OracleGuiConstants;
import com.gs.oracle.command.GuiCommandConstants;
import com.gs.oracle.command.GuiEventHandler;
import com.gs.oracle.util.MenuBarUtil;

/**
 * @author sabuj.das
 * 
 */
public class ToolbarButtons implements ActionListener{
	public static final String _TOOLBAR_BUTTON = "_TOOLBAR_BUTTON";

	public static final String NEW_CONNECTION_TOOLBAR_BUTTON = "NEW_CONNECTION_TOOLBAR_BUTTON";
	public static final String DISCONNECT_TOOLBAR_BUTTON = "DISCONNECT_TOOLBAR_BUTTON";
	public static final String DB_SYNC_TOOLBAR_BUTTON = "DB_SYNC_TOOLBAR_BUTTON";

	public static final String EXECUITE_CURRENT_SQL_TOOLBAR_BUTTON = "EXECUITE_CURRENT_SQL_TOOLBAR_BUTTON";
	public static final String EXECUITE_ALL_SQL_TOOLBAR_BUTTON = "EXECUITE_ALL_SQL_TOOLBAR_BUTTON";
	public static final String REFRESH_DATABASE_TOOLBAR_BUTTON = "REFRESH_DATABASE_TOOLBAR_BUTTON";
	public static final String BACKUP_DATABSE_TOOLBAR_BUTTON = "BACKUP_DATABSE_TOOLBAR_BUTTON";
	// public static final String _TOOLBAR_BUTTON = "";
	// public static final String _TOOLBAR_BUTTON = "";
	// public static final String _TOOLBAR_BUTTON = "";
	// public static final String _TOOLBAR_BUTTON = "";

	private Map<String, JButton> toolbarButtonsMap;
	private JFrame parentFrame;

	public ToolbarButtons(JFrame parent) {
		parentFrame = parent;
		toolbarButtonsMap = new HashMap<String, JButton>();
		createAllButtons();
	}

	private void createAllButtons() {
		toolbarButtonsMap.put(NEW_CONNECTION_TOOLBAR_BUTTON, createToolbarButton("", GuiCommandConstants.NEW_CONNECTION_ACT_CMD, "new_connection.gif", NEW_CONNECTION_TOOLBAR_BUTTON));
		toolbarButtonsMap.put(EXECUITE_CURRENT_SQL_TOOLBAR_BUTTON, createToolbarButton("", GuiCommandConstants.EXECUITE_CURRENT_SQL_ACT_CMD, "execution_obj.gif", EXECUITE_CURRENT_SQL_TOOLBAR_BUTTON));
		toolbarButtonsMap.put(DB_SYNC_TOOLBAR_BUTTON, createToolbarButton("", GuiCommandConstants.DB_SYNC_ACT_CMD, "repository-synchronize.gif", DB_SYNC_TOOLBAR_BUTTON));
		toolbarButtonsMap.put(REFRESH_DATABASE_TOOLBAR_BUTTON, createToolbarButton("", GuiCommandConstants.REFRESH_DATABASE_ACT_CMD, "Database_Refresh.gif", REFRESH_DATABASE_TOOLBAR_BUTTON));
		toolbarButtonsMap.put(BACKUP_DATABSE_TOOLBAR_BUTTON, createToolbarButton("", GuiCommandConstants.BACKUP_DATABSE_ACT_CMD, "BackupCD.gif", BACKUP_DATABSE_TOOLBAR_BUTTON));
	}

	public JButton createToolbarButton(String text, String command,
			String imageName) {
		return createToolbarButton(text, command, imageName, text
				+ _TOOLBAR_BUTTON);
	}

	public JButton createToolbarButton(String text, String command,
			String imageName, String name) {
		JButton button = new JButton(text);
		button.setActionCommand(command);
		if (null != imageName) {
			Icon image = new ImageIcon(MenuBarUtil.class
					.getResource(OracleGuiConstants.IMAGE_PATH + imageName));
			button.setIcon(image);
		}
		if (null != name) {
			button.setName(name);
		}
		button.addActionListener(this);
		return button;
	}

	public void addButton(String name, JButton button) {
		toolbarButtonsMap.put(name, button);
	}

	public JButton getButton(String name) {
		return toolbarButtonsMap.get(name);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		GuiEventHandler handler = new GuiEventHandler();
		handler.setParent(parentFrame);
		handler.actionPerformed(e);
	}
	
	
}
