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

import net.dahanne.android.g2android.G2AndroidApplication;
import net.dahanne.android.g2android.utils.G2ConnectionUtils;
import net.dahanne.android.g2android.utils.G2DataUtils;
import net.dahanne.android.g2android.utils.GalleryConnectionException;
import net.dahanne.android.g2android.utils.ShowUtils;
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
	private ProgressDialog progressDialog;

	public CreateAlbumTask(Activity context, ProgressDialog progressDialog) {
		super();
		this.activity = context;
		this.progressDialog = progressDialog;
	}

	@Override
	protected Integer doInBackground(Object... parameters) {
		galleryUrl = (String) parameters[0];
		Integer albumName = (Integer) parameters[1];
		String subalbumName = (String) parameters[2];
		try {
			int createdAlbumName = G2ConnectionUtils.getInstance()
					.createNewAlbum(galleryUrl, albumName, subalbumName,
							subalbumName, subalbumName);
			// we reload the rootAlbum and its hierarchy
			((G2AndroidApplication) activity.getApplication())
					.setRootAlbum(G2DataUtils.getInstance()
							.retrieveRootAlbumAndItsHierarchy(galleryUrl));
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
			// we refresh the albums if we are in the ShowAlbums class
			// if (ShowAlbums.class.isInstance(activity)) {
			// progressDialog = ProgressDialog.show(activity, activity
			// .getString(R.string.please_wait), activity
			// .getString(R.string.fetching_gallery_albums), true);
			// // now refresh the album hierarchy
			// new FetchAlbumTask((ListActivity) activity, progressDialog)
			// .execute(galleryUrl);
			// }

		} else if (exceptionMessage != null) {
			// Something went wrong
			ShowUtils.getInstance().alertConnectionProblem(exceptionMessage,
					galleryUrl, activity);
		}

	}
}