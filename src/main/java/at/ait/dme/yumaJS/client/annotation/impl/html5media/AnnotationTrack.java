package at.ait.dme.yumaJS.client.annotation.impl.html5media;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.ui.Composite;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.Range;
import at.ait.dme.yumaJS.client.init.InitParams;
import at.ait.dme.yumaJS.client.init.Labels;

public class AnnotationTrack extends Composite {
	
	private CanvasElement canvasElement;
	
	private Context2d context;
	
	private ProgressBar progressBar;
	
	/*
	private Map<Annotation, InfoPopup> annotations = new HashMap<Annotation, InfoPopup>();
	
	private InfoPopup currentPopup = null;
	*/
	
	public AnnotationTrack(final ProgressBar progressBar, InitParams params) throws InadequateBrowserException {
		this.progressBar = progressBar;
		
		Canvas annotationTrack = Canvas.createIfSupported();
		if (annotationTrack == null)
			throw new InadequateBrowserException("HTML5 Canvas not supported");
		
		canvasElement = annotationTrack.getCanvasElement();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
		    public void execute () {
				canvasElement.setWidth(progressBar.getOffsetWidth());
				canvasElement.setHeight(progressBar.getOffsetHeight());
		    }
		});
		context = annotationTrack.getContext2d();
		
		progressBar.addMouseMoveHandler(new MouseMoveHandler() {
			public void onMouseMove(MouseMoveEvent event) {
				Annotation a = getAnnotation(event.getX());
				if (a == null) {
					clearCurrentPopup();
				} else {
					showAnnotationAt(toRange(a.getFragment()).getFrom());
				}
			}
		});
		
		initWidget(annotationTrack);
	}
	
	public String toFragment(BoundingBox bbox, Range range) {
		// TODO Auto-generated method stub
		return null;
	}

	public Range toRange(String fragment) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void showAnnotationAt(double time) {
		Annotation a = getAnnotation(time);
		if (a == null) {
			clearCurrentPopup();
		} else {
			/*
			if (currentPopup == null || !currentPopup.isVisible()) {
				if (a != null)
					showPopup(
							annotations.get(a), 
							canvasElement.getAbsoluteLeft() + progressBar.toOffsetX(time), 
							canvasElement.getAbsoluteTop() + canvasElement.getOffsetHeight());
			}
			*/
		}
	}
	
	public void clearCurrentPopup() {
		/*
		if (currentPopup != null) {
			currentPopup.removeFromParent();
			currentPopup = null;
		}
		*/
	}
	
	/*
	public InfoPopup getCurrentPopup() {
		return currentPopup;
	}
	*/
	
	/*
	private void showPopup(InfoPopup popup, int x, int y) {
		if (currentPopup != null && currentPopup != popup) {
			clearCurrentPopup();
		}
		
		if (currentPopup == null) {
			currentPopup = popup;			
			RootPanel.get().add(currentPopup, x, y);
		}
		
		currentPopup.setVisible(true);
	}
	*/
	
	public void addAnnotation(Annotatable annotatable, Annotation a, Labels labels) {
		/*
		InfoPopup popup = new InfoPopup(annotatable, a, labels);
		annotations.put(a, popup);
		*/
		refresh();
	}
	
	public void removeAnnotation(Annotation a) {
		/*
		annotations.get(a).removeFromParent();
		annotations.remove(a);
		*/
		refresh();
	}
	
	private Annotation getAnnotation(double time) {
		// TODO make this more efficient!
		// TODO make this handle 'smallest first' overlap scenarios
		/*
		for (Annotation a : annotations.keySet()) {
			Range r = toRange(a.getFragment());
			if (time >= r.getFrom() && time <= r.getTo()) {
				return a;
			}
		}
		*/
		
		return null;
	}
	
	private Annotation getAnnotation(int offsetX) {
		return getAnnotation(progressBar.toTime(offsetX));
	}
	
	private void refresh() {
		context.clearRect(0, 0, canvasElement.getWidth(), canvasElement.getHeight());
		context.setFillStyle("#ffa500");
		
		/*
		for (Annotation a : annotations.keySet()) {
			Range r = toRange(a.getFragment());
			int start = progressBar.toOffsetX(r.getFrom());
			int end = progressBar.toOffsetX(r.getTo());
			context.setGlobalAlpha(0.8);
			context.fillRect(start, 0, 1, canvasElement.getHeight());
			context.fillRect(end, 0, 1, canvasElement.getHeight());
			
			context.setGlobalAlpha(0.4);
			context.fillRect(start, 0, end - start, canvasElement.getHeight());
		}
		*/
	}

}
