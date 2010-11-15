package net.dahanne.gallery.g2.java.client.utils;

import java.io.IOException;

import junit.framework.Assert;
import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.model.Picture;
import net.dahanne.gallery.g2.java.client.model.G2Album;
import net.dahanne.gallery.g2.java.client.model.G2Picture;

import org.json.JSONException;
import org.junit.Test;

public class G2ConvertUtilsTest {

	@Test
	public void g2AlbumToAlbum() throws IOException, JSONException {

		G2Album g2Album = new G2Album();
		g2Album.setId(1024);
		g2Album.setTitle("Title");
		g2Album.setName(12);
		g2Album.setSummary("Summary");
		g2Album.setParentName(1);
		g2Album.setExtrafields("extrafields");

		Album album = G2ConvertUtils.g2AlbumToAlbum(g2Album);

		Album expectedAlbum = new Album();
		expectedAlbum.setId(1024);
		expectedAlbum.setTitle("Title");
		expectedAlbum.setName(12);
		expectedAlbum.setSummary("Summary");
		expectedAlbum.setParentName(1);
		expectedAlbum.setExtrafields("extrafields");

		Assert.assertEquals(expectedAlbum, album);

	}

	@Test
	public void g2PictureToPicture() throws IOException, JSONException {

		G2Picture g2Picture = new G2Picture();

		g2Picture.setTitle("Title");
		g2Picture.setId(10214);
		g2Picture.setName("1");
		g2Picture.setThumbName("2");
		g2Picture.setThumbWidth(320);
		g2Picture.setThumbHeight(480);
		g2Picture.setResizedName("3");
		g2Picture.setResizedWidth(480);
		g2Picture.setResizedHeight(640);
		g2Picture.setRawWidth(768);
		g2Picture.setRawHeight(1024);
		g2Picture.setRawFilesize(10241024);
		g2Picture.setCaption("caption");
		g2Picture.setForceExtension("true");
		g2Picture.setHidden(true);

		String galleryUrl = "http://g2.dahanne.net";
		Picture picture = G2ConvertUtils.g2PictureToPicture(g2Picture,
				galleryUrl);
		
		Picture expectedPicture = new Picture();
		expectedPicture.setId(10214L);
		expectedPicture.setTitle("Title");
		expectedPicture.setName("1");
		expectedPicture.setFileUrl(galleryUrl + "/"
				+ G2ConvertUtils.BASE_URL_DEF + 1);
		expectedPicture.setWidth(768);
		expectedPicture.setHeight(1024);
		expectedPicture.setFileSize(10241024);

		expectedPicture.setThumbUrl(galleryUrl + "/"
				+ G2ConvertUtils.BASE_URL_DEF + 2);
		expectedPicture.setThumbWidth(320);
		expectedPicture.setThumbHeight(480);

		expectedPicture.setResizedUrl(galleryUrl + "/"
				+ G2ConvertUtils.BASE_URL_DEF + 3);
		expectedPicture.setResizedWidth(480);
		expectedPicture.setResizedHeight(640);

		Assert.assertEquals(expectedPicture, picture);

	}

}
