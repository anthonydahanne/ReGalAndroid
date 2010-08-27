package net.dahanne.android.g2android.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class UriUtils {

	public final static String URL_PATTERN = "^(http|https):\\/\\/(?:\\P{M}\\p{M}*)+([\\-\\.]{1}(?:\\P{M}\\p{M}*)+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";
	public final static String IP_ADDRESS_PATTERN = "^(http|https):\\/\\/\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}(:[0-9]{1,5})?(\\/.*)?$";

	public static File createFileFromUri(InputStream openInputStream,
			String mimeType) throws FileNotFoundException, IOException {

		String fileExtension = null;
		if (mimeType.equals("image/jpeg")) {
			fileExtension = ".jpg";
		} else if (mimeType.equals("image/png")) {
			fileExtension = ".png";
		} else if (mimeType.equals("image/gif")) {
			fileExtension = ".gif";
		} else {
			fileExtension = ".image";
		}
		File imageFile = File.createTempFile("G2AndroidPhoto", fileExtension);
		OutputStream out = new FileOutputStream(imageFile);
		byte buf[] = new byte[1024];
		int len;
		while ((len = openInputStream.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		out.close();
		openInputStream.close();
		return imageFile;
	}

	public static File getFileFromUri(Uri uri, Activity activity) {
		String filePath = null;
		String scheme = uri.getScheme();
		filePath = uri.getPath();
		if (filePath != null && scheme != null && scheme.equals("file")) {
			return new File(filePath);
		}

		String[] projection = { MediaStore.Images.ImageColumns.DATA };
		Cursor c = activity.managedQuery(uri, projection, null, null, null);
		if (c != null && c.moveToFirst()) {
			filePath = c.getString(0);
		}
		if (filePath != null) {
			return new File(filePath);
		}
		return null;

	}

	public static String getFileNameFromUri(Uri uri, Activity activity) {
		String[] projection = { MediaStore.Images.ImageColumns.DISPLAY_NAME };
		String fileName = null;
		Cursor c = activity.managedQuery(uri, projection, null, null, null);
		if (c != null && c.moveToFirst()) {
			fileName = c.getString(0);
		}
		return fileName;

	}

	public static boolean checkUrlIsValid(String url) {
		Pattern p = Pattern.compile(URL_PATTERN);
		Matcher m = p.matcher(url);
		if (!m.matches()) {
			// not an url ? maybe an ip address
			p = Pattern.compile(IP_ADDRESS_PATTERN);
			m = p.matcher(url);
			return m.matches();
		}
		return true;
	}

	// bug #25 : for embedded gallery, should not add main.php
	public static boolean isEmbeddedGallery(String url) {
		if (url.contains("action=gallery")) {
			return true;
		}
		return false;
	}

	public static String extractFilenameFromUri(Uri uri, Activity activity) {

		String fileName = null;
		String scheme = uri.getScheme();
		String path = uri.getPath();
		if (path != null && scheme != null && scheme.equals("file")) {
			fileName = path.substring(path.lastIndexOf("/") + 1);
		}

		String[] projection = { MediaStore.Images.ImageColumns.DISPLAY_NAME /* col1 */};

		Cursor c = activity.managedQuery(uri, projection, null, null, null);
		if (c != null && c.moveToFirst()) {
			fileName = c.getString(0);
		}
		return fileName;
	}

}
