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


public class ItemClientImplTest {

	
	

	@Test
	public void getItemTest__album() throws ItemGalleryException{
		
		ItemRestClient itemClient =  new ItemClientImpl("http://g3.dahanne.net/");
		Item item1 = itemClient.getItem(1);
		assertEquals("Gallery", item1.getEntity().getTitle());
		
		
//		Client client = Client.create();
//		ItemRestClientImpl itemRest = new ItemRestClientImpl("http://g3.dahanne.net/index.php/rest/item/");
//		itemRest.setClient(client);
//		Item item = itemRest.getItem(1);
//		assertEquals(1, item.getEntity().getId());
//		assertEquals("http://g3.dahanne.net/index.php/rest/item/1", item.getUrl());
		
	}
	
	
	
}
