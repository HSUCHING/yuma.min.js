package at.ait.dme.yumaJS.client.annotation.impl.seajax;

import com.google.gwt.user.client.Element;
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
	
	/**
	 * The selection change handler
	 */
	private SelectionChangeHandler handler = null;
	
	private Element bboxDiv;
	
	private AbsolutePanel editingLayer;
	
	private Annotatable annotatable;
	
	private Selection selection;
	
	public SeajaxFragmentWidget(BoundingBox bbox, SeadragonViewer viewer, AbsolutePanel editingLayer,
			Annotatable annotatable) {
		
		this.editingLayer = editingLayer;
		this.annotatable = annotatable;
		
		SeadragonPoint anchor =
			viewer.pointFromPixel(SeadragonPoint.create(bbox.getX(), bbox.getY())); 
		SeadragonPoint bottomRight = 
			viewer.pointFromPixel(SeadragonPoint.create(bbox.getX() + bbox.getWidth(), bbox.getY() + bbox.getHeight()));
		
		bboxDiv = new FlowPanel().getElement();
		viewer.addOverlay(bboxDiv, SeadragonRect.create(
				anchor.getX(), anchor.getY(), 
				bottomRight.getX() - anchor.getX(), 
				bottomRight.getY() - anchor.getY()));
	}

	public void setSelectionChangeHandler(SelectionChangeHandler handler) {
		this.handler = handler;
	}

	public BoundingBox getBoundingBox() {
		if (selection != null)
			return selection.getSelectedBounds();

		return BoundingBox.create(
				bboxDiv.getAbsoluteLeft() - editingLayer.getAbsoluteLeft(),
				bboxDiv.getAbsoluteTop() - editingLayer.getAbsoluteTop(),
				bboxDiv.getClientWidth(), 
				bboxDiv.getClientHeight());
	}

	public void setBoundingBox(BoundingBox bbox) {
		// 
	}

	public Range getRange() {
		return null;
	}

	public void setRange(Range range) {
		// Do nothing
	}

	public void startEditing() {
		// boxMarker.getDiv().getStyle().setVisibility(Visibility.HIDDEN);
		BoundingBox bbox = getBoundingBox();
		selection =  new BoundingBoxSelection(editingLayer, bbox);
		selection.setSelectionChangeHandler(handler);
	}

	public void cancelEditing() {
		selection.destroy();
		selection = null;
		// boxMarker.getDiv().getStyle().setVisibility(Visibility.VISIBLE);
	}

	public void stopEditing() {
		setBoundingBox(selection.getSelectedBounds());
		selection.destroy();
		selection = null;
		// boxMarker.getDiv().getStyle().setVisibility(Visibility.VISIBLE);
	}

	public void setZIndex(int idx) {
		// boxMarker.getDiv().getStyle().setZIndex(idx);
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
