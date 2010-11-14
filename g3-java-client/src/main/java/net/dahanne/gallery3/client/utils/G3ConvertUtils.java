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
		picture.setName(item.getEntity().getName());

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
		return picture;
	}

}
