package net.dahanne.android.g2android.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.net.Uri;

public class UriUtils {

	public static File createFileFromUri(Context context, Uri photoUri)
			throws FileNotFoundException, IOException {
		String mimeType = context.getContentResolver().getType(photoUri);
		InputStream openInputStream = context.getContentResolver()
				.openInputStream(photoUri);
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

}
