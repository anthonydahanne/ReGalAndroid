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
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class AndroidUriUtils {


//	public static File createFileFromUri(InputStream openInputStream,
//			String mimeType) throws FileNotFoundException, IOException {
//
//		String fileExtension = null;
//		if (mimeType.equals("image/jpeg")) {
//			fileExtension = ".jpg";
//		} else if (mimeType.equals("image/png")) {
//			fileExtension = ".png";
//		} else if (mimeType.equals("image/gif")) {
//			fileExtension = ".gif";
//		} else {
//			fileExtension = ".image";
//		}
//		File imageFile = File.createTempFile("G2AndroidPhoto", fileExtension);
//		OutputStream out = new FileOutputStream(imageFile);
//		byte buf[] = new byte[1024];
//		int len;
//		while ((len = openInputStream.read(buf)) > 0) {
//			out.write(buf, 0, len);
//		}
//		out.close();
//		openInputStream.close();
//		return imageFile;
//	}

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
