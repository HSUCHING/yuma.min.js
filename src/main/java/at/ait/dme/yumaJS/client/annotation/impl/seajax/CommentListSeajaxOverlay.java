package at.ait.dme.yumaJS.client.annotation.impl.seajax;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonViewer;
import at.ait.dme.yumaJS.client.annotation.ui.AnnotationWidget.AnnotationWidgetEditHandler;
import at.ait.dme.yumaJS.client.annotation.ui.edit.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.ui.edit.Range;
import at.ait.dme.yumaJS.client.annotation.ui.edit.Selection.SelectionChangeHandler;
import at.ait.dme.yumaJS.client.annotation.ui.AnnotationListWidget;
import at.ait.dme.yumaJS.client.annotation.ui.CompoundOverlay;

public class CommentListSeajaxOverlay implements CompoundOverlay {
	
	private AbsolutePanel editingLayer;
	
	private SeajaxFragmentWidget fragmentWidget;
	
	private AnnotationListWidget annotationListWidget;
	
	public CommentListSeajaxOverlay(Annotation rootAnnotation, SeadragonViewer viewer, 
			AbsolutePanel editingLayer, Annotatable annotatable) {
		
		this.editingLayer = editingLayer;
		
		this.fragmentWidget = new SeajaxFragmentWidget(
				annotatable.toBoundingBox(rootAnnotation.getFragment()), 
				viewer, 
				editingLayer,
				annotatable);
		
		this.fragmentWidget.setSelectionChangeHandler(new SelectionChangeHandler() {
			public void onRangeChanged(Range range) { }
			
			public void onBoundsChanged(BoundingBox bbox) {
				refresh();				
			}
		});
		
		annotationListWidget = new AnnotationListWidget(rootAnnotation, fragmentWidget, annotatable);
		
		annotationListWidget.addDomHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				if (!annotationListWidget.isEditing())
					annotationListWidget.setVisible(false);
			}
		}, MouseOutEvent.getType());
		
		editingLayer.add(annotationListWidget);		
		refresh();
	}
	
	private void refresh() {
		BoundingBox bbox = fragmentWidget.getBoundingBox();
		editingLayer.setWidgetPosition(annotationListWidget, bbox.getX(), bbox.getY() +  bbox.getHeight() + 2);
	}

	public void addAnnotationWidgetEditHandler(Annotation a,
			AnnotationWidgetEditHandler handler) {
		
		// TODO code duplication with OpenLayers impl (and image?)
		annotationListWidget.addAnnotationWidgetEditHandler(a, handler);		
	}

	public void removeAnnotationWidgetEditHandler(Annotation a,
			AnnotationWidgetEditHandler handler) {

		// TODO code duplication with OpenLayers impl (and image?)
		annotationListWidget.removeAnnotationWidgetEditHandler(a, handler);
	}

	public void updateAnnotation(String id, Annotation updated) {
		// TODO code duplication -> refactor
		annotationListWidget.setAnnotation(id, updated);
	}

	public void removeAnnotation(String id) {
		// TODO code duplication -> refactor
		annotationListWidget.removeFromList(id);
	}

	public void edit(Annotation a) {
		// TODO code duplication -> refactor
		annotationListWidget.edit(a);
	}

	public void setZIndex(int idx) {
		fragmentWidget.setZIndex(idx);
	}

	public void destroy() {
		fragmentWidget.destroy();
		annotationListWidget.removeFromParent();
	}

	public int compareTo(CompoundOverlay other) {
		if (!(other instanceof CommentListSeajaxOverlay))
			return 0;
		
		CommentListSeajaxOverlay overlay = (CommentListSeajaxOverlay) other;
		
		// Delegate to fragmentWidget overlay
		return fragmentWidget.compareTo(overlay.fragmentWidget);
	}
	
}
