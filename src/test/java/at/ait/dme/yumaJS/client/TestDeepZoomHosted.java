package at.ait.dme.yumaJS.client;

import at.ait.dme.yumaJS.client.annotation.impl.seajax.SeajaxAnnotationLayer;
import at.ait.dme.yumaJS.client.init.InitParams;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;

public class TestDeepZoomHosted implements EntryPoint {

	private SeajaxAnnotationLayer canvas = null;
	
	public void onModuleLoad() {				
		final PushButton annotate = new PushButton("Add Note");
		annotate.setStyleName("toggle-annotation");
		annotate.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				canvas.createNewAnnotation();
			}
		});
		
		RootPanel.get().add(annotate);
		
		Timer t = new Timer() {
			@Override
			public void run() {
				pollLoadStatus(this);
			}
		};
		pollLoadStatus(t);
	}
	
	private void pollLoadStatus(Timer t) {		
		if (getViewer() == null) {
			t.schedule(100);
		} else {
			canvas = new SeajaxAnnotationLayer("viewer", getViewer(), createInitParams());
		}
	}
	
	private native JavaScriptObject getViewer() /*-{
		return $wnd.viewer;
	}-*/;
	
	private native InitParams createInitParams() /*-{
		return {
			enableReplies:true,
			serverURL:"http://dme.ait.ac.at/yuma4j-server"
		};
	}-*/;

}
