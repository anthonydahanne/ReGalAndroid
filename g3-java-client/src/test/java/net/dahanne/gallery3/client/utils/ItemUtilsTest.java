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

package net.dahanne.gallery3.client.utils;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import net.dahanne.gallery3.client.model.Entity;
import net.dahanne.gallery3.client.model.Item;
import net.dahanne.gallery3.client.model.RelationShips;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ItemUtilsTest {
	
	
	@Test
	public void parseJSONToMultipleItems() throws IOException, JSONException {
		URL resource = Resources.getResource("tree-album-1.json");
		String string = Resources.toString(resource, Charsets.UTF_8);
		JSONObject jsonResult = (JSONObject) new JSONTokener(string)
		.nextValue();
		List<Item> items = ItemUtils.parseJSONToMultipleItems(jsonResult);
		assertEquals("http://192.168.1.60:8081/gallery3/index.php/rest/item/1264",items.get(0).getUrl() );
		assertEquals(1311332015,items.get(0).getEntity().getUpdated() );
		assertEquals("http://192.168.1.60:8081/gallery3/index.php/rest/item/1265",items.get(1).getUrl() );
		assertEquals(1311332144,items.get(1).getEntity().getUpdated() );
		assertEquals("http://192.168.1.60:8081/gallery3/index.php/rest/item/1661",items.get(2).getUrl() );
		assertEquals(1319238331,items.get(2).getEntity().getUpdated() );
	}
	
	
	
	@Test
	public void parseJSONTest__album() throws IOException, JSONException {
		URL resource = Resources.getResource("get-album-1.json");
		String string = Resources.toString(resource, Charsets.UTF_8);
		JSONObject jsonResult = (JSONObject) new JSONTokener(string)
				.nextValue();
		Item item = ItemUtils.parseJSONToItem(jsonResult);
		assertEquals("http://g3.dahanne.net/index.php/rest/item/1",item.getUrl());
		
		Entity entity = item.getEntity();
		assertEquals(1,entity.getId());
		assertEquals(0,entity.getCaptured());
		assertEquals(1276227460,entity.getCreated());
		assertEquals("",entity.getDescription());
		assertEquals(0,entity.getHeight());
		assertEquals(1,entity.getLevel());
		assertEquals(null,entity.getMimeType());
		assertEquals(null,entity.getName());
		assertEquals(2,entity.getOwnerId());
		assertEquals(0f,entity.getRandKey(),0);
		assertEquals(0,entity.getResizeHeight());
		assertEquals(0,entity.getResizeWidth());
		assertEquals(null,entity.getSlug());
		assertEquals("weight",entity.getSortColumn());
		assertEquals("ASC",entity.getSortOrder());
		assertEquals(200,entity.getThumbHeight());
		assertEquals(150,entity.getThumbWidth());
		assertEquals("Gallery",entity.getTitle());
		assertEquals("album",entity.getType());
		assertEquals(1276227718,entity.getUpdated());
		assertEquals(8,entity.getViewCount());
		assertEquals(0,entity.getWidth());
		assertEquals(1,entity.getView1());
		assertEquals(1,entity.getView2());
		assertEquals("http://g3.dahanne.net/index.php/rest/item/2",entity.getAlbumCover());
		assertEquals("http://g3.dahanne.net/index.php/",entity.getWebUrl());
		assertEquals("http://g3.dahanne.net/index.php/rest/data/1?size=thumb",entity.getThumbUrl());
		assertEquals(17151,entity.getThumbSize());
		assertEquals("http://g3.dahanne.net/var/thumbs//.album.jpg?m=1276227718",entity.getThumbUrlPublic());
		assertEquals(false,entity.isCanEdit());
		
		RelationShips relationShips =  item.getRelationships();
		assertEquals("http://g3.dahanne.net/index.php/rest/item_tags/1",relationShips.getTags().getUrl());
		assertEquals(new HashSet<String>(),relationShips.getTags().getMembers());
		assertEquals("http://g3.dahanne.net/index.php/rest/item_comments/1",relationShips.getComments().getUrl());
		
		Collection<String> members =  new HashSet<String>();
		members.add("http://g3.dahanne.net/index.php/rest/item/2");
		members.add("http://g3.dahanne.net/index.php/rest/item/3");
		assertEquals(members, item.getMembers());
		
		
	}
	
	
	
	@Test
	public void parseJSONTest__secretalbum() throws IOException, JSONException {
		URL resource = Resources.getResource("get-album-11.json");
		String string = Resources.toString(resource, Charsets.UTF_8);
		JSONObject jsonResult = (JSONObject) new JSONTokener(string)
				.nextValue();
		Item item = ItemUtils.parseJSONToItem(jsonResult);
		assertEquals("http://g3.dahanne.net/index.php/rest/item/11",item.getUrl());
		
		Entity entity = item.getEntity();
		assertEquals(11,entity.getId());
		assertEquals(0,entity.getCaptured());
		assertEquals(1284818165,entity.getCreated());
		assertEquals("",entity.getDescription());
		assertEquals(0,entity.getHeight());
		assertEquals(2,entity.getLevel());
		assertEquals(null,entity.getMimeType());
		assertEquals("G2AndroidSecretAlbum",entity.getName());
		assertEquals(2,entity.getOwnerId());
		assertEquals(0.952582f,entity.getRandKey(),0.000001f);
		assertEquals(0,entity.getResizeHeight());
		assertEquals(0,entity.getResizeWidth());
		assertEquals("G2AndroidSecretAlbum",entity.getSlug());
		assertEquals("created",entity.getSortColumn());
		assertEquals("ASC",entity.getSortOrder());
		assertEquals(0,entity.getThumbHeight());
		assertEquals(0,entity.getThumbWidth());
		assertEquals("G2AndroidSecretAlbum",entity.getTitle());
		assertEquals("album",entity.getType());
		assertEquals(1284830781,entity.getUpdated());
		assertEquals(6,entity.getViewCount());
		assertEquals(0,entity.getWidth());
		assertEquals(0,entity.getView1());
		assertEquals(0,entity.getView2());
		assertEquals(ItemUtils.getItemIdFromUrl("http://g3.dahanne.net/index.php/rest/item/1"),new Integer(entity.getParent()));
		assertEquals("http://g3.dahanne.net/index.php/G2AndroidSecretAlbum",entity.getWebUrl());
		assertEquals(true,entity.isCanEdit());
		
		RelationShips relationShips =  item.getRelationships();
		assertEquals("http://g3.dahanne.net/index.php/rest/item_tags/11",relationShips.getTags().getUrl());
		assertEquals(new HashSet<String>(),relationShips.getTags().getMembers());
		assertEquals("http://g3.dahanne.net/index.php/rest/item_comments/11",relationShips.getComments().getUrl());
		
		Collection<String> members =  new HashSet<String>();
		members.add("http://g3.dahanne.net/index.php/rest/item/13");
		members.add("http://g3.dahanne.net/index.php/rest/item/14");
		members.add("http://g3.dahanne.net/index.php/rest/item/15");
		members.add("http://g3.dahanne.net/index.php/rest/item/16");
		members.add("http://g3.dahanne.net/index.php/rest/item/18");
		members.add("http://g3.dahanne.net/index.php/rest/item/20");
		members.add("http://g3.dahanne.net/index.php/rest/item/22");
		members.add("http://g3.dahanne.net/index.php/rest/item/25");
		members.add("http://g3.dahanne.net/index.php/rest/item/26");
		members.add("http://g3.dahanne.net/index.php/rest/item/28");
		members.add("http://g3.dahanne.net/index.php/rest/item/30");
		members.add("http://g3.dahanne.net/index.php/rest/item/32");
		members.add("http://g3.dahanne.net/index.php/rest/item/34");
		members.add("http://g3.dahanne.net/index.php/rest/item/35");
		assertEquals(members, item.getMembers());
		
		
	}
	
	
	
	
	
	@Test
	public void parseJSONTest__photo() throws IOException, JSONException {
		URL resource = Resources.getResource("get-photo-2.json");
		String string = Resources.toString(resource, Charsets.UTF_8);
		JSONObject jsonResult = (JSONObject) new JSONTokener(string)
				.nextValue();
		Item item = ItemUtils.parseJSONToItem(jsonResult);
		assertEquals("http://g3.dahanne.net/index.php/rest/item/2",item.getUrl());
		
		Entity entity = item.getEntity();
		assertEquals(2,entity.getId());
		assertEquals(1272750491,entity.getCaptured());
		assertEquals(1276227630,entity.getCreated());
		assertEquals("La March\u00e9 bon secours \u00e0 Montr\u00e9al",entity.getDescription());
		assertEquals(3072,entity.getHeight());
		assertEquals(2,entity.getLevel());
		assertEquals("image/jpeg",entity.getMimeType());
		assertEquals("marche-bonsecours.JPG",entity.getName());
		assertEquals(2,entity.getOwnerId());
		assertEquals(0.451528,entity.getRandKey(),0.000001f);
		assertEquals(640,entity.getResizeHeight());
		assertEquals(480,entity.getResizeWidth());
		assertEquals("marche-bonsecours",entity.getSlug());
		assertEquals("created",entity.getSortColumn());
		assertEquals("ASC",entity.getSortOrder());
		assertEquals(200,entity.getThumbHeight());
		assertEquals(150,entity.getThumbWidth());
		assertEquals("March\u00e9 Bon secours",entity.getTitle());
		assertEquals("photo",entity.getType());
		assertEquals(1276229274,entity.getUpdated());
		assertEquals(60,entity.getViewCount());
		assertEquals(2304,entity.getWidth());
		assertEquals(1,entity.getView1());
		assertEquals(1,entity.getView2());
		assertEquals(ItemUtils.getItemIdFromUrl("http://g3.dahanne.net/index.php/rest/item/1"),new Integer(entity.getParent()));
		assertEquals("http://g3.dahanne.net/index.php/marche-bonsecours",entity.getWebUrl());

		assertEquals("http://g3.dahanne.net/index.php/rest/data/2?size=full",entity.getFileUrl());
		assertEquals(675745,entity.getFileSize());
		assertEquals("http://g3.dahanne.net/var/albums/marche-bonsecours.JPG?m=1276229274",entity.getFileUrlPublic());
		
		assertEquals("http://g3.dahanne.net/index.php/rest/data/2?size=resize",entity.getResizeUrl());
		assertEquals(58309,entity.getResizeSize());
		assertEquals("http://g3.dahanne.net/var/resizes/marche-bonsecours.JPG?m=1276229274",entity.getResizeUrlPublic());
		
		
		assertEquals("http://g3.dahanne.net/index.php/rest/data/2?size=thumb",entity.getThumbUrl());
		assertEquals(17151,entity.getThumbSize());
		assertEquals("http://g3.dahanne.net/var/thumbs/marche-bonsecours.JPG?m=1276229274",entity.getThumbUrlPublic());
		assertEquals(false,entity.isCanEdit());
		
		RelationShips relationShips =  item.getRelationships();
		assertEquals("http://g3.dahanne.net/index.php/rest/item_tags/2",relationShips.getTags().getUrl());
		Collection<String> members =  new HashSet<String>();
		members.add("http://g3.dahanne.net/index.php/rest/tag_item/6,2");
		members.add("http://g3.dahanne.net/index.php/rest/tag_item/7,2");
		members.add("http://g3.dahanne.net/index.php/rest/tag_item/8,2");
		members.add("http://g3.dahanne.net/index.php/rest/tag_item/9,2");
		members.add("http://g3.dahanne.net/index.php/rest/tag_item/10,2");
		members.add("http://g3.dahanne.net/index.php/rest/tag_item/11,2");
		members.add("http://g3.dahanne.net/index.php/rest/tag_item/12,2");
		assertEquals(members, relationShips.getTags().getMembers());
		assertEquals("http://g3.dahanne.net/index.php/rest/item_comments/2",relationShips.getComments().getUrl());
		
		
		
	}
	
	
	
	@Test
	public void convertAlbumEntityToJSON() throws JSONException{
		Entity albumEntity =  new Entity();
		albumEntity.setTitle("This is my Sample Album");
		albumEntity.setName("Sample Album");
		
		String convertEntityToJSON = ItemUtils.convertAlbumEntityToJSON(albumEntity);

        assertTrue("invalid JSON", new JSONObject(convertEntityToJSON) != null);
        assertTrue("missing title attribute", convertEntityToJSON.contains("\"title\":\"This is my Sample Album\""));
        assertTrue("missing name attribute", convertEntityToJSON.contains("\"name\":\"Sample Album\""));
        assertTrue("missing type attribute", convertEntityToJSON.contains("\"type\":\"album\""));
	}
	
	@Test
	public void convertItemToNameValuePair(){
		
		
		
		Item item =  new Item();
		Entity albumEntity =  new Entity();
		albumEntity.setTitle("New Album");
		albumEntity.setName("AlbumName");
		item.setEntity(albumEntity );
		
		String value = "{\"title\":\"This is my Sample Album\",\"name\":\"Sample Album\",\"type\":\"album\"}";
		BasicNameValuePair basicNameValuePair = new BasicNameValuePair("entity",value);
		
		assertEquals(basicNameValuePair, ItemUtils.convertJSONStringToNameValuePair(value));
//		
//		
//		List<NameValuePair>  nameValuePairs = ItemUtils.convertItemToNameValuePairs(item);
		
		
		
	}
	
	@Test
	public void convertJsonStringToApiKey() {
		String expectedKey = "e3450cdda082e6a2bddf5114a2bcc14d";
		String jsonResult="\"e3450cdda082e6a2bddf5114a2bcc14d\n\"";
		String key = ItemUtils.convertJsonResultToApiKey(jsonResult);
		assertEquals(expectedKey, key);
	}
	
	
	@Test
	public void convertJsonStringToUrl() throws JSONException{
		String jsonResult="{\"url\":\"http:\\/\\/g3.dahanne.net\\/index.php\\/rest\\/item\\/34\"}";
		String expectedString = "http://g3.dahanne.net/index.php/rest/item/34";
		String urlString = ItemUtils.convertJsonStringToUrl(jsonResult);
		assertEquals(expectedString, urlString);
	}
	
	@Test
	public void parseJSONTest_issue32() throws IOException, JSONException{
		//this test just tests an exception is not thrown; a smoke test in other words
		URL resource = Resources.getResource("get-album-bug32_owner-id-null.json");
		String string = Resources.toString(resource, Charsets.UTF_8);
		JSONObject jsonResult = (JSONObject) new JSONTokener(string)
				.nextValue();
		Item item = ItemUtils.parseJSONToItem(jsonResult);
		
		Entity entity = item.getEntity();
		
		RelationShips relationShips =  item.getRelationships();
		
	}
	
	@Test
	public void parseJSONTest_issue38() throws IOException, JSONException{
		//this test just tests an exception is not thrown; a smoke test in other words
		URL resource = Resources.getResource("get-albums-no-relationships-38.json");
		String string = Resources.toString(resource, Charsets.UTF_8);
		JSONObject jsonResult = (JSONObject) new JSONTokener(string)
				.nextValue();
		Item item = ItemUtils.parseJSONToItem(jsonResult);
		
		Entity entity = item.getEntity();
		
		RelationShips relationShips =  item.getRelationships();
		
	}
	@Test
	public void parseJSONTest_issue83() throws IOException, JSONException{
		//this test just tests an exception is not thrown; a smoke test in other words
		URL resource = Resources.getResource("get-albums-no-comments-issue82.json");
		String string = Resources.toString(resource, Charsets.UTF_8);
		JSONObject jsonResult = (JSONObject) new JSONTokener(string)
				.nextValue();
		Item item = ItemUtils.parseJSONToItem(jsonResult);
		
		Entity entity = item.getEntity();
		
		RelationShips relationShips =  item.getRelationships();
		
	}
	
}
