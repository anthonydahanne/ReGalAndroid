package net.dahanne.gallery3.client.business;

import javax.ws.rs.core.MediaType;

import net.dahanne.gallery3.client.model.Entity;
import net.dahanne.gallery3.client.model.Item;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

public class ItemRestClientImpl implements ItemRestClient {

	private String galleryUrl;
	private String apiKey;
	private Client client;

	public ItemRestClientImpl(String galleryUrl) {
		super();
		this.galleryUrl = galleryUrl;
	}

	public void createItem(Item itemToCreate) throws ItemGalleryException {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(galleryUrl);
		stringBuilder.append(itemToCreate.getEntity().getId());
		WebResource r = client.resource(stringBuilder.toString());
		try {
			Entity entity =  itemToCreate.getEntity();
			
			Builder accept = r.accept(MediaType.APPLICATION_JSON_TYPE).entity(itemToCreate, MediaType.APPLICATION_JSON_TYPE);
			//Optional auth
			if(apiKey!=null){
				accept = accept.header("X-Gallery-Request-Key", apiKey);
			}
			Item response = accept.	post(Item.class);
			System.out.println(response);
		} catch (Exception e) {
			throw new ItemGalleryException("getItem() failed with id :"
					+ itemToCreate.getEntity().getId() + " " + e.getMessage());
		}

	}

	public void deleteItem(int itemId) throws ItemGalleryException {
		// TODO Auto-generated method stub

	}

	public Item getItem(int itemId) throws ItemGalleryException {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(galleryUrl);
		stringBuilder.append(itemId);
		WebResource r = client.resource(stringBuilder.toString());
		try {
			Builder accept = r.accept(MediaType.APPLICATION_JSON_TYPE);
			//Optional auth
			if(apiKey!=null){
				accept = accept.header("X-Gallery-Request-Key", apiKey);
			}
			Item response = accept.get(	Item.class);
			return response;
		} catch (Exception e) {
			throw new ItemGalleryException("getItem() failed with id :"
					+ itemId + " " + e.getMessage());
		}
	}

	public void updateItem(Item itemToUpdate) throws ItemGalleryException {
		// TODO Auto-generated method stub

	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Client getClient() {
		return client;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiKey() {
		return apiKey;
	}

}
