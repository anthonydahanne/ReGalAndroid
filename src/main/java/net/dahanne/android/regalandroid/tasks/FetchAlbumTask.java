/**
 * g2android
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
package net.dahanne.android.regalandroid.tasks;

import java.util.Collections;
import java.util.List;

import net.dahanne.android.regalandroid.R;
import net.dahanne.android.regalandroid.RegalAndroidApplication;
import net.dahanne.android.regalandroid.adapters.AlbumAdapter;
import net.dahanne.android.regalandroid.model.Album;
import net.dahanne.android.regalandroid.utils.AlbumComparator;
import net.dahanne.android.regalandroid.utils.GalleryConnectionException;
import net.dahanne.android.regalandroid.utils.RemoteGallery;
import net.dahanne.android.regalandroid.utils.RemoteGalleryConnectionFactory;
import net.dahanne.android.regalandroid.utils.ShowUtils;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

/**
 * @author Anthony Dahanne
 * 
 */
public class FetchAlbumTask extends AsyncTask {
	String exceptionMessage = null;
	ListActivity activity;
	private String galleryUrl;
	private final ProgressDialog progressDialog;
	private final RemoteGallery remoteGallery;

	public FetchAlbumTask(ListActivity context, ProgressDialog progressDialog) {
		super();
		remoteGallery = RemoteGalleryConnectionFactory.getInstance();
		activity = context;
		this.progressDialog = progressDialog;
	}

	@Override
	protected Album doInBackground(Object... parameters) {
		galleryUrl = (String) parameters[0];
		Album freshRootAlbum;
		try {
			freshRootAlbum = remoteGallery
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
			((RegalAndroidApplication) activity.getApplication())
					.setRootAlbum((Album) rootAlbum);
			activity.setTitle(((Album) rootAlbum).getTitle());
			List<Album> albumChildren = ((Album) rootAlbum).getChildren();
			Collections.sort(albumChildren, new AlbumComparator());
			// we create a fake album, it will be used to choose to view the
			// pictures of the album
			Album viewPicturesAlbum = new Album();
			viewPicturesAlbum.setId(0);
			viewPicturesAlbum.setTitle(activity
					.getString(R.string.view_album_pictures));
			viewPicturesAlbum.setName(((Album) rootAlbum).getName());
			if (!albumChildren.contains(viewPicturesAlbum)) {
				albumChildren.add(0, viewPicturesAlbum);
			}

			// ArrayAdapter<Album> arrayAdapter = new ArrayAdapter<Album>(
			// activity, android.R.layout.simple_list_item_1,
			// albumChildren);
			// activity.setListAdapter(arrayAdapter);
			ArrayAdapter<Album> arrayAdapter = new AlbumAdapter(activity,
					R.layout.show_albums_row, albumChildren);

			activity.setListAdapter(arrayAdapter);
			arrayAdapter.notifyDataSetChanged();
		} else {
			// something went wrong
			ShowUtils.getInstance().alertConnectionProblem(exceptionMessage,
					galleryUrl, activity);
		}
		progressDialog.dismiss();
	}

}
