package at.ait.dme.yumaJS.client.annotation.widgets;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.init.Labels;
import at.ait.dme.yumaJS.client.io.Create;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.TextArea;

public class ReplyEnabledDetailsPopup extends DetailsPopup {
	
	public ReplyEnabledDetailsPopup(final Annotatable annotatable, Annotation a, Labels labels) {
		FlowPanel content = new FlowPanel();
		content.setStyleName("annotation-popup-content");
		content.add(new InlineHTML(a.getText()));
		
		final TextArea textArea = new TextArea();
		textArea.setStyleName("annotation-popup-add-comment");
		textArea.getElement().setAttribute("placeholder", "Add a Comment...");
		textArea.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					Annotation a = Annotation.create(
							annotatable.getObjectURI(),
							Document.get().getURL(),
							Document.get().getTitle(),
							annotatable.getMediaType(),
							null,
							textArea.getText());
					
					if (annotatable.getServerURL() == null) {
						// Local-mode: just add the annotation without storing
						// annotatable.addAnnotation(a);
					} else {
						Create.executeJSONP(annotatable.getServerURL(), a, new AsyncCallback<JavaScriptObject>() {
							public void onSuccess(JavaScriptObject result) {
								// annotatable.addAnnotation((Annotation) result);
							}
							
							public void onFailure(Throwable t) {
								YUMA.nonFatalError(t.getMessage());
							}
						});
					}
				}
			}
		});
		
		container = new FlowPanel();
		container.setStyleName("annotation-popup");		
		container.add(content);
		container.add(textArea);
		
		initWidget(container);
		
		addDomHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				setVisible(false);
			}
		}, MouseOutEvent.getType());
	}

}
