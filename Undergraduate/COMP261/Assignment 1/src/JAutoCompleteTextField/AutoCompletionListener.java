package JAutoCompleteTextField;

import java.util.ArrayList;
import java.util.EventListener;

public interface AutoCompletionListener extends EventListener {
	public void availableCompletions(AutoCompletionEvent e);
}
