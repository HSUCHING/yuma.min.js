package at.ait.dme.yumaJS.client.annotation.gui;

import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.gui.AnnotationWidget.AnnotationWidgetEditHandler;

/**
 * An abstract base class for image annotation overlays.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public abstract class AnnotationOverlay implements Comparable<AnnotationOverlay> {
	
	public abstract void setAnnotationWidgetEditHandler(Annotation a, AnnotationWidgetEditHandler handler);
	
	public abstract void edit(Annotation a);
	
	public abstract void setZIndex(int idx);
		
	public abstract void destroy();
	
}
