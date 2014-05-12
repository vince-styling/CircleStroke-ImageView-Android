package com.vincestyling.circleimageView.core;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;

public class RoundRectWithStrokeImplCircleImageView extends CircleImageView {

	public RoundRectWithStrokeImplCircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private RectF mMiddleRect;

	@Override
	protected void roundBitmap(Canvas nativeCanvas, Paint paint) {
		int left = getPaddingLeft();
		int top = getPaddingTop();
		int renderWidth = getWidth() - getPaddingLeft() - getPaddingRight();
		int renderHeight = getHeight() - getPaddingTop() - getPaddingBottom();

		Rect renderRect = new Rect(left, top, renderWidth, renderHeight);

		RectF bottomRect = new RectF(renderRect);
		Paint bottomBorderPaint = new Paint();
		bottomBorderPaint.setStyle(Paint.Style.STROKE);
		bottomBorderPaint.setAntiAlias(true);
		bottomBorderPaint.setColor(Color.parseColor("#1996f9ef"));
		bottomBorderPaint.setStrokeWidth(mBottomCircleIndent);

		float bottomRadius = bottomRect.width() / 2;
		nativeCanvas.drawRoundRect(bottomRect, bottomRadius, bottomRadius, bottomBorderPaint);


		mMiddleRect = new RectF(bottomRect);
		mMiddleRect.inset(mBottomCircleIndent, mBottomCircleIndent);
		Paint middleBorderPaint = new Paint();
		middleBorderPaint.setStyle(Paint.Style.STROKE);
		middleBorderPaint.setAntiAlias(true);
		middleBorderPaint.setColor(Color.WHITE);
		middleBorderPaint.setStrokeWidth(mMiddleCircleIndent);

		float middleRadius = mMiddleRect.width() / 2;
		nativeCanvas.drawRoundRect(mMiddleRect, middleRadius, middleRadius, middleBorderPaint);
	}

	@Override
	protected void cropBitmap(Canvas nativeCanvas, Bitmap contentBitmap, int contentAreaSize, Paint paint) {
		RectF areaRect = new RectF(mMiddleRect);
		areaRect.inset(mMiddleCircleIndent, mMiddleCircleIndent);
		BitmapShader mAreaShader = new BitmapShader(contentBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		Paint areaPaint = new Paint();
		areaPaint.setStyle(Paint.Style.FILL);
		areaPaint.setAntiAlias(true);
		areaPaint.setShader(mAreaShader);

		float areaRadius = areaRect.width() / 2 - 5;
		nativeCanvas.drawRoundRect(areaRect, areaRadius, areaRadius, areaPaint);
	}

}
