package ManhattanSkyline;
import java.util.ArrayList;

public class ManhattanSkyline {
	public static int barometer = 0;
	
	public static ArrayList<float[]> constructSkyline(ArrayList<float[]> buildings) {
		return recConstructSkyline(buildings, 0, buildings.size());
	}
	
	private static ArrayList<float[]> recConstructSkyline(ArrayList<float[]> buildings, int start, int end) {
		if ((end - start) == 1) {
			// Get the building
			float[] building = buildings.get(start);
			
			// Construct the skyline for the single building
			ArrayList<float[]> skyline = new ArrayList<>();
			skyline.add(new float[] {building[0], building[2]});
			skyline.add(new float[] {building[1], 0});
			
			return skyline;
		}
		
		// Otherwise recursively construct the skylines for the left and right halves and return the two merged
		int middle = (end - start) / 2;
		return mergeSkylines(recConstructSkyline(buildings, start, start+middle), recConstructSkyline(buildings, start+middle, end));
	}
	
	private static ArrayList<float[]> mergeSkylines(ArrayList<float[]> leftSkyline, ArrayList<float[]> rightSkyline) {
		ArrayList<float[]> skyline = new ArrayList<>();
		
		int left = 0;
		int right = 0;
		
		while (left < leftSkyline.size() || right < rightSkyline.size()) {
			barometer++;
			
			float[] currentPair = null;
			float[] prevPair = null;
			
			if (left >= leftSkyline.size()) {
				currentPair = rightSkyline.get(right);
				prevPair = leftSkyline.get(left - 1);
				
				right++;
			} else if (right >= rightSkyline.size()) {
				currentPair = leftSkyline.get(left);
				prevPair = rightSkyline.get(right - 1);
				
				left++;
			} else if (right >= rightSkyline.size() || leftSkyline.get(left)[0] < rightSkyline.get(right)[0]) {
				currentPair = leftSkyline.get(left);
				prevPair = (right != 0) ? rightSkyline.get(right - 1) : null;
				
				left++;
			} else if (left >= leftSkyline.size() || rightSkyline.get(right)[0] < leftSkyline.get(left)[0]) {
				currentPair = rightSkyline.get(right);
				prevPair = (left != 0) ? leftSkyline.get(left - 1) : null;
				
				right++;
			} else {
				// Dosnt matter which order we assign the pairs as they are at the same position
				currentPair = leftSkyline.get(left);
				prevPair = rightSkyline.get(right);
				
				left++;
				right++;
			}
			
			// If there is no previous pair or the current pair is higher than the previouse
			// then just add the current pair.
			if (prevPair == null) {
				skyline.add(currentPair);
			} else if (currentPair[1] > prevPair[1]) {
				skyline.add(currentPair);
			} else {
				float[] change = new float[] {currentPair[0], prevPair[1]};
				
				if (skyline.size() == 0 || skyline.get(skyline.size()-1)[1] != change[1]) {
					skyline.add(change);
				}
			}
		}
		
		return skyline;
	}
}
