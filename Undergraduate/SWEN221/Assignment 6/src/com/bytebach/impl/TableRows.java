package com.bytebach.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.bytebach.model.BooleanValue;
import com.bytebach.model.Field;
import com.bytebach.model.IntegerValue;
import com.bytebach.model.InvalidOperation;
import com.bytebach.model.ReferenceValue;
import com.bytebach.model.StringValue;
import com.bytebach.model.Value;

public class TableRows implements List<List<Value>> {

	private List<Field> fields;
	// private Map<List<Value>, Set<ReferenceValue>> refs;
	private Map<String, Set<String>> keys;
	private List<TableRow> rows;
	private MyDatabase db;
	private TableImpl table;

	public TableRows(TableImpl tableImpl, List<Field> f, MyDatabase database) {
		fields = f;
		db = database;
		table = tableImpl;

		rows = new ArrayList<TableRow>();

		// Construct the sorted sets of keys
		keys = new HashMap<String, Set<String>>();
		for (Field field : fields) {
			if (field.isKey()) {
				keys.put(field.title(), new TreeSet<String>());
			}
		}
	}

	@Override
	public boolean add(List<Value> row) {
		// Create the table row
		TableRow r = new TableRow(fields, keys.keySet(), row, table, db);

		// Ensure that the primary key isnt allready in the table
		for (int i = 0; i < fields.size(); i++) {
			if (fields.get(i).isKey()
					&& keys.get(fields.get(i).title()).contains(
							row.get(i).toString())) {
				throw new InvalidOperation("Primary key '"
						+ row.get(i).toString() + "' is allready in the table");
			}
		}

		// Try add the row to the list of rows
		if (!rows.add(r)) {
			return false;
		}

		// Update the primary key indexs
		for (int i = 0; i < fields.size(); i++) {
			if (fields.get(i).isKey()) {
				keys.get(fields.get(i).title()).add(row.get(i).toString());
			}
		}

		return true;
	}

	@Override
	public void add(int arg0, List<Value> arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean addAll(Collection<? extends List<Value>> arg0) {
		for (List<Value> l : arg0) {
			if (!add(l)) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends List<Value>> arg1) {
		throw new InvalidOperation("Not allowed to do that");
	}

	@Override
	public void clear() {
		rows.clear();

	}

	@Override
	public boolean contains(Object arg0) {
		return rows.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return rows.containsAll(arg0);
	}

	@Override
	public TableRow get(int i) {
		if (i < rows.size()) {
			return rows.get(i);
		}

		return null;
	}

	@Override
	public int indexOf(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public Iterator<List<Value>> iterator() {
		//return rows.iterator();
		return null;
	}

	@Override
	public int lastIndexOf(Object arg0) {
		return rows.lastIndexOf(arg0);
	}

	@Override
	public ListIterator<List<Value>> listIterator() {
		// return rows.listIterator();
		return null;
	}

	@Override
	public ListIterator<List<Value>> listIterator(int arg0) {
		// return rows.listIterator(arg0);
		return null;
	}

	@Override
	public boolean remove(Object arg0) {
		throw new InvalidOperation("Not allowed to do that");
	}

	@Override
	public TableRow remove(int arg0) {
		TableRow removed = rows.remove(arg0);

		// Remove the priamary key(s) from the database
		for (int i = 0; i < fields.size(); i++) {
			if (fields.get(i).isKey()) {
				keys.get(fields.get(i).title()).remove(
						removed.get(i).toString());
			}
		}

		// Remove all the refrences
		for (ReferenceValue ref : removed.refs) {
			db.table(ref.table()).delete(ref.keys());
		}

		// Construct a reference to this
		String tableName = table.name();
		Value[] vals = new Value[keys.size()];

		for (int j = 0; j < vals.length; j++) {
			vals[j] = removed.get(j);
		}

		ReferenceValue ref = new ReferenceValue(tableName, vals);

		// If we have a reference field then ensure that
		// there is no link to the removed from the referenced row

		for (int i = 0; i < removed.size(); i++) {
			if (removed.get(i) instanceof ReferenceValue) {

				ReferenceValue oldRef = (ReferenceValue) removed.get(i);
				TableRow r = (TableRow) db.table(oldRef.table()).row(
						oldRef.keys());

				if (r != null) {
					r.refs.remove(ref);
				}
			}
		}

		return removed;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Value> set(int arg0, List<Value> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		return rows.size();
	}

	@Override
	public List<List<Value>> subList(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
