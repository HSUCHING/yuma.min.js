package at.ait.dme.yumaJS.client.annotation.impl.openlayers.api;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTML;

public class BoxMarker extends JavaScriptObject {

	protected BoxMarker() { }
	
	public static native BoxMarker create(Bounds bounds) /*-{
		return new $wnd.OpenLayers.Marker.Box(bounds);
	}-*/;
	
	public final HTML getDiv() {
		return HTML.wrap(_getDiv());
	}
	
	private native Element _getDiv() /*-{
		return this.div;
	}-*/;
	
}
