package at.ait.dme.yumaJS.client.annotation.impl.openlayers.api;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTML;

public class Map {
	
	private JavaScriptObject map;
	
	public Map(JavaScriptObject map) { 
		this.map = map;
	}

	public void addBoxesLayer(BoxesLayer boxesLayer) {
		_addBoxesLayer(map, boxesLayer);
	}
	
	private native void _addBoxesLayer(JavaScriptObject map, BoxesLayer boxesLayer) /*-{
		map.addLayer(boxesLayer);
	}-*/;
	
	public HTML getDiv() {
		return HTML.wrap(_getDiv(map));
	}
	
	private native Element _getDiv(JavaScriptObject map) /*-{
		return map.div;
	}-*/;
	
	public LonLat getLonLatFromPixel(Pixel px) {
		return _getLonLatFromPixel(map, px);
	}
	
	private native LonLat _getLonLatFromPixel(JavaScriptObject map, Pixel px) /*-{
		return map.getLonLatFromPixel(px);
	}-*/;

	public Pixel getViewPortPixelFromLonLat(LonLat lonlat) {
		return _getViewPortPixelFromLonLat(map, lonlat);
	}
	
	private native Pixel _getViewPortPixelFromLonLat(JavaScriptObject map, LonLat lonlat) /*-{
		return map.getViewPortPxFromLonLat(lonlat);
	}-*/;

	
}
