package pab.showcase.endpoint;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

/**
 * Fake endpoint. Explained by balusC here:
 * 
 * https://github.com/javaserverfaces/mojarra/blob/598058f425f8894683f1d68796b0474e54cfea9e/test/javaee8/websocket/src/main/java/com/sun/faces/test/javaee8/websocket/FakeEndpoint.java
 */
public class FakeEndpoint extends Endpoint {

	@Override
	public void onOpen(Session session, EndpointConfig config) {
		// https://java.net/jira/browse/WEBSOCKET_SPEC-240
	}

}