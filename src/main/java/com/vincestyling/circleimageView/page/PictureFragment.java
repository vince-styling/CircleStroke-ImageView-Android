package com.vincestyling.circleimageView.page;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.vincestyling.circleimageView.Netroid;
import com.vincestyling.circleimageView.R;

public class PictureFragment extends Fragment {
//	String photo = "http://tp1.sinaimg.cn/1768720064/180/40043799298/1";
	String photo = "https://avatars3.githubusercontent.com/u/3348207?s=460";
//	String photo = "http://ww1.sinaimg.cn/crop.1.87.442.442.1024/696c86c0gw1ecb74t8l2xj20cd0hegna.jpg";
//	String photo = "http://ww1.sinaimg_diderror.cn/crop.1.87.442.442.1024/696c86c0gw1ecb74t8l2xj20cd0hegna.jpg";

	protected int mResId;
	public PictureFragment() {
		mResId = R.layout.picture_lot;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(mResId, container, false);
		initView(view);
		return view;
	}

	protected void initView(View view) {
		ImageView imvMemberHead = (ImageView) view.findViewById(R.id.imvMemberHead);
		Netroid.displayImage(photo, imvMemberHead,
				R.drawable.aboutme_log_default_head, R.drawable.aboutme_unlog_default_head_sml);

		imvMemberHead.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showToastMsg("imvMemberHead click!");
			}
		});

		view.findViewById(R.id.imvWithoutBorder).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showToastMsg("imvWithoutBorder click!");
			}
		});
	}

	Toast mToast;
	protected void showToastMsg(String msg) {
		if (mToast != null) mToast.cancel();
		mToast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
		mToast.show();
	}

}
