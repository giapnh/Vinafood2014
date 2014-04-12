package hust.hgbk.vtio.vinafood.vtioservice;

public interface ICoreService {
	public FullDataInstance[] getFullDataInstaceWithPreference(String classUri,
			double geoLat, double geoLon, float radius, boolean hasPreference,
			String keyWord, String[] preferences, int limit, int offset,
			String languageCode, String key);

	public FullDataInstance[] getFullDataInstanceWithPresAndRanking(
			String classUri, double geoLat, double geoLon, Float radius,
			Boolean hasPreference, String keyWord, String[] preferences,
			Integer limit, Integer offset, String languageCode, String key);

	public String[][] executeQuery(String query, Boolean reason, String key);

	public FullDataInstance[] getFullDataInstace(String queryUri, String lang,
			Boolean reason, String key);

	public DetailProperty[] getInstanceDataWithLabelObject(String uri,
			String lang, String key);

	public FullDataInstance[] getBankResourceDataBySimpleKeyword(
			String classUri, String cityUri, String keyWord, int limit,
			int offset, String languageCode, String key);

	public FullDataInstance[] getVehicleServiceDataBySimpleKeyword(
			String classUri, String cityUri, String keyWord, int limit,
			int offset, String languageCode, String key);

	public FullDataInstance[] getDiningServiceDataBySimpleKeyword(
			String classUri, String cityUri, String keyWord, Integer limit,
			Integer offset, String languageCode, String key);

	public FullDataInstance[] getTouristResourceDataBySimpleKeyword(
			String classUri, String cityUri, String keyWord, Integer limit,
			Integer offset, String languageCode, String key);

	public FullDataInstance[] getAccommodationDataBySimpleKeyword(
			String classUri, String cityUri, String keyWord, Integer limit,
			Integer offset, String languageCode, String key);

	public FullDataInstance[] getHealthServiceDataBySimpleKeyword(
			String classUri, String cityUri, String keyWord, Integer limit,
			Integer offset, String languageCode, String key);

	public FullDataInstance[] getEntertainmentServiceDataBySimpleKeyword(
			String classUri, String cityUri, String keyWord, Integer limit,
			Integer offset, String languageCode, String key);

	public FullDataInstance[] getBankResourceDataWithoutDistance(
			String classUri, String cityUri, String bankUri,
			String locationKeyWord, Integer limit, Integer offset,
			String languageCode, String key);

	public FullDataInstance[] getBankResourceDataWithDistance(String classUri,
			String cityUri, String bankUri, String locationKeyWord,
			Double geoLat, Double geoLon, Float radius, Integer limit,
			Integer offset, String languageCode, String key);

	public FullDataInstance[] getVehicleServiceDataWithoutDistance(
			String classUri, String cityUri, String companyUri,
			String nameKeyWord, String locationKeyWord, Integer limit,
			Integer offset, String languageCode, String key);

	public FullDataInstance[] getVehicleServiceDataWithDistance(
			String classUri, String cityUri, String companyUri,
			String nameKeyWord, String locationKeyWord, Double geoLat,
			Double geoLon, Float radius, Integer limit, Integer offset,
			String languageCode, String key);

	public FullDataInstance[] getAttractionOfCity(String cityUri,
			Integer limit, Integer offset, String languageCode, String key);

	public FullDataInstance[] getDiningServiceDataWithoutDistance(
			String classUri, String cityUri, String nameKeyWord,
			String locationKeyWord, String nearByKeyWord, String styleKeyWord,
			Integer numberStar, Float numberRanking, Boolean isWellKnow,
			Integer limit, Integer offset, String languageCode, String key);

	public FullDataInstance[] getDiningServiceDataWithDistance(String classUri,
			String cityUri, String nameKeyWord, String locationKeyWord,
			String nearByKeyWord, String styleKeyWord, Integer numberStar,
			Float numberRanking, Boolean isWellKnown, Double geoLat,
			Double geoLon, Float radius, Integer limit, Integer offset,
			String languageCode, String key);

	public FullDataInstance[] getTouristResourceDataWithoutDistance(
			String classUri, String cityUri, String nameKeyWord,
			String locationKeyWord, String nearByKeyWord, Float numberRanking,
			Boolean isWellKnown, Integer limit, Integer offset,
			String languageCode, String key);

	public FullDataInstance[] getTouristResourceDataWithDistance(
			String classUri, String cityUri, String nameKeyWord,
			String locationKeyWord, String nearByKeyWord, Float numberRanking,
			Boolean isWellKnown, Double geoLat, Double geoLon, Float radius,
			Integer limit, Integer offset, String languageCode, String key);

	public FullDataInstance[] getAccommodationDataWithoutDistance(
			String classUri, String cityUri, String nameKeyWord,
			String locationKeyWord, String nearByKeyWord, Integer numberStar,
			Float numberRanking, Boolean isWellKnown, Integer limit,
			Integer offset, String languageCode, String key);

	public FullDataInstance[] getAccommodationDataWithDistance(String classUri,
			String cityUri, String nameKeyWord, String locationKeyWord,
			String nearByKeyWord, Integer numberStar, Float numberRanking,
			Boolean isWellKnown, Double geoLat, Double geoLon, Float radius,
			Integer limit, Integer offset, String languageCode, String key);

	public FullDataInstance[] getHealthServiceDataWithoutDistance(
			String classUri, String cityUri, String nameKeyWord,
			String locationKeyWord, String nearByKeyWord, Float numberRanking,
			Integer limit, Integer offset, String languageCode, String key);

	public FullDataInstance[] getHealthServiceDataWithDistance(String classUri,
			String cityUri, String nameKeyWord, String locationKeyWord,
			String nearByKeyWord, Float numberRanking, Double geoLat,
			Double geoLon, Float radius, Integer limit, Integer offset,
			String languageCode, String key);

	public FullDataInstance[] getEntertainmentServiceDataWithoutDistance(
			String classUri, String cityUri, String nameKeyWord,
			String locationKeyWord, String nearByKeyWord, Float numberRanking,
			Boolean isWellKnown, Integer limit, Integer offset,
			String languageCode, String key);

	public FullDataInstance[] getEntertainmentServiceDataWithDistance(
			String classUri, String cityUri, String nameKeyWord,
			String locationKeyWord, String nearByKeyWord, Float numberRanking,
			Boolean isWellKnown, Double geoLat, Double geoLon, Float radius,
			Integer limit, Integer offset, String languageCode, String key);
}
