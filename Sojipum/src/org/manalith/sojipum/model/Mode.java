package org.manalith.sojipum.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

public class Mode {
	@DatabaseField(generatedId = true)
	public long id;

	@DatabaseField(index = true)
	public String name;
	
	@DatabaseField
	public Date created;

	@Override
	public String toString() {
		return name;
	}
}
