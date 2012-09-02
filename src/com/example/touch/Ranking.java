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
		// TODO �����������ꂽ���\�b�h�E�X�^�u
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
		webview.reload();//���ɊJ���Ă��ꍇ�͍X�V����Ȃ��H�ۂ��̂ŁA�J�����y�[�W�������[�h
	}
	
	public void rankUpload(View v){
		
		//�N���C�A���g�ݒ�
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		
		//db���J��
		DBHelper dbh = new DBHelper(this);
		SQLiteDatabase db = dbh.getReadableDatabase();
		Cursor c = db.query("recode", new String[] {"date","recode"}, null,null,null,null,"recode ASC");
		boolean isEof = c.moveToFirst();
				
		if(isEof){
			//POST���M����f�[�^���i�[
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
			nameValuePair.add(new BasicNameValuePair("name", MainActivity.getUserName()));
			nameValuePair.add(new BasicNameValuePair("date", c.getString(0)));
			nameValuePair.add(new BasicNameValuePair("recode", String.valueOf(c.getInt(1))));
			
			//�擾�����x�X�g�X�R�A���ȑO�o�^�������̂Ɠ����������瓮�삵�Ȃ�
			if(c.getInt(1) != recodeTime){
				//Log.d("ranking",recodeTime);
				
		
				try
				{

					//POST���M
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,HTTP.UTF_8));
					HttpResponse response = httpclient.execute(httppost);
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					response.getEntity().writeTo(byteArrayOutputStream);

					//�I����̉������擾
					if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){

						/*�ǂݍ���byteArrayOutputStream��TextView�ɕ\��
						 * Byte�œǂݍ��܂��_�ɒ���(HTML�ł͂Ȃ�)
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
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onDestroy();
		
		this.sharedAssist();
	}

	@Override
	protected void onRestart() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onRestart();
		
		SharedPreferences pref = this.getSharedPreferences("Touch", MODE_PRIVATE);
		this.recodeTime = Integer.valueOf(pref.getString("recode","0"));
		this.webView();
		Log.d("���b�Z�O�W","���A���܂���");
		
	}

	@Override
	protected void onStop() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onStop();
		
		this.sharedAssist();
		
	}
	
	//SharedPreferences���g���x�X�g���R�[�h��ۑ�(�l�g�����ւ̏d��������邽��)
	public void sharedAssist(){
		SharedPreferences pref = this.getSharedPreferences("Touch", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("recode", String.valueOf(recodeTime));
		editor.commit();
		
	}
	
	
}

