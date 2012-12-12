/**
 * 
 */
package pl.gda.pg.mif.edoswiadczenia;

import java.io.File;
import java.util.List;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;


/**
 * @author maja
 * 
 */

public class Downloading {

	private Object tmp;
	private DownloadManager mang;
	private String link = "http://download.macromedia.com/pub/flashplayer/"
			+ "installers/archive/android/11.1.115.27/install_flash_player_ics.apk";
	private DownloadManager.Request rtmp;
	private Context mContext;
	//private static final String TAG = "MyActivity";
	private systemRespond rec;
	
	private String externalStorageState;
	File externalPath;

	BroadcastReceiver mExternalStorageReceiver;
	boolean mExternalStorageAvailable = false;
	boolean mExternalStorageWriteable = false;
	private final String flashPackageName = "com.adobe.flashplayer";
	private final InfoForUser notificationDialog = new InfoForUser();
	
	
	public Downloading(Context c){
		mContext = c;
	}
		

	void checkExternalStorageState() {
		externalStorageState = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(externalStorageState)) {
	        mExternalStorageAvailable = mExternalStorageWriteable = true;
	        externalPath = Environment.getExternalStorageDirectory();
	        if(!externalPath.exists()){
	        	externalPath.mkdirs();
	        	} 
	        }
	    else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(externalStorageState)) {
	        mExternalStorageAvailable = true;
	        mExternalStorageWriteable = false;
	        //przekazanie informacji użytkownikowi
	        notificationDialog.pushInfoToUser(0,10);
	        }
	    else {
	        mExternalStorageAvailable = mExternalStorageWriteable = false;
	      //przekazanie informacji użytkownikowi
	        notificationDialog.pushInfoToUser(1,10);
	        }
	}

	void  startWatchingExternalStorage(){ 
		mExternalStorageReceiver = new BroadcastReceiver() {
		        @Override
		        public void onReceive(Context context, Intent intent) {
		            //Log.i("test", "Storage: " + intent.getData());
		            checkExternalStorageState();
		        }
		    };
		    IntentFilter filter = new IntentFilter();
		    
		    filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		    filter.addAction(Intent.ACTION_MEDIA_REMOVED);
		    mContext.registerReceiver(mExternalStorageReceiver, filter);
		    checkExternalStorageState();

	}
	
	void stopWatchingExternalStorage() {
		mContext.unregisterReceiver(mExternalStorageReceiver);
	}
	
	//bezparametrowa funkcja sprawdzająca czy idtnieje potrzeba instalowania flash playera na tablecie
	boolean checkIfFlashExixts(){
		
		boolean status = false;
		List<PackageInfo> mPackages;
		PackageManager mPm = mContext.getPackageManager(); 
		int iter = 0;
		mPackages = mPm.getInstalledPackages(0);
		while (!status && iter < mPackages.size() ) {
			status = mPackages.get(iter).packageName.equals(flashPackageName);
			iter++;
		}
		// for(PackageInfo item : mPm.getInstalledPackages(0))
		//	if(status = item.packageName.equals(flashPackageName))
		//		break;

		return status;
	}

	
	/* 
	 * bezparametrowa funkcja przygotowująca obiekt z danymi o pobieranym pliku,
	 * uruchamiająca systemową usługę pobierania, 
	 * oraz rozpoczynająca pobieranie pliku		
	 */
	private long prepareDownload() {
		tmp = mContext.getSystemService(Context.DOWNLOAD_SERVICE);
		mang = (DownloadManager) tmp;
		rtmp = new DownloadManager.Request(Uri.parse(link));
		
		startWatchingExternalStorage();
		if(mExternalStorageAvailable){
			rtmp.setDestinationInExternalPublicDir("DIRECTORY_DOWNLOADS","install_flash_player_ics.apk");	
		}
		rtmp.allowScanningByMediaScanner();
		rtmp.setMimeType("apk");	
		//Log.i(TAG,"Przygotowano dane do pobierania");
		stopWatchingExternalStorage();
		return mang.enqueue(rtmp);
	}

	/*
	 * funkcja nasłuchująca, jako parametr pobiera nr porządkowy ściągnięcia pliku
	 * wypustka odbierająca informacje o zdarzeniach systemowych 
	 * i filtrująca je pod kątem zakończenia pobierania plików
	 */
	private void checkDownload(long id) {
		rec = new systemRespond(id);
		mContext.registerReceiver(rec, new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}
		
	/*
	 * funkcja instalująca pobrany plik, jako parametr pobiera nr porządkowy ściągnięcia pliku
	 * rejestruje nowy "zamiar",
	 * przekazuje dane pobranego pliku  i rozpoczyna jego instalację
	 */
	private void install(long id) {				
		Intent install = new Intent(Intent.ACTION_INSTALL_PACKAGE);		
		//Log.i(TAG, mang.getUriForDownloadedFile(id).toString());
		install.setDataAndType(mang.getUriForDownloadedFile(id),"application/vnd.android.package-archive");
		install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.grantUriPermission("pl.gda.pg.mif.edoswiadczenia",mang.getUriForDownloadedFile(id), Intent.FLAG_GRANT_READ_URI_PERMISSION);
		//Log.i(TAG,"instalujemy");
		mContext.startActivity(install);
		afterInstall();
	}

	/*
	 * zamknięcie kodu w jednym wywołaniu
	 */
	public void downloadFlash() {
		checkDownload(prepareDownload());
	}
	
	//funkcja sprzątająca
	void afterInstall() {
		mContext.unregisterReceiver(rec);
	}
	
	/*
	 * klasa systemRespond jest klasą wewnętrzną klasy Downloading
	 * zapewnienie dostępu do danych przechowywanych w obiekcie Downloading
	 */
	class systemRespond extends BroadcastReceiver {

		private long idDown;
		
		public systemRespond() {
		}
		/*
		 * konstruktor parametrowy pobierający nr porządkowy ściągnięcia pliku
		 */
		public systemRespond(long id) {
			idDown = id;
		}

		/*
		 * (non-Javadoc)
		 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
		 * funkcja dokonująca porównania czy odebrana informacja o zdarzeniu systemowym jest pożądana
		 * jeżeli test przebiegnie pomyślnie zostaje wywołana funkcja instalująca pobrany plik.
		 */
		@Override
		public void onReceive(Context context, Intent intent) {
			long id = intent.getExtras().getLong(
					DownloadManager.EXTRA_DOWNLOAD_ID);
			//Log.i("test", "Storage: " + intent.getData());
			if (idDown == id) {
				//Log.i(TAG,"Za chwilę rozpocznie się instalacja.");
				install(id);
			}
		}
	
	}
}