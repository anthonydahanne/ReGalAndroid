package net.dahanne.gallery3.client.business;

import static org.junit.Assert.assertEquals;
import net.dahanne.gallery3.client.business.ItemGalleryException;
import net.dahanne.gallery3.client.business.ItemRestClientImpl;
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
