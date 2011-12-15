package at.ait.dme.yumaJS.client.annotation.impl.seajax;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;

import at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonPoint;
import at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonRect;
import at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonViewer;
import at.ait.dme.yumaJS.client.annotation.ui.FragmentWidget;
import at.ait.dme.yumaJS.client.annotation.ui.edit.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.ui.edit.Range;
import at.ait.dme.yumaJS.client.annotation.ui.edit.Selection.SelectionChangeHandler;

public class SeajaxFragmentWidget implements FragmentWidget {
	
	/**
	 * The selection change handler
	 */
	private SelectionChangeHandler handler = null;
	
	private Element bboxDiv;
	
	public SeajaxFragmentWidget(BoundingBox bbox, SeadragonViewer viewer) {
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

	public void setZIndex(int idx) {
		// TODO Auto-generated method stub
		
	}

	public int compareTo(FragmentWidget arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
