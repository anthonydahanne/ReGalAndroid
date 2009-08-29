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
import net.dahanne.android.g2android.utils.AlbumUtils;
import net.dahanne.android.g2android.utils.AsyncTask;
import net.dahanne.android.g2android.utils.G2ConnectionUtils;
import net.dahanne.android.g2android.utils.GalleryConnectionException;
import net.dahanne.android.g2android.utils.ToastUtils;
import net.dahanne.android.g2android.utils.UriUtils;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ViewSwitcher.ViewFactory;

public class ShowGallery extends Activity implements OnItemSelectedListener,
		ViewFactory {
	private static final String G2ANDROID_ALBUM = "g2android.Album";
	private List<G2Picture> albumPictures = new ArrayList<G2Picture>();
	private static final String TAG = "ShowGallery";
	private ImageSwitcher mSwitcher;
	private Gallery gallery;
	private int albumName;
	private String galleryHost;
	private String galleryPath;
	private int galleryPort;
	private Dialog progressDialog;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		galleryHost = Settings.getGalleryHost(this);
		galleryPath = Settings.getGalleryPath(this);
		galleryPort = Settings.getGalleryPort(this);
		albumName = (Integer) getIntent().getSerializableExtra(G2ANDROID_ALBUM);
		progressDialog = ProgressDialog.show(ShowGallery.this,
				getString(R.string.please_wait),
				getString(R.string.loading_first_photos_from_album), true);
		Album album = AlbumUtils.findAlbumFromAlbumName(
				((G2AndroidApplication) getApplication()).getRootAlbum(),
				albumName);
		setTitle(album.getTitle());
		new FetchImagesTask().execute(galleryHost, galleryPath, galleryPort,
				albumName);

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
			Bitmap downloadImage;
			ImageView i = new ImageView(mContext);
			if (bitmapsCache.get(position) == null) {
				downloadImage = BitmapFactory
						.decodeStream(getInputStreamFromUrl((Settings
								.getBaseUrl(ShowGallery.this) + albumPictures
								.get(position).getThumbName())));
				bitmapsCache.put(position, downloadImage);
			} else {
				downloadImage = bitmapsCache.get(position);
			}

			i.setImageBitmap(downloadImage);
			return i;
		}
	}

	private InputStream getInputStreamFromUrl(String url) {

		try {
			return G2ConnectionUtils.getInputStreamFromUrl(url);
		} catch (GalleryConnectionException e) {
			ToastUtils.toastGalleryException(this, e);
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		String resizedName = albumPictures.get(position).getResizedName();
		String uriString = Settings.getBaseUrl(ShowGallery.this) + resizedName;
		Bitmap currentThumbBitmap = (Bitmap) gallery
				.getItemAtPosition(position);
		BitmapDrawable bitmapDrawable = new BitmapDrawable(currentThumbBitmap);
		mSwitcher.setImageDrawable(bitmapDrawable);
		new DownloadImageTask().execute(uriString, mSwitcher, position);
	}

	public View makeView() {
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return i;

	}

	public void onNothingSelected(AdapterView<?> parent) {
	}

	private class DownloadImageTask extends AsyncTask {
		private ImageSwitcher imageSwitcher = null;
		private int originalPosition;

		@Override
		protected Bitmap doInBackground(Object... urls) {
			imageSwitcher = (ImageSwitcher) urls[1];
			originalPosition = (Integer) urls[2];
			Bitmap downloadImage = null;
			if (originalPosition == gallery.getSelectedItemPosition()) {
				downloadImage = BitmapFactory
						.decodeStream(getInputStreamFromUrl((String) urls[0]));
				// Log.i(TAG, "Downloading image in background "
				// + (String) urls[0]);
			} else {
				// Log.i(TAG, "Did not download image in background "
				// + (String) urls[0]);
			}
			return downloadImage;
		}

		@Override
		protected void onPostExecute(Object result) {
			if (result == null) {
				return;
			}
			// we check if the user is still looking at the same photo
			// if not, we don't refresh the main view
			if (originalPosition == gallery.getSelectedItemPosition()) {
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
					new AddPhotoTask().execute(galleryHost, galleryPath,
							galleryPort, albumName, photoUri);

				}
				break;
			case 2:
				String subalbumName = intent.getStringExtra("subalbumName");
				progressDialog = ProgressDialog.show(this,
						getString(R.string.please_wait),
						getString(R.string.creating_new_album), true);

				new CreateAlbumTask().execute(galleryHost, galleryPath,
						galleryPort, albumName, subalbumName);
				break;
			}
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@SuppressWarnings("unchecked")
	private class FetchImagesTask extends AsyncTask {

		@Override
		protected HashMap<String, String> doInBackground(Object... parameters) {
			galleryHost = (String) parameters[0];
			galleryPath = (String) parameters[1];
			galleryPort = (Integer) parameters[2];
			albumName = (Integer) parameters[3];
			HashMap<String, String> imagesProperties = new HashMap<String, String>(
					0);
			try {
				imagesProperties = G2ConnectionUtils.fetchImages(galleryHost,
						galleryPath, galleryPort, albumName);
			} catch (GalleryConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return imagesProperties;

		}

		@Override
		protected void onPostExecute(Object imagesProperties) {
			albumPictures
					.addAll(G2ConnectionUtils
							.extractG2PicturesFromProperties((HashMap<String, String>) imagesProperties));
			progressDialog.dismiss();
			if (albumPictures.size() == 0) {
				setContentView(R.layout.album_is_empty);
			} else {

				setContentView(R.layout.imagegallery);
				gallery = (Gallery) findViewById(R.id.gallery);
				mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);

				mSwitcher.setFactory(ShowGallery.this);
				mSwitcher.setInAnimation(AnimationUtils.loadAnimation(
						ShowGallery.this, android.R.anim.fade_in));
				mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
						ShowGallery.this, android.R.anim.fade_out));

				ImageAdapter adapter = new ImageAdapter(ShowGallery.this);
				gallery.setAdapter(adapter);
				gallery.setOnItemSelectedListener(ShowGallery.this);
			}

		}
	}

	@SuppressWarnings("unchecked")
	private class CreateAlbumTask extends AsyncTask {

		@Override
		protected Integer doInBackground(Object... parameters) {
			galleryHost = (String) parameters[0];
			galleryPath = (String) parameters[1];
			galleryPort = (Integer) parameters[2];
			albumName = (Integer) parameters[3];
			String subalbumName = (String) parameters[4];
			try {
				int createdAlbumName = G2ConnectionUtils.createNewAlbum(
						galleryHost, galleryPath, galleryPort, albumName,
						subalbumName, subalbumName, subalbumName);
				return createdAlbumName;
			} catch (GalleryConnectionException e) {
				ToastUtils.toastGalleryException(ShowGallery.this, e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object createdAlbumName) {

			if ((Integer) createdAlbumName != 0) {
				ToastUtils.toastAlbumSuccessfullyCreated(ShowGallery.this);
			}
			progressDialog.dismiss();

		}
	}

	@SuppressWarnings("unchecked")
	private class AddPhotoTask extends AsyncTask {

		@Override
		protected Integer doInBackground(Object... parameters) {
			galleryHost = (String) parameters[0];
			galleryPath = (String) parameters[1];
			galleryPort = (Integer) parameters[2];
			albumName = (Integer) parameters[3];
			Uri photoUri = (Uri) parameters[4];
			Integer imageCreatedName = null;
			try {
				File imageFile = UriUtils.createFileFromUri(ShowGallery.this,
						photoUri);
				imageCreatedName = G2ConnectionUtils.sendImageToGallery(
						galleryHost, galleryPath, galleryPort, albumName,
						imageFile);
				imageFile.delete();
			} catch (Exception e) {
				// TODO : do something
			}
			return imageCreatedName;
		}

		@Override
		protected void onPostExecute(Object createdAlbumName) {

			if (createdAlbumName != null) {
				ToastUtils.toastImageSuccessfullyAdded(ShowGallery.this);
			}
			progressDialog.dismiss();

		}
	}

	//
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	// Intent intent = new Intent(this, FullImage.class);
	// intent.putExtra("g2Picture", albumPictures.get(position) );
	// startActivity(intent );
	//
	// }

}
