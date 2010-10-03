package net.dahanne.android.g2android.adapters;

import java.util.List;

import net.dahanne.android.g2android.R;
import net.dahanne.android.g2android.model.Album;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbumAdapter extends ArrayAdapter<Album> {

	private final List<Album> items;
	private final Context context;

	public AlbumAdapter(Context context, int textViewResourceId,
			List<Album> items) {
		super(context, textViewResourceId, items);
		this.items = items;
		this.context = context;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.show_albums_row, null);
		}
		Album album = items.get(position);
		if (album != null) {
			TextView tt = (TextView) v.findViewById(R.id.first_line);
			TextView bt = (TextView) v.findViewById(R.id.second_line);
			if (tt != null) {
				tt.setText(album.getTitle());
			}
			if (bt != null) {
				String summary = album.getSummary();
				if (summary == null || summary.equals("")) {
					summary = context.getString(R.string.no_summary_available);
				}
				bt.setText(summary);
			}
			;
			ImageView iv = (ImageView) v.findViewById(R.id.icon);
			if (iv != null) {
				if (context.getString(R.string.view_album_pictures).equals(
						album.getTitle())) {
					// display another icon when fake album
					iv.setImageResource(R.drawable.ic_launcher_gallery);
				} else {
					iv.setImageResource(R.drawable.folder_images_icon);
				}
			}

		}
		return v;
	}
}
