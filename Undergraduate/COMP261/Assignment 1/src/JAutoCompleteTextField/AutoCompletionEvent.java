package JAutoCompleteTextField;

import java.util.ArrayList;
import java.util.EventObject;

public class AutoCompletionEvent extends EventObject {
	private String partial;
	private ArrayList<String> completions;
	
	public AutoCompletionEvent(Object source, String partial, ArrayList<String> completions) {
		super(source);
		
		this.partial = partial;
		this.completions = completions;
	}
	
	public String getPartialString() {
		return partial;
	}
	
	public ArrayList<String> getCompletions() {
		return completions;
	}
}
