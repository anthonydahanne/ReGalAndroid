/**
 *  g3-java-client, a Menalto Gallery3 Java Client API
 *  URLs: https://github.com/anthonydahanne/ReGalAndroid , http://blog.dahanne.net
 *  Copyright (c) 2010 Anthony Dahanne
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.dahanne.gallery3.client.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dahanne.gallery3.client.business.exceptions.G3BadRequestException;
import net.dahanne.gallery3.client.business.exceptions.G3ForbiddenException;
import net.dahanne.gallery3.client.business.exceptions.G3GalleryException;
import net.dahanne.gallery3.client.business.exceptions.G3ItemNotFoundException;
import net.dahanne.gallery3.client.model.Entity;
import net.dahanne.gallery3.client.model.Item;
import net.dahanne.gallery3.client.utils.ItemUtils;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.io.ByteStreams;

public class G3ClientTest {

	private static final int G2ANDROID_SECRET_ALBUM = 11;
	private static final String galleryUrl = "http://g3.dahanne.net/";
	private static final String username = "g2android";
	private static final String password = "g2android";
	private G3Client itemClient;
	private static int createdAlbumId;
	private static int createdPhotoId;

	@Before
	public void setUp() {
		itemClient = new G3Client(galleryUrl, "Unit Test");
		itemClient.setUsername(username);
		itemClient.setPassword(password);
	}

    @Test
	public void getItemTest__album() throws G3GalleryException {
		Item item1 = itemClient.getItem(1);
		assertEquals("Gallery", item1.getEntity().getTitle());
	}

	@Test
	public void getApiKey() throws G3GalleryException {
		String apiKey = itemClient.getApiKey();
		assertNotNull(apiKey);
	}

	@Test
	public void createAlbum() throws G3GalleryException {
		Entity albumToCreate = new Entity();
		albumToCreate.setName("AlbumName" + System.currentTimeMillis());
		albumToCreate.setTitle("New Album");
		albumToCreate.setParent(G2ANDROID_SECRET_ALBUM);
		createdAlbumId = itemClient.createItem(albumToCreate, null);
		assertNotNull(createdAlbumId);
	}

	@Ignore
	@Test(expected = G3BadRequestException.class)
	public void createAlbum__already_exists() throws G3GalleryException {
		long albumSuffix = System.currentTimeMillis();
		Entity albumToCreate = new Entity();
		albumToCreate.setName("AlbumName" + albumSuffix);
		albumToCreate.setTitle("New Album");
		albumToCreate.setParent(G2ANDROID_SECRET_ALBUM);

		itemClient.createItem(albumToCreate, null);

		// do it again -->EXCEPTION ! Twice the same album name in the same
		// parent !
		itemClient.createItem(albumToCreate, null);

	}

	@Test(expected = G3ItemNotFoundException.class)
	public void createAlbum__bad_api_key() throws G3GalleryException {
		itemClient.setExistingApiKey("plouf");
		Entity albumToCreate = new Entity();
		albumToCreate.setName("AlbumName" + System.currentTimeMillis());
		albumToCreate.setTitle("New Album");
		albumToCreate.setParent(G2ANDROID_SECRET_ALBUM);
		itemClient.createItem(albumToCreate, null);

	}

	@Test
	public void updateItemAlbum() throws G3GalleryException {
        createAlbum(); //first create album renamed below

		Entity albumToUpdate = new Entity();
		albumToUpdate.setId(createdAlbumId);
		albumToUpdate.setTitle("New Album renamed !");
		// the album is updated
		itemClient.updateItem(albumToUpdate);
		// we check it has be updated fetching it
		assertEquals("New Album renamed !", itemClient.getItem(createdAlbumId).getEntity().getTitle());
	}

	@Test
	public void getAlbumAndSubAlbumsTest() throws G3GalleryException {
		
		List<Item> subAlbums = itemClient.getAlbumAndSubAlbums(11);
		boolean foundRecentlyAddedAlbum = false;
		for (Item album : subAlbums) {
			// we test if we can find the previously added album
			if (album.getEntity().getId() == createdAlbumId) {
				foundRecentlyAddedAlbum = true;
			}
		}
		assertTrue(foundRecentlyAddedAlbum);
		
		subAlbums = itemClient.getAlbumAndSubAlbums(172);
		for (Item album : subAlbums) {
			// we test if there are only albums returned by the request
			if (!album.getEntity().getType().equals("album")) {
				fail("found some other types than album");
			}
		}
	}
	
	@Test
	public void getAlbumAndSubAlbumsAndPicturesTest() throws G3GalleryException {
		List<Item> albumAndSubAlbumsAndPictures = itemClient.getAlbumAndSubAlbumsAndPictures(172);
		
		Map<Integer,String> actual =  new HashMap<Integer, String>();
		for (Item album : albumAndSubAlbumsAndPictures) {
			actual.put(album.getEntity().getId(), album.getEntity().getName());
		}
		Map<Integer,String> expected =  new HashMap<Integer, String>();
		expected.put(172, "Ottawa");
		expected.put(173, "GP7AEU~V.JPG");
		expected.put(178, "G3LKDB~A.JPG");
		expected.put(179, "Canadian-parliament");
		expected.put(180, "Canal Rideau");
		
		assertEquals(expected, actual);
	}

	

	@Test
	public void getPicturesTest() throws G3GalleryException {
		List<Item> pictures = itemClient.getPictures(11);

		pictures = itemClient.getPictures(172);
		for (Item picture : pictures) {
			// we test if there are only photos returned by the request
			if (!picture.getEntity().getType().equals("photo")) {
				fail("found some other types than photo");
			}
			// only photos have a resize_height different to 0
			if (picture.getEntity().getResizeHeight() == 0) {
				fail("could not found a resize_height info");
			}

		}
	}

	@Test(expected = G3ItemNotFoundException.class)
	public void deleteItemAlbum() throws G3GalleryException {
		// we delete the item
		itemClient.deleteItem(createdAlbumId);
		// we then expect it not to exist anymore !--> ItemNotFoundException !
		itemClient.getItem(createdAlbumId);
	}

	@Test
	public void addPhoto() throws IOException, G3GalleryException {
		Entity photoToCreate = new Entity();
		photoToCreate.setName("logo.png");
		photoToCreate.setTitle("New Photo");
		photoToCreate.setParent(G2ANDROID_SECRET_ALBUM);
		
//		URL resource = Resources.getResource("logo.png");
//		System.out.println(resource.getPath());
		
		File photoFile = new File(this.getClass().getClassLoader()
				.getResource("logo.png").getPath());
		createdPhotoId = itemClient.createItem(photoToCreate, photoFile);
		Item item = itemClient.getItem(createdPhotoId);
		assertEquals("New Photo", item.getEntity().getTitle());
		assertEquals("logo.png", item.getEntity().getName());
		assertEquals(55, item.getEntity().getHeight());
		assertEquals(55, item.getEntity().getWidth());

	}

	@Test
	public void getPhotoInputStream() throws IOException, G3GalleryException, MagicParseException, MagicMatchNotFoundException, MagicException {
		Item item1 = itemClient.getItem(createdPhotoId);
		String url = item1.getEntity().getFileUrl();
		InputStream inputStream = itemClient.getPhotoInputStream(url);
		//conversion stuff..
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ByteStreams.copy(inputStream, outputStream);
		byte[] photoDownloadedAsAByteArray = outputStream.toByteArray();

		// getMagicMatch accepts  byte[],
		String contentType = Magic.getMagicMatch(photoDownloadedAsAByteArray).getMimeType();

		// it should be an image file
		assertEquals("image/png", contentType);

	}

	@Test(expected = G3GalleryException.class)
	public void getPhotoInputStream__exceptionNotExisting()
			throws G3GalleryException {
		Item item1 = itemClient.getItem(999999999);
		String url = item1.getUrl();
		InputStream inputStream = itemClient.getPhotoInputStream(url);
		assertNotNull(inputStream);

	}

	@Test
	public void updateItemPhoto() throws G3GalleryException {
		Entity photoToUpdate = new Entity();
		photoToUpdate.setId(createdPhotoId);
		photoToUpdate.setTitle("New Photo renamed !");
		// the photo entity is updated
		itemClient.updateItem(photoToUpdate);
		// we check it has be updated fetching it
		assertEquals("New Photo renamed !", itemClient.getItem(createdPhotoId)
				.getEntity().getTitle());
	}

	@Test(expected = G3ItemNotFoundException.class)
	public void deleteItemPhoto() throws G3GalleryException {
		// we delete the item
		itemClient.deleteItem(createdPhotoId);
		// we then expect it not to exist anymore !--> ItemNotFoundException !
		itemClient.getItem(createdPhotoId);
	}

	@Test
	/**
	 * tests that if we do not provide an username, it should not try to login and get an api key
	 * 
	 */
	public void noNeedToLoginIfNoUsernameNorPassword()
			throws G3GalleryException {
		itemClient.setUsername(null);
		itemClient.setExistingApiKey(null);
		List<Item> albumAndSubAlbums = itemClient.getAlbumAndSubAlbums(1);
		assertNotNull(albumAndSubAlbums);
		assertNotSame(0, albumAndSubAlbums.size());
		assertNull(itemClient.getExistingApiKey());

	}

	@AfterClass
	public static void tearDown() throws G3GalleryException {
		G3Client itemClient = new G3Client(galleryUrl, "Unit Test");
		itemClient.setUsername(username);
		itemClient.setPassword(password);
		// first we list all the items in the secret album
		Item g2AndroidSecretAlbumItem = itemClient
				.getItem(G2ANDROID_SECRET_ALBUM);
		// and then we delete each of them, to clean up the secret album
		for (String member : g2AndroidSecretAlbumItem.getMembers()) {
			int itemId = ItemUtils.getItemIdFromUrl(member);
			itemClient.deleteItem(itemId);
		}

	}

}
