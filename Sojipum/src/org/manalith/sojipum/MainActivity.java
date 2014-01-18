package org.manalith.sojipum;

import java.util.ArrayList;
import java.util.List;

import org.manalith.sojipum.db.DatabaseHelper;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class MainActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	@InjectView(R.id.drawer_layout)
	DrawerLayout drawerLayout;

	@InjectView(R.id.left_drawer)
	ListView drawerList;

	private ActionBarDrawerToggle drawerToggle;

	private List<Mode> modes;

	private ListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);

		drawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		drawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description */
		R.string.drawer_close /* "close drawer" description */
		) {
			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				getActionBar().setTitle("소지품");
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle("상태 선택");
			}
		};

		// Set the drawer toggle as the DrawerListener
		drawerLayout.setDrawerListener(drawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		Fragment fragment = new ItemListFragment();
		setFragment(fragment);

		adapter = new ListAdapter(this);
		drawerList.setAdapter(adapter);
		bindItem();
		drawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Mode mode = modes.get(position);

				Bundle args = new Bundle();
				args.putLong("modeId", mode.id);

				Fragment fragment = new ItemListFragment();
				fragment.setArguments(args);
				setFragment(fragment);

				drawerLayout.closeDrawer(drawerList);

				// setFragment();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		bindItem();
	}

	private void bindItem() {
		modes = getHelper().getModeDao().queryForAll();
		adapter.setModes(modes);
	}

	private void setFragment(Fragment fragment) {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		// Handle your other action bar items...
		switch (item.getItemId()) {
		case R.id.action_add_item: {
			Intent i = new Intent(this, AddItemActivity.class);
			startActivity(i);
			break;
		}
		case R.id.action_add_mode: {
			Intent i = new Intent(this, AddModeActivity.class);
			startActivity(i);
			break;
		}
		}

		return super.onOptionsItemSelected(item);
	}

	static class ListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<Mode> modes = new ArrayList<Mode>();

		public ListAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void setModes(List<Mode> modes) {
			this.modes = modes;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return modes.size();
		}

		@Override
		public Object getItem(int position) {
			return modes.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		static class ViewHolder {
			@InjectView(R.id.txtName)
			TextView txtName;

			public ViewHolder(View view) {
				ButterKnife.inject(this, view);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.drawer_list_mode,
						parent, false);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Mode mode = modes.get(position);
			holder.txtName.setText(mode.name);

			return convertView;
		}

	}
}
