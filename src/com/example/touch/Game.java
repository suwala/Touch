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

	Integer nextNum=1;//���ɉ����ׂ������@�f�o�b�O����20
	long start,clear;//�X�^�[�g�ƃN���A���̃^�C�����L�^�@�N���A���o�ߎ��ԂɕύX
	
	
	private Handler timerHandler = new Handler();
	private Handler deleteHandler = new Handler();
	
	DBHelper dbh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		
		
		
		//���\�b�h�`�F�[�� �߂�l�����^�Ń��\�b�h�����ꍇ�����ċL�q�ł���
		//�Q�[���X�^�[�g�̃_�C�A���O���쐬�@�X�^�[�g�������ƃQ�[���J�n
		new AlertDialog.Builder(this)
		//.setCancelable(false)//�o�b�N�L�[�̖�����
		.setTitle("�X�^�[�g�������ƊJ�n���܂�")
		.setPositiveButton("�X�^�[�g", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				
				
				
				/*
				 * �����̐������z��̗v�f�����Randam�N���X�@���g�̓����_���������
				 * �g���Â炢�̂ŃR�����g�A�E�g
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
				
				//DB���J��
				dbh = new DBHelper(Game.this);
				SQLiteDatabase db = dbh.getReadableDatabase();
				Cursor c = db.query("recode", new String[] {"date","recode"}, null,null,null,null,null);
				boolean isEof = c.moveToFirst();//�ǂݎ��Ƃ��ɕK�v
				
				//progressbar�ւ̐ݒ�
				ProgressBar progress = (ProgressBar)findViewById(R.id.progressBar1);
//				barX = progress.getWidth();
//				barY = progress.getHeight();
								
				if(isEof){
					Integer add=0;
					while(isEof){
						add += c.getInt(1);
						isEof = c.moveToNext();
					}
				
					//add�ɃA�x���[�W�X�R�A���L�^ �o�[��MAX���A�x+3�b�ɐݒ�
					add = add/c.getCount();
					
					//�Ō��"recode" + " ASC"�͂�recode�����ASC=�����ŕ��ёւ���
					c=db.query("recode", new String[] {"date","recode"}, null, null, null, null, "recode ASC");
					
					c.moveToFirst();
					Log.d("db",String.valueOf(progress.getHeight()));
					progress.setMax(add+3000);
					progress.setSecondaryProgress(c.getInt(1));
					
				
				}else{//db�ɐڑ��ł��Ȃ��Ƃ�
					progress.setMax(60000);
				}
				progress.setProgress(0);
				
				
				
				
				start = System.currentTimeMillis();
				timerHandler.postDelayed(CallbackTimer, 100);// �^�C���X�V�̃��\�b�h�@����I��(1s)�o�ߎ��Ԃ�\��������
			
				//c���ɕ��邱��
				c.close();
				db.close();
			}
		})
		.setOnCancelListener(new DialogInterface.OnCancelListener() {//�o�b�N�L�[���������Ƃ��̓���
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				Intent i = new Intent(Game.this,com.example.touch.MainActivity.class);
		    	startActivity(i);//this��AlertDialog���w������H
				
			}
		})
		.show();//AlertDialog�̃��\�b�h�`�F�[��(����
        
		
		/*�v���O���X�o�[�̏�ɕ`�悵�����@�ۗ�
		Bitmap bitmap,bitmap2;
		
		bitmap2 = Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888);
		bitmap =BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
		Paint paint = new Paint();
		Canvas canvas  = new Canvas(bitmap2);
		
		canvas.drawBitmap(bitmap,0,0, paint);
		
		
		ImageView view = new ImageView(this);
		view.setImageResource(R.id.view1);
		
		view.setImageBitmap(bitmap2);
		
		FrameLayout frameLayout =(FrameLayout)findViewById(R.id.Frame);
		frameLayout.addView(view);
		*/
		
		
	}

	@Override
	protected void onStop() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onStop();
	}
	
	public void onButton(View view){
		final Button btn = (Button)view;
		final Intent intent = new Intent(this,MainActivity.class);
		
		if(btn.getText().equals(nextNum.toString())){
			if(nextNum==20){//�N���A�����Ƃ��̏���
				deleteHandler.postDelayed(CallbackDelete, 0);
				clear = System.currentTimeMillis();
				
				
				//�L�^�������֓o�^�@�����ƃ��R�[�h
				GregorianCalendar cal1 = new GregorianCalendar();
				//SimpleDateFormat�ŏ����̎w�� yy/MM/dd HH:mm:ss���ƔN/��/���@0-23��/��/�b�ŏo��(���ׂē�)
				SimpleDateFormat sdf1 = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
				
				dbh = new DBHelper(this);
				
				final SQLiteDatabase db = dbh.getReadableDatabase();
				
				/*�N���A����̂�2�����z�����狭���I��2����  �K�v�Ȃ�����
				long clearTime;
				if(this.clear-this.start > 60*2*1000){
					clearTime = 60*2*1000;
				}else{
					clearTime = this.clear-this.start;
				}
				*/
				
				if(MainActivity.getUserName()!=null)
					dbh.recode_Set(db, sdf1.format(cal1.getTime()), this.clear-this.start);
						
				//�N���A�_�C�A���O�̕\��
				new AlertDialog.Builder(this).setTitle("���Ȃ��̃N���A�^�C����")
				.setMessage(this.clear_time())
				.setPositiveButton("OK", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						db.close();
						btn.getContext().startActivity(intent);
					}
				}).show();
			}else{
				nextNum++;
				TextView text = (TextView)findViewById(R.id.next);
				text.setText(nextNum.toString());
			}
			
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
	
	public String recodeToString(Integer i){
		return String.format("%02d:%02d.%02d�b",i/1000/60,i/1000%60,i%1000);
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
