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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Map;

import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.remote.GalleryConnectionException;
import net.dahanne.gallery.commons.remote.RemoteGallery;

import org.junit.Ignore;
import org.junit.Test;
@Ignore
public class G3ConnectionTest {

	private static final int G2ANDROID_SECRET_ALBUM = 11;
	private static final String galleryUrl = "http://g3.dahanne.net/";
	private static final String username = "g2android";
	private static final String password = "g2android";
	private G3Client itemClient;
	private static int createdAlbumId;
	private static int createdPhotoId;
	
	@Test
	public void testCreateNewAlbum() {
		fail("Not yet implemented");
	}

	@Test
	public void testFetchAlbums() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindAlbumFromAlbumName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllAlbums() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetInputStreamFromUrl() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPicturesFromAlbum() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSessionCookies() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEmbeddedGallery() {
		fail("Not yet implemented");
	}

	@Test
	public void testLoginToGallery() throws GalleryConnectionException {
//		RemoteGallery connection = new G3Connection(galleryUrl);
//		connection.loginToGallery(galleryUrl,username, password);
//		Map<Integer, Album> allAlbums = connection.getAllAlbums(galleryUrl);
//		assertNotNull(allAlbums);
	}

	@Test
	public void testRetrieveRootAlbumAndItsHierarchy() {
		fail("Not yet implemented");
	}

	@Test
	public void testSendImageToGallery() {
		fail("Not yet implemented");
	}

}
