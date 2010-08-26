package net.dahanne.android.g2android.utils;

import java.util.Comparator;

import net.dahanne.android.g2android.model.Album;

public class AlbumComparator implements Comparator<Album> {

	public int compare(Album album1, Album album2) {
		if (album1 == null) {
			return -1;
		}
		if (album2 == null) {
			return 1;
		}
		return album1.getName() > album2.getName() ? 1 : -1;
	}
}
