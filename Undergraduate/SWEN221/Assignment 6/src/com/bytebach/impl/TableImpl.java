package com.bytebach.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bytebach.model.Field;
import com.bytebach.model.IntegerValue;
import com.bytebach.model.ReferenceValue;
import com.bytebach.model.Table;
import com.bytebach.model.Value;

public class TableImpl implements Table {

	private String name;
	private List<Field> fields;
	private MyDatabase db;	
	private TableRows rows;
	
	public TableImpl(String n, List<Field> f, MyDatabase myDatabase) {
		name = n;
		fields = f;
		db = myDatabase;
		
		rows = new TableRows(this, fields, db);
	}
	
	@Override
	public String name() {
		return name;
	}

	@Override
	public List<Field> fields() {
		return fields;
	}

	@Override
	public List<List<Value>> rows() {
		return rows;
	}

	@Override
	public List<Value> row(Value... keys) {
		if (keys.length != 0) {
			if (keys[0] instanceof IntegerValue) {
				IntegerValue i = (IntegerValue) keys[0];
				
				return rows.get(i.value());
			}
		}
		
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TableImpl)) {
			return false;
		}
		TableImpl other = (TableImpl) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public void delete(Value... keys) {
		
		for (int i = 0; i < rows.size(); i++) {
			TableRow row = rows.get(i);
			
			for (int j = 0; j < fields.size(); j++) {
				if (fields.get(j).isKey()) {
					if (!keys[j].equals(row.get(j))) {
						break;
					}
				} else {
					rows.remove(i);
				}
			}
		}
		
//		if (keys.length != 0) {
//			if (keys[0] instanceof IntegerValue) {
//				IntegerValue i = (IntegerValue) keys[0];
//				
//				rows.remove(i.value());
//			}
//		}
	}

}
