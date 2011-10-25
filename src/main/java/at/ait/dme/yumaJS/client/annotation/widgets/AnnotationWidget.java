package at.ait.dme.yumaJS.client.annotation.widgets;

import java.util.Date;

import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.init.Labels;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.PushButton;

public class AnnotationWidget extends Composite {
	
	private FlowPanel panel;
	
	private PushButton btnEdit, btnDelete;
	
	private static final String DATE_FORMAT = "MMMM dd, yyyy 'at' HH:mm"; 
	
	public AnnotationWidget(final Annotation annotation, Labels labels) {
		panel = new FlowPanel();
		panel.setStyleName("yuma-annotation");
		
		btnDelete = new PushButton();
		btnDelete.setStyleName("yuma-annotation-btn");
		btnDelete.addStyleName("yuma-annotation-btn-delete");
		btnDelete.getElement().getStyle().setFloat(Float.RIGHT);
		btnDelete.getElement().getStyle().setCursor(Cursor.POINTER);
		btnDelete.setVisible(false);
		panel.add(btnDelete);
		
		btnEdit = new PushButton();
		btnEdit.setStyleName("yuma-annotation-btn");
		btnEdit.addStyleName("yuma-annotation-btn-edit");
		btnEdit.getElement().getStyle().setFloat(Float.RIGHT);
		btnEdit.getElement().getStyle().setCursor(Cursor.POINTER);
		btnEdit.setVisible(false);
		panel.add(btnEdit);
		
		if (labels == null) {
			btnDelete.setTitle("Delete this Comment");
			btnEdit.setTitle("Edit this Comment");
		} else {
			btnDelete.setTitle(labels.deleteTooltip());
			btnEdit.setTitle(labels.editTooltip());
		}
		
		// Username will be undefined in server-less mode!
		InlineHTML username = null;
		if (annotation.getUserRealName() != null) {
			username = new InlineHTML(annotation.getUserRealName());
		} else if (annotation.getUsername() != null) {
			username = new InlineHTML(annotation.getUsername());
		}
		
		if (username != null) {
			username.setStyleName("yuma-annotation-username");
			panel.add(username);
		}

		// Timestamps will be -1 in server-less mode!
		InlineHTML timestamp = new InlineHTML();
		long modified = annotation.getModified();
		if (modified > 0) {
			timestamp.setHTML(DateTimeFormat.getFormat(DATE_FORMAT).format(new Date(modified)));
			timestamp.setStyleName("yuma-annotation-modified");
		}
		
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
