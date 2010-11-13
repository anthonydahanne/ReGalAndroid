package net.dahanne.android.regalandroid;

import java.util.ArrayList;
import java.util.List;

import net.dahanne.android.regalandroid.remote.RemoteGalleryConnectionFactory;
import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.model.Picture;
import android.app.Application;

public class RegalAndroidApplication extends Application {
	private final List<Picture> pictures = new ArrayList<Picture>();
	private Album currentAlbum;
	private int currentPosition;
	
	
	
	
	public RegalAndroidApplication() {
		super();
		RemoteGalleryConnectionFactory.setContext(this);
		
	}

	public void setCurrentAlbum(Album currentAlbum) {
		this.currentAlbum = currentAlbum;
	}

	public Album getCurrentAlbum() {
		return currentAlbum;
	}

	public List<Picture> getPictures() {
		return pictures;
	}

	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

}
