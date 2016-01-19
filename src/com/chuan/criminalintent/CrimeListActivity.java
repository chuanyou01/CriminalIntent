package com.chuan.criminalintent;

import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

public class CrimeListActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new CrimeListFragment();
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);	
	}
}
