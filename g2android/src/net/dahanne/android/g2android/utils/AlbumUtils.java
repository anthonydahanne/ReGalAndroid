package net.dahanne.android.g2android.utils;

import java.util.HashMap;
import java.util.Map;

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

	public static Album retrieveRootAlbumAndItsHierarchy(String galleryUrl)
			throws GalleryConnectionException {
		HashMap<String, String> albumsProperties = new HashMap<String, String>(
				0);
		albumsProperties = G2ConnectionUtils.fetchAlbums(galleryUrl);

		Map<Integer, Album> nonSortedAlbums = G2ConnectionUtils
				.extractAlbumFromProperties(albumsProperties);
		Album rootAlbum = G2ConnectionUtils
				.organizeAlbumsHierarchy(nonSortedAlbums);
		return rootAlbum;
	}

}
