package com.example.touch;



import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Game extends Activity {

	Integer nextNum=1;//���ɉ����������L�^����
	long start,clear;//�X�^�[�g�ƃN���A���̃^�C�����L�^�@�N���A���o�ߎ��ԂɕύX
	
	
	private Handler timerHandler = new Handler();
	private Handler deleteHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		
		
		//���\�b�h�`�F�[�� �߂�l�����^�Ń��\�b�h�����ꍇ�����ċL�q�ł���
		//�Q�[���X�^�[�g�̃_�C�A���O���쐬�@�X�^�[�g�������ƃQ�[���J�n
		new AlertDialog.Builder(this)
		.setTitle("�X�^�[�g�������ƊJ�n���܂�")
		.setPositiveButton("�X�^�[�g", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				
				
				
				/*
				 * �����̐������z��̗v�f�����Randam�N���X�@���g�̓����_���������
				 * �g���Â炢�̂ŃA�E�g
				 * Randam ran= new Randam(20);
				 */
					       
				Integer[] box = new Integer[20];
				for(int i=0;i<20;i++){
					box[i] = i+1;
				}
				
				//integer�z����V���b�t������Randam2.Shuffle���\�b�h
				new Randam2().shuffle(box);
				for(int i=0;i<box.length;i++);
				
				
				//�{�^���ɔz��̐������Z�b�g����
				int id;
				TextView tv;
				for(int i=0;i<20;i++){
					id =getResources().getIdentifier("button"+String.valueOf(i+1), "id", getPackageName());
					tv = (TextView)findViewById(id);
					tv.setText(box[i].toString());
				}
				
				//progressbar�ւ̐ݒ�
				ProgressBar progress = (ProgressBar)findViewById(R.id.progressBar1);
				progress.setMax(20000);
				progress.setProgress(0);
				
				start = System.currentTimeMillis();
				timerHandler.postDelayed(CallbackTimer, 1000);// �^�C���X�V�̃��\�b�h�@����I��(1s)�o�ߎ��Ԃ�\��������
			}
		})
		.show();//AlertDialog�̃��\�b�h�`�F�[��(����
		
	}

	@Override
	protected void onStop() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onStop();
	}
	
	public void onButton(View view){
		final Button btn = (Button)view;
		final Intent intent = new Intent(this,MainActivity.class);
		
		Log.d("box",btn.getText().toString());
		if(btn.getText().equals(nextNum.toString())){
			if(nextNum==20){//�N���A�����Ƃ��̏���
				deleteHandler.postDelayed(CallbackDelete, 0);
				clear = System.currentTimeMillis();
				
				//�L�^�������֓o�^�@�����ƃ��R�[�h
				GregorianCalendar cal1 = new GregorianCalendar();
				SimpleDateFormat sdf1 = new SimpleDateFormat("yy/mm/dd hh:mm:ss");
				
				Log.d("test",sdf1.format(cal1.getTime()));
				DBHelper dbh = new DBHelper(this);
				
				SQLiteDatabase db = dbh.getReadableDatabase();
				Cursor c = db.query("recode", new String[] {"date","recode"}, null,null,null,null,null);
				
				
				
				
				//�N���A�_�C�A���O�̕\��
				new AlertDialog.Builder(this).setTitle("���Ȃ��̃N���A�^�C����")
				.setMessage(this.clear_time())
				.setPositiveButton("OK", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
						btn.getContext().startActivity(intent);
						Log.d("box","�N���A���Ă܂�");
					}
				}).show();
			}
			nextNum++;
			TextView text = (TextView)findViewById(R.id.next);
			text.setText(nextNum.toString());
			
			Log.d("box",nextNum.toString());
			
		}
	}
	
	public String time_count(){
				
		//string.fomat�ŏ������w�� %02d���Ɛ��������?�񌅂�0�l�߂ŕ\��
		return String.format("%02d:%02d",(this.clear-this.start)/1000/60,(this.clear-this.start)/1000%60);
		//return String.valueOf("00:"+(this.clear-this.start)/1000);
		
	}
	
	public String clear_time(){
		
		
		return String.format("%02d:%02d.%02d�b",(this.clear-this.start)/1000/60,
				(this.clear-this.start)/1000%60,(this.clear-this.start)%1000);
		
	}
	
    private Runnable CallbackTimer = new Runnable(){
        

		@SuppressWarnings("null")
		public void run() {

            /* ���̒ʒm��ݒ� */
            timerHandler.postDelayed(this, 100);

            clear = System.currentTimeMillis();
            TextView tv = (TextView)findViewById(R.id.timetext);
            tv.setText(time_count());
            ProgressBar progress = (ProgressBar)findViewById(R.id.progressBar1);
			progress.setProgress((int)(clear-start));
            

        }

		
    };    

    private Runnable CallbackDelete = new Runnable() {
        public void run() {
            /* �R�[���o�b�N���폜���Ď����������~ */
            timerHandler.removeCallbacks(CallbackTimer);
        }
    };
}
