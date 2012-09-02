package com.example.touch;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends Activity {


	private static String userName=null;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
		if(item.getItemId() == R.id.menu_settings){
			new AlertDialog.Builder(this).setTitle("�S�Ẵf�[�^���N���A���܂����H")
			.setPositiveButton("�͂�", new DialogInterface.OnClickListener(){
				@Override
        		public void onClick(DialogInterface dialog, int whichButton){
					DBHelper dbh = new DBHelper(MainActivity.this);
					SQLiteDatabase db = dbh.getReadableDatabase();
					dbh.dbClear(db);
					db.close();
					MainActivity.userName = null;
					
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        SharedPreferences pref = this.getSharedPreferences("Touch", MODE_PRIVATE);
        MainActivity.userName = pref.getString("userName",null);
 
        
        Log.d("db",getUserName()+":");
        
        if(userName == null){
        	new AlertDialog.Builder(this).setTitle(getString(R.string.app_name)+"�̐��E�ւ悤����")
        	.setMessage("�����ł͂��Ȃ��̖��O��o�^���܂�\n�o�^���Ȃ��ꍇ�X�R�A�̋L�^�Ȃǂ��o���Ȃ��Ȃ�܂�(��Őݒ肷�邱�Ƃ͉\�ł�)")
        	.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
        		@Override
        		public void onClick(DialogInterface dialog, int whichButton){
        			
        			
        			final EditText edtInput = new EditText(MainActivity.this);
        			//inputFilter[]=edittext�ɑ΂�����͕������̐����Ȃǂ�ݒ�ł���
        			InputFilter[] _inputFilter = new InputFilter[1];
        			_inputFilter[0] = new InputFilter.LengthFilter(6);
        			edtInput.setFilters(_inputFilter);
        			
        			//���s�s�̐ݒ�
        			edtInput.setInputType(InputType.TYPE_CLASS_TEXT);
        			
        			new AlertDialog.Builder(MainActivity.this).setTitle("���O����͂��Ă�������")
        			.setView(edtInput)
        			.setPositiveButton("OK",new DialogInterface.OnClickListener() {
        										
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO �����������ꂽ���\�b�h�E�X�^�u
							
							CharSequence str = edtInput.getText();
							userName = str.toString();
							TextView tv = (TextView)findViewById(R.id.textView2);
							tv.setText(userName);
							Log.d("db",getUserName());
							
							
						}
					})
        			.show();
        		}
        	})
        	.setNegativeButton("No",new DialogInterface.OnClickListener(){
        		public void onClick(DialogInterface dialog, int whichButton){
        			
        		}
        	})
        	.show();
        }else{
        	TextView tv = (TextView)findViewById(R.id.textView2);
			tv.setText(getUserName());
        }
        
        if(userName == null){
        	TextView tv = (TextView)findViewById(R.id.textView2);
			tv.setText("GUEST");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void game(View v){
    	Intent i = new Intent(this,com.example.touch.Game.class);
    	this.startActivity(i);
    }
    
    public void recode(View v){
    	Intent i = new Intent(this,com.example.touch.Recode.class);
    	this.startActivity(i);
    }
    
    public static String getUserName(){
		return userName;
    }
    
    public void ranking(View v){
    	Intent i = new Intent(this,com.example.touch.Ranking.class);
    	this.startActivity(i);
    }
    

	@Override
	protected void onStop() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onStop();
		if(getUserName() != null){
			SharedPreferences pref = getSharedPreferences("Touch", MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putString("userName", getUserName());
			editor.commit();
		}
	}
    
    
}
