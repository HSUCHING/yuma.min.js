package at.ait.dme.yumaJS.client.annotation.widgets;

import at.ait.dme.yumaJS.client.annotation.widgets.edit.selection.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.selection.Range;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.selection.SelectionChangedHandler;

public interface FragmentWidget {
	
	public BoundingBox getBoundingBox();
	
	public void setBoundingBox(BoundingBox bbox);
	
	public Range getRange();
	
	public void setRange(Range range);
	
	public void startEditing(SelectionChangedHandler hanlder);
	
	public void stopEditing();

}
