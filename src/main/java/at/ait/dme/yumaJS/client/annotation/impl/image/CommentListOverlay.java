package at.ait.dme.yumaJS.client.annotation.impl.image;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.gui.AnnotationListWidget;
import at.ait.dme.yumaJS.client.annotation.gui.AnnotationWidget;
import at.ait.dme.yumaJS.client.annotation.gui.CompoundOverlay;
import at.ait.dme.yumaJS.client.annotation.gui.AnnotationWidget.AnnotationWidgetEditHandler;
import at.ait.dme.yumaJS.client.annotation.gui.edit.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.gui.edit.Range;
import at.ait.dme.yumaJS.client.annotation.gui.edit.Selection.SelectionChangeHandler;

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
public class CommentListOverlay implements CompoundOverlay {
	
	private ImageFragmentWidget bboxOverlay;
	
	private AnnotationListWidget annotationListWidget;
	
	private AbsolutePanel annotationLayer;
	
	public CommentListOverlay(Annotation rootAnnotation, Annotatable annotatable, 
			final AbsolutePanel annotationLayer) {

		this.annotationLayer = annotationLayer;

		final BoundingBox bbox = annotatable.toBoundingBox(rootAnnotation.getFragment());		
		bboxOverlay = new ImageFragmentWidget(annotationLayer, bbox);
		
		bboxOverlay.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				annotationListWidget.setVisible(true);
			}
		});
		
		bboxOverlay.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				/*
				if (!annotationWidget.contains(
						event.getRelativeX(annotationLayer.getElement()) + annotationLayer.getAbsoluteLeft(), 
						event.getRelativeY(annotationLayer.getElement()) + annotationLayer.getAbsoluteTop()))
					
					annotationWidget.setVisible(false);
				*/
			}
		});
		
		bboxOverlay.setSelectionChangeHandler(new SelectionChangeHandler() {
			public void onRangeChanged(Range range) { }
			
			public void onBoundsChanged(BoundingBox bbox) {
				refresh();				
			}
		});
		
		this.annotationListWidget = new AnnotationListWidget(rootAnnotation, bboxOverlay, annotatable);
		annotationLayer.add(annotationListWidget);		
		refresh();
	}
	
	private void refresh() {
		BoundingBox bbox = bboxOverlay.getBoundingBox();
		annotationLayer.add(annotationListWidget, bbox.getX(), bbox.getY() + bbox.getHeight() + 2);		
	}

	public void setAnnotationWidgetEditHandler(Annotation a,
			AnnotationWidgetEditHandler handler) {
		
		annotationListWidget.setAnnotationWidgetEditHandler(a, handler);
	}

	public void edit(Annotation a) {
		annotationListWidget.edit(a);
	}
	
	public void setZIndex(int idx) {
		bboxOverlay.setZIndex(idx);
	}
	
	public void destroy() {
		// TODO implement
	}

	public int compareTo(CompoundOverlay o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
