package org.manalith.sojipum;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

public class Item {
	@DatabaseField(generatedId = true)
	public long id;

	@DatabaseField(index = true)
	public String name;

	@DatabaseField(foreign = true)
	public Mode mode;

	@DatabaseField
	public boolean carried;

	@DatabaseField
	public Date created;
}
