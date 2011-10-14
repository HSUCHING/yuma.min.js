package at.ait.dme.yumaJS.client.annotation.impl.openlayers.api;

import com.google.gwt.core.client.JavaScriptObject;

public class LonLat extends JavaScriptObject {
	
	protected LonLat() { }
	
	public static native LonLat create(double lon, double lat) /*-{
		return new $wnd.OpenLayers.LonLat(lon, lat);
	}-*/;
	
	public final native double getLon() /*-{
		return this.lon;
	}-*/;

	public final native double getLat() /*-{
		return this.lat;
	}-*/;
	
}
