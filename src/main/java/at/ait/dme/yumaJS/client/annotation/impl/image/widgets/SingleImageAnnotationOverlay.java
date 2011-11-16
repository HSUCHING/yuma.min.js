package at.ait.dme.yumaJS.client.annotation.impl.image.widgets;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.widgets.AnnotationWidget;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.AnnotationEditHandler;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.selection.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.selection.Range;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.selection.SelectionChangedHandler;
import at.ait.dme.yumaJS.client.init.Labels;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;

/**
 * This is the image annotation overlay type that is used when reply functionality is DISABLED. 
 * It consists of a {@link BoundingBoxOverlay} with a single {@link AnnotationWidget} underneath.
 * When in editing mode, the {@link AnnotationWidget} stays clamped to the lower-left corner
 * of the {@link BoundingBoxOverlay}.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class SingleImageAnnotationOverlay {
	
	private Annotation annotation;
	
	private Annotatable annotatable;

	private AbsolutePanel annotationLayer;
	
	private BoundingBoxOverlay bboxOverlay;
	
	private AnnotationWidget annotationWidget;
	
	public SingleImageAnnotationOverlay(Annotation annotation, Annotatable annotatable,
			final AbsolutePanel annotationLayer, Labels labels) {
		
		this.annotation = annotation;
		this.annotatable = annotatable;
		this.annotationLayer = annotationLayer;
	
		final BoundingBox bbox = annotatable.toBoundingBox(annotation.getFragment());
		
		bboxOverlay = new BoundingBoxOverlay(annotationLayer, bbox);
		
		bboxOverlay.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				annotationWidget.setVisible(true);
			}
		});
		
		bboxOverlay.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				if (!annotationWidget.contains(
						event.getRelativeX(annotationLayer.getElement()) + annotationLayer.getAbsoluteLeft(), 
						event.getRelativeY(annotationLayer.getElement()) + annotationLayer.getAbsoluteTop()))
					
					annotationWidget.setVisible(false);
			}
		});
		
		annotationWidget = new AnnotationWidget(annotation, labels);
		
		annotationWidget.addDomHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				if (!annotationWidget.isEditing())
					annotationWidget.setVisible(false);
			}
		}, MouseOutEvent.getType());
		
		annotationWidget.setVisible(false);
		
		annotationLayer.add(bboxOverlay, bbox.getX(), bbox.getY());
		annotationLayer.add(annotationWidget, bbox.getX(), bbox.getY() + bbox.getHeight() + 2);
	}
	
	public void startEditing(final AnnotationEditHandler handler) {
		final BoundingBox initialPosition = bboxOverlay.getBoundingBox();
		
		bboxOverlay.startEditing(new SelectionChangedHandler() {
			public void onRangeChanged(Range range) {
				// No range selection in image annotation
			}
			
			public void onBoundsChanged(BoundingBox bbox) {
				annotationLayer.setWidgetPosition(annotationWidget, bbox.getX(), bbox.getY() + bbox.getHeight() + 2);
			}
		});
		
		annotationWidget.startEditing(new AnnotationWidget.EditHandler() {
			public void onSave(String text) {
				bboxOverlay.stopEditing();
				annotation.setText(text);
				annotation.setFragment(annotatable.toFragment(bboxOverlay.getBoundingBox(), null));
				annotationWidget.setVisible(false);
				handler.onSave(annotation);
			}

			public void onCancel() {
				bboxOverlay.stopEditing();
				annotationLayer.setWidgetPosition(annotationWidget, initialPosition.getX(), 
						initialPosition.getY() + initialPosition.getHeight() + 2);
				annotationWidget.setVisible(false);
				handler.onCancel();
			}
		});
		annotationWidget.setVisible(true);
	}
	
	public void setAnnotation(Annotation a) {
		annotationWidget.setAnnotation(a);
		BoundingBox bbox = annotatable.toBoundingBox(a.getFragment());
		
		annotationLayer.setWidgetPosition(bboxOverlay, bbox.getX(), bbox.getY());
		bboxOverlay.setBoundingBox(bbox);
	}
	
	public BoundingBoxOverlay getBoundingBoxOverlay() {
		return bboxOverlay;
	}
	
	public AnnotationWidget getAnnotationWidget() {
		return annotationWidget;
	}
	
	public void destroy() {
		bboxOverlay.removeFromParent();
		annotationWidget.removeFromParent();
	}

}
