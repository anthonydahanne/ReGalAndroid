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

package net.dahanne.android.regalandroid.tasks;

import net.dahanne.android.regalandroid.R;
import net.dahanne.android.regalandroid.activity.UploadPhoto;
import net.dahanne.android.regalandroid.remote.RemoteGalleryConnectionFactory;
import net.dahanne.gallery.commons.remote.ImpossibleToLoginException;
import net.dahanne.gallery.commons.utils.UriUtils;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;

public class LoginTask extends AsyncTask<Object, Object, String> {

	private static final String NOTLOGGEDIN = "NOTLOGGEDIN";
	private static final String GUEST = "guest";
	private String user;
	private boolean galleryUrlIsValid = false;;
	private final Activity activity;
	private final ProgressDialog progressDialog;
	private String galleryUrl;
	private final TextView loggedInAsText;
	private final TextView galleryConfiguredTextView;
	private final Button enterGalleryButton;

	public LoginTask(Activity context, ProgressDialog progressDialog,
			TextView loggedInAsText, TextView galleryConfiguredTextView,
			Button enterGalleryButton) {
		super();
		this.activity = context;
		this.progressDialog = progressDialog;
		this.loggedInAsText = loggedInAsText;
		this.galleryConfiguredTextView = galleryConfiguredTextView;
		this.enterGalleryButton = enterGalleryButton;
	}

	@Override
	protected String doInBackground(Object... parameters) {
		String exceptionMessage = null;
		try {
			galleryUrl = (String) parameters[0];
			user = (String) parameters[1];
			String password = (String) parameters[2];
			galleryUrlIsValid = UriUtils.checkUrlIsValid(galleryUrl);
			if (StringUtils.isNotBlank(user)) {
				// the first thing is to login, if an username and password
				// are supplied !
				// This is done once and for all as the session cookie will
				// be stored !
				RemoteGalleryConnectionFactory.getInstance().loginToGallery();
			}
		} catch (ImpossibleToLoginException e) {
			// the connection went wrong, the authToken is then null
			galleryUrlIsValid = false;
			exceptionMessage = e.getMessage();
		}
		return exceptionMessage;
	}

	@Override
	protected void onPostExecute(String exceptionMessage) {
		if (galleryUrlIsValid) {
			if (loggedInAsText != null) {
				if (exceptionMessage != null) {
					// we 're not logged in
					loggedInAsText.setText(activity
							.getString(R.string.loggedin_as) + " " + GUEST);
					String message = activity.getString(R.string.not_connected)
							+ galleryUrl
							+ activity.getString(R.string.exception_thrown)
							+ exceptionMessage;
					showAlert(message);
				} else {
					// we are logged in
					loggedInAsText.setText(activity
							.getString(R.string.loggedin_as) + " " + user);
				}
				galleryConfiguredTextView.setText(galleryUrl);
				enterGalleryButton.setEnabled(true);
				if (activity instanceof UploadPhoto) {
					// we have to call it back to continue
					((UploadPhoto) activity).showAlbumList();
				}
			}

		} else {
			// neither login or simple command worked, we're offline
			if (loggedInAsText != null) {
				galleryConfiguredTextView
						.setText(R.string.regalandroid_no_gallery_configured);
				enterGalleryButton.setEnabled(false);
				loggedInAsText.setText(activity
						.getString(R.string.not_logged_in));
			}
			String message;
			// if there was an exception thrown, show it, or say to verify
			// settings
			if (exceptionMessage != null) {
				message = activity.getString(R.string.not_connected)
						+ galleryUrl
						+ activity.getString(R.string.exception_thrown)
						+ exceptionMessage;
			} else {

				message = activity.getString(R.string.not_connected)
						+ galleryUrl
						+ activity.getString(R.string.verify_your_settings);
			}
			showAlert(message);

		}
		progressDialog.dismiss();

	}

	private void showAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(R.string.problem)
				.setMessage(message)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
