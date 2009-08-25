/*
 * G2Android
 * Copyright (c) 2009 Anthony Dahanne
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.dahanne.android.g2android.activity;

import net.dahanne.android.g2android.R;
import net.dahanne.android.g2android.utils.G2ConnectionUtils;
import net.dahanne.android.g2android.utils.GalleryConnectionException;
import net.dahanne.android.g2android.utils.ToastExceptionUtils;
import android.app.Activity;
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
	private static final String TAG = "StartActivity";
	private Button enterGalleryButton;
	private TextView galleryConfiguredTextView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		enterGalleryButton = (Button) findViewById(R.id.enter_gallery_button);
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
		if (Settings.getGalleryUrl(this) != null
				&& !Settings.getGalleryUrl(this).equals("")) {
			// GalleryUrl is provided, but is it a Gallery2 URL ?
			try {
				if (G2ConnectionUtils.checkGalleryUrlIsValid(Settings
						.getGalleryHost(this), Settings.getGalleryPath(this),
						Settings.getGalleryPort(this))) {
					galleryConfiguredTextView.setText(Settings
							.getGalleryUrl(this));
					enterGalleryButton.setEnabled(true);

					return;
				} else {
					galleryConfiguredTextView
							.setText(R.string.gallery_url_is_not_valid);
				}
			} catch (NumberFormatException e) {
				ToastExceptionUtils.toastNumberFormatException(this, e);
			} catch (GalleryConnectionException e) {
				ToastExceptionUtils.toastGalleryException(this, e);
			}
		} else {
			galleryConfiguredTextView
					.setText(R.string.g2android_no_gallery_configured);
		}
		enterGalleryButton.setEnabled(false);
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

		super.onResume();
		// Log.i(TAG, "Starting");
		checkGalleryUrlIsValid();

	}

}