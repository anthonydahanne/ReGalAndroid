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
package net.dahanne.android.g2android.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.dahanne.android.g2android.R;
import net.dahanne.android.g2android.adapters.AlbumAdapterForUpload;
import net.dahanne.android.g2android.model.Album;
import net.dahanne.android.g2android.utils.AlbumComparator;
import net.dahanne.android.g2android.utils.G2DataUtils;
import net.dahanne.android.g2android.utils.GalleryConnectionException;
import net.dahanne.android.g2android.utils.ShowUtils;
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

	public FetchAlbumForUploadTask(Activity context,
			ProgressDialog progressDialog, Spinner spinner, Album currentAlbum) {
		super();
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
			allAlbums = G2DataUtils.getInstance().getAllAlbums(galleryUrl)
					.values();
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
