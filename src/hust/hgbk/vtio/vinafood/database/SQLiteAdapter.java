package hust.hgbk.vtio.vinafood.database;

import hust.hgbk.vtio.vinafood.R;
import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.config.log;
import hust.hgbk.vtio.vinafood.entities.Cookbook;
import hust.hgbk.vtio.vinafood.entities.Topic;
import hust.hgbk.vtio.vinafood.file.FileManager;
import hust.hgbk.vtio.vinafood.ontology.simple.ClassDataSimple;
import hust.hgbk.vtio.vinafood.ontology.simple.PlaceDataSimple;
import hust.hgbk.vtio.vinafood.vtioservice.FullDataInstance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class SQLiteAdapter extends SQLiteOpenHelper {
	private static SQLiteAdapter sqLiteAdapter;

	public static SQLiteAdapter getInstance(Context context) {
		if (sqLiteAdapter == null) {
			sqLiteAdapter = new SQLiteAdapter(context);
			sqLiteAdapter.checkAndCreateDatabase();
		}
		return sqLiteAdapter;
	}

	public static String databaseName = "amthucviet.sqlite";
	private static String DATABASE_PATH = "/data/data/hust.hgbk.vtio.vinafood/databases/";

	private SQLiteDatabase myDB;
	private final Context ctx;

	// private HashSet<String> favoriteUriSet;

	protected SQLiteAdapter(Context context) {
		super(context, databaseName, null, 1);
		this.ctx = context;
		// DATABASE_PATH = ctx.getFilesDir().getPath() + DATABASE_PATH;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public boolean isOpen() {
		if (myDB == null)
			return false;
		return myDB.isOpen();
	}

	public synchronized void close() {
		if (myDB != null && myDB.isOpen())
			myDB.close();
		super.close();
		// Log.v("DATABASE", "CLOSE");
	}

	public boolean checkAndCreateDatabase() {
		boolean dbExist = checkDataBase();
		if (dbExist) {
			Log.v("DATABASE", "Database exits");
			return true;
		} else {
			Log.v("DATABASE", "Not exits");
			try {
				try {
					this.getReadableDatabase();
					createFavoriteTable();
					createRecentViewTable();
					createPreferenceTable();
					Log.v("DATABASE", "Copy " + databaseName);
					return true;
				} catch (Exception e) {
					Log.v("DATABASE", "Not exits " + databaseName);
					return false;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
		}
	}

	public void createCuisineHelthTable() {
		try {
			openDataBase();
			executeSQL("CREATE  TABLE 'main'.'CuisineHelth' ('_id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,'imglink' TEXT NOT NULL, 'title' TEXT NOT NULL UNIQUE, 'description' TEXT, 'content' TEXT)");
			importDiscoveryHelthDbFromFile();
		} catch (Exception e) {
		} finally {
			close();
		}
	}

	public void createCookbookTable() {
		try {
			openDataBase();
			executeSQL("CREATE  TABLE 'main'.'Cookbook' ('_id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,'imglink' TEXT NOT NULL, 'title' TEXT NOT NULL UNIQUE, 'description' TEXT, 'content' TEXT)");
			importDiscoveryCookbookDbFromFile();
		} catch (Exception e) {
		} finally {
			close();
		}
	}

	private void createFavoriteTable() {
		try {
			openDataBase();
			executeSQL("CREATE  TABLE 'main'.'FavoritePlace' ('_id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , 'uri' TEXT NOT NULL UNIQUE, 'label' TEXT NOT NULL , 'type' TEXT, 'address' TEXT, 'abstract' TEXT, 'img' TEXT, 'welknown' TEXT, 'phone' TEXT, 'rating' INTEGER, 'longitude' DOUBLE, 'latitude' DOUBLE)");
		} catch (Exception e) {
		} finally {
			close();
		}
	}

	private void createRecentViewTable() {
		try {
			openDataBase();
			executeSQL("CREATE  TABLE 'main'.'RecentPlace' ('_id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , 'uri' TEXT NOT NULL UNIQUE, 'label' TEXT NOT NULL , 'type' TEXT, 'address' TEXT, 'abstract' TEXT, 'img' TEXT, 'welknown' TEXT, 'phone' TEXT, 'rating' INTEGER, 'longitude' DOUBLE, 'latitude' DOUBLE)");
		} catch (Exception e) {
		} finally {
			close();
		}
	}

	private void createPreferenceTable() {
		try {
			openDataBase();
			executeSQL("CREATE  TABLE 'main'.'Preference' ('_id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , 'uri' TEXT NOT NULL , 'label' TEXT NOT NULL )");
		} catch (Exception e) {
		} finally {
			close();
		}
	}

	public void addPlaceToFavoriteTable(PlaceDataSimple placeDataSimple) {
		try {
			openDataBase();
			// favoriteUriSet.add(placeDataSimple.getURI());
			executeSQL("INSERT INTO 'main'.'FavoritePlace' ('uri','label','type','address','abstract','img','welknown','phone','rating','longitude','latitude') VALUES ('"
					+ placeDataSimple.getURI().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getLabel().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getType().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getAddress().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getHasAbstract().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getImageURL().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.isWellknown()
					+ "','"
					+ placeDataSimple.getPhoneNumber().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getHasRating()
					+ "','"
					+ placeDataSimple.getLongtitude()
					+ "','"
					+ placeDataSimple.getLatitude() + "')");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	public void importDiscoveryHelthDbFromFile() {
		log.m("Start import");
		byte[] data = FileManager.loadFromRaw(R.raw.dining, ctx);
		ByteBuffer buffer = ByteBuffer.wrap(data);
		Topic topic = new Topic();
		while (buffer.hasRemaining()) {
			int imgLen = buffer.getInt();
			byte[] imgLinkData = new byte[imgLen];
			buffer.get(imgLinkData);
			topic.imgLink = new String(imgLinkData);
			int tLen = buffer.getInt();
			byte[] ttData = new byte[tLen];
			buffer.get(ttData);
			topic.title = new String(ttData);
			int desLen = buffer.getInt();
			byte[] desData = new byte[desLen];
			buffer.get(desData);
			topic.description = new String(desData);
			int contentLen = buffer.getInt();
			byte[] ctData = new byte[contentLen];
			buffer.get(ctData);
			topic.content = new String(ctData);
			addCuisineHelthTopic(topic);
		}
		log.m("Finish import");
	}

	public void importDiscoveryCookbookDbFromFile() {
		log.m("Start import");
		byte[] data = FileManager.loadFromRaw(R.raw.vaobep, ctx);
		ByteBuffer buffer = ByteBuffer.wrap(data);
		Cookbook topic = new Cookbook();
		while (buffer.hasRemaining()) {
			int imgLen = buffer.getInt();
			byte[] imgLinkData = new byte[imgLen];
			buffer.get(imgLinkData);
			topic.imgLink = new String(imgLinkData);
			int tLen = buffer.getInt();
			byte[] ttData = new byte[tLen];
			buffer.get(ttData);
			topic.title = new String(ttData);
			int desLen = buffer.getInt();
			byte[] desData = new byte[desLen];
			buffer.get(desData);
			topic.description = new String(desData);
			int contentLen = buffer.getInt();
			byte[] ctData = new byte[contentLen];
			buffer.get(ctData);
			topic.content = new String(ctData);
			addCookbookTopic(topic);
		}
		log.m("Finish import");
	}

	public void addPlaceToFavoriteTable(FullDataInstance placeDataSimple) {
		try {
			openDataBase();
			// favoriteUriSet.add(placeDataSimple.getUri());
			executeSQL("INSERT INTO 'main'.'FavoritePlace' ('uri','label','type','address','abstract','img','welknown','phone','rating','longitude','latitude') VALUES ('"
					+ placeDataSimple.getUri().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getLabel().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getType().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getAddress().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getAbstractInfo().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getImageURL().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getWellKnown()
					+ "','"
					+ placeDataSimple.getPhone().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getRatingNum()
					+ "','"
					+ placeDataSimple.getLongitude()
					+ "','"
					+ placeDataSimple.getLatitude() + "')");
		} catch (Exception e) {
		} finally {
			close();
		}
	}

	public void addClassToPreferenceTable(ClassDataSimple classDataSimple) {
		try {
			openDataBase();
			executeSQL("INSERT INTO 'main'.'Preference' ('uri','label') VALUES ('"
					+ classDataSimple.getUri().replaceAll("'", "''")
					+ "','"
					+ classDataSimple.getLabel().replaceAll("'", "''") + "')");
		} catch (Exception e) {
		} finally {
			close();
		}
	}

	public void addPlaceToRecentViewTable(FullDataInstance placeDataSimple) {
		try {
			openDataBase();
			// favoriteUriSet.add(placeDataSimple.getUri());
			executeSQL("INSERT INTO 'main'.'RecentPlace' ('uri','label','type','address','abstract','img','welknown','phone','rating','longitude','latitude') VALUES ('"
					+ placeDataSimple.getUri().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getLabel().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getType().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getAddress().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getAbstractInfo().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getImageURL().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getWellKnown()
					+ "','"
					+ placeDataSimple.getPhone().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getRatingNum()
					+ "','"
					+ placeDataSimple.getLongitude()
					+ "','"
					+ placeDataSimple.getLatitude() + "')");
		} catch (Exception e) {
		} finally {
			close();
		}
	}

	public void addPlaceToRecentViewTable(PlaceDataSimple placeDataSimple) {
		try {
			openDataBase();
			// favoriteUriSet.add(placeDataSimple.getURI());
			executeSQL("INSERT INTO 'main'.'RecentPlace' ('uri','label','type','address','abstract','img','welknown','phone','rating','longitude','latitude') VALUES ('"
					+ placeDataSimple.getURI().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getLabel().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getType().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getAddress().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getHasAbstract().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getImageURL().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.isWellknown()
					+ "','"
					+ placeDataSimple.getPhoneNumber().replaceAll("'", "''")
					+ "','"
					+ placeDataSimple.getHasRating()
					+ "','"
					+ placeDataSimple.getLongtitude()
					+ "','"
					+ placeDataSimple.getLatitude() + "')");
		} catch (Exception e) {
		} finally {
			close();
		}
	}

	public void addCuisineHelthTopic(Topic topic) {
		try {
			openDataBase();
			executeSQL("INSERT INTO 'main'.'CuisineHelth' ('imglink','title','description','content') VALUES('"
					+ topic.imgLink
					+ "','"
					+ topic.title
					+ "','"
					+ topic.description + "','" + topic.content + "')");
			log.m("Add: " + topic.title);
		} catch (Exception e) {
		} finally {
			close();
		}
	}

	public void addCookbookTopic(Cookbook topic) {
		try {
			openDataBase();
			executeSQL("INSERT INTO 'main'.'Cookbook' ('imglink','title','description','content') VALUES('"
					+ topic.imgLink
					+ "','"
					+ topic.title
					+ "','"
					+ topic.description + "','" + topic.content + "')");
			log.m("Add: " + topic.title);
		} catch (Exception e) {
		} finally {
			close();
		}
	}

	public void deletePlaceFromFavoriteTable(String uri) {
		try {
			openDataBase();
			executeSQL("DELETE FROM 'main'.'FavoritePlace' WHERE uri='" + uri
					+ "'");
		} catch (Exception e) {
		} finally {
			close();
		}
	}

	public void deleteClassFromPreferenceTable(String uri) {
		try {
			openDataBase();
			executeSQL("DELETE FROM 'main'.'Preference' WHERE uri='" + uri
					+ "'");
		} catch (Exception e) {
		} finally {
			close();
		}
	}

	public void deletePlaceFromRecentViewTable(String uri) {
		try {
			openDataBase();
			executeSQL("DELETE FROM 'main'.'RecentPlace' WHERE uri='" + uri
					+ "'");
		} catch (Exception e) {
		} finally {
			close();
		}
	}

	public boolean isAFavoritePlace(String uri) {
		try {
			openDataBase();
			// if (myDB!= null && myDB.isOpen()){
			Cursor sugCursor = rawQuery(" SELECT DISTINCT uri FROM 'main'.'FavoritePlace' "
					+ "where uri = '" + uri + "'");
			((Activity) ctx).startManagingCursor(sugCursor);
			int i = 0;
			if (sugCursor.moveToFirst()) {
				do {
					Cursor cur = sugCursor;
					i++;
				} while (sugCursor.moveToNext());
			}
			((Activity) ctx).stopManagingCursor(sugCursor);
			if (i > 0)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

	public boolean isARecentPlace(String uri) {
		try {
			openDataBase();
			Cursor cursor = rawQuery("SELECT DISTINCT uri FROM 'main'.'RecentPlace' where uri = '"
					+ uri + "'");
			((Activity) ctx).startManagingCursor(cursor);
			int i = 0;
			if (cursor.moveToFirst()) {
				do {
					Cursor cur = cursor;
					i++;
				} while (cursor.moveToNext());
			}
			((Activity) ctx).stopManagingCursor(cursor);
			if (i > 0)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

	public int topicCount() {
		try {
			openDataBase();
			Cursor cursor = rawQuery("SELECT COUNT(DISTINCT _id) FROM 'main'.'CuisineHelth'");
			((Activity) ctx).startManagingCursor(cursor);
			if (cursor.moveToFirst()) {
				Cursor cur = cursor;
				int numberTopic = cur.getInt(0);
				return numberTopic;
			}
		} catch (Exception e) {
		}
		return 0;
	}

	public int cookbookCount() {
		try {
			openDataBase();
			Cursor cursor = rawQuery("SELECT COUNT(DISTINCT _id) FROM 'main'.'Cookbook'");
			((Activity) ctx).startManagingCursor(cursor);
			if (cursor.moveToFirst()) {
				Cursor cur = cursor;
				int numberTopic = cur.getInt(0);
				return numberTopic;
			}
		} catch (Exception e) {
		}
		return 0;
	}

	public ArrayList<Topic> getTopics(int limit, int offset) {
		ArrayList<Topic> returnList = new ArrayList<Topic>();
		try {
			openDataBase();
			Cursor cursor = rawQuery("SELECT imglink,title,description,content FROM 'main'.'CuisineHelth' LIMIT "
					+ limit + " OFFSET " + offset);
			((Activity) ctx).startManagingCursor(cursor);
			if (cursor.moveToFirst()) {
				do {
					Cursor cur = cursor;
					try {
						Topic p = new Topic();
						p.imgLink = cur.getString(0);
						p.title = cur.getString(1);
						p.description = cur.getString(2);
						p.content = cur.getString(3);
						returnList.add(p);
					} catch (Exception e) {
					}

				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			close();
		}
		return returnList;
	}

	public ArrayList<Cookbook> getCookbookTopics(int limit, int offset) {
		ArrayList<Cookbook> returnList = new ArrayList<Cookbook>();
		try {
			openDataBase();
			Cursor cursor = rawQuery("SELECT imglink,title,description,content FROM 'main'.'Cookbook' LIMIT "
					+ limit + " OFFSET " + offset);
			((Activity) ctx).startManagingCursor(cursor);
			if (cursor.moveToFirst()) {
				do {
					Cursor cur = cursor;
					try {
						Cookbook p = new Cookbook();
						p.imgLink = cur.getString(0);
						p.title = cur.getString(1);
						p.description = cur.getString(2);
						p.content = cur.getString(3);
						returnList.add(p);
					} catch (Exception e) {
					}

				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			close();
		}
		return returnList;
	}

	public String[] getAllHelthTopic() {
		ArrayList<String> returnList = new ArrayList<String>();
		try {
			openDataBase();
			Cursor cursor = rawQuery("SELECT title FROM 'main'.'CuisineHelth'");
			((Activity) ctx).startManagingCursor(cursor);
			if (cursor.moveToFirst()) {
				do {
					Cursor cur = cursor;
					try {
						returnList.add(cur.getString(0));
					} catch (Exception e) {
					}

				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			close();
		}
		String[] topics = new String[returnList.size()];
		return returnList.toArray(topics);
	}

	public Topic getTopicWithName(String title) {
		try {
			openDataBase();
			Cursor cursor = rawQuery("SELECT imglink,title,description,content FROM 'main'.'CuisineHelth' WHERE title='"
					+ title + "'");
			((Activity) ctx).startManagingCursor(cursor);
			if (cursor.moveToFirst()) {
				do {
					Cursor cur = cursor;
					try {
						Topic p = new Topic();
						p.imgLink = cur.getString(0);
						p.title = cur.getString(1);
						p.description = cur.getString(2);
						p.content = cur.getString(3);
						return p;
					} catch (Exception e) {
					}

				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			close();
		}
		return null;
	}

	public Cookbook getCookbookTopicWithName(String title) {
		try {
			openDataBase();
			Cursor cursor = rawQuery("SELECT imglink,title,description,content FROM 'main'.'Cookbook' WHERE title='"
					+ title + "'");
			((Activity) ctx).startManagingCursor(cursor);
			if (cursor.moveToFirst()) {
				do {
					Cursor cur = cursor;
					try {
						Cookbook p = new Cookbook();
						p.imgLink = cur.getString(0);
						p.title = cur.getString(1);
						p.description = cur.getString(2);
						p.content = cur.getString(3);
						return p;
					} catch (Exception e) {
					}

				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			close();
		}
		return null;
	}

	public String[] getAllCookbookTopic() {
		ArrayList<String> returnList = new ArrayList<String>();
		try {
			openDataBase();
			Cursor cursor = rawQuery("SELECT title FROM 'main'.'Cookbook'");
			((Activity) ctx).startManagingCursor(cursor);
			if (cursor.moveToFirst()) {
				do {
					Cursor cur = cursor;
					try {
						returnList.add(cur.getString(0));
					} catch (Exception e) {
					}

				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
		} finally {
			close();
		}
		String[] topics = new String[returnList.size()];
		return returnList.toArray(topics);
	}

	@SuppressWarnings("deprecation")
	public FullDataInstance[] getAllFavoritePlace(int limit, int offset) {
		ArrayList<FullDataInstance> returnList = new ArrayList<FullDataInstance>();
		try {
			openDataBase();
			Cursor sugCursor = rawQuery("SELECT uri,label,type,address,abstract,img,welknown,phone,rating,longitude,latitude FROM 'main'.'FavoritePlace' LIMIT "
					+ limit + " OFFSET " + offset);
			((Activity) ctx).startManagingCursor(sugCursor);
			if (sugCursor.moveToFirst()) {
				do {
					Cursor cur = sugCursor;
					try {
						FullDataInstance p = new FullDataInstance();
						p.createData(cur.getString(0), cur.getString(1),
								cur.getString(4), cur.getString(2),
								cur.getInt(8), cur.getString(5),
								cur.getString(6), cur.getString(3),
								cur.getString(7), cur.getDouble(9),
								cur.getDouble(10));
						returnList.add(p);
					} catch (Exception e) {
						// TODO: handle exception
					}

				} while (sugCursor.moveToNext());
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			close();
		}
		FullDataInstance[] fullDataInstances = new FullDataInstance[returnList
				.size()];
		return returnList.toArray(fullDataInstances);
	}

	public FullDataInstance[] getAllRecentViewPlace(int limit, int offset) {
		ArrayList<FullDataInstance> returnList = new ArrayList<FullDataInstance>();
		try {
			openDataBase();
			Cursor sugCursor = rawQuery("SELECT uri,label,type,address,abstract,img,welknown,phone,rating,longitude,latitude FROM 'main'.'RecentPlace' LIMIT "
					+ limit + " OFFSET " + offset);
			((Activity) ctx).startManagingCursor(sugCursor);
			if (sugCursor.moveToFirst()) {
				do {
					Cursor cur = sugCursor;
					try {
						FullDataInstance p = new FullDataInstance();
						p.createData(cur.getString(0), cur.getString(1),
								cur.getString(4), cur.getString(2),
								cur.getInt(8), cur.getString(5),
								cur.getString(6), cur.getString(3),
								cur.getString(7), cur.getDouble(9),
								cur.getDouble(10));
						returnList.add(p);
					} catch (Exception e) {
						// TODO: handle exception
					}

				} while (sugCursor.moveToNext());
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			close();
		}
		FullDataInstance[] fullDataInstances = new FullDataInstance[returnList
				.size()];
		return returnList.toArray(fullDataInstances);
	}

	@SuppressWarnings("deprecation")
	public ArrayList<ClassDataSimple> getAllPreferenceClass() {
		ArrayList<ClassDataSimple> returnList = new ArrayList<ClassDataSimple>();
		try {
			openDataBase();
			Cursor sugCursor = rawQuery("SELECT uri,label FROM 'main'.'Preference'");
			((Activity) ctx).startManagingCursor(sugCursor);
			if (sugCursor.moveToFirst()) {
				do {
					Cursor cur = sugCursor;
					try {
						ClassDataSimple p = new ClassDataSimple();
						p.setUri(cur.getString(0));
						p.setLabel(cur.getString(1));
						returnList.add(p);
					} catch (Exception e) {
						// TODO: handle exception
					}

				} while (sugCursor.moveToNext());
			}
			close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return returnList;
	}

	public static boolean checkDataBase(String dbName) {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DATABASE_PATH + dbName;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
			// Log.v("DATABASE", "OPEN CHECK");
		} catch (SQLiteException e) {
		}
		if (checkDB != null) {
			checkDB.close();
			// Log.v("DATABASE", "CLOSE CHECK");
		}

		return checkDB != null ? true : false;
	}

	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DATABASE_PATH + databaseName;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
			// Log.v("DATABASE", "OPEN CHECK");
		} catch (SQLiteException e) {
			log.e("Khong the mo duoc co so du lieu. CSDL chua ton tai!");
		}
		if (checkDB != null) {
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	private void copyDataBase() throws Exception {
		InputStream myInput = ctx.getAssets().open(databaseName);

		String outFileName = DATABASE_PATH + databaseName;
		OutputStream myOutput = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];
		int length;
		int i = 0;

		while ((length = myInput.read(buffer)) > 0) {

			i++;
			myOutput.write(buffer, 0, length);

		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void openDataBase() throws Exception {
		if (myDB == null || (myDB != null && !myDB.isOpen())) {
			String myPath = DATABASE_PATH + databaseName;
			myDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
		}

	}

	public Cursor rawQuery(String query) throws Exception {
		return myDB.rawQuery(query, null);
	}

	public void executeSQL(String query) throws Exception {
		myDB.execSQL(query);
	}

	public ArrayList<String> getValue(String tableName) {
		ArrayList<String> listString = new ArrayList<String>();
		try {
			openDataBase();
			Cursor sugCursor = myDB.rawQuery(" SELECT DISTINCT Value FROM "
					+ tableName + " WHERE Language = '"
					+ ServerConfig.LANGUAGE_CODE + "'", null);
			((Activity) ctx).startManagingCursor(sugCursor);
			if (sugCursor.moveToFirst()) {
				do {
					Cursor cur = sugCursor;
					listString.add(cur.getString(0));

				} while (sugCursor.moveToNext());
			}
			close();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return listString;
	}

	public void insertValue(String tableName, String value) {
		try {
			openDataBase();
			myDB.execSQL("INSERT INTO '" + tableName
					+ "' (Value,Language) VALUES ('" + value + "','"
					+ ServerConfig.LANGUAGE_CODE + "')");

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			close();
		}
	}

	public void insertValue(String tableName,
			ArrayList<ArrayList<String>> listValue) {
		try {
			openDataBase();
			for (int i = 0; i < listValue.size(); i++) {
				String s = listValue.get(i).get(0);
				s = s.replace("@" + ServerConfig.LANGUAGE_CODE, "");
				s = s.replace("(", ":");
				s = s.split(":")[0];
				myDB.execSQL("INSERT INTO '" + tableName
						+ "' (Value,Language) VALUES ('" + s + "','"
						+ ServerConfig.LANGUAGE_CODE + "')");
			}

			close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// public boolean getStatus(String tableName){
	// boolean status = false;
	// try {
	// openDataBase();
	// Cursor sugCursor =
	// myDB.rawQuery(" SELECT Status FROM Status WHERE TableName = '" +
	// tableName + "' ",null);
	// ((Activity) ctx).startManagingCursor(sugCursor);
	// if (sugCursor.moveToFirst()) {
	// do {
	// Cursor cur = sugCursor;
	// String s = cur.getString(0);
	// if (!s.equals("FALSE")){
	// status = true;
	// }
	//
	// } while (sugCursor.moveToNext());
	// }
	// close();
	// } catch (Exception e) {
	// Log.v("TEST", "getStatus Exception");
	// }
	// return status;
	// }

	public ArrayList<ArrayList<String>> getConceptWithIcon() {
		ArrayList<ArrayList<String>> listConceptWithIcon = new ArrayList<ArrayList<String>>();
		try {
			openDataBase();
			Cursor sugCursor = myDB.rawQuery(
					" SELECT DISTINCT uri, icon, label FROM ConceptWithIcon WHERE lang = '"
							+ ServerConfig.LANGUAGE_CODE + "'", null);
			((Activity) ctx).startManagingCursor(sugCursor);
			if (sugCursor.moveToFirst()) {

				do {
					ArrayList<String> concept = new ArrayList<String>();
					Cursor cur = sugCursor;
					concept.add(cur.getString(0));
					concept.add(cur.getString(1));
					concept.add(cur.getString(2));
					listConceptWithIcon.add(concept);
				} while (sugCursor.moveToNext());

			}
			close();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return listConceptWithIcon;
	}

	public void insertListConcepts(
			ArrayList<ArrayList<String>> listConceptWithIcon) {
		try {
			openDataBase();
			for (int i = 0; i < listConceptWithIcon.size(); i++) {
				String uri = listConceptWithIcon.get(i).get(0);
				String icon = listConceptWithIcon.get(i).get(1);
				String label = listConceptWithIcon.get(i).get(2);
				label = label.replace("@" + ServerConfig.LANGUAGE_CODE, "");

				myDB.execSQL("INSERT INTO 'ConceptWithIcon' (uri,icon,label,lang) VALUES ('"
						+ uri
						+ "','"
						+ icon
						+ "','"
						+ label
						+ "','"
						+ ServerConfig.LANGUAGE_CODE + "')");
			}

			close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static boolean hasSDCard() {

		File root = Environment.getExternalStorageDirectory();

		return (root.exists() && root.canWrite());

	}

}
