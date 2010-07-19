/**
 * g2android
 * Copyright (c) 2009-2010 Anthony Dahanne
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.dahanne.android.g2android.R;
import net.dahanne.android.g2android.model.Album;
import net.dahanne.android.g2android.tasks.AddPhotoTask;
import net.dahanne.android.g2android.tasks.FetchAlbumTask2;
import net.dahanne.android.g2android.tasks.LoginTask;
import net.dahanne.android.g2android.utils.ShowUtils;
import net.dahanne.android.g2android.utils.UriUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UploadPhoto extends Activity implements OnClickListener {

	private static final String SENT_WITH_G2_ANDROID = "SentWithG2Android";
	private ProgressDialog progressDialog;
	private Button sendButton;
	private Button cancelButton;
	private Button goToG2AndroidButton;
	private TextView galleryUrlText;
	private TextView connectedAsUserText;
	private Uri mImageUri;
	private Spinner albumList;
	private ArrayAdapter<Album> emptyAdapter;
	private EditText filenameEditText;
	private File imageFromCamera;
	private ArrayList<Uri> mImageUris;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		emptyAdapter = new ArrayAdapter<Album>(this, 0);
		;
		setContentView(R.layout.upload_photo);
		setTitle(R.string.upload_photo_title);
		filenameEditText = (EditText) findViewById(R.id.filename);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();

		String fileName = SENT_WITH_G2_ANDROID;

		if (Intent.ACTION_SEND.equals(intent.getAction())) {
			if ((extras != null) || intent.getData() != null) {
				// depending on the source of the intent, the uri can be in
				// extra or data, or can also be a bmp if coming from the camera
				if (extras != null) {
					mImageUri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);
					if (mImageUri == null) {
						Bitmap bm = null;
						Object o = extras.get("data");
						if (o != null && o instanceof Bitmap) {
							bm = (Bitmap) o;
							try {
								StringBuilder stringBuilder = new StringBuilder();
								stringBuilder.append(Settings
										.getG2AndroidPath(this));
								stringBuilder.append("/");
								StringBuilder stringBuilderFileName = new StringBuilder();
								stringBuilderFileName.append(fileName);
								stringBuilderFileName
										.append(System.currentTimeMillis());
								stringBuilderFileName.append(".jpg");
								stringBuilder.append(stringBuilderFileName);
								imageFromCamera = new File(
										stringBuilder.toString());
								FileOutputStream fos = new FileOutputStream(
										imageFromCamera);
								bm.compress(CompressFormat.JPEG, 100, fos);
								fos.flush();
								fos.close();
								
								mImageUri=Uri.fromFile(imageFromCamera);
								fileName = stringBuilderFileName.toString();
								
							} catch (FileNotFoundException e) {
								ShowUtils.getInstance().alertFileProblem(e.getMessage(),this);
							} catch (IOException e) {
								ShowUtils.getInstance().alertFileProblem(e.getMessage(),this);
							}

						}
					}

				} else {
					mImageUri = intent.getData();
				}
			}
			if(fileName.equals(SENT_WITH_G2_ANDROID)){
				fileName = UriUtils.extractFilenameFromUri(mImageUri, this);
			}
			filenameEditText.setText(fileName);
		}
		
		if ("android.intent.action.SEND_MULTIPLE".equals(intent.getAction())) {
			mImageUris =  extras.getParcelableArrayList(Intent.EXTRA_STREAM);
			filenameEditText.setEnabled(false);
			filenameEditText.setText("Multiple files upload");
			
		}
		

		sendButton = (Button) findViewById(R.id.send_button);
		cancelButton = (Button) findViewById(R.id.cancel_button);
		goToG2AndroidButton = (Button) findViewById(R.id.g2android_button);
		sendButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		goToG2AndroidButton.setOnClickListener(this);
		
		

		galleryUrlText = (TextView) findViewById(R.id.gallery_url);
		connectedAsUserText = (TextView) findViewById(R.id.connected_as_user);

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onResume() {
		super.onResume();

		albumList = (Spinner) findViewById(R.id.album_list);
		albumList.setAdapter(emptyAdapter);

		progressDialog = ProgressDialog.show(this,
				getString(R.string.please_wait),
				getString(R.string.connecting_to_the_gallery), true);
		new LoginTask(this, progressDialog, connectedAsUserText,
				galleryUrlText, sendButton).execute(
				Settings.getGalleryUrl(this), Settings.getUsername(this),
				Settings.getPassword(this));

	}

	/**
	 * \ This method is called back from LoginTask
	 */
	@SuppressWarnings("unchecked")
	public void showAlbumList() {
		progressDialog = ProgressDialog.show(this,
				getString(R.string.please_wait),
				getString(R.string.fetching_gallery_albums), true);
		new FetchAlbumTask2(this, progressDialog, albumList).execute(Settings
				.getGalleryUrl(this));
	}

	@SuppressWarnings("unchecked")
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.send_button:

			Album selectAlbum = (Album) albumList.getSelectedItem();

			if (mImageUri != null) {
				boolean mustLogIn = ShowUtils.getInstance()
						.recoverContextFromDatabase(this);
				progressDialog = ProgressDialog.show(this,
						getString(R.string.please_wait),
						getString(R.string.adding_photo), true);
				new AddPhotoTask(this, progressDialog).execute(
						Settings.getGalleryUrl(this),
						Integer.valueOf(selectAlbum.getName()), mImageUri,
						mustLogIn, filenameEditText.getText().toString(),imageFromCamera);
			}
			//multiple file upload
			else if(mImageUris != null){
				boolean mustLogIn = ShowUtils.getInstance()
				.recoverContextFromDatabase(this);
				for (Uri imageUri : mImageUris) {
					progressDialog = ProgressDialog.show(this,
							getString(R.string.please_wait),
							getString(R.string.adding_photo), true);
					new AddPhotoTask(this, progressDialog).execute(
							Settings.getGalleryUrl(this),
							Integer.valueOf(selectAlbum.getName()), imageUri,
							mustLogIn, UriUtils.getFileNameFromUri(imageUri, this),null);
				}
			}
			
			
			else {
				// there is no image to send
				Toast.makeText(
						this,
						R.string.upload_photo_no_photo,
						Toast.LENGTH_LONG);
			}
			break;
		case R.id.cancel_button:
			finish();
			break;
		case R.id.g2android_button:
			startActivity(new Intent(this, Settings.class));
			break;

		}

	}

}
