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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import junit.framework.Assert;
import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.model.Picture;
import net.dahanne.gallery.commons.remote.GalleryConnectionException;
import net.dahanne.gallery.commons.remote.ImpossibleToLoginException;
import net.dahanne.gallery.g2.java.client.G2ConnectionUtils;

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
	private G2ConnectionUtils g2ConnectionUtils;

	@Before
	public void init() {
		galleryUrl = "https://g2.dahanne.net/";
		user = "g2android";
		password = "g2android";
		g2ConnectionUtils = new G2ConnectionUtils(galleryUrl,user,password);
	}

	@Test
	public void fetchImagesTest() throws GalleryConnectionException {
		HashMap<String, String> fetchImages = g2ConnectionUtils.fetchImages(
				galleryUrl, 11);
		assertTrue(fetchImages.size() != 0);
		assertTrue(fetchImages.containsKey("image_count"));
		assertFalse(fetchImages.get("image_count").equals("0"));

		fetchImages = g2ConnectionUtils.fetchImages(galleryUrl, 9999);
		// just debug messages
		assertTrue(fetchImages.size() == 3);

	}

	@Test(expected = GalleryConnectionException.class)
	public void fetchImagesTest__exception() throws GalleryConnectionException {
		g2ConnectionUtils.fetchImages("http://something.thatmaynotexist.com",
				11);

	}

