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

package net.dahanne.gallery.g2.java.client.utils;

import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.model.Picture;
import net.dahanne.gallery.g2.java.client.model.G2Album;
import net.dahanne.gallery.g2.java.client.model.G2Picture;

public class G2ConvertUtils {

	 static final String BASE_URL_DEF = "main.php?g2_view=core.DownloadItem&g2_itemId=";
	 static final String EMBEDDED_GALLERY_BASE_URL_DEF = "&g2_view=core.DownloadItem&g2_itemId=";

	
	public static Album g2AlbumToAlbum(G2Album g2Album) {
		if(g2Album==null){
			return null;
		}
		Album album = new Album();
		album.setId(g2Album.getId());
		album.setName(g2Album.getName());
		album.setTitle(g2Album.getTitle());
		album.setSummary(g2Album.getSummary());
		album.setParentName(g2Album.getParentName());
		return album;
	}
	public static Picture g2PictureToPicture(G2Picture g2Picture, String galleryUrl) {
		if(g2Picture==null ){
			return null;
		}
		String baseUrl = getBaseUrl(galleryUrl);
		Picture picture = new Picture();
		picture.setId(g2Picture.getId());
		picture.setTitle(g2Picture.getCaption());
		picture.setFileName(g2Picture.getTitle());

		picture.setThumbUrl( baseUrl +  g2Picture.getThumbName());
		picture.setThumbWidth(g2Picture.getThumbWidth());
		picture.setThumbHeight(g2Picture.getThumbHeight());
//		picture.setThumbSize(g2Picture.getThumbSize());
		
		if(g2Picture.getResizedName()!=null){
			picture.setResizedUrl(baseUrl +g2Picture.getResizedName());
		}
		
		picture.setResizedWidth(g2Picture.getResizedWidth());
		picture.setResizedHeight(g2Picture.getResizedHeight());
//		picture.setResizedSize(g2Picture.getResizeSize());

		picture.setFileUrl(baseUrl +g2Picture.getName());
		picture.setPublicUrl(baseUrl +g2Picture.getName());
		picture.setFileSize(g2Picture.getRawFilesize());
		picture.setHeight(g2Picture.getRawHeight());
		picture.setWidth(g2Picture.getRawWidth());
		return picture;
	}
	

	/** Get the baseUrl */
	public static String getBaseUrl(String galleryUrl) {
		// bug #25 : for embedded gallery, should not add main.php
		if (isEmbeddedGallery(galleryUrl)) {
			return new StringBuilder().append(galleryUrl).append(EMBEDDED_GALLERY_BASE_URL_DEF).toString();
		}
		return new StringBuilder().append(galleryUrl).append("/").append(BASE_URL_DEF).toString();

	}
	

	// bug #25 : for embedded gallery, should not add main.php
	public static boolean isEmbeddedGallery(String url) {
		if (url.contains("action=gallery")) {
			return true;
		}
		return false;
	}
	

}
