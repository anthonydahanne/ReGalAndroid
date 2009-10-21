package net.dahanne.android.g2android;

import java.util.ArrayList;
import java.util.List;

import net.dahanne.android.g2android.model.Album;
import net.dahanne.android.g2android.model.G2Picture;
import android.app.Application;

public class G2AndroidApplication extends Application {
	private List<G2Picture> pictures = new ArrayList<G2Picture>();
	private Album rootAlbum;

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

	public List<G2Picture> getPictures() {
		return pictures;
	}
}
