package at.ait.dme.yumaJS.client.annotation.impl.image.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dev.util.collect.HashMap;
import com.google.gwt.user.client.ui.AbsolutePanel;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.widgets.AnnotationWidget;
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
public class CommentListOverlay {

	private Annotation rootAnnotation;
	
	private Annotatable annotatable;

	private AbsolutePanel annotationLayer;
	
	private BoundingBoxOverlay bboxOverlay;
	
	private HashMap<Annotation, AnnotationWidget> annotations = new HashMap<Annotation, AnnotationWidget>();
	
	public CommentListOverlay(Annotation rootAnnotation, Annotatable annotatable, AbsolutePanel annotationLayer, Labels labels) {
		this.rootAnnotation = rootAnnotation;
		this.annotatable = annotatable;
		this.annotationLayer = annotationLayer;
	}

}
