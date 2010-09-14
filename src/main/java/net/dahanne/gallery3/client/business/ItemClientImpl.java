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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.dahanne.gallery3.client.model.Item;
import net.dahanne.gallery3.client.utils.ItemUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ItemClientImpl implements ItemRestClient {

	private final String galleryItemUrl;

	public ItemClientImpl(String galleryUrl) {
		this.galleryItemUrl = galleryUrl+"index.php/rest/item/";
	}

	public Item getItem(int itemId) throws ItemGalleryException {
		Item item;
		HttpGet httpGet = new HttpGet(
				galleryItemUrl + itemId);
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		try {
			HttpResponse response = defaultHttpClient.execute(httpGet);
			int responseStatusCode = response.getStatusLine().getStatusCode();
			switch (responseStatusCode) {
			case 200:
				break;
			default:
				throw new ItemGalleryException(
				"Error ! The gallery returned http code "+responseStatusCode);
			}
			HttpEntity responseEntity = response.getEntity();
			responseEntity = new BufferedHttpEntity(responseEntity);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					responseEntity.getContent()));
			String line;
			StringBuilder sb = new StringBuilder();
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line);
					sb.append("\n");
				}
			} finally {
				reader.close();
			}

			String plainResult = sb.toString();
			JSONObject jsonResult = (JSONObject) new JSONTokener(plainResult)
					.nextValue();
			item = ItemUtils.parseJSONToItem(jsonResult);

		} catch (ClientProtocolException e) {
			throw new ItemGalleryException(e.getMessage());
		} catch (IOException e) {
			throw new ItemGalleryException(e.getMessage());
		} catch (JSONException e) {
			throw new ItemGalleryException(e.getMessage());
		}

		return item;
	}

	public void createItem(Item itemToCreate) throws ItemGalleryException {
		// TODO Auto-generated method stub

	}

	public void updateItem(Item itemToUpdate) throws ItemGalleryException {
		// TODO Auto-generated method stub

	}

	public void deleteItem(int itemId) throws ItemGalleryException {
		// TODO Auto-generated method stub

	}

}
