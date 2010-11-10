/**
 *  Gallery3-java-client
 *  URLs: http://github.com/anthonydahanne/g3-java-client , http://blog.dahanne.net
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

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.dahanne.gallery3.client.business.exceptions.G3BadRequestException;
import net.dahanne.gallery3.client.business.exceptions.G3ForbiddenException;
import net.dahanne.gallery3.client.business.exceptions.G3GalleryException;
import net.dahanne.gallery3.client.business.exceptions.G3ItemNotFoundException;
import net.dahanne.gallery3.client.model.Entity;
import net.dahanne.gallery3.client.model.Item;
import net.dahanne.gallery3.client.utils.ItemUtils;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class G3ClientTest {

	private static final int G2ANDROID_SECRET_ALBUM = 11;
	private static final String galleryUrl = "http://g3.dahanne.net/";
	private static final String username = "g2android";
	private static final String password = "g2android";
	private G3Client itemClient;
	private static int createdAlbumId;
	private static int createdPhotoId;

	@Before
	public void setUp(){
		itemClient = new G3Client(galleryUrl);
		itemClient.setUsername(username);
		itemClient.setPassword(password);
		
	}
	
	@Test
	public void getItemTest__album() throws G3GalleryException {

		Item item1 = itemClient.getItem(1);
		assertEquals("Gallery", item1.getEntity().getTitle());
	}
	
	@Test
	public void getApiKey() throws G3GalleryException{
		String apiKey = itemClient.getApiKey("g2android", "g2android");
		assertNotNull(apiKey);
	}
	

	@Test
	public void createAlbum() throws G3GalleryException {
		Entity  albumToCreate =  new Entity();
		albumToCreate.setName("AlbumName" + System.currentTimeMillis());
		albumToCreate.setTitle("New Album");
		albumToCreate.setParent(G2ANDROID_SECRET_ALBUM);
		createdAlbumId = itemClient.createItem(albumToCreate,null );
		assertNotNull(createdAlbumId);

	}
	@Ignore
	@Test(expected=G3BadRequestException.class)
	public void createAlbum__already_exists() throws G3GalleryException {
		long albumSuffix = System.currentTimeMillis();
		Entity  albumToCreate =  new Entity();
		albumToCreate.setName("AlbumName"+albumSuffix);
		albumToCreate.setTitle("New Album");
		albumToCreate.setParent(G2ANDROID_SECRET_ALBUM);
		
		
		itemClient.createItem(albumToCreate,null);

		//do it again -->EXCEPTION ! Twice the same album name in the same parent !
		itemClient.createItem(albumToCreate,null);

	}
	
	@Test(expected=G3ForbiddenException.class)
	public void createAlbum__bad_api_key() throws G3GalleryException {
		itemClient.setExistingApiKey("plouf");
		Entity  albumToCreate =  new Entity();
		albumToCreate.setName("AlbumName" + System.currentTimeMillis());
		albumToCreate.setTitle("New Album");
		albumToCreate.setParent(G2ANDROID_SECRET_ALBUM);
		itemClient.createItem(albumToCreate,null);

	}
	
	
	@Test
	public void updateItemAlbum() throws G3GalleryException{
		Entity albumToUpdate = new Entity();
		albumToUpdate.setId(createdAlbumId);
		albumToUpdate.setTitle("New Album renamed !");
		//the album is updated
		itemClient.updateItem(albumToUpdate );
		//we check it has be updated fetching it
		assertEquals("New Album renamed !",itemClient.getItem(createdAlbumId).getEntity().getTitle());
	}
	
	
	@Test
	public void getSubAlbumsTest() throws G3GalleryException{
		List<Item> subAlbums = itemClient.getAlbumAndSubAlbums(11);
		boolean foundRecentlyAddedAlbum =  false;
		for (Item album : subAlbums) {
			if(album.getEntity().getId()==createdAlbumId){
				foundRecentlyAddedAlbum=true;
			}
		}
		assertTrue(foundRecentlyAddedAlbum);
	}
	
	
	@Test(expected=G3ItemNotFoundException.class)
	public void deleteItemAlbum() throws G3GalleryException{
		//we delete the item
		itemClient.deleteItem(createdAlbumId );
		//we then expect it not to exist anymore !--> ItemNotFoundException !
		itemClient.getItem(createdAlbumId);
	}
	
	@Test
	public void addPhoto() throws IOException, G3GalleryException{
		Entity  photoToCreate =  new Entity();
		photoToCreate.setName("logo.png");
		photoToCreate.setTitle("New Photo");
		photoToCreate.setParent(G2ANDROID_SECRET_ALBUM);
		File photoFile = new File( this.getClass().getClassLoader().getResource("logo.png").getPath());
		createdPhotoId = itemClient.createItem(photoToCreate, photoFile);
		Item item = itemClient.getItem(createdPhotoId);
		assertEquals("New Photo", item.getEntity().getTitle());
		assertEquals("logo.png", item.getEntity().getName());
		assertEquals(55, item.getEntity().getHeight());
		assertEquals(55, item.getEntity().getWidth());
		
	}
	
	@Test
	public void updateItemPhoto() throws G3GalleryException{
		Entity photoToUpdate = new Entity();
		photoToUpdate.setId(createdPhotoId);
		photoToUpdate.setTitle("New Photo renamed !");
		//the photo entity is updated
		itemClient.updateItem(photoToUpdate );
		//we check it has be updated fetching it
		assertEquals("New Photo renamed !",itemClient.getItem(createdPhotoId).getEntity().getTitle());
	}
	
	@Test(expected=G3ItemNotFoundException.class)
	public void deleteItemPhoto() throws G3GalleryException{
		//we delete the item
		itemClient.deleteItem(createdPhotoId );
		//we then expect it not to exist anymore !--> ItemNotFoundException !
		itemClient.getItem(createdPhotoId);
	}
	
	@Test
	/**
	 * tests that if we do not provide an username, it should not try to login and get an api key
	 * 
	 */
	public void noNeedToLoginIfNoUsernameNorPassword() throws G3GalleryException{
		itemClient.setUsername(null);
		itemClient.setExistingApiKey(null);
		List<Item> albumAndSubAlbums = itemClient.getAlbumAndSubAlbums(1);
		assertNotNull(albumAndSubAlbums);
		assertNotEquals(0, albumAndSubAlbums.size());
		assertNull(itemClient.getExistingApiKey());
		
	}
	
	
	
	private void assertNotEquals(int i, int size) {
		// TODO Auto-generated method stub
		
	}

	@AfterClass
	public  static void  tearDown() throws G3GalleryException{
		G3Client itemClient = new G3Client(galleryUrl);
		itemClient.setUsername(username);
		itemClient.setPassword(password);
		//first we list all the items in the secret album
		Item g2AndroidSecretAlbumItem = itemClient.getItem(G2ANDROID_SECRET_ALBUM);
		//and then we delete each of them, to clean up the secret album
		for (String member : g2AndroidSecretAlbumItem.getMembers()) {
			int itemId=  ItemUtils.getItemIdFromUrl(member);
			itemClient.deleteItem(itemId );
		}
		
	}
	

}
