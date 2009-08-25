package net.dahanne.android.g2android.utils;

import net.dahanne.android.g2android.model.Album;

import org.junit.Assert;
import org.junit.Test;

public class AlbumUtilsTest extends Assert {
	@Test
	public void findAlbumFromAlbumNameTest() {
		Album rootAlbum = new Album();
		rootAlbum.setName(999);
		Album album1 = new Album();
		album1.setName(1);
		rootAlbum.getChildren().add(album1);
		Album album2 = new Album();
		album2.setName(2);
		rootAlbum.getChildren().add(album2);
		Album album3 = new Album();
		album3.setName(3);
		rootAlbum.getChildren().add(album3);
		Album album4 = new Album();
		album4.setName(4);
		rootAlbum.getChildren().add(album4);
		Album album31 = new Album();
		album31.setName(31);
		album3.getChildren().add(album31);
		Album album311 = new Album();
		album311.setName(311);
		album31.getChildren().add(album311);
		Album albumFound = AlbumUtils.findAlbumFromAlbumName(rootAlbum, 311);
		assertEquals(album311, albumFound);

		assertNull(AlbumUtils.findAlbumFromAlbumName(rootAlbum, 312));

	}
}
