package at.ait.dme.yumaJS.client.annotation.impl.image;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.ui.FragmentWidget;
import at.ait.dme.yumaJS.client.annotation.ui.edit.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.ui.edit.BoundingBoxSelection;
import at.ait.dme.yumaJS.client.annotation.ui.edit.Range;
import at.ait.dme.yumaJS.client.annotation.ui.edit.Selection;
import at.ait.dme.yumaJS.client.annotation.ui.edit.Selection.SelectionChangeHandler;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * An implementation of {@link FragmentWidget} that represents the bounding box of
 * an annotation, composed of two nested DIVs.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class ImageFragmentWidget implements FragmentWidget {

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
	
	/**
	 * The selection change handler
	 */
	private SelectionChangeHandler handler = null;
	
	public ImageFragmentWidget(Annotatable annotatable, AbsolutePanel panel, BoundingBox bbox) {
		this.panel = panel;
		
		outerBorder = new FlowPanel();
		outerBorder.setStyleName("annotation-bbox-outer");
		panel.add(outerBorder);
		setBoundingBox(bbox);
		
		innerBorder = new FlowPanel();
		innerBorder.setWidth("100%");
		innerBorder.setHeight("100%");
		innerBorder.setStyleName("annotation-bbox-inner");
		
		if (annotatable.getRepliesEnabled())
			innerBorder.setTitle("Click or move the mouse over the annotation to comment!");
		
		outerBorder.add(innerBorder);
	}
	
	public void setSelectionChangeHandler(SelectionChangeHandler handler) {
		this.handler = handler;
	}
	
	public BoundingBox getBoundingBox() {
		if (selection != null)
			return selection.getSelectedBounds();
		
		return bbox;
	}

	public void setBoundingBox(BoundingBox bbox) {
		this.bbox = bbox;
		outerBorder.setPixelSize(bbox.getWidth(), bbox.getHeight());
		panel.setWidgetPosition(outerBorder, bbox.getX(), bbox.getY());
	}
		
	public Range getRange() {
		return null;
	}

	public void setRange(Range range) {
		// Do nothing
	}
		
	public void startEditing() {
		outerBorder.setVisible(false);
		selection =  new BoundingBoxSelection(panel, bbox);
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
		
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return outerBorder.addDomHandler(handler, MouseOutEvent.getType());
	}

	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return outerBorder.addDomHandler(handler, MouseOverEvent.getType());
	}
	
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return innerBorder.addDomHandler(handler, ClickEvent.getType());
	}
		
	public void setZIndex(int idx) {
		outerBorder.getElement().getStyle().setZIndex(idx);
	}
	
	public int compareTo(FragmentWidget other) {
		if (!(other instanceof ImageFragmentWidget))
			return 0;
		
		ImageFragmentWidget overlay = (ImageFragmentWidget) other;
		int thisArea = outerBorder.getOffsetWidth() * outerBorder.getOffsetHeight();
		int otherArea = overlay.outerBorder.getOffsetWidth() * overlay.outerBorder.getOffsetHeight();
		
		if (thisArea > otherArea)
			return -1;
		
		if (thisArea < otherArea)
			return 1;
		
		return 0;
	}
	
	public void destroy() {
		outerBorder.removeFromParent();
	}
	
}
