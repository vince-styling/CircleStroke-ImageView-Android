/**
 * Copyright (C) 2014 Vince Styling
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vincestyling.circleimageView.core;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import com.vincestyling.circleimageView.R;

import java.lang.ref.WeakReference;

public abstract class CircleImageView extends ImageView {
	protected int mBottomCircleIndent;
	protected int mMiddleCircleIndent;

	protected int mBottomCircleOnColor;
	protected int mBottomCircleOffColor;

	protected int mMiddleCircleOnColor;
	protected int mMiddleCircleOffColor;

	protected boolean mIsStatusOn;

	protected WeakReference<Bitmap> mOnBitmap;
	protected WeakReference<Bitmap> mOffBitmap;

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
			bitmap = produceBitmap();
		}

		if (bitmap != null) canvas.drawBitmap(bitmap, 0, 0, null);
	}

	private Bitmap produceBitmap() {
		Drawable srcDrawable = getDrawable();
		if (srcDrawable == null) return null;

		Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		Canvas nativeCanvas = new Canvas(bitmap);

		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setFilterBitmap(true);
		paint.setAntiAlias(true);
		paint.setDither(true);

		// ------------------------------ draw rounding bitmap
		roundBitmap(nativeCanvas, paint);

		// ------------------------------ draw content bitmap with proper size (zoom in|zoom out)
		int contentAreaSize = getWidth() - mBottomCircleIndent * 2;
		Bitmap contentBitmap = Bitmap.createBitmap(contentAreaSize, contentAreaSize, Bitmap.Config.ARGB_8888);
		Canvas contentCanvas = new Canvas(contentBitmap);
		srcDrawable.setBounds(0, 0, contentAreaSize, contentAreaSize);
		srcDrawable.draw(contentCanvas);

		cropBitmap(nativeCanvas, contentBitmap, contentAreaSize, paint);

		if (mIsStatusOn) {
			mOnBitmap = new WeakReference<Bitmap>(bitmap);
		} else {
			mOffBitmap = new WeakReference<Bitmap>(bitmap);
		}

		return bitmap;
	}

	protected void roundBitmap(Canvas nativeCanvas, Paint paint) {
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
	}

	protected abstract void cropBitmap(Canvas nativeCanvas, Bitmap contentBitmap, int contentAreaSize, Paint paint);

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
				if (mBottomCircleIndent > 0 || mMiddleCircleIndent > 0) {
					mIsStatusOn = true;
					invalidate();
				}
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
