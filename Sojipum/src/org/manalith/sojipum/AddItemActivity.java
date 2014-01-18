package org.manalith.sojipum;

import java.util.List;

import org.manalith.sojipum.db.DatabaseHelper;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class AddItemActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	@InjectView(R.id.txtName)
	TextView txtName;

	@InjectView(R.id.ddMode)
	Spinner ddMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);
		ButterKnife.inject(this);

		List<Mode> modes = getHelper().getModeDao().queryForAll();

		ArrayAdapter<Mode> adapter = new ArrayAdapter<Mode>(this,
				android.R.layout.simple_spinner_item,
				modes.toArray(new Mode[] {}));
		ddMode.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case R.id.action_finish: {
			Item item = new Item();
			Mode mode = (Mode) ddMode.getSelectedItem();
			item.mode = mode;
			item.name = txtName.getText().toString();
			getHelper().getItemDao().create(item);
			finish();
			break;
		}
		}

		return super.onOptionsItemSelected(menuItem);
	}

}
