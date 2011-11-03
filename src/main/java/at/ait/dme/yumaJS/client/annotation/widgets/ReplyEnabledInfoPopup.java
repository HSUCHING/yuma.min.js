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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ReplyEnabledInfoPopup extends InfoPopup {
	
	private Annotatable annotatable;
	
	private Annotation rootAnnotation;
	
	private CommentField replyField;
	
	private List<Annotation> replies = new ArrayList<Annotation>();
	
	public ReplyEnabledInfoPopup(final Annotatable annotatable, final Annotation rootAnnotation, Labels labels) {
		super(annotatable, rootAnnotation, labels);
		
		container.setStyleName("yuma-annotation-list");	
		
		this.annotatable = annotatable;
		this.rootAnnotation = rootAnnotation;
				
		replyField = new CommentField(labels, false);
		replyField.addSaveClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				save();
			}
		});
		replyField.setVisible(false);
		
		container.add(replyField);
		
		addDomHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				replyField.setVisible(true);
			}
		}, MouseOverEvent.getType());
	}
	
	@Override
	protected void onMouseLeave() {
		if (!replyField.hasFocus()) {
			replyField.setVisible(false);
			setVisible(false);
		}
	}
	
	public void addReply(final Annotation reply) {
		if (!reply.getIsReplyTo().equals(rootAnnotation.getID()))
			return;
		
		replyField.setText(null);
		replyField.setFocus(false);
		
		final AnnotationWidget widget = new AnnotationWidget(reply, annotatable.getLabels());
		widget.addEditClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				widget.makeEditable(new ClickHandler() {
					public void onClick(ClickEvent event) {
						System.out.println("yo");
						save();
					}
				}, annotatable.getLabels());
			}
		});
		
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
	
	private void save() {
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
