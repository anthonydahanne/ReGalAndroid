package net.dahanne.android.g2android.tasks;

import net.dahanne.android.g2android.R;
import net.dahanne.android.g2android.activity.UploadPhoto;
import net.dahanne.android.g2android.utils.G2ConnectionUtils;
import net.dahanne.android.g2android.utils.GalleryConnectionException;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
public class LoginTask extends AsyncTask {

	private static final String NOTLOGGEDIN = "NOTLOGGEDIN";
	private static final String GUEST = "guest";
	private String user;
	private boolean galleryUrlIsValid = false;;
	private String exceptionMessage = null;
	private Activity activity;
	private ProgressDialog progressDialog;
	private String galleryUrl;
	private TextView loggedInAsText;
	private TextView galleryConfiguredTextView;
	private Button enterGalleryButton;

	public LoginTask(Activity context, ProgressDialog progressDialog,
			TextView loggedInAsText, TextView galleryConfiguredTextView,
			Button enterGalleryButton) {
		super();
		this.activity = context;
		this.progressDialog = progressDialog;
		this.loggedInAsText = loggedInAsText;
		this.galleryConfiguredTextView = galleryConfiguredTextView;
		this.enterGalleryButton = enterGalleryButton;
	}

	@Override
	protected String doInBackground(Object... parameters) {
		String authToken = null;
		try {
			galleryUrl = (String) parameters[0];
			user = (String) parameters[1];
			String password = (String) parameters[2];
			galleryUrlIsValid = G2ConnectionUtils.getInstance()
					.checkGalleryUrlIsValid(galleryUrl);
			if (StringUtils.isNotBlank(user)) {
				// the first thing is to login, if an username and password
				// are supplied !
				// This is done once and for all as the session cookie will
				// be stored !
				authToken = G2ConnectionUtils.getInstance().loginToGallery(
						galleryUrl, user, password);
			}
			// if no username is provided or if the username did not match
			// any access
			if (authToken == null) {
				authToken = NOTLOGGEDIN;
			}
		} catch (GalleryConnectionException e) {
			// the connection went wrong, the authToken is then null
			authToken = null;
			galleryUrlIsValid = false;
			exceptionMessage = e.getMessage();
		}
		return authToken;
	}

	@Override
	protected void onPostExecute(Object authToken) {
		if (galleryUrlIsValid) {

			if (loggedInAsText != null) {
				if (authToken.equals(NOTLOGGEDIN)) {
					// we 're not logged in
					loggedInAsText.setText(activity
							.getString(R.string.loggedin_as) + " " + GUEST);
				} else {
					// we are logged in
					loggedInAsText.setText(activity
							.getString(R.string.loggedin_as) + " " + user);
				}
				galleryConfiguredTextView.setText(galleryUrl);
				enterGalleryButton.setEnabled(true);
				if (activity instanceof UploadPhoto) {
					// we have to call it back to continue
					((UploadPhoto) activity).showAlbumList();
				}
			}

		} else {
			// neither login or simple command worked, we're offline
			if (loggedInAsText != null) {
				galleryConfiguredTextView
						.setText(R.string.g2android_no_gallery_configured);
				enterGalleryButton.setEnabled(false);
				loggedInAsText.setText(activity
						.getString(R.string.not_logged_in));
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			String message;
			// if there was an exception thrown, show it, or say to verify
			// settings
			if (exceptionMessage != null) {
				message = activity.getString(R.string.not_connected)
						+ galleryUrl
						+ activity.getString(R.string.exception_thrown)
						+ exceptionMessage;
			} else {

				message = activity.getString(R.string.not_connected)
						+ galleryUrl
						+ activity.getString(R.string.verify_your_settings);
			}
			builder.setTitle(R.string.problem)
					.setMessage(message)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();

		}
		progressDialog.dismiss();

	}
}
