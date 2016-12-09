package ProbabilisticJobSchedule;

import java.util.Arrays;
import java.util.Random;
import java.util.prefs.BackingStoreException;

import Job.Job.JOB_TYPE;

public class ProbabilisticJobSchedule {
	public static int BAROMETER = 0;
	
	public static int scheduleJobs(int m, JOB_TYPE[] jobs) {
		BAROMETER = 0;
		
		Random r = new Random();
		int[] machines = new int[m];
		
		for (int i = 0; i < jobs.length; i++) {
			//BAROMETER++;
			
			//if (r.nextDouble() < 0.3) {
			if (jobs[i] == JOB_TYPE.SIMPLE) {
				BAROMETER++;
				int j = r.nextInt(m);
				machines[j] += jobs[i].getLength();
			} else {
				int j = 0;
				
				for (int k = 0; k < machines.length; k++) {
					BAROMETER++;
					
					if (machines[k] < machines[j]) {
						j = k;
					}
				}
				
				machines[j] += jobs[i].getLength();
			}
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
