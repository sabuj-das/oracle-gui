/**
 * 
 */
package com.gs.oracle.comps;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author sabuj.das
 *
 */
public class ResultSetTableModelFactory {

	private Connection connection;

	public ResultSetTableModelFactory(Connection connection) {
		this.connection = connection;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public ResultSetTableModel getResultSetTableModel(String query) throws SQLException{
		// If we've called close(), then we can't call this method
		if (connection == null)
			throw new IllegalStateException("Connection already closed.");

		// Create a Statement object that will be used to excecute the query.
		// The arguments specify that the returned ResultSet will be
		// scrollable, read-only, and insensitive to changes in the db.
		Statement statement = connection.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		// Run the query, creating a ResultSet
		ResultSet r = statement.executeQuery(query);
		// Create and return a TableModel for the ResultSet
		return new ResultSetTableModel(r);
	}
}
