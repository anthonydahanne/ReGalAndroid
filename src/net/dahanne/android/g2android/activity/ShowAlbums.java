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
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ShowAlbums extends ListActivity implements OnItemClickListener {

	private static final String TAG = "ShowAlbums";
	private List<Album> albums = new ArrayList<Album>();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		HashMap<String, String> albumsProperties = G2Utils.fetchAlbums(Settings
				.getGalleryUrl(this));

		// we load the given album from the previous selection
		Album selectedAlbum = (Album) getIntent().getSerializableExtra(
				"g2android.Album");
		// we're back in this activity to select a sub album or to see the
		// pictures
		if (selectedAlbum != null) {
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

			Map<Integer, Album> nonSortedAlbums = G2Utils
					.extractAlbumFromProperties(albumsProperties);
			Album rootAlbum = G2Utils.organizeAlbumsHierarchy(nonSortedAlbums);
			setTitle(rootAlbum.getTitle());
			albums = rootAlbum.getChildren();
			// albums.add(rootAlbum);
			// for (Album album : rootAlbum.getChildren()) {
			// albums.add(album);
			// if (album.getChildren().size() != 0) {
			// for (Album album2 : album.getChildren()) {
			// albums.add(album2);
			// }
			// }
			// }
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
			// the user want to see the pictures
			intent = new Intent(this, ShowGallery.class);
		} else {
			intent = new Intent(this, ShowAlbums.class);
		}
		intent.putExtra("g2android.Album", newSelectedAlbum);
		startActivity(intent);

	}
}
