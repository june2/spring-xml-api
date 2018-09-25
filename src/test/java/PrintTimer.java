//import java.util.Timer;
//
////import com.blackboard.consulting.util.ScheduledJob;
//
//public class PrintTimer {
//
//	
//	public static Timer jobScheduler = null;
//	private static ScheduledJob scheduledJob;
//	
//	public static void main(String[] args) {
//		
//		boolean isActive = false;
//		if(scheduledJob != null) {
//			System.out.println(isActive);
//		}
//		
//		System.out.println(isActive);
//		
//		if(jobScheduler == null) {
//			System.out.println("---------------- n ull");
//			jobScheduler = new Timer();
//		}
//		
//		jobScheduler.scheduleAtFixedRate(new ScheduledJob(), 0, 10000);
//
////		try {
////			Thread.sleep(1000 * 10);
////		} catch (InterruptedException ex) {
////			//
////		}
////		jobScheduler.cancel();
////		System.out.println("----------------");
////		
////		
////		
////		if(jobScheduler == null) {
////			System.out.println("restart");
////			jobScheduler = new Timer();
////		}
////		
////		jobScheduler.scheduleAtFixedRate(new ScheduledJob(), 0, 2000);
////		
////		try {
////			Thread.sleep(1000 * 10);
////		} catch (InterruptedException ex) {
////			//
////		}
////		
////		jobScheduler.cancel();
////		System.out.println("----------------");
//	}
//
//}
