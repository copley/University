package Job;

public class Job {
	public static enum JOB_TYPE {
		SIMPLE(5),
		COMPLEX(10);
		
		private int length;
		
		private JOB_TYPE(int l) {
			length = l;
		}
		
		public int getLength() {
			return length;
		}
	}
	
}
