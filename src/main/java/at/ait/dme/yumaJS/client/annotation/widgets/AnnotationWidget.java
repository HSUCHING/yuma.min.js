package at.ait.dme.yumaJS.client.annotation.widgets;

import java.util.Date;

import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.init.Labels;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.event.dom.client.ClickEvent;
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

/**
 * The {@link AnnotationWidget} is the elemental GUI element displaying
 * the contents of an annotation:
 * 
 * <ul>
 * <li>the creator's real name or username (not in server-less mode)</li>
 * <li>the annotation text</li>
 * <li>the creation timestamp (not in server-less mode)</li>
 * </ul> 
 * 
 * The {@link AnnotationWidget} also include the edit and delete button
 * elements. (However, it does not define their behavior! This needs to 
 * be defined externally by adding edit/delete {@link ClickHandler}s
 * using the appropriate methods.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at.>
 */
public class AnnotationWidget extends Composite {
	
	private FlowPanel container;
	
	private FlowPanel annotationPanel;
	
	private FlowPanel buttonPanel;
	
	private PushButton btnEdit, btnDelete;
	
	private Annotation annotation;
	
	private static final String CSS_HIDDEN = "yuma-button-hidden";
	private static final String DATE_FORMAT = "MMMM dd, yyyy 'at' HH:mm"; 
	
	public AnnotationWidget(Annotation a, Labels labels) {
		this.annotation = a;
		
		// Construct annotation panel
		annotationPanel = new FlowPanel();
		annotationPanel.setStyleName("yuma-annotation-content");
						
		// Username will be undefined in server-less mode!
		InlineHTML username = null;
		if (annotation.getUserRealName() != null) {
			username = new InlineHTML(annotation.getUserRealName());
		} else if (annotation.getUsername() != null) {
			username = new InlineHTML(annotation.getUsername());
		}
		
		if (username != null) {
			username.setStyleName("yuma-annotation-username");
			annotationPanel.add(username);
		}

		// Timestamps will be -1 in server-less mode!
		InlineHTML timestamp = new InlineHTML();
		long modified = annotation.getModified();
		if (modified > 0) {
			timestamp.setHTML(DateTimeFormat.getFormat(DATE_FORMAT).format(new Date(modified)));
			timestamp.setStyleName("yuma-annotation-modified");
		}
		
		annotationPanel.add(new InlineHTML(toHTML(annotation.getText()) + "<br/>"));
		annotationPanel.add(timestamp);

		annotationPanel.addDomHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				btnEdit.removeStyleName(CSS_HIDDEN);
				btnDelete.removeStyleName(CSS_HIDDEN);
			}
		}, MouseOverEvent.getType());

		// Construct button panel
		buttonPanel = new FlowPanel();
		buttonPanel.setStyleName("yuma-annotation-buttons");
		
		btnDelete = new PushButton();
		btnDelete.setStyleName("yuma-icon-button");
		btnDelete.addStyleName("yuma-button-delete");
		btnDelete.addStyleName(CSS_HIDDEN);
		btnDelete.getElement().getStyle().setFloat(Float.RIGHT);
		btnDelete.getElement().getStyle().setCursor(Cursor.POINTER);
		
		btnEdit = new PushButton();
		btnEdit.setStyleName("yuma-icon-button");
		btnEdit.addStyleName("yuma-button-edit");
		btnEdit.addStyleName(CSS_HIDDEN);
		btnEdit.getElement().getStyle().setFloat(Float.RIGHT);
		btnEdit.getElement().getStyle().setCursor(Cursor.POINTER);
	
		if (labels == null) {
			btnDelete.setTitle("Delete this Comment");
			btnEdit.setTitle("Edit this Comment");
		} else {
			btnDelete.setTitle(labels.deleteTooltip());
			btnEdit.setTitle(labels.editTooltip());
		}
		buttonPanel.add(btnDelete);
		buttonPanel.add(btnEdit);

		// Wrap everything into the container panel
		container = new FlowPanel();
		container.setStyleName("yuma-annotation");
		container.add(annotationPanel);
		container.add(buttonPanel);
		
		container.addDomHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				btnEdit.addStyleName(CSS_HIDDEN);
				btnDelete.addStyleName(CSS_HIDDEN);
			}
		}, MouseOutEvent.getType());

		initWidget(container);
	}
	
	private native String toHTML(String text) /*-{
	    var exp = /(\b(https?|ftp|file):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/ig;
	    return text.replace(exp,"<a href=\"$1\" target=\"blank\">$1</a>"); 
	}-*/;

	public void makeEditable(final EditHandler editHandler, Labels labels) {
		annotationPanel.setVisible(false);
		buttonPanel.setVisible(false);
		
		final CommentField commentField = new CommentField(annotation.getText(), labels, true);
		 
		commentField.addSaveClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				editHandler.onSave(commentField.getText());
				commentField.removeFromParent();
			}
		});
		
		commentField.addCancelClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				commentField.removeFromParent();
				annotationPanel.setVisible(true);
				buttonPanel.setVisible(true);
			}
		});
		container.insert(commentField, container.getWidgetIndex(annotationPanel));			
	}

	public HandlerRegistration addEditClickHandler(ClickHandler handler) {
		return btnEdit.addClickHandler(handler);
	}
	
	public HandlerRegistration addDeleteClickHandler(ClickHandler handler) {
		return btnDelete.addClickHandler(handler);
	}
	
	public interface EditHandler {

		public void onSave(String text);
		
	}

}
