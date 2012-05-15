package at.ait.dme.yumaJS.client;

import at.ait.dme.yumaJS.client.annotation.impl.image.ImageAnnotationLayer;
import at.ait.dme.yumaJS.client.init.InitParams;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;

public class TestImageHosted implements EntryPoint {

	public void onModuleLoad() {
		final ImageAnnotationLayer annotationLayer01 = 
			new ImageAnnotationLayer("annotateMe_01", createInitParams());
		
		PushButton annotate01 = new PushButton("Add Note to 1st Image");
		annotate01.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					annotationLayer01.createNewAnnotation();
				}
			});
		
		final ImageAnnotationLayer annotationLayer02 = 
				new ImageAnnotationLayer("annotateMe_02", createInitParams());
		
		PushButton annotate02 = new PushButton("Add Note to 2nd Image");
		annotate02.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				annotationLayer02.createNewAnnotation();
			}
		});
		
		RootPanel.get().add(annotate01);		
		RootPanel.get().add(annotate02);
	}
	
	private native InitParams createInitParams() /*-{
		return {
			enableReplies:true,
			serverURL:"http://dme.ait.ac.at/yuma4j-server"
		};
	}-*/;

}
