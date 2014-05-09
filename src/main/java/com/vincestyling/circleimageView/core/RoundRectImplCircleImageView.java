package com.vincestyling.circleimageView.core;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import com.vincestyling.circleimageView.core.CircleImageView;

public class RoundRectImplCircleImageView extends CircleImageView {

	public RoundRectImplCircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void cropBitmap(Canvas nativeCanvas, Bitmap contentBitmap, int contentAreaSize, Paint paint) {
		contentAreaSize -= mMiddleCircleIndent * 2;
		Bitmap cropBitmap = Bitmap.createBitmap(contentAreaSize, contentAreaSize, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(cropBitmap);

		float radius = contentAreaSize / 2;
		paint.setShader(new BitmapShader(contentBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
		canvas.drawRoundRect(new RectF(0, 0, contentAreaSize, contentAreaSize), radius, radius, paint);

		int pos = mBottomCircleIndent + mMiddleCircleIndent;
		nativeCanvas.drawBitmap(cropBitmap, pos, pos, paint);
	}

}
