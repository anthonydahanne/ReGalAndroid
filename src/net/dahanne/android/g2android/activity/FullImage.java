/*
 * G2Android
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
package net.dahanne.android.g2android.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.dahanne.android.g2android.R;
import net.dahanne.android.g2android.model.G2Picture;
import net.dahanne.android.g2android.utils.AsyncTask;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * This is NOT currently used
 * 
 * @author Anthony Dahanne
 * 
 */
public class FullImage extends Activity {

	private static final String TAG = "FullImage";
	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.image);
		imageView = (ImageView) findViewById(R.id.image_full);
		String urlTyped = Settings.getBaseUrl(this)
				+ ((G2Picture) getIntent().getSerializableExtra("g2Picture"))
						.getResizedName();
		imageView.setImageBitmap(downloadAndShowImage(urlTyped));

	}

	private Bitmap downloadAndShowImage(String urlTyped) {
		InputStream is = downloadUrl(urlTyped);
		Bitmap bm = BitmapFactory.decodeStream(is);

		return bm;
	}

	private void saveBitmap(Bitmap bm) {
		String storage_state = Environment.getExternalStorageState();
		String filePath = new String();
		if (storage_state.contains("mounted")) {
			try {
				File f = Environment.getExternalStorageDirectory();
				File j = new File(f, "/g2android");
				j.mkdir();
				File k = new File(j, "test");
				filePath = k.getPath();
				FileOutputStream fos = new FileOutputStream(k);
				bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				// Drawable drawable = Drawable.createFromPath(k.getPath());
				// imageView.setImageDrawable(drawable);

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		Toast.makeText(this, "Photo saved to " + filePath, Toast.LENGTH_LONG)
				.show();
	}

	private InputStream downloadUrl(String urlTyped2) {
		HttpURLConnection con = null;
		URL url;
		InputStream is = null;
		try {
			url = new URL(urlTyped2);
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(10000 /* milliseconds */);
			con.setConnectTimeout(15000 /* milliseconds */);
			con.setRequestMethod("GET");
			con.setDoInput(true);
			con.addRequestProperty("Referer", "G2Android");
			// Start the query
			con.connect();
			is = con.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;

	}

	private class DownloadImageTask extends AsyncTask {
		@Override
		protected Object doInBackground(Object... urls) {
			publishProgress(null);
			// Log.i(TAG, urls[0].toString());
			publishProgress(null);
			return new Object();
			// return downloadAndShowImage((String) urls[0]);

		}

		@Override
		protected void onProgressUpdate(Object... values) {
			super.onProgressUpdate(values);
			// Log.i(TAG, "onprogressupdate");

		}

		protected void onPreExecute(Object result) {
			// Log.i(TAG, "preexecute" + result.toString());
		}

		protected void onPostExecute(Bitmap result) {
			// Log.i(TAG, "postexecute" + result.toString());
			imageView.setImageBitmap(result);
		}

	}

}
