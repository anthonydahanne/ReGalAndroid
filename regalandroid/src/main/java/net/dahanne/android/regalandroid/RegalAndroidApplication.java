/**
 *  ReGalAndroid, a gallery client for Android, supporting G2, G3, etc...
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
