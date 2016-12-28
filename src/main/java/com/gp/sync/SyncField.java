package com.gp.sync;

public class SyncField {
	
	public static enum FieldType{		
		STRING,
		INT,
		LONG,
		DOUBLE,
		FLOAT,
		DATE,
		BOOLEAN
	}
	
	String name;
	
	String dbfield;
	
	FieldType type;

	public SyncField(){}
	
	public SyncField(String name, String dbfield, FieldType type){
		this.name = name;
		this.dbfield = dbfield;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDbfield() {
		return dbfield;
	}

	public void setDbfield(String dbfield) {
		this.dbfield = dbfield;
	}

	public FieldType getType() {
		return type;
	}

	public void setType(FieldType type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "SyncField [name=" + name + ", dbfield=" + dbfield + ", type=" + type + "]";
	}	
	
	@Override
	public int hashCode(){
		
		return this.name.hashCode();
	}
}
