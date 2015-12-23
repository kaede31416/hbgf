package com.example.hbhri;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.widget.ImageView;

public class SplashActivity extends ActionBarActivity {



	private ImageView iv_loading;
	private SharedPreferences sp ;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			boolean firstTime = sp.getBoolean("firstTime", true);
			if(firstTime) {
				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}else{
				Intent intent = new Intent(SplashActivity.this,RulesActivity.class);
				startActivity(intent);
				SharedPreferences.Editor editor = sp.edit();
				editor.putBoolean("firstTime",false);
				finish();
			}
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		sp= getSharedPreferences("config", Context.MODE_PRIVATE);
		iv_loading = (ImageView) findViewById(R.id.iv_loading);
		
		AnimationDrawable anim = (AnimationDrawable) iv_loading.getBackground();
		anim.start();
		
		new Thread() {
			@Override
			public void run() {
				handler.sendMessageDelayed(Message.obtain(), 4000);
			}
		}.start();
		

	}

}
