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

public class OpenlayersAnnotationOverlay {
	
	private Element boxMarkerDiv;
	
	private DetailsPopup detailsPopup;
	
	public OpenlayersAnnotationOverlay(Annotatable annotatable, Annotation a, BoxMarker marker, Labels labels) {
		this.boxMarkerDiv = marker.getDiv();
		
		DOM.sinkEvents(boxMarkerDiv, Event.ONMOUSEOVER | Event.ONMOUSEOUT);
		Event.setEventListener(boxMarkerDiv, new EventListener() {
			public void onBrowserEvent(Event event) {
				if (event.getTypeInt() == Event.ONMOUSEOVER) {
					RootPanel.get().setWidgetPosition(detailsPopup, 
							boxMarkerDiv.getAbsoluteLeft(), 
							boxMarkerDiv.getAbsoluteTop() +  boxMarkerDiv.getOffsetHeight());
					detailsPopup.setVisible(true);
				} else if (event.getTypeInt() == Event.ONMOUSEOUT) {
					if (!detailsPopup.contains(event.getClientX(), event.getClientY()))
						detailsPopup.setVisible(false);
				}
			}
		});
		
		detailsPopup = new DetailsPopup(annotatable, a, labels);
		detailsPopup.setVisible(false);
		
		RootPanel.get().add(detailsPopup);
	}

}
