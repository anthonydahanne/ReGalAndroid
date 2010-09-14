/**
 *  Gallery3-java-client
 *  URLs: http://github.com/anthonydahanne/g3-java-client , http://blog.dahanne.net
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

package net.dahanne.gallery3.client;

import javax.ws.rs.core.MediaType;

import net.dahanne.gallery3.client.model.Item;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

public class G3Client {

	public G3Client() {
		super();
		 Form f = new Form();
		   f.add("x", "foo");
		   f.add("y", "bar");
		   
		   Client c = Client.create();
		   WebResource r = c.resource("http://g3.dahanne.net/index.php/rest/item/2");
		   
		   Item response = r.accept(
			        MediaType.APPLICATION_JSON_TYPE).
			        get(Item.class);
		   
		   System.out.println(response);
		   
		   
//		   Builder type = r.
//		       type(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
//		   type.accept(MediaType.APPLICATION_JSON_TYPE);
//		JAXBBean bean = type
//		      .accept(MediaType.APPLICATION_JSON_TYPE)
//		      .post(JAXBBean.class, f);
	}

	
	
	
}
