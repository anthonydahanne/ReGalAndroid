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

package net.dahanne.android.regalandroid.api;

import net.dahanne.android.regalandroid.R;
import net.dahanne.gallery.commons.utils.UriUtils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

public class EditUrlPreference extends EditTextPreference {

	private Context givenContext;

	public EditUrlPreference(Context context) {
		super(context);
		givenContext=context;
	}

	
	
	public EditUrlPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		givenContext=context;
	}



	public EditUrlPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		givenContext = context;
	}



	@Override
	public void setText(String text) {
		super.setText(text);
		try{
			UriUtils.checkUrlIsValid(text);
		}catch (IllegalArgumentException e){
			alertValidationProblem();
		}
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		super.onClick(dialog, which);
		
	}
	
	public void alertValidationProblem() {
		AlertDialog.Builder builder = new AlertDialog.Builder(givenContext);
		String message = 
			givenContext.getString(R.string.url_validation_problem);
		builder.setTitle(R.string.problem).setMessage(message)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
