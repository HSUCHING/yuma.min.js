package at.ait.dme.yumaJS.client.annotation.widgets.edit;

import com.google.gwt.user.client.ui.RootPanel;

import at.ait.dme.yumaJS.client.annotation.Annotatable;
import at.ait.dme.yumaJS.client.annotation.Annotation;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.InadequateBrowserException;
import at.ait.dme.yumaJS.client.annotation.impl.html5media.ProgressBar;
import at.ait.dme.yumaJS.client.annotation.widgets.CommentField;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.selection.Range;
import at.ait.dme.yumaJS.client.annotation.widgets.edit.selection.RangeSelection;

public class AudioRangeEditor extends Editor {
	
	public AudioRangeEditor(Annotatable annotatable, ProgressBar progressBar, int offsetX)
		throws InadequateBrowserException {

		super(annotatable, null);		
		init(annotatable, progressBar, new RangeSelection(progressBar, offsetX, offsetX + 1), null); 
	}
	
	public AudioRangeEditor(Annotatable annotatable, ProgressBar progressBar, Annotation initialValue) 
		throws InadequateBrowserException {

		super(annotatable, initialValue);
		
		Range r = annotatable.toRange(initialValue.getFragment());
		int fromX = progressBar.toOffsetX(r.getFrom());
		int toX = progressBar.toOffsetX(r.getTo());
		init(annotatable, progressBar, new RangeSelection(progressBar, fromX, toX), initialValue);
	}
		
	private void init(Annotatable annotatable, ProgressBar progressBar, 
		RangeSelection selection, Annotation annotation) {
				
		setSelection(selection);
		
		CommentField commentField;
		if (annotation == null) {
			commentField = new CommentField(annotatable.getLabels(), true);
		} else {
			commentField = new CommentField(annotation.getText(), annotatable.getLabels(), true);
		}
		setCommentField(commentField);
		
		RootPanel.get().add(commentField, selection.getStartOffsetX()+ progressBar.getAbsoluteLeft(), 
				progressBar.getAbsoluteTop() + progressBar.getOffsetHeight());
	}

}
