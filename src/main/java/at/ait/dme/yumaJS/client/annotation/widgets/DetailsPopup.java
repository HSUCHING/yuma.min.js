package at.ait.dme.yumaJS.client.annotation.widgets;

import at.ait.dme.yumaJS.client.YUMA;
import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.init.Labels;
import at.ait.dme.yumaJS.client.io.Delete;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.PushButton;

/**
 * The overlay element used to show the details of an {@link Annotation}.
 * The overlay is composed of a container DIV (CSS class 'annotation-popup')
 * and the following nested elements:
 * <p>
 * <li>
 * <ul>A DIV containing the text content</ul>
 * <ul>A 'REPLY' button</ul> 
 * <ul>An 'EDIT' button</ul>
 * <ul>A 'DELETE' button</ul>
 * </li>
 * 
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class DetailsPopup extends Composite implements HasMouseOutHandlers {
	
	private FlowPanel container;
	
	private PushButton /* btnReply, */ btnEdit, btnDelete;
	
	public DetailsPopup(final Annotatable annotatable, final Annotation a, Labels labels) {
		FlowPanel content = new FlowPanel();
		content.setStyleName("annotation-popup-content");
		content.add(new InlineHTML(a.getText()));
		
		if (labels == null) {
			// btnReply = new PushButton("REPLY");
			btnEdit = new PushButton("EDIT");
			btnDelete = new PushButton("DELETE");
		} else {
			// btnReply = new PushButton(labels.reply());
			btnEdit = new PushButton(labels.edit());
			btnDelete = new PushButton(labels.delete());			
		}

		// btnReply.setStyleName("annotation-popup-button");
		btnEdit.setStyleName("annotation-popup-button");		
		btnEdit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				annotatable.removeAnnotation(a);
				annotatable.editAnnotation(a);
			}
		});
		
		btnDelete.setStyleName("annotation-popup-button");
		btnDelete.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (annotatable.getServerURL() == null) {
					annotatable.removeAnnotation(a);
				} else {
					Delete.executeJSONP(annotatable.getServerURL(), a.getID(), new AsyncCallback<Void>() {
						public void onSuccess(Void result) {
							annotatable.removeAnnotation(a);
						}			
	
						public void onFailure(Throwable t) {
							YUMA.nonFatalError(t.getMessage());
						}
					});
				}				
			}
		});
		
		showButtons(false);
		
		content.addDomHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				showButtons(true);
			}
		}, MouseOverEvent.getType());
		
		container = new FlowPanel();
		container.setStyleName("annotation-popup");		
		container.add(content);
		// container.add(btnReply);
		container.add(btnEdit);
		container.add(btnDelete);
		
		
		setVisible(false);
		
		initWidget(container);
		
		addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				setVisible(false);
				showButtons(false);
			}
		});
	}
	
	@Override
	public void setVisible(boolean visible) {
		container.setVisible(true);
		Style style = container.getElement().getStyle();
		if (visible) {
			style.setVisibility(Visibility.VISIBLE);
			style.setOpacity(1);
		} else {
			style.setVisibility(Visibility.HIDDEN);
			style.setOpacity(0);
		}
	}
	
	private void showButtons(boolean show) {
		// btnReply.setVisible(show);
		btnEdit.setVisible(show);
		btnDelete.setVisible(show);
	}
	
	public boolean contains(int x, int y) {
		int left = container.getAbsoluteLeft();
		int top = container.getAbsoluteTop();
		int w = container.getOffsetWidth();
		int h = container.getOffsetHeight();
		
		if (x < left)
			return false;
		
		if (x > left + w)
			return false;
		
		if (y < top)
			return false;
		
		if (y > top + h)
			return false;
		
		return true;
	}
	
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}

}
