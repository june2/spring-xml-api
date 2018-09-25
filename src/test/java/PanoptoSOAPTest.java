//import org.junit.Test;
//
//import com.panopto.blackboard.PanoptoData;
//import com.panopto.services.DetailedUsageResponseItem;
//
//public class PanoptoSOAPTest {
//	
//	@Test
//	public void sessionDetailedUsageAPI() throws Exception {
//		PanoptoData soap = new PanoptoData("testkorea.hosted.panopto.com", "jykim", "panopto321", "usageReporting");	
//		DetailedUsageResponseItem reporting[] = soap.getSessionDetailedUsage("ffa8ad24-b467-4fc8-8fec-a8560107e052",
//				"2017-09-06T00:00:00", "2018-12-06T00:00:00");
//		
//		System.out.println("getSessionDetailedUsage size :: " + reporting.length);
//		System.out.println("getSessionDetailedUsage size :: " + reporting[0].toString());
//	}
//	
////	@Test
////    public void sessionUserDetailedUsageAPI() throws Exception {
////		PanoptoData soap = new PanoptoData("salesdemo.panopto.kr", "jykim", "panopto321", "usageReporting");	
////		DetailedUsageResponseItem reporting[] = 
////				soap.getSessionUserDetailedUsage("c5024173-1d15-4ae5-bce8-e4116ae83978", "381017ee-9741-4903-9109-ffb95066a8db");
////		
////		System.out.println("getSessionUserDetailedUsage size :: " + reporting.length);
////	}
////	
////	@Test
////    public void getUserByKeyAPI() throws Exception {
////		PanoptoData soap = new PanoptoData("salesdemo.panopto.kr", "jykim", "panopto321", "userManagement");
////		com.panopto.services.User user = soap.getUserBykey("jykim");
////		System.out.println("getUserByKey :: " + user.toString());
////	}
//	
//}
