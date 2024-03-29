/**
 * 
 */
package com.gs.oracle.accesscontrol;

import java.sql.Connection;
import java.sql.SQLException;

import com.gs.oracle.common.StringUtil;
import com.gs.oracle.connection.ConnectionProperties;
import com.gs.oracle.jdbc.JdbcUtil;
import com.gs.oracle.model.Column;
import com.gs.oracle.model.Table;
import com.gs.oracle.security.DatabaseAccessControl;

/**
 * @author Sabuj Das
 *
 */
public class AuthorizationController {

	private static AuthorizationController instance;
	
	private Authorization authorization;
	private ConnectionProperties connectionProperties;
	private String schemaName;
	
	private AuthorizationController() {
		
	}
	
	public static AuthorizationController getInstance(){
		if(instance == null)
			instance = new AuthorizationController();
		return instance;
	}
	
	public void loadAuthorization(){
		if(connectionProperties == null
				|| !StringUtil.hasValidContent(schemaName)){
			authorization = null;
			return;
		}
		DatabaseAccessControl accessControl = new DatabaseAccessControl();
		Connection connection = null;
		try {
			connection = connectionProperties.getDataSource().getConnection();
			authorization = accessControl.grabDatabaseAuthorization(connection, schemaName);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			JdbcUtil.closeConnection(connection);
		}
	}
	
	public <T> boolean hasAccess(T t, String permissionName){
		if(authorization == null){
			return true;
		}
		if(t instanceof Table){
			return hasAccessForTable((Table) t, permissionName);
		}else if(t instanceof Column){
			return hasAccessForColumn((Column) t, permissionName);
		}
		return true;
	}
	
	private boolean hasAccessForTable(Table t, String permissionName){
		return true;
	}
	
	private boolean hasAccessForColumn(Column c, String permissionName){
		return true;
	}

	public ConnectionProperties getConnectionProperties() {
		return connectionProperties;
	}

	public void setConnectionProperties(ConnectionProperties connectionProperties) {
		this.connectionProperties = connectionProperties;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	
	
	
}
