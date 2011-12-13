package at.ait.dme.yumaJS.client;

import at.ait.dme.yumaJS.client.annotation.impl.openlayers.OpenLayersAnnotationLayer;
import at.ait.dme.yumaJS.client.init.InitParams;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;

public class TestOpenLayersHosted implements EntryPoint {

	public void onModuleLoad() {
		final OpenLayersAnnotationLayer annotationLayer = 
			new OpenLayersAnnotationLayer(
					getMap(), 
					"http://test.jpg",
					createInitParams());
		
		PushButton annotate = new PushButton("Add Note");
		annotate.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				annotationLayer.createNewAnnotation();
			}
		});
		
		RootPanel.get().add(annotate);
	}
	
	private native JavaScriptObject getMap() /*-{
		return $wnd.map;
	}-*/;
	
	private native InitParams createInitParams() /*-{
		return {
			enableReplies:true
			// serverURL:"http://dme.ait.ac.at/yuma4j-server"
		};
	}-*/;
	
}
