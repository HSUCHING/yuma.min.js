package at.ait.dme.yumaJS.client.annotation.impl.openlayers;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.RootPanel;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.BoxMarker;
import at.ait.dme.yumaJS.client.annotation.widgets.DetailsPopup;
import at.ait.dme.yumaJS.client.init.Labels;

public class OpenLayersAnnotationOverlay {
		
	private BoxMarker boxMarker;
	
	private DetailsPopup detailsPopup;
	
	public OpenLayersAnnotationOverlay(Annotatable annotatable,  Annotation a, BoxMarker marker, Labels labels) {
		this.boxMarker = marker;
		
		DOM.sinkEvents(boxMarker.getDiv(), 
				Event.ONMOUSEOVER |Event.ONMOUSEOUT | Event.ONMOUSEMOVE | Event.ONMOUSEWHEEL);
		
		Event.setEventListener(boxMarker.getDiv(), new EventListener() {
			public void onBrowserEvent(Event event) {
				if (event.getTypeInt() == Event.ONMOUSEOUT) {
					if (!detailsPopup.contains(event.getClientX(), event.getClientY()))
						detailsPopup.setVisible(false);
				} else {
					refresh();
				}
			}
		});
		
		detailsPopup = new DetailsPopup(annotatable, a, labels);
		detailsPopup.setVisible(false);
		
		RootPanel.get().add(detailsPopup);
	}
	
	private void refresh() {
		Element boxMarkerDiv = boxMarker.getDiv();
		RootPanel.get().setWidgetPosition(detailsPopup, 
				boxMarkerDiv.getAbsoluteLeft(), 
				boxMarkerDiv.getAbsoluteTop() +  boxMarkerDiv.getOffsetHeight());
		detailsPopup.setVisible(true);	
	}
	
	public BoxMarker getMarker() {
		return boxMarker;
	}
	
	public void destroy() {
		detailsPopup.removeFromParent();
	}

}
