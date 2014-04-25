package com.vincestyling.circleimageView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity implements View.OnClickListener {
	ImageView imvCircle1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		imvCircle1 = (ImageView) findViewById(R.id.imvCircle1);

		findViewById(R.id.btnRefresh1).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.btnRefresh1) {
			imvCircle1.setImageResource(R.drawable.aboutme_unlog_default_head);
		}
	}
}
