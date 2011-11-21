package at.ait.dme.yumaJS.client.annotation.impl.openlayers.widets;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.impl.image.ImageAnnotationOverlay;
import at.ait.dme.yumaJS.client.annotation.impl.image.widgets.SingleImageAnnotationOverlay;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.BoxMarker;
import at.ait.dme.yumaJS.client.annotation.widgets.AnnotationWidget;
import at.ait.dme.yumaJS.client.annotation.widgets.AnnotationWidget.AnnotationWidgetEditHandler;
import at.ait.dme.yumaJS.client.init.Labels;

public class SingleOpenLayersAnnotationOverlay extends ImageAnnotationOverlay {
		
	private BoxMarker boxMarker;
	
	private AnnotationWidget annotationWidget;
	
	public SingleOpenLayersAnnotationOverlay(Annotatable annotatable,  Annotation a, BoxMarker marker, Labels labels) {
		this.boxMarker = marker;
		
		Element boxMarkerDiv = marker.getDiv();
		
		Style markerStyle = boxMarkerDiv.getStyle();
		markerStyle.clearBorderColor();
		markerStyle.clearBorderWidth();
		markerStyle.clearBorderStyle();
		boxMarker.getDiv().setClassName("annotation-bbox-outer");
		
		FlowPanel innerBorder = new FlowPanel();
		innerBorder.setWidth("100%");
		innerBorder.setHeight("100%");
		innerBorder.setStyleName("annotation-bbox-inner");
		boxMarker.getDiv().appendChild(innerBorder.getElement());
		
		DOM.sinkEvents(boxMarkerDiv, 
				Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.ONMOUSEMOVE | Event.ONMOUSEWHEEL);
		
		Event.setEventListener(boxMarkerDiv, new EventListener() {
			public void onBrowserEvent(Event event) {
				if (event.getTypeInt() == Event.ONMOUSEOUT) {
					if (!annotationWidget.contains(event.getClientX(), event.getClientY()))
						annotationWidget.setVisible(false);
				} else if (event.getTypeInt() == Event.ONMOUSEWHEEL) {
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						public void execute() {
							refresh();
						}
					});
				} else {
					refresh();
				}
			}
		});
		
		annotationWidget = new AnnotationWidget(annotatable, a, labels);
		annotationWidget.setVisible(false);
		RootPanel.get().add(annotationWidget);
		refresh();
	}
	
	private void refresh() {
		Element boxMarkerDiv = boxMarker.getDiv();
		RootPanel.get().setWidgetPosition(annotationWidget, 
				boxMarkerDiv.getAbsoluteLeft(), 
				boxMarkerDiv.getAbsoluteTop() +  boxMarkerDiv.getOffsetHeight());
	}

	@Override
	public void setAnnotationWidgetEditHandler(Annotation a,
			AnnotationWidgetEditHandler handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void edit(Annotation a) {
		// TODO Auto-generated method stub
		
	}
	
	public void destroy() {
		annotationWidget.removeFromParent();
	}

	@Override
	public void setZIndex(int idx) {
		boxMarker.getDiv().getStyle().setZIndex(idx);
	}

	public int compareTo(ImageAnnotationOverlay other) {
		if (!(other instanceof SingleOpenLayersAnnotationOverlay))
			return 0;
		
		SingleOpenLayersAnnotationOverlay overlay = (SingleOpenLayersAnnotationOverlay) other;
		int thisArea = boxMarker.getDiv().getOffsetWidth() * boxMarker.getDiv().getOffsetHeight();
		int otherArea = overlay.boxMarker.getDiv().getOffsetWidth() * overlay.boxMarker.getDiv().getOffsetHeight();
		
		if (thisArea > otherArea)
			return -1;
		
		if (thisArea < otherArea)
			return 1;
		
		return 0;
	}

}
