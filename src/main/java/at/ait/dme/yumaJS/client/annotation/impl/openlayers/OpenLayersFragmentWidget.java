package at.ait.dme.yumaJS.client.annotation.impl.openlayers;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;

import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.Bounds;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.BoxMarker;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.BoxesLayer;
import at.ait.dme.yumaJS.client.annotation.ui.FragmentWidget;
import at.ait.dme.yumaJS.client.annotation.ui.edit.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.ui.edit.BoundingBoxSelection;
import at.ait.dme.yumaJS.client.annotation.ui.edit.Range;
import at.ait.dme.yumaJS.client.annotation.ui.edit.Selection;
import at.ait.dme.yumaJS.client.annotation.ui.edit.Selection.SelectionChangeHandler;

/**
 * An implementation of {@link FragmentWidget} that is based on an OpenLayers
 * {@link BoxMarker} .
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class OpenLayersFragmentWidget implements FragmentWidget {

	/**
	 * The annotation map layer
	 */
	private BoxesLayer annotationLayer;
	
	/**
	 * The editing layer
	 */
	private AbsolutePanel editingLayer;
			
	/**
	 * The annotatable
	 */
	private OpenLayersAnnotationLayer annotatable;
	
	/**
	 * The OpenLayers BoxMarker
	 */
	private BoxMarker boxMarker;
	
	/**
	 * The inner border DIV
	 */
	private FlowPanel innerBorder;
	
	/**
	 * The selection or <code>null</code> if not in editing mode
	 */
	private Selection selection = null;
	
	/**
	 * The selection change handler
	 */
	private SelectionChangeHandler handler = null;
	
	public OpenLayersFragmentWidget(Bounds bounds, BoxesLayer annotationLayer, AbsolutePanel editingLayer, 
			OpenLayersAnnotationLayer annotatable) {
		
		this.annotationLayer = annotationLayer;
		this.editingLayer = editingLayer;
		this.annotatable = annotatable;

		this.boxMarker = BoxMarker.create(bounds);
		annotationLayer.addMarker(boxMarker);
		
		Element boxMarkerDiv = boxMarker.getDiv();
		
		Style markerStyle = boxMarkerDiv.getStyle();
		markerStyle.clearBorderColor();
		markerStyle.clearBorderWidth();
		markerStyle.clearBorderStyle();
		boxMarker.getDiv().setClassName("annotation-bbox-outer");
		
		innerBorder = new FlowPanel();
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
		return BoundingBox.create(
				div.getAbsoluteLeft() - editingLayer.getAbsoluteLeft(),
				div.getAbsoluteTop() - editingLayer.getAbsoluteTop(),
				innerBorder.getElement().getClientWidth(), 
				innerBorder.getElement().getClientHeight());
	}

	public void setBoundingBox(BoundingBox bbox) {
		String fragment = annotatable.toFragment(bbox, null);
		Bounds bounds = annotatable.toOpenLayersBounds(fragment);
		boxMarker.setBounds(bounds);
		annotationLayer.redraw();
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
		selection =  new BoundingBoxSelection(editingLayer, bbox);
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

	public int compareTo(FragmentWidget other) {
		if (!(other instanceof OpenLayersFragmentWidget))
			return 0;
		
		OpenLayersFragmentWidget overlay = (OpenLayersFragmentWidget) other;
		int thisArea = boxMarker.getDiv().getOffsetWidth() * boxMarker.getDiv().getOffsetHeight();
		int otherArea = overlay.boxMarker.getDiv().getOffsetWidth() * overlay.boxMarker.getDiv().getOffsetHeight();
		
		if (thisArea > otherArea)
			return -1;

		if (thisArea < otherArea)
			return 1;
		
		return 0;
	}

}
