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
	Integer index=0,flag=1;//flag���t/�^�C���̐؂�ւ�������
	boolean isEof;
	
	public final int LINES = 15;//�L�^��\������s��
	

	private ScaleGestureDetector _gestureDetector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recode);
		
		if(MainActivity.getUserName()==null){
			TextView tv = (TextView)findViewById(R.id.name1);
			tv.setText("GUEST");
			tv = (TextView)findViewById(R.id.recode1);
			tv.setText("�����ł͖��O��o�^�����\n�L�^���c����悤�ɂȂ�܂�");
			
		}else{dbh = new DBHelper(this);
		db = dbh.getReadableDatabase();
		c = db.query("recode", new String[] {"date","recode"}, null,null,null,null,null);
		this.isEof = c.moveToFirst();
		
		TextView tv = (TextView)findViewById(R.id.name1);
		tv.setText(MainActivity.getUserName()+"�̋L�^");
		
		
		
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
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onStop();
		
		
		
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recode, menu);
               
        return true;
    }
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
		//�L�^�̏����@�_�C�A���O��'�͂�'�������ꂽ��db�փA�N�Z�X���L�^����������
		if(item.getItemId() == R.id.reco_menu){
			new AlertDialog.Builder(this).setTitle("�L�^���������܂����H")
			.setPositiveButton("�͂�", new DialogInterface.OnClickListener(){
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
			.setNegativeButton("������", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO �����������ꂽ���\�b�h�E�X�^�u
					
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
		
			/*setTextV()�ɓ���
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
	
	//�\�[�g���\�b�h�@���t/�^�C���ŕ��ёւ���
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
				Log.d("ranking",c.getInt(1)+"�b"+c.getInt(1)/1000%60);
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
