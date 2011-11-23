package at.ait.dme.yumaJS.client.annotation.gui;

import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.gui.AnnotationWidget.AnnotationWidgetEditHandler;

public interface AnnotationOverlay extends Comparable<AnnotationOverlay> {
	
	public void setAnnotationWidgetEditHandler(Annotation a, AnnotationWidgetEditHandler handler);
	
	public void edit(Annotation a);
	
	public void setZIndex(int idx);
		
	public void destroy();
	
}
