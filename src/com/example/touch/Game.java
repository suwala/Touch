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

	Integer nextNum=1;//次に押す数字を記録する
	long start,clear;//スタートとクリア時のタイムを記録　クリアを経過時間に変更
	
	
	private Handler timerHandler = new Handler();
	private Handler deleteHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		
		
		//メソッドチェーン 戻り値が同型でメソッドを持つ場合続けて記述できる
		//ゲームスタートのダイアログを作成　スタートを押すとゲーム開始
		new AlertDialog.Builder(this)
		.setTitle("スタートを押すと開始します")
		.setPositiveButton("スタート", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO 自動生成されたメソッド・スタブ
				
				
				
				/*
				 * 引数の数だけ配列の要素を作るRandamクラス　中身はランダム化される
				 * 使いづらいのでアウト
				 * Randam ran= new Randam(20);
				 */
					       
				Integer[] box = new Integer[20];
				for(int i=0;i<20;i++){
					box[i] = i+1;
				}
				
				//integer配列をシャッフルするRandam2.Shuffleメソッド
				new Randam2().shuffle(box);
				for(int i=0;i<box.length;i++);
				
				
				//ボタンに配列の数字をセットする
				int id;
				TextView tv;
				for(int i=0;i<20;i++){
					id =getResources().getIdentifier("button"+String.valueOf(i+1), "id", getPackageName());
					tv = (TextView)findViewById(id);
					tv.setText(box[i].toString());
				}
				
				//progressbarへの設定
				ProgressBar progress = (ProgressBar)findViewById(R.id.progressBar1);
				progress.setMax(20000);
				progress.setProgress(0);
				
				start = System.currentTimeMillis();
				timerHandler.postDelayed(CallbackTimer, 1000);// タイム更新のメソッド　定期的に(1s)経過時間を表示させる
			}
		})
		.show();//AlertDialogのメソッドチェーン(長い
		
	}

	@Override
	protected void onStop() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStop();
	}
	
	public void onButton(View view){
		final Button btn = (Button)view;
		final Intent intent = new Intent(this,MainActivity.class);
		
		Log.d("box",btn.getText().toString());
		if(btn.getText().equals(nextNum.toString())){
			if(nextNum==20){//クリアしたときの処理
				deleteHandler.postDelayed(CallbackDelete, 0);
				clear = System.currentTimeMillis();
				
				//記録をｄｂへ登録　日時とレコード
				GregorianCalendar cal1 = new GregorianCalendar();
				SimpleDateFormat sdf1 = new SimpleDateFormat("yy/mm/dd hh:mm:ss");
				
				Log.d("test",sdf1.format(cal1.getTime()));
				DBHelper dbh = new DBHelper(this);
				
				SQLiteDatabase db = dbh.getReadableDatabase();
				Cursor c = db.query("recode", new String[] {"date","recode"}, null,null,null,null,null);
				
				
				
				
				//クリアダイアログの表示
				new AlertDialog.Builder(this).setTitle("あなたのクリアタイムは")
				.setMessage(this.clear_time())
				.setPositiveButton("OK", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						
						btn.getContext().startActivity(intent);
						Log.d("box","クリアしてます");
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
				
		//string.fomatで書式を指定 %02dだと数字を常に?二桁で0詰めで表示
		return String.format("%02d:%02d",(this.clear-this.start)/1000/60,(this.clear-this.start)/1000%60);
		//return String.valueOf("00:"+(this.clear-this.start)/1000);
		
	}
	
	public String clear_time(){
		
		
		return String.format("%02d:%02d.%02d秒",(this.clear-this.start)/1000/60,
				(this.clear-this.start)/1000%60,(this.clear-this.start)%1000);
		
	}
	
    private Runnable CallbackTimer = new Runnable(){
        

		@SuppressWarnings("null")
		public void run() {

            /* 次の通知を設定 */
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
            /* コールバックを削除して周期処理を停止 */
            timerHandler.removeCallbacks(CallbackTimer);
        }
    };
}
