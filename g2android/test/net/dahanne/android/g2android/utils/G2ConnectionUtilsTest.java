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
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

import junit.framework.Assert;

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
