package at.ait.dme.yumaJS.client.annotation.impl.openlayers.widets;

import com.google.gwt.user.client.ui.AbsolutePanel;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.impl.image.ImageAnnotationOverlay;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.BoxMarker;
import at.ait.dme.yumaJS.client.annotation.widgets.AnnotationWidget;
import at.ait.dme.yumaJS.client.annotation.widgets.AnnotationWidget.AnnotationWidgetEditHandler;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.Range;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.Selection.SelectionChangeHandler;

public class SingleOpenLayersAnnotationOverlay extends ImageAnnotationOverlay {
		
	private AbsolutePanel panel;
	
	private OpenLayersBoundingboxOverlay boxOverlay;
	
	private AnnotationWidget annotationWidget;
	
	public SingleOpenLayersAnnotationOverlay(Annotatable annotatable, Annotation a,
			AbsolutePanel panel, BoxMarker marker) {
		
		this.panel = panel;
		this.boxOverlay = new OpenLayersBoundingboxOverlay(panel, marker);
		
		this.boxOverlay.setSelectionChangeHandler(new SelectionChangeHandler() {
			public void onRangeChanged(Range range) { }
			
			public void onBoundsChanged(BoundingBox bbox) {
				refresh();				
			}
		});

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
		panel.add(annotationWidget);
		refresh();
	}
	
	private void refresh() {
		BoundingBox bbox = boxOverlay.getBoundingBox();
		panel.setWidgetPosition(annotationWidget, bbox.getX(), bbox.getY() +  bbox.getHeight() + 2);
	}

	@Override
	public void setAnnotationWidgetEditHandler(Annotation a, final AnnotationWidgetEditHandler handler) {
		annotationWidget.setAnnotationWidgetEditHandler(new AnnotationWidgetEditHandler() {
			public void onSave(Annotation annotation) {
				if (handler != null)
					handler.onSave(annotation);
			}
			
			public void onCancel() {
				refresh();
				if (handler != null)
					handler.onCancel();
			}
		});		
	}
	
	public BoxMarker getMarker() {
		return boxOverlay.getBoxMarker();
	}

	@Override
	public void edit(Annotation a) {
		annotationWidget.edit();
		annotationWidget.setVisible(true);		
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
