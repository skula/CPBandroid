package com.skula.cpb.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.skula.cpb.Cnst;

public class DatabaseService {
	private static final String DATABASE_NAME = "cpasbien.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME_PARAM = "param";

	private Context context;
	private SQLiteDatabase database;
	private SQLiteStatement statement;

	public DatabaseService(Context context) {
		this.context = context;
		OpenHelper openHelper = new OpenHelper(this.context);
		this.database = openHelper.getWritableDatabase();
	}

	public void bouchon() {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PARAM);
		database.execSQL("CREATE TABLE " + TABLE_NAME_PARAM  + "(name INTEGER PRIMARY KEY, val TEXT)");
		
		insertParam(Cnst.PARAM_IP_TRANSMISSION, "192.168.1.52");
		insertParam(Cnst.PARAM_PORT_TRANSMISSION, "9091");
		insertParam(Cnst.PARAM_LOGIN_TRANSMISSION, "slown");
		insertParam(Cnst.PARAM_PW_TRANSMISSION, "salope");
		
		insertParam(Cnst.PARAM_LOGIN_BETASERIES, "skula");
		insertParam(Cnst.PARAM_PW_BETASERIES,"");
		insertParam(Cnst.PARAM_KEY_BETASERIES,"");
	}

	public void insertParam(String name, String value) {
		String sql = "insert into " + TABLE_NAME_PARAM + "(name, val) values (?,?)";
		statement = database.compileStatement(sql);
		statement.bindString(1, name);
		statement.bindString(2, value);
		statement.executeInsert();
	}

	public void updateParam(String name, String value) {
		ContentValues args = new ContentValues();
		args.put("val", value);
		database.update(TABLE_NAME_PARAM, args, "name='" + name +"'" , null);
	}

	public void deleteParam(String name) {
		database.delete(TABLE_NAME_PARAM, "name='" + name + "'", null);
	}

	public String getParam(String name) {
		Cursor cursor = database.query(TABLE_NAME_PARAM, new String[] { "val" }, "name=" + name, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				return cursor.getString(0);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return null;
	}

	private static class OpenHelper extends SQLiteOpenHelper {
		OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase database) {
			database.execSQL("CREATE TABLE " + TABLE_NAME_PARAM  + "(name INTEGER PRIMARY KEY, val TEXT)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
			database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PARAM);	
			onCreate(database);
		}
	}
}