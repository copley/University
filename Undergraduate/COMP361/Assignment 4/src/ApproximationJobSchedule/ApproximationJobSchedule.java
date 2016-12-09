package ApproximationJobSchedule;

import java.util.Arrays;
import java.util.Random;

import Job.Job.JOB_TYPE;

public class ApproximationJobSchedule {
	public static int BAROMETER = 0;
	
	public static int scheduleJobs(int m, JOB_TYPE[] jobs) {
		BAROMETER = 0;
		
		int[] machines = new int[m];
		
		for (int i = 0; i < jobs.length; i++) {
			int j = 0;
			
			for (int k = 0; k < machines.length; k++) {
				BAROMETER++;
				
				if (machines[k] < machines[j]) {
					j = k;
				}
			}
			
			machines[j] += jobs[i].getLength();
		}
		
		Arrays.sort(machines);
		
		return machines[m-1];
	}
	
	public static void main(String[] args) {
		//JOB_TYPE[] jobs = new JOB_TYPE[] {JOB_TYPE.SIMPLE, JOB_TYPE.SIMPLE, JOB_TYPE.COMPLEX, JOB_TYPE.COMPLEX, JOB_TYPE.SIMPLE, JOB_TYPE.SIMPLE, JOB_TYPE.SIMPLE};
		JOB_TYPE[] jobs = new JOB_TYPE[] {JOB_TYPE.COMPLEX, JOB_TYPE.COMPLEX, JOB_TYPE.COMPLEX, JOB_TYPE.COMPLEX, JOB_TYPE.COMPLEX, JOB_TYPE.COMPLEX, JOB_TYPE.COMPLEX};
		int maxTime = scheduleJobs(7, jobs);
		
		System.out.println(maxTime/(10.0f * jobs.length));
	}
}
