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

import java.io.File;

import net.dahanne.android.regalandroid.RegalAndroidApplication;
import net.dahanne.android.regalandroid.utils.FileHandlingException;
import net.dahanne.android.regalandroid.utils.FileUtils;
import net.dahanne.android.regalandroid.utils.modified_android_source.AsyncTask;
import net.dahanne.gallery.commons.model.Picture;
import net.dahanne.gallery.commons.remote.GalleryConnectionException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Gallery;
import android.widget.ImageSwitcher;

/**
 * @author Anthony Dahanne
 * 
 */
public class ReplaceMainImageTask extends AsyncTask {
	private ImageSwitcher imageSwitcher = null;
	private int originalPosition;
	private String exceptionMessage = null;
	private final Gallery gallery;

	Activity activity;
	private final ProgressDialog progressDialog;

	public ReplaceMainImageTask(Activity context,
			ProgressDialog progressDialog, Gallery gallery) {
		super();
		activity = context;
		this.progressDialog = progressDialog;
		this.gallery = gallery;
	}

	@Override
	protected Bitmap doInBackground(Object... urls) {
		String fileUrl = (String) urls[0];
		imageSwitcher = (ImageSwitcher) urls[1];
		originalPosition = (Integer) urls[2];
		Picture picture = (Picture) urls[3];
		Bitmap downloadImage = null;
		// make sure the user is watching the picture
		if (originalPosition == gallery.getSelectedItemPosition()) {
			try {
				File imageFileOnExternalDirectory = FileUtils.getInstance()
						.getFileFromGallery(
								activity,
								picture.getFileName(),
								picture.getForceExtension(),
								fileUrl,
								true,
								((RegalAndroidApplication) activity
										.getApplication()).getCurrentAlbum().getName());
				downloadImage = BitmapFactory
						.decodeFile(imageFileOnExternalDirectory.getPath());
				picture.setResizedImageCachePath(imageFileOnExternalDirectory
						.getPath());

			} catch (GalleryConnectionException e) {
				exceptionMessage = e.getMessage();
			} catch (FileHandlingException e) {
				exceptionMessage = e.getMessage();
			}
		}
		return downloadImage;
	}

	@Override
	protected void onPostExecute(Object result) {
		if (result == null) {
			// ShowUtils.getInstance().alertConnectionProblem(exceptionMessage,
			// Settings.getGalleryUrl(activity), activity);
		}
		// we check if the user is still looking at the same photo
		// if not, we don't refresh the main view
		if (result != null
				&& originalPosition == gallery.getSelectedItemPosition()) {
			imageSwitcher.setImageDrawable(new BitmapDrawable((Bitmap) result));
		}
	}
}
