package AStar;

/**
 * 
 * @author danielbraithwt
 *
 * @param <E>
 */
public interface Metric<E> {
	
	/**
	 * 
	 * @param e1
	 * @param e2
	 * @return
	 */
	public double estimate(E e1, E e2);
}
