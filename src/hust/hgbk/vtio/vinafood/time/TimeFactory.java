package hust.hgbk.vtio.vinafood.time;

import java.util.Date;

public class TimeFactory {
	public static String GET_CURRENT_TIME(){
		Date date = new Date();
		return "CURRENT TIME = Day "+date.getDay()+": "+date.getHours()+" Hours :  "+date.getMinutes()+" Minutes : "+date.getSeconds()+" Seconds";
	}
}
