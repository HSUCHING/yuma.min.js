package at.ait.dme.yumaJS.client.annotation.ui;

import java.util.HashMap;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.ui.AnnotationWidget.AnnotationWidgetEditHandler;
import at.ait.dme.yumaJS.client.io.Create;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * The {@link AnnotationListWidget} is a composite GUI widget that
 * combines several {@link AnnotationWidget}s into a list. The
 * widget can be used by {@link CompoundOverlay}s to display 
 * reply threads. 
 *  
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class AnnotationListWidget extends Composite {
	
	private FlowPanel container;

	private CommentWidget commentWidget;
	
	private HashMap<String, AnnotationWidget> widgets = new HashMap<String, AnnotationWidget>();
	
	private Annotatable annotatable;
	
	/**
	 * Creates a new {@link AnnotationListWidget} with a single annotation and a reference to 
	 * a {@link FragmentWidget}.
	 * @param a the annotation
	 * @param fragmentWidget the FragmentWidget
	 * @param annotatable the Annotatable
	 */
	public AnnotationListWidget(final Annotation a, FragmentWidget fragmentWidget, final Annotatable annotatable) {
		this.annotatable = annotatable;
		
		container = new FlowPanel();
		container.setStyleName("yuma-annotation-list");	
		
		container.addDomHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				showCommentWidget();
			}
		}, MouseOverEvent.getType());
	
		commentWidget = new CommentWidget(annotatable.getLabels(), false);
		commentWidget.addSaveClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Annotation reply = Annotation.create(
						annotatable.getObjectURI(), 
						Document.get().getURL(), 
						Document.get().getTitle(), 
						annotatable.getMediaType(), 
						commentWidget.getText(), 
						null,
						getRootAnnotation().getID());
		
				if (annotatable.getServerURL() == null) {
					addToList(reply);
				} else {
					Create.executeJSONP(annotatable.getServerURL(), reply, new AsyncCallback<JavaScriptObject>() {
						public void onSuccess(JavaScriptObject result) {
							addToList((Annotation) result);
						}			

						public void onFailure(Throwable t) {
							YUMA.nonFatalError(t.getMessage());
						}
					});
				}				
				
				setVisible(false);				
				commentWidget.setFocus(false);
			}
		});
		
		container.add(commentWidget);
		commentWidget.setVisible(false);
		container.setVisible(false);
		
		addToList(a, fragmentWidget);
		
		container.getElement().getStyle().setDisplay(Display.BLOCK);
		initWidget(container);
	}
	
	private Annotation getRootAnnotation() {
		return ((AnnotationWidget) container.getWidget(0)).getAnnotation();
	}
	
	public void showCommentWidget() {
		if (!isEditing())
			commentWidget.setVisible(true);
	}
	
	public void addToList(Annotation a) {
		addToList(a, null);
	}
	
	private void addToList(Annotation a, FragmentWidget f) {
		if (a.getID() == null)
			a.setID("unassigned-replyto-" + a.getIsReplyTo() + "-" + widgets.size());
		
		AnnotationWidget widget = new AnnotationWidget(a, f, annotatable); 
		widget.addAnnotationWidgetEditHandler(new AnnotationWidgetEditHandler() {
			public void onStartEditing() { 
				commentWidget.setVisible(false);
			}
			
			public void onSave(Annotation annotation) {
				setVisible(false);
			}
			
			public void onCancel() { 
				setVisible(false);
			}
		});
		
		widgets.put(a.getID(), widget);
		
		commentWidget.clear();
		container.remove(commentWidget);
		
		container.add(widget);
		container.add(commentWidget);
	}
	
	public void removeFromList(String id) {
		AnnotationWidget widget = widgets.get(id);
		if (widget != null) {
			container.remove(widget);
			widgets.remove(id);
		}
	}
	
	@Override
	public void setVisible(boolean visible) {
		Style style = container.getElement().getStyle();
		if (visible) {
			style.setOpacity(1);
			style.setVisibility(Visibility.VISIBLE);
		} else {
			style.setOpacity(0);
			style.setVisibility(Visibility.HIDDEN);
		}
		
		for (AnnotationWidget widget : widgets.values()) {
			widget.setVisible(visible);
		}
		
		// Always keep the commentWidget hidden initally
		commentWidget.setFocus(false);
		commentWidget.setVisible(false);
	}
	
	public void addAnnotationWidgetEditHandler(Annotation a, AnnotationWidgetEditHandler handler) {
		widgets.get(a.getID()).addAnnotationWidgetEditHandler(handler);
	}
	
	public void removeAnnotationWidgetEditHandler(Annotation a, AnnotationWidgetEditHandler handler) {
		widgets.get(a.getID()).removeAnnotationWidgetEditHandler(handler);
	}
	
	public void setAnnotation(String id, Annotation updated) {
		AnnotationWidget widget = widgets.get(id);
		if (widget != null) {
			widget.setAnnotation(updated);
			widgets.remove(id);
			widgets.put(updated.getID(), widget);
		}
	}
	
	public void edit(Annotation a) {
		Style style = container.getElement().getStyle();
		style.setVisibility(Visibility.VISIBLE);
		style.setOpacity(1);
		
		AnnotationWidget widget = widgets.get(a.getID());
		if (widget != null) {
			widget.edit();
			commentWidget.setVisible(false);
		}
	}
	
	public boolean isEditing() {
		boolean isEditing = false;
		for (AnnotationWidget widget : widgets.values()) {
			if (widget.isEditing()) {
				isEditing = true;
				break;
			}
		}
		
		return isEditing || commentWidget.hasFocus();
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
