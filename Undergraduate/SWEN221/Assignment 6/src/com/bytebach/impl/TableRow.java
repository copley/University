package com.bytebach.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import com.bytebach.model.BooleanValue;
import com.bytebach.model.Field;
import com.bytebach.model.IntegerValue;
import com.bytebach.model.InvalidOperation;
import com.bytebach.model.ReferenceValue;
import com.bytebach.model.StringValue;
import com.bytebach.model.Table;
import com.bytebach.model.Value;

public class TableRow implements List<Value> {

	private Table table;
	private List<Field> fields;
	private Set<String> keys;
	private List<Value> values;
	private MyDatabase db;

	public List<ReferenceValue> refs;

	public TableRow(List<Field> f, Set<String> k, List<Value> v, TableImpl t,
			MyDatabase database) {
		fields = f;
		keys = k;
		values = v;
		db = database;
		table = t;
		refs = new ArrayList<ReferenceValue>();

		for (int i = 0; i < values.size(); i++) {
			// Ensure that the row type is correct
			if (fields.get(i).type() == Field.Type.INTEGER
					&& !(values.get(i) instanceof IntegerValue)) {
				throw new InvalidOperation("Incorrect field type");
			} else if (fields.get(i).type() == Field.Type.TEXT
					&& (!(values.get(i) instanceof StringValue) || values
							.get(i).toString().indexOf("\n") != -1)) {
				throw new InvalidOperation("Incorrect field type");
			} else if (fields.get(i).type() == Field.Type.TEXTAREA
					&& !(values.get(i) instanceof StringValue)) {
				throw new InvalidOperation("Incorrect field type");
			} else if (fields.get(i).type() == Field.Type.BOOLEAN
					&& !(values.get(i) instanceof BooleanValue)) {
				throw new InvalidOperation("Incorrect field type");
			} else if (fields.get(i).type() == Field.Type.INTEGER
					&& !(values.get(i) instanceof IntegerValue)) {
				throw new InvalidOperation("Incorrect field type");
			} else if (fields.get(i).type() == Field.Type.REFERENCE
					&& !(values.get(i) instanceof ReferenceValue)) {
				throw new InvalidOperation("Incorrect field type");
			}

			// Ensure that if the type is reference then it is a valid one
			if (values.get(i) instanceof ReferenceValue) {
				ReferenceValue ref = (ReferenceValue) values.get(i);

				if (ref.keys().length != this.keys.size()) {
					throw new InvalidOperation("Wrong number of keys");
				}

				// Ensure the table exsists
				if (db.table(ref.table()) == null) {
					throw new InvalidOperation(
							"Reference to table that dosnt exsist");
				}

				// Ensure that the row referenced exsists
				if (db.table(ref.table()).row(ref.keys()) == null) {
					throw new InvalidOperation(
							"Reference to row that dosnt exsist");
				}

				// Tell the other table about the reference
				// db.table(ref.table())

				// Construct a reference
				String tableName = table.name();
				Value[] vals = new Value[keys.size()];

				for (int j = 0; j < vals.length; j++) {
					vals[j] = values.get(j);
				}

				ReferenceValue newRef = new ReferenceValue(tableName, vals);

				TableRow r = (TableRow) db.table(ref.table()).row(ref.keys());
				r.refs.add(newRef);

			}
		}
	}

	@Override
	public boolean add(Value e) {
		throw new InvalidOperation("Not allowed to add to a table row");
	}

	@Override
	public void add(int index, Value element) {
		throw new InvalidOperation("Not allowed to add to a table row");

	}

	@Override
	public boolean addAll(Collection c) {
		throw new InvalidOperation("Not allowed to add to a table row");

	}

	@Override
	public boolean addAll(int index, Collection c) {
		throw new InvalidOperation("Not allowed to add to a table row");

	}

	@Override
	public void clear() {
		throw new InvalidOperation("Not allowed to clear a table row");

	}

	@Override
	public boolean contains(Object o) {
		return values.contains(o);
	}

