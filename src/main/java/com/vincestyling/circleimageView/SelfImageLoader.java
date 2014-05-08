package com.vincestyling.circleimageView;

import com.duowan.mobile.netroid.RequestQueue;
import com.duowan.mobile.netroid.request.ImageRequest;
import com.duowan.mobile.netroid.toolbox.ImageLoader;

import java.util.concurrent.TimeUnit;

public class SelfImageLoader extends ImageLoader {

	public SelfImageLoader(RequestQueue queue, ImageCache cache) {
		super(queue, cache);
	}

	@Override
	public ImageRequest buildRequest(String requestUrl, int maxWidth, int maxHeight) {
		ImageRequest request = new ImageRequest(requestUrl, maxWidth, maxHeight);
		request.setCacheExpireTime(TimeUnit.MINUTES, 10);
//		request.setForceUpdate(true);
		return request;
	}

}
