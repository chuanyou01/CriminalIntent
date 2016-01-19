package com.chuan.criminalintent;

import java.util.ArrayList;

import android.R.anim;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CrimeListFragment extends ListFragment {
	private String TAG= "CrimelistFragment";
	private static final int REQUEST_CRIME = 1;
	private ArrayList<Crime> mCrimes;
	private boolean mSubTitleShow = false;
	private View mEmpty;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		setRetainInstance(true);
		mSubTitleShow = false;
		
		getActivity().setTitle(R.string.crimes_title);
		mCrimes = CrimeLab.get(getActivity()).getCrimes();
		CrimeAdapter adapter = new CrimeAdapter(mCrimes);
		setListAdapter(adapter);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
		 mEmpty = getActivity().getLayoutInflater()
				.inflate(R.layout.fragment_empty_view, container, false);		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (mSubTitleShow) {
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
			}
		}
		container.addView(mEmpty);
		Button btnAdd = (Button)mEmpty.findViewById(R.id.crime_emptyview_add);
		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				addCrime();
			}
		});
		ListView lstView =  (ListView) v.findViewById(android.R.id.list);
		lstView.setEmptyView(mEmpty);
		return v;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
//		getListView().setEmptyView(mEmpty);
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Crime c =((CrimeAdapter)getListAdapter()).getItem(position);
		
//		Intent i = new Intent(getActivity(), CrimeActivity.class);
		Intent i = new Intent(getActivity(), CrimePagerActivity.class);
		i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
		//startActivity(i);
		startActivityForResult(i, REQUEST_CRIME);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode==REQUEST_CRIME) {
			
		}
	}
	private class CrimeAdapter extends ArrayAdapter<Crime>{
		public CrimeAdapter(ArrayList<Crime> crimes) {
			super(getActivity()	, 0, crimes);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView==null) {
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.list_item_crime, null);
			}
			
			Crime c = getItem(position);
			TextView titleTextView = (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
			titleTextView.setText(c.getTitle());
			TextView DateTextView = (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
			DateTextView.setText(c.getDate().toString());
			CheckBox solvedCheckBox= (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
			solvedCheckBox.setChecked(c.isbSolved());
	
			return convertView;
		}
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
		MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
		if (mSubTitleShow && showSubtitle!=null) {
			showSubtitle.setTitle(R.string.hide_subtitle);
		}
	}
	private void addCrime(){
		Crime crime = new Crime();
		CrimeLab.get(getActivity()).addCrime(crime);
		Intent i = new Intent(getActivity(), CrimePagerActivity.class);
		i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
		startActivityForResult(i, REQUEST_CRIME);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_new_crime:
			addCrime();
			return true;
		case R.id.menu_item_show_subtitle:
			if(getActivity().getActionBar().getSubtitle()==null){
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
				item.setTitle(R.string.hide_subtitle);
				mSubTitleShow = true;
			}else{
				getActivity().getActionBar().setSubtitle(null);
				item.setTitle(R.string.show_subtitle);
				mSubTitleShow = false;
			}
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
