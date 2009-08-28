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
import net.dahanne.android.g2android.utils.ToastUtils;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
	private TextView loggedInAsText;
	private ProgressDialog progressDialog;
	private String galleryUrl;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		enterGalleryButton = (Button) findViewById(R.id.enter_gallery_button);
		galleryConfiguredTextView = (TextView) findViewById(R.id.gallery_configured);
		loggedInAsText = (TextView) findViewById(R.id.loggedin_as_id);
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
	@SuppressWarnings("unchecked")
	private void checkGalleryUrlIsValid() {
		if (Settings.getGalleryUrl(this) != null
				&& !Settings.getGalleryUrl(this).equals("")) {
			// GalleryUrl is provided, but is it a valid Gallery2 URL ?
			galleryUrl = Settings.getGalleryUrl(this);
			progressDialog = ProgressDialog.show(this,
					getString(R.string.app_name),
					getString(R.string.connecting_to_the_gallery), true);

			String galleryHost = Settings.getGalleryHost(this);
			String galleryPath = Settings.getGalleryPath(this);
			int galleryPort = Settings.getGalleryPort(this);
			String username = Settings.getUsername(this);
			String password = Settings.getPassword(this);

			new LoginTask().execute(galleryHost, galleryPath, galleryPort,
					username, password);
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
		}

		return false;
	}

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

	@SuppressWarnings("unchecked")
	private class LoginTask extends AsyncTask {

		private static final String NOTLOGGEDIN = "NOTLOGGEDIN";
		private static final String GUEST = "guest";
		private String password;
		private String user;
		private Integer galleryPort;
		private String galleryPath;
		private String galleryHost;
		private boolean galleryUrlIsValid;

		@Override
		protected String doInBackground(Object... parameters) {
			String authToken = null;
			try {
				galleryHost = (String) parameters[0];
				galleryPath = (String) parameters[1];
				galleryPort = (Integer) parameters[2];
				user = (String) parameters[3];
				password = (String) parameters[4];
				if (StringUtils.isNotBlank(user)) {
					// the first thing is to login, if an username and password
					// are supplied !
					// This is done once and for all as the session cookie will
					// be stored !
					authToken = G2ConnectionUtils.loginToGallery(galleryHost,
							galleryPath, galleryPort, user, password);
					if(authToken==null){
						authToken=NOTLOGGEDIN;
						galleryUrlIsValid = G2ConnectionUtils.checkGalleryUrlIsValid(galleryHost,
								galleryPath, galleryPort);
					}
				} else {
					authToken=NOTLOGGEDIN;
					// no username just send a random command to the gallery to
					// check it answers
					galleryUrlIsValid = G2ConnectionUtils.checkGalleryUrlIsValid(galleryHost,
							galleryPath, galleryPort);
				}
			} catch (GalleryConnectionException e) {
				ToastUtils.toastGalleryException(Start.this, e);
			}
			return authToken;
		}

		@Override
		protected void onPostExecute(Object authToken) {
			if (galleryUrlIsValid ) {
				if (authToken.equals(NOTLOGGEDIN)) {
					// we 're not logged in
					loggedInAsText.setText(getString(R.string.loggedin_as)+" "
							+ GUEST);
				} else {
					// we are logged in
					loggedInAsText.setText(getString(R.string.loggedin_as)+" "
							+ user);
				}
				galleryConfiguredTextView.setText(galleryUrl);
				enterGalleryButton.setEnabled(true);
			} else {
				// neither login or simple command worked, we're offline
				galleryConfiguredTextView
						.setText(R.string.g2android_no_gallery_configured);
				enterGalleryButton.setEnabled(false);
				AlertDialog.Builder builder = new AlertDialog.Builder(Start.this);
				builder.setTitle(R.string.app_name).setMessage( getString(R.string.not_connected) +galleryUrl+getString(R.string.verify_your_settings))
				       .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   dialog.cancel();
				           }
				       })
				       ;
				AlertDialog alert = builder.create();
				alert.show();

			}
			progressDialog.dismiss();

		}
	}

}