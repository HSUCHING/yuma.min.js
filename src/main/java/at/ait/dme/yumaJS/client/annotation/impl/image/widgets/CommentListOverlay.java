package at.ait.dme.yumaJS.client.annotation.impl.image.widgets;

import java.util.List;

import com.google.gwt.dev.util.collect.HashMap;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.impl.image.ImageAnnotationOverlay;
import at.ait.dme.yumaJS.client.annotation.widgets.AnnotationWidget;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.AnnotationEditHandler;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.selection.BoundingBox;
import at.ait.dme.yumaJS.client.init.Labels;

/**
 * This is the image annotation overlay type that is used when reply functionality is ENABLED. 
 * It consists of a single {@link BoundingBoxOverlay} with a list of {@link AnnotationWidget}s
 * underneath. Note: this overlay type will show the fragment of the first annotation in the 
 * reply thread. If there are replies in the thread that contain a fragment, it will be ignored
 * by this overlay type. In editing mode, the entire list of {@link AnnotationWidget}s stays 
 * clamped to the lower-left corner of the first annotation's {@link BoundingBoxOverlay}.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class CommentListOverlay extends ImageAnnotationOverlay {

	private Annotation rootAnnotation;
	
	private Annotatable annotatable;

	private AbsolutePanel annotationLayer;
	
	private BoundingBoxOverlay bboxOverlay;
	
	private AnnotationWidget rootAnnotationWidget;
	
	private FlowPanel annotationListPanel = new FlowPanel();
	
	private HashMap<Annotation, AnnotationWidget> annotations = new HashMap<Annotation, AnnotationWidget>();
	
	public CommentListOverlay(Annotation rootAnnotation, Annotatable annotatable, AbsolutePanel annotationLayer, Labels labels) {
		this.rootAnnotation = rootAnnotation;
		this.annotatable = annotatable;
		this.annotationLayer = annotationLayer;
		
		final BoundingBox bbox = annotatable.toBoundingBox(rootAnnotation.getFragment());
		
		bboxOverlay = new BoundingBoxOverlay(annotationLayer, bbox);
		
		bboxOverlay.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				annotationListPanel.setVisible(true);
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
		
		rootAnnotationWidget = new AnnotationWidget(rootAnnotation, labels);
		
		/*
		annotationWidget.addDomHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				if (!annotationWidget.isEditing())
					annotationWidget.setVisible(false);
			}
		}, MouseOutEvent.getType());
		
		annotationWidget.setVisible(false);
		*/
		annotationLayer.add(bboxOverlay, bbox.getX(), bbox.getY());
		annotationLayer.add(rootAnnotationWidget, bbox.getX(), bbox.getY() + bbox.getHeight() + 2);
	}

	@Override
	public void edit(Annotation a, AnnotationEditHandler handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<BoundingBoxOverlay> getBoundingBoxOverlays() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void destroy() {
		// TODO implement
	}
	
}
