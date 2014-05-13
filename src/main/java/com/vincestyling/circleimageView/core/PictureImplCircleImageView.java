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
import android.graphics.*;
import android.util.AttributeSet;
import com.vincestyling.circleimageView.core.CircleImageView;

public class PictureImplCircleImageView extends CircleImageView {

	public PictureImplCircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void cropBitmap(Canvas nativeCanvas, Bitmap contentBitmap, int contentAreaSize, Paint paint) {
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
	}

}
