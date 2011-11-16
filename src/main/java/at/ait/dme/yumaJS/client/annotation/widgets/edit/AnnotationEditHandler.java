package at.ait.dme.yumaJS.client.annotation.widgets.edit;

import at.ait.dme.yumaJS.client.annotation.Annotation;

public interface AnnotationEditHandler {

	public void onSave(Annotation annotation);
	
	public void onCancel();
	
}
