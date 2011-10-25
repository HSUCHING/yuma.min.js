package at.ait.dme.yumaJS.client.annotation.widgets;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.init.Labels;
import at.ait.dme.yumaJS.client.io.Delete;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

public class InfoPopup extends Composite {
	
	protected FlowPanel container;
	
	public InfoPopup(final Annotatable annotatable, final Annotation a, Labels labels) {
		AnnotationWidget annotationWidget = new AnnotationWidget(a); 
		
		annotationWidget.addEditClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				annotatable.removeAnnotation(a);
				annotatable.editAnnotation(a);
			}
		});
		
		annotationWidget.addDeleteClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (annotatable.getServerURL() == null) {
					annotatable.removeAnnotation(a);
				} else {
					Delete.executeJSONP(annotatable.getServerURL(), a.getID(), new AsyncCallback<Void>() {
						public void onSuccess(Void result) {
							annotatable.removeAnnotation(a);
						}			

						public void onFailure(Throwable t) {
							YUMA.nonFatalError(t.getMessage());
						}
					});
				}							
			}
		});
				
		container = new FlowPanel();
		container.setStyleName("annotation-popup");		
		container.add(annotationWidget);	
		setVisible(false);
		
		initWidget(container);
		
		addDomHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				setVisible(false);
			}
		}, MouseOutEvent.getType());
	}
	
	@Override
	public void setVisible(boolean visible) {
		container.setVisible(true);
		Style style = container.getElement().getStyle();
		if (visible) {
			style.setVisibility(Visibility.VISIBLE);
			style.setOpacity(1);
		} else {
			style.setVisibility(Visibility.HIDDEN);
			style.setOpacity(0);
		}
	}
	
	public boolean contains(int x, int y) {
		int left = container.getAbsoluteLeft();
		int top = container.getAbsoluteTop();
		int w = container.getOffsetWidth();
		int h = container.getOffsetHeight();
		
		if (x < left)
			return false;
		
		if (x > left + w)
			return false;
		
		if (y < top)
			return false;
		
		if (y > top + h)
			return false;
		
		return true;
	}

}
