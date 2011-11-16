package at.ait.dme.yumaJS.client.annotation.impl.image;

import java.util.List;

import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.impl.image.widgets.BoundingBoxOverlay;
import at.ait.dme.yumaJS.client.annotation.widgets.AnnotationWidget.AnnotationWidgetEditHandler;

/**
 * An abstract base class for image annotation overlays.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public abstract class ImageAnnotationOverlay {
	
	public abstract void edit(Annotation a);

	public abstract void edit(Annotation a, AnnotationWidgetEditHandler handler);
	
	public abstract List<BoundingBoxOverlay> getBoundingBoxOverlays();
		
	public abstract void destroy();
	
}
