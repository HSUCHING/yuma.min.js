package at.ait.dme.yumaJS.client.annotation.widgets;

import java.util.ArrayList;
import java.util.List;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.init.Labels;
import at.ait.dme.yumaJS.client.io.Create;
import at.ait.dme.yumaJS.client.io.Delete;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextArea;

public class ReplyEnabledInfoPopup extends InfoPopup {
	
	private Annotatable annotatable;
	
	private Annotation rootAnnotation;
	
	private TextArea replyField;
	
	private boolean hasFocus = false;
	
	private List<Annotation> replies = new ArrayList<Annotation>();
	
	public ReplyEnabledInfoPopup(final Annotatable annotatable, final Annotation rootAnnotation, Labels labels) {
		super(annotatable, rootAnnotation, labels);
		
		this.annotatable = annotatable;
		this.rootAnnotation = rootAnnotation;
				
		replyField = new TextArea();
		replyField.setStyleName("yuma-annotation-list-add-comment");
		replyField.getElement().setAttribute("placeholder", "Add a Comment...");
		replyField.addFocusHandler(new FocusHandler() {
			public void onFocus(FocusEvent event) {
				hasFocus = true;
			}
		});
		replyField.addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				hasFocus = false;
			}
		});
		replyField.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					Annotation a = Annotation.create(
							annotatable.getObjectURI(),
							Document.get().getURL(),
							Document.get().getTitle(),
							annotatable.getMediaType(),
							replyField.getText(),
							null,
							rootAnnotation.getID());
					
					if (annotatable.getServerURL() == null) {
						// Local-mode: just add the annotation without storing
						annotatable.addAnnotation(a);
					} else {
						Create.executeJSONP(annotatable.getServerURL(), a, new AsyncCallback<JavaScriptObject>() {
							public void onSuccess(JavaScriptObject result) {
								annotatable.addAnnotation((Annotation) result);
							}
							
							public void onFailure(Throwable t) {
								YUMA.nonFatalError(t.getMessage());
							}
						});
					}
				}
			}
		});
		
		container.add(replyField);
		
		// TODO resolve conflicting behavior with super-class!
		addDomHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				if (!hasFocus)
					setVisible(false);
			}
		}, MouseOutEvent.getType());
	}
	
	public void addReply(final Annotation reply) {
		if (!reply.getIsReplyTo().equals(rootAnnotation.getID()))
			return;
		
		replyField.setText(null);
		replyField.setFocus(false);

		final AnnotationWidget widget = new AnnotationWidget(reply, annotatable.getLabels());
		widget.addDeleteClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (annotatable.getServerURL() == null) {
					annotatable.removeAnnotation(reply);
					widget.removeFromParent();
				} else {
					Delete.executeJSONP(annotatable.getServerURL(), reply.getID(), new AsyncCallback<Void>() {
						public void onSuccess(Void result) {
							annotatable.removeAnnotation(reply);
							widget.removeFromParent();
						}			
	
						public void onFailure(Throwable t) {
							YUMA.nonFatalError(t.getMessage());
						}
					});
				}				
			}
		});
		container.insert(widget, container.getWidgetCount() - 1);
		
		replies.add(reply);
	}

}
