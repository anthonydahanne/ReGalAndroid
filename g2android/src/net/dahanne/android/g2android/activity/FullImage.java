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
import java.util.List;

import net.dahanne.android.g2android.G2AndroidApplication;
import net.dahanne.android.g2android.R;
import net.dahanne.android.g2android.model.G2Picture;
import net.dahanne.android.g2android.utils.FileHandlingException;
import net.dahanne.android.g2android.utils.FileUtils;
import net.dahanne.android.g2android.utils.GalleryConnectionException;
import net.dahanne.android.g2android.utils.ShowUtils;
import net.dahanne.android.g2android.utils.modified_android_source.AsyncTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 
 * @author Anthony Dahanne
 * 
 */
public class FullImage extends Activity implements OnGestureListener {

	private static final int REQUEST_CODE_CHOOSE_PHOTO_NUMBER = 1;
	private static final String SLASH = "/";
	private static final String FILE = "file://";
	private static final String IMAGE = "image/";
	private static final String IMAGE_JPEG = "image/jpeg";
	private static final String TEXT_PLAIN = "text/plain";
	private static final String MAIN_PHP_G2_ITEM_ID = "/main.php?g2_itemId=";
	private static final String TAG = "FullImage";
	static final String CURRENT_POSITION = "currentPhoto";
	private ImageView imageView;
	private G2Picture g2Picture;
	private String galleryUrl;
	private List<G2Picture> albumPictures;
	private int currentPosition;
	private ProgressDialog progressDialog;
	private GestureDetector gestureScanner;
	private Toast toast;
	private final FileUtils fileUtils = FileUtils.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		gestureScanner = new GestureDetector(this);

