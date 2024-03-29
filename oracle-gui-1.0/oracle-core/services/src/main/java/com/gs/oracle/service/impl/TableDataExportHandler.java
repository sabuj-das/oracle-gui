/**
 * 
 */
package com.gs.oracle.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;

import com.gs.oracle.connection.ConnectionProperties;
import com.gs.oracle.io.IOUtils;
import com.gs.oracle.service.vo.XmlColumnVo;
import com.gs.oracle.service.vo.XmlResultsVo;
import com.gs.oracle.service.vo.XmlRowVo;

/**
 * @author sabuj.das
 *
 */
public class TableDataExportHandler {
	
	public static final String INSERT_DATE_FORMAT = "dd-MMM-yy HH.mm.ss.SSS aaa";
	private static final String SQL_DATE_FORMAT = "'DD-MON-RR HH.MI.SS.FF AM'";
	private static final String SQL_DATE_FUNCTION = "to_timestamp";
	
	private ConnectionProperties connectionProperties;
	private String schemaName, tableName;
	
	public TableDataExportHandler(ConnectionProperties cp) {
		connectionProperties = cp;
	}

	/**
	 * @return the connectionProperties
	 */
	public ConnectionProperties getConnectionProperties() {
		return connectionProperties;
	}

	/**
	 * @param connectionProperties the connectionProperties to set
	 */
	public void setConnectionProperties(ConnectionProperties connectionProperties) {
		this.connectionProperties = connectionProperties;
	}

