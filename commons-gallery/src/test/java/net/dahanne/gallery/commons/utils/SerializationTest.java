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

package net.dahanne.gallery.commons.utils;

import org.junit.Assert;
import org.junit.Test;

import net.dahanne.gallery.commons.model.Album;

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
		myAlbum.getSubAlbums().add(
				new Album(2, 4343, "a sub album", "nothing more here !", 43,
						true, true, true, true, "Some fields!"));

		byte[] serialize = Serialization.serialize(myAlbum);
		Album unserializedAlbum = Serialization.unSerialize(serialize);
		Assert.assertEquals(myAlbum, unserializedAlbum);

	}
}
