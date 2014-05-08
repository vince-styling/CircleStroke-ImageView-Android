package com.vincestyling.circleimageView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class CircleImageView extends ImageView {
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
		if (isInEditMode()) {
			super.onDraw(canvas);
			return;
		}

		Bitmap bitmap =
				mIsStatusOn ?
				mOnBitmap != null ? mOnBitmap.get() : null:
				mOffBitmap != null ? mOffBitmap.get() : null;

		if (bitmap == null || bitmap.isRecycled()) {
			Drawable srcDrawable = getDrawable();
			if (srcDrawable == null) return;

			bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
			Canvas nativeCanvas = new Canvas(bitmap);

			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setFilterBitmap(true);
			paint.setAntiAlias(true);
			paint.setDither(true);

			float centerX = getWidth() / 2;
			float centerY = getHeight() / 2;


			// ------------------------------ draw bottom circle
			float bottomCircleRadius = getWidth() / 2;
			if (mBottomCircleIndent > 0) {
				int oriColor = paint.getColor();
				paint.setColor(mIsStatusOn ? mBottomCircleOnColor : mBottomCircleOffColor);
				nativeCanvas.drawCircle(centerX, centerY, bottomCircleRadius, paint);
				paint.setColor(oriColor);
			}


			// ------------------------------ draw middle circle
			float middleCircleRadius = bottomCircleRadius - mBottomCircleIndent;
			if (mMiddleCircleIndent > 0) {
				int oriColor = paint.getColor();
				paint.setColor(mIsStatusOn ? mMiddleCircleOnColor : mMiddleCircleOffColor);
				nativeCanvas.drawCircle(centerX, centerY, middleCircleRadius, paint);
				paint.setColor(oriColor);
			}


			// ------------------------------ draw content bitmap with proper size (zoom in|zoom out)
			int contentAreaSize = getWidth() - mBottomCircleIndent * 2;
			Bitmap contentBitmap = Bitmap.createBitmap(contentAreaSize, contentAreaSize, Bitmap.Config.ARGB_8888);
			Canvas contentCanvas = new Canvas(contentBitmap);
			srcDrawable.setBounds(0, 0, contentAreaSize, contentAreaSize);
			srcDrawable.draw(contentCanvas);


			// ------------------------------ make content bitmap center
			Bitmap cropBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
			Canvas cropCanvas = new Canvas(cropBitmap);
			cropCanvas.drawBitmap(contentBitmap, mBottomCircleIndent, mBottomCircleIndent, paint);


			// ------------------------------ recording the mask bitmap
			Picture picture = new Picture();
			Canvas pitCanvas = picture.beginRecording(contentAreaSize, contentAreaSize);
			Path p = new Path();
			int maskRadius = contentAreaSize / 2;
			p.addCircle(maskRadius, maskRadius, maskRadius - mMiddleCircleIndent, Path.Direction.CW);
			pitCanvas.drawPath(p, paint);
			picture.endRecording();

			Bitmap maskBitmap = Bitmap.createBitmap(contentAreaSize, contentAreaSize, Bitmap.Config.ARGB_8888);
			Canvas maskCanvas = new Canvas(maskBitmap);
			maskCanvas.drawPicture(picture);

			// ------------------------------ crop content bitmap by mask bitmap
			paint.reset();
			paint.setFilterBitmap(false);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
			cropCanvas.drawBitmap(maskBitmap, mBottomCircleIndent, mBottomCircleIndent, paint);


			// ------------------------------ draw cropped content bitmap
			paint.setXfermode(null);
			nativeCanvas.drawBitmap(cropBitmap, 0, 0, paint);

			if (mIsStatusOn) {
				mOnBitmap = new WeakReference<Bitmap>(bitmap);
			} else {
				mOffBitmap = new WeakReference<Bitmap>(bitmap);
			}
		}

		if (bitmap != null) canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
	}

	@Override
	public void setImageResource(int resId) {
		clearBitmapCache();
		super.setImageResource(resId);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		clearBitmapCache();
		super.setImageBitmap(bm);
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		clearBitmapCache();
		super.setImageDrawable(drawable);
	}

	private void clearBitmapCache() {
		if (mOffBitmap != null) mOffBitmap.clear();
		if (mOnBitmap != null) mOnBitmap.clear();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mIsStatusOn = true;
				invalidate();
				return true;
			case MotionEvent.ACTION_CANCEL:
				mIsStatusOn = false;
				invalidate();
				return false;
			case MotionEvent.ACTION_UP:
				mIsStatusOn = false;
				performClick();
				invalidate();
				return false;
		}
		return super.onTouchEvent(event);
	}
}
