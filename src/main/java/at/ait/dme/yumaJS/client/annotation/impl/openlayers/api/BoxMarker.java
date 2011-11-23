package at.ait.dme.yumaJS.client.annotation.impl.openlayers.api;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;

public class BoxMarker extends JavaScriptObject {

	protected BoxMarker() { }
	
	public static native BoxMarker create(Bounds bounds) /*-{
		return new $wnd.OpenLayers.Marker.Box(bounds);
	}-*/;
	
	public final native Element getDiv() /*-{
		return this.div;
	}-*/;
	
	public final native Bounds getBounds() /*-{
		return this.bounds;
	}-*/;
	
	public final native void setBounds(Bounds bounds) /*-{
		this.bounds = bounds;
	}-*/;
	
}
