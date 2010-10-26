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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dahanne.android.g2android.G2AndroidApplication;
import net.dahanne.android.g2android.R;
import net.dahanne.android.g2android.model.Album;
import net.dahanne.android.g2android.model.G2Picture;
import net.dahanne.android.g2android.tasks.CreateAlbumTask;
import net.dahanne.android.g2android.tasks.ReplaceMainImageTask;
import net.dahanne.android.g2android.utils.FileUtils;
import net.dahanne.android.g2android.utils.G2ConnectionUtils;
import net.dahanne.android.g2android.utils.G2DataUtils;
import net.dahanne.android.g2android.utils.GalleryConnectionException;
import net.dahanne.android.g2android.utils.ShowUtils;
import net.dahanne.android.g2android.utils.modified_android_source.AsyncTask;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

public class ShowGallery extends Activity implements OnItemSelectedListener,
		ViewFactory, OnClickListener {
	private static final int REQUEST_CODE_ADD_PHOTO = 1;
	private static final int REQUEST_CODE_ADD_ALBUM = 2;
	private static final int REQUEST_CODE_FULL_IMAGE = 3;
	private static final String G2ANDROID_ALBUM = "g2android.Album";
	private final List<G2Picture> albumPictures = new ArrayList<G2Picture>();
	private static final String TAG = "ShowGallery";
	private ImageSwitcher mSwitcher;
	private Gallery gallery;
	private ProgressDialog progressDialog;
	private boolean mustLogIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "resuming");

		// we recover the context from the database
		mustLogIn = ShowUtils.getInstance().recoverContextFromDatabase(this);
		// we write the title
		if (getTitle() == null || getTitle().equals("")
				|| getTitle().equals(getString(R.string.show_gallery_title))) {
			Album album = G2DataUtils.getInstance().findAlbumFromAlbumName(
					((G2AndroidApplication) getApplication()).getRootAlbum(),
					((G2AndroidApplication) getApplication()).getAlbumName());
			setTitle(album.getTitle());
		}
		progressDialog = ProgressDialog.show(ShowGallery.this,
				getString(R.string.please_wait),
				getString(R.string.loading_first_photos_from_album), true);
		new FetchImagesTask().execute(Settings.getGalleryUrl(this),
				((G2AndroidApplication) getApplication()).getAlbumName());

	}

	@Override
	protected void onPause() {
		super.onPause();
		ShowUtils.getInstance().saveContextToDatabase(this);

	}

	public class ImageAdapter extends BaseAdapter {
		private static final String THUMB_PREFIX = "thumb_";
		private final Context mContext;
		private final Map<Integer, Bitmap> bitmapsCache = new HashMap<Integer, Bitmap>();

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
			// issue #45 : a photo has been erased, this position does not exist
			// anymore
			if (albumPictures.size() == 0) {
				finish();
			}

			if (position < albumPictures.size()) {
				return albumPictures.get(position).getId();
			} else {
				return 0;
			}

		}

		public View getView(int position, View convertView, ViewGroup parent) {
			G2Picture g2Picture = albumPictures.get(position);
			int albumName = ((G2AndroidApplication) getApplication())
					.getAlbumName();
			File potentiallyAlreadyDownloadedFile = new File(
					Settings.getG2AndroidCachePath(ShowGallery.this)
							+ albumName + "/", THUMB_PREFIX
							+ g2Picture.getTitle());
			// maybe present in the local cache
			Bitmap downloadImage = bitmapsCache.get(position);
			if (downloadImage == null) {
				// only download the picture IF it has not yet been downloaded
				if (g2Picture.getThumbImagePath() != null
						|| potentiallyAlreadyDownloadedFile.exists()
						&& potentiallyAlreadyDownloadedFile.length() != 0) {
					downloadImage = BitmapFactory
							.decodeFile(potentiallyAlreadyDownloadedFile
									.getPath());
				} else {
					String thumbName = g2Picture.getThumbName();
					String uriString = Settings.getBaseUrl(ShowGallery.this)
							+ thumbName;
					try {
						File imageFileOnExternalDirectory = FileUtils
								.getInstance()
								.getFileFromGallery(
										ShowGallery.this,
										THUMB_PREFIX + g2Picture.getTitle(),
										g2Picture.getForceExtension(),
										uriString,
										true,
										((G2AndroidApplication) getApplication())
												.getAlbumName());
						downloadImage = BitmapFactory
								.decodeFile(imageFileOnExternalDirectory
										.getPath());
						g2Picture
								.setThumbImagePath(imageFileOnExternalDirectory
										.getPath());
						bitmapsCache.put(position, downloadImage);
					} catch (Exception e) {
						ShowUtils.getInstance().alertConnectionProblem(
								e.getMessage(),
								Settings.getGalleryUrl(ShowGallery.this),
								ShowGallery.this);
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
		int albumName = ((G2AndroidApplication) getApplication())
				.getAlbumName();
		File potentiallyAlreadyDownloadedFile = new File(
				Settings.getG2AndroidCachePath(this) + albumName + "/",
				g2Picture.getTitle());
		mSwitcher.setId(position);
		// remember the position where we were
		((G2AndroidApplication) getApplication()).setCurrentPosition(position);
		// only download the picture IF it has not yet been downloaded
		if (g2Picture.getResizedImagePath() != null
				&& potentiallyAlreadyDownloadedFile.exists()
				&& potentiallyAlreadyDownloadedFile.length() != 0) {
			Bitmap bitmap = BitmapFactory
					.decodeFile(potentiallyAlreadyDownloadedFile.getPath());
			BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
			mSwitcher.setImageDrawable(bitmapDrawable);
		} else {
			String uriString;
			String resizedName = g2Picture.getResizedName();
			// issue #23 : when there is no resized picture, we fetch the
			// original picture
			if (resizedName == null) {
				uriString = Settings.getBaseUrl(ShowGallery.this)
						+ g2Picture.getName();
			} else {
				uriString = Settings.getBaseUrl(ShowGallery.this) + resizedName;
			}
			Bitmap currentThumbBitmap = (Bitmap) gallery
					.getItemAtPosition(position);
			BitmapDrawable bitmapDrawable = new BitmapDrawable(
					currentThumbBitmap);
			mSwitcher.setImageDrawable(bitmapDrawable);
			new ReplaceMainImageTask(this, progressDialog, gallery).execute(
					uriString, mSwitcher, position, g2Picture);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.add_photo:
			intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");
			startActivityForResult(intent, REQUEST_CODE_ADD_PHOTO);
			break;

		case R.id.create_album:
			intent = new Intent(this, ChooseSubAlbumName.class);
			startActivityForResult(intent, REQUEST_CODE_ADD_ALBUM);
			break;

		case R.id.take_picture:
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, REQUEST_CODE_ADD_PHOTO);

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
		mustLogIn = ShowUtils.getInstance().recoverContextFromDatabase(this);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE_ADD_PHOTO:
				// add a new photo
				Intent intent2 = new Intent(this, UploadPhoto.class);
				intent2.setData(intent.getData());
				if (intent.getExtras() != null) {
					intent2.putExtras(intent.getExtras());
				}
				intent2.setAction(Intent.ACTION_SEND);
				startActivity(intent2);
				break;
			case REQUEST_CODE_ADD_ALBUM:
				String subalbumName = intent.getStringExtra("subalbumName");
				progressDialog = ProgressDialog.show(this,
						getString(R.string.please_wait),
						getString(R.string.creating_new_album), true);

				new CreateAlbumTask(this, progressDialog).execute(Settings
						.getGalleryUrl(this),
						((G2AndroidApplication) getApplication())
								.getAlbumName(), subalbumName, mustLogIn);
				break;

			}
		}

	}

	@SuppressWarnings("unchecked")
	private class FetchImagesTask extends AsyncTask {

		private String exceptionMessage;

		@Override
		protected HashMap<String, String> doInBackground(Object... parameters) {
			String galleryUrl = (String) parameters[0];
			int albumName = (Integer) parameters[1];
			HashMap<String, String> imagesProperties = null;
			try {
				if (mustLogIn) {
					G2ConnectionUtils.getInstance().loginToGallery(galleryUrl,
							Settings.getUsername(ShowGallery.this),
							Settings.getPassword(ShowGallery.this));
					mustLogIn = false;
				}
				imagesProperties = G2ConnectionUtils.getInstance().fetchImages(
						galleryUrl, albumName);
			} catch (GalleryConnectionException e) {
				exceptionMessage = e.getMessage();
				Log.d(TAG, "exception mess" + exceptionMessage);
			}

			return imagesProperties;

		}

		@Override
		protected void onPostExecute(Object imagesProperties) {
			progressDialog.dismiss();
			// }
			if (imagesProperties == null) {
				ShowUtils.getInstance().alertConnectionProblem(
						exceptionMessage,
						Settings.getGalleryUrl(ShowGallery.this),
						ShowGallery.this);
			} else {
				// first check to avoid reloading all the pictures
				// if (albumPictures.isEmpty()) {
				albumPictures.clear();
				albumPictures.addAll(G2DataUtils.getInstance()
						.extractG2PicturesFromProperties(
								(HashMap<String, String>) imagesProperties));
				// }
				if (albumPictures.isEmpty()) {
					setContentView(R.layout.album_is_empty);
				} else {
					// we set up the view
					setContentView(R.layout.show_gallery);
					gallery = (Gallery) findViewById(R.id.gallery);
					mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
					mSwitcher.setOnClickListener(ShowGallery.this);

					mSwitcher.setFactory(ShowGallery.this);
					mSwitcher.setInAnimation(AnimationUtils.loadAnimation(
							ShowGallery.this, android.R.anim.fade_in));
					mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
							ShowGallery.this, android.R.anim.fade_out));

					ImageAdapter adapter = new ImageAdapter(ShowGallery.this);
					gallery.setAdapter(adapter);
					gallery.setOnItemSelectedListener(ShowGallery.this);

					// issue #45 : a photo has been erased, this position does
					// not exist anymore
					if (albumPictures.size() == 0) {
						finish();
					}
					if (((G2AndroidApplication) getApplication())
							.getCurrentPosition() >= albumPictures.size()) {
						((G2AndroidApplication) getApplication())
								.setCurrentPosition(0);
					}
					// recover current position in current album
					int currentPosition = ((G2AndroidApplication) getApplication())
							.getCurrentPosition();
					if (currentPosition != 0) {
						gallery.setSelection(currentPosition);
						G2Picture g2Picture = albumPictures
								.get(currentPosition);
						String resizedName = g2Picture.getResizedName();
						// issue #23 : when there is no resized picture, we
						// fetch the
						// original picture
						String uriString;
						if (resizedName == null) {
							uriString = Settings.getBaseUrl(ShowGallery.this)
									+ g2Picture.getName();
						} else {
							uriString = Settings.getBaseUrl(ShowGallery.this)
									+ resizedName;
						}
						new ReplaceMainImageTask(ShowGallery.this,
								progressDialog, gallery).execute(uriString,
								mSwitcher, currentPosition, g2Picture);
					}

				}
			}
		}
	}

	public void onClick(View v) {
		Intent intent = new Intent(this, FullImage.class);
		((G2AndroidApplication) getApplication()).getPictures().clear();
		((G2AndroidApplication) getApplication()).getPictures().addAll(
				albumPictures);
		((G2AndroidApplication) getApplication()).setCurrentPosition(v.getId());
		startActivityForResult(intent, REQUEST_CODE_FULL_IMAGE);

	}

	/**
	 * this method comes with OnItemSelectedListener interface
	 */
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// the user tries to get back to the parent album
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Album currentAlbum = G2DataUtils
					.getInstance()
					.findAlbumFromAlbumName(
							((G2AndroidApplication) getApplication())
									.getRootAlbum(),
							((G2AndroidApplication) getApplication())
									.getAlbumName());
			if (currentAlbum != null && currentAlbum.getChildren().isEmpty()) {
				((G2AndroidApplication) getApplication())
						.setAlbumName(currentAlbum.getParentName());
			}
			this.finish();
			return true;
		}
		return false;
	}

}
