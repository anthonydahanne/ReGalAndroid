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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import net.dahanne.gallery3.client.business.exceptions.ItemBadRequestException;
import net.dahanne.gallery3.client.business.exceptions.ItemForbiddenException;
import net.dahanne.gallery3.client.business.exceptions.ItemGalleryException;
import net.dahanne.gallery3.client.business.exceptions.ItemNotFoundException;
import net.dahanne.gallery3.client.model.Item;
import net.dahanne.gallery3.client.utils.ItemUtils;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class ItemClientImplTest {

	private static final int G2ANDROID_SECRET_ALBUM = 11;
	private static final String galleryUrl = "http://g3.dahanne.net/";
	private static final String username = "g2android";
	private static final String password = "g2android";
	private ItemClientImpl itemClient;
	private static  String createdAlbumUrl;

	@Before
	public void setUp(){
		itemClient = new ItemClientImpl(galleryUrl);
		itemClient.setUsername(username);
		itemClient.setPassword(password);
		
	}
	
	@Test
	public void getItemTest__album() throws ItemGalleryException {

		Item item1 = itemClient.getItem(1,false);
		assertEquals("Gallery", item1.getEntity().getTitle());
	}
	
	@Test
	public void getApiKey() throws ItemGalleryException{
		String apiKey = itemClient.getApiKey("g2android", "g2android");
		assertNotNull(apiKey);
	}
	

	@Test
	public void createAlbum() throws ItemGalleryException {
		createdAlbumUrl = itemClient.createItem("AlbumName" + System.currentTimeMillis(),"New Album", G2ANDROID_SECRET_ALBUM);
		assertNotNull(createdAlbumUrl);

	}
	
	@Test(expected=ItemBadRequestException.class)
	public void createAlbum__already_exists() throws ItemGalleryException {
		long albumSuffix = System.currentTimeMillis();
		
		itemClient.createItem("AlbumName"+albumSuffix,"New Album", G2ANDROID_SECRET_ALBUM);

		//do it again -->EXCEPTION ! Twice the same album name in the same parent !
		itemClient.createItem("AlbumName"+albumSuffix,"New Album", G2ANDROID_SECRET_ALBUM);

	}
	
	@Test(expected=ItemForbiddenException.class)
	public void createAlbum__bad_api_key() throws ItemGalleryException {
		itemClient.setExistingApiKey("plouf");
		itemClient.createItem("AlbumName" + System.currentTimeMillis(),"New Album", G2ANDROID_SECRET_ALBUM);

	}
	
	@Test(expected=ItemNotFoundException.class)
	public void deleteItem() throws ItemGalleryException{
		int itemId =  ItemUtils.getItemIdFromUrl(createdAlbumUrl);
		//we delete the item
		itemClient.deleteItem(itemId );
		//we then expect it not to exist anymore !--> ItemNotFoundException !
		itemClient.getItem(itemId, true);
	}

	
	
	@AfterClass
	public  static void  tearDown() throws ItemGalleryException{
		ItemClientImpl itemClient = new ItemClientImpl(galleryUrl);
		itemClient.setUsername(username);
		itemClient.setPassword(password);
		//first we list all the items in the secret album
		Item g2AndroidSecretAlbumItem = itemClient.getItem(G2ANDROID_SECRET_ALBUM, true);
		//and then we delete each of them, to clean up the secret album
		for (String member : g2AndroidSecretAlbumItem.getMembers()) {
			int itemId=  ItemUtils.getItemIdFromUrl(member);
			itemClient.deleteItem(itemId );
		}
		
	}
	

}
