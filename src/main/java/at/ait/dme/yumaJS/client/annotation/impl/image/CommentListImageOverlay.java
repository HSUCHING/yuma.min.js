package at.ait.dme.yumaJS.client.annotation.impl.image;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
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
public class CommentListImageOverlay implements CompoundOverlay {
	
	private ImageFragmentWidget bboxOverlay;
	
	private AnnotationListWidget annotationListWidget;
	
	private AbsolutePanel annotationLayer;
	
	public CommentListImageOverlay(Annotation rootAnnotation, Annotatable annotatable, 
			final AbsolutePanel annotationLayer) {

		this.annotationLayer = annotationLayer;

		final BoundingBox bbox = annotatable.toBoundingBox(rootAnnotation.getFragment());		
		bboxOverlay = new ImageFragmentWidget(annotatable, annotationLayer, bbox);
		
		bboxOverlay.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				if (!annotationListWidget.isEditing()) {
					refresh();
					annotationListWidget.setVisible(true);
				}
			}
		});
		
		bboxOverlay.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				boolean contains = annotationListWidget.contains(
						event.getRelativeX(annotationLayer.getElement()) + annotationLayer.getAbsoluteLeft(), 
						event.getRelativeY(annotationLayer.getElement()) + annotationLayer.getAbsoluteTop());
				
				boolean editing = annotationListWidget.isEditing();
				
				if (!contains && !editing)
					annotationListWidget.setVisible(false);
			}
		});
		
		bboxOverlay.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				annotationListWidget.showCommentWidget();
			}
		});
		
		bboxOverlay.setSelectionChangeHandler(new SelectionChangeHandler() {
			public void onRangeChanged(Range range) { }
			
			public void onBoundsChanged(BoundingBox bbox) {
				refresh();				
			}
		});
		
		annotationListWidget = new AnnotationListWidget(rootAnnotation, bboxOverlay, annotatable);
		
		annotationListWidget.addDomHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				if (!annotationListWidget.isEditing())
					annotationListWidget.setVisible(false);
			}
		}, MouseOutEvent.getType());
		
		annotationLayer.add(annotationListWidget);		
		refresh();
	}
	
	private void refresh() {
		BoundingBox bbox = bboxOverlay.getBoundingBox();
		annotationLayer.add(annotationListWidget, bbox.getX(), bbox.getY() + bbox.getHeight() + 2);		
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
	
	public void edit(Annotation a) {
		annotationListWidget.edit(a);
	}
	
	public void setZIndex(int idx) {
		bboxOverlay.setZIndex(idx);
	}
	
	public void destroy() {
		bboxOverlay.destroy();
		annotationListWidget.removeFromParent();
	}

	public int compareTo(CompoundOverlay other) {
		if (!(other instanceof CommentListImageOverlay))
			return 0;
		
		CommentListImageOverlay overlay = (CommentListImageOverlay) other;
		
		// Delegate to bbox overlay
		return bboxOverlay.compareTo(overlay.bboxOverlay);
	}
	
}
