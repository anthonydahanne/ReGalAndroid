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
import net.dahanne.android.regalandroid.remote.RemoteGalleryConnectionFactory;
import net.dahanne.android.regalandroid.utils.AlbumComparator;
import net.dahanne.android.regalandroid.utils.ShowUtils;
import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.remote.GalleryConnectionException;
import net.dahanne.gallery.commons.remote.RemoteGallery;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

/**
 * @author Anthony Dahanne
 * 
 */
public class FetchAlbumTask extends AsyncTask<Object, Void, Album> {
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
		int albumId = (Integer) parameters[1];
		
		Album albumAndSubAlbums;
		try {
			albumAndSubAlbums = remoteGallery
					.getAlbumAndSubAlbumsAndPictures(albumId);
		} catch (GalleryConnectionException e) {
			albumAndSubAlbums = null;
			exceptionMessage = e.getMessage();
		}
		return albumAndSubAlbums;
	}

	@Override
	protected void onPostExecute(Album albumAndSubAlbums) {

		if (albumAndSubAlbums != null) {
			((RegalAndroidApplication) activity.getApplication()).setCurrentAlbum(albumAndSubAlbums);
			activity.setTitle((albumAndSubAlbums).getTitle());
			List<Album> albumChildren = (albumAndSubAlbums).getSubAlbums();
			Collections.sort(albumChildren, new AlbumComparator());
			// we create a fake album, it will be used to choose to view the
			// pictures of the album
			Album viewPicturesAlbum = new Album();
			viewPicturesAlbum.setId(0);
			viewPicturesAlbum.setTitle(activity
					.getString(R.string.view_album_pictures));
			viewPicturesAlbum.setName((albumAndSubAlbums).getName());
			if (!albumChildren.contains(viewPicturesAlbum)) {
				albumChildren.add(0, viewPicturesAlbum);
			}
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