	@Override
	public boolean containsAll(Collection c) {
		return values.containsAll(c);
	}

	@Override
	public Value get(int index) {
		return values.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return values.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public Iterator<Value> iterator() {
		return values.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return values.lastIndexOf(o);
	}

	@Override
	public ListIterator<Value> listIterator() {
		return values.listIterator();
	}

	@Override
	public ListIterator<Value> listIterator(int index) {
		return values.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		throw new InvalidOperation("Not allowed to remove from a table row");

	}

	@Override
	public Value remove(int index) {
		throw new InvalidOperation("Not allowed to remove from a table row");
	}

	@Override
	public boolean removeAll(Collection c) {
		throw new InvalidOperation("Not allowed to remove from a table row");

	}

	@Override
	public boolean retainAll(Collection c) {
		throw new InvalidOperation("");
	}

	@Override
	public Value set(int index, Value element) {
		// Ensure its not a key field
		if (fields.get(index).isKey()) {
			throw new InvalidOperation("You cant moderfy a key field");
		}

		// Ensure its the correct type
		if (fields.get(index).type() == Field.Type.INTEGER
				&& !(element instanceof IntegerValue)) {
			throw new InvalidOperation("Incorrect field type");
		} else if (fields.get(index).type() == Field.Type.TEXT
				&& (!(element instanceof StringValue) || element.toString()
						.indexOf("\n") != -1)) {
			throw new InvalidOperation("Incorrect field type");
		} else if (fields.get(index).type() == Field.Type.TEXTAREA
				&& !(element instanceof StringValue)) {
			throw new InvalidOperation("Incorrect field type");
		} else if (fields.get(index).type() == Field.Type.BOOLEAN
				&& !(element instanceof BooleanValue)) {
			throw new InvalidOperation("Incorrect field type");
		} else if (fields.get(index).type() == Field.Type.INTEGER
				&& !(element instanceof IntegerValue)) {
			throw new InvalidOperation("Incorrect field type");
		} else if (fields.get(index).type() == Field.Type.REFERENCE
				&& !(element instanceof ReferenceValue)) {
			throw new InvalidOperation("Incorrect field type");
		}

		// Ensure that if the type is reference then it is a valid one
		if (element instanceof ReferenceValue) {
			ReferenceValue ref = (ReferenceValue) element;
			
			if (ref.keys().length != this.keys.size()) {
				throw new InvalidOperation("Wrong number of keys");
			}
			
			// Ensure the table exsists
			if (db.table(ref.table()) == null) {
				throw new InvalidOperation(
						"Reference to table that dosnt exsist");
			}

			// Ensure that the row referenced exsists
			if (db.table(ref.table()).row(ref.keys()) == null) {
				throw new InvalidOperation("Reference to row that dosnt exsist");
			}
			
			// Construct a reference to this
			String tableName = table.name();
			Value[] vals = new Value[keys.size()];
			
			for (int j = 0; j < vals.length; j++) {
				vals[j] = values.get(j);
			}
			
			// Add the new reference
			ReferenceValue newRef = new ReferenceValue(tableName, vals);
			
			TableRow r = (TableRow) db.table(ref.table()).row(ref.keys());
			r.refs.add(newRef);
			
			// Remove the old reference
			ReferenceValue oldRef = new ReferenceValue(tableName, vals);
			r = (TableRow) db.table(oldRef.table()).row(oldRef.keys());
			r.refs.remove(newRef);
					
		}

		return values.set(index, element);
	}

	@Override
	public int size() {
		return values.size();
	}
	
	public Value[] getKeys() {
		Value[] vals = new Value[keys.size()];
		
		for (int j = 0; j < vals.length; j++) {
			vals[j] = values.get(j);
		}
		
		return vals;
	}

	@Override
	public List<Value> subList(int fromIndex, int toIndex) {
		return values.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return values.toArray();
	}

	@Override
	public Object[] toArray(Object[] a) {
		return values.toArray(a);
	}
}
