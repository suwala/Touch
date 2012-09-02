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

	Integer nextNum=1;//次に押すべき数字　デバッグ中は20
	long start,clear;//スタートとクリア時のタイムを記録　クリアを経過時間に変更
	
	
	private Handler timerHandler = new Handler();
	private Handler deleteHandler = new Handler();
	
	DBHelper dbh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		
		
		
		//メソッドチェーン 戻り値が同型でメソッドを持つ場合続けて記述できる
		//ゲームスタートのダイアログを作成　スタートを押すとゲーム開始
		new AlertDialog.Builder(this)
		//.setCancelable(false)//バックキーの無効化
		.setTitle("スタートを押すと開始します")
		.setPositiveButton("スタート", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO 自動生成されたメソッド・スタブ
				
				
				
				/*
				 * 引数の数だけ配列の要素を作るRandamクラス　中身はランダム化される
				 * 使いづらいのでコメントアウト
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
				
				//DBを開く
				dbh = new DBHelper(Game.this);
				SQLiteDatabase db = dbh.getReadableDatabase();
				Cursor c = db.query("recode", new String[] {"date","recode"}, null,null,null,null,null);
				boolean isEof = c.moveToFirst();//読み取るときに必要
				
				//progressbarへの設定
				ProgressBar progress = (ProgressBar)findViewById(R.id.progressBar1);
//				barX = progress.getWidth();
//				barY = progress.getHeight();
								
				if(isEof){
					Integer add=0;
					while(isEof){
						add += c.getInt(1);
						isEof = c.moveToNext();
					}
				
					//addにアベレージスコアを記録 バーのMAXをアベ+3秒に設定
					add = add/c.getCount();
					
					//最後の"recode" + " ASC"はれrecodeを基準にASC=昇順で並び替える
					c=db.query("recode", new String[] {"date","recode"}, null, null, null, null, "recode ASC");
					
					c.moveToFirst();
					Log.d("db",String.valueOf(progress.getHeight()));
					progress.setMax(add+3000);
					progress.setSecondaryProgress(c.getInt(1));
					
				
				}else{//dbに接続できないとき
					progress.setMax(60000);
				}
				progress.setProgress(0);
				
				
				
				
				start = System.currentTimeMillis();
				timerHandler.postDelayed(CallbackTimer, 100);// タイム更新のメソッド　定期的に(1s)経過時間を表示させる
			
				//cを先に閉じること
				c.close();
				db.close();
			}
		})
		.setOnCancelListener(new DialogInterface.OnCancelListener() {//バックキーを押したときの動作
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO 自動生成されたメソッド・スタブ
				Intent i = new Intent(Game.this,com.example.touch.MainActivity.class);
		    	startActivity(i);//thisがAlertDialogを指すから？
				
			}
		})
		.show();//AlertDialogのメソッドチェーン(長い
        
		
		/*プログレスバーの上に描画したい　保留
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
		// TODO 自動生成されたメソッド・スタブ
		super.onStop();
	}
	
	public void onButton(View view){
		final Button btn = (Button)view;
		final Intent intent = new Intent(this,MainActivity.class);
		
		if(btn.getText().equals(nextNum.toString())){
			if(nextNum==20){//クリアしたときの処理
				deleteHandler.postDelayed(CallbackDelete, 0);
				clear = System.currentTimeMillis();
				
				
				//記録をｄｂへ登録　日時とレコード
				GregorianCalendar cal1 = new GregorianCalendar();
				//SimpleDateFormatで書式の指定 yy/MM/dd HH:mm:ssだと年/月/日　0-23時/分/秒で出力(すべて二桁)
				SimpleDateFormat sdf1 = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
				
				dbh = new DBHelper(this);
				
				final SQLiteDatabase db = dbh.getReadableDatabase();
				
				/*クリアするのに2分を越えたら強制的に2分へ  必要ないかも
				long clearTime;
				if(this.clear-this.start > 60*2*1000){
					clearTime = 60*2*1000;
				}else{
					clearTime = this.clear-this.start;
				}
				*/
				
				if(MainActivity.getUserName()!=null)
					dbh.recode_Set(db, sdf1.format(cal1.getTime()), this.clear-this.start);
						
				//クリアダイアログの表示
				new AlertDialog.Builder(this).setTitle("あなたのクリアタイムは")
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
				
		//string.fomatで書式を指定 %02dだと数字を常に?二桁で0詰めで表示
		return String.format("%02d:%02d",(this.clear-this.start)/1000/60,(this.clear-this.start)/1000%60);
		//return String.valueOf("00:"+(this.clear-this.start)/1000);
		
	}
	
	public String clear_time(){
		
		
		return String.format("%02d:%02d.%02d秒",(this.clear-this.start)/1000/60,
				(this.clear-this.start)/1000%60,(this.clear-this.start)%1000);
		
	}
	
	public String recodeToString(Integer i){
		return String.format("%02d:%02d.%02d秒",i/1000/60,i/1000%60,i%1000);
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
