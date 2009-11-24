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

import net.dahanne.android.g2android.R;
import net.dahanne.android.g2android.utils.modified_android_source.NumberPicker;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChoosePhotoNumber extends Activity implements OnClickListener {

	private NumberPicker np;
	private Button okButton;
	private Button cancelButton;
	private Button firstPhotoButton;
	private Button lastPhotoButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_photo_number);
		np = (NumberPicker) findViewById(R.id.photo_number);
		okButton = (Button) findViewById(R.id.ok);
		cancelButton = (Button) findViewById(R.id.cancel);
		firstPhotoButton = (Button) findViewById(R.id.first_photo);
		lastPhotoButton = (Button) findViewById(R.id.last_photo);

		setTitle(R.string.choose_photo_number_title);
		np.setRange(0, 100, null);
		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ok:
			int currentNumber = np.getCurrent();
			Log.d("yop", Integer.toString(currentNumber));
			// TODO
			break;
		case R.id.cancel:
			this.finish();
			break;
		case R.id.first_photo:
			// TODO
			break;
		case R.id.last_photo:
			// TODO
			break;
		default:
			break;
		}

	}
}
