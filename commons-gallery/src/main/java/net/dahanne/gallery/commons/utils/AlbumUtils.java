package net.dahanne.gallery.commons.utils;

import net.dahanne.gallery.commons.model.Album;

public class AlbumUtils {
	public static Album findAlbumFromAlbumName(Album rootAlbum, int albumName) {
		if (rootAlbum.getName() == albumName) {
			return rootAlbum;
		}
		for (Album album : rootAlbum.getChildren()) {
			if (album.getName() == albumName) {
				return album;
			}
			Album fromAlbumName = findAlbumFromAlbumName(album, albumName);
			if (fromAlbumName != null) {
				return fromAlbumName;
			}

		}
		return null;
	}
}
