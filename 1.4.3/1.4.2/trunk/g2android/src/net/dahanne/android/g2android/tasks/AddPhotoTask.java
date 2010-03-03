/**
 * g2android
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
package net.dahanne.android.g2android.tasks;

import java.io.File;
import java.io.InputStream;

import net.dahanne.android.g2android.activity.Settings;
import net.dahanne.android.g2android.activity.ShowGallery;
import net.dahanne.android.g2android.utils.G2ConnectionUtils;
import net.dahanne.android.g2android.utils.ShowUtils;
import net.dahanne.android.g2android.utils.UriUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

/**
 * @author Anthony Dahanne
 * 
 */
@SuppressWarnings("unchecked")
public class AddPhotoTask extends AsyncTask {
	private String exceptionMessage = null;
	Activity activity;
	private String galleryUrl;
	private ProgressDialog progressDialog;

	public AddPhotoTask(Activity context, ProgressDialog progressDialog) {
		super();
		this.activity = context;
		this.progressDialog = progressDialog;
	}

	@Override
	protected Integer doInBackground(Object... parameters) {
		String galleryUrl = (String) parameters[0];
		Integer albumName = (Integer) parameters[1];
		Uri photoUri = (Uri) parameters[2];
		boolean mustLogIn = (Boolean) parameters[3];
		Log.d("addphototask", Boolean.toString(mustLogIn));
		Integer imageCreatedName = null;
		try {
			String mimeType = activity.getContentResolver().getType(photoUri);
			InputStream openInputStream = activity.getContentResolver()
					.openInputStream(photoUri);
			File imageFile = UriUtils.createFileFromUri(openInputStream,
					mimeType);
			if (mustLogIn) {
				G2ConnectionUtils.getInstance().loginToGallery(galleryUrl,
						Settings.getUsername(activity),
						Settings.getPassword(activity));
			}
			imageCreatedName = G2ConnectionUtils.getInstance()
					.sendImageToGallery(galleryUrl, albumName, imageFile);
			imageFile.delete();
		} catch (Exception e) {
			exceptionMessage = e.getMessage();
		}
		return imageCreatedName;
	}

	@Override
	protected void onPostExecute(Object imageCreatedName) {

		progressDialog.dismiss();
		if (exceptionMessage != null) {
			// Something went wrong
			ShowUtils.getInstance().alertConnectionProblem(exceptionMessage,
					galleryUrl, activity);
		} else {
			// we refresh the photos if we are in the ShowGallery class
			if (ShowGallery.class.isInstance(activity)) {

			}
			ShowUtils.getInstance().toastImageSuccessfullyAdded(activity);
		}

	}
}
