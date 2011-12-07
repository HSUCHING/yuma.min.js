package at.ait.dme.yumaJS.client.annotation.impl.seajax;

import java.util.HashMap;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonMouseHandler;
import at.ait.dme.yumaJS.client.annotation.impl.seajax.api.SeadragonViewer;
import at.ait.dme.yumaJS.client.annotation.ui.edit.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.ui.edit.Range;
import at.ait.dme.yumaJS.client.init.InitParams;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * An implementation of {@link Annotatable} for an HTML DIV holding a 
 * Seadragon AJAX viewer instance.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
@Export
@ExportPackage("YUMA")
public class SeajaxAnnotationLayer extends Annotatable implements Exportable {
	
	private static final String MEDIATYPE = "IMAGE";
	
	private static String objectURI;
	
	private HTML parentDiv;
	
	private AbsolutePanel annotationLayer;
	
	private SeadragonViewer viewer;
	
	private HashMap<Annotation, ZoomableAnnotationOverlay> annotations = 
		new HashMap<Annotation, ZoomableAnnotationOverlay>();

	public SeajaxAnnotationLayer(String id, JavaScriptObject deepZoomViewer) {
		this(id, deepZoomViewer, null);
	}
	
	public SeajaxAnnotationLayer(String id, JavaScriptObject deepZoomViewer, InitParams params) {
		super(params);
		
		Element el = DOM.getElementById(id);
		if (el == null)
			YUMA.fatalError("Error: no element with id '" + id + "' found on this page");
		
		if (!el.getTagName().toLowerCase().equals("div"))
			YUMA.fatalError("Error: you can only create a DeepZoomCanvas on a <div> element");
		
		if (deepZoomViewer == null) 
			YUMA.fatalError("Error: Seadragon viewer not found (not initialized yet?)");
		
		parentDiv = HTML.wrap(el);
		
		annotationLayer = new AbsolutePanel();
		annotationLayer.setStyleName("deepzoom-canvas");
		annotationLayer.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		annotationLayer.setPixelSize(parentDiv.getOffsetWidth(), parentDiv.getOffsetHeight());		
		RootPanel.get().insert(annotationLayer, parentDiv.getAbsoluteLeft(), parentDiv.getAbsoluteTop(), 0);
		
		viewer = new SeadragonViewer(deepZoomViewer);
		viewer.addMouseHandler(new SeadragonMouseHandler() {
			public void onMouseOver() {
				parentDiv.addStyleName("hover");
				parentDiv.removeStyleName("no-hover");
			}
			
			public void onMouseOut() {
				parentDiv.removeStyleName("hover");
				parentDiv.addStyleName("no-hover");
			}
		});
		
		objectURI = viewer.getObjectURI();
	}
	
	@Override
	public String getObjectURI() {
		return objectURI;
	}
	
	@Override
	public String getMediaType() {
		return MEDIATYPE;
	}

	/*
	No5.Seajax.toImageCoordinates = function(viewer, viewerX, viewerY) {
		   return new Seadragon.Point(viewerX * viewer.source.width, viewerY * viewer.source.height * viewer.source.aspectRatio);
		}


		No5.Seajax.toWorldCoordinates = function(viewer, imageX, imageY) {
		   return new Seadragon.Point(imageX / viewer.source.width, imageY / viewer.source.height / viewer.source.aspectRatio);
		}
	*/
	
	
	
	@Override
	public String toFragment(BoundingBox bbox, Range range) {
		return null;
	}

	@Override
	public Range toRange(String fragment) {
		// Zoomable images don't support range fragments
		return null;
	}

	@Override
	public BoundingBox toBoundingBox(String fragment) {
		// TODO Auto-generated method stub
		return null;
	}
		
	@Override
	protected void onWindowResize(int width, int height) {
		RootPanel.get().setWidgetPosition(annotationLayer, parentDiv.getAbsoluteLeft(), parentDiv.getAbsoluteTop());
	}
	
	@Override
	public void addAnnotation(Annotation a) {
		ZoomableAnnotationOverlay overlay = 
			new ZoomableAnnotationOverlay(a, this, viewer, getLabels());		
		annotations.put(a, overlay);
		fireOnAnnotationCreated(a);
	}

	@Override
	public void removeAnnotation(Annotation a) {
		ZoomableAnnotationOverlay overlay = annotations.get(a);
		if (overlay != null) {
			overlay.destroy();
			annotations.remove(a);
		}
	}
	
	@Override
	public void updateAnnotation(String id, Annotation updated) {
		// TODO Auto-generated method stub
		
	}
	
	public void createNewAnnotation() {
		// new ResizableBoxEditor(this, annotationLayer);
	}
	
}
