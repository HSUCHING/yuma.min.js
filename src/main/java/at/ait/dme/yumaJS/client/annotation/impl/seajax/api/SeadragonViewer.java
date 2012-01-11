package at.ait.dme.yumaJS.client.annotation.impl.seajax.api;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

/**
 * Wraps some relevant methods of Seadragon.Viewer API class. 
 * (For convenience, some of the methods of the Viewer.viewport
 * class are directly included in this wrapper.)
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class SeadragonViewer {

	private JavaScriptObject viewer;
	
	public SeadragonViewer(JavaScriptObject viewer) {
		this.viewer = viewer;
	}
	
	public String getObjectURI() {
		return _getObjectURI(viewer);
	};
	
	private native String _getObjectURI(JavaScriptObject viewer) /*-{
		return viewer.source.xmlUrl;
	}-*/;
	
	/**
	 * Converts a Seadragon World coordinate pair to an image pixel coordinate
	 * pair. 
	 * @param pt the Seadragon World coordinate
	 * @return the image pixel coordinate
	 */
	public SeadragonPoint toImageCoordinates(SeadragonPoint pt) {
		return _toImageCoordinates(pt, viewer);
	}
	
	private native SeadragonPoint _toImageCoordinates(SeadragonPoint pt, JavaScriptObject viewer) /*-{
		return new $wnd.Seadragon.Point(pt.x * viewer.source.width, pt.y * viewer.source.height * viewer.source.aspectRatio);
	}-*/;

	/**
	 * Converts an image pixel coordinate pair (x = {0, imgWidth}, y = {0, imgHeight}) to
	 * a normalized Seadragon World coordinate pair.
	 * @param pt the image pixel coordinate
	 * @return the Seadragon World coordiante
	 */
	public SeadragonPoint toWorldCoordinates(SeadragonPoint pt) {
		return _toWorldCoordinates(pt, viewer);
	}
	
	private native SeadragonPoint _toWorldCoordinates(SeadragonPoint pt, JavaScriptObject viewer) /*-{
		   return new $wnd.Seadragon.Point(pt.x / viewer.source.width, pt.y / viewer.source.height / viewer.source.aspectRatio);	
	}-*/;
	
	/**
	 * Converts a viewport pixel coordinate to a Seadragon World coordinate.
	 * @param p the viewport pixel coordinate
	 * @return the Seadragon World coordinate
	 */
	public SeadragonPoint pointFromPixel(SeadragonPoint p) {
		return _pointFromPixel(viewer, p);
	}
	
	private native SeadragonPoint _pointFromPixel(JavaScriptObject viewer, SeadragonPoint p) /*-{
		return viewer.viewport.pointFromPixel(p, true);
	}-*/;

	/**
	 * Converts a viewport pixel coordinate to a Seadragon World coordinate.
	 * @param p the viewport pixel coordinate
	 * @return the Seadragon World coordinate
	 */
	public SeadragonPoint pixelFromPoint(SeadragonPoint p) {
		return _pixelFromPoint(viewer, p);
	}
	
	private native SeadragonPoint _pixelFromPoint(JavaScriptObject viewer, SeadragonPoint p) /*-{
		return viewer.viewport.pixelFromPoint(p, true);
	}-*/;
	
	public void addOverlay(Element el, SeadragonRect rect) {
		_addOverlay(viewer, el, rect);
	}
	
	private native void _addOverlay(JavaScriptObject viewer, Element el, SeadragonRect rect) /*-{
		viewer.drawer.addOverlay(el, rect);
	}-*/;
	
	public void removeOverlay(Element el) {
		_removeOverlay(viewer, el);
	}
	
	private native void _removeOverlay(JavaScriptObject viewer, Element el) /*-{
		viewer.drawer.removeOverlay(el);
	}-*/;
	
	public void updateOverlay(Element el, SeadragonRect rect) {
		_updateOverlay(viewer, el, rect);
	}
	
	private native void _updateOverlay(JavaScriptObject viewer, Element el, SeadragonRect rect) /*-{
		viewer.drawer.updateOverlay(el, rect);
	}-*/;
	
	public void addAnimationtListener(SeadragonAnimationHandler listener) {
		_addAnimationListener(viewer, listener);
	}
	
	private native void _addAnimationListener(JavaScriptObject viewer, SeadragonAnimationHandler handler) /*-{
		viewer.addEventListener('animation', function() {
			handler.@at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonAnimationHandler::onAnimation()();
		});
	}-*/;
	
	public void addMouseHandler(SeadragonMouseHandler handler) {
		// TODO for some reason, it is not possible to attach a working mouseover handler to viewer.elmt 
		// using the normal GWT way (it does work for mouseout though!) 
		// The SeajaxMouseHandler is a workaround for this
		_addMouseHandler(viewer, handler);
	}
	
	private native void _addMouseHandler(JavaScriptObject viewer, SeadragonMouseHandler handler) /*-{
		viewer.elmt.onmouseover = function() { 
			handler.@at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonMouseHandler::onMouseOver()(); 
		};
		
		viewer.elmt.onmouseout = function() { 
			handler.@at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonMouseHandler::onMouseOut()(); 
		};
	}-*/;
	
}
