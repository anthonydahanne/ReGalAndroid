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

package net.dahanne.android.regalandroid.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dahanne.android.regalandroid.R;
import net.dahanne.android.regalandroid.RegalAndroidApplication;
import net.dahanne.android.regalandroid.remote.RemoteGalleryConnectionFactory;
import net.dahanne.android.regalandroid.tasks.CreateAlbumTask;
import net.dahanne.android.regalandroid.tasks.DownloadImageTask;
import net.dahanne.android.regalandroid.tasks.ReplaceMainImageTask;
import net.dahanne.android.regalandroid.utils.DBUtils;
import net.dahanne.android.regalandroid.utils.FileUtils;
import net.dahanne.android.regalandroid.utils.ShowUtils;
import net.dahanne.gallery.commons.model.Picture;
import net.dahanne.gallery.commons.remote.GalleryConnectionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
	private final List<Picture> albumPictures = new ArrayList<Picture>();
	private ImageSwitcher mSwitcher;
	private Gallery gallery;
	private ProgressDialog progressDialog;
	private boolean mustLogIn;
	private RegalAndroidApplication application;
	private final Logger logger = LoggerFactory.getLogger(ShowGallery.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		logger.debug("resuming");
		application = (RegalAndroidApplication) getApplication();
		
		if (((RegalAndroidApplication) getApplication()).getCurrentAlbum() == null) {
			// we recover the context from the database
			DBUtils.getInstance()
					.recoverContextFromDatabase(this);
		}
		// we write the title
		if (getTitle() == null || getTitle().equals("")
				|| getTitle().equals(getString(R.string.show_gallery_title))) {
			setTitle(application.getCurrentAlbum().getTitle());
		}
		albumPictures.clear();
		// we already have the pictures
		if (application.getCurrentAlbum() != null
				&& application.getCurrentAlbum().getPictures().size() != 0) {
			albumPictures.clear();
			albumPictures.addAll(application.getCurrentAlbum().getPictures());
			logger.debug("no need to fetch the pictures");
			setUpView();
		} else {
			// we recover the context from the database
			DBUtils.getInstance().recoverContextFromDatabase(this);
			if (application.getCurrentAlbum() != null
					&& application.getCurrentAlbum().getPictures().size() != 0) {
				albumPictures.clear();
				albumPictures.addAll(application.getCurrentAlbum()
						.getPictures());
				logger.debug("pictures loaded from the DB");
				setUpView();
			} else {
				logger.debug("pictures loaded from the gallery");
				progressDialog = ProgressDialog.show(ShowGallery.this,
						getString(R.string.please_wait),
						getString(R.string.loading_first_photos_from_album),
						true);
				if(application.getCurrentAlbum()==null){
					startActivity(new Intent(this, Start.class));
				}
				new FetchImagesTask().execute(application.getCurrentAlbum().getName());
			}
		}
	}

	@Override
	protected void onPause() {
		logger.debug("pausing");
		super.onPause();

		DBUtils.getInstance().saveContextToDatabase(this);

	}

	public class ImageAdapter extends BaseAdapter {
		private static final String THUMB_PREFIX = "thumb_";
		private final Context mContext;
		private final Map<Integer, Bitmap> bitmapsCache = new HashMap<Integer, Bitmap>();

		public ImageAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			return albumPictures.size();
		}

		@Override
		public Object getItem(int position) {
			return bitmapsCache.get(position);
		}

		@Override
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(mContext);
			Bitmap downloadImage = findBitmapWithPosition(position,i);
