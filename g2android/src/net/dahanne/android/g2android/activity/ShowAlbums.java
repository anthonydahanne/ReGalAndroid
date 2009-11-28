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
import java.util.List;

import net.dahanne.android.g2android.G2AndroidApplication;
import net.dahanne.android.g2android.R;
import net.dahanne.android.g2android.model.Album;
import net.dahanne.android.g2android.utils.G2ConnectionUtils;
import net.dahanne.android.g2android.utils.G2DataUtils;
import net.dahanne.android.g2android.utils.GalleryConnectionException;
import net.dahanne.android.g2android.utils.UriUtils;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ShowAlbums extends ListActivity implements OnItemClickListener {

	private static final int REQUEST_CREATE_ALBUM = 2;
	private static final int REQUEST_ADD_PHOTO = 1;
	private static final String IMAGE_TYPE = "image/*";
	private static final String G2ANDROID_ALBUM = "g2android.Album";
	private static final String TAG = "ShowAlbums";
	private Integer albumName;
	private List<Album> albumChildren;
	private String galleryUrl;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		albumName = (Integer) getIntent().getSerializableExtra(G2ANDROID_ALBUM);

		getListView().setTextFilterEnabled(true);
		getListView().setOnItemClickListener(this);

	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int albumPosition,
			long arg3) {
		Intent intent;
		Album newSelectedAlbum = albumChildren.get(albumPosition);
		if (newSelectedAlbum.getId() == 0
				|| newSelectedAlbum.getChildren().size() == 0) {
			// the user wants to see the pictures
			intent = new Intent(this, ShowGallery.class);
			intent.putExtra(G2ANDROID_ALBUM, newSelectedAlbum.getName());
		} else {
			intent = new Intent(this, ShowAlbums.class);
			intent.putExtra(G2ANDROID_ALBUM, newSelectedAlbum.getName());
		}
		startActivity(intent);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.add_photo:
			intent = new Intent(Intent.ACTION_PICK);
			intent.setType(IMAGE_TYPE);
			startActivityForResult(intent, REQUEST_ADD_PHOTO);
			break;

		case R.id.create_album:
			intent = new Intent(this, ChooseSubAlbumName.class);
			startActivityForResult(intent, REQUEST_CREATE_ALBUM);
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
				String subalbumName = intent
						.getStringExtra(ChooseSubAlbumName.SUBALBUM_NAME);
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
	@Override
	protected void onResume() {

		super.onResume();
		galleryUrl = Settings.getGalleryUrl(this);
		// we're back in this activity to select a sub album or to see the
		// pictures
		if (albumName != null) {
			// we recover the selected album
			Album selectedAlbum = G2DataUtils.findAlbumFromAlbumName(
					((G2AndroidApplication) getApplication()).getRootAlbum(),
					albumName);
			// we create a fake album, it will be used to choose to view the
			// pictures of the album
			Album viewPicturesAlbum = new Album();
			viewPicturesAlbum.setId(0);
			viewPicturesAlbum.setTitle(getString(R.string.view_album_pictures));
			viewPicturesAlbum.setName(selectedAlbum.getName());
			albumChildren = selectedAlbum.getChildren();
			if (!albumChildren.contains(viewPicturesAlbum)) {
				albumChildren.add(0, viewPicturesAlbum);
			}
			setTitle(selectedAlbum.getTitle());
			setListAdapter(new ArrayAdapter<Album>(this,
					android.R.layout.simple_list_item_1, albumChildren));

		}
		// it's the first time we get into this view, let's find the albums of
		// the gallery
		else {
			progressDialog = ProgressDialog.show(this,
					getString(R.string.please_wait),
					getString(R.string.fetching_gallery_albums), true);

			new FetchAlbumTask().execute(galleryUrl);
		}

		// we have to clear the currentPosition in album as the user is going to
		// choose another album
		((G2AndroidApplication) getApplication()).setCurrentPosition(0);

	}

	@SuppressWarnings("unchecked")
	private class FetchAlbumTask extends AsyncTask {
		String exceptionMessage = null;

		@Override
		protected Album doInBackground(Object... parameters) {
			galleryUrl = (String) parameters[0];
			Album freshRootAlbum;
			try {
				freshRootAlbum = G2DataUtils
						.retrieveRootAlbumAndItsHierarchy(galleryUrl);
			} catch (GalleryConnectionException e) {
				freshRootAlbum = null;
				exceptionMessage = e.getMessage();
			}
			return freshRootAlbum;
		}

		@Override
		protected void onPostExecute(Object rootAlbum) {

			if (rootAlbum != null) {
				((G2AndroidApplication) getApplication())
						.setRootAlbum((Album) rootAlbum);
				setTitle(((Album) rootAlbum).getTitle());
				albumChildren = ((Album) rootAlbum).getChildren();
				albumName = ((Album) rootAlbum).getName();
				ArrayAdapter<Album> arrayAdapter = new ArrayAdapter<Album>(
						ShowAlbums.this, android.R.layout.simple_list_item_1,
						albumChildren);
				setListAdapter(arrayAdapter);
				arrayAdapter.notifyDataSetChanged();
			} else {
				// something went wrong
				alertConnectionProblem(exceptionMessage, galleryUrl);
			}
			progressDialog.dismiss();
		}

	}

	@SuppressWarnings("unchecked")
	private class CreateAlbumTask extends AsyncTask {
		String exceptionMessage = null;

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
			if ((Integer) createdAlbumName != null
					&& (Integer) createdAlbumName != 0) {
				toastAlbumSuccessfullyCreated(ShowAlbums.this);
				progressDialog = ProgressDialog.show(ShowAlbums.this,
						getString(R.string.please_wait),
						getString(R.string.fetching_gallery_albums), true);
				// now refresh the album hierarchy
				new FetchAlbumTask().execute(galleryUrl);
			} else if (exceptionMessage != null) {
				// Something went wrong
				alertConnectionProblem(exceptionMessage, galleryUrl);
			}

		}
	}

	@SuppressWarnings("unchecked")
	private class AddPhotoTask extends AsyncTask {
		private String exceptionMessage = null;

		@Override
		protected Integer doInBackground(Object... parameters) {
			galleryUrl = (String) parameters[0];
			albumName = (Integer) parameters[1];
			Uri photoUri = (Uri) parameters[2];
			Integer imageCreatedName = null;
			try {
				String mimeType = ShowAlbums.this.getContentResolver().getType(
						photoUri);
				InputStream openInputStream = ShowAlbums.this
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
		protected void onPostExecute(Object createdAlbumName) {

			progressDialog.dismiss();
			if (createdAlbumName != null) {
				toastImageSuccessfullyAdded(ShowAlbums.this);
				progressDialog = ProgressDialog.show(ShowAlbums.this,
						getString(R.string.please_wait),
						getString(R.string.fetching_gallery_albums), true);
				// now refresh the album hierarchy
				new FetchAlbumTask().execute(galleryUrl);
			} else if (exceptionMessage != null) {
				// Something went wrong
				alertConnectionProblem(exceptionMessage, galleryUrl);
			}

		}
	}

	protected void alertConnectionProblem(String exceptionMessage,
			String galleryUrl) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ShowAlbums.this);
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
				Toast.LENGTH_LONG);
	}

	protected void toastImageSuccessfullyAdded(Context context) {
		Toast.makeText(context, getString(R.string.image_successfully_created),
				Toast.LENGTH_LONG);
	}

}
