package at.ait.dme.yumaJS.client.annotation.ui;

import java.util.HashMap;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.ui.AnnotationWidget.AnnotationWidgetEditHandler;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
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
				if (!isEditing())
					commentWidget.setVisible(true);
			}
		}, MouseOverEvent.getType());
	
		commentWidget = new CommentWidget(annotatable.getLabels(), false);
		commentWidget.addSaveClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addToList(Annotation.create(
						annotatable.getObjectURI(), 
						Document.get().getURL(), 
						Document.get().getTitle(), 
						annotatable.getMediaType(), 
						commentWidget.getText(), 
						null,
						a.getID()));
				commentWidget.setFocus(false);
				setVisible(false);
			}
		});
		
		commentWidget.setVisible(false);
		container.add(commentWidget);
		
		addToList(a, fragmentWidget);
		
		initWidget(container);
	}
	
	private void addToList(Annotation a) {
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
		container.insert(widget, container.getWidgetCount() - 1);
		commentWidget.clear();
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
		container.setVisible(visible);
		for (AnnotationWidget widget : widgets.values()) {
			widget.setVisible(visible);
		}
		
		// Always keep the commentWidget hidden initally
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
