package org.manalith.sojipum.db;

import java.sql.SQLException;

import org.manalith.sojipum.Item;
import org.manalith.sojipum.Mode;
import org.manalith.sojipum.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	public static final String DATABASE_NAME = "SOJIPUM_COMMON";

	public static final int DATABASE_VERSION = 1;

	private RuntimeExceptionDao<Item, Integer> itemDao = null;
	private RuntimeExceptionDao<Mode, Integer> modeDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, Mode.class);
			TableUtils.createTable(connectionSource, Item.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

	public RuntimeExceptionDao<Item, Integer> getItemDao() {
		if (itemDao == null) {
			itemDao = getRuntimeExceptionDao(Item.class);
		}
		return itemDao;
	}

	public RuntimeExceptionDao<Mode, Integer> getModeDao() {
		if (modeDao == null) {
			modeDao = getRuntimeExceptionDao(Mode.class);
		}
		return modeDao;
	}
}
