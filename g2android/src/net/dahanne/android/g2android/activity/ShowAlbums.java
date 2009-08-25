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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dahanne.android.g2android.R;
import net.dahanne.android.g2android.model.Album;
import net.dahanne.android.g2android.utils.G2Utils;
import net.dahanne.android.g2android.utils.GalleryConnectionException;
import net.dahanne.android.g2android.utils.ToastExceptionUtils;

import org.apache.commons.lang.StringUtils;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ShowAlbums extends ListActivity implements OnItemClickListener {

	private static final String TAG = "ShowAlbums";
	private static List<Album> albums = new ArrayList<Album>();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// we load the given album from the previous selection
		Integer albumName = (Integer) getIntent().getSerializableExtra(
				"g2android.Album");

		// we're back in this activity to select a sub album or to see the
		// pictures
		if (albumName != null) {
			// we recover the selected album
			Album selectedAlbum = albums.get(albumName);
			// we create a fake album, it will be used to choose to view the
			// pictures of the album
			Album viewPicturesAlbum = new Album();
			viewPicturesAlbum.setId(0);
			viewPicturesAlbum.setTitle(getString(R.string.view_album_pictures));
			viewPicturesAlbum.setName(selectedAlbum.getName());
			albums = selectedAlbum.getChildren();
			albums.add(0, viewPicturesAlbum);
			setTitle(selectedAlbum.getTitle());

		}
		// it's the first time we get into this view, let's find the albums of
		// the gallery
		else {
			HashMap<String, String> albumsProperties = new HashMap<String, String>(
					0);
			try {
				// the first thing is to login, if an username and password are
				// supplied !
				// This is done once and for all as the session cookie will be
				// stored !
				if (StringUtils.isNotBlank(Settings.getUsername(this))) {
					G2Utils.loginToGallery(Settings.getGalleryHost(this),
							Settings.getGalleryPath(this), Settings
									.getGalleryPort(this), Settings
									.getUsername(this), Settings
									.getPassword(this));
				}

				albumsProperties = G2Utils.fetchAlbums(Settings
						.getGalleryHost(this), Settings.getGalleryPath(this),
						Settings.getGalleryPort(this));
			} catch (NumberFormatException e) {
				ToastExceptionUtils.toastNumberFormatException(this, e);
			} catch (GalleryConnectionException e) {
				ToastExceptionUtils.toastGalleryException(this, e);
			}

			Map<Integer, Album> nonSortedAlbums = G2Utils
					.extractAlbumFromProperties(albumsProperties);
			Album rootAlbum = G2Utils.organizeAlbumsHierarchy(nonSortedAlbums);
			setTitle(rootAlbum.getTitle());
			albums = rootAlbum.getChildren();
		}

		setListAdapter(new ArrayAdapter<Album>(this,
				android.R.layout.simple_list_item_1, albums));
		getListView().setTextFilterEnabled(true);
		getListView().setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// Log.d(TAG, "" + arg2);
		Intent intent;
		Album newSelectedAlbum = albums.get(arg2);
		if (newSelectedAlbum.getId() == 0
				|| newSelectedAlbum.getChildren().size() == 0) {
			// the user want to see the pictures, no need to have all the albums
			// hierarchy
			newSelectedAlbum.getChildren().clear();
			newSelectedAlbum.setParent(null);
			intent = new Intent(this, ShowGallery.class);
			intent.putExtra("g2android.Album", newSelectedAlbum);
		} else {
			intent = new Intent(this, ShowAlbums.class);
			intent.putExtra("g2android.Album", arg2);
		}
		startActivity(intent);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		switch (item.getItemId()) {
		case R.id.add_photo:
			// intent.setClassName("com.android.camera",
			// "com.android.camera.GalleryPicker");
			intent.setType("image/*");
			startActivityForResult(intent, 1);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == RESULT_OK) {
			Uri photoUri = intent.getData();
			Log.d(TAG, "should be adding a photo");
			if (photoUri != null) {

				Log.d(TAG, "photo uri is not blank");
				// do something with the content Uri
				// TODO figure out why this does not work!!
				Log.d(TAG, "the photo URI is " + photoUri.getPath());
				// Drawable thePic =
				// Drawable.createFromPath(photoUri.getPath());
				// // thePic is Null
				// if (thePic != null) {
				// Log.d(TAG, "the pic has loaded");
				// myRecipe.addPic(thePic);
				// ((RecipeAdapter) myListView.getAdapter())
				// .notifyDataSetChanged();
				//
				// }
			}
		}
	}
}
