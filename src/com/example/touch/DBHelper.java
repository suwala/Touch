package com.example.touch;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{
	
	static final Integer version = 1;
	static final CursorFactory factory = null;
	static final String name = "recode.db";
	
	public DBHelper(Context context) {
		super(context, name, factory, version);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("create table recode("+
				" date string not null,"+
				" recode integer not null"+
				");"
			);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	
	public void recode_Set(SQLiteDatabase db,Date data,long recode){
		ContentValues val = new ContentValues();
		
		val.put("date", data.toString());
		val.put("recode", recode);
		db.insert("recode", null, val);
		Log.d("tttttttttttt",String.valueOf(data));
		//db.execSQL("insert into recode(data,recode) values (+100,200+);");
	}

}
