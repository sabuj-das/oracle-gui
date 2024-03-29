/**
 * 
 */
package com.gs.oracle.comps;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.gs.oracle.model.Column;
import com.gs.oracle.model.ColumnHeader;
import com.gs.oracle.model.Constraint;

/**
 * @author sabuj.das
 *
 */
public class ConstraintDetailsTableModel implements TableModel {

	private List<Constraint> dataList = new ArrayList<Constraint>();
	private List<String> columnNameList = new ArrayList<String>(100);
	private int columnCount;
	private int rowCount;
	

	public ConstraintDetailsTableModel(List<Constraint> data) {
		this.dataList = data;
		prepareModel();
	}
	
	private void prepareModel(){
		rowCount = dataList.size();
		Method[] ms = Constraint.class.getMethods();
		for (Method method : ms) {
			if(method.getName().startsWith("get") ){
				ColumnHeader ch = method.getAnnotation(ColumnHeader.class);
				if(ch != null){
					columnNameList.add(ch.title());
				}
			}
		}
		columnCount = columnNameList.size();
		
	}
	
	

	
	@Override
	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		Method[] ms = Constraint.class.getMethods();
		for (Method method : ms) {
			if(method.getName().startsWith("get") && method.isAnnotationPresent(ColumnHeader.class)){
				ColumnHeader ch = method.getAnnotation(ColumnHeader.class);
				if(ch != null){
					if(columnIndex == ch.index())
						return method.getReturnType();
				}
			}
		}
		return String.class;
	}

	
	@Override
	public int getColumnCount() {
		return columnCount;
	}

	
	@Override
	public String getColumnName(int columnIndex) {
		Method[] ms = Constraint.class.getMethods();
		for (Method method : ms) {
			if(method.getName().startsWith("get") && method.isAnnotationPresent(ColumnHeader.class)){
				ColumnHeader ch = method.getAnnotation(ColumnHeader.class);
				if(ch != null){
					if(columnIndex == ch.index())
						return ch.title();
				}
			}
		}
		return "";
	}

	@Override
	public int getRowCount() {
		return rowCount;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		Constraint constraint = dataList.get(rowIndex);
		Method[] ms = constraint.getClass().getMethods();
		for (Method method : ms) {
			if(method.getName().startsWith("get") && method.isAnnotationPresent(ColumnHeader.class)){
				ColumnHeader ch = method.getAnnotation(ColumnHeader.class);
				if(ch != null){
					if(columnIndex == ch.index())
						try {
							return method.invoke(constraint, null);
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
				}
			}
		}
		return "";
	}

	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub

	}


	public List<String> getColumnNameList() {
		return columnNameList;
	}

	public void setColumnNameList(List<String> columnNameList) {
		this.columnNameList = columnNameList;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public List<Constraint> getDataList() {
		return dataList;
	}

	public void setDataList(List<Constraint> dataList) {
		this.dataList = dataList;
	}




}
