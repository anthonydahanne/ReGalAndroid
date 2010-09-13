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
