package Tests;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import QuadTree.QuadTree;

public class QuadTreeTest {

	private Random r = new Random();
	private final int TEST_SIZE = 200;
	private final int MAX_COORD_SIZE = 1000;
	private QuadTree<Integer> quadTree;

	private ArrayList<Point> addData() {
		ArrayList<Point> points = new ArrayList<Point>();
		for (int i = 0; i < TEST_SIZE; i++) {
			int x = r.nextInt(MAX_COORD_SIZE);
			int y = r.nextInt(MAX_COORD_SIZE);

			Point p = new Point(x, y);
			points.add(p);

			quadTree.add(p, i);
		}

		return points;
	}

	@Before
	public void initQuadTree() {
		quadTree = new QuadTree<Integer>(0, 0, MAX_COORD_SIZE, MAX_COORD_SIZE);
	}

	@Test
	public void testInsert() {
		for (int i = 0; i < TEST_SIZE; i++) {
			int x = r.nextInt(MAX_COORD_SIZE);
			int y = r.nextInt(MAX_COORD_SIZE);

			Point p = new Point(x, y);

			boolean res = quadTree.add(p, i);

			if (!res) {
				fail();
			}
		}
	}

	@Test
	public void testGetClosestOnMatch() {
		ArrayList<Point> points = addData();

		for (int i = 0; i < points.size(); i++) {
			if (quadTree.get(points.get(i)) != i) {
				quadTree.get(points.get(i));
				fail();
			}
			//assertTrue();
		}
	}

	@Test
	public void testGetClosestOnRandom() {
		addData();

		for (int i = 0; i < TEST_SIZE; i++) {
			int x = r.nextInt(MAX_COORD_SIZE);
			int y = r.nextInt(MAX_COORD_SIZE);

			Point p = new Point(x, y);

			Integer res = quadTree.get(p);

			if (res == null) {
				fail();
			}
		}
	}

	@Test
	public void testInsertOutside() {
		try {
			quadTree.add(new Point(MAX_COORD_SIZE + 50, MAX_COORD_SIZE + 50), 1);
			fail();
		} catch (IndexOutOfBoundsException e) {
			
		}
	}
}
