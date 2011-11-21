package at.ait.dme.yumaJS.client.annotation.widgets;

import java.util.Date;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.init.Labels;
import at.ait.dme.yumaJS.client.io.Delete;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
	
	private InlineHTML username;
	
	private InlineHTML text;
	
	private InlineHTML timestamp;
	
	private PushButton btnEdit, btnDelete;
		
	private boolean isEditing = false;
	
	private AnnotationWidgetEditHandler handler = null;
	
	private Annotation annotation;
	
	private FragmentWidget fragmentWidget;
	
	private Annotatable annotatable;
	
	private Labels labels;
	
	private static final String CSS_HIDDEN = "yuma-button-hidden";
	private static final String DATE_FORMAT = "MMMM dd, yyyy 'at' HH:mm"; 
	
	public AnnotationWidget(Annotation a, FragmentWidget fragmentWidget, Annotatable annotatable) {
		this.annotation = a;
		this.fragmentWidget = fragmentWidget;
		this.annotatable = annotatable;
		
		// Construct annotation panel
		annotationPanel = new FlowPanel();
		annotationPanel.setStyleName("yuma-annotation-content");

		username = new InlineHTML();
		username.setStyleName("yuma-annotation-username");
		annotationPanel.add(username);

		text = new InlineHTML();
		annotationPanel.add(text);

		timestamp = new InlineHTML();
		timestamp.setStyleName("yuma-annotation-modified");
		annotationPanel.add(timestamp);
	
		annotationPanel.addDomHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				btnEdit.removeStyleName(CSS_HIDDEN);
				btnDelete.removeStyleName(CSS_HIDDEN);
			}
		}, MouseOverEvent.getType());

		setAnnotation(a);
		
		// Construct button panel
		buttonPanel = new FlowPanel();
		buttonPanel.setStyleName("yuma-annotation-buttons");
		
		btnDelete = new PushButton();
		btnDelete.setStyleName("yuma-icon-button");
		btnDelete.addStyleName("yuma-button-delete");
		btnDelete.addStyleName(CSS_HIDDEN);
		btnDelete.getElement().getStyle().setFloat(Float.RIGHT);
		btnDelete.getElement().getStyle().setCursor(Cursor.POINTER);
		btnDelete.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				delete();
			}
		});
		
		btnEdit = new PushButton();
		btnEdit.setStyleName("yuma-icon-button");
		btnEdit.addStyleName("yuma-button-edit");
		btnEdit.addStyleName(CSS_HIDDEN);
		btnEdit.getElement().getStyle().setFloat(Float.RIGHT);
		btnEdit.getElement().getStyle().setCursor(Cursor.POINTER);
		btnEdit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				edit();
			}
		});
	
		Labels labels = annotatable.getLabels();
		if (annotatable.getLabels() == null) {
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
	
	public void setAnnotationWidgetEditHandler(AnnotationWidgetEditHandler handler) {
		this.handler = handler;
	}
	
	private void setAnnotation(Annotation a) {				
		// Username will be undefined in server-less mode!
		if (annotation.getUserRealName() != null) {
			username.setHTML(annotation.getUserRealName());
		} else if (annotation.getUsername() != null) {
			username.setHTML(annotation.getUsername());
		} else {
			username.setVisible(false);
		}

		if (annotation.getText() != null)
			text.setHTML(toHTML(annotation.getText()) + "<br/>");

		// Timestamps will be -1 in server-less mode!
		long modified = annotation.getModified();
		if (modified > 0) {
			timestamp.setHTML(DateTimeFormat.getFormat(DATE_FORMAT).format(new Date(modified)));
		} else {
			timestamp.setVisible(false);
		}
	}

	private native String toHTML(String text) /*-{
    	var exp = /(\b(https?|ftp|file):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/ig;
    	return text.replace(exp,"<a href=\"$1\" target=\"blank\">$1</a>"); 
	}-*/;
	
	public void edit() {
		isEditing = true;
		
		if (fragmentWidget != null)
			fragmentWidget.startEditing();
		
		// Hide panel and edit/delete buttons
		annotationPanel.setVisible(false);
		buttonPanel.setVisible(false);

		// Add comment field in place
		final CommentWidget commentField = new CommentWidget(annotation.getText(), labels, true);
		 
		commentField.addSaveClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {	
				if (fragmentWidget != null) {
					fragmentWidget.stopEditing();
					annotation.setFragment(annotatable
							.toFragment(fragmentWidget.getBoundingBox(), fragmentWidget.getRange()));
				}
				
				annotation.setText(commentField.getText());
				setAnnotation(annotation);
				
				if (handler != null)
					handler.onSave(annotation);
				
				commentField.removeFromParent();
				annotationPanel.setVisible(true);
				buttonPanel.setVisible(true);
				setVisible(false);
				isEditing = false;
			}
		});
		
		commentField.addCancelClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (fragmentWidget != null)
					fragmentWidget.cancelEditing();
				
				if (handler != null)
					handler.onCancel();

				commentField.removeFromParent();
				annotationPanel.setVisible(true);
				buttonPanel.setVisible(true);
				setVisible(false);
				isEditing = false;
			}
		});
		container.insert(commentField, container.getWidgetIndex(annotationPanel));			
	}
		
	public boolean isEditing() {
		return isEditing;
	}
	
	private void delete() {
		if (annotatable.getServerURL() == null) {
			annotatable.removeAnnotation(annotation);
		} else {
			Delete.executeJSONP(annotatable.getServerURL(), annotation.getID(), new AsyncCallback<Void>() {
				public void onSuccess(Void result) {
					annotatable.removeAnnotation(annotation);
				}			

				public void onFailure(Throwable t) {
					YUMA.nonFatalError(t.getMessage());
				}
			});
		}							
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
	
	public interface AnnotationWidgetEditHandler {

		public void onSave(Annotation annotation);
		
		public void onCancel();
		
	}

}
