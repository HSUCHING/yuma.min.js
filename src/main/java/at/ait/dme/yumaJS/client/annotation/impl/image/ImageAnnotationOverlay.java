package at.ait.dme.yumaJS.client.annotation.impl.image;

import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.widgets.AnnotationWidget.AnnotationWidgetEditHandler;

/**
 * An abstract base class for image annotation overlays.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public abstract class ImageAnnotationOverlay implements Comparable<ImageAnnotationOverlay> {
	
	public abstract void setAnnotationWidgetEditHandler(Annotation a, AnnotationWidgetEditHandler handler);
	
	public abstract void edit(Annotation a);
	
	public abstract void setZIndex(int idx);
		
	public abstract void destroy();
	
}
