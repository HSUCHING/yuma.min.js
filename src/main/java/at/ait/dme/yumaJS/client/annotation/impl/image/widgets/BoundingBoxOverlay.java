package at.ait.dme.yumaJS.client.annotation.impl.image.widgets;

import at.ait.dme.yumaJS.client.annotation.widgets.FragmentWidget;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.selection.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.selection.ResizableBoxSelection;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.selection.Selection;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.selection.SelectionChangedHandler;

import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * An implementation of {@link FragmentWidget} that represents the bounding box of
 * an annotation, composed of two nested DIVs.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class BoundingBoxOverlay extends Composite 
	implements FragmentWidget, HasMouseOverHandlers, HasMouseOutHandlers, Comparable<BoundingBoxOverlay> {

	/**
	 * The parent AbsolutePanel
	 */
	private AbsolutePanel panel;
	
	/**
	 * The outer border DIV
	 */
	private FlowPanel outerBorder;
	
	/**
	 * The inner border DIV
	 */
	private FlowPanel innerBorder;

	/**
	 * The current bounding box
	 */
	private BoundingBox bbox;
	
	/**
	 * The selection or <code>null</code> if not in editing mode
	 */
	private Selection selection = null;
	
	public BoundingBoxOverlay(AbsolutePanel panel, BoundingBox bbox) {
		this.panel = panel;

		outerBorder = new FlowPanel();
		outerBorder.setStyleName("annotation-bbox-outer");
		setBoundingBox(bbox);
		
		innerBorder = new FlowPanel();
		innerBorder.setWidth("100%");
		innerBorder.setHeight("100%");
		innerBorder.setStyleName("annotation-bbox-inner");
		
		outerBorder.add(innerBorder);
		initWidget(outerBorder);
	}
		
	public void startEditing(SelectionChangedHandler handler) {
		this.setVisible(false);
		selection =  new ResizableBoxSelection(panel, bbox);
		selection.setSelectionChangedHandler(handler);
	} 
	
	public void stopEditing() {
		bbox = selection.getSelectedBounds();
		selection.destroy();
		selection = null;
		this.setVisible(true);
	}
	
	public void setBoundingBox(BoundingBox bbox) {
		this.bbox = bbox;
		outerBorder.setPixelSize(bbox.getWidth(), bbox.getHeight());
	}
	
	public BoundingBox getBoundingBox() {
		if (selection != null)
			bbox = selection.getSelectedBounds();
		return bbox;
	}

	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}

	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler, MouseOverEvent.getType());
	}
		
	public void setZIndex(int idx) {
		outerBorder.getElement().getStyle().setZIndex(idx);
	}
	
	public int compareTo(BoundingBoxOverlay other) {
		int thisArea = outerBorder.getOffsetWidth() * outerBorder.getOffsetHeight();
		int otherArea = other.outerBorder.getOffsetWidth() * other.outerBorder.getOffsetHeight();
		
		if (thisArea > otherArea)
			return -1;
		
		if (thisArea < otherArea)
			return 1;
		
		return 0;
	}
	
}
