package org.manalith.sojipum;

import java.util.ArrayList;
import java.util.List;

import org.manalith.sojipum.db.DatabaseHelper;

import android.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class ItemListFragment extends Fragment {

	private List<Item> items = new ArrayList<Item>();

	@InjectView(R.id.listItems)
	ListView list;

	private ListAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_item_list, container,
				false);
		ButterKnife.inject(this, view);

		adapter = new ListAdapter(items,
				(OrmLiteBaseActivity<DatabaseHelper>) getActivity());
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				CheckBox chkCarried = (CheckBox) arg1
						.findViewById(R.id.chkCarried);
				chkCarried.setChecked(!chkCarried.isChecked());

			}
		});
		registerForContextMenu(list);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		bindItem();
	}

	private void bindItem() {
		Bundle args = getArguments();
		long modeId = args == null ? 0 : args.getLong("modeId");

		if (modeId != 0)
			items = getDao().queryForEq("modeId", modeId);
		else
			items = getDao().queryForAll();

		adapter.setItems(items);
	}

	private RuntimeExceptionDao<Item, Integer> getDao() {
		@SuppressWarnings("unchecked")
		RuntimeExceptionDao<Item, Integer> dao = ((OrmLiteBaseActivity<DatabaseHelper>) getActivity())
				.getHelper().getItemDao();

		return dao;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.listItems) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(items.get(info.position).name);
			menu.add(Menu.NONE, 0, 0, "수정");
			menu.add(Menu.NONE, 1, 1, "삭제");
			// String[] menuItems = getResources().getStringArray(R.array.menu);
			// for (int i = 0; i < menuItems.length; i++) {
			// menu.add(Menu.NONE, i, i, menuItems[i]);
			// }
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem menuItem) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem
				.getMenuInfo();
		Item item = items.get(info.position);

		switch (menuItem.getItemId()) {
		case 0: {
			break;
		}
		case 1: {
			getDao().deleteById((int) item.id);
			bindItem();
			break;
		}
		}

		return super.onContextItemSelected(menuItem);
	}

	static class ListAdapter extends BaseAdapter {
		private OrmLiteBaseActivity<DatabaseHelper> activity;
		private LayoutInflater inflater;
		private List<Item> items;

		public ListAdapter(List<Item> items,
				OrmLiteBaseActivity<DatabaseHelper> activity) {
			this.activity = activity;
			inflater = LayoutInflater.from(activity);
			this.items = items;
		}

		public void setItems(List<Item> items) {
			this.items = items;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		static class ViewHolder {
			@InjectView(R.id.chkCarried)
			CheckBox chkCarried;

			@InjectView(R.id.txtName)
			TextView txtName;

			@InjectView(R.id.txtMode)
			TextView txtMode;

			public ViewHolder(View view) {
				ButterKnife.inject(this, view);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_list_item, parent,
						false);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Item item = items.get(position);
			holder.txtName.setText(item.name);
			holder.txtMode.setText(item.mode.name);
			holder.chkCarried.setTag(position);
			holder.chkCarried.setChecked(item.carried);
			holder.chkCarried.setOnCheckedChangeListener(checkListener);

			return convertView;
		}

		private OnCheckedChangeListener checkListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				int position = (Integer) buttonView.getTag();
				Item item = items.get(position);
				item.carried = isChecked;
				activity.getHelper().getItemDao().update(item);
			}
		};
	}

	// XXX DELETEME
	@Deprecated
	private List<Item> getTestItems() {
		List<Item> items = new ArrayList<Item>();

		Mode travelMode = new Mode();
		travelMode.name = "단거리 여행";

		Item item = new Item();
		item.name = "마우스";
		item.mode = travelMode;
		items.add(item);

		item = new Item();
		item.name = "노트북";
		item.mode = travelMode;
		items.add(item);

		item = new Item();
		item.name = "키보드";
		item.mode = travelMode;
		items.add(item);

		Mode normalMode = new Mode();
		normalMode.name = "평상시";

		for (int i = 0; i < 30; i++) {
			item = new Item();
			item.name = "기타등등";
			item.mode = normalMode;
			items.add(item);
		}

		return items;
	}

}
