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
package net.dahanne.gallery3.client.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.dahanne.gallery3.client.model.Comments;
import net.dahanne.gallery3.client.model.Entity;
import net.dahanne.gallery3.client.model.Item;
import net.dahanne.gallery3.client.model.RelationShips;
import net.dahanne.gallery3.client.model.Tags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemUtils {

	public static Item parseJSONToItem(JSONObject jsonResult)
			throws JSONException {
		Item item = new Item();
		item.setUrl(jsonResult.getString("url"));

		item.setEntity(parseJSONToEntity(jsonResult));

		item.setRelationships(parseJSONToRelationShips(jsonResult));
		
		
		if(item.getEntity().getType().equals("album")){
			JSONArray jsonArray = jsonResult.getJSONArray("members");
			item.getMembers().addAll(convertJSONArrayToList(jsonArray));
		}

		return item;
	}

	private static Collection<String> convertJSONArrayToList(JSONArray jsonArray)
			throws JSONException {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < jsonArray.length(); i++) {
			list.add((String) jsonArray.get(i));
		}
		return list;
	}

	private static Entity parseJSONToEntity(JSONObject jsonResult)
			throws JSONException {
		Entity entity = new Entity();
		JSONObject entityJSON = jsonResult.getJSONObject("entity");

		entity.setId(entityJSON.getInt("id"));
		entity.setCaptured(entityJSON.getString("captured").equals("null") ? 0L
				: Integer.parseInt(entityJSON.getString("captured")));
		entity.setCreated(entityJSON.getString("created").equals("null") ? 0
				: Integer.parseInt(entityJSON.getString("created")));
		entity.setDescription(entityJSON.getString("description"));
		entity.setHeight(entityJSON.getString("height").equals("null") ? 0
				: Integer.parseInt(entityJSON.getString("height")));
		entity.setLevel(entityJSON.getInt("level"));
		entity.setMimeType(entityJSON.getString("mime_type").equals("null") ? null
				: entityJSON.getString("mime_type"));
		entity.setName(entityJSON.getString("name").equals("null") ? null
				: entityJSON.getString("name"));
		entity.setOwnerId(entityJSON.getInt("owner_id"));
		entity.setRandKey(entityJSON.getString("rand_key").equals("null") ? 0f
				: Float.parseFloat(entityJSON.getString("rand_key")));
		entity.setResizeHeight(entityJSON.getString("resize_height").equals(
				"null") ? 0 : Integer.parseInt(entityJSON
				.getString("resize_height")));
		entity.setResizeWidth(entityJSON.getString("resize_width").equals(
				"null") ? 0 : Integer.parseInt(entityJSON
				.getString("resize_width")));
		entity.setSlug(entityJSON.getString("slug").equals("null") ? null
				: entityJSON.getString("slug"));
		entity.setSortColumn(entityJSON.getString("sort_column"));
		entity.setSortOrder(entityJSON.getString("sort_order"));
		entity.setThumbHeight(entityJSON.getInt("thumb_height"));
		entity.setThumbWidth(entityJSON.getInt("thumb_width"));
		entity.setTitle(entityJSON.getString("title"));
		entity.setType(entityJSON.getString("type"));
		entity.setUpdated(entityJSON.getString("updated").equals("null") ? 0
				: Integer.parseInt(entityJSON.getString("updated")));
		entity.setViewCount(entityJSON.getInt("view_count"));
		entity.setWidth(entityJSON.getString("width").equals("null") ? 0
				: Integer.parseInt(entityJSON.getString("width")));
		entity.setView1(entityJSON.getInt("view_1"));
		entity.setView2(entityJSON.getInt("view_2"));
		entity.setWebUrl(entityJSON.getString("web_url"));
		entity.setThumbUrl(entityJSON.getString("thumb_url"));
		entity.setThumbSize(entityJSON.getInt("thumb_size"));
		entity.setThumbUrlPublic(entityJSON.getString("thumb_url_public"));
		entity.setCanEdit(entityJSON.getBoolean("can_edit"));
		
		if(entity.getType().equals("album")){
			entity.setAlbumCover(entityJSON.getString("album_cover"));
		}
		
		if(entity.getType().equals("photo")){
			entity.setParent(entityJSON.getString("parent"));
			entity.setFileUrl(entityJSON.getString("file_url"));
			entity.setFileSize(entityJSON.getInt("file_size"));
			entity.setFileUrlPublic(entityJSON.getString("file_url_public"));
			entity.setResizeUrl(entityJSON.getString("resize_url"));
			entity.setResizeSize(entityJSON.getInt("resize_size"));
			entity.setResizeUrlPublic(entityJSON.getString("resize_url_public"));
		}

		

		return entity;
	}

	private static RelationShips parseJSONToRelationShips(JSONObject jsonResult)
			throws JSONException {
		JSONObject relationShipsJSON = jsonResult
				.getJSONObject("relationships");

		JSONObject commentsJSON = relationShipsJSON.getJSONObject("comments");
		Comments comments = new Comments();
		comments.setUrl(commentsJSON.getString("url"));

		JSONObject tagsJSON = relationShipsJSON.getJSONObject("tags");
		Tags tags = new Tags();
		tags.setUrl(tagsJSON.getString("url"));
		JSONArray jsonArray = tagsJSON.getJSONArray("members");
		tags.getMembers().addAll(convertJSONArrayToList(jsonArray));

		RelationShips relationShips = new RelationShips();
		relationShips.setComments(comments);
		relationShips.setTags(tags);
		return relationShips;
	}

}
