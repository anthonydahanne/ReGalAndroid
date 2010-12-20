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

import junit.framework.Assert;
import net.dahanne.gallery.commons.model.Album;

import org.junit.Test;

public class AlbumUtilsTest extends Assert {
	@Test
	public void findAlbumFromAlbumNameTest() {
		Album rootAlbum = new Album();
		rootAlbum.setName(999);
		Album album1 = new Album();
		album1.setName(1);
		rootAlbum.getSubAlbums().add(album1);
		Album album2 = new Album();
		album2.setName(2);
		rootAlbum.getSubAlbums().add(album2);
		Album album3 = new Album();
		album3.setName(3);
		rootAlbum.getSubAlbums().add(album3);
		Album album4 = new Album();
		album4.setName(4);
		rootAlbum.getSubAlbums().add(album4);
		Album album31 = new Album();
		album31.setName(31);
		album3.getSubAlbums().add(album31);
		Album album311 = new Album();
		album311.setName(311);
		album311.setId(12);
		album31.getSubAlbums().add(album311);
		
		
		//simulation of a fake album not to be taken into account! 
		Album album311_fake = new Album();
		album311_fake.setName(311);
		album311_fake.setFakeAlbum(true);
		album311.getSubAlbums().add(album311_fake);

		Album albumFound = AlbumUtils.findAlbumFromAlbumName(rootAlbum, 311);
		assertEquals(album311, albumFound);

		assertNull(AlbumUtils.findAlbumFromAlbumName(rootAlbum, 312));

	}
}