	/**
	 * @return the schemaName
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param schemaName the schemaName to set
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void exportToInsertStatement(String exportQuery, File outputFile){
		Connection connection = null;
		ResultSet resultSet = null;
		List<StringBuffer> insertStmtList = new ArrayList<StringBuffer>();
		StringBuffer resultBuffer = new StringBuffer("INSERT INTO ")
		.append(schemaName).append(".").append(tableName).append(" ( ");
		try{
			connection = getConnectionProperties().getDataSource().getConnection();
			PreparedStatement ps = connection.prepareStatement(exportQuery,
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = ps.executeQuery();
			
			if(resultSet == null){
				return;
			}
			
			ResultSetMetaData metaData = resultSet.getMetaData();
			
			int columnCount = metaData.getColumnCount();
			for(int i = 1; i<= columnCount; i++){
				resultBuffer.append(metaData.getColumnName(i));
				if(i != columnCount){
					resultBuffer.append(", ");
				}
			}
			resultBuffer.append(" ) VALUES ( "); 
			resultSet.first();
			while(resultSet.next()){
				StringBuffer rowResultBuffer = new StringBuffer(resultBuffer.toString());
				for(int i = 1; i<= columnCount; i++){
					int columnType = metaData.getColumnType(i);
					Object o = resultSet.getObject(i);
					String value = "null";
					if(o != null)
						value = o.toString();
					rowResultBuffer.append(getStringForObject(o, columnType));
					if(i != columnCount){
						rowResultBuffer.append(", ");
					}else{
						rowResultBuffer.append(" );");
					}
				}
				insertStmtList.add(rowResultBuffer);
			}
		} catch(SQLException e){
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(insertStmtList.size() > 0){
			StringBuffer b = new StringBuffer();
			for (StringBuffer sb : insertStmtList) {
				b.append(sb).append("\n");
			}
			IOUtils.writeAsText(outputFile, b.toString());
			
		}
	}
	
	public static String getStringForObject(Object o, int type){
		String value = "";
		if(o == null){
			return "null";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(INSERT_DATE_FORMAT);
		switch(type){
			case Types.SMALLINT:
			case Types.INTEGER:
			case Types.DECIMAL:
			case Types.BIGINT:
			case Types.REAL:
			case Types.DOUBLE:
			case Types.NUMERIC:
				value = o.toString();
				break;
			
			case Types.DATE:
			case Types.TIME:
			case Types.TIMESTAMP:
				java.sql.Timestamp sqlTimestamp = (java.sql.Timestamp) o;
				java.util.Date utilDate = new java.util.Date();
				utilDate.setTime(sqlTimestamp.getTime());
				value = SQL_DATE_FUNCTION + "('" +
					dateFormat.format(utilDate) + "', " + SQL_DATE_FORMAT + ")";
				break;
			
			default:
				value = "'" + o.toString() + "'";
				break;
		}
		return value;
	}
	
	
	public void exportToTEXT(String exportQuery, File outputFile, boolean isCsv, char separator){
		Connection connection = null;
		ResultSet resultSet = null;
		
		List<StringBuffer> rowDataList = new ArrayList<StringBuffer>();
		try{
			connection = getConnectionProperties().getDataSource().getConnection();
			PreparedStatement ps = connection.prepareStatement(exportQuery,
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = ps.executeQuery();
			
			if(resultSet == null){
				return;
			}
			
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			int columnCount = resultSetMetaData.getColumnCount();
			StringBuffer rowData = new StringBuffer("");
			for (int i = 1; i <= columnCount; i++) {
				rowData.append(resultSetMetaData.getColumnName(i));
				if(i != columnCount){
					rowData.append(",");
				}
			}
			rowData.append("\r\n");
			rowDataList.add(rowData);
			
			while(resultSet.next()){
				rowData = new StringBuffer("");
				for(int i = 1; i<= columnCount; i++){
					int columnType = resultSetMetaData.getColumnType(i);
					Object object = resultSet.getObject(i);
					rowData.append(getCsvStringForObject(object, columnType, isCsv));
					if(i != columnCount){
						rowData.append(separator);
					}
				}
				rowData.append("\r\n");
				rowDataList.add(rowData);
			}
			
		} catch(SQLException e){
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		if(rowDataList.size() > 0){
			StringBuffer b = new StringBuffer();
			for (StringBuffer sb : rowDataList) {
				b.append(sb);
			}
			IOUtils.writeAsText(outputFile, b.toString());
			
		}
	}
	
	private Object getCsvStringForObject(Object o, int columnType, boolean isCsv) {
		String value = "";
		if(o == null){
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(INSERT_DATE_FORMAT);
		switch(columnType){
			case Types.SMALLINT:
			case Types.INTEGER:
			case Types.DECIMAL:
			case Types.BIGINT:
			case Types.REAL:
			case Types.DOUBLE:
			case Types.NUMERIC:
				value = o.toString();
				break;
			
			case Types.DATE:
			case Types.TIME:
			case Types.TIMESTAMP:
				java.sql.Timestamp sqlTimestamp = (java.sql.Timestamp) o;
				java.util.Date utilDate = new java.util.Date();
				utilDate.setTime(sqlTimestamp.getTime());
				value = dateFormat.format(utilDate);
				break;
			
			default:
				value = o.toString();
				break;
		}
		//TODO: Format as CSV
		if(isCsv)
			value = "\"" + o.toString() + "\"";
		
		return value;
	}

	
	public void exportToHTML(String exportQuery, File outputFile){
		
		Connection connection = null;
		ResultSet resultSet = null;
		StringBuffer rowData = new StringBuffer("<html>\n<head>\n<META http-equiv=\"Content-Type\" content=\"text/html; charset=Cp1252\">\n</head>\n<body><table border=\"1\">\n");
		try{
			connection = getConnectionProperties().getDataSource().getConnection();
			PreparedStatement ps = connection.prepareStatement(exportQuery,
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = ps.executeQuery();
			
			if(resultSet == null){
				return;
			}
			
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			int columnCount = resultSetMetaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				rowData.append("\t<TH>").append(resultSetMetaData.getColumnName(i)).append("</TH>\n");
			}
			while(resultSet.next()){
				rowData.append("<TR>\n");
				for (int i = 1; i <= columnCount; i++) {
					int columnType = resultSetMetaData.getColumnType(i);
					Object object = resultSet.getObject(i);
					rowData.append("\t<TD>")
						.append(getHTMLStringForObject(object, columnType))
						.append("</TD>\n");
				}
				rowData.append("</TR>\n");
			}
			rowData.append("</table></body></html>");
		} catch(SQLException e){
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		IOUtils.writeAsText(outputFile, rowData.toString());
		
	}
	
	private Object getHTMLStringForObject(Object o, int columnType) {
		String value = "";
		if(o == null){
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(INSERT_DATE_FORMAT);
		switch(columnType){
			case Types.SMALLINT:
			case Types.INTEGER:
			case Types.DECIMAL:
			case Types.BIGINT:
			case Types.REAL:
			case Types.DOUBLE:
			case Types.NUMERIC:
				value = o.toString();
				break;
			
			case Types.DATE:
			case Types.TIME:
			case Types.TIMESTAMP:
				java.sql.Timestamp sqlTimestamp = (java.sql.Timestamp) o;
				java.util.Date utilDate = new java.util.Date();
				utilDate.setTime(sqlTimestamp.getTime());
				value = dateFormat.format(utilDate);
				break;
			
			default:
				value = o.toString();
				break;
		}
		//TODO: Format as HTML to remove special character
		
		return value;
	}
	
	
	public void exportToXML(String exportQuery, File outputFile){
		
		Connection connection = null;
		ResultSet resultSet = null;
		XmlResultsVo resultsVo = new XmlResultsVo();
		StringBuffer resultBuffer = new StringBuffer("");
		try{
			connection = getConnectionProperties().getDataSource().getConnection();
			PreparedStatement ps = connection.prepareStatement(exportQuery,
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = ps.executeQuery();
			
			if(resultSet == null){
				return;
			}
			
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			int columnCount = resultSetMetaData.getColumnCount();
			while(resultSet.next()){
				XmlRowVo rowVo = new XmlRowVo();
				for (int i = 1; i <= columnCount; i++) {
					int columnType = resultSetMetaData.getColumnType(i);
					String columnName = resultSetMetaData.getColumnName(i);
					Object object = resultSet.getObject(i);
					XmlColumnVo columnVo = new XmlColumnVo();
					columnVo.setColumnName(columnName);
					columnVo.setColumnValue(object);
					rowVo.getXmlColumnList().add(columnVo);
				}
				resultsVo.getXmlRowList().add(rowVo);
			}
		} catch(SQLException e){
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		Mapping mapping = new Mapping();
		
		try {
			mapping.loadMapping(getClass().getResource("/castor/castor-resultset-mapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(new FileOutputStream(outputFile)));
			marshaller.setMapping(mapping);
			marshaller.marshal(resultsVo);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MappingException e) {
			e.printStackTrace();
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
		

	}
	
	public void exportToExcel(String exportQuery, File outputFile){
		
		Connection connection = null;
		ResultSet resultSet = null;
		List<StringBuffer> insertStmtList = new ArrayList<StringBuffer>();
		StringBuffer resultBuffer = new StringBuffer("INSERT INTO ")
		.append(schemaName).append(".").append(tableName).append(" ( ");
		try{
			connection = getConnectionProperties().getDataSource().getConnection();
			PreparedStatement ps = connection.prepareStatement(exportQuery,
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = ps.executeQuery();
			
			if(resultSet == null){
				return;
			}
			
			
		} catch(SQLException e){
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
}