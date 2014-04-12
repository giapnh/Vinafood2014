package hust.hgbk.vtio.vinafood.config;

import java.util.ArrayList;

public class ServerConfig {
	public static final String VTIO_REPOSITORY_KEY = "Db0hsr9lGsz6FaUGW7lPZcZ2VcFOMrzHZIJT2qi27p8=";
	public static final String SERVER_DOMAIN = "icompanion.vn";
	public static final String SERVICE_NAMESPACE = "http://service.sig.com/";
	public static final String USER_SERVICE_NAME_SPACE = "http://userservice.icom.sig/";
	private static String serverSpecificIp = "14.160.65.178";
	public static String SIG_APP_URI = "https://play.google.com/store/apps/developer?id=Semantic+Innovation+Group";

	public static String getWSDLURL() {
		return "http://" + serverSpecificIp
				+ ":8080/SIG-CORE-SERVICE/CoreServicePort?WSDL";
	}

	public static String getUserServiceWSDLURL() {
		return "http://" + serverSpecificIp
				+ ":8080/IComUser/IcomUserServicePort?WSDL";
	}

	public static void setServerSpecificIp(String ip) {
		serverSpecificIp = ip;
	}

	public static String getServerSpecificIp() {
		return serverSpecificIp;
	}

	// public static String serverIp = "icompanion.vn";
	// public static String serverIp = "113.161.116.127";
	// public static String
	// URL="http://192.168.43.186:8080/VTIO-SERVER/VtioServicePort?WSDL";
	// public static String URL="http://"+ serverIp
	// +":8080/SIG-CORE-SERVICE/CoreServicePort?WSDL";
	// public static String
	// URL="http://210.245.54.61:8080/SIG-CORE-SERVICE/CoreServicePort?WSDL";
	// public static String
	// URL="http://icompanion.vn:8080/VTIO-SERVER/VtioServicePort?WSDL";
	// public static String NAMESPACE = "http://service.sig.com/";
	// public static String SERVER_KEY =
	// "Db0hsr9lGsz6FaUGW7lPZcZ2VcFOMrzHZIJT2qi27p8=";
	// public static String SERVER_KEY =
	// "NX9cmJk7pS6XXKyJ1WQPtZMLzCQ2zOgbRhrMip82BJw=";
	public static String LANGUAGE_CODE = "en";

	public static final String HANOI_URI = "http://hust.se.vtio.owl#hanoi-city";
	public static final String HOCHIMINH_URI = "http://hust.se.vtio.owl#hochiminh-city";
	public static String currentCityUri = HANOI_URI;
	public static String currentCityLabel = "Hà Nội";
	// public static void validateURL(String newIp){
	// serverIp = newIp;
	// URL="http://"+ serverIp +":8080/SIG-CORE-SERVICE/CoreServicePort?WSDL";
	// }
	public static final String[][] cityString = {
			{ "hanoi", "Hà Nội", HANOI_URI },
			{ "hochiminh", "Tp. Hồ Chí Minh", HOCHIMINH_URI },
			{ "danang", "Đà Nẵng", "http://hust.se.vtio.owl#da-nang-city" },
			{ "quangninh", "Quảng Ninh",
					"http://hust.se.vtio.owl#quang-ninh-province" },
			{ "haiphong", "Hải Phòng", "http://hust.se.vtio.owl#hai-phong-city" },
			{ "khanhhoa", "Khánh Hòa",
					"http://hust.se.vtio.owl#khanh-hoa-province" },
			{ "lamdong", "Lâm Đồng",
					"http://hust.se.vtio.owl#lam-dong-province" },
			{ "vungtau", "Bà Rịa-Vũng Tàu",
					"http://hust.se.vtio.owl#ba-ria-vung-tau-province" } };

	public static String[] listOfCity() {
		ArrayList<String> cityName = new ArrayList<String>();
		for (int i = 0; i < cityString.length; i++) {
			cityName.add(cityString[i][1]);
		}
		String[] vals = new String[cityName.size()];
		for (int j = 0; j < cityName.size(); j++) {
			vals[j] = cityName.get(j);
		}
		return vals;
	}

	public static String[] infoOfCity(String cityName) {
		for (int i = 0; i < cityString.length; i++) {
			if (cityString[i][1].trim().equals(cityName.trim())) {
				return cityString[i];
			}
		}
		// Return default
		return cityString[0];
	}
}
