package org.jawed.javabrains.rest.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jawed.javabrains.messenger.model.Message;

public class RestApiClient {

	public static void main(String[] args) {
		
		//You need to make only one instance of client. With this instance you can call multiple end points.
		Client client = ClientBuilder.newClient();  
		
		//First way to get response
		/*Response response = client.target("http://localhost:8080/advanced-jaxrs-06/webapi/messages/1").request().get();
		Message message = response.readEntity(Message.class);*/
		
		//Second way to get response
		/*Message message = client
				.target("http://localhost:8080/advanced-jaxrs-06/webapi/messages/1")
				.request(MediaType.APPLICATION_JSON)
				.get(Message.class);
		System.out.println(message.getMessage());*/
		
		//Third way to get response
		/*String message = client
				.target("http://localhost:8080/advanced-jaxrs-06/webapi/messages/1")
				.request(MediaType.APPLICATION_JSON)
				.get(String.class);
		System.out.println(message);*/
		
		//Generic way to call the resource and get the output.
		WebTarget baseTarget = client.target("http://localhost:8080/advanced-jaxrs-06/webapi");
		WebTarget messageTarget = baseTarget.path("messages");
		WebTarget singleMessageTarget = messageTarget.path("{messageId}");
		
		Message message1 = singleMessageTarget
							.resolveTemplate("messageId", 1)
							.request(MediaType.APPLICATION_JSON)
							.get(Message.class);
		
		Message message2 = singleMessageTarget
				.resolveTemplate("messageId", 2)
				.request(MediaType.APPLICATION_JSON)
				.get(Message.class);
		
		System.out.println(message1.getMessage());
		System.out.println(message2.getMessage());
		
		//To make POST message request
		Message newMessage = new Message(4, "New Message from JAXRS client", "Jawed");
		Response postResponse = messageTarget
								.request()
								.post(Entity.json(newMessage));
		if(postResponse.getStatus() != 201){
			System.out.println("Error");
		}
		Message createdMessage = postResponse.readEntity(Message.class);
		System.out.println(createdMessage.getMessage());
		
		//To make PUT message request
		Message putMessage = new Message(1, "Put Message from JAXRS client", "Jawed");
		Response putResponse = singleMessageTarget
				.resolveTemplate("messageId", 1)
				.request()
				.put(Entity.json(putMessage));
		Message putMessageCreated = putResponse.readEntity(Message.class);
		System.out.println(putMessageCreated.getMessage());
	}

}
