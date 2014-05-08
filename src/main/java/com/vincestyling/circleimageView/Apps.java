package com.vincestyling.circleimageView;

import android.app.Application;

public class Apps extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Netroid.init(this);
	}

}
