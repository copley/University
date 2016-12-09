package OptimalJobSchedule;

import java.util.Arrays;
import Job.Job.JOB_TYPE;

public class OptimalJobSchedule {
	public static int scheduleJobs(int m, JOB_TYPE[] jobs) {
		int[] machines = new int[m];
		
		for (int i = 0; i < jobs.length; i++) {
			if (jobs[i] == JOB_TYPE.COMPLEX) {
				Arrays.sort(machines);
				machines[0] += JOB_TYPE.COMPLEX.getLength();
			}
		}
		
		for (int i = 0; i < jobs.length; i++) {
			if (jobs[i] == JOB_TYPE.SIMPLE) {
				Arrays.sort(machines);
				machines[0] += JOB_TYPE.SIMPLE.getLength();
			}
		}
		
		Arrays.sort(machines);
		
		return machines[m-1];
	}
}
