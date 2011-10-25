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
	
	public AnnotationWidget(final Annotation annotation) {
		panel = new FlowPanel();
		panel.setStyleName("yuma-annotation");
		
		btnDelete = new PushButton("DELETE");
		btnDelete.setStyleName("yuma-annotation-btn");
		btnDelete.getElement().getStyle().setFloat(Float.RIGHT);
		btnDelete.getElement().getStyle().setCursor(Cursor.POINTER);
		btnDelete.setVisible(false);
		panel.add(btnDelete);
		
		btnEdit = new PushButton("EDIT");
		btnEdit.setStyleName("yuma-annotation-btn");
		btnEdit.getElement().getStyle().setFloat(Float.RIGHT);
		btnEdit.getElement().getStyle().setCursor(Cursor.POINTER);
		btnEdit.setVisible(false);
		panel.add(btnEdit);
		
		InlineHTML username = new InlineHTML();
		if (annotation.getUserRealName() == null) {
			username.setHTML(annotation.getUsername());
		} else {
			username.setHTML(annotation.getUserRealName());
		}
		username.setStyleName("yuma-annotation-username");

		InlineHTML timestamp = new InlineHTML();
		timestamp.setHTML(new Date((long) annotation.getModified()).toString());
		timestamp.setStyleName("yuma-annotation-modified");
		
		panel.add(username);
		panel.add(new InlineHTML(annotation.getText() + "<br/>"));
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
		return btnEdit.addClickHandler(handler);
	}
	
	public HandlerRegistration addDeleteClickHandler(ClickHandler handler) {
		return btnDelete.addClickHandler(handler);
	}

}
