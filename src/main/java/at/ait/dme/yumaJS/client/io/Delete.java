package at.ait.dme.yumaJS.client.io;

import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Delete {

	private static final String JSONP_PATH = "api/annotation/jsonp/delete?id=";
	
	public static void executeJSONP(String serverURL, String annotationID, AsyncCallback<Void> callback) {
		JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
		jsonp.send(serverURL + JSONP_PATH + annotationID, callback);
	}
	
}
