package at.ait.dme.yumaJS.client.annotation.impl.openlayers.widets;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.AbsolutePanel;

import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.gui.AnnotationWidget;
import at.ait.dme.yumaJS.client.annotation.gui.AnnotationOverlay;
import at.ait.dme.yumaJS.client.annotation.gui.AnnotationWidget.AnnotationWidgetEditHandler;
import at.ait.dme.yumaJS.client.annotation.gui.edit.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.gui.edit.Range;
import at.ait.dme.yumaJS.client.annotation.gui.edit.Selection.SelectionChangeHandler;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.OpenLayersAnnotationLayer;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.BoxMarker;

public class SingleOpenLayersAnnotationOverlay implements AnnotationOverlay {
		
	private AbsolutePanel panel;
	
	private OpenLayersBoundingboxOverlay boxOverlay;
	
	private AnnotationWidget annotationWidget;
	
	public SingleOpenLayersAnnotationOverlay(OpenLayersAnnotationLayer annotatable, Annotation a,
			AbsolutePanel panel, BoxMarker marker) {
		
		this.panel = panel;
		this.boxOverlay = new OpenLayersBoundingboxOverlay(panel, marker, annotatable);
		
		this.boxOverlay.setSelectionChangeHandler(new SelectionChangeHandler() {
			public void onRangeChanged(Range range) { }
			
			public void onBoundsChanged(BoundingBox bbox) {
				refresh();				
			}
		});

		this.boxOverlay.setEventListener(new EventListener() {
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
					annotationWidget.setVisible(true);
					refresh();
				}
			}
		});
		
		annotationWidget = new AnnotationWidget(a, boxOverlay, annotatable);
		annotationWidget.setVisible(false);
		panel.add(annotationWidget);
		refresh();
	}
	
	private void refresh() {
		BoundingBox bbox = boxOverlay.getBoundingBox();
		panel.setWidgetPosition(annotationWidget, bbox.getX(), bbox.getY() +  bbox.getHeight() + 2);
	}

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

	public void edit(Annotation a) {
		annotationWidget.edit();
		annotationWidget.setVisible(true);		
	}
	
	public void destroy() {
		annotationWidget.removeFromParent();
	}

	public void setZIndex(int idx) {
		boxOverlay.setZIndex(idx);
	}

	public int compareTo(AnnotationOverlay other) {
		if (!(other instanceof SingleOpenLayersAnnotationOverlay))
			return 0;
		
		SingleOpenLayersAnnotationOverlay overlay = (SingleOpenLayersAnnotationOverlay) other;
		
		// Delegate to bbox overlay
		return boxOverlay.compareTo(overlay.boxOverlay);
	}

}
