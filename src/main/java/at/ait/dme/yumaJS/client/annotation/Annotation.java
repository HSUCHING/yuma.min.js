package at.ait.dme.yumaJS.client.annotation;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * The annotation.
 *  
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class Annotation extends JavaScriptObject {
	
	protected Annotation() { }

	public static Annotation create(String objectURI, String contextURI, String contextTitle, String mediatype, String text, String fragment) {
		return create(objectURI, contextURI, contextTitle, mediatype, text, fragment, null);
	}
	
	public static native Annotation create(String objectURI, String contextURI, String contextTitle, String mediatype, String text, String fragment, String isReplyTo) /*-{
		return { 
				 objectURI: objectURI,
				 context: { uri: contextURI, title: contextTitle },
		         mediatype: mediatype,
		         fragment: fragment, 
		         text: text,
		         isReplyTo: isReplyTo 
		       };
	}-*/;
	
	public final native String getID() /*-{
		return this.id;
	}-*/;
	
	public final native void setID(String id) /*-{
		this.id = id;
	}-*/;
	
	public final native String getIsReplyTo() /*-{
		return this.isReplyTo;
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
	
	public final native String getUsername() /*-{
		return this.creator.username;
	}-*/;
	
	public final native String getUserRealName() /*-{
		return this.creator.name;
	}-*/;
	
	public final native double getModified() /*-{
		return this.modified;
	}-*/;

}
