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

import java.util.List;

import net.dahanne.android.g2android.G2AndroidApplication;
import net.dahanne.android.g2android.R;
import net.dahanne.android.g2android.model.Album;
import net.dahanne.android.g2android.tasks.AddPhotoTask;
import net.dahanne.android.g2android.tasks.CreateAlbumTask;
import net.dahanne.android.g2android.tasks.FetchAlbumTask;
import net.dahanne.android.g2android.utils.G2DataUtils;
import net.dahanne.android.g2android.utils.ShowUtils;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ShowAlbums extends ListActivity implements OnItemClickListener {

	private static final int REQUEST_CREATE_ALBUM = 2;
	private static final int REQUEST_ADD_PHOTO = 1;
	private static final String IMAGE_TYPE = "image/*";
	private static final String G2ANDROID_ALBUM = "g2android.Album";
	private static final String TAG = "ShowAlbums";
	private ProgressDialog progressDialog;
	private boolean mustLogIn;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getListView().setTextFilterEnabled(true);
		getListView().setOnItemClickListener(this);

	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int albumPosition,
			long arg3) {
		Intent intent;
		Album newSelectedAlbum = G2DataUtils.getInstance()
				.findAlbumFromAlbumName(
						((G2AndroidApplication) getApplication())
								.getRootAlbum(),
						((Album) getListAdapter().getItem(albumPosition))
								.getName());
		if (newSelectedAlbum.getName() == ((G2AndroidApplication) getApplication())
				.getAlbumName()
				|| newSelectedAlbum.getChildren().size() == 0) {
			// the user wants to see the pictures
			intent = new Intent(this, ShowGallery.class);
		} else {
			intent = new Intent(this, ShowAlbums.class);
		}
		((G2AndroidApplication) getApplication()).setAlbumName(newSelectedAlbum
				.getName());
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
		if (((G2AndroidApplication) getApplication()).getRootAlbum() == null) {
			// rootAlbum is null ? the app died
			// we recover the context from the database
			mustLogIn = ShowUtils.getInstance()
					.recoverContextFromDatabase(this);
		}
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				// add a new photo
				Uri photoUri = intent.getData();
				if (photoUri != null) {
					progressDialog = ProgressDialog.show(this,
							getString(R.string.please_wait),
							getString(R.string.adding_photo), true);
					new AddPhotoTask(this, progressDialog).execute(Settings
							.getGalleryUrl(this),
							((G2AndroidApplication) getApplication())
									.getAlbumName(), photoUri, mustLogIn);

				}
				break;
			case 2:
				String subalbumName = intent
						.getStringExtra(ChooseSubAlbumName.SUBALBUM_NAME);
				progressDialog = ProgressDialog.show(this,
						getString(R.string.please_wait),
						getString(R.string.creating_new_album), true);

				new CreateAlbumTask(this, progressDialog).execute(Settings
						.getGalleryUrl(this),
						((G2AndroidApplication) getApplication())
								.getAlbumName(), subalbumName);
				break;
			}
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "resuming");
		if (((G2AndroidApplication) getApplication()).getRootAlbum() != null) {

			Log.i(TAG, "rootalbum "
					+ ((G2AndroidApplication) getApplication()).getRootAlbum()
							.toString());
		}
		Log.i(TAG, "albumname "
				+ ((G2AndroidApplication) getApplication()).getAlbumName());
		// we're back in this activity to select a sub album or to see the
		// pictures

		if (((G2AndroidApplication) getApplication()).getRootAlbum() == null) {
			Log.i(TAG, "rootAlbum is null");
			// we recover the context from the database
			mustLogIn = ShowUtils.getInstance()
					.recoverContextFromDatabase(this);
		}
		if (((G2AndroidApplication) getApplication()).getAlbumName() != 0) {
			// we recover the selected album
			Album selectedAlbum = G2DataUtils.getInstance()
					.findAlbumFromAlbumName(
							((G2AndroidApplication) getApplication())
									.getRootAlbum(),
							((G2AndroidApplication) getApplication())
									.getAlbumName());
			// we create a fake album, it will be used to choose to view the
			// pictures of the album
			Album viewPicturesAlbum = new Album();
			viewPicturesAlbum.setId(0);
			viewPicturesAlbum.setTitle(getString(R.string.view_album_pictures));
			viewPicturesAlbum.setName(selectedAlbum.getName());
			List<Album> albumChildren = selectedAlbum.getChildren();
			if (!albumChildren.contains(viewPicturesAlbum)) {
				albumChildren.add(0, viewPicturesAlbum);
			}
			setTitle(selectedAlbum.getTitle());
			setListAdapter(new ArrayAdapter<Album>(this,
					android.R.layout.simple_list_item_1, albumChildren));

			// now the root album is the current album
			// ((G2AndroidApplication) getApplication())
			// .setRootAlbum(selectedAlbum);

		}
		// it's the first time we get into this view, let's find the albums of
		// the gallery
		else {
			progressDialog = ProgressDialog.show(this,
					getString(R.string.please_wait),
					getString(R.string.fetching_gallery_albums), true);

			new FetchAlbumTask(this, progressDialog).execute(Settings
					.getGalleryUrl(this));
		}

		// we have to clear the currentPosition in album as the user is going to
		// choose another album
		((G2AndroidApplication) getApplication()).setCurrentPosition(0);

	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "pausing");
		ShowUtils.getInstance().saveContextToDatabase(this);
		if (((G2AndroidApplication) getApplication()).getRootAlbum() != null) {

			Log.i(TAG, "rootalbum "
					+ ((G2AndroidApplication) getApplication()).getRootAlbum()
							.toString());
		}
		Log.i(TAG, "albumname "
				+ ((G2AndroidApplication) getApplication()).getAlbumName());

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// the user tries to get back to the parent album
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Album currentAlbum = G2DataUtils.getInstance()
					.findAlbumFromAlbumName(
							((G2AndroidApplication) getApplication())
									.getRootAlbum(),
							((G2AndroidApplication) getApplication())
									.getAlbumName());
			((G2AndroidApplication) getApplication()).setAlbumName(currentAlbum
					.getParentName());
			this.finish();
			return true;
		}
		return false;
	}

}
