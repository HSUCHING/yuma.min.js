package at.ait.dme.yumaJS.client.annotation.impl.openlayers.widets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.FlowPanel;

import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.BoxMarker;
import at.ait.dme.yumaJS.client.annotation.widgets.FragmentWidget;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.Range;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.Selection.SelectionChangeHandler;

public class OpenLayersBoundingboxOverlay implements FragmentWidget {
	
	private BoxMarker boxMarker;
	
	private SelectionChangeHandler handler = null;
	
	public OpenLayersBoundingboxOverlay(BoxMarker boxMarker) {
		this.boxMarker = boxMarker;
		
		Element boxMarkerDiv = boxMarker.getDiv();
		
		Style markerStyle = boxMarkerDiv.getStyle();
		markerStyle.clearBorderColor();
		markerStyle.clearBorderWidth();
		markerStyle.clearBorderStyle();
		boxMarker.getDiv().setClassName("annotation-bbox-outer");
		
		FlowPanel innerBorder = new FlowPanel();
		innerBorder.setWidth("100%");
		innerBorder.setHeight("100%");
		innerBorder.setStyleName("annotation-bbox-inner");
		boxMarker.getDiv().appendChild(innerBorder.getElement());
		
		DOM.sinkEvents(boxMarkerDiv, 
				Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.ONMOUSEMOVE | Event.ONMOUSEWHEEL);		
	}

	public void setSelectionChangeHandler(SelectionChangeHandler handler) {
		this.handler = handler;
	}

	public BoundingBox getBoundingBox() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setBoundingBox(BoundingBox bbox) {
		// TODO Auto-generated method stub
		
	}

	public Range getRange() {
		return null;
	}

	public void setRange(Range range) {
		// Do nothing
	}

	public void startEditing() {
		// TODO Auto-generated method stub
		
	}

	public void cancelEditing() {
		// TODO Auto-generated method stub
		
	}

	public void stopEditing() {
		// TODO Auto-generated method stub
		
	}
	
	public void setEventListener(EventListener listener) {
		Event.setEventListener(boxMarker.getDiv(), listener);
	}

	public void setZIndex(int idx) {
		boxMarker.getDiv().getStyle().setZIndex(idx);
	}

	public int compareTo(FragmentWidget o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
