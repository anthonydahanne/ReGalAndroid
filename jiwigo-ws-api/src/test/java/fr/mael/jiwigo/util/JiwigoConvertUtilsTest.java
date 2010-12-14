/**
 *  g2-java-client, a Menalto Gallery2 Java Client API
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

package fr.mael.jiwigo.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.model.Picture;

import org.junit.Test;

import fr.mael.jiwigo.om.Category;
import fr.mael.jiwigo.om.Image;

public class JiwigoConvertUtilsTest {

	@Test
	public void jiwigoCategoryToAlbum() {

		Category jiwigoCategory = new Category();
		jiwigoCategory.setIdentifiant(43);
		jiwigoCategory.setNom("MyAlbum");
		jiwigoCategory.setParentDirect(1);
		// jiwigoCategory.set

		// jiwigoCategory.setId(1024);
		// jiwigoCategory.setTitle("Title");
		// jiwigoCategory.setName(12);
		// jiwigoCategory.setSummary("Summary");
		// jiwigoCategory.setParentName(1);
		// jiwigoCategory.setExtrafields("extrafields");

		Album album = JiwigoConvertUtils.jiwigoCategoryToAlbum(jiwigoCategory);

		Album expectedAlbum = new Album();
		expectedAlbum.setId(43);
		expectedAlbum.setName(43);

		expectedAlbum.setTitle("MyAlbum");
		// expectedAlbum.setName(12);
		// expectedAlbum.setSummary("Summary");
		expectedAlbum.setParentName(1);
		// expectedAlbum.setExtrafields("extrafields");

		Assert.assertEquals(expectedAlbum, album);

	}

	@Test
	public void jiwigoImageToPicture() {

		Image jiwigoImage = new Image();

		jiwigoImage.setName("Title");
		jiwigoImage.setFile("Title.jpg");
		jiwigoImage.setIdentifiant(10214);
		jiwigoImage
				.setMiniature("http://piwigo.org/index.php?/path/to/picture/10214&miniature=true");
		jiwigoImage.setWidth(768);
		jiwigoImage.setHeight(1024);
		jiwigoImage
				.setUrl("http://piwigo.org/index.php?/path/to/picture/10214");

		// jiwigoImage.setTitle("Title");
		// jiwigoImage.setThumbWidth(320);
		// jiwigoImage.setThumbHeight(480);
		// jiwigoImage.setResizedName("3");
		// jiwigoImage.setResizedWidth(480);
		// jiwigoImage.setResizedHeight(640);
		// jiwigoImage.setRawFilesize(10241024);
		// jiwigoImage.setCaption("caption");
		// jiwigoImage.setForceExtension("true");
		// jiwigoImage.setHidden(true);

		// String galleryUrl = "http://g2.dahanne.net";
		Picture picture = JiwigoConvertUtils.jiwigoImageToPicture(jiwigoImage);

		Picture expectedPicture = new Picture();
		expectedPicture.setId(10214L);
		expectedPicture.setTitle("Title");
		expectedPicture.setName("Title.jpg");
		expectedPicture
		.setFileUrl("http://piwigo.org/index.php?/path/to/picture/10214");
		expectedPicture
		.setPublicUrl("http://piwigo.org/index.php?/path/to/picture/10214");
		expectedPicture.setWidth(768);
		expectedPicture.setHeight(1024);
		// expectedPicture.setFileSize(10241024);

		expectedPicture
				.setThumbUrl("http://piwigo.org/index.php?/path/to/picture/10214&miniature=true");
		// expectedPicture.setThumbWidth(320);
		// expectedPicture.setThumbHeight(480);
		//
		// expectedPicture.setResizedUrl(galleryUrl + "/"
		// + G2ConvertUtils.BASE_URL_DEF + 3);
		// expectedPicture.setResizedWidth(480);
		// expectedPicture.setResizedHeight(640);

		Assert.assertEquals(expectedPicture, picture);

	}

	@Test
	public void categoriesToAlbum() {
		List<Category> categories = new ArrayList<Category>();
		Category coaticook = new Category();
		coaticook.setNom("coaticook");
		coaticook.setIdentifiant(1);

		Category barrage = new Category();
		barrage.setNom("barrage");
		barrage.setIdentifiant(2);
		

		Category barrage2 = new Category();
		barrage2.setNom("barrage2");
		barrage2.setIdentifiant(20);

		Category sousBarrage = new Category();
		sousBarrage.setNom("sous barrage");
		sousBarrage.setIdentifiant(3);
		
		
		coaticook.getCategoriesFilles().add(barrage);
		coaticook.getCategoriesFilles().add(barrage2);
		barrage.getCategoriesFilles().add(sousBarrage);
		
		sousBarrage.getCategoriesMeres().add(barrage);
		barrage.getCategoriesMeres().add(coaticook);
		barrage2.getCategoriesMeres().add(coaticook);
		

		categories.add(coaticook);
		categories.add(barrage);
		categories.add(barrage2);
		categories.add(sousBarrage);
		
		Album resultAlbum = JiwigoConvertUtils.categoriesToAlbum(categories);
		
		Assert.assertEquals(0, resultAlbum.getName());
		Assert.assertEquals(1, resultAlbum.getSubAlbums().get(0).getName());
		Assert.assertEquals(2, resultAlbum.getSubAlbums().get(0).getSubAlbums().get(0).getName());
		Assert.assertEquals(20, resultAlbum.getSubAlbums().get(0).getSubAlbums().get(1).getName());
		Assert.assertEquals(3, resultAlbum.getSubAlbums().get(0).getSubAlbums().get(0).getSubAlbums().get(0).getName());
		
	}
}
