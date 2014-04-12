package hust.hgbk.vtio.vinafood.constant;

import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.ontology.ClassData;
import hust.hgbk.vtio.vinafood.ontology.simple.ClassDataSimple;
import hust.hgbk.vtio.vinafood.ontology.simple.InstanceDataSimple;
import hust.hgbk.vtio.vinafood.ontology.simple.LabelIconUriSimple;
import hust.hgbk.vtio.vinafood.ontology.simple.NodeTreeDataSimple;
import hust.hgbk.vtio.vinafood.ontology.simple.PlaceDataSimple;
import hust.hgbk.vtio.vinafood.ontology.simple.PropertyDataSimple;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;

public class OntologyCache {
	public static HashMap<String, ArrayList<InstanceDataSimple>> listAllSimpleClass = new HashMap<String, ArrayList<InstanceDataSimple>>();
	public static HashMap<String, ArrayList<InstanceDataSimple>> listSubject = new HashMap<String, ArrayList<InstanceDataSimple>>();
	public static HashMap<String, ArrayList<InstanceDataSimple>> listPredicate = new HashMap<String, ArrayList<InstanceDataSimple>>();
	public static HashMap<String, ArrayList<InstanceDataSimple>> listObject = new HashMap<String, ArrayList<InstanceDataSimple>>();

	public static HashMap<String, ArrayList<PropertyDataSimple>> propertiesOfClass = new HashMap<String, ArrayList<PropertyDataSimple>>();
	public static HashMap<String, ArrayList<ClassDataSimple>> rangesOfProperty = new HashMap<String, ArrayList<ClassDataSimple>>();
	public static HashMap<String, ArrayList<ClassDataSimple>> listCategory = new HashMap<String, ArrayList<ClassDataSimple>>();
	public static HashMap<String, ArrayList<ClassDataSimple>> listAllSubclass = new HashMap<String, ArrayList<ClassDataSimple>>();
	public static HashMap<String, ArrayList<InstanceDataSimple>> instanceOfClass = new HashMap<String, ArrayList<InstanceDataSimple>>();
	public static HashMap<String, ArrayList<PlaceDataSimple>> placeInstance = new HashMap<String, ArrayList<PlaceDataSimple>>();

	public static HashMap<String, ClassData> listClassData = new HashMap<String, ClassData>();

	public static HashMap<String, ArrayList<ArrayList<String>>> listQuery = new HashMap<String, ArrayList<ArrayList<String>>>();
	// public static HashMap<String, LabelIconSimple> hashMapLabelIcon = new
	// HashMap<String, LabelIconSimple>();
	public static HashMap<String, String> hashMapTypeLabelToUri = new HashMap<String, String>();
	public static HashMap<String, String> hashMapLabelToURI = new HashMap<String, String>();
	// public static ArrayList<ArrayList<ClassDataSimple>>
	// allSubClassLevel2OfTopic=new ArrayList<ArrayList<ClassDataSimple>>();
	public static ArrayList<ClassDataSimple> preferUser = new ArrayList<ClassDataSimple>();
	public static ArrayList<ArrayList<String>> classWithParent = new ArrayList<ArrayList<String>>();

	public static HashMap<String, ArrayList<ArrayList<String>>> listQueryWithGeo = new HashMap<String, ArrayList<ArrayList<String>>>();
	public static HashMap<String, Boolean> listAskQuery = new HashMap<String, Boolean>();

	public static NodeTreeDataSimple rootTree = new NodeTreeDataSimple();

	public static HashMap<String, Integer> objectPropertySize = new HashMap<String, Integer>();
	public static HashMap<String, Integer> dataPropertySize = new HashMap<String, Integer>();
	public static HashMap<String, Integer> allClassSize = new HashMap<String, Integer>();
	public static HashMap<String, Integer> allInstanceSize = new HashMap<String, Integer>();

	// hashmap luu uri và id cua icon
	public static HashMap<String, LabelIconUriSimple> uriOfIcon = new HashMap<String, LabelIconUriSimple>();

	// luu anh cua dia diem
	public static HashMap<String, SoftReference<Bitmap>> imageOfPlace = new HashMap<String, SoftReference<Bitmap>>();