//			BitmapDrawable bitmapDrawable = new BitmapDrawable(downloadImage);
//			i.setImageDrawable(bitmapDrawable);
			i.setImageBitmap(downloadImage);
			return i;
		}

		private Bitmap findBitmapWithPosition(int position,ImageView view) {
			Picture picture = albumPictures.get(position);
			int albumName = application.getCurrentAlbum().getName();
			File potentiallyAlreadyDownloadedFile = new File(
					Settings.getReGalAndroidCachePath(ShowGallery.this)
							+ albumName + "/", THUMB_PREFIX + picture.getFileName());
			// maybe present in the local cache
			Bitmap downloadImage = bitmapsCache.get(position);
			if (downloadImage == null) {
				// only download the picture IF it has not yet been downloaded
				if (picture.getThumbImageCachePath() != null
						|| potentiallyAlreadyDownloadedFile.exists()
						&& potentiallyAlreadyDownloadedFile.length() != 0) {
					logger.debug("getting picture from cache : {}",
							potentiallyAlreadyDownloadedFile.getPath());
					downloadImage = BitmapFactory
							.decodeFile(potentiallyAlreadyDownloadedFile
									.getPath());
				}
				// not downloaded yet
				else {
					String thumbUrl = picture.getThumbUrl();
					try {
						logger.debug("getting picture from gallery : {}",thumbUrl);
						DownloadImageTask downloadTask = new DownloadImageTask(ShowGallery.this,view);
						downloadTask.execute(THUMB_PREFIX + picture.getFileName(),picture.getForceExtension(), thumbUrl,albumName,bitmapsCache,position,picture,null);

					} catch (Exception e) {
						logger.debug("exception  : {}", e.getStackTrace());
						ShowUtils.getInstance().alertConnectionProblem(e.getMessage(),
								Settings.getGalleryUrl(ShowGallery.this),
								ShowGallery.this);
					}
				}

			}
			if(downloadImage== null){
			    //ok, the thumb is not downloaded yet, let's put the waiting image
			    downloadImage = BitmapFactory.decodeResource(getResources(), R.drawable.loading);
			    bitmapsCache.put(position, downloadImage);
			}
			return downloadImage;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		Picture picture = albumPictures.get(position);
		int albumName = application.getCurrentAlbum().getName();
		File potentiallyAlreadyDownloadedFile = new File(
				Settings.getReGalAndroidCachePath(this) + albumName + "/",
				picture.getFileName());
		logger.debug(
				"selecting an item, position : {} -- picture : {} -- albumName : {} -- potentiallyAlreadyDownloadedFile : {}",
				new Object[] { position, picture, albumName,
						potentiallyAlreadyDownloadedFile.getAbsolutePath() });
		mSwitcher.setId(position);
		// remember the position where we were
		application.setCurrentPosition(position);
		// only download the picture IF it has not yet been downloaded
		if (picture.getResizedImageCachePath() != null
				&& potentiallyAlreadyDownloadedFile.exists()
				&& potentiallyAlreadyDownloadedFile.length() != 0) {
			logger.debug("picture present in the cache, not downloading");
			Bitmap bitmap = BitmapFactory
					.decodeFile(potentiallyAlreadyDownloadedFile.getPath());
			BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
			mSwitcher.setImageDrawable(bitmapDrawable);
		} else {
			String uriString = FileUtils.getInstance()
					.chooseBetweenResizedAndOriginalUrl(picture);
			logger.debug("fetching picture from gallery : {}",uriString);
			Bitmap currentThumbBitmap = (Bitmap) gallery
					.getItemAtPosition(position);
			BitmapDrawable bitmapDrawable = new BitmapDrawable(
					currentThumbBitmap);
			mSwitcher.setImageDrawable(bitmapDrawable);
			new ReplaceMainImageTask(this, gallery).execute(
					uriString, mSwitcher, position, picture);
		}
	}

	@Override
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
		DBUtils.getInstance().recoverContextFromDatabase(this);
		if(application==null){
			application = (RegalAndroidApplication) getApplication();
		}
		int albumName = application.getCurrentAlbum().getName();
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

				new CreateAlbumTask(this, progressDialog).execute(
						Settings.getGalleryUrl(this), albumName, subalbumName,
						mustLogIn);
				break;

			}
		}

	}

	private class FetchImagesTask extends
			AsyncTask<Object, Void, Collection<Picture>> {

		private String exceptionMessage;

		public FetchImagesTask() {
		}

		@Override
		protected Collection<Picture> doInBackground(Object... parameters) {
			int albumName = (Integer) parameters[0];
			Collection<Picture> pictures = null;
			try {
				if (mustLogIn) {
					RemoteGalleryConnectionFactory.getInstance().loginToGallery();
					mustLogIn = false;
				}
				pictures = RemoteGalleryConnectionFactory.getInstance().getPicturesFromAlbum(albumName);
			} catch (GalleryConnectionException e) {
				logger.debug("exception : {}", e.getStackTrace());
			}
			logger.debug("pictures returned : {}",pictures);
			return pictures;

		}

		@Override
		protected void onPostExecute(Collection<Picture> pictures) {
			progressDialog.dismiss();
			if (pictures == null) {
				ShowUtils.getInstance().alertConnectionProblem(
						exceptionMessage,
						Settings.getGalleryUrl(ShowGallery.this),
						ShowGallery.this);
			} else {
				albumPictures.clear();
				albumPictures.addAll(pictures);
				if (albumPictures.isEmpty()) {
					setContentView(R.layout.album_is_empty);
				} else {
					setUpView();

				}
			}
		}

	}

	void setUpView() {
		// we set up the view
		setContentView(R.layout.show_gallery);
		gallery = (Gallery) findViewById(R.id.gallery);
		mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
		mSwitcher.setOnClickListener(ShowGallery.this);

		mSwitcher.setFactory(ShowGallery.this);
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(ShowGallery.this,
				android.R.anim.fade_in));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
				ShowGallery.this, android.R.anim.fade_out));

		ImageAdapter adapter = new ImageAdapter(ShowGallery.this);
		gallery.setAdapter(adapter);
		gallery.setOnItemSelectedListener(ShowGallery.this);

		// issue #45 : a photo has been erased, this position does
		// not exist anymore
		if (application.getCurrentPosition() >= albumPictures.size()) {
			application.setCurrentPosition(0);
		}
		// recover current position in current album
		int currentPosition = ((RegalAndroidApplication) getApplication())
				.getCurrentPosition();
		logger.debug("setting up view currentPosition : {}",currentPosition);
		if (currentPosition != 0) {
			gallery.setSelection(currentPosition);
			Picture picture = albumPictures.get(currentPosition);
			String uriString = FileUtils.getInstance()
					.chooseBetweenResizedAndOriginalUrl(picture);
			new ReplaceMainImageTask(ShowGallery.this, gallery)
					.execute(uriString, mSwitcher, currentPosition, picture);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, FullImage.class);
		((RegalAndroidApplication) getApplication()).getPictures().clear();
		((RegalAndroidApplication) getApplication()).getPictures().addAll(
				albumPictures);
		((RegalAndroidApplication) getApplication()).setCurrentPosition(v
				.getId());
		startActivityForResult(intent, REQUEST_CODE_FULL_IMAGE);

	}

	/**
	 * this method comes with OnItemSelectedListener interface
	 */
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public void onBackPressed() {
		// the user tries to get back to the parent album
		// we are leaving the gallery view, so we want to remember we want
		// to see the parent album
		// unless there are several albums; in this case we want to browse
		// the album
		if (application.getCurrentAlbum().getSubAlbums().size() == 0) {
			application.setCurrentAlbum(application.getCurrentAlbum()
					.getParent());
			logger.debug("leaving activity, new currentAlbum : {}",application.getCurrentAlbum());
		}
		this.finish();
	}

}
