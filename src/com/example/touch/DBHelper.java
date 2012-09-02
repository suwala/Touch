package com.example.touch;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBHelper extends SQLiteOpenHelper{
	
	static final Integer version = 1;
	static final CursorFactory factory = null;
	static final String name = "recode.db";
	
	public DBHelper(Context context) {
		super(context, name, factory, version);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("create table if not exists recode("+
				"_id integer primary key autoincrement,"+
				" date String not null,"+
				" recode integer not null"+
				");"
			);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
	}
	
	public void recode_Set(SQLiteDatabase db,String date,long recode){
		ContentValues val = new ContentValues();
		
		val.put("date", date);
		val.put("recode", recode);
		db.insert("recode", null, val);
	}
	
	public void dbClear(SQLiteDatabase db){
		db.execSQL("DELETE FROM recode");
	}

}