	/*
	 * public static void loadUriIcon() {
	 * 
	 * 
	 * 
	 * LabelIconUriSimple atmIcon = new LabelIconUriSimple("ATM",
	 * "http://hust.se.vtio.owl#ATM",
	 * "http://icompanion.vn/resources/icon/atm.png", R.drawable.atm);
	 * uriOfIcon.put("http://hust.se.vtio.owl#ATM", atmIcon);
	 * 
	 * LabelIconUriSimple accomodationIcon = new
	 * LabelIconUriSimple("Accomodation","http://hust.se.vtio.owl#Accomodation",
	 * "http://icompanion.vn/resources/icon/accomodation.png",
	 * R.drawable.accommodation);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Accomodation", accomodationIcon);
	 * 
	 * LabelIconUriSimple architectureIcon = new
	 * LabelIconUriSimple("Architecture","http://hust.se.vtio.owl#Architecture",
	 * "http://icompanion.vn/resources/icon/architecture.png",
	 * R.drawable.architecture);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Architecture", architectureIcon);
	 * 
	 * LabelIconUriSimple bankAgentIcon = new
	 * LabelIconUriSimple("Bank Agent","http://hust.se.vtio.owl#Bank-Agent",
	 * "http://icompanion.vn/resources/icon/bank_agent.png",
	 * R.drawable.bank_agent);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Bank-Agent", bankAgentIcon);
	 * 
	 * LabelIconUriSimple bankResourceIcon = new
	 * LabelIconUriSimple("Bank Resource"
	 * ,"http://hust.se.vtio.owl#Bank-Resource",
	 * "http://icompanion.vn/resources/icon/bank.png", R.drawable.bank);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Bank-Resource", bankResourceIcon);
	 * 
	 * LabelIconUriSimple barIcon = new
	 * LabelIconUriSimple("Bar","http://hust.se.vtio.owl#Bar",
	 * "http://icompanion.vn/resources/icon/bar.png", R.drawable.bar);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Bar", barIcon);
	 * 
	 * LabelIconUriSimple buildingIcon = new
	 * LabelIconUriSimple("Building","http://hust.se.vtio.owl#Building",
	 * "http://icompanion.vn/resources/icon/building.png", R.drawable.building);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Building", buildingIcon);
	 * 
	 * LabelIconUriSimple campingIcon = new
	 * LabelIconUriSimple("Camping Area","http://hust.se.vtio.owl#Camping-Area",
	 * "http://icompanion.vn/resources/icon/camping.png", R.drawable.camping);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Camping-Area", campingIcon);
	 * 
	 * LabelIconUriSimple churchIcon = new
	 * LabelIconUriSimple("Church","http://hust.se.vtio.owl#Church",
	 * "http://icompanion.vn/resources/icon/church.png", R.drawable.church);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Church",churchIcon);
	 * 
	 * LabelIconUriSimple cinemaIcon = new
	 * LabelIconUriSimple("Cinema","http://hust.se.vtio.owl#Cinema",
	 * "http://icompanion.vn/resources/icon/cinema.png", R.drawable.cinema);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Cinema",cinemaIcon);
	 * 
	 * LabelIconUriSimple clinicIcon = new
	 * LabelIconUriSimple("Clinic","http://hust.se.vtio.owl#Clinic",
	 * "http://icompanion.vn/resources/icon/clinic.png", R.drawable.clinic);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Clinic", clinicIcon);
	 * 
	 * LabelIconUriSimple coffeeIcon = new
	 * LabelIconUriSimple("Coffee","http://hust.se.vtio.owl#Coffee",
	 * "http://icompanion.vn/resources/icon/cafe.png", R.drawable.cafe);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Coffee", coffeeIcon);
	 * 
	 * LabelIconUriSimple commercialCenterIcon = new
	 * LabelIconUriSimple("Commercial Center"
	 * ,"http://hust.se.vtio.owl#Commercial-Center",
	 * "http://icompanion.vn/resources/icon/commercial_resource.png",
	 * R.drawable.commercial_resource);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Commercial-Center",
	 * commercialCenterIcon);
	 * 
	 * LabelIconUriSimple commercialResourceIcon = new
	 * LabelIconUriSimple("Commercial Resource"
	 * ,"http://hust.se.vtio.owl#Commercial-Resource",
	 * "http://icompanion.vn/resources/icon/commercial_resource.png",
	 * R.drawable.commercial_resource);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Commercial-Resource",
	 * commercialResourceIcon);
	 * 
	 * LabelIconUriSimple cuisineIcon = new
	 * LabelIconUriSimple("Cuisine","http://hust.se.vtio.owl#Cuisine",
	 * "http://icompanion.vn/resources/icon/cuisine.png", R.drawable.cuisine);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Cuisine",cuisineIcon);
	 * 
	 * LabelIconUriSimple cultureArtIcon = new
	 * LabelIconUriSimple("Culture Art","http://hust.se.vtio.owl#Culture-Art",
	 * "http://icompanion.vn/resources/icon/culture_art.png",
	 * R.drawable.culture_art);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Culture-Art",cultureArtIcon);
	 * 
	 * LabelIconUriSimple diningServiceIcon = new
	 * LabelIconUriSimple("Dining Service"
	 * ,"http://hust.se.vtio.owl#Dining-Service",
	 * "http://icompanion.vn/resources/icon/dining_service.png",
	 * R.drawable.dining_service);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Dining-Service"
	 * ,diningServiceIcon);
	 * 
	 * LabelIconUriSimple entertainmentIcon = new
	 * LabelIconUriSimple("Entertainment"
	 * ,"http://hust.se.vtio.owl#Entertainment",
	 * "http://icompanion.vn/resources/icon/entertainment.png",
	 * R.drawable.entertainment);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Entertainment",entertainmentIcon);
	 * 
	 * LabelIconUriSimple fashionShopIcon = new
	 * LabelIconUriSimple("Fashion Shop","http://hust.se.vtio.owl#Fashion-Shop",
	 * "http://icompanion.vn/resources/icon/fashion_shop.png",
	 * R.drawable.fashion_shop);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Fashion-Shop",fashionShopIcon);
	 * 
	 * LabelIconUriSimple fastFoodIcon = new
	 * LabelIconUriSimple("Fast Food","http://hust.se.vtio.owl#Fast-Food",
	 * "http://icompanion.vn/resources/icon/fastfood.png", R.drawable.fastfood);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Fast-Food",fastFoodIcon);
	 * 
	 * LabelIconUriSimple galleryIcon = new
	 * LabelIconUriSimple("Gallery","http://hust.se.vtio.owl#Gallery",
	 * "http://icompanion.vn/resources/icon/gallery.png", R.drawable.gallery);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Gallery",galleryIcon);
	 * 
	 * LabelIconUriSimple gardenIcon = new
	 * LabelIconUriSimple("Garden","http://hust.se.vtio.owl#Garden",
	 * "http://icompanion.vn/resources/icon/garden.png", R.drawable.garden);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Garden",gardenIcon);
	 * 
	 * LabelIconUriSimple greenZoneIcon = new
	 * LabelIconUriSimple("Green Zone","http://hust.se.vtio.owl#Green-Zone",
	 * "http://icompanion.vn/resources/icon/green_zone.png",
	 * R.drawable.green_zone);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Green-Zone",greenZoneIcon);
	 * 
	 * LabelIconUriSimple healthServiceIcon = new
	 * LabelIconUriSimple("Health Service"
	 * ,"http://hust.se.vtio.owl#Health-Service",
	 * "http://icompanion.vn/resources/icon/health_service.png",
	 * R.drawable.health_service);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Health-Service"
	 * ,healthServiceIcon);
	 * 
	 * LabelIconUriSimple historyIcon= new
	 * LabelIconUriSimple("History","http://hust.se.vtio.owl#History",
	 * "http://icompanion.vn/resources/icon/history.png", R.drawable.history);
	 * uriOfIcon.put("http://hust.se.vtio.owl#History",historyIcon);
	 * 
	 * LabelIconUriSimple hospitalIcon= new
	 * LabelIconUriSimple("Hospital","http://hust.se.vtio.owl#Hospital",
	 * "http://icompanion.vn/resources/icon/hospital.png", R.drawable.hospital);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Hospital",hospitalIcon);
	 * 
	 * LabelIconUriSimple hostelIcon= new
	 * LabelIconUriSimple("Hostel","http://hust.se.vtio.owl#Hostel",
	 * "http://icompanion.vn/resources/icon/hostel.png", R.drawable.hostel);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Hostel",hostelIcon);
	 * 
	 * LabelIconUriSimple hotelIcon= new
	 * LabelIconUriSimple("Hotel","http://hust.se.vtio.owl#Hotel",
	 * "http://icompanion.vn/resources/icon/hotel.png", R.drawable.hotel);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Hotel",hotelIcon);
	 * 
	 * LabelIconUriSimple guessHouseIcon= new
	 * LabelIconUriSimple("Guess house","http://hust.se.vtio.owl#Guest-House",
	 * "http://icompanion.vn/resources/icon/guest_house.png",
	 * R.drawable.guest_house);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Guest-House",guessHouseIcon);
	 * 
	 * LabelIconUriSimple iceCreamShopIcon= new
	 * LabelIconUriSimple("Ice cream shop"
	 * ,"http://hust.se.vtio.owl#Ice-Cream-Shop",
	 * "http://icompanion.vn/resources/icon/ice_cream_shop.png",
	 * R.drawable.ice_cream_shop);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Ice-Cream-Shop",iceCreamShopIcon);
	 * 
	 * LabelIconUriSimple kfcIcon= new
	 * LabelIconUriSimple("KFC","http://hust.se.vtio.owl#KFC",
	 * "http://icompanion.vn/resources/icon/kfc.png", R.drawable.kfc);
	 * uriOfIcon.put("http://hust.se.vtio.owl#KFC",kfcIcon);
	 * 
	 * LabelIconUriSimple lakeIcon= new
	 * LabelIconUriSimple("Lake","http://hust.se.vtio.owl#Lake",
	 * "http://icompanion.vn/resources/icon/lake.png", R.drawable.lake);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Lake",lakeIcon);
	 * 
	 * 
	 * LabelIconUriSimple lotteriaIcon= new
	 * LabelIconUriSimple("Lotteria","http://hust.se.vtio.owl#Lotteria",
	 * "http://icompanion.vn/resources/icon/lotteria.png", R.drawable.lotteria);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Lotteria",lotteriaIcon);
	 * 
	 * LabelIconUriSimple luxIcon= new
	 * LabelIconUriSimple("Luxurious Restaurant",
	 * "http://hust.se.vtio.owl#Luxurious-Restaurant",
	 * "http://icompanion.vn/resources/icon/luxurious_restaurant.png",
	 * R.drawable.luxurious_restaurant);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Luxurious-Restaurant",luxIcon);
	 * 
	 * LabelIconUriSimple marketIcon= new
	 * LabelIconUriSimple("Market","http://hust.se.vtio.owl#Market",
	 * "http://icompanion.vn/resources/icon/market.png", R.drawable.market);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Market",marketIcon);
	 * 
	 * LabelIconUriSimple museumIcon= new
	 * LabelIconUriSimple("Museum","http://hust.se.vtio.owl#Museum",
	 * "http://icompanion.vn/resources/icon/museum.png", R.drawable.museum);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Museum",museumIcon);
	 * 
	 * LabelIconUriSimple pagodaIcon= new
	 * LabelIconUriSimple("Pagoda","http://hust.se.vtio.owl#Pagoda",
	 * "http://icompanion.vn/resources/icon/pagoda.png", R.drawable.pagoda);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Pagoda",pagodaIcon);
	 * 
	 * LabelIconUriSimple parkIcon= new
	 * LabelIconUriSimple("Park","http://hust.se.vtio.owl#Park",
	 * "http://icompanion.vn/resources/icon/park.png", R.drawable.park);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Park",parkIcon);
	 * 
	 * LabelIconUriSimple pharmacyIcon= new
	 * LabelIconUriSimple("Pharmacies","http://hust.se.vtio.owl#Pharmacies",
	 * "http://icompanion.vn/resources/icon/pharmacy.png", R.drawable.pharmacy);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Pharmacies",pharmacyIcon);
	 * 
	 * 
	 * LabelIconUriSimple placeIcon= new
	 * LabelIconUriSimple("Place","http://hust.se.vtio.owl#Place",
	 * "http://icompanion.vn/resources/icon/place.png", R.drawable.place);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Place",placeIcon);
	 * 
	 * LabelIconUriSimple plazaIcon= new
	 * LabelIconUriSimple("Plaza","http://hust.se.vtio.owl#Plaza",
	 * "http://icompanion.vn/resources/icon/plaza.png", R.drawable.plaza);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Plaza",plazaIcon);
	 * 
	 * LabelIconUriSimple popularRestaurantIcon= new
	 * LabelIconUriSimple("Popular Restaurant"
	 * ,"http://hust.se.vtio.owl#Popular-Restaurant",
	 * "http://icompanion.vn/resources/icon/popular_restaurant.png",
	 * R.drawable.popular_restaurant);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Popular-Restaurant",
	 * popularRestaurantIcon);
	 * 
	 * LabelIconUriSimple religionIcon= new
	 * LabelIconUriSimple("Religion","http://hust.se.vtio.owl#Religion",
	 * "http://icompanion.vn/resources/icon/religion.png", R.drawable.religion);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Religion",religionIcon);
	 * 
	 * LabelIconUriSimple restaurantIcon= new
	 * LabelIconUriSimple("Restaurant","http://hust.se.vtio.owl#Restaurant",
	 * "http://icompanion.vn/resources/icon/restaurant.png",
	 * R.drawable.restaurant);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Restaurant",restaurantIcon);
	 * 
	 * LabelIconUriSimple riverIcon= new
	 * LabelIconUriSimple("River","http://hust.se.vtio.owl#River",
	 * "http://icompanion.vn/resources/icon/river.png", R.drawable.river);
	 * uriOfIcon.put("http://hust.se.vtio.owl#River",riverIcon);
	 * 
	 * LabelIconUriSimple shopIcon= new
	 * LabelIconUriSimple("Shop","http://hust.se.vtio.owl#Shop",
	 * "http://icompanion.vn/resources/icon/shop.png", R.drawable.shop);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Shop",shopIcon);
	 * 
	 * LabelIconUriSimple side_walk_foodIcon= new
	 * LabelIconUriSimple("Sidewalk Food"
	 * ,"http://hust.se.vtio.owl#Sidewalk-Food",
	 * "http://icompanion.vn/resources/icon/side_walk_food.png",
	 * R.drawable.side_walk_food);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Sidewalk-Food"
	 * ,side_walk_foodIcon);
	 * 
	 * LabelIconUriSimple squareIcon= new
	 * LabelIconUriSimple("Square","http://hust.se.vtio.owl#Square",
	 * "http://icompanion.vn/resources/icon/square.png", R.drawable.square);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Square",squareIcon);
	 * 
	 * LabelIconUriSimple supermarketIcon= new
	 * LabelIconUriSimple("Super-Market","http://hust.se.vtio.owl#Super-Market",
	 * "http://icompanion.vn/resources/icon/supermarket.png",
	 * R.drawable.supermarket);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Super-Market",supermarketIcon);
	 * 
	 * LabelIconUriSimple templeIcon= new
	 * LabelIconUriSimple("Temple","http://hust.se.vtio.owl#Temple",
	 * "http://icompanion.vn/resources/icon/temple.png", R.drawable.temple);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Temple",templeIcon);
	 * 
	 * LabelIconUriSimple theaterIcon= new
	 * LabelIconUriSimple("Theater","http://hust.se.vtio.owl#Theater",
	 * "http://icompanion.vn/resources/icon/theater.png", R.drawable.theater);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Theater",theaterIcon);
	 * 
	 * LabelIconUriSimple tourist_resourceIcon= new
	 * LabelIconUriSimple("Tourist-Resource"
	 * ,"http://hust.se.vtio.owl#Tourist-Resource",
	 * "http://icompanion.vn/resources/icon/tourist_resource.png",
	 * R.drawable.tourist_resource);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Tourist-Resource"
	 * ,tourist_resourceIcon );
	 * 
	 * LabelIconUriSimple travel_agentIcon= new
	 * LabelIconUriSimple("Travel Agent","http://hust.se.vtio.owl#Travel-Agent",
	 * "http://icompanion.vn/resources/icon/travel_agent.png",
	 * R.drawable.travel_agent);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Travel-Agent",travel_agentIcon);
	 * 
	 * LabelIconUriSimple zooIcon= new
	 * LabelIconUriSimple("Zoo","http://hust.se.vtio.owl#Zoo",
	 * "http://icompanion.vn/resources/icon/zoo.png", R.drawable.zoo);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Zoo",zooIcon);
	 * 
	 * LabelIconUriSimple accessories_shopIcon= new
	 * LabelIconUriSimple("Accesssory shop","http://hust.se.vtio.owl#accessory",
	 * "http://icompanion.vn/resources/icon/accessories_shop.png",
	 * R.drawable.accessories_shop);
	 * uriOfIcon.put("http://hust.se.vtio.owl#accessory",accessories_shopIcon);
	 * 
	 * LabelIconUriSimple shoeIcon= new
	 * LabelIconUriSimple("Shoes","http://hust.se.vtio.owl#shoes",
	 * "http://icompanion.vn/resources/icon/shoe.png", R.drawable.shoe);
	 * uriOfIcon.put("http://hust.se.vtio.owl#shoes",shoeIcon);
	 * 
	 * LabelIconUriSimple universityIcon= new
	 * LabelIconUriSimple("University","http://hust.se.vtio.owl#University",
	 * "http://icompanion.vn/resources/icon/university.png",
	 * R.drawable.university);
	 * uriOfIcon.put("http://hust.se.vtio.owl#University",universityIcon);
	 * 
	 * LabelIconUriSimple vehicleIcon= new LabelIconUriSimple("Vehicle Service",
	 * "http://hust.se.vtio.owl#Vehicle-Service",
	 * "http://icompanion.vn/resources/icon/vehicle.png", R.drawable.vehicle);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Vehicle-Service",vehicleIcon);
	 * 
	 * LabelIconUriSimple filling_stationIcon= new
	 * LabelIconUriSimple("Filling Station"
	 * ,"http://hust.se.vtio.owl#Filling-Station",
	 * "http://icompanion.vn/resources/icon/filling_station.png",
	 * R.drawable.filling_station);
	 * uriOfIcon.put("http://hust.se.vtio.owl#Filling-Station"
	 * ,filling_stationIcon );
	 * 
	 * }
	 */

