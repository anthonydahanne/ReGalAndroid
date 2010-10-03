package net.dahanne.android.g2android.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.dahanne.android.g2android.R;
import net.dahanne.android.g2android.activity.Settings;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.os.Environment;

public class FileUtils {

	private static final String NO_CACHE_PATH = "/.nomedia";
	private static FileUtils fileUtils = new FileUtils();

	public static FileUtils getInstance() {
		return fileUtils;
	}

	private FileUtils() {

	}

	private final G2ConnectionUtils g2ConnectionUtils = G2ConnectionUtils
			.getInstance();

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

		File imageFileOnExternalDirectory = null;
		try {
			InputStream inputStreamFromUrl = null;
			String storageState = Environment.getExternalStorageState();
			if (storageState.contains("mounted")) {

				File savePath = new File(
						Settings.getG2AndroidCachePath(context) + "/"
								+ albumName);
				// if the cache has never been used before
				if (!savePath.exists()) {
					// we make sure g2android path exists (/g2android)
					File g2AndroidDirectory = new File(
							Settings.getG2AndroidPath(context));
					g2AndroidDirectory.mkdir();
					// we then create g2android cache path (tmp)
					File g2AndroidCacheDirectory = new File(
							Settings.getG2AndroidCachePath(context));
					g2AndroidCacheDirectory.mkdir();
					// and also that the specific album folder exists, bug #65
					File albumCacheDirectory = new File(
							Settings.getG2AndroidCachePath(context) + "/"
									+ albumName);
					albumCacheDirectory.mkdir();

					// issue #30 : insert the .nomedia file so that the dir
					// won't be parsed by other photo apps
					File noMediaFile = new File(
							Settings.getG2AndroidCachePath(context) + "/"
									+ albumName + NO_CACHE_PATH);
					if (!noMediaFile.createNewFile()) {
						throw new FileHandlingException(
								context.getString(R.string.external_storage_problem));
					}
				}
				// if the file downloaded is not a cache file, but a file to
				// keep
				if (!isTemporary) {
					savePath = new File(Settings.getG2AndroidPath(context));
					// if there is no file extension, we add the one that
					// corresponds to the picture (if we have it)
					if (fileName.lastIndexOf(".") == -1
							&& !StringUtils.isEmpty(extension)) {
						fileName = fileName + "." + extension;
					}
				}

				imageFileOnExternalDirectory = new File(savePath, fileName);
				inputStreamFromUrl = g2ConnectionUtils
						.getInputStreamFromUrl(imageUrl);
			} else {
				throw new FileHandlingException(
						context.getString(R.string.external_storage_problem));
			}
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

	public void clearCache(Context context) {
		File tempG2AndroidPath = new File(
				Settings.getG2AndroidCachePath(context));
		if (tempG2AndroidPath.exists()) {
			for (File file : tempG2AndroidPath.listFiles()) {
				file.delete();
			}
			tempG2AndroidPath.delete();
		}

	}
}
