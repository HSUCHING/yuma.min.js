package at.ait.dme.yumaJS.client.annotation.impl.openlayers.widets;

import com.google.gwt.user.client.ui.RootPanel;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.impl.image.ImageAnnotationOverlay;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.BoxMarker;
import at.ait.dme.yumaJS.client.annotation.widgets.AnnotationWidget;
import at.ait.dme.yumaJS.client.annotation.widgets.AnnotationWidget.AnnotationWidgetEditHandler;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.BoundingBox;
import at.ait.dme.yumaJS.client.init.Labels;

public class SingleOpenLayersAnnotationOverlay extends ImageAnnotationOverlay {
		
	private OpenLayersBoundingboxOverlay boxOverlay;
	
	private AnnotationWidget annotationWidget;
	
	public SingleOpenLayersAnnotationOverlay(Annotatable annotatable,  Annotation a, BoxMarker marker) {
		this.boxOverlay = new OpenLayersBoundingboxOverlay(marker);

		/*
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
		*/
		
		annotationWidget = new AnnotationWidget(a, boxOverlay, annotatable);
		annotationWidget.setVisible(false);
		RootPanel.get().add(annotationWidget);
		refresh();
	}
	
	private void refresh() {
		BoundingBox bbox = boxOverlay.getBoundingBox();
		RootPanel.get().setWidgetPosition(annotationWidget, bbox.getX(), bbox.getY() +  bbox.getHeight());
	}

	@Override
	public void setAnnotationWidgetEditHandler(Annotation a,
			AnnotationWidgetEditHandler handler) {
		// TODO Auto-generated method stub
		
	}
	
	public BoxMarker getMarker() {
		return boxOverlay.getBoxMarker();
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
		boxOverlay.setZIndex(idx);
	}

	public int compareTo(ImageAnnotationOverlay other) {
		if (!(other instanceof SingleOpenLayersAnnotationOverlay))
			return 0;
		
		SingleOpenLayersAnnotationOverlay overlay = (SingleOpenLayersAnnotationOverlay) other;
		
		// Delegate to bbox overlay
		return boxOverlay.compareTo(overlay.boxOverlay);
	}

}
