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
import java.util.Map;

import net.dahanne.android.regalandroid.utils.FileHandlingException;
import net.dahanne.android.regalandroid.utils.FileUtils;
import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.model.Picture;
import net.dahanne.gallery.commons.remote.GalleryConnectionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * @author Anthony Dahanne
 * 
 */
public class DownloadImageTask extends AsyncTask<Object, Void, Bitmap> {
	Context activity;
	private ImageView view;
	private final Logger logger = LoggerFactory.getLogger(ReplaceMainImageTask.class);

	public DownloadImageTask(Context context, ImageView imageView) {
		super();
		activity = context;
		view = imageView;
	}

	@Override
	protected Bitmap doInBackground(Object... parameters) {
		String fileName = (String) parameters[0];
		String extension = (String) parameters[1];
		String thumbUrl = (String) parameters[2];
		Integer currentAlbumName = (Integer) parameters[3];
		Map<Integer, Bitmap> bitmapsCache = (Map<Integer, Bitmap>) parameters[4];
		Integer position = (Integer) parameters[5];
		Picture picture = (Picture) parameters[6];
		Album album = (Album) parameters[7];
		
		Bitmap downloadImage = null;
		File imageFileOnExternalDirectory = null;
		try {
			imageFileOnExternalDirectory = FileUtils.getInstance().getFileFromGallery(activity, fileName, extension,
					thumbUrl, true, currentAlbumName);
			downloadImage = BitmapFactory.decodeFile(imageFileOnExternalDirectory.getPath());
			if(picture!=null){
				//only for showgallery activity
				picture.setThumbImageCachePath(imageFileOnExternalDirectory.getPath());
				bitmapsCache.put(position, downloadImage);
			} else if(album!=null){
				//only for albumadapter
				album.setAlbumCoverCachePath(imageFileOnExternalDirectory.getPath());
			}
		} catch (GalleryConnectionException e) {
			logger.debug(e.getMessage());
		} catch (FileHandlingException e) {
			logger.debug(e.getMessage());
		}

		return downloadImage;
	}

	@Override
	protected void onPostExecute(Bitmap downloadImage) {

		if (downloadImage != null) {
			view.setImageBitmap(downloadImage);
		}
	}

}
