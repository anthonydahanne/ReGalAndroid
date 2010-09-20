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
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import net.dahanne.gallery3.client.business.exceptions.ItemBadRequestException;
import net.dahanne.gallery3.client.business.exceptions.ItemForbiddenException;
import net.dahanne.gallery3.client.business.exceptions.ItemGalleryException;
import net.dahanne.gallery3.client.business.exceptions.ItemNotFoundException;
import net.dahanne.gallery3.client.model.Entity;
import net.dahanne.gallery3.client.model.Item;
import net.dahanne.gallery3.client.utils.ItemUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ItemClientImpl implements ItemRestClient {

	private static final String X_GALLERY_REQUEST_KEY = "X-Gallery-Request-Key";
	private static final String X_GALLERY_REQUEST_METHOD = "X-Gallery-Request-Method";
	private static final String GET = "get";
	private static final String DELETE = "delete";
	private static final String POST = "post";

	private static final String INDEX_PHP_REST_ITEM = "index.php/rest/item/";
	private final String galleryItemUrl;
	private String existingApiKey;
	private String password;
	private String username;

	public ItemClientImpl(String galleryUrl) {
		this.galleryItemUrl = galleryUrl;
	}

	public Item getItem(int itemId, boolean useExistingApiKey)
			throws ItemGalleryException {
		Item item = null;
		String stringResult = sendHttpPostRequest(INDEX_PHP_REST_ITEM + itemId,
				new ArrayList<NameValuePair>(), useExistingApiKey, GET);
		try {
			JSONObject jsonResult = (JSONObject) new JSONTokener(stringResult)
					.nextValue();
			item = ItemUtils.parseJSONToItem(jsonResult);
		} catch (JSONException e) {
			throw new ItemGalleryException(e.getMessage());
		}
		return item;
		// Item item;
		// HttpGet httpGet = new HttpGet(galleryItemUrl + INDEX_PHP_REST_ITEM
		// + itemId);
		// DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		// try {
		// HttpResponse response = defaultHttpClient.execute(httpGet);
		// int responseStatusCode = response.getStatusLine().getStatusCode();
		// switch (responseStatusCode) {
		// case HttpURLConnection.HTTP_OK:
		// break;
		// default:
		// throw new ItemGalleryException(
		// "Error ! The gallery returned http code "
		// + responseStatusCode);
		// }
		// HttpEntity responseEntity = response.getEntity();
		// responseEntity = new BufferedHttpEntity(responseEntity);
		//
		// BufferedReader reader = new BufferedReader(new InputStreamReader(
		// responseEntity.getContent()));
		// String line;
		// StringBuilder sb = new StringBuilder();
		// try {
		// while ((line = reader.readLine()) != null) {
		// sb.append(line);
		// sb.append("\n");
		// }
		// } finally {
		// reader.close();
		// }
		//
		// String plainResult = sb.toString();
		// JSONObject jsonResult = (JSONObject) new JSONTokener(plainResult)
		// .nextValue();
		// item = ItemUtils.parseJSONToItem(jsonResult);
		//
		// } catch (ClientProtocolException e) {
		// throw new ItemGalleryException(e.getMessage());
		// } catch (IOException e) {
		// throw new ItemGalleryException(e.getMessage());
		// } catch (JSONException e) {
		// throw new ItemGalleryException(e.getMessage());
		// }
		//
		// return item;
	}

	public String createItem(String albumName, String albumTitle, int parentItem)
			throws ItemGalleryException {

		String resultUrl;

		Entity entity = new Entity();
		entity.setName(albumName);
		entity.setTitle(albumTitle);
		NameValuePair nameValuePair;
		try {
			nameValuePair = new BasicNameValuePair("entity",
					ItemUtils.convertAlbumEntityToJSON(entity));
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(nameValuePair);
			resultUrl = sendHttpPostRequest(INDEX_PHP_REST_ITEM + parentItem,
					nameValuePairs, true, POST);

			resultUrl = ItemUtils.convertJsonStringToUrl(resultUrl);
		}

		catch (JSONException e) {
			throw new ItemGalleryException(e.getMessage());
		}
		return resultUrl;

	}

	private String sendHttpPostRequest(String appendToGalleryUrl,
			List<NameValuePair> nameValuePairs, boolean useExistingApiKey,
			String requestMethod) throws ItemGalleryException {

		String result;
		HttpClient defaultHttpClient = new DefaultHttpClient();

		// do we have to use the existingApiKey ?
		if (useExistingApiKey) {
			if (existingApiKey == null) {
				existingApiKey = getApiKey(username, password);
			}
		}

		try {
			HttpRequestBase httpMethod;
			if (POST.equals(requestMethod)) {
				httpMethod = new HttpPost(galleryItemUrl + appendToGalleryUrl);
				httpMethod.setHeader(X_GALLERY_REQUEST_METHOD, requestMethod);
				((HttpPost) httpMethod).setEntity(new UrlEncodedFormEntity(
						nameValuePairs));
			} 
			else if (DELETE.equals(requestMethod)) {
				httpMethod = new HttpGet(galleryItemUrl + appendToGalleryUrl);
				httpMethod.setHeader(X_GALLERY_REQUEST_METHOD, requestMethod);
			} 
			else {
				httpMethod = new HttpGet(galleryItemUrl + appendToGalleryUrl);
			}
			httpMethod.setHeader(X_GALLERY_REQUEST_KEY, existingApiKey);

			HttpResponse response = defaultHttpClient.execute(httpMethod);
			int responseStatusCode = response.getStatusLine().getStatusCode();

			switch (responseStatusCode) {
			case HttpURLConnection.HTTP_CREATED:
				break;
			case HttpURLConnection.HTTP_OK:
				break;
			case HttpURLConnection.HTTP_BAD_REQUEST:
				throw new ItemBadRequestException();
			case HttpURLConnection.HTTP_FORBIDDEN:
				throw new ItemForbiddenException();
			case HttpURLConnection.HTTP_NOT_FOUND:
				throw new ItemNotFoundException();
			default:
				throw new ItemGalleryException("HTTP code "
						+ responseStatusCode);
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

			result = sb.toString();

		} catch (ClientProtocolException e) {
			throw new ItemGalleryException(e.getMessage());
		} catch (IOException e) {
			throw new ItemGalleryException(e.getMessage());
		}
		return result;
	}

	public void updateItem(Item itemToUpdate) throws ItemGalleryException {
		// TODO Auto-generated method stub

	}

	public void deleteItem(int itemId) throws ItemGalleryException {
		sendHttpPostRequest(INDEX_PHP_REST_ITEM + itemId,
				new ArrayList<NameValuePair>(), true, DELETE);

	}

	public String getApiKey(String username, String password)
			throws ItemGalleryException {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("user", username));
		nameValuePairs.add(new BasicNameValuePair("password", password));

		String jsonResult = sendHttpPostRequest("index.php/rest",
				nameValuePairs, false, POST);

		String key = ItemUtils.convertJsonResultToApiKey(jsonResult);

		return key;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setExistingApiKey(String existingApiKey) {
		this.existingApiKey = existingApiKey;
	}

}
