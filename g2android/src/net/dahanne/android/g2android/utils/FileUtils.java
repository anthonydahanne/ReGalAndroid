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

	public static File getFileFromGallery(Context context, String fileName,
			String extension, String imageUrl, boolean isTemporary)
			throws GalleryConnectionException, FileHandlingException {

		File imageFileOnExternalDirectory = null;
		try {
			InputStream inputStreamFromUrl = null;
			String storage_state = Environment.getExternalStorageState();
			if (storage_state.contains("mounted")) {

				File savePath = new File(Settings
						.getG2AndroidCachePath(context));
				if (!savePath.exists()) {
					File g2AndroidDirectory = new File(Settings
							.getG2AndroidPath(context));
					g2AndroidDirectory.mkdir();
					File g2AndroidCacheDirectory = new File(Settings
							.getG2AndroidCachePath(context));
					g2AndroidCacheDirectory.mkdir();
				}
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
				inputStreamFromUrl = G2ConnectionUtils
						.getInputStreamFromUrl(imageUrl);
			} else {
				throw new FileHandlingException(context
						.getString(R.string.external_storage_problem));
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

	public static void clearCache(Context context) {
		File tempG2AndroidPath = new File(Settings
				.getG2AndroidCachePath(context));
		if (tempG2AndroidPath.exists()) {
			for (File file : tempG2AndroidPath.listFiles()) {
				file.delete();
			}
			tempG2AndroidPath.delete();
		}

	}
}
