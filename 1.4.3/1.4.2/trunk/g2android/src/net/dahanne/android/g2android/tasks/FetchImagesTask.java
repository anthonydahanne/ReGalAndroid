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

import java.util.HashMap;
import java.util.List;

import net.dahanne.android.g2android.G2AndroidApplication;
import net.dahanne.android.g2android.R;
import net.dahanne.android.g2android.activity.Settings;
import net.dahanne.android.g2android.model.G2Picture;
import net.dahanne.android.g2android.utils.G2ConnectionUtils;
import net.dahanne.android.g2android.utils.G2DataUtils;
import net.dahanne.android.g2android.utils.GalleryConnectionException;
import net.dahanne.android.g2android.utils.ShowUtils;
import net.dahanne.android.g2android.utils.modified_android_source.AsyncTask;
import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Gallery;
import android.widget.ImageSwitcher;

/**
 * @author Anthony Dahanne
 * 
 */
@SuppressWarnings("unchecked")
public class FetchImagesTask extends AsyncTask {

	private String exceptionMessage;
	Activity activity;
	private Gallery gallery;
	private ProgressDialog progressDialog;
	private ImageSwitcher mSwitcher;
	private List<G2Picture> albumPictures;

	public FetchImagesTask(Activity context, ProgressDialog progressDialog,
			Gallery gallery, ImageSwitcher imageSwitcher,
			List<G2Picture> albumPictures) {
		super();
		this.activity = context;
		this.progressDialog = progressDialog;
		this.gallery = gallery;
		this.mSwitcher = imageSwitcher;
		this.albumPictures = albumPictures;

	}

	@Override
	protected HashMap<String, String> doInBackground(Object... parameters) {
		String galleryUrl = (String) parameters[0];
		int albumName = (Integer) parameters[1];
		boolean mustLogIn = (Boolean) parameters[2];
		HashMap<String, String> imagesProperties = null;
		try {
			if (mustLogIn) {
				G2ConnectionUtils.getInstance().loginToGallery(galleryUrl,
						Settings.getUsername(activity),
						Settings.getPassword(activity));
				mustLogIn = false;
			}
			imagesProperties = G2ConnectionUtils.getInstance().fetchImages(
					galleryUrl, albumName);
		} catch (GalleryConnectionException e) {
			exceptionMessage = e.getMessage();
		}

		return imagesProperties;

	}

	@Override
	protected void onPostExecute(Object imagesProperties) {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		if (imagesProperties == null) {
			ShowUtils.getInstance().alertConnectionProblem(exceptionMessage,
					Settings.getGalleryUrl(activity), activity);
		} else {
			// first check to avoid reloading all the pictures
			if (albumPictures.isEmpty()) {
				albumPictures.addAll(G2DataUtils.getInstance()
						.extractG2PicturesFromProperties(
								(HashMap<String, String>) imagesProperties));
			}
			if (albumPictures.isEmpty()) {
				activity.setContentView(R.layout.album_is_empty);
			} else {
				// we set up the view
				// activity.setContentView(R.layout.show_gallery);
				// gallery = (Gallery) activity.findViewById(R.id.gallery);
				// mSwitcher = (ImageSwitcher) activity
				// .findViewById(R.id.switcher);
				// mSwitcher.setOnClickListener(activity);
				//
				// mSwitcher.setFactory(activity);
				// mSwitcher.setInAnimation(AnimationUtils.loadAnimation(activity,
				// android.R.anim.fade_in));
				// mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
				// activity, android.R.anim.fade_out));
				//
				// ImageAdapter adapter = new ImageAdapter(activity);
				// gallery.setAdapter(adapter);
				// gallery.setOnItemSelectedListener(activity);

				// recover current position in current album
				int currentPosition = ((G2AndroidApplication) activity
						.getApplication()).getCurrentPosition();
				if (currentPosition != 0) {
					gallery.setSelection(currentPosition);
					G2Picture g2Picture = albumPictures.get(currentPosition);
					String resizedName = g2Picture.getResizedName();
					// issue #23 : when there is no resized picture, we
					// fetch the
					// original picture
					String uriString;
					if (resizedName == null) {
						uriString = Settings.getBaseUrl(activity)
								+ g2Picture.getName();
					} else {
						uriString = Settings.getBaseUrl(activity) + resizedName;
					}
					new ReplaceMainImageTask(activity, progressDialog, gallery)
							.execute(uriString, mSwitcher, currentPosition,
									g2Picture);
				}

			}
		}
	}
}