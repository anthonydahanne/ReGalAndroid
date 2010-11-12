package net.dahanne.android.regalandroid.remote.utils;

import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.model.G2Picture;
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

	public static G2Picture itemToG2Picture(Item item) {
		if(item==null ||item.getEntity()==null){
			return null;
		}
		G2Picture picture = new G2Picture();
		picture.setId(item.getEntity().getId());
		picture.setName(item.getEntity().getName());
		picture.setTitle(item.getEntity().getTitle());
		picture.setRawFilesize(item.getEntity().getFileSize());
		picture.setRawHeight(item.getEntity().getHeight());
		picture.setRawWidth(item.getEntity().getWidth());
		return picture;
	}

}
