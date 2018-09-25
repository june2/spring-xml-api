//import java.util.Date;
//import java.util.TimerTask;
//
//import com.panopto.blackboard.PanoptoData;
//import com.panopto.services.DetailedUsageResponseItem;
//
//import blackboard.platform.RuntimeBbServiceException;
//
//class ScheduledJob extends TimerTask {
//
//	private boolean isRunning = false;
//
//	public boolean isRunning() {
//		return isRunning;
//	}
//
//	public void setRunning(boolean isRunning) {
//		this.isRunning = isRunning;
//	}
//
//	@Override
//	public void run() {
//		
//		PanoptoData soap = new PanoptoData("salesdemo.panopto.kr1", "jykim", "panopto321", "usageReporting");
//		
////		try {
//			DetailedUsageResponseItem reporting[] = soap.getSessionDetailedUsage("f5841236-1e6e-4ea7-975b-afe6c58bfc1d", "2017-10-31 23:59:59.0", "2017-12-31 23:59:59.0");
//			
//			if(reporting != null && reporting.length>0) {				
//				System.out.println("getSessionDetailedUsage size :: " + reporting);
//				System.out.println("getSessionDetailedUsage size :: " + reporting.length);
//			}
//			System.out.println(new Date());
////			throw new RuntimeBbServiceException("error");
//			
////		} catch (Exception e) {
////			System.out.println("error !!!!" + e);
////			// TODO: handle exception
////		}
//	}
//}
//
