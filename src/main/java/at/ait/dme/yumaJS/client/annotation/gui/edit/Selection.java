package at.ait.dme.yumaJS.client.annotation.gui.edit;


import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;

/**
 * An abstract base class for different types of fragment 
 * selection tools.
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public abstract class Selection {
	
	public abstract BoundingBox getSelectedBounds();
	
	public abstract Range getSelectedRange();
	
	public abstract void setSelectionChangeHandler(SelectionChangeHandler handler);
	
	public abstract void destroy();
	
	/**
	 * Utiltity method that disables all selection functionality on the document.
	 * This avoids the nasty (and involuntary) selection effect that otherwise happens
	 * when dragging the mouse
	 */
	protected void disableTextSelection() {
		Style body = Document.get().getBody().getStyle();
		body.setProperty("MozUserSelect", "none");
		body.setProperty("WebkitUserSelect", "none");
		body.setProperty("UserSelect", "none");
		_disableTextSelection();
	}
	
	private native void _disableTextSelection() /*-{
		document.onselectstart = function(){return false;}
		document.onmousedown = function() {return false;}
	}-*/;
	
	/**
	 * Re-enables text selection on the document
	 */
	protected void enableTextSelection() {
		Style body = Document.get().getBody().getStyle();
		body.setProperty("MozUserSelect", "auto");
		body.setProperty("WebkitUserSelect", "auto");
		body.setProperty("UserSelect", "auto");	
		_enableTextSelection();
	}
	
	private native void _enableTextSelection() /*-{
		document.onselectstart = function(){return true;}
  		document.onmousedown = function(){return true;}
	}-*/;
	
	public interface SelectionChangeHandler {

		public void onBoundsChanged(BoundingBox bbox);
		
		public void onRangeChanged(Range range);
		
	}

}
