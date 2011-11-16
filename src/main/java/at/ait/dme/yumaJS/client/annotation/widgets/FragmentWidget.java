package at.ait.dme.yumaJS.client.annotation.widgets;

import at.ait.dme.yumaJS.client.annotation.widgets.edit.selection.SelectionChangedHandler;

public interface FragmentWidget {
	
	public void startEditing(SelectionChangedHandler hanlder);
	
	public void stopEditing();

}
