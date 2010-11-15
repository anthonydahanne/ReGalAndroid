/**
 *  commons-gallery, a common API module for ReGalAndroid
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

package net.dahanne.gallery.commons.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import net.dahanne.gallery.commons.model.Album;

/**
 * @author Anthony Dahanne
 * 
 */
public class Serialization {

	/**
	 * @param myAlbum
	 */
	public static byte[] serialize(Album myAlbum) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(output);
			oos.writeObject(myAlbum);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output.toByteArray();

	}

	/**
	 * @return
	 */
	public static Album unSerialize(byte[] serializedAlbum) {
		Album album = null;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(
					serializedAlbum);
			ObjectInputStream ois = new ObjectInputStream(bais);
			album = (Album) ois.readObject();
			ois.close();
		} catch (StreamCorruptedException e) {
			// Log.e("PLOUF", e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return album;

	}
}
