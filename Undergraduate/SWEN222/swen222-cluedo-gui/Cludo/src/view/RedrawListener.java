package view;

import java.util.EventListener;

public interface RedrawListener extends EventListener {
	public void onRedraw(RedrawEvent e);
}
