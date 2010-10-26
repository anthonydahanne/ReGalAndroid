package net.dahanne.android.g2android.api;

import net.dahanne.android.g2android.R;
import net.dahanne.android.g2android.utils.UriUtils;

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
		boolean valid = UriUtils.checkUrlIsValid(text);
		if(!valid){
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
