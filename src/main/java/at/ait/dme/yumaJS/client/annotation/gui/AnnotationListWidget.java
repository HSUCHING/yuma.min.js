package at.ait.dme.yumaJS.client.annotation.gui;

import java.util.HashMap;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
	
	/**
	 * Creates a new {@link AnnotationListWidget} with a single annotation and a reference to 
	 * a {@link FragmentWidget}.
	 * @param a the annotation
	 * @param fragmentWidget the FragmentWidget
	 * @param annotatable the Annotatable
	 */
	public AnnotationListWidget(final Annotation a, FragmentWidget fragmentWidget, final Annotatable annotatable) {
		container = new FlowPanel();
		container.setStyleName("yuma-annotation-list");		
		
		AnnotationWidget rootAnnotationWidget = 
				new AnnotationWidget(a, fragmentWidget, annotatable);
		widgets.put(a, rootAnnotationWidget);
		
		container.add(rootAnnotationWidget);	
		
		commentWidget = new CommentWidget(annotatable.getLabels(), false);
		commentWidget.addSaveClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				/*
				save(Annotation.create(
						annotatable.getObjectURI(),
						Document.get().getURL(),
						Document.get().getTitle(),
						annotatable.getMediaType(),
						commentWidget.getText(),
						null,
						a.getID()));
				commentWidget.setVisible(false);
				*/
			}
		});
		
		container.add(commentWidget);
		initWidget(container);
	}
	
	public void addToList(Annotation a) {
		
	}
	
	public void edit(Annotation a) {
		AnnotationWidget widget = widgets.get(a);
		if (widget != null)
			widget.edit();
	}

}
