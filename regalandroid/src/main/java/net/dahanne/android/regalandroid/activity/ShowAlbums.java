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
package net.dahanne.android.regalandroid.activity;

import java.util.Collections;
import java.util.List;

import net.dahanne.android.regalandroid.R;
import net.dahanne.android.regalandroid.RegalAndroidApplication;
import net.dahanne.android.regalandroid.adapters.AlbumAdapter;
import net.dahanne.android.regalandroid.remote.RemoteGalleryConnectionFactory;
import net.dahanne.android.regalandroid.tasks.CreateAlbumTask;
import net.dahanne.android.regalandroid.tasks.FetchAlbumTask;
import net.dahanne.android.regalandroid.tasks.LoginTask;
import net.dahanne.android.regalandroid.utils.AlbumComparator;
import net.dahanne.android.regalandroid.utils.ShowUtils;
import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.remote.RemoteGallery;
import net.dahanne.gallery.commons.utils.AlbumUtils;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;

public class ShowAlbums extends ListActivity implements OnItemClickListener {

	private static final int REQUEST_CREATE_ALBUM = 2;
	private static final int REQUEST_ADD_PHOTO = 1;
	private static final String IMAGE_TYPE = "image/*";
	private static final String G2ANDROID_ALBUM = "g2android.Album";
	private static final String TAG = "ShowAlbums";
	private ProgressDialog progressDialog;
	private RegalAndroidApplication application;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.show_albums);

		getListView().setOnItemClickListener(this);

	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int albumPosition,
			long arg3) {
		Intent intent;
		Album newSelectedAlbum = AlbumUtils.findAlbumFromAlbumName(
				application.getCurrentAlbum(),
				((Album) getListAdapter().getItem(albumPosition)).getName());
		if (newSelectedAlbum != null
				&& (newSelectedAlbum.getName() == application.getCurrentAlbum()
						.getName() || newSelectedAlbum.getChildren()
						.size() == 0)) {
			// the user wants to see the pictures
			intent = new Intent(this, ShowGallery.class);
		} else {
			intent = new Intent(this, ShowAlbums.class);
		}
		application
				.setCurrentAlbum(newSelectedAlbum);
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

		case R.id.take_picture:
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, REQUEST_ADD_PHOTO);

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
//		mustLogIn = ShowUtils.getInstance().recoverContextFromDatabase(this);
//		if (((RegalAndroidApplication) getApplication()).getRootAlbum() == null) {
//			// rootAlbum is null ? the app died
//			// we recover the context from the database
//			if (mustLogIn) {
//				new LoginTask(this, progressDialog, null, null, null);
//			}
//		}
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_ADD_PHOTO:
				// add a new photo
				Intent intent2 = new Intent(this, UploadPhoto.class);
				intent2.setData(intent.getData());
				if (intent.getExtras() != null) {
					intent2.putExtras(intent.getExtras());
				}
				intent2.setAction(Intent.ACTION_SEND);
				startActivity(intent2);
				break;
			case REQUEST_CREATE_ALBUM:
				String subalbumName = intent
						.getStringExtra(ChooseSubAlbumName.SUBALBUM_NAME);
				progressDialog = ProgressDialog.show(this,
						getString(R.string.please_wait),
						getString(R.string.creating_new_album), true);

				new CreateAlbumTask(this, progressDialog).execute(Settings
						.getGalleryUrl(this),
						application
								.getCurrentAlbum().getName(), subalbumName);
				break;
			}
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onResume() {
		super.onResume();
		application = (RegalAndroidApplication) getApplication();
		Log.i(TAG, "resuming");
		// we're back in this activity to select a sub album or to see the
		// pictures
		if (((RegalAndroidApplication) getApplication()).getCurrentAlbum() == null) {
			// we recover the context from the database
			ShowUtils.getInstance()
					.recoverContextFromDatabase(this);
		}
		progressDialog = ProgressDialog.show(this,
				getString(R.string.please_wait),
				getString(R.string.fetching_gallery_albums), true);
//		if (((RegalAndroidApplication) getApplication()).getAlbumName() != 0) {
//			// we recover the selected album
//			Album selectedAlbum = remoteGallery
//					.findAlbumFromAlbumName(
//							((RegalAndroidApplication) getApplication())
//									.getCurrentAlbum(),
//							((RegalAndroidApplication) getApplication())
//									.getAlbumName());
//
//			if (selectedAlbum != null) {
//				// we create a fake album, it will be used to choose to view the
//				// pictures of the album
//				Album viewPicturesAlbum = new Album();
//				viewPicturesAlbum.setId(0);
//				viewPicturesAlbum
//						.setTitle(getString(R.string.view_album_pictures));
//				viewPicturesAlbum.setName(selectedAlbum.getName());
//				List<Album> albumChildren = selectedAlbum.getChildren();
//				if (!albumChildren.contains(viewPicturesAlbum)) {
//					albumChildren.add(0, viewPicturesAlbum);
//				}
//				setTitle(selectedAlbum.getTitle());
//				Collections.sort(albumChildren, new AlbumComparator());
//				ListAdapter adapter = new AlbumAdapter(this,
//						R.layout.show_albums_row, albumChildren);
//				setListAdapter(adapter);
//			}
//		}
		if(application.getCurrentAlbum()!=null){
			new FetchAlbumTask(this, progressDialog).execute(Settings
					.getGalleryUrl(this),application.getCurrentAlbum().getName());
		}
		// it's the first time we get into this view, let's find the albums of
		// the gallery
		else{
			new FetchAlbumTask(this, progressDialog).execute(Settings
					.getGalleryUrl(this),0);
		}

		// we have to clear the currentPosition in album as the user is going to
		// choose another album
		application.setCurrentPosition(0);

	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "pausing");
		ShowUtils.getInstance().saveContextToDatabase(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// the user tries to get back to the parent album
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//we are leaving the gallery view, so we want to remember we want to see the parent album
			if(application.getCurrentAlbum().getParent()!=null){
				application.setCurrentAlbum(application.getCurrentAlbum().getParent());
			}
			this.finish();
//			Album currentAlbum = remoteGallery
//					.findAlbumFromAlbumName(
//							application
//									.getRootAlbum(),
//							((RegalAndroidApplication) getApplication())
//									.getAlbumName());
//			// TODO check if it makes sense when the currentAlbum is null
//			if (currentAlbum != null) {
//				((RegalAndroidApplication) getApplication())
//						.setAlbumName(currentAlbum.getParentName());
//			}
			this.finish();
			return true;
		}
		return false;
	}

}
