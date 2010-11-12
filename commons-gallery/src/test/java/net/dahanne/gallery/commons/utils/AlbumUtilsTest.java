/*
 * G2Android
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

import junit.framework.Assert;
import net.dahanne.gallery.commons.model.Album;

import org.junit.Test;

/**
 * @author Anthony Dahanne
 * 
 */
public class AlbumUtilsTest extends Assert {
	@Test
	public void findAlbumFromAlbumNameTest() {
		Album rootAlbum = new Album();
		rootAlbum.setName(999);
		Album album1 = new Album();
		album1.setName(1);
		rootAlbum.getChildren().add(album1);
		Album album2 = new Album();
		album2.setName(2);
		rootAlbum.getChildren().add(album2);
		Album album3 = new Album();
		album3.setName(3);
		rootAlbum.getChildren().add(album3);
		Album album4 = new Album();
		album4.setName(4);
		rootAlbum.getChildren().add(album4);
		Album album31 = new Album();
		album31.setName(31);
		album3.getChildren().add(album31);
		Album album311 = new Album();
		album311.setName(311);
		album31.getChildren().add(album311);
		Album albumFound = AlbumUtils.findAlbumFromAlbumName(rootAlbum, 311);
		assertEquals(album311, albumFound);

		assertNull(AlbumUtils.findAlbumFromAlbumName(rootAlbum, 312));

	}
}
