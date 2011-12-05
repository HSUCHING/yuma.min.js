package at.ait.dme.yumaJS.client.annotation.impl.openlayers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.gui.AnnotationWidget.AnnotationWidgetEditHandler;
import at.ait.dme.yumaJS.client.annotation.gui.edit.BoundingBox;
import at.ait.dme.yumaJS.client.annotation.gui.edit.Range;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.Bounds;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.BoxesLayer;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.LonLat;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.Map;
import at.ait.dme.yumaJS.client.annotation.impl.openlayers.api.Pixel;
import at.ait.dme.yumaJS.client.init.InitParams;

@Export
@ExportPackage("YUMA")
public class OpenLayersAnnotationLayer extends Annotatable implements Exportable {
	
	private static final int DEFAULT_FRAGMENT_LEFT = 30;
	private static final int DEFAULT_FRAGMENT_TOP = 30;
	private static final int DEFAULT_SIZE = 60;

	private static final String MEDIATYPE = "MAP";
	
	private String objectURI;

	private Map map;
	
	private BoxesLayer annotationLayer;
	
	private AbsolutePanel editingLayer;
	
	// { annotationID -> overlay }
	private HashMap<String, SingleOpenLayersAnnotationOverlay> overlays = 
		new HashMap<String, SingleOpenLayersAnnotationOverlay>();
	
	private int annotationCtr = 0;
	
	public OpenLayersAnnotationLayer(JavaScriptObject openLayersMap, String objectURI, InitParams params) {
		super(params);

		if (openLayersMap == null) 
			YUMA.fatalError("Error: OpenLayers map undefined (not initialized yet?)");
		
		map = new Map(openLayersMap);
		this.objectURI = objectURI;

		// TODO make annotation layer name configurable via init params
		annotationLayer = BoxesLayer.create("Annotations");
		map.addBoxesLayer(annotationLayer);
		
		HTML parentDiv = map.getDiv();
		editingLayer = new AbsolutePanel();
		editingLayer.setStyleName("openlayers-editing-layer");
		editingLayer.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		editingLayer.setPixelSize(parentDiv.getOffsetWidth(), parentDiv.getOffsetHeight());		
		RootPanel.get().insert(editingLayer, parentDiv.getAbsoluteLeft(), parentDiv.getAbsoluteTop(), 0);
		
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
	
	/**
	 * Converts from viewport pixel coordinates to a fragment. The fragment
	 * is expressed in map coordinates. 
	 */
	@Override
	public String toFragment(BoundingBox bbox, Range range) {
		Pixel pxBottomLeft = Pixel.create(bbox.getX(), bbox.getY() + bbox.getHeight());
		Pixel pxTopRight = Pixel.create(bbox.getX() + bbox.getWidth(), bbox.getY());
		
		LonLat llBottomLeft = map.getLonLatFromPixel(pxBottomLeft);
		LonLat llTopRight = map.getLonLatFromPixel(pxTopRight);
		
		return "bbox=" 
			+ llBottomLeft.getLon() + "," 
			+ llBottomLeft.getLat() + ","
			+ llTopRight.getLon() + "," 
			+ llTopRight.getLat();
	}

	@Override
	public Range toRange(String fragment) {
		// Openlayers doesn't support range fragments
		return null;
	}

	/**
	 * Converts from a fragment (expressed in map coordinates) to viewport coordinates.
	 */
	@Override
	public BoundingBox toBoundingBox(String fragment) {
		Bounds bounds = toOpenLayersBounds(fragment);
		LonLat llBottomLeft = LonLat.create(bounds.getLeft(), bounds.getBottom());
		LonLat llTopRight = LonLat.create(bounds.getRight(), bounds.getTop());
		
		Pixel pxBottomLeft = map.getViewPortPixelFromLonLat(llBottomLeft);
		Pixel pxTopRight = map.getViewPortPixelFromLonLat(llTopRight);
		
		return BoundingBox.create(
				pxBottomLeft.getX(), 
				pxTopRight.getY(),
				pxTopRight.getX() - pxBottomLeft.getX(),
				pxBottomLeft.getY() - pxTopRight.getY());
	}
	
	/**
	 * Resolves a fragment string (expressed in map coordinates) to map coordinates.
	 * @param fragment the fragment String
	 * @return the map coordinate bounds
	 */
	public Bounds toOpenLayersBounds(String fragment) {
		String[] bbox = fragment.substring(5).split(",");
		if (bbox.length != 4)
			return null;
		
		return Bounds.create(
				Double.parseDouble(bbox[0]),
				Double.parseDouble(bbox[1]),
				Double.parseDouble(bbox[2]),
				Double.parseDouble(bbox[3]));
	}

	@Override
	protected void onWindowResize(int width, int height) {
		editingLayer.setPixelSize(width, height);
		RootPanel.get().setWidgetPosition(editingLayer, map.getDiv().getAbsoluteLeft(), map.getDiv().getAbsoluteTop());
	}

	@Override
	public void addAnnotation(Annotation annotation) {
		if (annotation.getID() == null && getServerURL() == null)
			annotation.setID(Integer.toString(annotationCtr++));
		
		SingleOpenLayersAnnotationOverlay overlay = 
			new SingleOpenLayersAnnotationOverlay(annotation, annotationLayer, editingLayer, this);

		overlays.put(annotation.getID(), overlay);
		redraw();
		fireOnAnnotationCreated(annotation);
	}
	
	@Override
	public void redraw() {
		// Redraw not necessary - just re-assign z-indexes
		ArrayList<SingleOpenLayersAnnotationOverlay> sortedOverlays = new ArrayList<SingleOpenLayersAnnotationOverlay>();
		for (String id : overlays.keySet()) {
			sortedOverlays.add(overlays.get(id));
		}
		Collections.sort(sortedOverlays);
		
		int zIndex = 9010;
		for (SingleOpenLayersAnnotationOverlay overlay : sortedOverlays) {
			overlay.setZIndex(zIndex);
			zIndex++;
		}
	}

	@Override
	public void removeAnnotation(Annotation annotation) {
		System.out.println("removing: " + annotation.getID());
		SingleOpenLayersAnnotationOverlay overlay = overlays.get(annotation.getID());
		if (overlay != null) {
			annotationLayer.removeMarker(overlay.getMarker());
			overlay.destroy();
			overlays.remove(annotation);
		} 
	}
	
	private String createEmptyFragment() {
		return toFragment(BoundingBox.create(DEFAULT_FRAGMENT_LEFT, DEFAULT_FRAGMENT_TOP, 
				DEFAULT_SIZE, DEFAULT_SIZE), null);
	}
	
	public void createNewAnnotation() {
		annotationLayer.redraw();
		
		final Annotation empty = createEmptyAnnotation();
		empty.setFragment(createEmptyFragment());
		addAnnotation(empty);
		
		final SingleOpenLayersAnnotationOverlay overlay = overlays.get(empty.getID());
		
		// It's a new annotation - we'll listen to the first save/cancel
		overlay.setAnnotationWidgetEditHandler(empty, new AnnotationWidgetEditHandler() {
			public void onSave(Annotation a) { 
				// If save, just remove the listener
				overlay.setAnnotationWidgetEditHandler(empty, null);
			}
			
			public void onCancel() {
				// If cancel, we'll remove the annotation from the GUI
				removeAnnotation(empty);
			}
		});
		overlay.edit(empty);
	}
	
}
