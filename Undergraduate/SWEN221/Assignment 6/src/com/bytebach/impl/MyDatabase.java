package com.bytebach.impl;

import java.util.*;

import com.bytebach.model.*;

public class MyDatabase implements Database {

	private Collection<Table> tables;
	
	public MyDatabase() {
		tables = new ArrayList<>();
	}
	
	@Override
	public Collection<Table> tables() {
		return tables;
	}

	@Override
	public Table table(String name) {
		for (Table t : tables) {
			if (t.name().equals(name)) {
				return t;
			}
		}
		
		return null;
	}

	@Override
	public void createTable(String name, List<Field> fields) {
		
		if (table(name) == null) {
			tables.add(new TableImpl(name, fields, this)); 
		} else {
			throw new InvalidOperation("Table with that name exsists");
		}
	}

	// Make sure to delete reference tables
	@Override
	public void deleteTable(String name) {
		
		Table t = table(name);
		
		// For each row delete it from the table to ensure the reference
		// tables are deleted aswell
		while (t.rows().size() != 0) {
			TableRow r = (TableRow) t.rows().get(0);
			
			t.delete(r.getKeys());
		}
		
		tables.remove(t);
		
	}
	// This is where you'll probably want to start. You'll need to provide an
	// implementation of Table as well.
	//
	// One of the key challenges in this assignment is to provide you're
	// own implementations of the List interface which can intercept the various
	// operations (e.g. add, set, remove, etc) and check whether they violate
	// the constraints and/or update the database appropriately (e.g. for the
	// cascading delete).
	//
	// HINT: to get started, don't bother providing your own implementations of
	// List as discussed above! Instead, implement MyDatabase and MyTabe using
	// conventional Collections. When you have that working, and the web system
	// is doing something sensible, then consider how you're going to get those
	// unit test to past. 
	
}
