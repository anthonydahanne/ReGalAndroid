package net.dahanne.gallery3.client.business;

import net.dahanne.gallery3.client.model.Item;

public interface ItemRestClient {

	Item getItem(int itemId)throws ItemGalleryException;
	void createItem(Item itemToCreate) throws ItemGalleryException;
	void updateItem(Item itemToUpdate)throws ItemGalleryException;
	void deleteItem(int itemId)throws ItemGalleryException;
	
}
