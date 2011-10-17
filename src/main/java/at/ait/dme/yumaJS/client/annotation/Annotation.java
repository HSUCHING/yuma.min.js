package at.ait.dme.yumaJS.client.annotation;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * The annotation.
 *  
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class Annotation extends JavaScriptObject {
	
	protected Annotation() { }
	
	public static native Annotation create(String objectURI, String contextURI, String contextTitle, String mediatype, String fragment, String text) /*-{
		return { 
				 objectURI: objectURI,
				 context: { uri: contextURI, title: contextTitle },
		         mediatype: mediatype,
		         fragment: fragment, 
		         text: text 
		       };
	}-*/;
	
	public final native String getID() /*-{
		return this.id;
	}-*/;

	public final native String getFragment() /*-{
		return this.fragment;
	}-*/;
	
	public final native void setFragment(String fragment) /*-{
		this.fragment = fragment;
	}-*/;
	
	public final native String getText() /*-{
		return this.text;
	}-*/;
	
	public final native void setText(String text) /*-{
		this.text = text;
	}-*/;

}
