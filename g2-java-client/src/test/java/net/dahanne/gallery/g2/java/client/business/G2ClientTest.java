/**
 *  g2-java-client, a Menalto Gallery2 Java Client API
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

package net.dahanne.gallery.g2.java.client.business;

import junit.framework.Assert;
import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.remote.GalleryConnectionException;
import net.dahanne.gallery.commons.remote.ImpossibleToLoginException;
import net.dahanne.gallery.g2.java.client.model.G2Album;
import net.dahanne.gallery.g2.java.client.model.G2Picture;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * These tests should run, provided that the gallery at http://g2.dahanne.net is
 * up and running !
 * 
 * @author Anthony Dahanne
 * 
 */
@Ignore
public class G2ClientTest extends Assert {
	private String galleryUrl;
	private String user;
	private String password;
	private G2Client g2ConnectionUtils;

	@Before
	public void init() {
		galleryUrl = "http://g2.dahanne.net/";
		user = "g2android";
		password = "g2android";
		g2ConnectionUtils = new G2Client( "Unit Test");
	}

	@Test
	public void fetchImagesTest() throws GalleryConnectionException {
		HashMap<String, String> fetchImages = g2ConnectionUtils.fetchImages(
				galleryUrl, 11);
		assertTrue("no pictures found",fetchImages.size() != 0);
		assertTrue("the string image_count could not be found",fetchImages.containsKey("image_count"));
		assertFalse("the value of image_count was 0",fetchImages.get("image_count").equals("0"));

		fetchImages = g2ConnectionUtils.fetchImages(galleryUrl, 9999);
		// just debug messages
		assertTrue("there were not 3 pictures",fetchImages.size() == 3);

	}

	@Test(expected = GalleryConnectionException.class)
	public void fetchImagesTest__exception() throws GalleryConnectionException {
		g2ConnectionUtils.fetchImages("http://something.thatmaynotexist.com",
				11);

	}


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
	
	@Test(expected=ImpossibleToLoginException.class)
	public void loginToGalleryTest__galleryDoesNotAnswerWithProperties__issue24() throws GalleryConnectionException {
		g2ConnectionUtils.loginToGallery("http://regalandroid.pixi.me",
				"g2android", "g2android");
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
    @Ignore
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
	public void extractG2PictureFromPropertiesTest() throws GalleryConnectionException {
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

		Collection<G2Picture> pictures = g2ConnectionUtils
				.extractG2PicturesFromProperties(fetchImages);
		G2Picture picture = pictures.iterator().next();
		assertEquals(150, picture.getThumbHeight());
		assertEquals("picture.jpg", picture.getTitle());
		assertEquals("12600",  picture.getResizedName());
		assertEquals(800, picture.getResizedWidth());
		assertEquals(2848, picture.getRawHeight());
		assertEquals("12598", picture.getName());
		assertEquals("12599", picture.getThumbName());
		assertEquals(150, picture.getThumbWidth());
		assertEquals(4288, picture.getRawWidth());
		assertEquals("caption", picture.getCaption());
		assertEquals("jpg", picture.getForceExtension());
		assertEquals(true, picture.isHidden());
//		assertEquals(45, picture.getImageClicks());

	}

	@Test
	public void extractAlbumFromPropertiesTest() throws GalleryConnectionException {
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

		Collection<G2Album> albums = g2ConnectionUtils.extractAlbumFromProperties(
				albumsProperties).values();
		G2Album album = albums.iterator().next();
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
		Map<Integer, G2Album> albums = new HashMap<Integer, G2Album>();
		// 0 does not exist (root's parent)
		// 10 is the gallery name (id 100)
		// 20 (id200), 3(id300), 4(id400) are 10's children
		// 5(id500),6(id600) are 20's children

		G2Album album = new G2Album();
		album.setName(6);
		album.setId(600);
		album.setParentName(20);
		albums.put(album.getId(), album);
		album = new G2Album();
		album.setName(3);
		album.setId(300);
		album.setParentName(10);
		albums.put(album.getId(), album);
		album = new G2Album();
		album.setName(4);
		album.setId(400);
		album.setParentName(10);
		albums.put(album.getId(), album);
		album = new G2Album();
		album.setName(10);
		album.setId(100);
		album.setParentName(0);
		albums.put(album.getId(), album);
		album = new G2Album();
		album.setName(5);
		album.setId(500);
		album.setParentName(20);
		albums.put(album.getId(), album);
		album = new G2Album();
		album.setName(20);
		album.setId(200);
		album.setParentName(10);
		albums.put(album.getId(), album);

		Album finalAlbum = g2ConnectionUtils.organizeAlbumsHierarchy(albums);

        // Depending on the Java version the ArrayList behaves different (ex. the sort order).
        // Sorting the list makes asserting a lot easier!
        Collections.sort(finalAlbum.getSubAlbums(), new Comparator<Album>() {
            public int compare(final Album albumA, final Album albumB) {
                return Integer.valueOf(albumA.getName()).compareTo(Integer.valueOf(albumB.getName()));
            }
        });

		assertEquals(10, finalAlbum.getName());
		assertEquals(3, finalAlbum.getSubAlbums().get(0).getName());
		assertEquals(4, finalAlbum.getSubAlbums().get(1).getName());
		assertEquals(20, finalAlbum.getSubAlbums().get(2).getName());
		assertEquals(5, finalAlbum.getSubAlbums().get(2).getSubAlbums().get(0).getName());
		assertEquals(6, finalAlbum.getSubAlbums().get(2).getSubAlbums().get(1).getName());

	}

}
