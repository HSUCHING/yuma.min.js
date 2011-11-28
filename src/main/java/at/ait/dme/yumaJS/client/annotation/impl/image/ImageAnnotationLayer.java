package at.ait.dme.yumaJS.client.annotation.impl.image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.gui.CompoundOverlay;
import at.ait.dme.yumaJS.client.annotation.gui.AnnotationWidget.AnnotationWidgetEditHandler;
import at.ait.dme.yumaJS.client.annotation.gui.edit.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.gui.edit.Range;
import at.ait.dme.yumaJS.client.init.InitParams;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * An implementation of {@link Annotatable} for an HTML IMAGE.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
@Export
@ExportPackage("YUMA")
public class ImageAnnotationLayer extends Annotatable implements Exportable {
	
	private static final String EMPTY_ANNOTATION = "xywh=30,30,60,60";
	
	private static final String MEDIATYPE = "IMAGE";
	
	private static String objectURI;
	
	private Element image;
	
	private AbsolutePanel annotationLayer;
	
	// { annotationID -> overlay }
	private HashMap<String, CompoundOverlay> overlays = 
		new HashMap<String, CompoundOverlay>();
	
	private int annotationCtr = 0;
	
	public ImageAnnotationLayer(String id) {
		this(id, null);
	}

	public ImageAnnotationLayer(String id, InitParams params) {	
		super(params);
		
		image = DOM.getElementById(id);
		if (image == null)
			YUMA.fatalError("Error: no element with id '" + id + "' found on this page");
		
		if (!image.getTagName().toLowerCase().equals("img"))
			YUMA.fatalError("Error: you can only create an ImageCanvas on an <img> element");
		
		objectURI = Image.wrap(image).getUrl();

		annotationLayer = new AbsolutePanel();
		annotationLayer.setStyleName("image-canvas");	
		annotationLayer.addStyleName("no-hover");
		annotationLayer.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		annotationLayer.setPixelSize(image.getClientWidth(), image.getClientHeight());
		
		annotationLayer.addDomHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				annotationLayer.addStyleName("hover");
				annotationLayer.removeStyleName("no-hover");
			}
		}, MouseOverEvent.getType());
		
		annotationLayer.addDomHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				annotationLayer.removeStyleName("hover");
				annotationLayer.addStyleName("no-hover");
			}
		}, MouseOutEvent.getType());
		
		RootPanel.get().add(annotationLayer, image.getAbsoluteLeft(), image.getAbsoluteTop());
		
		if (getServerURL() != null) {
			fetchAnnotations(getServerURL());
		}
	}
	
	@Override
	public String getObjectURI() {
		return objectURI;
	}

	@Override
	public String getMediaType() {
		return MEDIATYPE;
	}
	
	@Override
	public String toFragment(BoundingBox bbox, Range range) {
		return "xywh=pixel:" 
			+ bbox.getX()+ "," 
			+ bbox.getY() + "," 
			+ bbox.getWidth() + ","
			+ bbox.getHeight();
	}

	@Override
	public Range toRange(String fragment) {
		// Images don't support range fragments
		return null;
	}

	@Override
	public BoundingBox toBoundingBox(String fragment) {
		if (fragment.startsWith("xywh=pixel:")) {
			fragment = fragment.substring(11);
		} else if (fragment.startsWith("xywh=")) {
			fragment = fragment.substring(5);
		} else {
			return null;
		}

		String[] xywh = fragment.split(",");
		if (xywh.length != 4)
			return null;
		
		return BoundingBox.create(
				Integer.parseInt(xywh[0]),
				Integer.parseInt(xywh[1]),
				Integer.parseInt(xywh[2]),
				Integer.parseInt(xywh[3]));
	}

	@Override
	protected void onWindowResize(int width, int height) {
		RootPanel.get().setWidgetPosition(annotationLayer, image.getAbsoluteLeft(), image.getAbsoluteTop());
	}
	
	@Override
	public void addAnnotation(final Annotation a) {
		if (a.getID() == null && getServerURL() == null)
			a.setID(Integer.toString(annotationCtr++));
			
		if (a.getIsReplyTo() == null) {
			// Root annotation - add new overlay
			final CompoundOverlay overlay = (getRepliesEnabled()) ? 
					new CommentListOverlay(a, this, annotationLayer, getLabels()) :
					new SingleImageAnnotationOverlay(a, this, annotationLayer);

			overlays.put(a.getID(), overlay);
			redraw();
		} else {
			// Reply - ignore if replies are not enabled!
			if (getInitParams().isRepliesEnabled()) {
				// ImageAnnotationOverlay overlay = overlays.get(a.getIsReplyTo());
				// ((ReplyEnabledInfoPopup) overlay.getDetailsPopup()).addReply(a);
			}
		}
	}
	
	@Override
	public void removeAnnotation(Annotation a) {
		CompoundOverlay overlay = overlays.get(a.getID());
		if (overlay != null) {
			overlay.destroy();
			overlays.remove(a.getID());
		}
	}
	
	@Override
	public void redraw() {
		// Redraw not necessary - just re-assign z-indexes
		ArrayList<CompoundOverlay> sortedOverlays = new ArrayList<CompoundOverlay>();
		for (String id : overlays.keySet()) {
			sortedOverlays.add(overlays.get(id));
		}
		Collections.sort(sortedOverlays);

		int zIndex = 9010;
		for (CompoundOverlay overlay : sortedOverlays) {
			overlay.setZIndex(zIndex);
			zIndex++;
		}
	}
		
	public void createNewAnnotation() {
		final Annotation empty = createEmptyAnnotation();
		empty.setFragment(EMPTY_ANNOTATION);
		addAnnotation(empty);
		
		final CompoundOverlay overlay = overlays.get(empty.getID());
		
		// It's a new annotation - we'll listen to the first save/cancel
		overlay.setAnnotationWidgetEditHandler(empty, new AnnotationWidgetEditHandler() {
			public void onSave(Annotation a) { 
				// If save, just remove the listener
				overlay.setAnnotationWidgetEditHandler(empty, null);
			}
			
			public void onCancel() {
				// If cancel, we'll remove the annotation from the GUI
				overlay.destroy();
			}
		});
		overlay.edit(empty);
	}

}
