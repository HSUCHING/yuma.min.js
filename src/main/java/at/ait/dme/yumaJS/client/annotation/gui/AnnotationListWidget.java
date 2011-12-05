package at.ait.dme.yumaJS.client.annotation.gui;

import java.util.HashMap;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.gui.AnnotationWidget.AnnotationWidgetEditHandler;

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
	
	private HashMap<Annotation, AnnotationWidget> widgets = new HashMap<Annotation, AnnotationWidget>();
	
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
				if (!widgets.get(a).isEditing())
					commentWidget.setVisible(true);
			}
		}, MouseOverEvent.getType());
		
		AnnotationWidget rootAnnotationWidget = 
				new AnnotationWidget(a, fragmentWidget, annotatable);
		widgets.put(a, rootAnnotationWidget);
		
		container.add(rootAnnotationWidget);	
		
		commentWidget = new CommentWidget(annotatable.getLabels(), false);
		commentWidget.addSaveClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addToList(Annotation.create(
						annotatable.getObjectURI(), 
						Document.get().getURL(), 
						Document.get().getTitle(), 
						annotatable.getMediaType(), 
						commentWidget.getText(), 
						null));
				commentWidget.setVisible(false);
			}
		});
		
		container.add(commentWidget);
		commentWidget.setVisible(false);
		initWidget(container);
	}
	
	@Override
	public void setVisible(boolean visible) {
		for (AnnotationWidget widget : widgets.values()) {
			widget.setVisible(visible);
		}
	}
	
	private void addToList(Annotation a) {
		AnnotationWidget widget = new AnnotationWidget(a, null, annotatable); 
		widgets.put(a, widget);
		container.insert(widget, container.getWidgetCount() - 1);
	}
	
	public void setAnnotationWidgetEditHandler(Annotation a, AnnotationWidgetEditHandler handler) {
		widgets.get(a).setAnnotationWidgetEditHandler(handler);
	}
	
	public void edit(Annotation a) {
		AnnotationWidget widget = widgets.get(a);
		if (widget != null)
			widget.edit();
	}

}
