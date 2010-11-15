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

package net.dahanne.android.regalandroid.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.dahanne.android.regalandroid.R;
import net.dahanne.android.regalandroid.adapters.AlbumAdapterForUpload;
import net.dahanne.android.regalandroid.remote.RemoteGalleryConnectionFactory;
import net.dahanne.android.regalandroid.utils.AlbumComparator;
import net.dahanne.android.regalandroid.utils.ShowUtils;
import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.remote.GalleryConnectionException;
import net.dahanne.gallery.commons.remote.RemoteGallery;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * @author Anthony Dahanne
 * 
 */
@SuppressWarnings("unchecked")
public class FetchAlbumForUploadTask extends AsyncTask {
	String exceptionMessage = null;
	Activity activity;
	private String galleryUrl;
	private final ProgressDialog progressDialog;
	private final Spinner spinner;
	private final Album currentAlbum;
	private final RemoteGallery remoteGallery;

	public FetchAlbumForUploadTask(Activity context,
			ProgressDialog progressDialog, Spinner spinner, Album currentAlbum) {
		super();
		remoteGallery = RemoteGalleryConnectionFactory.getInstance();
		activity = context;
		this.spinner = spinner;
		this.progressDialog = progressDialog;
		this.currentAlbum = currentAlbum;
	}

	@Override
	protected Collection<Album> doInBackground(Object... parameters) {
		galleryUrl = (String) parameters[0];
		Collection<Album> allAlbums;
		try {
			allAlbums = remoteGallery.getAllAlbums(galleryUrl).values();
		} catch (GalleryConnectionException e) {
			allAlbums = null;
			exceptionMessage = e.getMessage();
		}
		return allAlbums;
	}

	@Override
	protected void onPostExecute(Object allAlbums) {

		if (allAlbums != null) {
			List<Album> albumChildren = new ArrayList<Album>();
			albumChildren.addAll((Collection<? extends Album>) allAlbums);
			Collections.sort(albumChildren, new AlbumComparator());
			int position = albumChildren.indexOf(currentAlbum);
			ArrayAdapter<Album> arrayAdapter = new AlbumAdapterForUpload(
					activity, R.layout.show_albums_for_upload_row,
					albumChildren);
			spinner.setAdapter(arrayAdapter);
			if (position != -1) {
				spinner.setSelection(position);
			}
			arrayAdapter.notifyDataSetChanged();
		} else {
			// something went wrong
			ShowUtils.getInstance().alertConnectionProblem(exceptionMessage,
					galleryUrl, activity);
		}
		progressDialog.dismiss();
	}

}
