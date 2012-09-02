package com.example.touch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;

public class Recode extends Activity {
	DBHelper dbh;
	public Cursor c;
	SQLiteDatabase db;
	Integer index=0,flag=1;//flag日付/タイムの切り替えを示す
	boolean isEof;
	
	public final int LINES = 15;//記録を表示する行数
	

	private ScaleGestureDetector _gestureDetector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recode);
		
		if(MainActivity.getUserName()==null){
			TextView tv = (TextView)findViewById(R.id.name1);
			tv.setText("GUEST");
			tv = (TextView)findViewById(R.id.recode1);
			tv.setText("ここでは名前を登録すると\n記録が残せるようになります");
			
		}else{dbh = new DBHelper(this);
		db = dbh.getReadableDatabase();
		c = db.query("recode", new String[] {"date","recode"}, null,null,null,null,null);
		this.isEof = c.moveToFirst();
		
		TextView tv = (TextView)findViewById(R.id.name1);
		tv.setText(MainActivity.getUserName()+"の記録");
		
		
		
		int i = 0,id;
		if(isEof)
			Log.d("ra","aaaaaaa");
		
		if(isEof)			
			this.setTextV();
		/*while(isEof){
			if(i ==LINES){
				break;
			}
			
			id = getResources().getIdentifier("recode"+String.valueOf((i+1)), "id", getPackageName());
			
			tv = (TextView)findViewById(id);

			tv.setText(c.getString(0)+" | "+String.format("%02d:%03d",c.getInt(1)/1000%60,c.getInt(1)%1000));
			isEof = c.moveToNext();
			i++;
		}*/
		
		c.close();
		db.close();
		}
	}

	@Override
	protected void onStop() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStop();
		
		
		
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recode, menu);
               
        return true;
    }
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO 自動生成されたメソッド・スタブ
		
		//記録の消去　ダイアログで'はい'が押されたらdbへアクセスし記録を消去する
		if(item.getItemId() == R.id.reco_menu){
			new AlertDialog.Builder(this).setTitle("記録を消去しますか？")
			.setPositiveButton("はい", new DialogInterface.OnClickListener(){
				@Override
        		public void onClick(DialogInterface dialog, int whichButton){
					DBHelper dbh = new DBHelper(Recode.this);
					SQLiteDatabase db = dbh.getReadableDatabase();
					dbh.dbClear(db);
					db.close();
					new Recode().setTextV();
					/*for(int i=0,id;i<LINES;i++){
						id = getResources().getIdentifier("recode"+String.valueOf(i+1), "id", getPackageName());
						TextView tv = (TextView)findViewById(id);
						tv.setText(null);
					}*/
					
				}
			})
			.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO 自動生成されたメソッド・スタブ
					
				}
			})
			.show();
		}
		return super.onOptionsItemSelected(item);
	}

	public void next(View view){
		
		
		if(MainActivity.getUserName()==null){
		
		}else{
		index++;
		
		db = dbh.getReadableDatabase();
		if(flag%2 ==0)
			c = db.query("recode", new String[] {"date","recode"}, null,null,null,null,"recode ASC");
		else
			c = db.query("recode", new String[] {"date","recode"}, null,null,null,null,null);
		isEof = c.move(index*LINES+1);
		if(isEof){		
			this.setTextV();
		}else{
			index--;
		}
		c.close();
		db.close();
		}
	}
	
	public void back(View view){
		
		if(MainActivity.getUserName()==null){
			
		}else{
		index--;
		
		db = dbh.getReadableDatabase();
		if(flag%2 ==0)
			c = db.query("recode", new String[] {"date","recode"}, null,null,null,null,"recode ASC");
		else
			c = db.query("recode", new String[] {"date","recode"}, null,null,null,null,null);
		isEof = c.move(index*LINES+1);
		if(isEof){
			
			
			this.setTextV();
		
			/*setTextV()に統合
			int i=0,id;
			TextView tv ;
			while(i <LINES){
				id = getResources().getIdentifier("recode"+String.valueOf((i+1)), "id", getPackageName());
			
				tv = (TextView)findViewById(id);

				if(isEof){
					tv.setText(c.getString(0)+" | "+String.format("%02d:%03d",c.getInt(1)/1000%60,c.getInt(1)%1000));
					isEof = c.moveToNext();
				}else{
					tv.setText("");
				}
				i++;
			}*/

		}else{
			index++;
		}
		c.close();
		db.close();
		}
	}
	
	//ソートメソッド　日付/タイムで並び替える
	public void sort(View v){
		flag++;
		db = dbh.getReadableDatabase();
		if(flag%2 ==0)
			c = db.query("recode", new String[] {"date","recode"}, null,null,null,null,"recode ASC");
		else
			c = db.query("recode", new String[] {"date","recode"}, null,null,null,null,null);
		isEof = c.moveToFirst();	
		this.setTextV();
		index = 0;
		
	}
	
	public void setTextV(){
		TextView tv;
		int i=0,id;
		while(i < LINES){
			id = getResources().getIdentifier("recode"+String.valueOf(i+1), "id", getPackageName());
			tv = (TextView)findViewById(id);
			
			if(isEof){
				Log.d("ranking",c.getInt(1)+"秒"+c.getInt(1)/1000%60);
				tv.setText(c.getString(0)+" | "+String.format("%01d.%03dSec",c.getInt(1)/1000,c.getInt(1)%1000));
				isEof = c.moveToNext();
			}else{
				Log.d("ra","aaaaaaa");
				tv.setText(null);
			}
			i++;
		}
	}
}
