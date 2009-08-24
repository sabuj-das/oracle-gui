/** ***************************************************************************
 *		Oracle GUI	
 *	
 *	File	: BaseDbModel.java
 *	Type	: com.gs.oracle.BaseDbModel.java
 *	Date	: Jul 29, 2009	1:04:03 PM
 *
 *	Author	: Green Moon
 *
 *	
 *****************************************************************************/
package com.gs.oracle;

import java.io.Serializable;

/**
 * Type Name	: com.gs.oracle.BaseDbModel
 *
 */
public abstract class BaseDbModel implements Serializable {

	private String modelName;
	private String comments;
	private boolean deleted = false;
	
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	@Override
	public String toString() {
		return getModelName();
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
