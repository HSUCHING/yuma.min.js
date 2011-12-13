package at.ait.dme.yumaJS.client.annotation.impl.openlayers;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.AbsolutePanel;

import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.BoxMarker;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.BoxesLayer;
import at.ait.dme.yumaJS.client.annotation.ui.AnnotationListWidget;
import at.ait.dme.yumaJS.client.annotation.ui.AnnotationWidget;
import at.ait.dme.yumaJS.client.annotation.ui.CompoundOverlay;
import at.ait.dme.yumaJS.client.annotation.ui.AnnotationWidget.AnnotationWidgetEditHandler;
import at.ait.dme.yumaJS.client.annotation.ui.edit.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.ui.edit.Range;
import at.ait.dme.yumaJS.client.annotation.ui.edit.Selection.SelectionChangeHandler;

/**
 * This is the image annotation overlay type that is used when reply functionality is ENABLED. 
 * It consists of a single {@link ImageFragmentWidget} with a list of {@link AnnotationWidget}s
 * underneath. Note: this overlay type will show the fragment of the first annotation in the 
 * reply thread. If there are replies in the thread that contain a fragment, it will be ignored
 * by this overlay type. In editing mode, the entire list of {@link AnnotationWidget}s stays 
 * clamped to the lower-left corner of the first annotation's {@link ImageFragmentWidget}.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class CommentListOpenLayersOverlay implements OpenLayersCompoundOverlay {
	
	private OpenLayersFragmentWidget bboxOverlay;
	
	private AnnotationListWidget annotationListWidget;
	
	private AbsolutePanel editingLayer;
	
	public CommentListOpenLayersOverlay(Annotation rootAnnotation, BoxesLayer annotationLayer,
			AbsolutePanel editingLayer, OpenLayersAnnotationLayer annotatable) {

		this.editingLayer = editingLayer;

		bboxOverlay = new OpenLayersFragmentWidget(
				annotatable.toOpenLayersBounds(rootAnnotation.getFragment()),
				annotationLayer, editingLayer, annotatable);
		
		bboxOverlay.setSelectionChangeHandler(new SelectionChangeHandler() {
			public void onRangeChanged(Range range) { }
			
			public void onBoundsChanged(BoundingBox bbox) {
				refresh();				
			}
		});
		
		bboxOverlay.setEventListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				if (event.getTypeInt() == Event.ONMOUSEOUT) {
					if (!annotationListWidget.contains(event.getClientX(), event.getClientY())
						&& !annotationListWidget.isEditing())
						
						annotationListWidget.setVisible(false);
				} else if (event.getTypeInt() == Event.ONMOUSEWHEEL) {
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						public void execute() {
							refresh();
						}
					});
				} else {
					if (!annotationListWidget.isEditing()) {
						refresh();
						annotationListWidget.setVisible(true);
					}
				}
			}
		});
		
		annotationListWidget = new AnnotationListWidget(rootAnnotation, bboxOverlay, annotatable);
		
		annotationListWidget.addDomHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				if (!annotationListWidget.isEditing())
					annotationListWidget.setVisible(false);
			}
		}, MouseOutEvent.getType());
		
		editingLayer.add(annotationListWidget);		
		refresh();
	}
	
	private void refresh() {
		BoundingBox bbox = bboxOverlay.getBoundingBox();
		editingLayer.add(annotationListWidget, bbox.getX(), bbox.getY() + bbox.getHeight() + 2);		
	}
	
	public void addAnnotationWidgetEditHandler(Annotation a,
			AnnotationWidgetEditHandler handler) {
		
		annotationListWidget.addAnnotationWidgetEditHandler(a, handler);
	}
	
	public void addToList(Annotation a) {
		annotationListWidget.addToList(a);
	}

	public void removeAnnotationWidgetEditHandler(Annotation a,
			AnnotationWidgetEditHandler handler) {
		
		annotationListWidget.removeAnnotationWidgetEditHandler(a, handler);
	}

	public void updateAnnotation(String id, Annotation updated) {
		annotationListWidget.setAnnotation(id, updated);
	}
	
	public void removeAnnotation(String id) {
		annotationListWidget.removeFromList(id);
	}
	
	public BoxMarker getMarker() {
		return bboxOverlay.getBoxMarker();
	}
	
	public void edit(Annotation a) {
		annotationListWidget.edit(a);
	}
	
	public void setZIndex(int idx) {
		bboxOverlay.setZIndex(idx);
	}
	
	public void destroy() {
		annotationListWidget.removeFromParent();
	}

	public int compareTo(CompoundOverlay other) {
		if (!(other instanceof CommentListOpenLayersOverlay))
			return 0;
		
		CommentListOpenLayersOverlay overlay = (CommentListOpenLayersOverlay) other;
		
		// Delegate to bbox overlay
		return bboxOverlay.compareTo(overlay.bboxOverlay);
	}
	
}
