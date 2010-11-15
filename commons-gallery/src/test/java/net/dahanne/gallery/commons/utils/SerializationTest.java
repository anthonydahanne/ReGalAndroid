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

import net.dahanne.gallery.commons.model.Album;

import org.junit.Assert;
import org.junit.Test;

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
