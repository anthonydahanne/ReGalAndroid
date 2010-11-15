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

import net.dahanne.android.regalandroid.RegalAndroidApplication;
import net.dahanne.android.regalandroid.R;
import net.dahanne.android.regalandroid.utils.modified_android_source.NumberPicker;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChoosePhotoNumber extends Activity implements OnClickListener {

	private NumberPicker np;
	private Button okButton;
	private Button cancelButton;
	private Button firstPhotoButton;
	private Button lastPhotoButton;
	private int numberOfPictures;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// view creation and context loading
		setContentView(R.layout.choose_photo_number);
		numberOfPictures = ((RegalAndroidApplication) getApplication())
				.getPictures().size();
		int currentPhoto = getIntent().getIntExtra(FullImage.CURRENT_POSITION,
				0);
		setTitle(R.string.choose_photo_number_title);

		// number picker initialization
		np = (NumberPicker) findViewById(R.id.photo_number);
		np.setRange(1, numberOfPictures, null);
		np.setCurrent(currentPhoto + 1);

		// buttons and listeners attached
		okButton = (Button) findViewById(R.id.ok);
		cancelButton = (Button) findViewById(R.id.cancel);
		firstPhotoButton = (Button) findViewById(R.id.first_photo);
		lastPhotoButton = (Button) findViewById(R.id.last_photo);
		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		firstPhotoButton.setOnClickListener(this);
		lastPhotoButton.setOnClickListener(this);

	}

	public void onClick(View v) {
		int chosenPhoto = 0;
		switch (v.getId()) {
		case R.id.ok:
			chosenPhoto = np.getCurrent() - 1;
			break;
		case R.id.first_photo:
			// already 0 so nothing to do here
			break;
		case R.id.last_photo:
			chosenPhoto = numberOfPictures - 1;
			break;
		case R.id.cancel:
			setResult(RESULT_CANCELED);
			finish();
			break;
		default:
			break;
		}
		((RegalAndroidApplication) getApplication())
				.setCurrentPosition(chosenPhoto);
		finish();

	}
}
