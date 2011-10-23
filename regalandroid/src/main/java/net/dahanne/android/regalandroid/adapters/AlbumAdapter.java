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

package net.dahanne.android.regalandroid.adapters;

import java.util.List;

import net.dahanne.android.regalandroid.R;
import net.dahanne.android.regalandroid.tasks.DownloadImageTask;
import net.dahanne.gallery.commons.model.Album;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbumAdapter extends ArrayAdapter<Album> {

	private final List<Album> items;
	private final Context context;
	private final Logger logger = LoggerFactory.getLogger(AlbumAdapter.class);
	private static final String COVER_PREFIX = "album_cover_";
	private static final String COVER_POSTFIX = ".jpg";
	private static final int COVER_MAX_WIDTH = 100;
	private static final int COVER_MAX_HEIGHT = 100;


	public AlbumAdapter(Context context, int textViewResourceId, List<Album> items) {
		super(context, textViewResourceId, items);
		this.items = items;
		this.context = context;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View showAlbumsLineView = convertView;
		if (showAlbumsLineView == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			showAlbumsLineView = vi.inflate(R.layout.show_albums_row, null);
		}
		Album album = items.get(position);
		if (album != null) {
			TextView firstLine = (TextView) showAlbumsLineView.findViewById(R.id.first_line);
			TextView secondLine = (TextView) showAlbumsLineView.findViewById(R.id.second_line);
			if (firstLine != null) {
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(album.getTitle());
				if (album.getPictures().size() > 0) {
					stringBuilder.append(" (");
					stringBuilder.append(album.getPictures().size());
					stringBuilder.append(")");
				}
				firstLine.setText(stringBuilder.toString());
			}
			if (secondLine != null) {
				String summary = album.getSummary();
				if (summary == null || summary.equals("")) {
					summary = "";
				}
				secondLine.setText(summary);
			}
			
			//the famous album cover thumb
			ImageView iconView = (ImageView) showAlbumsLineView.findViewById(R.id.icon);
			iconView.setAdjustViewBounds(true);
			iconView.setMaxWidth(COVER_MAX_WIDTH);
			iconView.setMaxHeight(COVER_MAX_HEIGHT);
			if (iconView != null) {
				if (context.getString(R.string.view_album_pictures).equals(album.getTitle())) {
					// display another icon when fake album
					iconView.setImageResource(R.drawable.ic_launcher_gallery);
				} else {
					//it is a true album, let s try to display the album cover
					if(album.getAlbumCoverCachePath()!=null){
						//we can hit the cache
						iconView.setImageBitmap(BitmapFactory.decodeFile(album.getAlbumCoverCachePath()));
					}
					else if(album.getAlbumCoverUrl()!=null){
						//not in cache, let s get download it
						logger.debug("getting album cover from gallery : {}",album.getAlbumCoverUrl());
						DownloadImageTask downloadTask = new DownloadImageTask(context,iconView);
						int albumName = album.getName();
						downloadTask.execute(COVER_PREFIX + albumName,COVER_POSTFIX, album.getAlbumCoverUrl(),albumName,null,0,null,album);
					}
					else {
						//no cover at all, default icon
						logger.debug("no cover for this album : {}",album.getTitle());
						iconView.setImageResource(R.drawable.folder_images_icon);
					}
					
				}
			}

		}
		return showAlbumsLineView;
	}
}
