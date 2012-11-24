/**
 * 
 */
package pl.gda.pg.mif.edoswiadczenia;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;

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
	private static final String TAG = "MyActivity";
	
	public Downloading(Context c){
		mContext = c;
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
		rtmp.setMimeType("apk");	
		Log.i(TAG,"Przygotowano dane do pobierania");
		return mang.enqueue(rtmp);		
	}

	/*
	 * funkcja nasłuchująca, jako parametr pobiera nr porządkowy ściągnięcia pliku
	 * wypustka odbierająca informacje o zdarzeniach systemowych 
	 * i filtrująca je pod kątem zakończenia pobierania plików
	 */
	private void checkDownload(long id) {
		systemRespond rec = new systemRespond(id);
		mContext.registerReceiver(rec, new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		Log.i(TAG,"Plik został pobrany");
	}

	
	
	/*
	 * funkcja instalująca pobrany plik, jako parametr pobiera nr porządkowy ściągnięcia pliku
	 * rejestruje nowy "zamiar",
	 * przekazuje dane pobranego pliku  i rozpoczyna jego instalację
	 */
	private void install(long id) {
		Intent install = new Intent(Intent.ACTION_VIEW);
		//"application/vnd.android.package-archive"
		Log.i(TAG, mang.getUriForDownloadedFile(id).toString());
		
		//install.setDataAndType(mang.getUriForDownloadedFile(id),mang.getMimeTypeForDownloadedFile(id));
		install.setDataAndType(mang.getUriForDownloadedFile(id),"application/vnd.android.package-archive");
		install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Log.i(TAG,"instalujemy");
		mContext.startActivity(install);		
	}

	/*
	 * zamknięcie kodu w jednym wywołaniu
	 */
	public void downloadFlash() {
		checkDownload(prepareDownload());
	}

	
	/*
	 * public class installedApps extends Activity { private ApplicationInfo
	 * downFlashInfo; private ApplicationInfo instalFlashInfo; private
	 * PackageManager pMang;
	 * 
	 * private installedApps(ApplicationInfo info){ downFlashInfo = info; }
	 * 
	 * public boolean checkInst(){ try{ instalFlashInfo =
	 * pMang.getApplicationInfo
	 * (downFlashInfo.packageName,PackageManager.GET_UNINSTALLED_PACKAGES );
	 * if(instalFlashInfo.equals(downFlashInfo)){ return true; } return true; }
	 * catch(PackageManager.NameNotFoundException nameExp){ return false; } } }
	 */

	/*
	 * klasa systemRespond jest klasą wewnętrzną klasy Downloading
	 * zapewnienie dostępu do danych przechowywanych w obiekcie Downloading
	 */
	class systemRespond extends BroadcastReceiver {


		public systemRespond() {
		}
		/*
		 * konstruktor parametrowy pobierający nr porządkowy ściągnięcia pliku
		 */
		public systemRespond(long id) {
			idDown = id;
		}

		private long idDown;

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
			if (idDown == id) {
				Log.i(TAG,"Za chwilę rozpocznie się instalacja.");
				install(id);
			}
			Log.i(TAG,"Wykonało się.");
		}

	}

}
