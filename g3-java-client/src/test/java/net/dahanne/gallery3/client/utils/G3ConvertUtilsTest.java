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

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;

import junit.framework.Assert;
import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.model.Picture;
import net.dahanne.gallery3.client.model.Item;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class G3ConvertUtilsTest {

	@Test
	public void itemToAlbum() throws IOException, JSONException {
		URL resource = Resources.getResource("get-album-1.json");
		String string = Resources.toString(resource, Charsets.UTF_8);
		JSONObject jsonResult = (JSONObject) new JSONTokener(string)
				.nextValue();
		Item albumItem = ItemUtils.parseJSONToItem(jsonResult);
		
		Album itemToAlbum = G3ConvertUtils.itemToAlbum(albumItem );
		Album expectedAlbum = new Album();
		expectedAlbum.setId(1);
		expectedAlbum.setName(1);
		expectedAlbum.setTitle("Gallery");
		expectedAlbum.setSummary("");
		expectedAlbum.setAlbumUrl("http://g3.dahanne.net/index.php/");
		assertEquals(expectedAlbum, itemToAlbum);
		//not part of the equals
		assertEquals("http://g3.dahanne.net/index.php/rest/data/1?size=thumb",itemToAlbum.getAlbumCoverUrl());
		
	}
	
	
	@Test
	public void itemToPicture() throws IOException, JSONException {
		URL resource = Resources.getResource("get-photo-2.json");
		String string = Resources.toString(resource, Charsets.UTF_8);
		JSONObject jsonResult = (JSONObject) new JSONTokener(string)
				.nextValue();
		Item pictureItem = ItemUtils.parseJSONToItem(jsonResult);
		
		Picture itemToPicture = G3ConvertUtils.itemToPicture(pictureItem );
		Picture expectedPicture = new Picture();
		expectedPicture.setId(2);
		expectedPicture.setTitle("March\u00e9 Bon secours");
		expectedPicture.setFileName("marche-bonsecours.JPG");
		
		expectedPicture.setThumbUrl("http://g3.dahanne.net/index.php/rest/data/2?size=thumb");
		expectedPicture.setThumbWidth(150);
		expectedPicture.setThumbHeight(200);
		expectedPicture.setThumbSize(17151);
		
		expectedPicture.setResizedUrl("http://g3.dahanne.net/index.php/rest/data/2?size=resize");
		expectedPicture.setResizedWidth(480);
		expectedPicture.setResizedHeight(640);
		expectedPicture.setResizedSize(58309);
		
		expectedPicture.setFileUrl("http://g3.dahanne.net/index.php/rest/data/2?size=full");
		expectedPicture.setWidth(2304);
		expectedPicture.setHeight(3072);
		expectedPicture.setFileSize(675745);
		
		expectedPicture.setPublicUrl("http://g3.dahanne.net/index.php/marche-bonsecours");
		
		
		assertEquals(expectedPicture, itemToPicture);
		
		
	}
	
	@Test
	public void itemToPicture__bug3_resize_size_false() throws IOException, JSONException {
		URL resource = Resources.getResource("get-photo-bug3_resize_size_false.json");
		String string = Resources.toString(resource, Charsets.UTF_8);
		JSONObject jsonResult = (JSONObject) new JSONTokener(string)
				.nextValue();
		Item pictureItem = ItemUtils.parseJSONToItem(jsonResult);
		
		Picture itemToPicture = G3ConvertUtils.itemToPicture(pictureItem );
		Picture expectedPicture = new Picture();
		expectedPicture.setId(502);
		expectedPicture.setTitle("graduation pics 427");
		expectedPicture.setFileName("graduation pics 427.jpg");
		
		
		expectedPicture.setResizedUrl("http://www.iffam.org/gallery/index.php/rest/data/502?size=resize");
		
		expectedPicture.setFileUrl("http://www.iffam.org/gallery/index.php/rest/data/502?size=full");
		expectedPicture.setWidth(2576);
		expectedPicture.setHeight(1932);
		expectedPicture.setFileSize(1144458);
		expectedPicture.setPublicUrl("http://www.iffam.org/gallery/index.php/Twitpic/graduation-pics-427");
		
		
		
		assertEquals(expectedPicture, itemToPicture);
		
		
	}
	

	

}
