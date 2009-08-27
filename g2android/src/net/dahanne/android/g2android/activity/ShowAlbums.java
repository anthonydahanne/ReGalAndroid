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

import org.apache.commons.lang.StringUtils;

import net.dahanne.android.g2android.G2AndroidApplication;
import net.dahanne.android.g2android.R;
import net.dahanne.android.g2android.model.Album;
import net.dahanne.android.g2android.utils.AlbumUtils;
import net.dahanne.android.g2android.utils.G2ConnectionUtils;
import net.dahanne.android.g2android.utils.G2ConnectionUtilsMock;
import net.dahanne.android.g2android.utils.GalleryConnectionException;
import net.dahanne.android.g2android.utils.ToastUtils;
import net.dahanne.android.g2android.utils.UriUtils;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
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
import android.widget.AdapterView.OnItemClickListener;

public class ShowAlbums extends ListActivity implements OnItemClickListener {

	private static final String TAG = "ShowAlbums";
	private Integer albumName;
	private List<Album> albumChildren;
	private String galleryHost;
	private String galleryPath;
	private int galleryPort;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		albumName = (Integer) getIntent().getSerializableExtra(
				"g2android.Album");

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
			intent.putExtra("g2android.Album", newSelectedAlbum.getName());
		} else {
			intent = new Intent(this, ShowAlbums.class);
			intent.putExtra("g2android.Album", newSelectedAlbum.getName());
		}
		startActivity(intent);

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
					try {
						File imageFile = UriUtils.createFileFromUri(this,
								photoUri);
						G2ConnectionUtils.sendImageToGallery(Settings
								.getGalleryHost(this), Settings
								.getGalleryPath(this), Settings
								.getGalleryPort(this), albumName, imageFile);
						imageFile.delete();
					} catch (Exception e) {
						ToastUtils.toastGalleryException(this, e);
					}
				}

				break;
			case 2:
				String subalbumName = intent.getStringExtra("subalbumName");
				// create a new subalbum
				try {
					G2ConnectionUtils.createNewAlbum(Settings
							.getGalleryHost(this), Settings
							.getGalleryPath(this), Settings
							.getGalleryPort(this), albumName, subalbumName,
							subalbumName, subalbumName);

					if (albumName != 0) {
						ToastUtils.toastAlbumSuccessfullyCreated(this,
								subalbumName);
					}

					// now refresh the album hierarchy
					Album rootAlbum = AlbumUtils
							.retrieveRootAlbumAndItsHierarchy(this,galleryHost,galleryPath,galleryPort);
					((G2AndroidApplication) getApplication())
							.setRootAlbum(rootAlbum);

				} catch (NumberFormatException e) {
					ToastUtils.toastNumberFormatException(this, e);
				} catch (GalleryConnectionException e) {
					ToastUtils.toastGalleryException(this, e);
				}
				break;
			}
		}

	}

	@Override
	protected void onResume() {
		galleryHost = Settings.getGalleryHost(this);
		galleryPath = Settings.getGalleryPath(this);
		galleryPort = Settings.getGalleryPort(this);
		
		super.onResume();
		// we're back in this activity to select a sub album or to see the
		// pictures
		if (albumName != null) {
			// we recover the selected album
			Album rootAlbum = ((G2AndroidApplication) getApplication())
					.getRootAlbum();
			Album selectedAlbum = AlbumUtils.findAlbumFromAlbumName(rootAlbum,
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

		}
		// it's the first time we get into this view, let's find the albums of
		// the gallery
		else {
			Album rootAlbum = AlbumUtils.retrieveRootAlbumAndItsHierarchy(this,galleryHost,galleryPath,galleryPort);
			((G2AndroidApplication) getApplication()).setRootAlbum(rootAlbum);
			setTitle(rootAlbum.getTitle());
			albumChildren = rootAlbum.getChildren();
			albumName = rootAlbum.getName();
		}

		setListAdapter(new ArrayAdapter<Album>(this,
				android.R.layout.simple_list_item_1, albumChildren));
	}

	private class FetchAlbumTask extends AsyncTask {

		private static final String NOTLOGGEDIN = "NOTLOGGEDIN";
		private static final String GUEST = "guest";
		private Integer galleryPort;
		private String galleryPath;
		private String galleryHost;

		@Override
		protected Album doInBackground(Object... parameters) {
				galleryHost = (String) parameters[0];
				galleryPath = (String) parameters[1];
				galleryPort = (Integer) parameters[2];
				return AlbumUtils.retrieveRootAlbumAndItsHierarchy(ShowAlbums.this,galleryHost,galleryPath,galleryPort);
		}

		@Override
		protected void onPostExecute(Object rootAlbum) {
			progressDialog.dismiss();

		}
	}
	
}
