package com.vincestyling.circleimageView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
//	String photo = "http://tp1.sinaimg.cn/1768720064/180/40043799298/1";
	String photo = "https://avatars3.githubusercontent.com/u/3348207?s=460";
//	String photo = "http://ww1.sinaimg.cn/crop.1.87.442.442.1024/696c86c0gw1ecb74t8l2xj20cd0hegna.jpg";
//	String photo = "http://ww1.sinaimg_diderror.cn/crop.1.87.442.442.1024/696c86c0gw1ecb74t8l2xj20cd0hegna.jpg";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ImageView imvMemberHead = (ImageView) findViewById(R.id.imvMemberHead);
		Netroid.displayImage(photo, imvMemberHead,
				R.drawable.aboutme_log_default_head, R.drawable.aboutme_unlog_default_head_sml);

		imvMemberHead.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showToastMsg("imvMemberHead click!");
			}
		});

		findViewById(R.id.imvWithoutBorder).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showToastMsg("imvWithoutBorder click!");
			}
		});
	}

	Toast mToast;
	private void showToastMsg(String msg) {
		if (mToast != null) mToast.cancel();
		mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		mToast.show();
	}

}
