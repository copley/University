package UnionFind;

import java.util.Collection;

public interface UnionFindElement {
	public int getID();
	public Collection<? extends UnionFindElement> getConnectedNodes();
}
