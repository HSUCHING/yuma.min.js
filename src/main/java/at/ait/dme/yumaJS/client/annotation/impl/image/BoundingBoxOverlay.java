package at.ait.dme.yumaJS.client.annotation.impl.image;

import at.ait.dme.yumaJS.client.annotation.editors.selection.BoundingBox;

import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * An overlay element used to represent the bounding box of
 * an annotation, composed of two nested DIVs.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class BoundingBoxOverlay extends Composite 
	implements HasMouseOverHandlers, HasMouseOutHandlers, Comparable<BoundingBoxOverlay> {
	
	/**
	 * The outer border DIV
	 */
	private FlowPanel outerBorder;
	
	/**
	 * The inner border DIV
	 */
	private FlowPanel innerBorder;
	
	public BoundingBoxOverlay(BoundingBox bbox) {
		outerBorder = new FlowPanel();
		outerBorder.setStyleName("annotation-bbox-outer");
		outerBorder.setPixelSize(bbox.getWidth(), bbox.getHeight());
		
		innerBorder = new FlowPanel();
		innerBorder.setWidth("100%");
		innerBorder.setHeight("100%");
		innerBorder.setStyleName("annotation-bbox-inner");
		
		outerBorder.add(innerBorder);
		initWidget(outerBorder);
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
