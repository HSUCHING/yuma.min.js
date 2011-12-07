package at.ait.dme.yumaJS.client.annotation.ui;

import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.ui.AnnotationWidget.AnnotationWidgetEditHandler;

/**
 * {@link CompoundOverlay} is a common interface for GUI overlays 
 * that embody an annotation, or a thread of annotations. In general,
 * implementations of {@link CompoundOverlay} consist of at least
 * one {@link AnnotationWidget} and one {@link FragmentWidget}.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public interface CompoundOverlay extends Comparable<CompoundOverlay> {
	
	public void addAnnotationWidgetEditHandler(Annotation a, AnnotationWidgetEditHandler handler);
	
	public void removeAnnotationWidgetEditHandler(Annotation a, AnnotationWidgetEditHandler handler);
	
	public void updateAnnotation(String id, Annotation updated);
	
	public void removeAnnotation(String id);
	
	public void edit(Annotation a);
	
	public void setZIndex(int idx);
		
	public void destroy();
	
}
