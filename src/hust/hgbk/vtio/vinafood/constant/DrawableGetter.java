package hust.hgbk.vtio.vinafood.constant;

import hust.hgbk.vtio.vinafood.R;

public class DrawableGetter {
	public static int getDrawable(String typeOfPlace) {
		if (typeOfPlace.equals("Bar")) {
			return R.drawable.bar;
		} else if (typeOfPlace.equals("Quán ăn vỉa hè")) {
			return R.drawable.side_walk_food;
		} else if (typeOfPlace.equals("Nhà hàng")) {
			return R.drawable.restaurant;
		} else if (typeOfPlace.equals("Quán ăn bình dân")) {
			return R.drawable.popular_restaurant;
		} else if (typeOfPlace.equals("Nhà hàng sang trọng")) {
			return R.drawable.luxurious_restaurant;
		} else if (typeOfPlace.equals("Quán kem")) {
			return R.drawable.ice_cream_shop;
		} else if (typeOfPlace.equals("Quán ăn nhanh")) {
			return R.drawable.fastfood;
		} else if (typeOfPlace.equals("Quán cà phê")) {
			return R.drawable.cafe;
		} else if (typeOfPlace.equals("Quán bia")) {
			return R.drawable.beer;
		} else if (typeOfPlace.equals("KFC")) {
			return R.drawable.kfc;
		} else {
			return R.drawable.green_end;
		}
	}
}
