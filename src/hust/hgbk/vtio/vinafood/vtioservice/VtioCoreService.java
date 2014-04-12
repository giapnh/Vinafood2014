package hust.hgbk.vtio.vinafood.vtioservice;

import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.config.log;
import hust.hgbk.vtio.vinafood.constant.LanguageCode;
import hust.hgbk.vtio.vinafood.constant.MethodName;
import hust.hgbk.vtio.vinafood.constant.NameSpace;
import hust.hgbk.vtio.vinafood.constant.OntologyCache;
import hust.hgbk.vtio.vinafood.constant.ResourceType;
import hust.hgbk.vtio.vinafood.ontology.InstanceData;
import hust.hgbk.vtio.vinafood.ontology.PropertyData;
import hust.hgbk.vtio.vinafood.ontology.PropertyWithValue;
import hust.hgbk.vtio.vinafood.ontology.simple.ClassDataSimple;
import hust.hgbk.vtio.vinafood.ontology.simple.InstanceDataSimple;
import hust.hgbk.vtio.vinafood.ontology.simple.NodeTreeDataSimple;
import hust.hgbk.vtio.vinafood.ontology.simple.PlaceDataSimple;
import hust.hgbk.vtio.vinafood.ontology.simple.PropertyDataSimple;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.util.Log;

public class VtioCoreService {
	public int countCategoryCall = 0;

	public VtioCoreService() {

	}

	public ArrayList<ClassDataSimple> getRangeSimpleOfObjectProperty(
			String propertyURI) {
		ArrayList<ClassDataSimple> returnValues = new ArrayList<ClassDataSimple>();
		// String queryString =
		// "select distinct ?uri ?label where {<"+propertyURI+"> rdfs:range ?uri. ?uri rdfs:label ?label. FILTER(lang(?label)='"+ServerConfig.LANGUAGE_CODE+"')} ORDER BY ASC(?label)";
		// dungct: sua de adapted voi ontology co nhan tieng viet khong dau
		String queryString = "select distinct ?uri ?label where {<"
				+ propertyURI
				+ "> rdfs:range ?uri. {SELECT ?label WHERE {?uri rdfs:label ?label. FILTER(lang(?label)='"
				+ ServerConfig.LANGUAGE_CODE
				+ "')} LIMIT 1 }} ORDER BY ASC(?label)";
		ArrayList<ArrayList<String>> results = executeQuery(queryString, false);
		for (int i = 0; i < results.size(); i++) {
			/**
			 * Class truc tiep
			 */
			ClassDataSimple classData = new ClassDataSimple();
			ArrayList<String> result = results.get(i);
			classData.setUri(result.get(0));
			classData.setLabel(result.get(1)
					.replace("^^" + NameSpace.xsd + "string", "")
					.replace("@" + ServerConfig.LANGUAGE_CODE, ""));
			returnValues.add(classData);
			/**
			 * Class gian tiep (Sub class)
			 */
			ArrayList<ClassDataSimple> arraySubclass = getSubClassSimpleOfClass(classData
					.getUri());
			returnValues.addAll(arraySubclass);
		}
		return returnValues;
	}

	/**
	 * Lay ra cac lop con cua mot lop
	 * 
	 * @param uri
	 * @return
	 */
	private ArrayList<ClassDataSimple> getSubClassSimpleOfClass(String uri) {
		ArrayList<ClassDataSimple> returnValues = new ArrayList<ClassDataSimple>();
		// String query =
		// "SELECT distinct ?subClass ?label where {?subClass rdfs:subClassOf <"+uri+">. ?subClass rdfs:label ?label. FILTER(lang(?label)='"+ServerConfig.LANGUAGE_CODE+"')} ORDER BY ASC(?label)";
		// dungct: sua de adapted voi ontology co nhan tieng viet khong dau
		String query = "SELECT distinct ?subClass ?label where {?subClass rdfs:subClassOf <"
				+ uri
				+ ">. {SELECT ?label WHERE {?subClass rdfs:label ?label. FILTER(lang(?label)='"
				+ ServerConfig.LANGUAGE_CODE
				+ "')} LIMIT 1 }} ORDER BY ASC(?label)";

		ArrayList<ArrayList<String>> results = executeQuery(query, true);
		for (int i = 0; i < results.size(); i++) {
			String classUri = results.get(i).get(0);
			String label = results.get(i).get(1)
					.replace("^^" + NameSpace.xsd + "string", "")
					.replace("@" + ServerConfig.LANGUAGE_CODE, "");
			ClassDataSimple classDataSimple = new ClassDataSimple();
			classDataSimple.setUri(classUri);
			classDataSimple.setLabel(label);
			returnValues.add(classDataSimple);
		}
		return returnValues;
	}

	/**
	 * lay ra cÃƒÂ¡c instance trÃ¡Â»Â±c tiÃ¡ÂºÂ¿p cua mot lop
	 * 
	 * @param classUri
	 * @return
	 */
	public ArrayList<InstanceDataSimple> getInstanceSimpleOfClass(
			String classUri) {
		ArrayList<InstanceDataSimple> returnValues = new ArrayList<InstanceDataSimple>();

		String queryString = "select distinct ?uri ?label where {?uri rdf:type <"
				+ classUri
				+ ">. ?uri rdfs:label ?label. FILTER(lang(?label)='"
				+ ServerConfig.LANGUAGE_CODE + "')} ";

		ArrayList<ArrayList<String>> results = executeQuery(queryString, true);
		for (int i = 0; i < results.size(); i++) {
			InstanceDataSimple instanceData = new InstanceDataSimple();
			ArrayList<String> result = results.get(i);
			instanceData.setURI(result.get(0));
			instanceData.setLabel(result.get(1)
					.replace("^^" + NameSpace.xsd + "string", "")
					.replace("@" + ServerConfig.LANGUAGE_CODE, ""));
			returnValues.add(instanceData);
		}
		return returnValues;
	}

	public ArrayList<InstanceDataSimple> getInstanceSimpleOfClassWithCity(
			String classUri) {
		ArrayList<InstanceDataSimple> returnValues = new ArrayList<InstanceDataSimple>();

		String queryString = "select distinct ?uri ?label where {"
				+ "?uri rdf:type <"
				+ classUri
				+ ">. "
				+ "?uri rdfs:label ?label. FILTER(lang(?label)='"
				+ ServerConfig.LANGUAGE_CODE
				+ "')"
				+ " ?uri"
				+ " vtio:hasLocation ?addresscity .   ?addresscity vtio:isPartOf <"
				+ ServerConfig.currentCityUri + ">.}";

		ArrayList<ArrayList<String>> results = executeQuery(queryString, true);
		for (int i = 0; i < results.size(); i++) {
			InstanceDataSimple instanceData = new InstanceDataSimple();
			ArrayList<String> result = results.get(i);
			instanceData.setURI(result.get(0));
			instanceData.setLabel(result.get(1)
					.replace("^^" + NameSpace.xsd + "string", "")
					.replace("@" + ServerConfig.LANGUAGE_CODE, ""));
			returnValues.add(instanceData);
		}
		return returnValues;
	}

	public ArrayList<InstanceDataSimple> getInstanceSimpleOfClassWithCity(
			String classUri, String property) {
		ArrayList<InstanceDataSimple> returnValues = new ArrayList<InstanceDataSimple>();

		String queryString = "select distinct ?uri ?label where "
				+ "{?uri rdf:type <" + classUri
				+ ">. ?uri rdfs:label ?label. FILTER(lang(?label)='"
				+ ServerConfig.LANGUAGE_CODE + "')" + " ?object <" + property
				+ ">  ?uri." + " {?object vtio:isPartOf ?location."
				+ " } UNION {" + " ?object vtio:hasLocation ?location.}"
				+ " ?location vtio:isPartOf <" + ServerConfig.currentCityUri
				+ ">.}";

		ArrayList<ArrayList<String>> results = executeQuery(queryString, true);
		for (int i = 0; i < results.size(); i++) {
			InstanceDataSimple instanceData = new InstanceDataSimple();
			ArrayList<String> result = results.get(i);
			instanceData.setURI(result.get(0));
			instanceData.setLabel(result.get(1)
					.replace("^^" + NameSpace.xsd + "string", "")
					.replace("@" + ServerConfig.LANGUAGE_CODE, ""));
			returnValues.add(instanceData);
		}
		return returnValues;
	}

	/**
	 * Lay tat ca cac lop la range cua mot lop
	 * 
	 * @param propertyURI
	 * @return
	 */
	// public ArrayList<ClassData> getRangeOfObjectProperty(String propertyURI){
	// ArrayList<ClassData> returnValues = new ArrayList<ClassData>();
	// String SOAP_ACTION = ServerConfig.NAMESPACE +
	// MethodName.GET_RANGE_OF_OBJECT_PROPERTY;
	//
	// SoapObject soapRequestObject = new SoapObject(ServerConfig.NAMESPACE,
	// MethodName.GET_RANGE_OF_OBJECT_PROPERTY);
	// soapRequestObject.addProperty("arg0", propertyURI);
	//
	// SoapSerializationEnvelope envelope = new
	// SoapSerializationEnvelope(SoapEnvelope.VER11);
	// envelope.setOutputSoapObject(soapRequestObject);
	//
	// HttpTransportSE androidHttpTransport = new
	// HttpTransportSE(ServerConfig.URL);
	// try {
	// androidHttpTransport.call(SOAP_ACTION, envelope);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (XmlPullParserException e) {
	// e.printStackTrace();
	// }
	// try {
	// SoapObject result = (SoapObject)envelope.bodyIn;
	// for (int i=0; i< result.getPropertyCount(); i++){
	// ClassData classItem = new ClassData();
	// SoapObject soapItem = (SoapObject) result.getProperty(i);
	// String label = soapItem.getProperty("classLabel").toString();
	// String uri = soapItem.getProperty("classURI").toString();
	// String hasSubClassString =
	// soapItem.getProperty("hasSubClass").toString();
	// String hasSuperClassString =
	// soapItem.getProperty("hasSuperClass").toString();
	// classItem.setClassLabel(label);
	// classItem.setClassURI(uri);
	// if (hasSubClassString.equals("true")){
	// classItem.setHasSubClass(true);
	// } else classItem.setHasSubClass(false);
	// if (hasSuperClassString.equals("true")){
	// classItem.setHasSuperClass(true);
	// } else classItem.setHasSuperClass(false);
	// returnValues.add(classItem);
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	//
	// return returnValues;
	// }

	/**
	 * Lay kieu du lieu cua mot property
	 * 
	 * @param propertyURI
	 * @return
	 */

	public String getTypeOfDataProperty(String propertyURI) {
		String returnValues = null;
		String SOAP_ACTION = ServerConfig.SERVICE_NAMESPACE
				+ MethodName.GET_TYPE_OF_DATA_PROPERTY;

		SoapObject soapRequestObject = new SoapObject(
				ServerConfig.SERVICE_NAMESPACE,
				MethodName.GET_TYPE_OF_DATA_PROPERTY);
		soapRequestObject.addProperty("arg0", propertyURI);
		soapRequestObject.addProperty("arg1", ServerConfig.VTIO_REPOSITORY_KEY);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(soapRequestObject);

		callService(SOAP_ACTION, envelope);

		try {
			SoapObject result = (SoapObject) envelope.bodyIn;
			SoapPrimitive soapPrimitive = (SoapPrimitive) result.getProperty(0);
			returnValues = soapPrimitive.toString().replace(NameSpace.xsd, "");
		} catch (Exception e) {
			// TODO: handle exception
		}

		return returnValues;
	}

