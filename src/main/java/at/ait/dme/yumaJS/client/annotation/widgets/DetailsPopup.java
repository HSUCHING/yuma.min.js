package at.ait.dme.yumaJS.client.annotation.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

public abstract class DetailsPopup extends Composite {
	
	protected FlowPanel container;
	
	@Override
	public void setVisible(boolean visible) {
		container.setVisible(true);
		Style style = container.getElement().getStyle();
		if (visible) {
			style.setVisibility(Visibility.VISIBLE);
			style.setOpacity(1);
		} else {
			style.setVisibility(Visibility.HIDDEN);
			style.setOpacity(0);
		}
	}
	
	public boolean contains(int x, int y) {
		int left = container.getAbsoluteLeft();
		int top = container.getAbsoluteTop();
		int w = container.getOffsetWidth();
		int h = container.getOffsetHeight();
		
		if (x < left)
			return false;
		
		if (x > left + w)
			return false;
		
		if (y < top)
			return false;
		
		if (y > top + h)
			return false;
		
		return true;
	}

}