		galleryUrl = Settings.getGalleryUrl(this);
		setContentView(R.layout.full_image);
		imageView = (ImageView) findViewById(R.id.image_full);

	}

	@Override
	protected void onResume() {
		super.onResume();
		albumPictures = ((G2AndroidApplication) getApplication()).getPictures();
		if (albumPictures == null || albumPictures.size() == 0) {
			finish();
		} else {
			currentPosition = ((G2AndroidApplication) getApplication())
					.getCurrentPosition();
			loadingPicture();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		((G2AndroidApplication) getApplication())
				.setCurrentPosition(currentPosition);
		albumPictures = ((G2AndroidApplication) getApplication()).getPictures();
		if (albumPictures != null && albumPictures.size() != 0) {
			ShowUtils.getInstance().saveContextToDatabase(this);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		return gestureScanner.onTouchEvent(me);
	}

	@SuppressWarnings("unchecked")
	private void loadingPicture() {
		g2Picture = albumPictures.get(currentPosition);
		int albumName = ((G2AndroidApplication) getApplication())
				.getAlbumName();
		File potentialAlreadyDownloadedFile = new File(
				Settings.getG2AndroidCachePath(this) + albumName + "/",
				g2Picture.getTitle());
		if (potentialAlreadyDownloadedFile.exists()
				&& potentialAlreadyDownloadedFile.length() != 0) {
			imageView.setImageDrawable(Drawable
					.createFromPath(potentialAlreadyDownloadedFile.getPath()));
		}
		// only download the picture IF it has not yet been downloaded
		else {
			String uriString;
			String resizedName = g2Picture.getResizedName();
			// issue #23 : when there is no resized picture, we fetch the
			// original picture
			if (resizedName == null) {
				uriString = Settings.getBaseUrl(FullImage.this)
						+ g2Picture.getName();
			} else {
				uriString = Settings.getBaseUrl(FullImage.this) + resizedName;
			}
			progressDialog = ProgressDialog.show(FullImage.this,
					getString(R.string.please_wait),
					getString(R.string.loading_photo), true);
			new LoadImageTask().execute(uriString, g2Picture);
		}
	}

	@SuppressWarnings("unchecked")
	private class LoadImageTask extends AsyncTask {
		private String exceptionMessage = null;

		@Override
		protected Bitmap doInBackground(Object... urls) {
			String fileUrl = (String) urls[0];
			G2Picture g2Picture = (G2Picture) urls[1];
			Bitmap downloadImage = null;
			try {
				File imageFileOnExternalDirectory = fileUtils
						.getFileFromGallery(FullImage.this, g2Picture
								.getTitle(), g2Picture.getForceExtension(),
								fileUrl, true,
								((G2AndroidApplication) getApplication())
										.getAlbumName());
				downloadImage = BitmapFactory
						.decodeFile(imageFileOnExternalDirectory.getPath());
				g2Picture.setResizedImagePath(imageFileOnExternalDirectory
						.getPath());

			} catch (GalleryConnectionException e) {
				exceptionMessage = e.getMessage();
			} catch (FileHandlingException e) {
				exceptionMessage = e.getMessage();
			}

			return downloadImage;
		}

		@Override
		protected void onPostExecute(Object result) {
			progressDialog.dismiss();
			if (result == null) {
				alertConnectionProblem(exceptionMessage, galleryUrl);
			}
			imageView.setImageDrawable(new BitmapDrawable((Bitmap) result));
		}
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
		Intent intent = new Intent(Intent.ACTION_SEND);
		switch (item.getItemId()) {
		case R.id.download_full_res_image:
			new DownloadImageTask().execute(g2Picture);
			break;
		case R.id.show_image_properties:
			showImageProperties(g2Picture);
			break;
		case R.id.share_image:
			intent.setType(TEXT_PLAIN);
			intent.putExtra(Intent.EXTRA_TEXT, Settings.getGalleryUrl(this)
					+ MAIN_PHP_G2_ITEM_ID + g2Picture.getName());
			startActivity(Intent.createChooser(intent,
					getString(R.string.choose_action)));
			break;
		case R.id.send_image:
			// we first download the full res image
			new DownloadImageTask().execute(g2Picture);
			String filePath = Settings.getG2AndroidPath(this) + SLASH
					+ g2Picture.getTitle();
			String extension = g2Picture.getForceExtension();
			// if no extension is found, let's assume it's a jpeg...
			if (extension == null || extension.equals("jpg")) {
				intent.setType(IMAGE_JPEG);
			} else {
				intent.setType(IMAGE + extension);
			}
			intent.setType(IMAGE + extension);

			intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(FILE + filePath));
			startActivity(Intent.createChooser(intent,
					getString(R.string.choose_action)));
			break;
		case R.id.choose_photo_number:
			intent = new Intent(this, ChoosePhotoNumber.class);
			intent.putExtra(CURRENT_POSITION, currentPosition);
			startActivityForResult(intent, REQUEST_CODE_CHOOSE_PHOTO_NUMBER);
			break;

		case R.id.advanced_control:
			intent = new Intent();
			intent.setAction(android.content.Intent.ACTION_VIEW);
			// imageFilePath is a path to a file located on the sd card
			// such "/sdcard/temp.jpg"
			filePath = Settings.getG2AndroidPath(this) + SLASH
					+ g2Picture.getTitle();
			File file = new File(filePath);
			if (!file.exists()) {
				filePath = Settings.getG2AndroidCachePath(this) + SLASH
						+ g2Picture.getTitle();
				file = new File(filePath);
			}
			intent.setDataAndType(Uri.fromFile(file), "image/*");
			startActivity(intent);

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
				downloadImage = fileUtils.getFileFromGallery(
						FullImage.this,
						g2Picture.getTitle(),
						g2Picture.getForceExtension(),
						Settings.getBaseUrl(FullImage.this)
								+ g2Picture.getName(), false,
						((G2AndroidApplication) getApplication())
								.getAlbumName());
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
		builder.setTitle(R.string.problem)
				.setMessage(message)
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
		StringBuilder message = new StringBuilder()
				.append(getString(R.string.name)).append(" : ")
				.append(picture.getName()).append("\n")
				.append(getString(R.string.title)).append(" : ")
				.append(picture.getTitle()).append("\n")
				.append(getString(R.string.caption)).append(" : ")
				.append(picture.getCaption()).append("\n")
				.append(getString(R.string.hidden)).append(" : ")
				.append(Boolean.toString(picture.isHidden())).append("\n")
				.append(getString(R.string.full_res_filesize)).append(" : ")
				.append(picture.getRawFilesize()).append("\n")
				.append(getString(R.string.full_res_width)).append(" : ")
				.append(picture.getRawWidth()).append("px.\n")
				.append(getString(R.string.full_res_height)).append(" : ")
				.append(picture.getRawHeight()).append("px.\n");

		// NOT USED IN G2...
		// append(
		// "Date of capture (dd/mm/yyyy) : ").append(
		// picture.getCaptureDateDay()).append("/").append(
		// picture.getCaptureDateMonth()).append("/").append(
		// picture.getCaptureDateYear()).append("/").append(
		// "Time of capture : ").append(picture.getCaptureDateHour())
		// .append(":").append(picture.getCaptureDateMinute()).append(":")
		// .append(picture.getCaptureDateSecond()).append(":");

		builder.setTitle(R.string.image_properties)
				.setMessage(message.toString())
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		int newPosition = currentPosition;
		// right scroll we show the next picture
		if (velocityX < 0) {
			newPosition += 1;
		}
		// left scroll we show the previous picture
		else {
			newPosition -= 1;
		}
		String message;
		// we're above the limit
		if (newPosition < 0 || newPosition >= albumPictures.size()) {
			message = getString(R.string.no_more_pictures);
		} else {
			currentPosition = newPosition;
			loadingPicture();
			StringBuilder showingPictureSb = new StringBuilder();
			showingPictureSb.append(getString(R.string.showing_picture));
			int currentPositionToDisplay = currentPosition + 1;
			showingPictureSb.append(currentPositionToDisplay);
			showingPictureSb.append(SLASH);
			showingPictureSb.append(albumPictures.size());
			message = showingPictureSb.toString();
		}
		// make the toast or update it
		if (toast == null) {
			toast = Toast.makeText(FullImage.this, message, Toast.LENGTH_SHORT);
		} else {
			toast.setText(message);
		}
		toast.show();
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_OK);
			((G2AndroidApplication) getApplication())
					.setCurrentPosition(currentPosition);
			this.finish();
			return true;
		}
		return false;
	}

	public void onLongPress(MotionEvent e) {
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return true;
	}

	public boolean onDown(MotionEvent e) {
		return false;
	}

	public void onShowPress(MotionEvent e) {
	}

	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

}
