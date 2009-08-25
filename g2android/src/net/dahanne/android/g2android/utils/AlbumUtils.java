package net.dahanne.android.g2android.utils;

import net.dahanne.android.g2android.model.Album;

public class AlbumUtils {
	public static Album findAlbumFromAlbumName(Album rootAlbum, int i) {
		if (rootAlbum.getName() == i) {
			return rootAlbum;
		}
		for (Album album : rootAlbum.getChildren()) {
			if (album.getName() == i) {
				return album;
			}
			Album fromAlbumName = findAlbumFromAlbumName(album, i);
			if (fromAlbumName != null) {
				return fromAlbumName;
			}

		}
		return null;
	}
}