	/**
	 * Lay thong tin mot instance qua URI cua no
	 * 
	 * @param instanceURI
	 * @return
	 */
	public ArrayList<PropertyWithValue> getInstanceDataWithLabelObject(
			String instanceURI, String langCode) {
		ArrayList<PropertyWithValue> listPropertyValues = new ArrayList<PropertyWithValue>();

		String SOAP_ACTION = ServerConfig.SERVICE_NAMESPACE
				+ MethodName.GET_INSTANCE_DATA_WITH_LABEL_OBJECT;
		SoapObject soapRequestObject = new SoapObject(
				ServerConfig.SERVICE_NAMESPACE,
				MethodName.GET_INSTANCE_DATA_WITH_LABEL_OBJECT);
		soapRequestObject.addProperty("arg0", instanceURI);
		soapRequestObject.addProperty("arg1", langCode);
		soapRequestObject.addProperty("arg2", ServerConfig.VTIO_REPOSITORY_KEY);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(soapRequestObject);

		callService(SOAP_ACTION, envelope);

		try {
			SoapObject result = (SoapObject) envelope.bodyIn;

			for (int i = 0; i < result.getPropertyCount(); i++) {
				SoapObject soapItem = (SoapObject) result.getProperty(i);

				PropertyInfo propertyInfo = new PropertyInfo();
				soapItem.getPropertyInfo(0, propertyInfo);

				try {
					PropertyWithValue propertyWithValues = new PropertyWithValue();
					PropertyData property = new PropertyData();
					if (soapItem.getProperty("objectProperty").toString()
							.equals("true")) {
						property.setObjectProperty(true);
					} else
						property.setObjectProperty(false);
					property.setPropertyLabel(soapItem.getProperty(
							"propertyLabel").toString());
					property.setPropertyURI(soapItem.getProperty("propertyUri")
							.toString());
					propertyWithValues.setProperty(property);
					propertyWithValues.setValue(soapItem.getProperty("value")
							.toString());
					try {
						String label = soapItem.getProperty("valueLabel")
								.toString();
						if (label != null && !label.toString().equals("null")
								&& !label.toString().contains("anyType")) {
							propertyWithValues.setValueLabel(label.toString());
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

					listPropertyValues.add(propertyWithValues);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// Log.v("PROPERTIES", "size: " + listPropertyValues.size());
		return listPropertyValues;
	}

	public InstanceData getInstanceData(String instanceURI, String langCode) {
		InstanceData returnValues = new InstanceData();
		ArrayList<PropertyWithValue> listPropertyValues = new ArrayList<PropertyWithValue>();

		String SOAP_ACTION = ServerConfig.SERVICE_NAMESPACE
				+ MethodName.GET_INSTANCE_DATA_FROM_URI;
		SoapObject soapRequestObject = new SoapObject(
				ServerConfig.SERVICE_NAMESPACE,
				MethodName.GET_INSTANCE_DATA_FROM_URI);
		soapRequestObject.addProperty("arg0", instanceURI);
		soapRequestObject.addProperty("arg1", langCode);
		soapRequestObject.addProperty("arg2", ServerConfig.VTIO_REPOSITORY_KEY);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(soapRequestObject);

		callService(SOAP_ACTION, envelope);

		try {
			SoapObject result = (SoapObject) envelope.bodyIn;
			SoapObject soapItem = (SoapObject) result.getProperty(0);

			for (int i = 0; i < soapItem.getPropertyCount(); i++) {

				PropertyInfo propertyInfo = new PropertyInfo();
				soapItem.getPropertyInfo(i, propertyInfo);
				// Log.v("PROPERTIES", propertyInfo.getName());
				// Lay ve thong tin class cua instance
				// if (propertyInfo.getName().equals("instanceClass")){
				// ClassData classOfInstance = new ClassData();
				// SoapObject classOfInstanceSoapObject = (SoapObject)
				// soapItem.getProperty("instanceClass");
				// try {
				// classOfInstance.setClassLabel(classOfInstanceSoapObject.getProperty("classLabel").toString().replace("@"+ServerConfig.LANGUAGE_CODE,
				// ""));
				// } catch (Exception e) {
				// // TODO: handle exception
				// }
				// classOfInstance.setClassURI(classOfInstanceSoapObject.getProperty("classURI").toString());
				// if
				// (classOfInstanceSoapObject.getProperty("hasSubClass").toString().equals("true")){
				// classOfInstance.setHasSubClass(true);
				// } else classOfInstance.setHasSubClass(false);
				// if
				// (classOfInstanceSoapObject.getProperty("hasSuperClass").toString().equals("true")){
				// classOfInstance.setHasSuperClass(true);
				// } else classOfInstance.setHasSuperClass(false);
				// returnValues.setInstanceClass(classOfInstance);
				// }

				// Lay ve thong tin label cua instance
				if (propertyInfo.getName().equals("label")) {
					SoapPrimitive labelSoapPrimitive = (SoapPrimitive) soapItem
							.getProperty("label");
					returnValues
							.setInstanceLabel(labelSoapPrimitive.toString());
				}

				// Lay ve thong tin URI
				if (propertyInfo.getName().equals("uri")) {
					SoapPrimitive uriSoapPrimitive = (SoapPrimitive) soapItem
							.getProperty("uri");
					returnValues.setInstanceURI(uriSoapPrimitive.toString());
				}

				// Lay ve thong tin cac thuoc tinh
				if (propertyInfo.getName().equals("properties")) {
					try {
						PropertyWithValue propertyWithValues = new PropertyWithValue();
						SoapObject propertyWithValuesSoapObject = (SoapObject) soapItem
								.getProperty(i);
						// Lay thong tin property
						PropertyData property = new PropertyData();
						SoapObject propertySoapObject = (SoapObject) propertyWithValuesSoapObject
								.getProperty("property");
						property.setPropertyURI(propertySoapObject.getProperty(
								"uri").toString());
						property.setPropertyLabel(propertySoapObject
								.getProperty("label").toString()
								.replace("@" + ServerConfig.LANGUAGE_CODE, ""));
						if (propertySoapObject.getProperty("objectProperty")
								.toString().equals("true")) {
							property.setObjectProperty(true);
						} else
							property.setObjectProperty(false);
						propertyWithValues.setProperty(property);

						// Lay thong tin value
						propertyWithValues
								.setValue(propertyWithValuesSoapObject
										.getProperty("value").toString());
						listPropertyValues.add(propertyWithValues);

					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}

			returnValues.setListPropertyValues(listPropertyValues);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return returnValues;
	}

	/**
	 * Lay tat ca cac Instance cua mot lop
	 * 
	 * @param classURI
	 * @return
	 */
	// public ArrayList<InstanceData> getAllInstanceOfClass(String classURI){
	// ArrayList<InstanceData> returnValues = new ArrayList<InstanceData>();
	// String SOAP_ACTION = ServerConfig.NAMESPACE +
	// MethodName.GET_ALL_INSTANCE_OF_CLASS;
	// SoapObject soapRequestObject = new SoapObject(ServerConfig.NAMESPACE,
	// MethodName.GET_ALL_INSTANCE_OF_CLASS);
	// soapRequestObject.addProperty("arg0", classURI);
	//
	// SoapSerializationEnvelope envelope = new
	// SoapSerializationEnvelope(SoapEnvelope.VER11);
	// envelope.setOutputSoapObject(soapRequestObject);
	//
	// HttpTransportSE androidHttpTransport = new
	// HttpTransportSE(ServerConfig.URL);
	// try {
	// androidHttpTransport.call(SOAP_ACTION, envelope);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (XmlPullParserException e) {
	// e.printStackTrace();
	// }
	//
	// SoapObject results = (SoapObject)envelope.bodyIn;
	// for (int i=0; i<results.getPropertyCount(); i++){
	// InstanceData instanceData = new InstanceData();
	// ArrayList<PropertyWithValue> listPropertyWithValues = new
	// ArrayList<PropertyWithValue>();
	// SoapObject instanceDataSoapObject = (SoapObject) results.getProperty(i);
	//
	// for (int j=0; j<instanceDataSoapObject.getPropertyCount(); j++){
	//
	// PropertyInfo propertyInfo = new PropertyInfo();
	// instanceDataSoapObject.getPropertyInfo(j, propertyInfo);
	//
	// // Lay ve thong tin class cua instance
	// if (propertyInfo.getName().equals("instanceClass")){
	// ClassData classOfInstance = new ClassData();
	//
	// SoapObject classOfInstanceSoapObject = (SoapObject)
	// instanceDataSoapObject.getProperty("instanceClass");
	// classOfInstance.setClassLabel(classOfInstanceSoapObject.getProperty("classLabel").toString());
	// classOfInstance.setClassURI(classOfInstanceSoapObject.getProperty("classURI").toString());
	// if
	// (classOfInstanceSoapObject.getProperty("hasSubClass").toString().equals("true")){
	// classOfInstance.setHasSubClass(true);
	// } else classOfInstance.setHasSubClass(false);
	// if
	// (classOfInstanceSoapObject.getProperty("hasSuperClass").toString().equals("true")){
	// classOfInstance.setHasSuperClass(true);
	// } else classOfInstance.setHasSuperClass(false);
	// instanceData.setInstanceClass(classOfInstance);
	// }
	//
	// // Lay ve thong tin label cua instance
	// if (propertyInfo.getName().equals("instanceLabel")){
	// SoapPrimitive labelSoapPrimitive = (SoapPrimitive)
	// instanceDataSoapObject.getProperty("instanceLabel");
	// instanceData.setInstanceLabel(labelSoapPrimitive.toString());
	// }
	//
	// // Lay ve thong tin URI
	// if (propertyInfo.getName().equals("instanceURI")){
	// SoapPrimitive uriSoapPrimitive = (SoapPrimitive)
	// instanceDataSoapObject.getProperty("instanceURI");
	// instanceData.setInstanceURI(uriSoapPrimitive.toString());
	// }
	//
	// // Lay ve thong tin cac thuoc tinh
	// if (propertyInfo.getName().equals("listPropertyValues")){
	// PropertyWithValue propertyWithValues = new PropertyWithValue();
	// SoapObject propertyWithValuesSoapObject = (SoapObject)
	// instanceDataSoapObject.getProperty(j);
	// // Lay thong tin property
	// PropertyData property = new PropertyData();
	// SoapObject propertySoapObject = (SoapObject)
	// propertyWithValuesSoapObject.getProperty("property");
	// property.setPropertyURI(propertySoapObject.getProperty("propertyURI").toString());
	// property.setPropertyLabel(propertySoapObject.getProperty("propertyLabel").toString());
	// if
	// (propertySoapObject.getProperty("objectProperty").toString().equals("true")){
	// property.setObjectProperty(true);
	// } else property.setObjectProperty(false);
	// propertyWithValues.setProperty(property);
	//
	// // Lay thong tin value
	// propertyWithValues.setValue(propertyWithValuesSoapObject.getProperty("value").toString());
	//
	// listPropertyWithValues.add(propertyWithValues);
	// }
	// instanceData.setListPropertyValues(listPropertyWithValues);
	// }
	// returnValues.add(instanceData);
	// }
	// return returnValues;
	// }

	/**
	 * Lay tat ca cac lop la domain cua mot property
	 * 
	 * @param propertyURI
	 * @return
	 */
	// public ArrayList<ClassData> getDomainOfProperty(String propertyURI){
	// ArrayList<ClassData> returnValues = new ArrayList<ClassData>();
	// String SOAP_ACTION = ServerConfig.NAMESPACE +
	// MethodName.GET_DOMAIN_OF_PROPERTY;
	// SoapObject soapRequestObject = new SoapObject(ServerConfig.NAMESPACE,
	// MethodName.GET_DOMAIN_OF_PROPERTY);
	// soapRequestObject.addProperty("arg0", propertyURI);
	//
	// SoapSerializationEnvelope envelope = new
	// SoapSerializationEnvelope(SoapEnvelope.VER11);
	// envelope.setOutputSoapObject(soapRequestObject);
	//
	// HttpTransportSE androidHttpTransport = new
	// HttpTransportSE(ServerConfig.URL);
	// try {
	// androidHttpTransport.call(SOAP_ACTION, envelope);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (XmlPullParserException e) {
	// e.printStackTrace();
	// }
	// try {
	// SoapObject result = (SoapObject)envelope.bodyIn;
	// for (int i=0; i< result.getPropertyCount(); i++){
	// ClassData classItem = new ClassData();
	// SoapObject soapItem = (SoapObject) result.getProperty(i);
	// String label = soapItem.getProperty("classLabel").toString();
	// String uri = soapItem.getProperty("classURI").toString();
	// String hasSubClassString =
	// soapItem.getProperty("hasSubClass").toString();
	// String hasSuperClassString =
	// soapItem.getProperty("hasSuperClass").toString();
	// classItem.setClassLabel(label);
	// classItem.setClassURI(uri);
	// if (hasSubClassString.equals("true")){
	// classItem.setHasSubClass(true);
	// } else classItem.setHasSubClass(false);
	// if (hasSuperClassString.equals("true")){
	// classItem.setHasSuperClass(true);
	// } else classItem.setHasSuperClass(false);
	// returnValues.add(classItem);
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	//
	// return returnValues;
	// }

	/**
	 * lay ra tat ca cac lop la domain cua mot property
	 * 
	 * @param propertyURI
	 * @return
	 */
	// public ArrayList<ClassDataSimple> getDomainSimpleOfProperty(String
	// propertyURI){
	// ArrayList<ClassDataSimple> returnValues = new
	// ArrayList<ClassDataSimple>();
	// String SOAP_ACTION = ServerConfig.NAMESPACE +
	// MethodName.GET_DOMAIN_SIMPLE_OF_PROPERTY;
	// SoapObject soapRequestObject = new SoapObject(ServerConfig.NAMESPACE,
	// MethodName.GET_DOMAIN_SIMPLE_OF_PROPERTY);
	// soapRequestObject.addProperty("arg0", propertyURI);
	//
	// SoapSerializationEnvelope envelope = new
	// SoapSerializationEnvelope(SoapEnvelope.VER11);
	// envelope.setOutputSoapObject(soapRequestObject);
	//
	// HttpTransportSE androidHttpTransport = new
	// HttpTransportSE(ServerConfig.URL);
	// try {
	// androidHttpTransport.call(SOAP_ACTION, envelope);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (XmlPullParserException e) {
	// e.printStackTrace();
	// }
	// try {
	// SoapObject result = (SoapObject)envelope.bodyIn;
	// for (int i=0; i< result.getPropertyCount(); i++){
	// ClassDataSimple classItem = new ClassDataSimple();
	// SoapObject soapItem = (SoapObject) result.getProperty(i);
	// String label = soapItem.getProperty("label").toString();
	// String uri = soapItem.getProperty("url").toString();
	// classItem.setLabel(label);
	// classItem.setUri(uri);
	// returnValues.add(classItem);
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	//
	// return returnValues;
	// }

	/**
	 * Thuc thi mo cau truy van, tra ve list cac ket qua
	 * 
	 * @param queryString
	 * @return
	 */
	public ArrayList<ArrayList<String>> executeQuery(String queryString,
			boolean reason) {
		ArrayList<ArrayList<String>> returnValues = new ArrayList<ArrayList<String>>();
		/**
		 * Kiem tra trong cache
		 */
		String key = String.valueOf(reason) + queryString;

		if (OntologyCache.listQuery.size() > 0
				&& OntologyCache.listQuery.containsKey(key)) {
			if (OntologyCache.listQuery.get(key) != null
					&& OntologyCache.listQuery.get(key).size() > 0) {
				return OntologyCache.listQuery.get(key);
			}

		}

		// Log.d("QUERY-ZERO", "ZERO = "+queryString);

		String SOAP_ACTION = ServerConfig.SERVICE_NAMESPACE
				+ MethodName.EXECUTE_QUERY;
		SoapObject soapRequestObject = new SoapObject(
				ServerConfig.SERVICE_NAMESPACE, MethodName.EXECUTE_QUERY);
		soapRequestObject
				.addProperty("arg0", queryString.replaceAll("\n", " "));
		soapRequestObject.addProperty("arg1", reason);
		soapRequestObject.addProperty("arg2", ServerConfig.VTIO_REPOSITORY_KEY);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(soapRequestObject);

		callService(SOAP_ACTION, envelope);

		try {
			SoapObject result = (SoapObject) envelope.bodyIn;
			for (int i = 0; i < result.getPropertyCount(); i++) {
				SoapObject soapItem = (SoapObject) result.getProperty(i);
				ArrayList<String> resultItem = new ArrayList<String>();
				for (int j = 0; j < soapItem.getPropertyCount(); j++) {
					String value = soapItem.getProperty(j).toString();
					resultItem.add(value);
				}
				returnValues.add(resultItem);
			}
		} catch (Exception e) {
		}

		/**
		 * Ã„ï¿½Ã†Â°a vÃƒÂ o cache
		 */
		// if (returnValues.size() > 0) {
		//
		// OntologyCache.listQuery.put(key, returnValues);
		// }
		// Log.d("QUERY-ZERO", "ZERO SIZE: " + returnValues.size());
		return returnValues;
	}

	public ArrayList<ArrayList<String>> executeQuery(String queryString,
			boolean reason, boolean isCache) {
		ArrayList<ArrayList<String>> returnValues = new ArrayList<ArrayList<String>>();
		/**
		 * Kiem tra trong cache
		 */
		String key = String.valueOf(reason) + queryString;

		if (OntologyCache.listQuery.size() > 0
				&& OntologyCache.listQuery.containsKey(key)) {
			if (OntologyCache.listQuery.get(key) != null
					&& OntologyCache.listQuery.get(key).size() > 0) {
				return OntologyCache.listQuery.get(key);
			}

		}

		// Log.d("QUERY-ZERO", "ZERO = "+queryString);

		String SOAP_ACTION = ServerConfig.SERVICE_NAMESPACE
				+ MethodName.EXECUTE_QUERY;
		SoapObject soapRequestObject = new SoapObject(
				ServerConfig.SERVICE_NAMESPACE, MethodName.EXECUTE_QUERY);
		soapRequestObject
				.addProperty("arg0", queryString.replaceAll("\n", " "));
		soapRequestObject.addProperty("arg1", reason);
		soapRequestObject.addProperty("arg2", ServerConfig.VTIO_REPOSITORY_KEY);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(soapRequestObject);

		callService(SOAP_ACTION, envelope);

		try {
			SoapObject result = (SoapObject) envelope.bodyIn;
			// Log.d("QUERY-ZERO", "RESULT SERVICE SIZE: " +
			// result.getPropertyCount());
			for (int i = 0; i < result.getPropertyCount(); i++) {
				SoapObject soapItem = (SoapObject) result.getProperty(i);
				ArrayList<String> resultItem = new ArrayList<String>();
				for (int j = 0; j < soapItem.getPropertyCount(); j++) {
					String value = soapItem.getProperty(j).toString();
					resultItem.add(value);
				}
				returnValues.add(resultItem);
			}
		} catch (Exception e) {
		}
		if (returnValues.size() > 0 && isCache) {

			OntologyCache.listQuery.put(key, returnValues);
		}
		return returnValues;
	}

	/**
	 * Lay ra list URL cua Place
	 * 
	 * @param placeURL
	 * @return
	 */
	// public ArrayList<String> getMediasOfPlace(String placeURL){
	// String queryString =
	// "select ?url where {<"+placeURL+"> vtio:hasMedia ?media. ?media vtio:hasURL ?url.}";
	// // Log.d("QUERY", "QUERY GET MEDIA URL 	: "+queryString);
	// ArrayList<ArrayList<String>> results = executeQuery(queryString, false);
	// ArrayList<String> listImagesURL = new ArrayList<String>();
	// for (int i=0; i< results.size(); i++){
	// ArrayList<String> result = results.get(i);
	// for (int j=0; j<results.size(); j++){
	// listImagesURL.add(result.get(j).replace("^^"+NameSpace.xsd+"string",
	// ""));
	// }
	// }
	// // Log.d("QUERY", "RESULT 	: "+listImagesURL);
	// return listImagesURL;
	// }

	/**
	 * Lay ve thong tin co ban cua mot Place
	 * 
	 * @param placeURI
	 * @return
	 */
	public PlaceDataSimple getPlaceDataSimple(String placeURI) {
		PlaceDataSimple returnValue = new PlaceDataSimple();

		returnValue.setURI(placeURI);

		// dungct - lay them lat, long

		String query = ""
				+ "SELECT ?label ?abstract ?imageURL "
				+ "WHERE { 			 <"
				+ placeURI
				+ "> rdfs:label ?label."
				+ "		 	OPTIONAL{<"
				+ placeURI
				+ "> vtio:hasAbstract ?abstract}"
				+ "			OPTIONAL{<"
				+ placeURI
				+ "> rdf:type ?typeURI. ?typeURI rdfs:label ?type. FILTER regex(Str(?typeURI), 'vtio')} "
				+ "			OPTIONAL{<"
				+ placeURI
				+ "> vtio:hasMedia ?media. ?media rdf:type vtio:Image. ?media vtio:hasURL ?imageURL.}"
				+ "FILTER(lang(?label)='" + ServerConfig.LANGUAGE_CODE + "')"
				+ "}  LIMIT 1";

		ArrayList<ArrayList<String>> placeInfo = executeQuery(query, false);
		// Log.d("STRING QUERY ", "QUERY = "+query);
		// Log.d("RESULT QUERY ", "PLACE RESULT = "+placeInfo);

		if (placeInfo.size() > 0) {
			ArrayList<String> resultString = placeInfo.get(0);
			// Log.v("TEST", "info size: " + resultString.size());
			String label = "";
			try {
				label = resultString.get(0).replace(
						"@" + ServerConfig.LANGUAGE_CODE, "");
				// Log.v("TEST", "info size: " + label);
			} catch (Exception e) {
			}

			String type = "";
			try {
				type = resultString.get(1).replace(
						"@" + ServerConfig.LANGUAGE_CODE, "");
			} catch (Exception e) {
			}

			String location = "";
			try {
				location = resultString.get(2);
			} catch (Exception e) {
			}

			String isWellknown = "";
			try {
				isWellknown = resultString.get(3);
			} catch (Exception e) {
				// TODO: handle exception
			}

			String hasAbstract = "";
			try {
				hasAbstract = resultString.get(1);
			} catch (Exception e) {
			}

			String imageURL = "";
			try {
				imageURL = resultString.get(2);
			} catch (Exception e) {
			}

			String ratingLabel = "0";
			try {
				if (!resultString.get(6).contains("anyType")) {
					ratingLabel = resultString.get(6).replace(
							"^^" + NameSpace.xsd + "integer", "");
				}
			} catch (Exception e) {
			}
			// add by dungct
			String longValue = "";
			try {
				longValue = resultString.get(7).replace(
						"^^" + NameSpace.xsd + "double", "");
			} catch (Exception e) {

			}

			String latValue = "";
			try {
				latValue = resultString.get(8).replace(
						"^^" + NameSpace.xsd + "double", "");
			} catch (Exception e) {

			}
			// end add

			returnValue.setLabel(label.replace("^^" + NameSpace.xsd + "string",
					""));
			returnValue.setHasAbstract(hasAbstract.replace("^^" + NameSpace.xsd
					+ "string", ""));
			returnValue.setType(type.replace("^^" + NameSpace.xsd + "string",
					""));
			returnValue.setWellknown(Boolean.valueOf(isWellknown.replace("^^"
					+ NameSpace.xsd + "bpolean", "")));
			returnValue.setAddress(location);
			returnValue.setImageURL(imageURL.replace("^^" + NameSpace.xsd
					+ "string", ""));
			// add by dungct

			try {

				if (longValue.equals(""))
					returnValue.setLongtitude(0);
				else
					returnValue.setLongtitude(Double.parseDouble(longValue));
				if (latValue.equals(""))
					returnValue.setLatitude(0);
				else
					returnValue.setLatitude(Double.parseDouble(latValue));
			} catch (Exception e) {
				// Log.v("LOI", placeURI + "Lo:" + longValue + "La:"+ latValue
				// );
				returnValue.setLatitude(0);
				returnValue.setLongtitude(0);
				e.printStackTrace();
			}
			// end add
			// Log.d("IMAGES", "URL: "+returnValue.getImageURL());
			// Log.d("LONGLAT", "Long: "+returnValue.getLongtitude() +
			// " Lat:"+returnValue.getLatitude());

			returnValue.setHasRating(Integer.valueOf(ratingLabel));

		}
		return returnValue;
	}

	/**
	 * Lay ve dia chi cua mot place
	 * 
	 * @param placeURI
	 * @return
	 */
	public String getAddressOfPlace(String placeURI) {
		String returnValue = "";
		/*
		 * String query = "" +
		 * "SELECT DISTINCT ?loc1 ?loc2 ?loc3 ?loc4 ?loc5 where { " +
		 * "<"+placeURI+"> vtio:hasLocation ?x.  ?x vtio:hasValue ?loc1." +
		 * "OPTIONAL {?x vtio:isPartOf ?y. ?y vtio:hasValue ?loc2.} " +
		 * "OPTIONAL {?y vtio:isPartOf ?z. ?z vtio:hasValue ?loc3.}" +
		 * "OPTIONAL {?z vtio:isPartOf ?k. ?k vtio:hasValue ?loc4.}" +
		 * "OPTIONAL {?k vtio:isPartOf ?t. ?t vtio:hasValue ?loc5.} " + "}";
		 */
		// Dungct - Query get label of street, district and city by language
		String query = ""
				+ "SELECT DISTINCT ?loc1 ?loc2 ?loc3 ?loc4 ?loc5 where { "
				+ "<"
				+ placeURI
				+ "> vtio:hasLocation ?x. ?x vtio:isPartOf ?y. "
				+ "OPTIONAL { ?x vtio:hasValue ?loc1. } "
				+ "OPTIONAL {SELECT ?loc2 WHERE {?x vtio:isPartOf ?y. ?y rdfs:label ?loc2. FILTER(lang(?loc2)='"
				+ ServerConfig.LANGUAGE_CODE
				+ "')} LIMIT 1 } "
				+ "OPTIONAL {SELECT ?loc3 WHERE {?y vtio:isPartOf ?z. ?z rdfs:label ?loc3. FILTER(lang(?loc3)='"
				+ ServerConfig.LANGUAGE_CODE
				+ "')} LIMIT 1 } "
				+ "OPTIONAL {SELECT ?loc4 WHERE {?z vtio:isPartOf ?k. ?k rdfs:label ?loc4. FILTER(lang(?loc4)='"
				+ ServerConfig.LANGUAGE_CODE + "')} LIMIT 1 }} ";
		// "OPTIONAL {SELECT ?loc5 WHERE {?k vtio:isPartOf ?t. ?t rdfs:label ?loc5. FILTER(lang(?loc5)='"+ServerConfig.LANGUAGE_CODE+"')} LIMIT 1 }} ";
		// Viet Nam

		// Log.d("QUERY", "GET ADD OF PLACE "+query);
		ArrayList<ArrayList<String>> arrayResult = new ArrayList<ArrayList<String>>();
		ArrayList<String> result = new ArrayList<String>();
		try {
			arrayResult = executeQuery(query, false);
			result = arrayResult.get(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
		String partOfAddress = "";
		for (int i = 0; i < result.size(); i++) {
			if (!(result.get(i).contains("anyType"))) {
				if (i != 0) {

					partOfAddress = "-"
							+ result.get(i)
									.replace("^^" + NameSpace.xsd + "string",
											"")
									.replace("@" + ServerConfig.LANGUAGE_CODE,
											"");
					if (partOfAddress.indexOf("street") != -1)
						partOfAddress = partOfAddress.replace("street", "str");
					if (partOfAddress.indexOf("Street") != -1)
						partOfAddress = partOfAddress.replace("Street", "str");
					if (partOfAddress.indexOf("district") != -1)
						partOfAddress = partOfAddress.replace("district",
								"dist");
					if (partOfAddress.indexOf("District") != -1)
						partOfAddress = partOfAddress.replace("District",
								"dist");
					if (partOfAddress.indexOf("city") != -1)
						partOfAddress = partOfAddress.replace("city", "");
					if (partOfAddress.indexOf("City") != -1)
						partOfAddress = partOfAddress.replace("City", "");
					if (partOfAddress.indexOf("country") != -1)
						partOfAddress = partOfAddress.replace("country", "");
					returnValue = returnValue + partOfAddress;

				} else {
					returnValue = returnValue
							+ result.get(i).replace(
									"^^" + NameSpace.xsd + "string", "");
				}
			}
		}
		// Log.d("QUERY", "ADD RESULT: "+returnValue);
		return returnValue;
	}

	/**
	 * Lay ra list type cua mot place
	 * 
	 * @param placeURI
	 * @return
	 */
	public String listTypesOfPlace(String placeURI) {
		String returnValue = "";

		String query = ""
				+ "SELECT DISTINCT ?loc1 ?loc2 ?loc3 ?loc4 ?loc5 where { "
				+ "<" + placeURI
				+ "> vtio:hasLocation ?x.  ?x vtio:hasValue ?loc1."
				+ "OPTIONAL {?x vtio:isPartOf ?y. ?y vtio:hasValue ?loc2.} "
				+ "OPTIONAL {?y vtio:isPartOf ?z. ?z vtio:hasValue ?loc3.}"
				+ "OPTIONAL {?z vtio:isPartOf ?k. ?k vtio:hasValue ?loc4.}"
				+ "OPTIONAL {?k vtio:isPartOf ?t. ?t vtio:hasValue ?loc5.} "
				+ "}";

		// Log.d("QUERY", "GET ADD OF PLACE "+query);
		ArrayList<ArrayList<String>> arrayResult = new ArrayList<ArrayList<String>>();
		ArrayList<String> result = new ArrayList<String>();
		try {
			arrayResult = executeQuery(query, false);
			result = arrayResult.get(0);
		} catch (Exception e) {
			// TODO: handle exception
		}

		for (int i = 0; i < result.size(); i++) {
			if (!(result.get(i).contains("anyType"))) {
				if (i != 0) {
					returnValue = returnValue
							+ "-"
							+ result.get(i).replace(
									"^^" + NameSpace.xsd + "string", "");
				} else {
					returnValue = returnValue
							+ result.get(i).replace(
									"^^" + NameSpace.xsd + "string", "");
				}
			}
		}
		// Log.d("QUERY", "ADD RESULT: "+returnValue);

		return returnValue;
	}

	/**
	 * Lay ra thong tin co ban cua tat ca cac class. xep theo thu tu A--> Z
	 * 
	 * @return
	 */
	public ArrayList<ClassDataSimple> getAllClassSimple() {
		ArrayList<ClassDataSimple> returnValues = new ArrayList<ClassDataSimple>();

		String query = "SELECT DISTINCT ?uri ?label WHERE {?uri rdf:type owl:Class. ?uri rdfs:label ?label. FILTER(lang(?label)='"
				+ ServerConfig.LANGUAGE_CODE + "') } ORDER BY ASC(?label)";
		ArrayList<ArrayList<String>> listResult = executeQuery(query, false);
		for (int i = 0; i < listResult.size(); i++) {
			// Log.d("", "CLASS "+i+" :" +listResult.get(i).toString());
			ClassDataSimple classDataSimple = new ClassDataSimple();
			ArrayList<String> row = listResult.get(i);
			classDataSimple.setUri(row.get(0));
			classDataSimple.setLabel(row.get(1)
					.replace("^^" + NameSpace.xsd + "string", "")
					.replace("@" + ServerConfig.LANGUAGE_CODE, ""));
			returnValues.add(classDataSimple);
		}

		return returnValues;
	}

	/**
	 * lay ve du lieu don gian cua tat ca cac class la con cua mot class
	 * 
	 * @param classUri
	 * @return
	 */
	public ArrayList<ClassDataSimple> getAllSubClassDataSimpleOf(String classUri) {
		ArrayList<ClassDataSimple> returnValues = new ArrayList<ClassDataSimple>();
		// String query =
		// "SELECT DISTINCT ?uri ?label WHERE {?uri rdfs:subClassOf <"+classUri+">. ?uri rdf:type owl:Class. ?uri rdfs:label ?label. FILTER(lang(?label)='"+ServerConfig.LANGUAGE_CODE+"')} ORDER BY ASC(?label)";
		// dungct: sua de adapted voi ontology co nhan tieng viet khong dau
		String query = "SELECT DISTINCT ?uri ?label WHERE {?uri rdfs:subClassOf <"
				+ classUri
				+ ">. ?uri rdf:type owl:Class. {SELECT ?label WHERE {?uri rdfs:label ?label. FILTER(lang(?label)='"
				+ ServerConfig.LANGUAGE_CODE
				+ "')} LIMIT 1}} ORDER BY ASC(?label)";
		// String query =
		// "SELECT DISTINCT ?uri ?label WHERE { ?place rdf:type ?uri. ?uri rdf:type owl:Class. ?place vtio:hasLocation ?lo.   ?uri rdfs:subClassOf <"+classUri+">.  {SELECT ?label WHERE {?uri rdfs:label ?label. FILTER(lang(?label)='"+ServerConfig.LANGUAGE_CODE+"')} LIMIT 1} ?lo vtio:isPartOf <"+ServerConfig.currentCityUri+">. } ";
		// Log.v("QUERY", query);
		ArrayList<ArrayList<String>> listResult = executeQuery(query, true,
				true);
		for (int i = 0; i < listResult.size(); i++) {
			// Log.d("", "CLASS "+i+" :" +listResult.get(i).toString());
			ClassDataSimple classDataSimple = new ClassDataSimple();
			ArrayList<String> row = listResult.get(i);
			classDataSimple.setUri(row.get(0));
			classDataSimple.setLabel(row.get(1)
					.replace("^^" + NameSpace.xsd + "string", "")
					.replace("@" + ServerConfig.LANGUAGE_CODE, ""));
			returnValues.add(classDataSimple);
		}
		return returnValues;
	}

	public ArrayList<ClassDataSimple> getAllAdaptedSubClassOf(String classUri) {
		ArrayList<ClassDataSimple> returnValues = new ArrayList<ClassDataSimple>();
		String key = classUri + ServerConfig.currentCityLabel;
		Log.v("QUERY", "key subclass: " + key);
		if (OntologyCache.listAllSubclass.size() > 0
				&& OntologyCache.listAllSubclass.containsKey(key)) {
			if (OntologyCache.listAllSubclass.get(key).size() > 0)
				return OntologyCache.listAllSubclass.get(key);
		}
		String SOAP_ACTION = ServerConfig.SERVICE_NAMESPACE
				+ MethodName.GET_ALL_ADAPTED_SUBCLASS;
		SoapObject soapRequestObject = new SoapObject(
				ServerConfig.SERVICE_NAMESPACE,
				MethodName.GET_ALL_ADAPTED_SUBCLASS);
		soapRequestObject.addProperty("arg0", ServerConfig.LANGUAGE_CODE);
		soapRequestObject.addProperty("arg1", ServerConfig.currentCityUri);
		soapRequestObject.addProperty("arg2", classUri);
		soapRequestObject.addProperty("arg3", ServerConfig.VTIO_REPOSITORY_KEY);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(soapRequestObject);

		callService(SOAP_ACTION, envelope);

		// Log.v("TEST", SOAP_ACTION);
		try {
			SoapObject result = (SoapObject) envelope.bodyIn;
			// Log.v("TEST", "result: " + result.toString());
			// Log.v("COUNT","result adapted:" + result.getProperty(0));
			for (int i = 0; i < result.getPropertyCount(); i++) {
				SoapObject soapItem = (SoapObject) result.getProperty(i);
				ClassDataSimple classItem = new ClassDataSimple();
				String label = soapItem.getProperty("label").toString();
				String uri = soapItem.getProperty("url").toString();
				classItem.setLabel(label);
				classItem.setUri(uri);

				returnValues.add(classItem);

			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("TEST", "Exception : " + e.getCause());
		}

		if (returnValues.size() > 0) {
			OntologyCache.listAllSubclass.put(key, returnValues);
		}
		return returnValues;
	}

	public ArrayList<ClassDataSimple> getSubClassDataSimpleOf(String classUri) {
		ArrayList<ClassDataSimple> returnValues = new ArrayList<ClassDataSimple>();
		String query = "SELECT DISTINCT ?uri ?label WHERE {?uri rdfs:subClassOf <"
				+ classUri
				+ ">. ?uri rdf:type owl:Class. ?uri rdfs:label ?label. FILTER(lang(?label)='"
				+ ServerConfig.LANGUAGE_CODE + "')} ORDER BY ASC(?label)";
		ArrayList<ArrayList<String>> listResult = executeQuery(query, false);
		for (int i = 0; i < listResult.size(); i++) {
			ClassDataSimple classDataSimple = new ClassDataSimple();
			ArrayList<String> row = listResult.get(i);
			classDataSimple.setUri(row.get(0));
			classDataSimple.setLabel(row.get(1)
					.replace("^^" + NameSpace.xsd + "string", "")
					.replace("@" + ServerConfig.LANGUAGE_CODE, ""));
			returnValues.add(classDataSimple);
		}
		return returnValues;
	}

	public ArrayList<ClassDataSimple> getTypesOfInstance(String Uri) {
		ArrayList<ClassDataSimple> returnValues = new ArrayList<ClassDataSimple>();

		String query = "SELECT DISTINCT ?uri ?label WHERE {<"
				+ Uri
				+ "> rdf:type ?uri. ?uri rdfs:label ?label. FILTER(lang(?label)='"
				+ ServerConfig.LANGUAGE_CODE + "')} ORDER BY ASC(?label)";
		ArrayList<ArrayList<String>> listResult = executeQuery(query, true);
		for (int i = 0; i < listResult.size(); i++) {
			ArrayList<String> row = listResult.get(i);
			if (!row.get(0).contains("Thing")) {
				ClassDataSimple classDataSimple = new ClassDataSimple();
				classDataSimple.setUri(row.get(0));
				classDataSimple.setLabel(row.get(1)
						.replace("^^" + NameSpace.xsd + "string", "")
						.replace("@" + ServerConfig.LANGUAGE_CODE, ""));
				returnValues.add(classDataSimple);
			}
		}

		return returnValues;
	}

	/**
	 * Lay ve du lieu cua tat ca cac resource
	 * 
	 * @return
	 */
	public ArrayList<InstanceDataSimple> getAllInstanceSimple() {
		ArrayList<InstanceDataSimple> returnValues = new ArrayList<InstanceDataSimple>();
		String query = "SELECT DISTINCT ?uri ?label WHERE {?uri rdfs:label ?label. FILTER(lang(?label)='"
				+ ServerConfig.LANGUAGE_CODE + "')} ORDER BY ASC(?label)";

		ArrayList<ArrayList<String>> listResult = executeQuery(query, true);
		for (int i = 0; i < listResult.size(); i++) {
			ArrayList<String> row = listResult.get(i);
			// if ((!row.get(0).contains("Thing")) &&
			// (!row.get(1).toLowerCase().contains("general"))){
			if ((!row.get(0).contains("Thing"))) {
				InstanceDataSimple instanceDataSimple = new InstanceDataSimple();
				instanceDataSimple.setURI(row.get(0));
				instanceDataSimple.setLabel(row.get(1)
						.replace("^^" + NameSpace.xsd + "string", "")
						.replace("@" + ServerConfig.LANGUAGE_CODE, ""));
				returnValues.add(instanceDataSimple);
			}
		}
		return returnValues;
	}

	/**
	 * Lay ve du lieu don gian cua tat ca cac lop
	 * 
	 * @return
	 */
	public ArrayList<InstanceDataSimple> getAllClassAsInstanceDataSimple() {
		ArrayList<InstanceDataSimple> returnValues = new ArrayList<InstanceDataSimple>();

		String query = "select distinct ?class ?label where { ?class rdf:type owl:Class.  ?class rdfs:label ?label. FILTER(lang(?label)='"
				+ ServerConfig.LANGUAGE_CODE + "')} ORDER BY ASC(?label)";

		ArrayList<ArrayList<String>> listResult = executeQuery(query, true);
		for (int i = 0; i < listResult.size(); i++) {
			ArrayList<String> row = listResult.get(i);
			if ((!row.get(0).contains("Thing"))
					&& (!row.get(1).toLowerCase().contains("general"))) {
				InstanceDataSimple instanceDataSimple = new InstanceDataSimple();
				instanceDataSimple.setURI(row.get(0));
				instanceDataSimple.setLabel(row.get(1)
						.replace("^^" + NameSpace.xsd + "string", "")
						.replace("@" + ServerConfig.LANGUAGE_CODE, ""));
				instanceDataSimple.setType(ResourceType.CLASS_TYPE);
				returnValues.add(instanceDataSimple);
			}
		}
		return returnValues;
	}

	/**
	 * Lay ve du lieu tat ca cac object property
	 */
	public ArrayList<InstanceDataSimple> getAllObjectPropertyAsInstanceDataSimple() {
		ArrayList<InstanceDataSimple> returnValues = new ArrayList<InstanceDataSimple>();

		String query = "select distinct ?objectproperty ?label where { ?objectproperty rdf:type owl:ObjectProperty.  ?objectproperty rdfs:label ?label. FILTER(lang(?label)='"
				+ ServerConfig.LANGUAGE_CODE + "')} ORDER BY ASC(?label)";

		ArrayList<ArrayList<String>> listResult = executeQuery(query, true);
		for (int i = 0; i < listResult.size(); i++) {
			ArrayList<String> row = listResult.get(i);
			if ((!row.get(0).contains("Thing"))
					&& (!row.get(1).toLowerCase().contains("general"))) {
				InstanceDataSimple instanceDataSimple = new InstanceDataSimple();
				instanceDataSimple.setURI(row.get(0));
				instanceDataSimple.setLabel(row.get(1)
						.replace("^^" + NameSpace.xsd + "string", "")
						.replace("@" + ServerConfig.LANGUAGE_CODE, ""));
				instanceDataSimple.setType(ResourceType.PROPERTY_OBJECT_TYPE);
				returnValues.add(instanceDataSimple);
			}
		}
		return returnValues;
	}

	/**
	 * Lay ve du lieu cua tat ca cac datatype property
	 */
	public ArrayList<InstanceDataSimple> getAllDataPropertyAsInstanceDataSimple() {
		ArrayList<InstanceDataSimple> returnValues = new ArrayList<InstanceDataSimple>();

		String query = "select distinct ?data ?label where { ?data rdf:type owl:DatatypeProperty.  ?data rdfs:label ?label. FILTER(lang(?label)='"
				+ ServerConfig.LANGUAGE_CODE + "')} ORDER BY ASC(?label)";
		//
		ArrayList<ArrayList<String>> listResult = executeQuery(query, true);
		for (int i = 0; i < listResult.size(); i++) {
			ArrayList<String> row = listResult.get(i);
			if ((!row.get(0).contains("Thing"))
					&& (!row.get(1).toLowerCase().contains("general"))) {
				InstanceDataSimple instanceDataSimple = new InstanceDataSimple();
				instanceDataSimple.setURI(row.get(0));
				instanceDataSimple.setLabel(row.get(1)
						.replace("^^" + NameSpace.xsd + "string", "")
						.replace("@" + ServerConfig.LANGUAGE_CODE, ""));
				instanceDataSimple.setType(ResourceType.PROPERTY_DATA_TYPE);
				returnValues.add(instanceDataSimple);
			}
		}
		return returnValues;
	}

	/**
	 * Lay ve du lieu data property
	 */
	public ArrayList<PropertyDataSimple> getAllDataProperty() {
		ArrayList<PropertyDataSimple> returnData = new ArrayList<PropertyDataSimple>();
		String query = "select distinct ?data ?label where { ?data rdf:type owl:DatatypeProperty.  ?data rdfs:label ?label. FILTER(lang(?label)='"
				+ ServerConfig.LANGUAGE_CODE + "')}";
		ArrayList<ArrayList<String>> results = executeQuery(query, false);
		for (int i = 0; i < results.size(); i++) {
			PropertyDataSimple property = new PropertyDataSimple();
			property.setUrl(results.get(i).get(0));
			property.setLabel(results.get(i).get(1)
					.replace("@" + ServerConfig.LANGUAGE_CODE, ""));
			property.setObjectProperty(false);
			returnData.add(property);
		}
		return returnData;
	}

	/**
	 * Lay ve tat ca cac object property
	 */
	public ArrayList<PropertyDataSimple> getAllObjectProperty() {
		ArrayList<PropertyDataSimple> returnData = new ArrayList<PropertyDataSimple>();
		String query = "select distinct ?uri ?label where { ?uri rdf:type owl:ObjectProperty.  ?uri rdfs:label ?label. FILTER(lang(?label)='"
				+ ServerConfig.LANGUAGE_CODE + "')}";
		ArrayList<ArrayList<String>> results = executeQuery(query, false);
		for (int i = 0; i < results.size(); i++) {
			PropertyDataSimple property = new PropertyDataSimple();
			property.setUrl(results.get(i).get(0));
			property.setLabel(results.get(i).get(1)
					.replace("@" + ServerConfig.LANGUAGE_CODE, ""));
			property.setObjectProperty(true);
			returnData.add(property);
		}
		return returnData;
	}

	public ArrayList<PropertyDataSimple> getAllProperty() {
		ArrayList<PropertyDataSimple> returnData = new ArrayList<PropertyDataSimple>();
		String query = "select distinct ?uri ?label where "
				+ "{ {{?uri rdf:type owl:ObjectProperty.} " + " UNION"
				+ " {?uri rdf:type owl:DatatypeProperty.}}"
				+ " ?uri rdfs:label ?label. FILTER(lang(?label)='"
				+ ServerConfig.LANGUAGE_CODE + "')}";
		ArrayList<ArrayList<String>> results = executeQuery(query, false);
		for (int i = 0; i < results.size(); i++) {
			PropertyDataSimple property = new PropertyDataSimple();
			property.setUrl(results.get(i).get(0));
			property.setLabel(results.get(i).get(1)
					.replace("@" + ServerConfig.LANGUAGE_CODE, ""));
			property.setObjectProperty(true);
			returnData.add(property);
		}
		return returnData;
	}

	/**
	 * Lay ra cac property truc tiep cua mot class
	 * 
	 * @param classUri
	 * @return
	 */
	ArrayList<PropertyDataSimple> getPropertiesSimpleOfClass(String classUri) {
		ArrayList<PropertyDataSimple> returnValues = new ArrayList<PropertyDataSimple>();
		// Lay gop cac property nhan class lam domain truc tiep, cap 1, cap 2,
		// cap 3, cap 4

		String query = "select distinct ?p ?type ?label where {" + "{"
				+ "{ ?p rdfs:domain <"
				+ classUri
				+ ">. } "
				+ "UNION "
				+ "{"
				+ "?p rdfs:domain ?blank_node1. "
				+ "?blank_node1 owl:unionOf ?blank_node2. "
				+ "?blank_node2 rdf:first <"
				+ classUri
				+ ">. "
				+ "} "
				+ "UNION { "
				+ "?p rdfs:domain ?blank_node1. "
				+ "?blank_node1 owl:unionOf ?blank_node2. "
				+ "?blank_node2 rdf:rest ?blank_node3. "
				+ "?blank_node3 rdf:first <"
				+ classUri
				+ ">. "
				+ "} "
				+ "UNION { "
				+ "?p rdfs:domain ?blank_node1. "
				+ "?blank_node1 owl:unionOf ?blank_node2. "
				+ "?blank_node2 rdf:rest ?blank_node3. "
				+ "?blank_node3 rdf:rest ?blank_node4. "
				+ "?blank_node4 rdf:first <"
				+ classUri
				+ ">. "
				+ "} "
				+ "UNION {"
				+ "?p rdfs:domain ?blank_node1. "
				+ "?blank_node1 owl:unionOf ?blank_node2. "
				+ "?blank_node2 rdf:rest ?blank_node3. "
				+ "?blank_node3 rdf:rest ?blank_node4. "
				+ "?blank_node4 rdf:rest ?blank_node5. "
				+ "?blank_node5 rdf:first <"
				+ classUri
				+ ">. "
				+ "} "
				+

				"} "
				+ "?p rdf:type ?type. "
				+ "?p rdfs:label ?label. "
				+ "FILTER(lang(?label)='"
				+ ServerConfig.LANGUAGE_CODE
				+ "')"
				+ "}";

		ArrayList<ArrayList<String>> queryResults = executeQuery(query, false);
		for (int i = 0; i < queryResults.size(); i++) {
			PropertyDataSimple property = new PropertyDataSimple();
			property.setUrl(queryResults.get(i).get(0));
			if (queryResults.get(i).get(1).toLowerCase().contains("datatype")) {
				property.setObjectProperty(false);
			} else {
				property.setObjectProperty(true);
			}
			property.setLabel(queryResults.get(i).get(2)
					.replace("^^" + NameSpace.xsd + "string", "")
					.replace("@" + ServerConfig.LANGUAGE_CODE, ""));

			if ((!queryResults.get(i).get(1).contains("SymmetricProperty"))
					&& (!queryResults.get(i).get(1)
							.contains("TransitiveProperty"))) {
				returnValues.add(property);
			}
		}

		/*
		 * // Lay cac property nhan class lam  domain truc tiep
		 * 
		 * String query = "select distinct ?p ?type ?label where {" +
		 * "?p rdfs:domain <"+classUri+">. " + "?p rdf:type ?type. " +
		 * "?p rdfs:label ?label." +
		 * "FILTER(lang(?label)='"+ServerConfig.LANGUAGE_CODE+"')"+ "}";
		 * ArrayList<ArrayList<String>> queryResults = executeQuery(query,
		 * false); for (int i=0; i<queryResults.size(); i++){ PropertyDataSimple
		 * property = new PropertyDataSimple();
		 * property.setUrl(queryResults.get(i).get(0)); if
		 * (queryResults.get(i).get(1).toLowerCase().contains("datatype")){
		 * property.setObjectProperty(false); } else {
		 * property.setObjectProperty(true); }
		 * property.setLabel(queryResults.get
		 * (i).get(2).replace("^^"+NameSpace.xsd+"string",
		 * "").replace("@"+ServerConfig.LANGUAGE_CODE, ""));
		 * 
		 * if ((!queryResults.get(i).get(1).contains("SymmetricProperty")) &&
		 * (!queryResults.get(i).get(1).contains("TransitiveProperty"))){
		 * returnValues.add(property); } }
		 * 
		 * 
		 * // Lay cac property nhan class lam  domain cap 1
		 * 
		 * String query1 = "select distinct ?p ?type ?label where { " +
		 * "?p rdfs:domain ?blank_node1. " +
		 * "?blank_node1 owl:unionOf ?blank_node2. " +
		 * "?blank_node2 rdf:first <"+classUri+">." + "?p rdf:type ?type." +
		 * "?p rdfs:label ?label." +
		 * "FILTER(lang(?label)='"+ServerConfig.LANGUAGE_CODE+"')"+ "}";
		 * queryResults = executeQuery(query1, false); for (int i=0;
		 * i<queryResults.size(); i++){ PropertyDataSimple property = new
		 * PropertyDataSimple(); property.setUrl(queryResults.get(i).get(0)); if
		 * (queryResults.get(i).get(1).toLowerCase().contains("object")){
		 * property.setObjectProperty(true); } else {
		 * property.setObjectProperty(false); }
		 * property.setLabel(queryResults.get
		 * (i).get(2).replace("^^"+NameSpace.xsd+"string",
		 * "").replace("@"+ServerConfig.LANGUAGE_CODE, ""));
		 * 
		 * returnValues.add(property); }
		 * 
		 * 
		 * // Lay cac property nhan class lam  domain cap 2
		 * 
		 * String query2 = "select distinct ?p ?type ?label where {" +
		 * "?p rdfs:domain ?blank_node1. " +
		 * "?blank_node1 owl:unionOf ?blank_node2. " +
		 * "?blank_node2 rdf:rest ?blank_node3. " +
		 * "?blank_node3 rdf:first <"+classUri+">." + "?p rdf:type ?type." +
		 * "?p rdfs:label ?label." +
		 * "FILTER(lang(?label)='"+ServerConfig.LANGUAGE_CODE+"')"+ "}";
		 * queryResults = executeQuery(query2, false); for (int i=0;
		 * i<queryResults.size(); i++){ PropertyDataSimple property = new
		 * PropertyDataSimple(); property.setUrl(queryResults.get(i).get(0)); if
		 * (queryResults.get(i).get(1).toLowerCase().contains("object")){
		 * property.setObjectProperty(true); } else {
		 * property.setObjectProperty(false); }
		 * property.setLabel(queryResults.get
		 * (i).get(2).replace("^^"+NameSpace.xsd+"string",
		 * "").replace("@"+ServerConfig.LANGUAGE_CODE, ""));
		 * 
		 * returnValues.add(property); }
		 * 
		 * 
		 * // Lay cac property nhan class lam  domain cap 3
		 * 
		 * String query3 = "select ?p ?type ?label where {" +
		 * "?p rdfs:domain ?blank_node1. " +
		 * "?blank_node1 owl:unionOf ?blank_node2.  " +
		 * "?blank_node2 rdf:rest ?blank_node3. " +
		 * "?blank_node3 rdf:rest ?blank_node4." +
		 * "?blank_node4 rdf:first <"+classUri+">." + "?p rdf:type ?type." +
		 * "?p rdfs:label ?label." +
		 * "FILTER(lang(?label)='"+ServerConfig.LANGUAGE_CODE+"')"+ "}";
		 * queryResults = executeQuery(query3, false); for (int i=0;
		 * i<queryResults.size(); i++){ PropertyDataSimple property = new
		 * PropertyDataSimple(); property.setUrl(queryResults.get(i).get(0)); if
		 * (queryResults.get(i).get(1).toLowerCase().contains("object")){
		 * property.setObjectProperty(true); } else {
		 * property.setObjectProperty(false); }
		 * property.setLabel(queryResults.get
		 * (i).get(2).replace("^^"+NameSpace.xsd+"string",
		 * "").replace("@"+ServerConfig.LANGUAGE_CODE, ""));
		 * 
		 * returnValues.add(property); }
		 * 
		 * 
		 * // Lay cac property nhan class lam  domain cap 4
		 * 
		 * String query4 = "select ?p ?type ?label where { " +
		 * "?p rdfs:domain ?blank_node1. " +
		 * "?blank_node1 owl:unionOf ?blank_node2. " +
		 * "?blank_node2 rdf:rest ?blank_node3. " +
		 * "?blank_node3 rdf:rest ?blank_node4." +
		 * "?blank_node4 rdf:rest ?blank_node5. " +
		 * "?blank_node5 rdf:first <"+classUri+">." + "?p rdf:type ?type." +
		 * "?p rdfs:label ?label." +
		 * "FILTER(lang(?label)='"+ServerConfig.LANGUAGE_CODE+"')"+ "}";
		 * queryResults = executeQuery(query4, false); for (int i=0;
		 * i<queryResults.size(); i++){ PropertyDataSimple property = new
		 * PropertyDataSimple(); property.setUrl(queryResults.get(i).get(0)); if
		 * (queryResults.get(i).get(1).toLowerCase().contains("object")){
		 * property.setObjectProperty(true); } else {
		 * property.setObjectProperty(false); }
		 * property.setLabel(queryResults.get
		 * (i).get(2).replace("^^"+NameSpace.xsd+"string",
		 * "").replace("@"+ServerConfig.LANGUAGE_CODE, ""));
		 * 
		 * returnValues.add(property); }
		 */

		return returnValues;
	}

	/**
	 * Lay ve tat ca cac thuoc tinh cua mot lop
	 * 
	 * @param classUri
	 * @return
	 */

	public ArrayList<PropertyDataSimple> getAllPropertySimpleOfClass(
			String classUri) {
		ArrayList<PropertyDataSimple> returnValues = new ArrayList<PropertyDataSimple>();
		/**
		 * property chung cua tat ca cac lop
		 */
		/*
		 * PropertyDataSimple property = new PropertyDataSimple(); if
		 * (ServerConfig.LANGUAGE_CODE.equals(LanguageCode.VIETNAMESE)){
		 * property.setLabel("Có tóm tắt"); } else if
		 * (ServerConfig.LANGUAGE_CODE.equals(LanguageCode.ENGLISH)){
		 * property.setLabel("has abstract"); }
		 * 
		 * 
		 * 
		 * property.setUrl(NameSpace.vtio+"hasAbstract");
		 * property.setObjectProperty(false); returnValues.add(property);
		 */
		/*
		 * Tam thoi thay bang query moi voi toc do cao hon - tranh viec goi
		 * query nhieu lan
		 */
		/**
		 * Lay ra tat ca cac property truc tiep
		 */
		/*
		 * Tam thoi thay bang query moi voi toc do cao hon - tranh viec goi
		 * query nhieu lan
		 */
		/*
		 * Log.d("", "Property truc tiep cua "+classUri);
		 * returnValues.addAll(getPropertiesSimpleOfClass(classUri));
		 * 
		 * /** lay ra tat ca cac lop cha
		 */
		/*
		 * ArrayList<ClassDataSimple> listSuperClass =
		 * getAllSuperClassSimpleOf(classUri); for (int i=0;
		 * i<listSuperClass.size(); i++){ Log.d("",
		 * "Property truc tiep cua "+listSuperClass.get(i).getLabel());
		 * returnValues
		 * .addAll(getPropertiesSimpleOfClass(listSuperClass.get(i).getUri()));
		 * }
		 */

		// Query 1 lan lay het property can thiet

		String query = "select distinct ?p ?type ?label where { " + "<"
				+ classUri
				+ "> rdfs:subClassOf ?super. "
				+ "{"
				+ "{ ?p rdfs:domain "
				+ "<"
				+ classUri
				+ ">. } "
				+ "UNION "
				+ "{ ?p rdfs:domain ?super. } "
				+ "UNION "
				+ "{ "
				+

				"?p rdfs:domain ?blank_node1. "
				+ "?blank_node1 owl:unionOf ?blank_node2. "
				+ "{{?blank_node2 rdf:first ?super.} UNION {?blank_node2 rdf:first "
				+ "<"
				+ classUri
				+ ">.}} "
				+

				"} "
				+ "UNION { "
				+ "?p rdfs:domain ?blank_node1. "
				+ "?blank_node1 owl:unionOf ?blank_node2. "
				+ "?blank_node2 rdf:rest ?blank_node3. "
				+ "{{?blank_node3 rdf:first ?super.} UNION {?blank_node3 rdf:first "
				+ "<"
				+ classUri
				+ ">.}} "
				+

				"} "
				+ "UNION { "
				+ "?p rdfs:domain ?blank_node1. "
				+ "?blank_node1 owl:unionOf ?blank_node2. "
				+ "?blank_node2 rdf:rest ?blank_node3. "
				+ "?blank_node3 rdf:rest ?blank_node4. "
				+ "{{?blank_node4 rdf:first ?super.} UNION {?blank_node4 rdf:first "
				+ "<"
				+ classUri
				+ ">.}} "
				+

				"} "
				+ "UNION { "
				+ "?p rdfs:domain ?blank_node1. "
				+ "?blank_node1 owl:unionOf ?blank_node2. "
				+ "?blank_node2 rdf:rest ?blank_node3. "
				+ "?blank_node3 rdf:rest ?blank_node4. "
				+ "?blank_node4 rdf:rest ?blank_node5. "
				+ "{{?blank_node5 rdf:first ?super.} UNION {?blank_node5 rdf:first "
				+ "<" + classUri + ">.}} "
				+

				"} "
				+

				"} "
				+ "?p rdf:type ?type. "
				+
				// "?p rdfs:label ?label. " +
				// "FILTER(lang(?label)='"+ServerConfig.LANGUAGE_CODE+"')}";
				// dungct - thay doi de ho tro cac thuoc tinh tieng Viet khong
				// dau trong ontology
				"{SELECT ?label WHERE {?p rdfs:label ?label. FILTER(lang(?label)='"
				+ ServerConfig.LANGUAGE_CODE + "')}  LIMIT 1 }}";

		ArrayList<ArrayList<String>> queryResults = executeQuery(query, true,
				true);
		for (int i = 0; i < queryResults.size(); i++) {
			PropertyDataSimple prop = new PropertyDataSimple();
			prop.setUrl(queryResults.get(i).get(0));
			if (queryResults.get(i).get(1).toLowerCase().contains("datatype")) {
				prop.setObjectProperty(false);
			} else {
				prop.setObjectProperty(true);
			}
			prop.setLabel(queryResults.get(i).get(2)
					.replace("^^" + NameSpace.xsd + "string", "")
					.replace("@" + ServerConfig.LANGUAGE_CODE, ""));

			if ((!queryResults.get(i).get(1).contains("SymmetricProperty"))
					&& (!queryResults.get(i).get(1)
							.contains("TransitiveProperty"))) {
				returnValues.add(prop);
			}
		}
		return returnValues;
	}

	/**
	 * Lay ve du lieu cua tat ca cac instance
	 */
	public ArrayList<InstanceDataSimple> getAllInstanceDataSimple() {
		ArrayList<InstanceDataSimple> returnValues = new ArrayList<InstanceDataSimple>();

		String query = "select distinct ?uri ?label ?type  {"
				+ "{{?uri rdf:type owl:Thing.}"
				+ "UNION {"
				+ "?uri rdf:type owl:NamedIndividual.}"
				+ "UNION { ?uri rdf:type vtio:Description.  }}"
				+ "?uri rdfs:label ?label. ?uri rdf:type ?type. "
				+ " FILTER(lang(?label)='"
				+ ServerConfig.LANGUAGE_CODE
				+ "' && ?type != owl:NamedIndividual && ?type != owl:Thing && ?type !=vtio:Description)}"
				+ "ORDER BY ASC(?label)";
		ArrayList<ArrayList<String>> listResult = executeQuery(query, false);
		Log.v("QUERY", "GETINSTANCE: " + query);
		for (int i = 0; i < listResult.size(); i++) {
			ArrayList<String> row = listResult.get(i);
			// if (!row.get(2).contains("Thing")){
			// Log.v("QUERY", "label" + row.get(1) + " type:" +row.get(2));
			InstanceDataSimple instanceDataSimple = new InstanceDataSimple();
			instanceDataSimple.setURI(row.get(0));
			instanceDataSimple.setType(ResourceType.INSTANCE_TYPE);
			instanceDataSimple.setInstanceType(row.get(2));
			instanceDataSimple.setLabel(row.get(1)
					.replace("^^" + NameSpace.xsd + "string", "")
					.replace("@" + ServerConfig.LANGUAGE_CODE, ""));
			returnValues.add(instanceDataSimple);
			// }
		}
		return returnValues;
	}

	/**
	 * Lay tat ca Concept
	 */
	// public ArrayList<ArrayList<String>> getAllConceptWithIcon(){
	// String querry ="select ?conceptURI ?iconURL ?label where " + "{"
	// + "?conceptURI rdf:type owl:Class."
	// // + "?conceptURI rdfs:subClassOf owl:Thing. "
	// + "?conceptURI vtio:hasIcon ?iconURL."
	// + "?conceptURI rdfs:label ?label."
	// +
	// "FILTER(lang(?label)='"+ServerConfig.LANGUAGE_CODE+"' && ?iconURL!=\"null\") "
	// + "}";
	//
	// //return executeQuery(querry, true);
	// return executeQuery(querry, false);
	// }
	public ArrayList<ArrayList<String>> getAllConceptWithIcon(Context ctx) {
		// SQLiteAdapter sqLiteAdapter = new SQLiteAdapter(ctx);
		// sqLiteAdapter.checkAndCopyDatabase();
		// ArrayList<ArrayList<String>> listConcepts =
		// sqLiteAdapter.getConceptWithIcon();
		ArrayList<ArrayList<String>> listConcepts = new ArrayList<ArrayList<String>>();
		Log.v("TEST", "list concepts: " + listConcepts.size());
		if (listConcepts == null || listConcepts.size() == 0) {
			/*
			 * String querry ="select ?conceptURI ?iconURL ?label where " + "{"
			 * + "?conceptURI rdf:type owl:Class." // +
			 * "?conceptURI rdfs:subClassOf owl:Thing. " +
			 * "?conceptURI vtio:hasIcon ?iconURL." +
			 * "?conceptURI rdfs:label ?label." + "FILTER( ?iconURL!=\"null\") "
			 * + "}";
			 */

			String querry = "select ?conceptURI ?iconURL ?label where "
					+ "{"
					+ "?conceptURI rdf:type owl:Class."
					+ "?conceptURI vtio:hasIcon ?iconURL."
					+ " { select ?label where {?conceptURI rdfs:label ?label. } LIMIT 2 } "
					+ "FILTER( ?iconURL!=\"null\") " + "}";

			ArrayList<ArrayList<String>> listResult = executeQuery(querry,
					false);
			// sqLiteAdapter.insertListConcepts(listResult);
			return listResult;
		} else
			return listConcepts;

	}

	public ArrayList<ArrayList<String>> getURIClassFromURIInstance(
			String URIInstance) {
		String querry = "select ?conceptURI where " + " { <" + URIInstance
				+ "> rdf:type ?conceptURI. "
				+ " FILTER ((?conceptURI != owl:Thing)&&"
				+ "(?conceptURI !=owl:NamedIndividual)&&"
				+ "(?conceptURI !=vtio:Description)) " + "}";

		return executeQuery(querry, false);
	}

	/**
	 * Thuc thi mo cau truy van co rang buoc toa do dia ly, tra ve list cac ket
	 * qua
	 * 
	 * @param queryString
	 * @return
	 */
	public ArrayList<ArrayList<String>> executeQueryWithGeoConstrains(
			String queryString, boolean usesReasonner, int varIndex,
			float geoLat, float geoLon, float radius) {
		ArrayList<ArrayList<String>> returnValues = new ArrayList<ArrayList<String>>();

		/**
		 * Kiem tra trong cache
		 */
		String key = String.valueOf(usesReasonner) + queryString
				+ String.valueOf(varIndex) + String.valueOf(geoLat)
				+ String.valueOf(geoLon) + String.valueOf(radius);
		if (OntologyCache.listQueryWithGeo.size() > 0
				&& OntologyCache.listQueryWithGeo.containsKey(key)) {
			if (OntologyCache.listQueryWithGeo.get(key).size() > 0)
				return OntologyCache.listQueryWithGeo.get(key);
		}

		String SOAP_ACTION = ServerConfig.SERVICE_NAMESPACE
				+ MethodName.EXECUTE_QUERY_WITH_GEO_CONSTRAINS;
		SoapObject soapRequestObject = new SoapObject(
				ServerConfig.SERVICE_NAMESPACE,
				MethodName.EXECUTE_QUERY_WITH_GEO_CONSTRAINS);
		soapRequestObject
				.addProperty("arg0", queryString.replaceAll("\n", " "));
		soapRequestObject.addProperty("arg1", usesReasonner);
		soapRequestObject.addProperty("arg2", varIndex);
		soapRequestObject.addProperty("arg3", String.valueOf(geoLat));
		soapRequestObject.addProperty("arg4", String.valueOf(geoLon));
		soapRequestObject.addProperty("arg5", String.valueOf(radius));
		soapRequestObject.addProperty("arg6", ServerConfig.VTIO_REPOSITORY_KEY);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(soapRequestObject);

		callService(SOAP_ACTION, envelope);

		try {
			SoapObject result = (SoapObject) envelope.bodyIn;
			for (int i = 0; i < result.getPropertyCount(); i++) {
				SoapObject soapItem = (SoapObject) result.getProperty(i);
				ArrayList<String> resultItem = new ArrayList<String>();
				for (int j = 0; j < soapItem.getPropertyCount(); j++) {
					String value = soapItem.getProperty(j).toString();
					resultItem.add(value);
				}
				returnValues.add(resultItem);
			}
		} catch (Exception e) {
		}

		/**
		 * Luu vao cache
		 */
		if (returnValues.size() > 0) {

			OntologyCache.listQueryWithGeo.put(key, returnValues);
		}
		return returnValues;
	}

	public boolean executeAskQuery(String queryString, boolean reason) {
		boolean returnValues = false;
		String key = String.valueOf(reason) + queryString;
		if (OntologyCache.listQuery.size() > 0
				&& OntologyCache.listQuery.containsKey(key)) {
			// Log.d("", "CACHE TRUE: MATCH THIS QUERY IN CLIENT CACHE");
			return OntologyCache.listAskQuery.get(key);
		}

		// Log.d("", "QUERY STRING = "+queryString);

		String SOAP_ACTION = ServerConfig.SERVICE_NAMESPACE
				+ MethodName.EXECUTE_ASK_QUERY;
		SoapObject soapRequestObject = new SoapObject(
				ServerConfig.SERVICE_NAMESPACE, MethodName.EXECUTE_ASK_QUERY);
		soapRequestObject
				.addProperty("arg0", queryString.replaceAll("\n", " "));
		soapRequestObject.addProperty("arg1", reason);
		soapRequestObject.addProperty("arg2", ServerConfig.VTIO_REPOSITORY_KEY);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(soapRequestObject);

		callService(SOAP_ACTION, envelope);

		try {
			SoapObject result = (SoapObject) envelope.bodyIn;
			SoapPrimitive value = (SoapPrimitive) result.getProperty(0);
			returnValues = Boolean.parseBoolean(value.toString());
			// Log.d("", "ASK RESULT = "+String.valueOf(returnValues));
			OntologyCache.listAskQuery.put(queryString, returnValues);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return returnValues;
	}

	public NodeTreeDataSimple createRootTreeTopic() {

		// String query =
		// "SELECT DISTINCT ?class ?labelen ?labelvn ?parent WHERE{"+
		// "?class rdf:type owl:Class. " +
		// "?class rdfs:label ?labelen. " +
		// "?class rdfs:label ?labelvn. " +
		// "?class rdfs:subClassOf ?parent. "+
		// "FILTER((lang(?labelen)='en') && (lang(?labelvn)='vn'))"+
		// "}";
		// dungct: sua lai de adapted voi ontology 2 nhan tieng Viet co dau va
		// khong dau
		// String query =
		// "SELECT DISTINCT ?class ?labelen ?labelvn ?parent WHERE{"+
		// "?class rdf:type owl:Class. " +
		// "?class rdfs:label ?labelen. " +
		// "{ SELECT ?labelvn WHERE {?class rdfs:label ?labelvn. FILTER (lang(?labelvn)='vn')} LIMIT 1 } "
		// +
		// "?class rdfs:subClassOf ?parent. "+
		// "FILTER(lang(?labelen)='en')"+
		// "}";

		// 21-06-2012
		String query = "SELECT DISTINCT ?class ?labelen ?labelvn ?parent WHERE{"
				+ "?class rdf:type owl:Class. "
				+ "?class rdfs:label ?labelen. "
				+ "{ SELECT ?labelvn WHERE {?class rdfs:label ?labelvn. FILTER (lang(?labelvn)='vn')} LIMIT 1 } "
				+ "?class rdfs:subClassOf ?parent. "
				+ "FILTER(lang(?labelen)='en') "
				+ "FILTER ((?parent!=vtio:Place) && (?parent!=vtio:Style)&& (?parent!=owl:Thing) "
				+ "&& (?parent!=vtio:Accomodation) && (?parent!=vtio:Dining-Service) "
				+ "&& (?parent!=vtio:Tourist-Resource)&& (?parent!= vtio:Eastern-Architecture-Style) "
				+ "&& (?parent!=vtio:Location) && (?parent!=vtio:Activity) "
				+ "&& (?parent!=vtio:Asian-Cuisine-Style) && (?parent!=vtio:Commercial-Resource) "
				+ "&& (?parent!=vtio:European-Cuisine-Style)&& (?parent!=vtio:General-Class) "
				+ ") " + "} ";

		ArrayList<ArrayList<String>> listResult;
		if (OntologyCache.classWithParent.size() == 0) {
			listResult = executeQuery(query, false);
			Log.v("QUERY", "PREFERENCE: " + query);
			OntologyCache.classWithParent = listResult;
		} else {
			listResult = OntologyCache.classWithParent;

			Log.v("QUERY", "PREFERENCE: "
					+ OntologyCache.classWithParent.get(0).get(0));
		}

		if (listResult.size() == 0) {
			listResult = executeQuery(query, false);
		}

		NodeTreeDataSimple returnValue = new NodeTreeDataSimple();
		returnValue.setDataNode("http://hust.se.vtio.owl#Topic", "Topic", "");

		// khoi tao gia tri cho cac lop con cua topic
		for (int i = 0; i < listResult.size(); i++) {
			ArrayList<String> row = listResult.get(i);
			// Log.v("XUANXUANXUANXUANXYUAN",
			// "1111111111111111111111111111 :: "+row.get(2));
			if (row.get(3).equals("http://hust.se.vtio.owl#Topic")) {
				// Log.v("XUANXUANXUANXUANXYUAN",
				// "2222222222222222222222222222222222 :: "+row.get(0)+" 333:: "+row.get(1));
				NodeTreeDataSimple temp = new NodeTreeDataSimple();
				temp.setNodeParent(returnValue);
				temp.setDataNode(row.get(0),
						row.get(1).replace("^^" + NameSpace.xsd + "string", "")
								.replace("@en", ""),
						row.get(2).replace("^^" + NameSpace.xsd + "string", "")
								.replace("@vn", ""));

				returnValue.addChildNode(temp);
			}
		}
		// khoi tao gia tri cac lop con cua con topic
		for (int i = 0; i < returnValue.child.size(); i++) {
			for (int j = 0; j < listResult.size(); j++) {
				ArrayList<String> row = listResult.get(j);
				// Log.v("XUANXUANXUANXUANXYUAN",
				// "1111111111111111111111111111 :: "+row.get(2));
				if (row.get(3).equals(returnValue.child.get(i).Uri)) {
					// Log.v("XUANXUANXUANXUANXYUAN",
					// "2222222222222222222222222222222222 :: "+row.get(0)+" 333:: "+row.get(1));
					NodeTreeDataSimple temp = new NodeTreeDataSimple();
					temp.setNodeParent(returnValue.child.get(i));
					temp.setDataNode(
							row.get(0),
							row.get(1)
									.replace("^^" + NameSpace.xsd + "string",
											"").replace("@en", ""),
							row.get(2)
									.replace("^^" + NameSpace.xsd + "string",
											"").replace("@vn", ""));

					returnValue.child.get(i).addChildNode(temp);
				}
			}
		}

		return returnValue;

		//
	}

	/**
	 * 
	 * @param queryString
	 * @param usesReasonner
	 * @param varIndex
	 * @param geoLat
	 * @param geoLon
	 * @param radius
	 * @param isFaster
	 * @return uri long lat
	 */
	public ArrayList<ArrayList<String>> executeQueryWithGeoConstrains(
			String classUri, double geoLat, double geoLon, float radius,
			boolean hasPreference) {

		String queryString = "select distinct ?place  ?long ?lat where{"
				+ " GEO OBJECT "
				+ " SUBTYPE 'http://franz.com/ns/allegrograph/3.0/geospatial/spherical/degrees/-180.0/180.0/-90.0/90.0/5.0'"
				+ " HAVERSINE (POINT(" + geoLon + ", " + geoLat + "), "
				+ radius + " KM) {" + " ?place vtio:hasGeoPoint ?loc."
				+ " ?place rdf:type <" + classUri + ">." +
				// " ?place vtio:hasLongtitude ?long. ?place vtio:hasLatitude ?lat."
				// +
				" }  where{       	       ";
		if (OntologyCache.preferUser.size() > 0 && hasPreference) {
			queryString = queryString + " ?place  vtio:relatedToTopic ?t. {";
			queryString = queryString + " { ?t rdf:type <"
					+ OntologyCache.preferUser.get(0).getUri() + "> } ";
			try {
				for (int i = 1; i < OntologyCache.preferUser.size(); i++) {
					queryString = queryString + "UNION { ?t rdf:type <"
							+ OntologyCache.preferUser.get(i).getUri() + ">. }";
				}
			} catch (Exception e) {
			}
			queryString = queryString + " } ";
		}
		queryString = queryString
				+ " }} ORDER BY geo:haversine-km(?loc,(POINT(" + geoLon + ", "
				+ geoLat + ")) )";
		Log.v("QUERY", queryString);
		return executeQuery(queryString, true);
	}

	public ArrayList<PlaceDataSimple> getInstanceWithGeoConstrains(
			String classUri, double geoLat, double geoLon, float radius,
			boolean hasPreference, int LIMIT, int OFFSET) {

		String queryString = "select distinct ?place where{"
				+ " GEO OBJECT "
				+ " SUBTYPE 'http://franz.com/ns/allegrograph/3.0/geospatial/spherical/degrees/-180.0/180.0/-90.0/90.0/5.0'"
				+ " HAVERSINE (POINT(" + geoLon + ", " + geoLat + "), "
				+ radius + " KM) {" + " ?place vtio:hasGeoPoint ?loc."
				+ " ?place rdf:type <" + classUri + ">." +
				// " ?place vtio:hasLongtitude ?long. ?place vtio:hasLatitude ?lat."
				// +
				" }  where{       	       ";
		if (OntologyCache.preferUser.size() > 0 && hasPreference) {
			queryString = queryString + " ?place  vtio:relatedToTopic ?t. {";
			queryString = queryString + " { ?t rdf:type <"
					+ OntologyCache.preferUser.get(0).getUri() + "> } ";
			try {
				for (int i = 1; i < OntologyCache.preferUser.size(); i++) {
					queryString = queryString + "UNION { ?t rdf:type <"
							+ OntologyCache.preferUser.get(i).getUri() + ">. }";
				}
			} catch (Exception e) {
			}
			queryString = queryString + " } ";
		}
		queryString = queryString
				+ " }} ORDER BY geo:haversine-km(?loc,(POINT(" + geoLon + ", "
				+ geoLat + ")) ) LIMIT " + LIMIT + " OFFSET " + OFFSET;
		Log.v("QUERY", queryString);
		return getFullDataInstance(queryString, ServerConfig.LANGUAGE_CODE);
	}

	public ArrayList<ClassDataSimple> getAdaptedPlaceCategory(double latitude,
			double longitude, float range, String lang) {
		ArrayList<ClassDataSimple> returnValues = new ArrayList<ClassDataSimple>();
		String key = latitude + longitude + range + lang;
		if (OntologyCache.listCategory.size() > 0
				&& OntologyCache.listCategory.containsKey(key)) {
			if (OntologyCache.listCategory.get(key).size() > 0)
				return OntologyCache.listCategory.get(key);
		}
		String SOAP_ACTION = ServerConfig.SERVICE_NAMESPACE
				+ MethodName.GET_ADAPTED_PLACE_CATEGORY;
		SoapObject soapRequestObject = new SoapObject(
				ServerConfig.SERVICE_NAMESPACE,
				MethodName.GET_ADAPTED_PLACE_CATEGORY);
		soapRequestObject.addProperty("arg0", "" + latitude);
		soapRequestObject.addProperty("arg1", "" + longitude);
		soapRequestObject.addProperty("arg2", "" + range);
		soapRequestObject.addProperty("arg3", lang);
		soapRequestObject.addProperty("arg4", ServerConfig.VTIO_REPOSITORY_KEY);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(soapRequestObject);

		callService(SOAP_ACTION, envelope);

		// Log.v("TEST", SOAP_ACTION);
		try {
			SoapObject result = (SoapObject) envelope.bodyIn;
			// Log.v("TEST", "result: " + result.toString());
			// Log.v("COUNT","result adapted:" + result.getProperty(0));
			for (int i = 0; i < result.getPropertyCount(); i++) {
				SoapObject soapItem = (SoapObject) result.getProperty(i);
				ClassDataSimple classItem = new ClassDataSimple();
				String label = soapItem.getProperty("label").toString();
				String uri = soapItem.getProperty("url").toString();
				classItem.setLabel(label);
				classItem.setUri(uri);

				returnValues.add(classItem);

			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("TEST", "Exception : " + e.getCause());
		}

		if (returnValues.size() > 0) {
			OntologyCache.listCategory.clear();
			OntologyCache.listCategory.put(key, returnValues);
		} else {
			if (countCategoryCall == 0) {
				countCategoryCall += 1;
				return getAdaptedPlaceCategory(latitude, longitude, range, lang);
			} else {
				countCategoryCall = 0;
			}
		}

		ClassDataSimple placeGeneral = new ClassDataSimple();
		if (ServerConfig.LANGUAGE_CODE.equals(LanguageCode.VIETNAMESE)) {
			placeGeneral.setLabel("Tất cả");
		} else {
			placeGeneral.setLabel("All");
		}
		placeGeneral.setUri(NameSpace.vtio + "Place");
		returnValues.add(0, placeGeneral);
		return returnValues;
	}

	public ArrayList<PlaceDataSimple> getFullDataInstance(String query,
			String lang) {
		ArrayList<PlaceDataSimple> returnValues = new ArrayList<PlaceDataSimple>();
		String key = query + lang;
		if (OntologyCache.placeInstance.size() > 0
				&& OntologyCache.placeInstance.containsKey(key)) {
			if (OntologyCache.placeInstance.get(key).size() > 0)
				return OntologyCache.placeInstance.get(key);
		}
		String SOAP_ACTION = ServerConfig.SERVICE_NAMESPACE
				+ MethodName.GET_FULL_DATA_INSTANCE;
		SoapObject soapRequestObject = new SoapObject(
				ServerConfig.SERVICE_NAMESPACE,
				MethodName.GET_FULL_DATA_INSTANCE);
		soapRequestObject.addProperty("arg0", query);
		soapRequestObject.addProperty("arg1", lang);

		// add by dungct - do service web da them tham so reasoning
		soapRequestObject.addProperty("arg2", "true");
		soapRequestObject.addProperty("arg3", ServerConfig.VTIO_REPOSITORY_KEY);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(soapRequestObject);

		callService(SOAP_ACTION, envelope);

		try {
			SoapObject result = (SoapObject) envelope.bodyIn;
			Log.v("COUNT", "result instances: " + result.getProperty(0));
			for (int i = 0; i < result.getPropertyCount(); i++) {
				SoapObject soapItem = (SoapObject) result.getProperty(i);
				PlaceDataSimple classItem = new PlaceDataSimple();
				String abstractInfo = soapItem.getProperty("abstractInfo")
						.toString();
				String address = soapItem.getProperty("address").toString();
				String imageURL = soapItem.getProperty("imageURL").toString();
				String label = soapItem.getProperty("label").toString();
				double latitude = 0.0f;
				double longitude = 0.0f;
				try {
					latitude = Double.parseDouble(soapItem.getProperty(
							"latitude").toString());
					longitude = Double.parseDouble(soapItem.getProperty(
							"longitude").toString());

				} catch (Exception e) {
					// TODO: handle exception
				}
				String location = soapItem.getProperty("location").toString();
				String phone = soapItem.getProperty("phone").toString();
				String type = soapItem.getProperty("type").toString();
				String uri = soapItem.getProperty("uri").toString();
				int ratingNum = 0;
				try {
					ratingNum = Integer.parseInt(soapItem.getProperty(
							"ratingNum").toString());
				} catch (Exception e) {
					// TODO: handle exception
				}
				boolean wellKnown = false;
				try {
					wellKnown = Boolean.parseBoolean(soapItem.getProperty(
							"wellKnown").toString());
				} catch (Exception e) {
					// TODO: handle exception
				}
				classItem.set(uri, label, abstractInfo, type, ratingNum,
						imageURL, wellKnown, address, phone, longitude,
						latitude);
				returnValues.add(classItem);

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (returnValues.size() > 0) {
			OntologyCache.placeInstance.put(key, returnValues);
		}
		return returnValues;
	}

	// dungct
	public ArrayList<PlaceDataSimple> getFullDataInstance(String query,
			String lang, boolean isCache, boolean isReasoning) {
		Log.v("QUERY", "reason: " + isReasoning + " query: " + query);
		ArrayList<PlaceDataSimple> returnValues = new ArrayList<PlaceDataSimple>();
		String key = query + lang;
		if (OntologyCache.placeInstance.size() > 0
				&& OntologyCache.placeInstance.containsKey(key)) {
			if (OntologyCache.placeInstance.get(key).size() > 0)
				return OntologyCache.placeInstance.get(key);
		}
		String SOAP_ACTION = ServerConfig.SERVICE_NAMESPACE
				+ MethodName.GET_FULL_DATA_INSTANCE;
		SoapObject soapRequestObject = new SoapObject(
				ServerConfig.SERVICE_NAMESPACE,
				MethodName.GET_FULL_DATA_INSTANCE);
		soapRequestObject.addProperty("arg0", query);
		soapRequestObject.addProperty("arg1", lang);
		soapRequestObject.addProperty("arg2", "" + isReasoning);
		soapRequestObject.addProperty("arg3", ServerConfig.VTIO_REPOSITORY_KEY);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(soapRequestObject);

		callService(SOAP_ACTION, envelope);

		try {
			SoapObject result = (SoapObject) envelope.bodyIn;
			Log.v("COUNT", "result instances: " + result.getProperty(0));
			for (int i = 0; i < result.getPropertyCount(); i++) {
				SoapObject soapItem = (SoapObject) result.getProperty(i);
				PlaceDataSimple classItem = new PlaceDataSimple();
				String abstractInfo = soapItem.getProperty("abstractInfo")
						.toString();
				String address = soapItem.getProperty("address").toString();
				String imageURL = soapItem.getProperty("imageURL").toString();
				String label = soapItem.getProperty("label").toString();
				double latitude = 0.0f;
				double longitude = 0.0f;
				try {
					latitude = Double.parseDouble(soapItem.getProperty(
							"latitude").toString());
					longitude = Double.parseDouble(soapItem.getProperty(
							"longitude").toString());

				} catch (Exception e) {
					// TODO: handle exception
				}
				String location = soapItem.getProperty("location").toString();
				String phone = soapItem.getProperty("phone").toString();
				String type = soapItem.getProperty("type").toString();
				String uri = soapItem.getProperty("uri").toString();
				int ratingNum = 0;
				try {
					ratingNum = Integer.parseInt(soapItem.getProperty(
							"ratingNum").toString());
				} catch (Exception e) {
					// TODO: handle exception
				}
				boolean wellKnown = false;
				try {
					wellKnown = Boolean.parseBoolean(soapItem.getProperty(
							"wellKnown").toString());
				} catch (Exception e) {
					// TODO: handle exception
				}
				classItem.set(uri, label, abstractInfo, type, ratingNum,
						imageURL, wellKnown, address, phone, longitude,
						latitude);
				returnValues.add(classItem);

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (isCache && returnValues.size() > 0) {
			OntologyCache.placeInstance.put(key, returnValues);
		}
		return returnValues;
	}

	private void callService(String soapAction,
			SoapSerializationEnvelope envelope) {
		System.setProperty("http.keepAlive", "false");
		int counter = 10;
		boolean isError = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(
				ServerConfig.getWSDLURL());
		while (counter > 0 && isError) {
			isError = false;
			counter -= 1;
			try {
				androidHttpTransport.call("\"" + soapAction + "\"", envelope);
			} catch (Exception e) {
				isError = true;
				log.e("Exception");
				try {
					Thread.sleep(50);
				} catch (InterruptedException e1) {
					log.e("Interrupted Exception");
				}
			}
		}
	}

}
