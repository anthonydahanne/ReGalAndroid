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

import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.model.Picture;
import net.dahanne.gallery3.client.model.Item;

public class G3ConvertUtils {

	public static Album itemToAlbum(Item item) {
		if(item==null ||item.getEntity()==null){
			return null;
		}
		Album album = new Album();
		album.setId(item.getEntity().getId());
		album.setName(item.getEntity().getId());
		album.setTitle(item.getEntity().getTitle());
		album.setSummary(item.getEntity().getDescription());
		album.setAlbumUrl(item.getEntity().getWebUrl());
		return album;
	}
	public static Picture itemToPicture(Item item) {
		if(item==null ||item.getEntity()==null){
			return null;
		}
		Picture picture = new Picture();
		picture.setId(item.getEntity().getId());
		picture.setTitle(item.getEntity().getTitle());
		picture.setFileName(item.getEntity().getName());

		picture.setThumbUrl(item.getEntity().getThumbUrl());
		picture.setThumbWidth(item.getEntity().getThumbWidth());
		picture.setThumbHeight(item.getEntity().getThumbHeight());
		picture.setThumbSize(item.getEntity().getThumbSize());
		
		picture.setResizedUrl(item.getEntity().getResizeUrl());
		picture.setResizedWidth(item.getEntity().getResizeWidth());
		picture.setResizedHeight(item.getEntity().getResizeHeight());
		picture.setResizedSize(item.getEntity().getResizeSize());
		
		picture.setFileUrl(item.getEntity().getFileUrl());
		picture.setFileSize(item.getEntity().getFileSize());
		picture.setHeight(item.getEntity().getHeight());
		picture.setWidth(item.getEntity().getWidth());
		
		picture.setPublicUrl(item.getEntity().getWebUrl());
		
		return picture;
	}

}
