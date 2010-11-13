package net.dahanne.gallery3.client.utils;

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
		
		Assert.assertEquals(expectedAlbum, itemToAlbum);
		
		
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
		expectedPicture.setName("marche-bonsecours.JPG");
		
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
		
		
		Assert.assertEquals(expectedPicture, itemToPicture);
		
		
	}

	

}
