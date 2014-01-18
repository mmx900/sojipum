package org.manalith.sojipum;

import org.manalith.sojipum.db.DatabaseHelper;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class AddModeActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	@InjectView(R.id.txtName)
	TextView txtName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_mode);
		ButterKnife.inject(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_mode, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_finish: {
			Mode mode = new Mode();
			mode.name = txtName.getText().toString();
			getHelper().getModeDao().create(mode);

			finish();
			break;
		}
		}

		return super.onOptionsItemSelected(item);
	}
}
