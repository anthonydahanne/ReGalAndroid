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

package net.dahanne.android.regalandroid.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.dahanne.android.regalandroid.R;
import net.dahanne.android.regalandroid.activity.Settings;
import net.dahanne.android.regalandroid.remote.RemoteGalleryConnectionFactory;
import net.dahanne.gallery.commons.model.Picture;
import net.dahanne.gallery.commons.remote.GalleryConnectionException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.os.Environment;

public class FileUtils {

	private static final String NO_CACHE_PATH = "/.nomedia";
	private static FileUtils fileUtils = new FileUtils();
	private final Logger logger = LoggerFactory.getLogger(FileUtils.class);

	public static FileUtils getInstance() {
		return fileUtils;
	}

	private FileUtils() {

	}

	/**
	 * download the requested file from the gallery, and save it to cache
	 * 
	 * @param context
	 * @param fileName
	 * @param extension
	 * @param imageUrl
	 * @param isTemporary
	 * @return
	 * @throws GalleryConnectionException
	 * @throws FileHandlingException
	 */
	public File getFileFromGallery(Context context, String fileName,
			String extension, String imageUrl, boolean isTemporary,
			int albumName) throws GalleryConnectionException,
			FileHandlingException {
		logger.debug(
				"gettingFileFromGallery, fileName : {} -- extension : {} -- imageUrl : {} -- isTemporary : {} -- albumName : {}",
				new Object[] { fileName, extension, imageUrl, isTemporary,
						albumName });
		File imageFileOnExternalDirectory = null;
		try {
			imageFileOnExternalDirectory = prepareFileSystemForImage(context, fileName, extension,
				isTemporary, albumName, imageFileOnExternalDirectory);
			InputStream inputStreamFromUrl = null;
			inputStreamFromUrl = RemoteGalleryConnectionFactory.getInstance()
				.getInputStreamFromUrl(imageUrl);
			FileOutputStream fos;
			fos = new FileOutputStream(imageFileOnExternalDirectory);
			byte[] buf = new byte[1024];
			int len;
			while ((len = inputStreamFromUrl.read(buf)) > 0) {
				fos.write(buf, 0, len);
			}
			fos.close();
			inputStreamFromUrl.close();

		} catch (FileNotFoundException e) {
			throw new FileHandlingException(e.getMessage());
		} catch (IOException e) {
			throw new FileHandlingException(e.getMessage());
		}
		return imageFileOnExternalDirectory;
	}

	private synchronized File prepareFileSystemForImage(Context context, String fileName, String extension, boolean isTemporary,
		int albumName, File imageFileOnExternalDirectory) throws IOException, FileHandlingException {
	    String storageState = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(storageState)) {
	    	logger.debug("storage is mounted");
	    	File savePath = new File(
	    			Settings.getReGalAndroidCachePath(context) + "/"
	    					+ albumName);
	    	// if the cache has never been used before
	    	if (!savePath.exists()) {
	    		// we make sure regalandroid path exists (ex : /regalandroid)
	    		File regalAndroidDirectory = new File(
	    				Settings.getReGalAndroidPath(context));
	    		regalAndroidDirectory.mkdir();
	    		// we then create regalandroid cache path (tmp)
	    		File regalAndroidCacheDirectory = new File(
	    				Settings.getReGalAndroidCachePath(context));
	    		regalAndroidCacheDirectory.mkdir();
	    		// and also that the specific album folder exists, bug #65
	    		File albumCacheDirectory = new File(
	    				Settings.getReGalAndroidCachePath(context) + "/"
	    						+ albumName);
	    		albumCacheDirectory.mkdir();

	    		// issue #30 : insert the .nomedia file so that the dir
	    		// won't be parsed by other photo apps
	    		File noMediaFile = new File(
	    				Settings.getReGalAndroidCachePath(context) + "/"
	    						+ albumName + NO_CACHE_PATH);
	    		if (!noMediaFile.createNewFile()) {
	    			throw new FileHandlingException(
	    					context.getString(R.string.external_storage_problem));
	    		}
	    	}
	    	// if the file downloaded is not a cache file, but a file to
	    	// keep
	    	if (!isTemporary) {
	    		savePath = new File(Settings.getReGalAndroidPath(context));
	    		// if there is no file extension, we add the one that
	    		// corresponds to the picture (if we have it)
	    		if (fileName.lastIndexOf(".") == -1
	    				&& !StringUtils.isEmpty(extension)) {
	    			fileName = fileName + "." + extension;
	    		}
	    	}
	    	logger.debug("savePath is : {}", savePath);
	    	//in case the filename has special characters
	    	imageFileOnExternalDirectory = new File(savePath, fileName);
	    } else {
	    	throw new FileHandlingException(
	    			context.getString(R.string.external_storage_problem));
	    }
	    return imageFileOnExternalDirectory;
	}

	public void clearCache(Context context) throws IOException {
		logger.debug("clearingCache");
		File tempReGalAndroidPath = new File(
				Settings.getReGalAndroidCachePath(context));
		org.apache.commons.io.FileUtils.deleteDirectory(tempReGalAndroidPath);

	}

	/**
	 * 
	 * issue #23 : when there is no resized picture, we fetch the original
	 * picture
	 * 
	 * @param picture
	 * @return
	 */
	public String chooseBetweenResizedAndOriginalUrl(Picture picture) {
		logger.debug("picture : {}", picture);
		String resizedName = picture.getResizedUrl();
		if (resizedName == null) {
			resizedName = picture.getFileUrl();
		}
		return resizedName;
	}

}
