package at.ait.dme.yumaJS.client.annotation.impl.seajax;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.AbsolutePanel;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.impl.image.ImageFragmentWidget;
import at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonAnimationHandler;
import at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonViewer;
import at.ait.dme.yumaJS.client.annotation.ui.AnnotationWidget.AnnotationWidgetEditHandler;
import at.ait.dme.yumaJS.client.annotation.ui.edit.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.ui.edit.Range;
import at.ait.dme.yumaJS.client.annotation.ui.edit.Selection.SelectionChangeHandler;
import at.ait.dme.yumaJS.client.annotation.ui.AnnotationWidget;
import at.ait.dme.yumaJS.client.annotation.ui.CompoundOverlay;

/**
 * An overlay that represents an annotation on a {@link SeajaxAnnotationLayer} by attaching a 
 * {@link ImageFragmentWidget} to a Seadragon rectangle overlay and combining it with a movable
 * {@link SimpleDetailsPopup}.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class SingleSeajaxAnnotationOverlay implements CompoundOverlay {
		
	private AbsolutePanel editingLayer;
	
	private SeajaxFragmentWidget fragmentWidget;
	
	private AnnotationWidget annotationWidget;
	
	public SingleSeajaxAnnotationOverlay(Annotation annotation, SeadragonViewer viewer, 
		AbsolutePanel editingLayer, Annotatable annotatable) {
		
		this.editingLayer = editingLayer;
		
		this.fragmentWidget = new SeajaxFragmentWidget(
				annotatable.toBoundingBox(annotation.getFragment()), 
				viewer, 
				editingLayer,
				annotatable);
		
		this.fragmentWidget.setSelectionChangeHandler(new SelectionChangeHandler() {
			public void onRangeChanged(Range range) { }
			
			public void onBoundsChanged(BoundingBox bbox) {
				refresh();				
			}
		});
		
		this.fragmentWidget.setEventListener(new EventListener() {
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

		// TODO this means we're attaching a listener for EVERY annotation 
		// whereas we really only need to listen for those with visible 
		// popup-> make this more efficient!
		viewer.addAnimationtListener(new SeadragonAnimationHandler() {
			public void onAnimation() {
				if (fragmentWidget.isVisible())
					refresh();
			}
		});
		
		annotationWidget = new AnnotationWidget(annotation, fragmentWidget, annotatable);
		annotationWidget.addDomHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				if (!annotationWidget.isEditing())
					annotationWidget.setVisible(false);
			}
		}, MouseOutEvent.getType());
		
		annotationWidget.setVisible(false);
		editingLayer.add(annotationWidget);
		refresh();
	}
	
	private void refresh() {
		BoundingBox bbox = fragmentWidget.getBoundingBox();
		editingLayer.setWidgetPosition(annotationWidget, bbox.getX(), bbox.getY() +  bbox.getHeight() + 2);
	}

	public void addAnnotationWidgetEditHandler(Annotation a, AnnotationWidgetEditHandler handler) {
		// TODO code duplication with all Single_AnnotationOverlay impls -> refactor
		annotationWidget.addAnnotationWidgetEditHandler(handler);
	}

	public void removeAnnotationWidgetEditHandler(Annotation a, AnnotationWidgetEditHandler handler) {
		annotationWidget.removeAnnotationWidgetEditHandler(handler);
	}

	public void updateAnnotation(String id, Annotation updated) {
		annotationWidget.setAnnotation(updated);
	}

	public void removeAnnotation(String id) {
		// Not supported by this CompoundOverlayType
		// use destroy() instead!
	}

	public void edit(Annotation a) {
		annotationWidget.edit();	
	}
	
	public void destroy() {
		fragmentWidget.destroy();
		annotationWidget.removeFromParent();
	}

	public void setZIndex(int idx) {
		fragmentWidget.setZIndex(idx);
	}

	public int compareTo(CompoundOverlay other) {
		if (!(other instanceof SingleSeajaxAnnotationOverlay))
			return 0;
		
		SingleSeajaxAnnotationOverlay overlay = (SingleSeajaxAnnotationOverlay) other;
		
		// Delegate to fragmentWidget overlay
		return fragmentWidget.compareTo(overlay.fragmentWidget);
	}	
	
}
