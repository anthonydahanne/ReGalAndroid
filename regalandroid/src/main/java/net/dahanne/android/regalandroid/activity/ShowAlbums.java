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

package net.dahanne.android.regalandroid.activity;

import java.util.Collections;
import java.util.List;

import net.dahanne.android.regalandroid.R;
import net.dahanne.android.regalandroid.RegalAndroidApplication;
import net.dahanne.android.regalandroid.adapters.AlbumAdapter;
import net.dahanne.android.regalandroid.tasks.CreateAlbumTask;
import net.dahanne.android.regalandroid.tasks.FetchAlbumAndSubAlbumsAndPicturesTask;
import net.dahanne.android.regalandroid.utils.DBUtils;
import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.model.AlbumComparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class ShowAlbums extends ListActivity implements OnItemClickListener {

	private static final int REQUEST_CREATE_ALBUM = 2;
	private static final int REQUEST_ADD_PHOTO = 1;
	private static final String IMAGE_TYPE = "image/*";
	private ProgressDialog progressDialog;
	private RegalAndroidApplication application;
	private final Logger logger = LoggerFactory.getLogger(ShowAlbums.class);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		logger.debug("onCreating");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.show_albums);

		getListView().setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int albumPosition,
			long arg3) {

		int albumName = ((Album) getListAdapter().getItem(albumPosition)).getName();
		logger.debug("selecting album {}",albumName);
		if(application.getCurrentAlbum()!=null && application.getCurrentAlbum().getName()==albumName){
			//we want to get back to the same album
			//probably because we want to see the pictures this time
			logger.debug("going to ShowGallery");
			this.startActivity(new Intent(this, ShowGallery.class));
			return;
		}
		
		progressDialog = ProgressDialog.show(this,
				getString(R.string.please_wait),
				getString(R.string.fetching_gallery_albums), true);
		boolean refreshView = false;
		logger.debug("getting the album contents");
		new FetchAlbumAndSubAlbumsAndPicturesTask(this, progressDialog,refreshView ).execute(Settings
				.getGalleryUrl(this),albumName);
//		if(application.getCurrentAlbum()!=null && application.getCurrentAlbum().getSubAlbums().size()!=0 ){
//			
//		
////		if (newSelectedAlbum != null
////				&& (newSelectedAlbum.getName() == application.getCurrentAlbum()
////						.getName() || newSelectedAlbum.getSubAlbums()
////						.size() == 0)) {
//			// the user wants to see the pictures
//			intent = new Intent(this, ShowGallery.class);
//		} else {
//			intent = new Intent(this, ShowAlbums.class);
//		}
//		application
//				.setCurrentAlbum(newSelectedAlbum);
//		startActivity(intent);

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
		logger.debug("onResuming");
		// we're back in this activity to select a sub album or to see the
		// pictures
		if (((RegalAndroidApplication) getApplication()).getCurrentAlbum() == null) {
			// we recover the context from the database
			DBUtils.getInstance()
					.recoverContextFromDatabase(this);
		}
		
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
//			new FetchAlbumTask(this, progressDialog).execute(Settings
//					.getGalleryUrl(this),application.getCurrentAlbum().getName());
			logger.debug("currentAlbum not null : {} -- setting up view",application.getCurrentAlbum());
			setUpView();
			
			
		}
		// it's the first time we get into this view, let's find the albums of
		// the gallery
		else{
			logger.debug("firsttime in this view, getting album content");
			progressDialog = ProgressDialog.show(this,
					getString(R.string.please_wait),
					getString(R.string.fetching_gallery_albums), true);
			boolean refreshView = true;
			new FetchAlbumAndSubAlbumsAndPicturesTask(this, progressDialog, refreshView).execute(Settings
					.getGalleryUrl(this),0);
		}

		// we have to clear the currentPosition in album as the user is going to
		// choose another album
		application.setCurrentPosition(0);

	}

	public void setUpView() {
		this.setTitle((application.getCurrentAlbum()).getTitle());
		List<Album> albumChildren = (application.getCurrentAlbum()).getSubAlbums();
		Collections.sort(albumChildren, new AlbumComparator());
		// if there are pictures in this album we create a fake album, it will be used to choose to view the
		// pictures of the album
		if((application.getCurrentAlbum()).getPictures().size()!=0){
			logger.debug("creating fakeAlbum, as there are pictures");
			Album fakeAlbum = new Album();
			fakeAlbum.setId(0);
			fakeAlbum.setTitle(this
					.getString(R.string.view_album_pictures));
			fakeAlbum.setName((application.getCurrentAlbum()).getName());
			//add the pictures to have the right count
			fakeAlbum.getPictures().addAll((application.getCurrentAlbum()).getPictures());
			if (!albumChildren.contains(fakeAlbum)) {
				albumChildren.add(0, fakeAlbum);
			}
		}
		ArrayAdapter<Album> arrayAdapter = new AlbumAdapter(this,
				R.layout.show_albums_row, albumChildren);

		this.setListAdapter(arrayAdapter);
		arrayAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(progressDialog!=null){
			progressDialog.dismiss();
		}
		logger.debug("onPausing");
		DBUtils.getInstance().saveContextToDatabase(this);
	}

	@Override
	public void onBackPressed() {
		//we are leaving the gallery view, so we want to remember we want to see the parent album
		if(application.getCurrentAlbum()!=null && application.getCurrentAlbum().getParent()!=null){
			application.setCurrentAlbum(application.getCurrentAlbum().getParent());
			logger.debug("leaving activity, new currentAlbum : {}",application.getCurrentAlbum());
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
//		this.finish();
	}

}
