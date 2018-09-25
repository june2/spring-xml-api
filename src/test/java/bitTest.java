//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//
//public class bitTest {
//	
//	public static void main(String[] args) {
//		// total viewing time
//		int total = 0;
//		
//		// Generate dummy data
//		List<Map<String, Object>> datas= crateDummyData();
//		
//		for (Map<String, Object> map : datas) {
//			System.out.println(map);
//		}
//		
//		String sum[] =  {};
//		
//		for (int i = 1; i < datas.size(); i++) {
//			if(i == 1 ) {
//				sum = orBinary(
//					convertTimeToBinary(datas.get(0).get("watching_start").toString(), datas.get(0).get("watching_time").toString()),
//					convertTimeToBinary(datas.get(1).get("watching_start").toString(), datas.get(1).get("watching_time").toString()));
//			}else {
//				sum = orBinary(
//						sum,
//						convertTimeToBinary(datas.get(i).get("watching_start").toString(), datas.get(i).get("watching_time").toString()));
//			}
//		}
//		
//		for (int i = 0; i < sum.length; i++) {
//			System.out.println(i +" "+ sum[i] +" "+ Integer.parseInt(sum[i], 2) +" "+ Integer.bitCount(Integer.parseInt(sum[i], 2)));
//			total = total + Integer.bitCount(Integer.parseInt(sum[i], 2));
//		}
//		
//		System.out.println();
//		System.out.println(total);
//		
//		
////		// total viewing time
////		int total = 0;
////		
////		// watching time 10~15
////		String arr1[] = convertTimeToBinary("0", "10");
////		for (int i = 0; i < arr1.length; i++) {
////			System.out.println(i +" "+ arr1[i] +" "+ Integer.parseInt(arr1[i], 2) +" "+ Integer.bitCount(Integer.parseInt(arr1[i], 2)));
////		}
////		System.out.println();
////
////		// watching time 60~70
////		String arr2[] = convertTimeToBinary("61", "70");
////		for (int i = 0; i < arr2.length; i++) {
////			System.out.println(i +" "+ arr2[i] +" "+ Integer.parseInt(arr2[i], 2) +" "+ Integer.bitCount(Integer.parseInt(arr2[i], 2)));
////		}
////		System.out.println();
////		
////		// calculate time
////		String res[] = orBinary(arr1, arr2);
////		
////		for (int i = 0; i < res.length; i++) {
////			System.out.println(i +" "+ res[i] +" "+ Integer.parseInt(res[i], 2) +" "+ Integer.bitCount(Integer.parseInt(res[i], 2)));
////			total = total + Integer.bitCount(Integer.parseInt(res[i], 2));
////		}
////		
////		System.out.println();
////		System.out.println(total);
//	}
//	
//	public static List<Map<String, Object>> crateDummyData() {
//		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
//		Map<String, Object> data;
//		Random ran = new Random();
//
////		for (int i = 0; i < 10; i++) {
////			data = new HashMap<String, Object>();
////			data.put("watching_start", ran.nextInt(120));
////			data.put("watching_time", ran.nextInt(160));
////			datas.add(data);
////		}
//		
//		data = new HashMap<String, Object>();
//		data.put("watching_start", 0);
//		data.put("watching_time", 1);
//		datas.add(data);
//		
//		data = new HashMap<String, Object>();
//		data.put("watching_start", 12);
//		data.put("watching_time", 12);
//		datas.add(data);
//
////		data = new HashMap<String, Object>();
////		data.put("watching_start", 1);
////		data.put("watching_time", 1);
////		datas.add(data);
//		
////		data = new HashMap<String, Object>();
////		data.put("watching_start", 6);
////		data.put("watching_time", 6);
////		datas.add(data);
////
////		data = new HashMap<String, Object>();
////		data.put("watching_start", 6);
////		data.put("watching_time", 6);
////		datas.add(data);
////		
////		data = new HashMap<String, Object>();
////		data.put("watching_start", 6);
////		data.put("watching_time", 24);
////		datas.add(data);
//		
//
//		return datas;
//	}
//	
//	
//	public static String[] orBinary(String[] arr1, String[] arr2) {
//		int size = 0;
//		String a = "0";
//		String b = "0";
//		
//		if(arr1.length > arr2.length) {
//			size = arr1.length;
//		}else {
//			size = arr2.length;
//		}
//		
//		String arr[] =  new String[size];
//		
//		for (int i = 0; i < size; i++) {
//			if(i >= arr1.length) {
//				a = "0";
//			}else {
//				a = arr1[i];
//			}
//			
//			if(i >= arr2.length) {
//				b = "0";
//			}else {
//				b = arr2[i];
//			}
//			
//			arr[i] = Integer.toBinaryString(Integer.parseInt(a, 2)|Integer.parseInt(b, 2));
//		}
//		
//		return arr;
//	}
//	
//	public static String[] convertTimeToBinary(String watchingStart, String watchingEnd) {
//		
//		int start = Integer.parseInt(watchingStart);
//		int end = Integer.parseInt(watchingEnd);
//		
//		if(start == 0) start = 1;
//		
//		int divide = 30;
//		int size = (end/divide);
//		int rest = (end%divide);
//		int position = 0;
//		
//		String temp = "";
//		String flag = "0"; 
//		
//		if(rest > 0) {
//			size = size + 1;
//		} 
//		
//		String arr[] =  new String[size];
//
//		for (int j = 1; j <= end; j++) {
//			if(j == start) {
//				flag = "1";
//			}
//			
//			temp = temp + flag;
//			
//			if(j%divide == 0) {
//				arr[position] = temp;
//				temp = "";
//				position = position +1;
//			}
//			
//			if(end%divide != 0 && j == end) {
//				String zero = "";
//				for (int i = 0; i < divide-(end%divide); i++) {
//					zero = zero + "0"; 
//				}
//				arr[position] = temp + zero;
//			}
//		}
//		
//		return arr;
//	}
//	
//}
