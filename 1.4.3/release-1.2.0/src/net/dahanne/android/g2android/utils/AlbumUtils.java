package net.dahanne.android.g2android.utils;

import java.util.HashMap;
import java.util.Map;

import net.dahanne.android.g2android.activity.Settings;
import net.dahanne.android.g2android.activity.ShowAlbums;
import net.dahanne.android.g2android.model.Album;
import android.content.Context;

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

	public static Album retrieveRootAlbumAndItsHierarchy(Context context,String galleryHost,String galleryPath,int galleryPort) throws GalleryConnectionException {
		HashMap<String, String> albumsProperties = new HashMap<String, String>(
				0);
			albumsProperties = G2ConnectionUtils.fetchAlbums(galleryHost, galleryPath,
					galleryPort);

		Map<Integer, Album> nonSortedAlbums = G2ConnectionUtils
				.extractAlbumFromProperties(albumsProperties);
		Album rootAlbum = G2ConnectionUtils
				.organizeAlbumsHierarchy(nonSortedAlbums);
		return rootAlbum;
	}

}
