package com.vincestyling.circleimageView;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import com.vincestyling.circleimageView.page.ClipPathFragment;
import com.vincestyling.circleimageView.page.NaturalFragment;
import com.vincestyling.circleimageView.page.PictureFragment;
import com.vincestyling.circleimageView.page.RoundRectFragment;

public class MainActivity extends Activity implements ActionBar.OnNavigationListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		getActionBar().setListNavigationCallbacks(
				ArrayAdapter.createFromResource(
						getActionBar().getThemedContext(),
						R.array.action_list,
						android.R.layout.simple_spinner_dropdown_item),
				this);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.replace(android.R.id.content, new RoundRectFragment()).commit();
		}
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		Fragment newFragment;
		switch (itemPosition) {
			default:
			case 0:
				newFragment = new RoundRectFragment();
				break;
			case 1:
				newFragment = new PictureFragment();
				break;
			case 2:
				newFragment = new ClipPathFragment();
				break;
			case 3:
				newFragment = new NaturalFragment();
				break;
		}

		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, newFragment).commit();

		return true;
	}

}
