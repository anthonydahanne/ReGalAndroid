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


package net.dahanne.gallery.jiwigo.converter;
import java.util.List;

import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.model.Picture;
import net.dahanne.gallery.commons.utils.AlbumUtils;
import fr.mael.jiwigo.om.Category;
import fr.mael.jiwigo.om.Image;



public class JiwigoConvertUtils {

	public static Album jiwigoCategoryToAlbum(Category jiwigoCategory) {
		if(jiwigoCategory==null){
			return null;
		}
		Album album = new Album();
		album.setId(jiwigoCategory.getIdentifier());
		album.setName(jiwigoCategory.getIdentifier());
		album.setTitle(jiwigoCategory.getName());
//		album.setSummary(jiwigoCategory.getSummary());
		album.setParentName(jiwigoCategory.getDirectParent()==null?0:jiwigoCategory.getDirectParent());
		album.setAlbumCoverUrl(jiwigoCategory.getUrlThumbnail());
		return album;
	}
	public static Picture jiwigoImageToPicture(Image jiwigoImage) {
		if(jiwigoImage==null ){
			return null;
		}
		Picture picture = new Picture();
		picture.setId(jiwigoImage.getIdentifier());
		picture.setTitle(jiwigoImage.getName());
		picture.setFileName(jiwigoImage.getFile());

		picture.setThumbUrl(jiwigoImage.getThumbnailUrl());
//		picture.setThumbWidth(jiwigoImage.getThumbWidth());
//		picture.setThumbHeight(jiwigoImage.getThumbHeight());
//		picture.setThumbSize(g2Picture.getThumbSize());
		
//		picture.setResizedUrl(baseUrl +jiwigoImage.getResizedName());
//		picture.setResizedWidth(jiwigoImage.getResizedWidth());
//		picture.setResizedHeight(jiwigoImage.getResizedHeight());
//		picture.setResizedSize(g2Picture.getResizeSize());

		picture.setFileUrl(jiwigoImage.getUrl());
		picture.setPublicUrl(jiwigoImage.getUrl());
//		picture.setFileSize(jiwigoImage.getRawFilesize());
		picture.setHeight(jiwigoImage.getHeight());
		picture.setWidth(jiwigoImage.getWidth());
		return picture;
	}
	
	public static Album categoriesToAlbum(List<Category> categories){
		// in jiwigo, the root album can not contain pictures (not an album); it
		// is not listed among the available albums
		Album resultAlbum = new Album();
		resultAlbum.setName(0);
		resultAlbum.setId(0);
		
		Album album;
		for (Category category : categories) {
			album = jiwigoCategoryToAlbum(category);
			if(category.getParentCategories().size()==0){
					//no parents, so it is at the root
					resultAlbum.getSubAlbums().add(album);
			}
			else{
				Album parentAlbum = AlbumUtils.findAlbumFromAlbumName(resultAlbum,  category.getParentCategories().get(0).getIdentifier());
				parentAlbum.getSubAlbums().add(album);
				
			}
		}
		return resultAlbum;
	}
	

	

}
