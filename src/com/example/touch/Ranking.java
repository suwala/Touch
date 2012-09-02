package com.example.touch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class Ranking extends Activity {

	public static String url = "http://192.168.11.16/touch2.php";
	public int recodeTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ranking);
		
		SharedPreferences pref = this.getSharedPreferences("Touch", MODE_PRIVATE);
//		SharedPreferences.Editor editor = pref.edit();
//		editor.clear().commit();
		this.recodeTime = Integer.valueOf(pref.getString("recode","0"));
        
        Log.d("ranking",String.valueOf(recodeTime));
		
		this.webView();
		
		
	}
	
	public void webView(){
		TextView tv = (TextView)findViewById(R.id.ran_text);
		tv.setText(MainActivity.getUserName());
		
		WebView webview = (WebView)findViewById(R.id.webview);
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(false);
		webSettings.setSaveFormData(false);
		webSettings.setSavePassword(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        
        webview.setWebViewClient(new WebViewClient(){
        	@Override
        	public boolean shouldOverrideUrlLoading(WebView view, String url) {
        		view.loadUrl(url);
                return true;
        	}
        });
        
        webview.loadUrl("http://192.168.11.16/touch.php");
		webview.reload();//既に開いてた場合は更新されない？ぽいので、開いたページをリロード
	}
	
	public void rankUpload(View v){
		
		//クライアント設定
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		
		//dbを開く
		DBHelper dbh = new DBHelper(this);
		SQLiteDatabase db = dbh.getReadableDatabase();
		Cursor c = db.query("recode", new String[] {"date","recode"}, null,null,null,null,"recode ASC");
		boolean isEof = c.moveToFirst();
				
		if(isEof){
			//POST送信するデータを格納
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
			nameValuePair.add(new BasicNameValuePair("name", MainActivity.getUserName()));
			nameValuePair.add(new BasicNameValuePair("date", c.getString(0)));
			nameValuePair.add(new BasicNameValuePair("recode", String.valueOf(c.getInt(1))));
			
			//取得したベストスコアが以前登録したものと同じだったら動作しない
			if(c.getInt(1) != recodeTime){
				//Log.d("ranking",recodeTime);
				
		
				try
				{

					//POST送信
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,HTTP.UTF_8));
					HttpResponse response = httpclient.execute(httppost);
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					response.getEntity().writeTo(byteArrayOutputStream);

					//鯖からの応答を取得
					if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){

						/*読み込んだbyteArrayOutputStreamをTextViewに表示
						 * Byteで読み込まれる点に注意(HTMLではない)
					TextView tv = (TextView)findViewById(R.id.ran_text);
					tv.setText(byteArrayOutputStream.toString());
						 */

						recodeTime = c.getInt(1);
						this.webView();

					}else{
						Toast.makeText(this,"error:"+response.getStatusLine(),Toast.LENGTH_LONG).show();
					}
				}catch(UnsupportedEncodingException e){
					e.printStackTrace();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		c.close();
		db.close();
	}

	@Override
	protected void onDestroy() {
		// TODO 自動生成されたメソッド・スタブ
		super.onDestroy();
		
		this.sharedAssist();
	}

	@Override
	protected void onRestart() {
		// TODO 自動生成されたメソッド・スタブ
		super.onRestart();
		
		SharedPreferences pref = this.getSharedPreferences("Touch", MODE_PRIVATE);
		this.recodeTime = Integer.valueOf(pref.getString("recode","0"));
		this.webView();
		Log.d("メッセ０ジ","復帰しました");
		
	}

	@Override
	protected void onStop() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStop();
		
		this.sharedAssist();
		
	}
	
	//SharedPreferencesを使いベストレコードを保存(ネトランへの重複を避けるため)
	public void sharedAssist(){
		SharedPreferences pref = this.getSharedPreferences("Touch", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("recode", String.valueOf(recodeTime));
		editor.commit();
		
	}
	
	
}

