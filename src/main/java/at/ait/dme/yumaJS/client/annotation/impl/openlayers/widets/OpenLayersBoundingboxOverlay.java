package at.ait.dme.yumaJS.client.annotation.impl.openlayers.widets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;

import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.BoxMarker;
import at.ait.dme.yumaJS.client.annotation.widgets.FragmentWidget;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.Range;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.ResizableBoxSelection;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.Selection;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.Selection.SelectionChangeHandler;

/**
 * An implementation of {@link FragmentWidget} that is based on an OpenLayers
 * {@link BoxMarker} .
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class OpenLayersBoundingboxOverlay implements FragmentWidget {

	/**
	 * The parent AbsolutePanel
	 */
	private AbsolutePanel panel;
	
	/**
	 * The OpenLayers BoxMarker
	 */
	private BoxMarker boxMarker;
	
	/**
	 * The selection or <code>null</code> if not in editing mode
	 */
	private Selection selection = null;
	
	/**
	 * The selection change handler
	 */
	private SelectionChangeHandler handler = null;
	
	public OpenLayersBoundingboxOverlay(AbsolutePanel panel, BoxMarker boxMarker) {
		this.panel = panel;
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
	
	public BoundingBox getBoundingBox() {
		if (selection != null)
			return selection.getSelectedBounds();
		
		Element div = boxMarker.getDiv();
		return BoundingBox.create(div.getAbsoluteLeft(), div.getAbsoluteTop(),
				div.getClientWidth(), div.getClientHeight());
	}

	public void setBoundingBox(BoundingBox bbox) {
		// TODO Auto-generated method stub
		
	}
	
	public BoxMarker getBoxMarker() {
		return boxMarker;
	}

	public void setSelectionChangeHandler(SelectionChangeHandler handler) {
		this.handler = handler;
	}

	public Range getRange() {
		return null;
	}

	public void setRange(Range range) {
		// Do nothing
	}

	public void startEditing() {
		boxMarker.getDiv().getStyle().setVisibility(Visibility.HIDDEN);
		BoundingBox bbox = getBoundingBox();
		selection =  new ResizableBoxSelection(panel, bbox);
		selection.setSelectionChangeHandler(handler);
	}

	public void cancelEditing() {
		selection.destroy();
		selection = null;
		boxMarker.getDiv().getStyle().setVisibility(Visibility.VISIBLE);
	}

	public void stopEditing() {
		setBoundingBox(selection.getSelectedBounds());
		selection.destroy();
		selection = null;
		boxMarker.getDiv().getStyle().setVisibility(Visibility.VISIBLE);
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
