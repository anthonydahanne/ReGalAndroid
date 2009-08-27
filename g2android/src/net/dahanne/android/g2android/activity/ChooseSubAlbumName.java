package net.dahanne.android.g2android.activity;

import net.dahanne.android.g2android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ChooseSubAlbumName extends Activity implements OnClickListener {
	private EditText subalbumEditText;
	private Button okButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_subalbum);
		subalbumEditText = (EditText) findViewById(R.id.subalbum_id);
		okButton = (Button) findViewById(R.id.ok_id);
		okButton.setOnClickListener(this);

	}

	public void onClick(View v) {
		Intent data = new Intent();
		data.putExtra("subalbumName", subalbumEditText.getText().toString());
		setResult(RESULT_OK, data);
		finish();

	}
}
