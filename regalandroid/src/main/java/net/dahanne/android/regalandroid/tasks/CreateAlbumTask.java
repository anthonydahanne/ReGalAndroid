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

import net.dahanne.android.regalandroid.RegalAndroidApplication;
import net.dahanne.android.regalandroid.activity.Settings;
import net.dahanne.android.regalandroid.remote.RemoteGalleryConnectionFactory;
import net.dahanne.android.regalandroid.utils.ShowUtils;
import net.dahanne.gallery.commons.remote.GalleryConnectionException;
import net.dahanne.gallery.commons.remote.RemoteGallery;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * @author Anthony Dahanne
 * 
 */
@SuppressWarnings("unchecked")
public class CreateAlbumTask extends AsyncTask {
	String exceptionMessage = null;
	Activity activity;
	private String galleryUrl;
	private final ProgressDialog progressDialog;
	private final RemoteGallery remoteGallery;

	public CreateAlbumTask(Activity context, ProgressDialog progressDialog) {
		super();
		remoteGallery = RemoteGalleryConnectionFactory.getInstance();
		activity = context;
		this.progressDialog = progressDialog;
	}

	@Override
	protected Integer doInBackground(Object... parameters) {
		galleryUrl = (String) parameters[0];
		Integer albumName = (Integer) parameters[1];
		String subalbumName = (String) parameters[2];
//		boolean mustLogIn = (Boolean) parameters[3];
		try {
//			if (mustLogIn) {
//				remoteGallery.loginToGallery(galleryUrl,
//						Settings.getUsername(activity),
//						Settings.getPassword(activity));
//			}
			int createdAlbumName = remoteGallery.createNewAlbum(galleryUrl,
					albumName, subalbumName, subalbumName,
					Settings.getDefaultSummary(activity));
//			if (mustLogIn) {
//				remoteGallery.loginToGallery(galleryUrl,
//						Settings.getUsername(activity),
//						Settings.getPassword(activity));
//			}
			// we reload the rootAlbum and its hierarchy
//			((RegalAndroidApplication) activity.getApplication())
//					.setRootAlbum(remoteGallery
//							.retrieveRootAlbumAndItsHierarchy(galleryUrl));
			return createdAlbumName;
		} catch (GalleryConnectionException e) {
			exceptionMessage = e.getMessage();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object createdAlbumName) {

		progressDialog.dismiss();
		if ((Integer) createdAlbumName != null
				&& (Integer) createdAlbumName != 0) {
			ShowUtils.getInstance().toastAlbumSuccessfullyCreated(activity);

		} else if (exceptionMessage != null) {
			// Something went wrong
			ShowUtils.getInstance().alertConnectionProblem(exceptionMessage,
					galleryUrl, activity);
		}

	}
}