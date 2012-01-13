package at.ait.dme.yumaJS.client.annotation.impl.seajax;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonPoint;
import at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonRect;
import at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonViewer;
import at.ait.dme.yumaJS.client.annotation.ui.FragmentWidget;
import at.ait.dme.yumaJS.client.annotation.ui.edit.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.ui.edit.BoundingBoxSelection;
import at.ait.dme.yumaJS.client.annotation.ui.edit.Range;
import at.ait.dme.yumaJS.client.annotation.ui.edit.Selection;
import at.ait.dme.yumaJS.client.annotation.ui.edit.Selection.SelectionChangeHandler;

public class SeajaxFragmentWidget implements FragmentWidget {
	
	private SelectionChangeHandler handler = null;
	
	private SeadragonViewer viewer;
	
	private FlowPanel outerBorder;
	
	private SeadragonRect overlay;
	
	private AbsolutePanel editingLayer;
	
	private Selection selection;
	
	public SeajaxFragmentWidget(BoundingBox bbox, SeadragonViewer viewer, AbsolutePanel editingLayer,
			Annotatable annotatable) {
		
		this.viewer = viewer;
		this.editingLayer = editingLayer;
		
		outerBorder = new FlowPanel();
		outerBorder.setStyleName("annotation-bbox-outer");
		outerBorder.setPixelSize(bbox.getWidth(), bbox.getHeight());

		FlowPanel innerBorder = new FlowPanel();
		innerBorder.setWidth("100%");
		innerBorder.setHeight("100%");
		innerBorder.setStyleName("annotation-bbox-inner");
		outerBorder.add(innerBorder);
		
		overlay = toRect(bbox);
		
		Element bboxDiv = outerBorder.getElement();
		
		DOM.sinkEvents(bboxDiv, 
				Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.ONMOUSEMOVE | Event.ONMOUSEWHEEL);		
		
		viewer.addOverlay(bboxDiv, overlay);
	}

	private SeadragonRect toRect(BoundingBox bbox) {
		SeadragonPoint topLeft =
			viewer.pointFromPixel(SeadragonPoint.create(bbox.getX(), bbox.getY())); 
		SeadragonPoint bottomRight = 
			viewer.pointFromPixel(SeadragonPoint.create(bbox.getX() + bbox.getWidth(), bbox.getY() + bbox.getHeight()));
		
		return SeadragonRect.create(
			topLeft.getX(), topLeft.getY(), 
			bottomRight.getX() - topLeft.getX(), 
			bottomRight.getY() - topLeft.getY());
	}
	
	public void setSelectionChangeHandler(SelectionChangeHandler handler) {
		this.handler = handler;
	}

	public BoundingBox getBoundingBox() {
		if (selection != null)
			return selection.getSelectedBounds();
		
		SeadragonPoint topLeft = viewer.pixelFromPoint(overlay.getTopLeft());
		SeadragonPoint bottomRight = viewer.pixelFromPoint(overlay.getBottomRight());
		
		return BoundingBox.create(
				(int) topLeft.getX(),
				(int) topLeft.getY(),
				(int) (bottomRight.getX() - topLeft.getX()),
				(int) (bottomRight.getY() - topLeft.getY()));
	}

	public void setBoundingBox(BoundingBox bbox) {
		overlay = toRect(bbox);
		viewer.updateOverlay(outerBorder.getElement(), overlay);
	}

	public Range getRange() {
		return null;
	}

	public void setRange(Range range) {
		// Do nothing
	}

	public void startEditing() {
		outerBorder.setVisible(false);
		BoundingBox bbox = getBoundingBox();
		selection =  new BoundingBoxSelection(editingLayer, bbox);
		selection.setSelectionChangeHandler(handler);
	}

	public void cancelEditing() {
		selection.destroy();
		selection = null;
		outerBorder.setVisible(true);
	}

	public void stopEditing() {
		setBoundingBox(selection.getSelectedBounds());
		selection.destroy();
		selection = null;
		outerBorder.setVisible(true);
	}
	
	public void destroy() {
		viewer.removeOverlay(outerBorder.getElement());
		outerBorder.removeFromParent();
	}
	
	public void setEventListener(EventListener listener) {
		Event.setEventListener(outerBorder.getElement(), listener);
	}

	public void setZIndex(int idx) {
		outerBorder.getElement().getStyle().setZIndex(idx);
	}

	public int compareTo(FragmentWidget other) {
		if (!(other instanceof SeajaxFragmentWidget))
			return 0;
		
		/*
		SeajaxFragmentWidget overlay = (SeajaxFragmentWidget) other;
		
		int thisArea = boxMarker.getDiv().getOffsetWidth() * boxMarker.getDiv().getOffsetHeight();
		int otherArea = overlay.boxMarker.getDiv().getOffsetWidth() * overlay.boxMarker.getDiv().getOffsetHeight();
		
		if (thisArea > otherArea)
			return -1;

		if (thisArea < otherArea)
			return 1;
		*/
		return 0;
	}

}
