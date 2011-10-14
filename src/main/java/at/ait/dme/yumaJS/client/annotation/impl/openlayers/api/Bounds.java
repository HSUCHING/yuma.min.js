package at.ait.dme.yumaJS.client.annotation.impl.openlayers.api;

import com.google.gwt.core.client.JavaScriptObject;

public class Bounds extends JavaScriptObject {
	
	protected Bounds() { }
	
	public static native Bounds create(double left, double bottom, double right, double top) /*-{
		return new $wnd.OpenLayers.Bounds(left, bottom, right, top);
	}-*/;
	
	public final native double getLeft() /*-{
		return this.left;
	}-*/;

	public final native double getBottom() /*-{
		return this.bottom;
	}-*/;
	
	public final native double getRight() /*-{
		return this.right;
	}-*/;
	
	public final native double getTop() /*-{
		return this.top;
	}-*/;
	
}
