package com.vincestyling.circleimageView.core;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;

public class ClipPathImplCircleImageView extends CircleImageView {

	public ClipPathImplCircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void cropBitmap(Canvas nativeCanvas, Bitmap contentBitmap, int contentAreaSize, Paint paint) {
		contentAreaSize -= mMiddleCircleIndent * 2;
		Bitmap cropBitmap = Bitmap.createBitmap(contentAreaSize, contentAreaSize, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(cropBitmap);

		Path path = new Path();
		float radius = contentAreaSize / 2;
		path.addCircle(radius, radius, radius, Path.Direction.CW);
		canvas.clipPath(path);

		paint.setFilterBitmap(true);
		paint.setAntiAlias(true);
		paint.setDither(true);

		canvas.drawBitmap(contentBitmap, 0, 0, paint);

		int pos = mBottomCircleIndent + mMiddleCircleIndent;
		nativeCanvas.drawBitmap(cropBitmap, pos, pos, paint);
	}

}
