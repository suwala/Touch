package com.example.touch;

import java.text.AttributedCharacterIterator.Attribute;
import java.util.jar.Attributes;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ProgressBarLine extends View{

	Canvas canvas;
	
	public ProgressBarLine(Context context,AttributeSet attrs) {
		super(context,attrs);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
		setFocusable(true);
	}
	
	public ProgressBarLine(Context context) {
		super(context);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
		setFocusable(true);
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		
		super.onDraw(canvas);
		
		int x = 50;//pb.getWidth();
		int y = 50;
		
				

		Bitmap[] bitmap = new Bitmap[4];
		
	    Paint paint = new Paint();
	    bitmap[0] =BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
	    bitmap[1] = Bitmap.createScaledBitmap(bitmap[0], bitmap[0].getWidth(), y, true);
	    
	    canvas.drawBitmap(bitmap[1], 0,0, paint);
	    
	    //AVD��MIN�̍��W������o���Ί���
	    
		
		
	}
	
	public void draw(){
//		Bitmap bitmap =Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888);
//		Canvas canvas = new Canvas(bitmap);
//		this.onDraw(canvas);
//		������������-------------------------------
//		Bitmap bitmap;
//		
//		bitmap =BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
//		ImageView view = new ImageView(Game.this);
//		view.setImageBitmap(bitmap);	
	}
	

}
