package net.dahanne.android.g2android;

import java.util.ArrayList;
import java.util.List;

import net.dahanne.android.g2android.model.Album;
import android.app.Application;

public class G2AndroidApplication extends Application {
	private List<Album> albums = new ArrayList<Album>();
	private Album rootAlbum;

	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}

	public List<Album> getAlbums() {
		return albums;
	}

	public void setRootAlbum(Album rootAlbum) {
		this.rootAlbum = rootAlbum;
	}

	public Album getRootAlbum() {
		return rootAlbum;
	}

	public void currentAlbum(Album rootAlbum) {
		this.rootAlbum = rootAlbum;
	}

	public Album getCurrentAlbum() {
		return rootAlbum;
	}

}