//	@Test
//	public void checkGalleryUrlIsValidTest() throws GalleryConnectionException {
//
//		// I know this is not a galleryUrl...
//		String falseGalleryHost = "www.google.com";
//		assertFalse(g2ConnectionUtils.checkGalleryUrlIsValid(falseGalleryHost));
//
//		// This is supposed to be a valid Url !
//		assertTrue(g2ConnectionUtils.checkGalleryUrlIsValid(galleryUrl));
//
//	}

	@Test
	public void loginToGalleryTest() throws GalleryConnectionException {
		g2ConnectionUtils
				.loginToGallery(galleryUrl, user, password);
		// if we're logged in, we should see the g2android album
		HashMap<String, String> fetchAlbums = g2ConnectionUtils
				.fetchAlbums(galleryUrl);
		boolean found = false;
		for (Entry<String, String> entry : fetchAlbums.entrySet()) {
			if (entry.getValue().equals("G2AndroidSecretAlbum")) {
				found = true;
			}
		}
		assertEquals(true, found);
	}
	
	@Test(expected=ImpossibleToLoginException.class)
	public void loginToGalleryTest__badPassword() throws GalleryConnectionException {
		g2ConnectionUtils.loginToGallery(galleryUrl,
				"hacker", "hackerPassword");
	}
	
	@Test(expected = ImpossibleToLoginException.class)
	public void loginToGalleryTestFailBecauseUrlIsWrong()
			throws GalleryConnectionException {

		g2ConnectionUtils.loginToGallery("http://g2.dahanne .net/", "hacker",
				"hackerPassword");

	}

	/**
	 * This test fail because the user can't add a photo item in given album
	 * 
	 * @throws GalleryConnectionException
	 * @throws IOException
	 */
	@Test(expected = GalleryConnectionException.class)
	public void sendImageToGalleryTestFail() throws GalleryConnectionException, IOException {
		g2ConnectionUtils.loginToGallery(galleryUrl, user, password);
		File imageFile = new File("image.png");
		InputStream is = getClass().getResourceAsStream( "/image.png" );

	    OutputStream out=new FileOutputStream(imageFile);
	    byte buf[]=new byte[1024];
	    int len;
	    while((len=is.read(buf))>0)
	    out.write(buf,0,len);
	    out.close();
	    is.close();
		
		g2ConnectionUtils.sendImageToGallery(galleryUrl, -5, imageFile, null,
				null, null);
	}

	@Test
	public void sendImageToGalleryTest() throws GalleryConnectionException {
		g2ConnectionUtils.loginToGallery(galleryUrl, user, password);
		File imageFile = new File("image.png");
		int newItemId = g2ConnectionUtils.sendImageToGallery(galleryUrl, 174,
				imageFile, "plouf", "Summary from test",
				"Description from test");
		assertTrue(newItemId != 0);
	}

	@Test
	public void createNewAlbumTest() throws GalleryConnectionException {
		g2ConnectionUtils.loginToGallery(galleryUrl, user, password);
		Random random = new Random();
		int randomInt = random.nextInt();
		String albumName = "UnitTestAlbumNumber" + randomInt;
		String albumTitle = "Unit Test Album";
		String albumDescription = "Yet another Unit Test Album !";
		int newAlbumName = g2ConnectionUtils.createNewAlbum(galleryUrl, 174,
				albumName, albumTitle, albumDescription);
		assertTrue(newAlbumName != 0);
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

		Collection<Picture> pictures = g2ConnectionUtils
				.extractG2PicturesFromProperties(fetchImages);
		Picture picture = pictures.iterator().next();
		assertEquals(150, picture.getThumbHeight());
		assertEquals("picture.jpg", picture.getTitle());
		assertEquals("https://g2.dahanne.net//main.php?g2_view=core.DownloadItem&g2_itemId=12600",  picture.getResizedUrl());
		assertEquals(800, picture.getResizedWidth());
		assertEquals(2848, picture.getHeight());
		assertEquals("picture.jpg", picture.getName());
		assertEquals("https://g2.dahanne.net//main.php?g2_view=core.DownloadItem&g2_itemId=12599", picture.getThumbUrl());
		assertEquals(150, picture.getThumbWidth());
		assertEquals(4288, picture.getWidth());
		assertEquals("caption", picture.getCaption());
		assertEquals("jpg", picture.getForceExtension());
		assertEquals(true, picture.isHidden());
//		assertEquals(45, picture.getImageClicks());

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

		Collection<Album> albums = g2ConnectionUtils.extractAlbumFromProperties(
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

		Album finalALbum = g2ConnectionUtils.organizeAlbumsHierarchy(albums);
		assertEquals(10, finalALbum.getName());
		assertEquals(20, finalALbum.getSubAlbums().get(0).getName());
		assertEquals(4, finalALbum.getSubAlbums().get(1).getName());
		assertEquals(3, finalALbum.getSubAlbums().get(2).getName());
		assertEquals(5, finalALbum.getSubAlbums().get(0).getSubAlbums().get(0)
				.getName());
		assertEquals(6, finalALbum.getSubAlbums().get(0).getSubAlbums().get(1)
				.getName());

	}

	
//	@Test
//	public void findSubAlbumsTest(){
//		Album rootAlbum = new Album();
//		rootAlbum.setName(999);
//		Album album1 = new Album();
//		album1.setName(1);
//		rootAlbum.getChildren().add(album1);
//		Album album2 = new Album();
//		album2.setName(2);
//		rootAlbum.getChildren().add(album2);
//		Album album3 = new Album();
//		album3.setName(3);
//		rootAlbum.getChildren().add(album3);
//		Album album4 = new Album();
//		album4.setName(4);
//		rootAlbum.getChildren().add(album4);
//		Album album31 = new Album();
//		album31.setName(31);
//		album3.getChildren().add(album31);
//		Album album311 = new Album();
//		album311.setName(311);
//		album31.getChildren().add(album311);
//		List<Album> albumsFound = g2ConnectionUtils.findSubAlbums(rootAlbum, 999);
//		List<Album> expectedSubAlbumsOfRootAlbum = new ArrayList<Album>();
//		expectedSubAlbumsOfRootAlbum.add(album1);
//		expectedSubAlbumsOfRootAlbum.add(album2);
//		expectedSubAlbumsOfRootAlbum.add(album3);
//		expectedSubAlbumsOfRootAlbum.add(album4);
//		
//		assertEquals(expectedSubAlbumsOfRootAlbum, albumsFound);
//		
//		albumsFound = g2ConnectionUtils.findSubAlbums(rootAlbum, 31);
//		List<Album> expectedSubAlbumsOfAlbum31 = new ArrayList<Album>();
//		expectedSubAlbumsOfAlbum31.add(album311);
//		
//		assertEquals(expectedSubAlbumsOfAlbum31, albumsFound);
//		
//
//	}

}
