package hust.hgbk.vtio.vinafood.constant;

import hust.hgbk.vtio.vinafood.ontology.simple.ClassDataSimple;
import hust.hgbk.vtio.vinafood.ontology.simple.PropertyDataSimple;
import hust.hgbk.vtio.vinafood.vtioservice.VtioCoreService;

import java.util.ArrayList;

public class Ontology {
	public static VtioCoreService service = new VtioCoreService();
	public static ArrayList<ClassDataSimple> allClass = service.getAllClassSimple();
	public static ArrayList<PropertyDataSimple> allDataProperties = service.getAllDataProperty();
	public static ArrayList<PropertyDataSimple> allObjectProperties = service.getAllObjectProperty();
}
