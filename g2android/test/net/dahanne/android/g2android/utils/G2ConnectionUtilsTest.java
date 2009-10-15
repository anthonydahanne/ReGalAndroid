/*
 * G2Android
 * Copyright (c) 2009 Anthony Dahanne
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.dahanne.android.g2android.utils;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import junit.framework.Assert;
import net.dahanne.android.g2android.model.Album;
import net.dahanne.android.g2android.model.G2Picture;

import org.junit.Before;
import org.junit.Test;

/**
 * These tests should run, provided that the gallery at http://g2.dahanne.net is
 * up and running !
 * 
 * @author Anthony Dahanne
 * 
 */
public class G2ConnectionUtilsTest extends Assert {
	private String galleryUrl;
	private String user;
	private String password;

	@Before
	public void init() {
		galleryUrl = "http://g2.dahanne.net/";
		user = "g2android";
		password = "g2android";
	}

	@Test
	public void fetchImagesTest() throws GalleryConnectionException {
		HashMap<String, String> fetchImages = G2ConnectionUtils.fetchImages(
				galleryUrl, 11);
		assertTrue(fetchImages.size() != 0);
		assertTrue(fetchImages.containsKey("image_count"));
		assertFalse(fetchImages.get("image_count").equals("0"));

		fetchImages = G2ConnectionUtils.fetchImages(galleryUrl, 9999);
		// just debug messages
		assertTrue(fetchImages.size() == 3);

	}

	@Test(expected = GalleryConnectionException.class)
	public void fetchImagesTest__exception() throws GalleryConnectionException {
		G2ConnectionUtils.fetchImages("something.thatmaynotexist.com", 11);

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
		fetchImages.put("image_count", "437");
		fetchImages
				.put("baseurl",
						"http://g2.dahanne.net/main.php?g2_view=core.DownloadItem&g2_itemId=");

		Collection<G2Picture> pictures = G2ConnectionUtils
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
		assertEquals(3400643, picture.getRawFilesize());

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

		Collection<Album> albums = G2ConnectionUtils
				.extractAlbumFromProperties(albumsProperties).values();
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

		Album finalALbum = G2ConnectionUtils.organizeAlbumsHierarchy(albums);
		assertEquals(10, finalALbum.getName());
		assertEquals(20, finalALbum.getChildren().get(0).getName());
		assertEquals(4, finalALbum.getChildren().get(1).getName());
		assertEquals(3, finalALbum.getChildren().get(2).getName());
		assertEquals(5, finalALbum.getChildren().get(0).getChildren().get(0)
				.getName());
		assertEquals(6, finalALbum.getChildren().get(0).getChildren().get(1)
				.getName());

	}

	@Test
	public void checkGalleryUrlIsValidTest() throws GalleryConnectionException {

		// I know this is not a galleryUrl...
		String falseGalleryHost = "www.google.com";
		assertFalse(G2ConnectionUtils.checkGalleryUrlIsValid(falseGalleryHost));

		// This is supposed to be a valid Url !
		assertTrue(G2ConnectionUtils.checkGalleryUrlIsValid(galleryUrl));

	}

	@Test
	public void loginToGalleryTest() throws GalleryConnectionException {

		String authToken = G2ConnectionUtils.loginToGallery(galleryUrl,
				"hacker", "hackerPassword");
		assertNull(authToken);

		authToken = G2ConnectionUtils
				.loginToGallery(galleryUrl, user, password);
		assertNotNull(authToken);
		// if we're logged in, we should see the g2android album
		HashMap<String, String> fetchAlbums = G2ConnectionUtils
				.fetchAlbums(galleryUrl);
		boolean found = false;
		for (Entry<String, String> entry : fetchAlbums.entrySet()) {
			if (entry.getValue().equals("G2AndroidSecretAlbum")) {
				found = true;
			}
		}
		assertEquals(true, found);

	}

