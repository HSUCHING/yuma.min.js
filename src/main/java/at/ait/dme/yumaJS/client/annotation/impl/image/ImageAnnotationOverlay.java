package at.ait.dme.yumaJS.client.annotation.impl.image;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.editors.selection.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.widgets.InfoPopup;
import at.ait.dme.yumaJS.client.annotation.widgets.ReplyEnabledInfoPopup;
import at.ait.dme.yumaJS.client.init.Labels;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;

/**
 * An overlay that represents an annotation on an {@link ImageAnnotationLayer}
 * by combining a {@link BoundingBoxOverlay} with a fixed-location
 * {@link SimpleDetailsPopup}.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class ImageAnnotationOverlay {
		
	private BoundingBoxOverlay bboxOverlay;
	
	private InfoPopup detailsPopup;
	
	public ImageAnnotationOverlay(Annotation annotation, Annotatable annotatable,
			final AbsolutePanel annotationLayer, boolean enableReplies, Labels labels) {
		
		final BoundingBox bbox = annotatable.toBoundingBox(annotation.getFragment());
		
		bboxOverlay = new BoundingBoxOverlay(bbox);
		
		bboxOverlay.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				detailsPopup.setVisible(true);
			}
		});
		
		bboxOverlay.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				if (!detailsPopup.contains(
						event.getRelativeX(annotationLayer.getElement()) + annotationLayer.getAbsoluteLeft(), 
						event.getRelativeY(annotationLayer.getElement()) + annotationLayer.getAbsoluteTop()))
					
					detailsPopup.setVisible(false);
			}
		});
		
		if (enableReplies) {
			detailsPopup = new ReplyEnabledInfoPopup(annotatable, annotation, labels);	 
		} else {
			detailsPopup = new InfoPopup(annotatable, annotation, labels);
		}
		detailsPopup.setVisible(false);
		
		annotationLayer.add(bboxOverlay, bbox.getX(), bbox.getY());
		annotationLayer.add(detailsPopup, bbox.getX(), bbox.getY() + bbox.getHeight());
	}
	
	public BoundingBoxOverlay getBoundingBoxOverlay() {
		return bboxOverlay;
	}
	
	public Composite getDetailsPopup() {
		return detailsPopup;
	}
	
	public void destroy() {
		bboxOverlay.removeFromParent();
		detailsPopup.removeFromParent();
	}

}
