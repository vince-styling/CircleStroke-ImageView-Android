package com.vincestyling.circleimageView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class CircleImageView extends ImageView {
	private int mImgRes;

	private int mMiddleCircleIndent;
	private int mBottomCircleIndent;

	private int mBottomCircleOnColor;
	private int mBottomCircleOffColor;

	private int mMiddleCircleOnColor;
	private int mMiddleCircleOffColor;

	private boolean mIsStatusOn;

	private WeakReference<Bitmap> mOnBitmap;
	private WeakReference<Bitmap> mOffBitmap;

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);

		mImgRes = array.getResourceId(R.styleable.CircleImageView_src, 0);

		mBottomCircleOnColor = array.getColor(R.styleable.CircleImageView_bottomCircleOnColor, 0);
		mBottomCircleOffColor = array.getColor(R.styleable.CircleImageView_bottomCircleOffColor, 0);

		mMiddleCircleOnColor = array.getColor(R.styleable.CircleImageView_middleCircleOnColor, 0);
		mMiddleCircleOffColor = array.getColor(R.styleable.CircleImageView_middleCircleOffColor, 0);

		mBottomCircleIndent = array.getDimensionPixelOffset(R.styleable.CircleImageView_bottomCircleIndent, 0);
		mMiddleCircleIndent = array.getDimensionPixelOffset(R.styleable.CircleImageView_middleCircleIndent, 0);

		array.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (isInEditMode() || mImgRes == 0) {
			super.onDraw(canvas);
			return;
		}

		Bitmap bitmap =
				mIsStatusOn ?
				mOnBitmap != null ? mOnBitmap.get() : null:
				mOffBitmap != null ? mOffBitmap.get() : null;

		if (bitmap == null || bitmap.isRecycled()) {
			bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
			Canvas tmpCanvas = new Canvas(bitmap);

			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setFilterBitmap(true);
			paint.setAntiAlias(true);
			paint.setDither(true);

			int centerX = getWidth() / 2;
			int centerY = getHeight() / 2;


			int bottomCircleRadius = getWidth() / 2;
			if (mBottomCircleIndent > 0) {
				int oriColor = paint.getColor();
				paint.setColor(mIsStatusOn ? mBottomCircleOnColor : mBottomCircleOffColor);
				tmpCanvas.drawCircle(centerX, centerY, bottomCircleRadius, paint);
				paint.setColor(oriColor);
			}


			int middleCircleRadius = bottomCircleRadius - mBottomCircleIndent;
			if (mMiddleCircleIndent > 0) {
				int oriColor = paint.getColor();
				paint.setColor(mIsStatusOn ? mMiddleCircleOnColor : mMiddleCircleOffColor);
				tmpCanvas.drawCircle(centerX, centerY, middleCircleRadius, paint);
				paint.setColor(oriColor);
			}


			Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), mImgRes);

//			int width = originalBitmap.getWidth();
//			int height = originalBitmap.getHeight();
//			float scaleWidth = ((float) headWidth) / width;
//			float scaleHeight = ((float) headWidth) / height;
//			Matrix matrix = new Matrix();
//			matrix.postScale(scaleWidth, scaleHeight);
//			originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, width, height, matrix, false);
//			int newW = originalBitmap.getWidth();
//			int newH = originalBitmap.getHeight();


//			Bitmap scaleBitmap = Bitmap.createBitmap(headWidth, headWidth, Bitmap.Config.RGB_565);
//			Canvas tmpCanvas = new Canvas(scaleBitmap);
//			tmpCanvas.drawBitmap(originalBitmap,
//					new Rect(0, 0, originalBitmap.getWidth(), originalBitmap.getHeight()),
//					new Rect(0, 0, headWidth, headWidth), mPaint);

			BitmapShader shader = new BitmapShader(originalBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
			paint.setShader(shader);

			int contentRadius = middleCircleRadius - mMiddleCircleIndent;
			tmpCanvas.drawCircle(centerX, centerX, contentRadius, paint);

			if (mIsStatusOn) {
				mOnBitmap = new WeakReference<Bitmap>(bitmap);
			} else {
				mOffBitmap = new WeakReference<Bitmap>(bitmap);
			}
		}

		if (bitmap != null) canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mBottomCircleIndent == 0 && mMiddleCircleIndent == 0) return super.onTouchEvent(event);

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mIsStatusOn = true;
				invalidate();
				return true;
			case MotionEvent.ACTION_UP:
				mIsStatusOn = false;
				invalidate();
				return true;
		}

		return super.onTouchEvent(event);
	}
}
