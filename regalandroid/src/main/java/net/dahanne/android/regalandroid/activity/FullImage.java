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

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.dahanne.android.regalandroid.R;
import net.dahanne.android.regalandroid.RegalAndroidApplication;
import net.dahanne.android.regalandroid.utils.DBUtils;
import net.dahanne.android.regalandroid.utils.FileHandlingException;
import net.dahanne.android.regalandroid.utils.FileUtils;
import net.dahanne.gallery.commons.model.Picture;
import net.dahanne.gallery.commons.remote.GalleryConnectionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 *
 * @author Anthony Dahanne
 *
 */
public class FullImage extends AppCompatActivity {

	private static final int REQUEST_CODE_CHOOSE_PHOTO_NUMBER = 1;
	private static final String SLASH = "/";
	private static final String FILE = "file://";
	private static final String IMAGE = "image/";
	private static final String IMAGE_JPEG = "image/jpeg";
	private static final String TEXT_PLAIN = "text/plain";
	static final String CURRENT_POSITION = "currentPhoto";
	private TouchImageView imageView;
	private Picture picture;
	private String galleryUrl;
	private List<Picture> albumPictures;
	private int currentPosition;
	private ProgressDialog progressDialog;
	private Toast toast;
	private final FileUtils fileUtils = FileUtils.getInstance();
	private RegalAndroidApplication application;
	private final Logger logger = LoggerFactory.getLogger(FullImage.class);
    private Timer slideshowTimer;
	private Toolbar mToolbar;
	private Animation mSlideUp;
	private Animation mSlideDown;
	private View mDecorView;
	private boolean TBvisible;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		RelativeLayout parent;

		super.onCreate(savedInstanceState);

		// generate relativelayout for touchimageview
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

		// load fullimage.xml
		LayoutInflater inflater = LayoutInflater.from(this);
		parent = (RelativeLayout) inflater.inflate(R.layout.fullimage, null);

		galleryUrl = Settings.getGalleryUrl(this);

		//setup touchimageview and add it to view. position 0 is essential. if added at the end toolbar will not work
		imageView =  new TouchImageView(this);
		imageView.setMaxZoom(4f);
		imageView.setFullImage(this);
		imageView.setLayoutParams(param);
		imageView.setOnClickListener(onTIVClick);
		parent.addView(imageView, 0);

		// setup listener
		mDecorView = getWindow().getDecorView();
		mDecorView.setOnSystemUiVisibilityChangeListener(mOnSystemUiVisibilityChangeListener);

