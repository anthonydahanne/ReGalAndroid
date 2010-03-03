/**
 * g2android
 * Copyright (c) 2009 Anthony Dahanne
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.dahanne.android.g2android.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import net.dahanne.android.g2android.model.Album;

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
