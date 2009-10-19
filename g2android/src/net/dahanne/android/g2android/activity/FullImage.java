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

import net.dahanne.android.g2android.R;
import net.dahanne.android.g2android.model.G2Picture;
import net.dahanne.android.g2android.utils.AsyncTask;
import net.dahanne.android.g2android.utils.FileHandlingException;
import net.dahanne.android.g2android.utils.FileUtils;
import net.dahanne.android.g2android.utils.GalleryConnectionException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 
 * @author Anthony Dahanne
 * 
 */
public class FullImage extends Activity {

	private static final String TAG = "FullImage";
	private ImageView imageView;
	private G2Picture g2Picture;
	private String galleryUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		galleryUrl = Settings.getGalleryUrl(this);
		setContentView(R.layout.full_image);
		imageView = (ImageView) findViewById(R.id.image_full);
		g2Picture = (G2Picture) getIntent().getSerializableExtra("g2Picture");
		File potentialAlreadyDownloadedFile = new File(Settings
				.getG2AndroidCachePath(this), g2Picture.getTitle());

		if (potentialAlreadyDownloadedFile == null) {
			// potentialAlreadyDownloadedFile = downloadUrl(urlTyped, g2Picture
			// .getTitle());
		}
		imageView.setImageDrawable(Drawable
				.createFromPath(potentialAlreadyDownloadedFile.getPath()));
		// imageView.setImageBitmap(downloadAndShowImage(urlTyped));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_full_image, menu);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.download_full_res_image:
			new DownloadImageTask().execute(g2Picture);
			break;
		case R.id.show_image_properties:
			showImageProperties(g2Picture);
			break;
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	class DownloadImageTask extends AsyncTask {
		private String exceptionMessage = null;

		@Override
		protected File doInBackground(Object... urls) {
			G2Picture g2Picture = (G2Picture) urls[0];
			File downloadImage = null;
			try {
				downloadImage = FileUtils.getFileFromGallery(FullImage.this,
						g2Picture.getTitle(), g2Picture.getForceExtension(),
						Settings.getBaseUrl(FullImage.this)
								+ g2Picture.getName(), false);
			} catch (GalleryConnectionException e) {
				exceptionMessage = e.getMessage();
			} catch (FileHandlingException e) {
				exceptionMessage = e.getMessage();
			}

			return downloadImage;
		}

		@Override
		protected void onPostExecute(Object result) {
			if (result == null) {
				alertConnectionProblem(exceptionMessage, galleryUrl);
			} else {
				Toast.makeText(FullImage.this,
						getString(R.string.image_successfully_downloaded),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private void alertConnectionProblem(String exceptionMessage,
			String galleryUrl) {
		AlertDialog.Builder builder = new AlertDialog.Builder(FullImage.this);
		// if there was an exception thrown, show it, or say to verify
		// settings
		String message = getString(R.string.not_connected) + galleryUrl
				+ getString(R.string.exception_thrown) + exceptionMessage;
		builder.setTitle(R.string.problem).setMessage(message)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void showImageProperties(G2Picture picture) {
		AlertDialog.Builder builder = new AlertDialog.Builder(FullImage.this);
		StringBuilder message = new StringBuilder().append(
				getString(R.string.name)).append(" : ").append(
				picture.getName()).append("\n").append(
				getString(R.string.title)).append(" : ").append(
				picture.getTitle()).append("\n").append(
				getString(R.string.caption)).append(" : ").append(
				picture.getCaption()).append("\n").append(
				getString(R.string.hidden)).append(" : ").append(
				Boolean.toString(picture.isHidden())).append("\n").append(
				getString(R.string.full_res_filesize)).append(" : ").append(
				picture.getRawFilesize()).append("\n").append(
				getString(R.string.full_res_width)).append(" : ").append(
				picture.getRawWidth()).append("px.\n").append(
				getString(R.string.full_res_height)).append(" : ").append(
				picture.getRawHeight()).append("px.\n");

		// NOT USED IN G2...
		// append(
		// "Date of capture (dd/mm/yyyy) : ").append(
		// picture.getCaptureDateDay()).append("/").append(
		// picture.getCaptureDateMonth()).append("/").append(
		// picture.getCaptureDateYear()).append("/").append(
		// "Time of capture : ").append(picture.getCaptureDateHour())
		// .append(":").append(picture.getCaptureDateMinute()).append(":")
		// .append(picture.getCaptureDateSecond()).append(":");

		builder.setTitle(R.string.image_properties).setMessage(
				message.toString()).setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	// private Bitmap downloadAndShowImage(String urlTyped) {
	// InputStream is = downloadUrl(urlTyped);
	// Bitmap bm = BitmapFactory.decodeStream(is);
	//
	// return bm;
	// }

	// private Drawable imageFileToDrawable(File fileToShow) {
	// Drawable drawable = null;
	// String storage_state = Environment.getExternalStorageState();
	// if (storage_state.contains("mounted")) {
	// File externalStorageDirectory = Environment
	// .getExternalStorageDirectory();
	// File directoryToSaveTheImageTo = new File(externalStorageDirectory,
	// "/g2android");
	// directoryToSaveTheImageTo.mkdir();
	// File imageFileOnExternalDirectory = new File(
	// directoryToSaveTheImageTo, fileToShow.getPath());
	// // FileOutputStream fos = new FileOutputStream(k);
	// // bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
	// drawable = Drawable.createFromPath(imageFileOnExternalDirectory
	// .getPath());
	// }
	// return drawable;
	// // Toast.makeText(this, "Photo saved to " + filePath, Toast.LENGTH_LONG)
	// // .show();
	// }

	// private File downloadUrl(String urlTyped2, String fileName) {
	// HttpURLConnection con = null;
	// URL url;
	// InputStream is = null;
	// int contentLength = 0;
	// File fileFromUrl = null;
	// try {
	// url = new URL(urlTyped2);
	// con = (HttpURLConnection) url.openConnection();
	// con.setReadTimeout(10000 /* milliseconds */);
	// con.setConnectTimeout(15000 /* milliseconds */);
	// con.setRequestMethod("GET");
	// con.setDoInput(true);
	// con.addRequestProperty("Referer", "G2Android");
	// // Start the query
	// con.connect();
	// contentLength = con.getContentLength();
	// is = con.getInputStream();
	// fileFromUrl = File
	// .createTempFile("g2android-", "-tempFileToDelete");
	//
	// FileOutputStream fos = new FileOutputStream(fileFromUrl);
	// byte[] b = new byte[contentLength * 2];
	// while (is.read(b) != -1) {
	// fos.write(b);
	// }
	//
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return fileFromUrl;
	// }

}
