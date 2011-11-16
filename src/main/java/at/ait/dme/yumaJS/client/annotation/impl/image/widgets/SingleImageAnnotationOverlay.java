package at.ait.dme.yumaJS.client.annotation.impl.image.widgets;

import java.util.Arrays;
import java.util.List;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.impl.image.ImageAnnotationOverlay;
import at.ait.dme.yumaJS.client.annotation.widgets.AnnotationWidget;
import at.ait.dme.yumaJS.client.annotation.widgets.AnnotationWidget.AnnotationWidgetEditHandler;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.Range;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.Selection.SelectionChangeHandler;
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
public class SingleImageAnnotationOverlay extends ImageAnnotationOverlay {
		
	private BoundingBoxOverlay bboxOverlay;
	
	private AnnotationWidget annotationWidget;
	
	public SingleImageAnnotationOverlay(final Annotation annotation, final Annotatable annotatable,
			final AbsolutePanel annotationLayer, Labels labels) {
		
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
		
		annotationWidget = new AnnotationWidget(annotation, bboxOverlay, annotatable);
		
		annotationWidget.addDomHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				if (!annotationWidget.isEditing())
					annotationWidget.setVisible(false);
			}
		}, MouseOutEvent.getType());
		
		annotationWidget.setVisible(false);		
		annotationLayer.add(annotationWidget, bbox.getX(), bbox.getY() + bbox.getHeight() + 2);
	}
	
	@Override
	public void edit(Annotation a) {
		
	}
	
	@Override
	public void edit(Annotation a, final AnnotationWidgetEditHandler handler) {
		bboxOverlay.startEditing(new SelectionChangeHandler() {
			public void onRangeChanged(Range range) { }
			
			public void onBoundsChanged(BoundingBox bbox) {
				
				
			}
		});
		annotationWidget.edit(handler);
		annotationWidget.setVisible(true);
	}
	
	@Override
	public List<BoundingBoxOverlay> getBoundingBoxOverlays() {
		return Arrays.asList(bboxOverlay);
	}
	
	public AnnotationWidget getAnnotationWidget() {
		return annotationWidget;
	}
	
	@Override
	public void destroy() {
		bboxOverlay.destroy();
		annotationWidget.removeFromParent();
	}

}
