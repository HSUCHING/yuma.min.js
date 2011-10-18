package at.ait.dme.yumaJS.client.annotation.widgets;

import java.util.Date;

import at.ait.dme.yumaJS.client.annotation.Annotation;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.PushButton;

public class AnnotationWidget extends Composite {
	
	private FlowPanel panel;
	
	private PushButton btnEdit, btnDelete;
	
	public AnnotationWidget(Annotation a) {
		panel = new FlowPanel();
		panel.setStyleName("yuma-annotation");
		
		btnDelete = new PushButton("delete");
		btnDelete.setStyleName("yuma-annotation-btn");
		btnDelete.getElement().getStyle().setFloat(Float.RIGHT);
		btnDelete.getElement().getStyle().setCursor(Cursor.POINTER);
		btnDelete.setVisible(false);
		panel.add(btnDelete);
		
		btnEdit = new PushButton("edit");
		btnEdit.setStyleName("yuma-annotation-btn");
		btnEdit.getElement().getStyle().setFloat(Float.RIGHT);
		btnEdit.getElement().getStyle().setCursor(Cursor.POINTER);
		btnEdit.setVisible(false);
		panel.add(btnEdit);
		
		InlineHTML username = new InlineHTML();
		if (a.getUserRealName() == null) {
			username.setHTML(a.getUsername());
		} else {
			username.setHTML(a.getUserRealName());
		}
		username.setStyleName("yuma-annotation-username");

		
		InlineHTML timestamp = new InlineHTML();
		timestamp.setHTML(new Date((long) a.getModified()).toString());
		timestamp.setStyleName("yuma-annotation-modified");
		
		panel.add(username);
		panel.add(new InlineHTML(a.getText() + "<br/>"));
		panel.add(timestamp);
		
		panel.addDomHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				btnEdit.setVisible(true);
				btnDelete.setVisible(true);
			}
		}, MouseOverEvent.getType());
		panel.addDomHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				btnEdit.setVisible(false);
				btnDelete.setVisible(false);	
			}
		}, MouseOutEvent.getType());
		
		initWidget(panel);
	}
	
	public HandlerRegistration addEditClickHandler(ClickHandler handler) {
		System.out.println("Ok");
		return btnEdit.addClickHandler(handler);
	}
	
	public HandlerRegistration addDeleteClickHandler(ClickHandler handler) {
		return btnDelete.addClickHandler(handler);
	}

}