	/**
	 * This test fail because the user can't add a photo item in given album
	 * 
	 * @throws GalleryConnectionException
	 */
	@Test(expected = GalleryConnectionException.class)
	public void sendImageToGalleryTestFail() throws GalleryConnectionException {
		String authToken = G2ConnectionUtils.loginToGallery(galleryUrl, user,
				password);
		File imageFile = new File("image.png");
		int newItemId = G2ConnectionUtils.sendImageToGallery(galleryUrl,
				9999999, imageFile);
	}

	@Test
	public void sendImageToGalleryTest() throws GalleryConnectionException {
		String authToken = G2ConnectionUtils.loginToGallery(galleryUrl, user,
				password);
		File imageFile = new File("image.png");
		int newItemId = G2ConnectionUtils.sendImageToGallery(galleryUrl, 174,
				imageFile);
		assertTrue(newItemId != 0);
	}

	@Test
	public void createNewAlbumTest() throws GalleryConnectionException {
		String authToken = G2ConnectionUtils.loginToGallery(galleryUrl, user,
				password);
		Random random = new Random();
		int randomInt = random.nextInt();
		String albumName = "UnitTestAlbumNumber" + randomInt;
		String albumTitle = "Unit Test Album";
		String albumDescription = "Yet another Unit Test Album !";
		// File imageFile = new File("image.png");
		int newAlbumName = G2ConnectionUtils.createNewAlbum(galleryUrl, 174,
				albumName, albumTitle, albumDescription);
		assertTrue(newAlbumName != 0);
	}

	@Test
	public void getPortFromUrlTest() {
		int portFromUrl = G2ConnectionUtils
				.getPortFromUrl(galleryUrl + ":4343");
		assertEquals(4343, portFromUrl);

		portFromUrl = G2ConnectionUtils.getPortFromUrl(galleryUrl
				+ ":4343/gallery2");
		assertEquals(4343, portFromUrl);

		portFromUrl = G2ConnectionUtils.getPortFromUrl("https://url.net");
		assertEquals(443, portFromUrl);

		portFromUrl = G2ConnectionUtils.getPortFromUrl("https://url.net:4343");
		assertEquals(4343, portFromUrl);

		portFromUrl = G2ConnectionUtils
				.getPortFromUrl("https://url.net:4343/gallery2");
		assertEquals(4343, portFromUrl);

	}

	@Test
	public void getPathFromUrlTest() {
		String pathFromUrl = G2ConnectionUtils
				.getPathFromUrl("http://g2.dahanne.net/gallery2");
		assertEquals("/gallery2", pathFromUrl);

		pathFromUrl = G2ConnectionUtils
				.getPathFromUrl("http://g2.dahanne.net:8080/gallery2");
		assertEquals("/gallery2", pathFromUrl);
		pathFromUrl = G2ConnectionUtils
				.getPathFromUrl("https://g2.dahanne.net:8080/gallery2");
		assertEquals("/gallery2", pathFromUrl);
		pathFromUrl = G2ConnectionUtils.getPathFromUrl("https://dahanne.net");
		assertEquals("/", pathFromUrl);

	}

	@Test
	public void getHostFromUrlTest() {
		String pathFromUrl = G2ConnectionUtils
				.getHostFromUrl("http://g2.dahanne.net/gallery2");
		assertEquals("g2.dahanne.net", pathFromUrl);

		pathFromUrl = G2ConnectionUtils
				.getHostFromUrl("http://g2.dahanne.net:8080/gallery2");
		assertEquals("g2.dahanne.net", pathFromUrl);
		pathFromUrl = G2ConnectionUtils
				.getHostFromUrl("https://g2.dahanne.net:8080/gallery2");
		assertEquals("g2.dahanne.net", pathFromUrl);
		pathFromUrl = G2ConnectionUtils.getHostFromUrl("https://dahanne.net");
		assertEquals("dahanne.net", pathFromUrl);

	}
}
