package at.ait.dme.yumaJS.client.io;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;

import at.ait.dme.yumaJS.client.annotation.Annotation;

public class Create {
	
	private static final String JSONP_PATH = "api/annotation/jsonp/create?json=";
	
	public static void executeJSONP(String serverURL, Annotation a, AsyncCallback<JavaScriptObject> callback) {
		JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
		JSONObject json = new JSONObject(a);
		
		// Note: GWT puts some sort of annoying hash key into overlay types - remove!
		if (json.containsKey("$H"))
			json.put("$H", null);
		
		jsonp.requestObject(serverURL + JSONP_PATH + URL.encodeQueryString(json.toString()), callback);
	}

}