	public static void clearCache() {
		objectPropertySize = new HashMap<String, Integer>();
		dataPropertySize = new HashMap<String, Integer>();
		allClassSize = new HashMap<String, Integer>();
		allInstanceSize = new HashMap<String, Integer>();
		objectPropertySize.put(ServerConfig.LANGUAGE_CODE, 0);
		dataPropertySize.put(ServerConfig.LANGUAGE_CODE, 0);
		allInstanceSize.put(ServerConfig.LANGUAGE_CODE, 0);
		allClassSize.put(ServerConfig.LANGUAGE_CODE, 0);

		listClassData = new HashMap<String, ClassData>();
		listAllSimpleClass = new HashMap<String, ArrayList<InstanceDataSimple>>();
		listSubject = new HashMap<String, ArrayList<InstanceDataSimple>>();
		listPredicate = new HashMap<String, ArrayList<InstanceDataSimple>>();
		listObject = new HashMap<String, ArrayList<InstanceDataSimple>>();
		propertiesOfClass = new HashMap<String, ArrayList<PropertyDataSimple>>();
		rangesOfProperty = new HashMap<String, ArrayList<ClassDataSimple>>();
		instanceOfClass = new HashMap<String, ArrayList<InstanceDataSimple>>();
		listQuery = new HashMap<String, ArrayList<ArrayList<String>>>();
		/*
		 * if (hashMapLabelIcon != null) { hashMapLabelIcon.clear(); }
		 * hashMapLabelIcon = new HashMap<String, LabelIconSimple>();
		 */

		if (uriOfIcon != null) {
			uriOfIcon.clear();
		}
		hashMapLabelToURI = new HashMap<String, String>();
		listQueryWithGeo = new HashMap<String, ArrayList<ArrayList<String>>>();
		preferUser = new ArrayList<ClassDataSimple>();
		classWithParent = new ArrayList<ArrayList<String>>();
		if (rootTree != null) {
			rootTree = null;
			rootTree = new NodeTreeDataSimple();
		}
		if (OntologyCache.listAllSimpleClass.get(ServerConfig.LANGUAGE_CODE) == null) {
			OntologyCache.listAllSimpleClass.put(ServerConfig.LANGUAGE_CODE,
					new ArrayList<InstanceDataSimple>());
		}
		if (OntologyCache.listSubject.get(ServerConfig.LANGUAGE_CODE) == null) {
			OntologyCache.listSubject.put(ServerConfig.LANGUAGE_CODE,
					new ArrayList<InstanceDataSimple>());
		}
		if (OntologyCache.listPredicate.get(ServerConfig.LANGUAGE_CODE) == null) {
			OntologyCache.listPredicate.put(ServerConfig.LANGUAGE_CODE,
					new ArrayList<InstanceDataSimple>());
		}
		if (OntologyCache.listObject.get(ServerConfig.LANGUAGE_CODE) == null) {
			OntologyCache.listObject.put(ServerConfig.LANGUAGE_CODE,
					new ArrayList<InstanceDataSimple>());
		}
	}
}
