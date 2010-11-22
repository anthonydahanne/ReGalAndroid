/**
 *  ReGalAndroid, a gallery client for Android, supporting G2, G3, etc...
 *  URLs: https://github.com/anthonydahanne/ReGalAndroid , http://blog.dahanne.net
 *  Copyright (c) 2010 Anthony Dahanne
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.dahanne.android.regalandroid.activity;

import net.dahanne.android.regalandroid.R;
import net.dahanne.android.regalandroid.tasks.LoginTask;
import net.dahanne.android.regalandroid.utils.DBHelper;
import net.dahanne.android.regalandroid.utils.FileUtils;
import net.dahanne.android.regalandroid.utils.ShowUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Start extends Activity implements OnClickListener {
	private Button enterGalleryButton;
	private TextView galleryConfiguredTextView;
	private TextView loggedInAsText;
	private ProgressDialog progressDialog;
	private DBHelper dbHelper;
	private final FileUtils fileUtils = FileUtils.getInstance();
	private final Logger logger = LoggerFactory.getLogger(Start.class);

	@Override
	protected void onPause() {
		super.onPause();
		logger.debug("onPausing");
		dbHelper.cleanup();
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		logger.debug("onCreating");
		super.onCreate(savedInstanceState);
		dbHelper = new DBHelper(this);
		dbHelper.deleteAll();

		setContentView(R.layout.main);
		if (Settings.isCacheClearedEverySession(this)) {
			fileUtils.clearCache(this);
		}

		enterGalleryButton = (Button) findViewById(R.id.enter_gallery_button);
		loggedInAsText = (TextView) findViewById(R.id.loggedin_as_id);
		galleryConfiguredTextView = (TextView) findViewById(R.id.gallery_configured);

		enterGalleryButton.setOnClickListener(this);
		// if this is the first launch, we print a screen to explain the user
		// what it is all about !
		if (!FirstTimePreference.getFirsTime(this)) {
			startActivity(new Intent(this, FirstTime.class));
		}

	}

	/**
	 * if a gallery URL is already configured, we print it in this view,
	 * otherwise we keep the default text and we disable the button
	 */
	private void checkGalleryUrlIsValid() {
		logger.debug("checking GalleryUrl Is Valid");
		// GalleryUrl is provided, but is it a valid Gallery2 URL ?
		if (StringUtils.isNotBlank(Settings.getGalleryUrl(this))) {
			progressDialog = ProgressDialog.show(this,
					getString(R.string.please_wait),
					getString(R.string.connecting_to_the_gallery), true);

			String username = Settings.getUsername(this);
			String password = Settings.getPassword(this);
			String galleryUrl = Settings.getGalleryUrl(this);
			new LoginTask(this, progressDialog, loggedInAsText,
					galleryConfiguredTextView, enterGalleryButton).execute(
					galleryUrl, username, password);
		} else {
			enterGalleryButton.setEnabled(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.settings:
			intent = new Intent(this, Settings.class);
			startActivity(intent);
			break;

		case R.id.about:
			intent = new Intent(this, About.class);
			startActivity(intent);
			break;

		case R.id.clear_cache:
			intent = new Intent(this, Settings.class);
			FileUtils.getInstance().clearCache(this);
			ShowUtils.getInstance().toastCacheSuccessfullyCleared(this);
			break;

		}
		return false;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.enter_gallery_button:
			startActivity(new Intent(this, ShowAlbums.class));
			break;
		}
	}

	@Override
	protected void onResume() {
		logger.debug("onResuming");
		super.onResume();
		// we check if we already have a gallery configured
		checkGalleryUrlIsValid();
	}

}