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
import net.dahanne.gallery3.client.model.Item;

import org.junit.Test;

import com.sun.jersey.api.client.Client;


public class ItemRestClientImplTest {

	
	
//	@Test
//	public void getItemTest__photo() throws ItemGalleryException{
//		
////		LowLevelAppDescriptor descriptor =  new LowLevelAppDescriptor.Builder("net.dahanne.g3restapi.business").build();
////		InMemoryTestContainerFactory containerFactory =  new InMemoryTestContainerFactory();
////		TestContainer create = containerFactory.create(URI.create("http://g3.dahanne.net/index.php/rest/"), descriptor);
////		Client client = create.getClient();
//		Client client = Client.create();
//		ItemRestClientImpl itemRest = new ItemRestClientImpl("http://g3.dahanne.net/index.php/rest/item/");
//		itemRest.setClient(client);
//		Item item = itemRest.getItem(2);
//		assertEquals(2, item.getEntity().getId());
//		assertEquals("http://g3.dahanne.net/index.php/rest/item/2", item.getUrl());
//		assertEquals("http://g3.dahanne.net/var/albums/marche-bonsecours.JPG?m=1276229274", item.getEntity().getFileUrl());
//		
//	}
//	
//	@Test
//	public void getItemTest__photo__withauth() throws ItemGalleryException{
//		
//		Client client = Client.create();
//		ItemRestClientImpl itemRest = new ItemRestClientImpl("http://g3.dahanne.net/index.php/rest/item/");
//		itemRest.setClient(client);
//		itemRest.setApiKey("971122b4879049c13d03d822f9f14ad2");
//		Item item = itemRest.getItem(2);
//		assertEquals(2, item.getEntity().getId());
//		assertEquals("http://g3.dahanne.net/index.php/rest/item/2", item.getUrl());
//		assertEquals("http://g3.dahanne.net/var/albums/marche-bonsecours.JPG?m=1276229274", item.getEntity().getFileUrl());
//		
//	}
	
	
	@Test
	public void getItemTest__album() throws ItemGalleryException{
		
		Client client = Client.create();
		ItemRestClientImpl itemRest = new ItemRestClientImpl("http://g3.dahanne.net/index.php/rest/item/");
		itemRest.setClient(client);
		Item item = itemRest.getItem(1);
		assertEquals(1, item.getEntity().getId());
		assertEquals("http://g3.dahanne.net/index.php/rest/item/1", item.getUrl());
		assertEquals("Gallery", item.getEntity().getTitle());
		
	}
	
	
//	@Test
//	public void createItemTest__album() throws ItemGalleryException{
//		Client client = Client.create();
//		ItemRestClientImpl itemRest = new ItemRestClientImpl("http://g3.dahanne.net/index.php/rest/item/");
//		itemRest.setClient(client);
//		itemRest.setApiKey("971122b4879049c13d03d822f9f14ad2");
//		Item itemToCreate =  new Item();
//		Entity entity = new Entity();
//		entity.setTitle("This is my Sample Album" );
//		entity.setName("Sample Album");
//		entity.setId(1);
//		itemToCreate.setEntity(entity);
//		itemRest.createItem(itemToCreate );
//		
//		
//		
//	}
	
	
}
