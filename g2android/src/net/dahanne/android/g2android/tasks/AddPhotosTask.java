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
import java.util.ArrayList;

import net.dahanne.android.g2android.activity.Settings;
import net.dahanne.android.g2android.utils.G2ConnectionUtils;
import net.dahanne.android.g2android.utils.GalleryConnectionException;
import net.dahanne.android.g2android.utils.ShowUtils;
import net.dahanne.android.g2android.utils.UriUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.Window;

public class AddPhotosTask extends AsyncTask<Object, Integer, Void> {
	private String exceptionMessage = null;
	Activity activity;
	private String galleryUrl;
	private final ProgressDialog progressDialog;

	public AddPhotosTask(Activity context, ProgressDialog progressDialog) {
		super();
		activity = context;
		this.progressDialog = progressDialog;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Void doInBackground(Object... parameters) {
		String galleryUrl = (String) parameters[0];
		Integer albumName = (Integer) parameters[1];
		ArrayList<Uri> photoUris = (ArrayList<Uri>) parameters[2];
		boolean mustLogIn = (Boolean) parameters[3];

		try {
			if (mustLogIn) {
				G2ConnectionUtils.getInstance().loginToGallery(galleryUrl,
						Settings.getUsername(activity),
						Settings.getPassword(activity));
			}

			for (Uri photoUri : photoUris) {
				File imageFile = UriUtils.getFileFromUri(photoUri, activity);
				String imageName = UriUtils.getFileNameFromUri(photoUri,
						activity);
				G2ConnectionUtils.getInstance().sendImageToGallery(galleryUrl,
						albumName, imageFile, imageName,
						Settings.getDefaultSummary(activity),
						Settings.getDefaultDescription(activity));
				publishProgress((int) (((photoUris.indexOf(photoUri) + 1) / (float) photoUris
						.size()) * 10000));
			}
		} catch (GalleryConnectionException e) {
			exceptionMessage = e.getMessage();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void useless) {

		progressDialog.dismiss();
		if (exceptionMessage != null) {
			// Something went wrong
			ShowUtils.getInstance().alertConnectionProblem(exceptionMessage,
					galleryUrl, activity);
		} else {
			ShowUtils.getInstance().toastImagesSuccessfullyAdded(activity);
		}

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		activity.getWindow().setFeatureInt(Window.FEATURE_PROGRESS, 0);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		activity.getWindow().setFeatureInt(Window.FEATURE_PROGRESS, values[0]);
	}

}
