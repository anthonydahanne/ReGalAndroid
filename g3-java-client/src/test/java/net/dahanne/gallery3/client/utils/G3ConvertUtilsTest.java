package net.dahanne.gallery3.client.utils;

import java.io.IOException;
import java.net.URL;

import junit.framework.Assert;
import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery3.client.model.Item;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class G3ConvertUtilsTest {

	@Test
	public void testItemToAlbum() throws IOException, JSONException {
		URL resource = Resources.getResource("get-album-1.json");
		String string = Resources.toString(resource, Charsets.UTF_8);
		JSONObject jsonResult = (JSONObject) new JSONTokener(string)
				.nextValue();
		Item albumItem = ItemUtils.parseJSONToItem(jsonResult);
//		
//		Album itemToAlbum = G3ConvertUtils.itemToAlbum(albumItem );
//		Album expectedAlbum = new Album();
//		expectedAlbum.setId(1);
//		expectedAlbum.setName(1);
//		expectedAlbum.setTitle("Gallery");
//		expectedAlbum.setSummary("");
//		expectedAlbum.setAlbumUrl("http://g3.dahanne.net/index.php/");
//		
//		Assert.assertEquals(expectedAlbum, itemToAlbum);
		
		
	}

}
