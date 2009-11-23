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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dahanne.android.g2android.G2AndroidApplication;
import net.dahanne.android.g2android.R;
import net.dahanne.android.g2android.model.Album;
import net.dahanne.android.g2android.model.G2Picture;
import net.dahanne.android.g2android.utils.AsyncTask;
import net.dahanne.android.g2android.utils.FileHandlingException;
import net.dahanne.android.g2android.utils.FileUtils;
import net.dahanne.android.g2android.utils.G2ConnectionUtils;
import net.dahanne.android.g2android.utils.G2DataUtils;
import net.dahanne.android.g2android.utils.GalleryConnectionException;
import net.dahanne.android.g2android.utils.UriUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ViewSwitcher.ViewFactory;

public class ShowGallery extends Activity implements OnItemSelectedListener,
		ViewFactory, OnClickListener {
	private static final String G2ANDROID_ALBUM = "g2android.Album";
	private List<G2Picture> albumPictures = new ArrayList<G2Picture>();
	private static final String TAG = "ShowGallery";
	private ImageSwitcher mSwitcher;
	private Gallery gallery;
	private int albumName;
	private String galleryUrl;
	private Dialog progressDialog;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		galleryUrl = Settings.getGalleryUrl(this);
		albumName = (Integer) getIntent().getSerializableExtra(G2ANDROID_ALBUM);
		progressDialog = ProgressDialog.show(ShowGallery.this,
				getString(R.string.please_wait),
				getString(R.string.loading_first_photos_from_album), true);
		Album album = G2DataUtils.findAlbumFromAlbumName(
				((G2AndroidApplication) getApplication()).getRootAlbum(),
				albumName);
		setTitle(album.getTitle());
		new FetchImagesTask().execute(galleryUrl, albumName);

	}

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;
		private Map<Integer, Bitmap> bitmapsCache = new HashMap<Integer, Bitmap>();

		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return albumPictures.size();
		}

		public Object getItem(int position) {
			return bitmapsCache.get(position);
		}

		public long getItemId(int position) {
			return albumPictures.get(position).getId();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			Bitmap downloadImage = null;
			ImageView i = new ImageView(mContext);
			if (bitmapsCache.get(position) == null) {
				try {
					downloadImage = BitmapFactory
							.decodeStream(G2ConnectionUtils
									.getInputStreamFromUrl((Settings
											.getBaseUrl(ShowGallery.this) + albumPictures
											.get(position).getThumbName())));
				} catch (GalleryConnectionException e) {
					alertConnectionProblem(e.getMessage(), galleryUrl);
				}
				bitmapsCache.put(position, downloadImage);
			} else {
				downloadImage = bitmapsCache.get(position);
			}

			i.setImageBitmap(downloadImage);
			return i;
		}
	}

	public class ImageAdapter2 extends BaseAdapter {
		private Context mContext;
		private Map<Integer, Bitmap> bitmapsCache = new HashMap<Integer, Bitmap>();

		public ImageAdapter2(Context c) {
			mContext = c;
		}

		public int getCount() {
			return albumPictures.size();
		}

		public Object getItem(int position) {
			return bitmapsCache.get(position);
		}

		public long getItemId(int position) {
			return albumPictures.get(position).getId();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			G2Picture g2Picture = albumPictures.get(position);
			File potentiallyAlreadyDownloadedFile = new File(Settings
					.getG2AndroidCachePath(ShowGallery.this), "thumb_"
					+ g2Picture.getTitle());
			// maybe present in the local cache
			Bitmap downloadImage = bitmapsCache.get(position);
			if (downloadImage == null) {
				// only download the picture IF it has not yet been downloaded
				if (g2Picture.getThumbImagePath() != null
						|| (potentiallyAlreadyDownloadedFile.exists() && potentiallyAlreadyDownloadedFile
								.length() != 0)) {
					downloadImage = BitmapFactory
							.decodeFile(potentiallyAlreadyDownloadedFile
									.getPath());
				} else {
					String thumbName = g2Picture.getThumbName();
					String uriString = Settings.getBaseUrl(ShowGallery.this)
							+ thumbName;
					try {
						File imageFileOnExternalDirectory = FileUtils
								.getFileFromGallery(ShowGallery.this, "thumb_"
										+ g2Picture.getTitle(), g2Picture
										.getForceExtension(), uriString, true);
						downloadImage = BitmapFactory
								.decodeFile(imageFileOnExternalDirectory
										.getPath());
						g2Picture
								.setThumbImagePath(imageFileOnExternalDirectory
										.getPath());
						bitmapsCache.put(position, downloadImage);
					} catch (Exception e) {
						alertConnectionProblem(e.getMessage(), galleryUrl);
					}
				}

			}
			ImageView i = new ImageView(mContext);
			i.setImageBitmap(downloadImage);
			return i;
		}
	}

	@SuppressWarnings("unchecked")
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		G2Picture g2Picture = albumPictures.get(position);
		File potentiallyAlreadyDownloadedFile = new File(Settings
				.getG2AndroidCachePath(this), g2Picture.getTitle());
		mSwitcher.setId(position);
		// only download the picture IF it has not yet been downloaded
		if (g2Picture.getResizedImagePath() != null
				|| (potentiallyAlreadyDownloadedFile.exists() && potentiallyAlreadyDownloadedFile
						.length() != 0)) {
			Bitmap bitmap = BitmapFactory
					.decodeFile(potentiallyAlreadyDownloadedFile.getPath());
			BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
			mSwitcher.setImageDrawable(bitmapDrawable);
		} else {
			String resizedName = g2Picture.getResizedName();
			String uriString = Settings.getBaseUrl(ShowGallery.this)
					+ resizedName;
			Bitmap currentThumbBitmap = (Bitmap) gallery
					.getItemAtPosition(position);
			BitmapDrawable bitmapDrawable = new BitmapDrawable(
					currentThumbBitmap);
			mSwitcher.setImageDrawable(bitmapDrawable);
			new ReplaceMainImageTask().execute(uriString, mSwitcher, position,
					g2Picture);
		}
	}

	public View makeView() {
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return i;

	}

	@SuppressWarnings("unchecked")
	private class ReplaceMainImageTask extends AsyncTask {
		private ImageSwitcher imageSwitcher = null;
		private int originalPosition;
		private String exceptionMessage = null;

		@Override
		protected Bitmap doInBackground(Object... urls) {
			String fileUrl = (String) urls[0];
			imageSwitcher = (ImageSwitcher) urls[1];
			originalPosition = (Integer) urls[2];
			G2Picture g2Picture = (G2Picture) urls[3];
			Bitmap downloadImage = null;
			// make sure the user is watching the picture
			if (originalPosition == gallery.getSelectedItemPosition()) {
				try {
					File imageFileOnExternalDirectory = FileUtils
							.getFileFromGallery(ShowGallery.this, g2Picture
									.getTitle(), g2Picture.getForceExtension(),
									fileUrl, true);
					downloadImage = BitmapFactory
							.decodeFile(imageFileOnExternalDirectory.getPath());
					g2Picture.setResizedImagePath(imageFileOnExternalDirectory
							.getPath());

				} catch (GalleryConnectionException e) {
					exceptionMessage = e.getMessage();
				} catch (FileHandlingException e) {
					exceptionMessage = e.getMessage();
				}
			}
			return downloadImage;
		}

		@Override
		protected void onPostExecute(Object result) {
			if (result == null) {
				alertConnectionProblem(exceptionMessage, galleryUrl);
			}
			// we check if the user is still looking at the same photo
			// if not, we don't refresh the main view
			if (result != null
					&& originalPosition == gallery.getSelectedItemPosition()) {
				imageSwitcher.setImageDrawable(new BitmapDrawable(
						(Bitmap) result));
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.add_photo:
			intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");
			startActivityForResult(intent, 1);
			break;

		case R.id.create_album:
			intent = new Intent(this, ChooseSubAlbumName.class);
			startActivityForResult(intent, 2);
			break;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_show_albums, menu);
		return true;
	}

	/**
	 * we work on the return from the photo picker
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				// add a new photo
				Uri photoUri = intent.getData();
				if (photoUri != null) {
					progressDialog = ProgressDialog.show(this,
							getString(R.string.please_wait),
							getString(R.string.adding_photo), true);
					new AddPhotoTask().execute(galleryUrl, albumName, photoUri);

				}
				break;
			case 2:
				String subalbumName = intent.getStringExtra("subalbumName");
				progressDialog = ProgressDialog.show(this,
						getString(R.string.please_wait),
						getString(R.string.creating_new_album), true);

				new CreateAlbumTask().execute(galleryUrl, albumName,
						subalbumName);
				break;
			}
		}

	}

	@SuppressWarnings("unchecked")
	private class FetchImagesTask extends AsyncTask {

		private String exceptionMessage;

		@Override
		protected HashMap<String, String> doInBackground(Object... parameters) {
			galleryUrl = (String) parameters[0];
			albumName = (Integer) parameters[1];
			HashMap<String, String> imagesProperties = null;
			try {
				imagesProperties = G2ConnectionUtils.fetchImages(galleryUrl,
						albumName);
			} catch (GalleryConnectionException e) {
				exceptionMessage = e.getMessage();
			}
			return imagesProperties;

		}

		@Override
		protected void onPostExecute(Object imagesProperties) {
			progressDialog.dismiss();
			if (imagesProperties == null) {
				alertConnectionProblem(exceptionMessage, galleryUrl);
			} else {

				albumPictures
						.addAll(G2DataUtils
								.extractG2PicturesFromProperties((HashMap<String, String>) imagesProperties));
				if (albumPictures.size() == 0) {
					setContentView(R.layout.album_is_empty);
				} else {
					setContentView(R.layout.show_gallery);
					gallery = (Gallery) findViewById(R.id.gallery);
					mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
					mSwitcher.setOnClickListener(ShowGallery.this);

					mSwitcher.setFactory(ShowGallery.this);
					mSwitcher.setInAnimation(AnimationUtils.loadAnimation(
							ShowGallery.this, android.R.anim.fade_in));
					mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
							ShowGallery.this, android.R.anim.fade_out));

					ImageAdapter2 adapter = new ImageAdapter2(ShowGallery.this);
					gallery.setAdapter(adapter);
					gallery.setOnItemSelectedListener(ShowGallery.this);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private class CreateAlbumTask extends AsyncTask {
		private String exceptionMessage;

		@Override
		protected Integer doInBackground(Object... parameters) {
			galleryUrl = (String) parameters[0];
			albumName = (Integer) parameters[1];
			String subalbumName = (String) parameters[2];
			try {
				int createdAlbumName = G2ConnectionUtils.createNewAlbum(
						galleryUrl, albumName, subalbumName, subalbumName,
						subalbumName);
				return createdAlbumName;
			} catch (GalleryConnectionException e) {
				exceptionMessage = e.getMessage();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object createdAlbumName) {

			progressDialog.dismiss();
			if ((Integer) createdAlbumName != 0) {
				toastAlbumSuccessfullyCreated(ShowGallery.this);
			} else {
				alertConnectionProblem(exceptionMessage, galleryUrl);
			}

		}
	}

	@SuppressWarnings("unchecked")
	private class AddPhotoTask extends AsyncTask {
		private String exceptionMessage;

		@Override
		protected Integer doInBackground(Object... parameters) {
			galleryUrl = (String) parameters[0];
			albumName = (Integer) parameters[1];
			Uri photoUri = (Uri) parameters[2];
			Integer imageCreatedName = null;
			try {
				String mimeType = ShowGallery.this.getContentResolver()
						.getType(photoUri);
				InputStream openInputStream = ShowGallery.this
						.getContentResolver().openInputStream(photoUri);
				File imageFile = UriUtils.createFileFromUri(openInputStream,
						mimeType);
				imageCreatedName = G2ConnectionUtils.sendImageToGallery(
						galleryUrl, albumName, imageFile);
				imageFile.delete();
			} catch (Exception e) {
				exceptionMessage = e.getMessage();
			}
			return imageCreatedName;
		}

		@Override
		protected void onPostExecute(Object createdPhotoName) {

			progressDialog.dismiss();
			if ((Integer) createdPhotoName != 0) {
				toastAlbumSuccessfullyCreated(ShowGallery.this);
			} else {
				alertConnectionProblem(exceptionMessage, galleryUrl);
			}

		}
	}

	protected void alertConnectionProblem(String exceptionMessage,
			String galleryUrl) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ShowGallery.this);
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

	protected void toastAlbumSuccessfullyCreated(Context context) {
		Toast.makeText(context, getString(R.string.album_successfully_created),
				Toast.LENGTH_LONG).show();
	}

	protected void toastImageSuccessfullyAdded(Context context) {
		Toast.makeText(context, getString(R.string.image_successfully_created),
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, FullImage.class);
		intent.putExtra("currentPosition", v.getId());
		((G2AndroidApplication) getApplication()).getPictures().clear();
		((G2AndroidApplication) getApplication()).getPictures().addAll(
				albumPictures);
		startActivity(intent);

	}

	@Override
	/**
	 * this method comes with OnItemSelectedListener interface
	 */
	public void onNothingSelected(AdapterView<?> parent) {
	}

}
