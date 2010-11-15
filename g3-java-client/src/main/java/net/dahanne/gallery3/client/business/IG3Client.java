/**
 *  g3-java-client, a Menalto Gallery3 Java Client API
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

package net.dahanne.gallery3.client.business;

import java.io.File;
import java.util.List;

import net.dahanne.gallery3.client.business.exceptions.G3GalleryException;
import net.dahanne.gallery3.client.model.Entity;
import net.dahanne.gallery3.client.model.Item;

public interface IG3Client {

	Item getItem(int itemId)throws G3GalleryException;
	int createItem(Entity entity,File file) throws G3GalleryException;
	void updateItem(Entity entity)throws G3GalleryException;
	void deleteItem(int itemId)throws G3GalleryException;
	String getApiKey()throws G3GalleryException;
	List<Item> getAlbumAndSubAlbums(int albumId) throws G3GalleryException;
	public List<Item> getPictures(int albumId) throws G3GalleryException;
}
