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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dahanne.android.g2android.G2AndroidApplication;
import net.dahanne.android.g2android.R;
import net.dahanne.android.g2android.model.Album;
import net.dahanne.android.g2android.utils.AlbumUtils;
import net.dahanne.android.g2android.utils.G2ConnectionUtils;
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
	private Integer albumName;
	private List<Album> albumChildren;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		albumName = (Integer) getIntent().getSerializableExtra(
				"g2android.Album");
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
			HashMap<String, String> albumsProperties = new HashMap<String, String>(
					0);
			try {
				// the first thing is to login, if an username and password are
				// supplied !
				// This is done once and for all as the session cookie will be
				// stored !
				if (StringUtils.isNotBlank(Settings.getUsername(this))) {
					G2ConnectionUtils.loginToGallery(Settings
							.getGalleryHost(this), Settings
							.getGalleryPath(this), Settings
							.getGalleryPort(this), Settings.getUsername(this),
							Settings.getPassword(this));
				}

				albumsProperties = G2ConnectionUtils.fetchAlbums(Settings
						.getGalleryHost(this), Settings.getGalleryPath(this),
						Settings.getGalleryPort(this));
			} catch (NumberFormatException e) {
				ToastExceptionUtils.toastNumberFormatException(this, e);
			} catch (GalleryConnectionException e) {
				ToastExceptionUtils.toastGalleryException(this, e);
			}

			Map<Integer, Album> nonSortedAlbums = G2ConnectionUtils
					.extractAlbumFromProperties(albumsProperties);
			Album rootAlbum = G2ConnectionUtils
					.organizeAlbumsHierarchy(nonSortedAlbums);
			setTitle(rootAlbum.getTitle());
			((G2AndroidApplication) getApplication()).setRootAlbum(rootAlbum);
			albumChildren = rootAlbum.getChildren();
			albumName = rootAlbum.getName();
		}

		setListAdapter(new ArrayAdapter<Album>(this,
				android.R.layout.simple_list_item_1, albumChildren));
		getListView().setTextFilterEnabled(true);
		getListView().setOnItemClickListener(this);

	}

	@Override
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		switch (requestCode) {
		case 1:
			// add a new photo
			if (resultCode == RESULT_OK) {
				Uri photoUri = intent.getData();
				if (photoUri != null) {
					Log.d(TAG, "the photo URI is " + photoUri.getPath());
					try {
						String mimeType = getContentResolver()
								.getType(photoUri);
						System.out.println(mimeType);
						InputStream openInputStream = getContentResolver()
								.openInputStream(photoUri);
						File imageFile = File.createTempFile("G2Android",
								".png");
						// new File("/data/local/tmp/imageToUpload");
						OutputStream out = new FileOutputStream(imageFile);
						byte buf[] = new byte[1024];
						int len;
						while ((len = openInputStream.read(buf)) > 0) {
							out.write(buf, 0, len);
						}
						out.close();
						openInputStream.close();
						G2ConnectionUtils.sendImageToGallery(Settings
								.getGalleryHost(this), Settings
								.getGalleryPath(this), Settings
								.getGalleryPort(this), albumName, imageFile);
					} catch (Exception e) {
						// TODO : handle the exception
						e.printStackTrace();
					}
				}
			}
			break;
		case 2:
			String subalbumName = intent.getStringExtra("subalbumName");
			// create a new subalbum
			try {
				G2ConnectionUtils.createNewAlbum(Settings.getGalleryHost(this),
						Settings.getGalleryPath(this), Settings
								.getGalleryPort(this), albumName, subalbumName,
						subalbumName, subalbumName);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GalleryConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}

	}
}
