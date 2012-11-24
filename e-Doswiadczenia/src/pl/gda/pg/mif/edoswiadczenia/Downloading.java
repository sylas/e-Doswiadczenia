/**
 * 
 */
package pl.gda.pg.mif.edoswiadczenia;

import android.R.menu;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.provider.Settings;
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
	
	private long prepareDownload() {
		tmp = mContext.getSystemService(mContext.DOWNLOAD_SERVICE);
		mang = (DownloadManager) tmp;
		rtmp = new DownloadManager.Request(Uri.parse(link));
		rtmp.setMimeType("apk");	
		Log.i(TAG,"Przygotowano dane do pobierania");
		return mang.enqueue(rtmp);		
	}

	private void checkDownload(long id) {
		systemRespond rec = new systemRespond(id);
		mContext.registerReceiver(rec, new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		Log.i(TAG,"Plik został pobrany");
	}

	private void install(long id) {
/*		int result = Settings.Secure.getInt(mContext.getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS, 0);
		if (result == 0) {
		    // show some dialog here
		    // ...
		    // and may be show application settings dialog manually
		    Intent intent = new Intent();
		    intent.setAction(Settings.ACTION_APPLICATION_SETTINGS);
		   mContext.startActivity(intent);
		}*/
		
		Intent install = new Intent(Intent.ACTION_VIEW);
		//"application/vnd.android.package-archive"
		Log.i(TAG, mang.getUriForDownloadedFile(id).toString());
		
		//install.setDataAndType(mang.getUriForDownloadedFile(id),mang.getMimeTypeForDownloadedFile(id));
		install.setDataAndType(mang.getUriForDownloadedFile(id),"application/vnd.android.package-archive");
		install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Log.i(TAG,"instalujemy");
		mContext.startActivity(install);		
	}

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

	class systemRespond extends BroadcastReceiver {

		public systemRespond() {
		}

		public systemRespond(long id) {
			idDown = id;
		}

		private long idDown;

		@Override
		public void onReceive(Context context, Intent intent) {
			long id = intent.getExtras().getLong(
					DownloadManager.EXTRA_DOWNLOAD_ID);
			if (idDown == id) {
				Log.i(TAG,"Za chwilę rozpocznie się instalacja.");
				install(id);
			}
			System.out.print("blehh, ");
		}

	}

}
