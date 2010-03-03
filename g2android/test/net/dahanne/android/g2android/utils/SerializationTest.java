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

import junit.framework.Assert;
import net.dahanne.android.g2android.model.Album;

import org.junit.Test;

/**
 * @author Anthony Dahanne
 * 
 */
public class SerializationTest {
	@Test
	public void serializeAndUnSerializeAnAlbum() {
		Album myAlbum = new Album(1, 43, "myAlbum",
				"Inthis album, there's not much", 0, false, false, false,
				false, "no extrafield");
		myAlbum.getChildren().add(
				new Album(2, 4343, "a sub album", "nothing more here !", 43,
						true, true, true, true, "Some fields!"));

		Serialization serialization = new Serialization();
		String string = new String("salut les gars!");
		byte[] serialize = serialization.serialize(myAlbum);
		Album unserializedAlbum = serialization.unSerialize(serialize);
		Assert.assertEquals(myAlbum, unserializedAlbum);

	}
}
