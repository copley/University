package ManhattanSkyline.tests;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import ManhattanSkyline.ManhattanSkyline;

public class ManhattanSkylineTests {

	@Test
	public void test_01() {
		ArrayList<float[]> buildings = new ArrayList<>();

		buildings.add(new float[] { 0.0f, 2.0f, 1.0f });
		buildings.add(new float[] { 1.0f, 4.0f, 2.0f });
		buildings.add(new float[] { 2.0f, 3.0f, 3.0f });
		buildings.add(new float[] { 5.0f, 6.0f, 2.0f });
		
		ArrayList<float[]> answer = new ArrayList<>();
		answer.add(new float[] {0.0f, 1.0f});
		answer.add(new float[] {1.0f, 2.0f});
		answer.add(new float[] {2.0f, 3.0f});
		answer.add(new float[] {3.0f, 2.0f});
		answer.add(new float[] {4.0f, 0.0f});
		answer.add(new float[] {5.0f, 2.0f});
		answer.add(new float[] {6.0f, 0.0f});
		
		ArrayList<float[]> skyline = ManhattanSkyline.constructSkyline(buildings);
		
		assertTrue(compareAnswer(answer, skyline));
	}
	
	@Test
	public void test_02() {
		ArrayList<float[]> buildings = new ArrayList<>();

		buildings.add(new float[] { 0.0f, 1.0f, 2.0f });
		buildings.add(new float[] { 2.0f, 3.0f, 3.0f });
		buildings.add(new float[] { 4.0f, 5.0f, 1.0f });
		
		ArrayList<float[]> answer = new ArrayList<>();
		answer.add(new float[] {0.0f, 2.0f});
		answer.add(new float[] {1.0f, 0.0f});
		answer.add(new float[] {2.0f, 3.0f});
		answer.add(new float[] {3.0f, 0.0f});
		answer.add(new float[] {4.0f, 1.0f});
		answer.add(new float[] {5.0f, 0.0f});
		
		ArrayList<float[]> skyline = ManhattanSkyline.constructSkyline(buildings);
		
		assertTrue(compareAnswer(answer, skyline));
	}
	
	@Test
	public void test_03() {
		ArrayList<float[]> buildings = new ArrayList<>();

		buildings.add(new float[] { 0.0f, 6.0f, 2.0f });
		buildings.add(new float[] { 1.0f, 3.0f, 1.0f });
		buildings.add(new float[] { 4.0f, 5.0f, 1.0f });
		
		ArrayList<float[]> answer = new ArrayList<>();
		answer.add(new float[] {0.0f, 2.0f});
		answer.add(new float[] {6.0f, 0.0f});
		
		ArrayList<float[]> skyline = ManhattanSkyline.constructSkyline(buildings);
		
		assertTrue(compareAnswer(answer, skyline));
	}
	
	@Test
	public void test_04() {
		ArrayList<float[]> buildings = new ArrayList<>();

		buildings.add(new float[] { 1.0f, 4.0f, 2.0f });
		buildings.add(new float[] { 0.0f, 2.0f, 1.0f });
		buildings.add(new float[] { 5.0f, 6.0f, 2.0f });
		buildings.add(new float[] { 2.0f, 3.0f, 3.0f });
		
		ArrayList<float[]> answer = new ArrayList<>();
		answer.add(new float[] {0.0f, 1.0f});
		answer.add(new float[] {1.0f, 2.0f});
		answer.add(new float[] {2.0f, 3.0f});
		answer.add(new float[] {3.0f, 2.0f});
		answer.add(new float[] {4.0f, 0.0f});
		answer.add(new float[] {5.0f, 2.0f});
		answer.add(new float[] {6.0f, 0.0f});
		
		ArrayList<float[]> skyline = ManhattanSkyline.constructSkyline(buildings);
		
		assertTrue(compareAnswer(answer, skyline));
	}
	
	public boolean compareAnswer(ArrayList<float[]> answer, ArrayList<float[]> skyline) {
		for (int i = 0; i < answer.size(); i++) {
			if (answer.get(i)[0] != skyline.get(i)[0] || answer.get(i)[1] != skyline.get(i)[1]) {
				return false;
			}
		}
		
		return true;
	}

}
