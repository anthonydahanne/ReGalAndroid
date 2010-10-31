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
package net.dahanne.android.regalandroid.tasks;

import java.io.File;

import net.dahanne.android.regalandroid.activity.Settings;
import net.dahanne.android.regalandroid.remote.RemoteGalleryConnectionFactory;
import net.dahanne.android.regalandroid.utils.ShowUtils;
import net.dahanne.android.regalandroid.utils.AndroidUriUtils;
import net.dahanne.gallery.commons.remote.GalleryConnectionException;
import net.dahanne.gallery.commons.remote.RemoteGallery;
import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;

public class AddPhotoTask extends AsyncTask<Object, Void, String> {
	private String exceptionMessage = null;
	Activity activity;
	private String galleryUrl;
	private final ProgressDialog progressDialog;
	private final RemoteGallery remoteGallery;

	public AddPhotoTask(Activity context, ProgressDialog progressDialog) {
		super();
		remoteGallery = RemoteGalleryConnectionFactory.getInstance();
		activity = context;
		this.progressDialog = progressDialog;
	}

	@Override
	protected String doInBackground(Object... parameters) {
		String galleryUrl = (String) parameters[0];
		Integer albumName = (Integer) parameters[1];
		Uri photoUri = (Uri) parameters[2];
		boolean mustLogIn = (Boolean) parameters[3];
		String imageName = (String) parameters[4];
		File imageFile = (File) parameters[5];

		// not from the camera
		if (imageFile == null) {
			imageFile = AndroidUriUtils.getFileFromUri(photoUri, activity);
		}

		try {
			if (mustLogIn) {
				remoteGallery.loginToGallery(galleryUrl,
						Settings.getUsername(activity),
						Settings.getPassword(activity));
			}
			remoteGallery.sendImageToGallery(galleryUrl, albumName, imageFile,
					imageName, Settings.getDefaultSummary(activity),
					Settings.getDefaultDescription(activity));
		} catch (GalleryConnectionException e) {
			exceptionMessage = e.getMessage();
		}
		return imageName;
	}

	@Override
	protected void onPostExecute(String imageName) {

		progressDialog.dismiss();
		if (exceptionMessage != null) {
			// Something went wrong
			ShowUtils.getInstance().alertConnectionProblem(exceptionMessage,
					galleryUrl, activity);
		} else {
			ShowUtils.getInstance().toastImageSuccessfullyAdded(activity,
					imageName);
		}

	}

}