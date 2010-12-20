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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlbumUtils {
	private final static Logger logger = LoggerFactory.getLogger(AlbumUtils.class);
	
	/**
	 * This method returns the Album corresponding to albumName, among the rootAlbum hierarchy
	 * @param rootAlbum
	 * @param albumName
	 * @return
	 */
	public static Album findAlbumFromAlbumName(Album rootAlbum, int albumName) {
		logger.debug("rootAlbum is : {} -- albumName is : {}",rootAlbum,albumName);
		Album albumFound=null;
		if (rootAlbum.getName() == albumName&& !rootAlbum.isFakeAlbum()) {
			albumFound= rootAlbum;
		}
		for (Album album : rootAlbum.getSubAlbums()) {
			if (album.getName() == albumName && !album.isFakeAlbum()) {
				albumFound= album;
				break;
			}
			Album fromAlbumName = findAlbumFromAlbumName(album, albumName);
			if (fromAlbumName != null && !fromAlbumName.isFakeAlbum()) {
				albumFound= fromAlbumName;
			}

		}
		logger.debug("albumFound is : {}",albumFound);
		return albumFound;
	}
}
