//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Locale;
//import java.util.TimeZone;
//
//public class TimezoneTest {
//	public static void main(String[] args) throws Exception {
//		//2017-11-12T12:45:25.95Z
//		System.out.println("TIMES " + utcToLocaltime(convertToDate("2017-11-12T12:45:25.95Z".replace("T", " "))));
//		System.out.println("TIMES " + utcToLocaltime("2017-11-27 16:35:00.000"));
//		
////		Date from = new Date();
////		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////		transFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
////		String to = transFormat.format(from);
////		System.out.println(to.replace(" ","T"));
////		
////		long ONE_MINUTE_IN_MILLIS=60000;//millisecs
////
////		Calendar date = Calendar.getInstance();
////		long t= date.getTimeInMillis();
////		Date afterAddingTenMins=new Date(t + (10 * ONE_MINUTE_IN_MILLIS));
////		
////		
////		System.out.println(afterAddingTenMins);
//	}
//
//	public static String utcToLocaltime(String datetime) throws Exception {
//		String locTime = null;
//		TimeZone tz = TimeZone.getTimeZone("GMT-09:00"); // 해당 국가 일시 확인 할 때, 한국은 +9
////		 TimeZone tz = TimeZone.getDefault();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//		try {
//			Date parseDate = sdf.parse(datetime);
//			long milliseconds = parseDate.getTime();
//			int offset = tz.getOffset(milliseconds);
//			locTime = sdf.format(milliseconds + offset);
//			locTime = locTime.replace("+0000", "");
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new Exception(e);
//		}
//
//		return locTime;
//	}
//
//	public static String convertToDate(String inputDate) throws Exception {
//		String dateTime = inputDate;
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
//		Date parseDate = null;
//		String convertedDate = null;
//
//		try {
//			parseDate = format.parse(dateTime);
//			DateFormat dateFormatNeeded = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			convertedDate = dateFormatNeeded.format(parseDate);
//		} catch (ParseException e) {
//			e.printStackTrace();
//			throw new Exception(e);
//		}
//
//		return convertedDate;
//	}
//
//}
