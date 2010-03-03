package net.dahanne.android.g2android.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.dahanne.android.g2android.model.Album;
import net.dahanne.android.g2android.model.G2Picture;

import org.junit.Assert;
import org.junit.Test;

public class G2DataUtilsTest extends Assert {
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
		Album albumFound = G2DataUtils.findAlbumFromAlbumName(rootAlbum, 311);
		assertEquals(album311, albumFound);

		assertNull(G2DataUtils.findAlbumFromAlbumName(rootAlbum, 312));

	}

	@Test
	public void extractG2FromPropertiesTest() {
		HashMap<String, String> fetchImages = new HashMap<String, String>();
		fetchImages.put("image.thumb_height.393", "150");
		fetchImages.put("image.title.393", "picture.jpg");
		fetchImages.put("image.resizedName.393", "12600");
		fetchImages.put("image.resized_width.393", "800");
		fetchImages.put("image.resized_height.393", "600");
		fetchImages.put("image.raw_height.393", "2848");
		fetchImages.put("image.caption.393", "");
		fetchImages.put("image.name.393", "12598");
		fetchImages.put("image.thumbName.393", "12599");
		fetchImages.put("image.thumb_width.393", "150");
		fetchImages.put("image.raw_width.393", "4288");
		fetchImages.put("image.raw_filesize.393", "3400643");
		fetchImages.put("image.caption.393", "caption");
		fetchImages.put("image.forceExtension.393", "jpg");
		fetchImages.put("image.hidden.393", "true");
		fetchImages.put("image.clicks.393", "45");
		fetchImages.put("image.capturedate.year.393", "2009");
		fetchImages.put("image.capturedate.mon.393", "10");
		fetchImages.put("image.capturedate.mday.393", "19");
		fetchImages.put("image.capturedate.hours.393", "22");
		fetchImages.put("image.capturedate.minutes.393", "45");
		fetchImages.put("image.capturedate.seconds.393", "48");
		fetchImages.put("image_count", "437");
		fetchImages
				.put("baseurl",
						"http://g2.dahanne.net/main.php?g2_view=core.DownloadItem&g2_itemId=");

		Collection<G2Picture> pictures = G2DataUtils
				.extractG2PicturesFromProperties(fetchImages);
		G2Picture picture = pictures.iterator().next();
		assertEquals(150, picture.getThumbHeight());
		assertEquals("picture.jpg", picture.getTitle());
		assertEquals("12600", picture.getResizedName());
		assertEquals(800, picture.getResizedWidth());
		assertEquals(2848, picture.getRawHeight());
		assertEquals("12598", picture.getName());
		assertEquals("12599", picture.getThumbName());
		assertEquals(150, picture.getThumbWidth());
		assertEquals(4288, picture.getRawWidth());
		assertEquals("caption", picture.getCaption());
		assertEquals("jpg", picture.getForceExtension());
		assertEquals(true, picture.isHidden());
		assertEquals(45, picture.getImageClicks());
		// assertEquals("2009", picture.getCaptureDateYear());
		// assertEquals("10", picture.getCaptureDateMonth());
		// assertEquals("19", picture.getCaptureDateDay());
		// assertEquals("22", picture.getCaptureDateHour());
		// assertEquals("45", picture.getCaptureDateMinute());
		// assertEquals("48", picture.getCaptureDateSecond());

	}

	@Test
	public void extractAlbumFromPropertiesTest() {
		HashMap<String, String> albumsProperties = new HashMap<String, String>();
		albumsProperties.put("album.name.1", "10726");
		albumsProperties.put("album.title.1", "Brunch");
		albumsProperties.put("album.summary.1",
				"Le Dimanche de 11h à 15h avait lieu le brunch");
		albumsProperties.put("album.parent.1", "7");
		albumsProperties.put("album.perms.add.1", "false");
		albumsProperties.put("album.perms.write.1", "false");
		albumsProperties.put("album.perms.del_alb.1", "false");
		albumsProperties.put("album.perms.create_sub.1", "false");
		albumsProperties.put("album.info.extrafields.1", "Summary,Description");
		albumsProperties.put("debug_time_generate", "0,075s");
		albumsProperties.put("can_create_root", "false");
		albumsProperties.put("album_count", "9");
		albumsProperties.put("status", "0");

		Collection<Album> albums = G2DataUtils.extractAlbumFromProperties(
				albumsProperties).values();
		Album album = albums.iterator().next();
		assertEquals(1, album.getId());
		assertEquals("Brunch", album.getTitle());
		assertEquals(10726, album.getName());
		assertEquals("Le Dimanche de 11h à 15h avait lieu le brunch", album
				.getSummary());
		assertEquals(7, album.getParentName());
		assertEquals("Summary,Description", album.getExtrafields());

	}

	@Test
	public void organizeAlbumsHierarchy() throws Exception {
		Map<Integer, Album> albums = new HashMap<Integer, Album>();
		// 0 does not exist (root's parent)
		// 10 is the gallery name (id 100)
		// 20 (id200), 3(id300), 4(id400) are 10's children
		// 5(id500),6(id600) are 20's children

		Album album = new Album();
		album.setName(6);
		album.setId(600);
		album.setParentName(20);
		albums.put(album.getId(), album);
		album = new Album();
		album.setName(3);
		album.setId(300);
		album.setParentName(10);
		albums.put(album.getId(), album);
		album = new Album();
		album.setName(4);
		album.setId(400);
		album.setParentName(10);
		albums.put(album.getId(), album);
		album = new Album();
		album.setName(10);
		album.setId(100);
		album.setParentName(0);
		albums.put(album.getId(), album);
		album = new Album();
		album.setName(5);
		album.setId(500);
		album.setParentName(20);
		albums.put(album.getId(), album);
		album = new Album();
		album.setName(20);
		album.setId(200);
		album.setParentName(10);
		albums.put(album.getId(), album);

		Album finalALbum = G2DataUtils.organizeAlbumsHierarchy(albums);
		assertEquals(10, finalALbum.getName());
		assertEquals(20, finalALbum.getChildren().get(0).getName());
		assertEquals(4, finalALbum.getChildren().get(1).getName());
		assertEquals(3, finalALbum.getChildren().get(2).getName());
		assertEquals(5, finalALbum.getChildren().get(0).getChildren().get(0)
				.getName());
		assertEquals(6, finalALbum.getChildren().get(0).getChildren().get(1)
				.getName());

	}

}