		// setup animations
		mSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
		mSlideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);

		// set view
		hideSystemUI();
		setContentView(parent);

		// add toolbar
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		TBvisible = false;
	}

    @Override
	protected void onResume() {
		super.onResume();
		logger.debug("onResuming");
		application = ((RegalAndroidApplication) getApplication());
		albumPictures = application.getPictures();

		if (albumPictures == null || albumPictures.size() == 0) {
			logger.debug("albumPictures is empty getting out");
			finish();
		} else {
			currentPosition = ((RegalAndroidApplication) getApplication()).getCurrentPosition();
			logger.debug("currentPosition is : {}", currentPosition);
			changingPhoto(currentPosition);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		logger.debug("onPausing");
		application.setCurrentPosition(currentPosition);
		albumPictures = application.getPictures();
		if (albumPictures != null && albumPictures.size() != 0) {
			DBUtils.getInstance().saveContextToDatabase(this);
		}
        stopSlideshow();
    }


	private void loadingPicture() {
		logger.debug("loadingPicture");
		picture = albumPictures.get(currentPosition);
		int albumName = application.getCurrentAlbum().getName();
		File potentialAlreadyDownloadedFile = new File(Settings.getReGalAndroidCachePath(this) + albumName + "/",
				picture.getFileName());
		if (potentialAlreadyDownloadedFile.exists() && potentialAlreadyDownloadedFile.length() != 0) {
			logger.debug("loadingPicture from cache : {}", potentialAlreadyDownloadedFile.getAbsolutePath());
			Bitmap decodeFile = BitmapFactory.decodeFile(potentialAlreadyDownloadedFile.getPath());
			imageView.setImageBitmap(decodeFile);
//			imageView.setImageDrawable(Drawable.createFromPath(potentialAlreadyDownloadedFile.getPath()));
		}
		// only download the picture IF it has not yet been downloaded
		else {
			String uriString = FileUtils.getInstance().chooseBetweenResizedAndOriginalUrl(picture);
			logger.debug("loadingPicture from gallery : {}", uriString);
			progressDialog = ProgressDialog.show(FullImage.this, getString(R.string.please_wait),
					getString(R.string.loading_photo), true);
			new LoadImageTask().execute(uriString, picture);
		}
	}

	private final class LoadImageTask extends AsyncTask<Object, Void, Bitmap> {
		private String exceptionMessage = null;

		@Override
		protected Bitmap doInBackground(Object... urls) {
			String fileUrl = (String) urls[0];
			Picture picture = (Picture) urls[1];
			Bitmap downloadImage = null;
			try {
				File imageFileOnExternalDirectory = fileUtils.getFileFromGallery(FullImage.this, picture.getFileName(),
						picture.getForceExtension(), fileUrl, true, application.getCurrentAlbum().getName());
				downloadImage = BitmapFactory.decodeFile(imageFileOnExternalDirectory.getPath());
				picture.setResizedImageCachePath(imageFileOnExternalDirectory.getPath());

			} catch (GalleryConnectionException e) {
				exceptionMessage = e.getMessage();
			} catch (FileHandlingException e) {
				exceptionMessage = e.getMessage();
			}

			return downloadImage;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			progressDialog.dismiss();
			if (result == null) {
				alertConnectionProblem(exceptionMessage, galleryUrl);
			}
			imageView.setImageBitmap(result);
//			imageView.setImageDrawable(new BitmapDrawable(result));
		}
	}

	private final class DownloadImageTask extends AsyncTask<Object, Void, File> {
		private String exceptionMessage = null;

		@Override
		protected File doInBackground(Object... urls) {
            Picture picture = (Picture) urls[0];
            File downloadImage = null;
            try {
                downloadImage = fileUtils.getFileFromGallery(FullImage.this, picture.getFileName(),
                        picture.getForceExtension(), picture.getFileUrl(), false,
                        application.getCurrentAlbum().getName());
            } catch (GalleryConnectionException e) {
                exceptionMessage = e.getMessage();
            } catch (FileHandlingException e) {
                exceptionMessage = e.getMessage();
            }

			return downloadImage;
		}

		@Override
		protected void onPostExecute(File result) {
			if (result == null) {
				alertConnectionProblem(exceptionMessage, galleryUrl);
			} else {
				Toast.makeText(FullImage.this, getString(R.string.image_successfully_downloaded), Toast.LENGTH_LONG)
						.show();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_full_image, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String filePath = null;
        switch (item.getItemId()) {
            case R.id.start_slideshow:
                slideshowTimer = new Timer();
                slideshowTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        FullImage.this.runOnUiThread(new TimerTask() {
                            @Override
                            public void run() {
                                if(currentPosition+1 < albumPictures.size() ) {
                                    moveRight();
                                } else {
                                    slideshowTimer.cancel();
                                    slideshowTimer = null;
                                }
                            }
                        });
                    }
                }, 1000, Integer.parseInt(Settings.getSlideshowInterval(getApplicationContext())) * 1000);
                break;
            case R.id.download_full_res_image:
                new DownloadImageTask().execute(picture);
                break;
            case R.id.show_image_properties:
                showImageProperties(picture);
                break;
            case R.id.share_image:
                intent.setType(TEXT_PLAIN);
                intent.putExtra(Intent.EXTRA_TEXT, picture.getPublicUrl());
                startActivity(Intent.createChooser(intent, getString(R.string.choose_action)));
                break;
            case R.id.send_image:
                logger.debug("about to send the image");
                // we first download the full res image
                String extension = picture.getForceExtension();
                // if no extension is found, let's assume it's a jpeg...
                if (extension == null || extension.equals("jpg")) {
                    intent.setType(IMAGE_JPEG);
                } else {
                    intent.setType(IMAGE + extension);
                }
                filePath = picture.getResizedImageCachePath();
                // if the resized picture does not exist, we can download the full
                // size
                if (filePath == null || filePath.equals("")) {
                    new DownloadImageTask().execute(picture);
                    filePath = Settings.getReGalAndroidPath(this) + SLASH + picture.getFileUrl();
                }
                logger.debug("The image about to be sent is : {}", filePath);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(FILE + filePath));
                startActivity(Intent.createChooser(intent, getString(R.string.choose_action)));
                break;
            case R.id.choose_photo_number:
                intent = new Intent(this, ChoosePhotoNumber.class);
                intent.putExtra(CURRENT_POSITION, currentPosition);
                startActivityForResult(intent, REQUEST_CODE_CHOOSE_PHOTO_NUMBER);
                break;

            case R.id.advanced_control:
                intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                // imageFilePath is a path to a file located on the sd card
                // such "/sdcard/temp.jpg"
                filePath = Settings.getReGalAndroidPath(this) + SLASH + picture.getFileName();
                File file = new File(filePath);
                if (!file.exists()) {
					int albumName = application.getCurrentAlbum().getName();
                    filePath = Settings.getReGalAndroidCachePath(this) + albumName + SLASH + picture.getFileName();
                    file = new File(filePath);
                }
                intent.setDataAndType(Uri.fromFile(file), "image/*");
                startActivity(intent);

                break;
        }

        return false;
    }

	private void alertConnectionProblem(String exceptionMessage, String galleryUrl) {
		AlertDialog.Builder builder = new AlertDialog.Builder(FullImage.this);
		// if there was an exception thrown, show it, or say to verify
		// settings
		String message = getString(R.string.not_connected) + galleryUrl
                + getString(R.string.exception_thrown) + exceptionMessage;
		builder.setTitle(R.string.problem).setMessage(message)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void showImageProperties(Picture picture) {
		AlertDialog.Builder builder = new AlertDialog.Builder(FullImage.this);
		StringBuilder message = new StringBuilder().append(getString(R.string.name)).append(" : ")
				.append(picture.getFileName()).append("\n").append(getString(R.string.title)).append(" : ")
				.append(picture.getTitle()).append("\n").append(getString(R.string.full_res_filesize)).append(" : ")
				.append(picture.getFileSize()).append("\n").append(getString(R.string.full_res_width)).append(" : ")
				.append(picture.getWidth()).append("px.\n").append(getString(R.string.full_res_height)).append(" : ")
				.append(picture.getHeight()).append("px.\n");


		builder.setTitle(R.string.image_properties).setMessage(message.toString())
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

/*	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// this tells the framework to start tracking for
			// a long press and eventual key up.  it will only
			// do so if this is the first down (not a repeat).
			event.startTracking();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
				&& !event.isCanceled()) {
			setResult(RESULT_OK);
			((RegalAndroidApplication) getApplication()).setCurrentPosition(currentPosition);
			this.finish();
			return true;
		}
		return false;
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// a long press of the call key.
			// do our work, returning true to consume it.  by
			// returning true, the framework knows an action has
			// been performed on the long press, so will set the
			// canceled flag for the following up event.
			super.onKeyLongPress(keyCode, event);
			return true;
		}
		return super.onKeyLongPress(keyCode, event);
	}
*/
	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		((RegalAndroidApplication) getApplication()).setCurrentPosition(currentPosition);
		this.finish();
	}

	void moveRight(){
		int newPosition = currentPosition+1;
		changingPhoto(newPosition);
	}

	void moveLeft(){
        stopSlideshow();
        int newPosition = currentPosition-1;
		changingPhoto(newPosition);
	}

    private void stopSlideshow() {
        if(slideshowTimer != null){
            slideshowTimer.cancel();
            slideshowTimer = null;
        }
    }

    private void changingPhoto(int newPosition) {
		String message;
		String title = "";
		// we're above the limit
		if (newPosition < 0 || newPosition >= albumPictures.size()) {
			message = getString(R.string.no_more_pictures);
		} else {
			currentPosition = newPosition;
			loadingPicture();
			StringBuilder showingPictureSb = new StringBuilder();
			StringBuilder showTitle = new StringBuilder();
			showingPictureSb.append(getString(R.string.showing_picture));
			int currentPositionToDisplay = currentPosition + 1;
			showTitle.append(currentPositionToDisplay);
			showTitle.append(SLASH);
			showTitle.append(albumPictures.size());
			title = showTitle.toString();
			showingPictureSb.append(title);
			message = showingPictureSb.toString();

			picture = albumPictures.get(currentPosition);
			String filename = picture.getFileName();
			showTitle.append(" ").append(filename);
			title = showTitle.toString();
		}
		// make the toast or update it
		if (toast == null) {
			toast = Toast.makeText(FullImage.this, message, Toast.LENGTH_SHORT);
		} else {
			toast.setText(message);
		}
		toast.show();
		setTitle(title);
	}

	private View.OnSystemUiVisibilityChangeListener mOnSystemUiVisibilityChangeListener = new View.OnSystemUiVisibilityChangeListener() {
		@Override
		public void onSystemUiVisibilityChange(int visibility) {
			if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == View.VISIBLE) {
				mToolbar.startAnimation(mSlideDown);
				getSupportActionBar().show();
			} else {
				getSupportActionBar().hide();
				mToolbar.startAnimation(mSlideUp);
			}
		}
	};

	private void showSystemUI() {
		mDecorView.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}

	private void hideSystemUI() {
		// Set the IMMERSIVE flag.
		// Set the content to appear under the system bars so that the content
		// doesn't resize when the system bars hide and show.
		mDecorView.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
						| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
						| View.SYSTEM_UI_FLAG_IMMERSIVE);
	}

	private View.OnClickListener onTIVClick = new View.OnClickListener() {
		public void onClick (View v) {
			if (TBvisible) {
				hideSystemUI();
				TBvisible = false;
			} else {
				showSystemUI();
				TBvisible = true;
			}
		}
	};
}
