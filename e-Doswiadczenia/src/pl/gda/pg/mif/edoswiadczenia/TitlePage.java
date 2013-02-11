package pl.gda.pg.mif.edoswiadczenia;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Strona tytulowa aplikacji.
 *
 * @author sylas
 */
public class TitlePage extends Activity {

	private SimpleOnGestureListener myGestureScanner;
	//private GestureDetector myGestureScanner;
	private String wwwAddress;
	DisplayMetrics mDisplayMetrics;
	long eventTime;
	private final int MIN_SCREEN_WIDTH = 1280;
	private final int MIN_SCREEN_HEIGHT = 752;
	public static final String EN_FLAG = "en";
	public static final String PL_FLAG = "pl";
	public static NanoHTTPD nanoHTTPD;

	public static Boolean updateDone = false;
	public static final String ED_REMOTE_REPOSITORY = "http://e-doswiadczenia.mif.pg.gda.pl/files/ed-android-repo/";
	public static final String PREFS_UPDATE_SUFFIX = "_update";
	public static final String PREFS_DATE_MODF_SUFFIX = "_date";
	
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Sprawdzamy rozdzielczość - musi być co najmniej 1280x752 px (z uwzględnieniem status baru - 800)
		mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		
		
		if (mDisplayMetrics.widthPixels < MIN_SCREEN_WIDTH || mDisplayMetrics.heightPixels < MIN_SCREEN_HEIGHT) {
			Toast.makeText(getApplicationContext(), getString(R.string.msg_screen_not_supported), Toast.LENGTH_LONG).show();
			finish();
		}

		setContentView(R.layout.title_page);
         
		File NanoHTTPDserverRoot = new File(Environment.getExternalStorageDirectory().
				getAbsolutePath() + File.separator + ListED.ED_SDCARD_DIR);
		try {
			// Start serwera WWW i losowanie portu  
			
			nanoHTTPD = new NanoHTTPD(0, NanoHTTPDserverRoot);		
		} 
		//ewentualne błędy podczas tworzenia socket
		catch (IOException ioe) {
			Toast.makeText(getApplicationContext(), getString(R.string.msg_title_error), Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), getString(R.string.msg_internal_error001), Toast.LENGTH_LONG).show();       
			Toast.makeText(getApplicationContext(), ioe.getMessage(), Toast.LENGTH_LONG).show();
		}

			new CheckForEDUpdates().execute(EdFileNames.edName);

		
		//myGestureScanner = new GestureDetector(this);
		myGestureScanner = new GestureDetector.SimpleOnGestureListener(){
		
			@Override
			public boolean onDown(MotionEvent e) {
				return true;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				return true;
			}

			@Override
			public void onLongPress(MotionEvent e) {
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
				return true;
			}

			@Override
			public void onShowPress(MotionEvent e) {
			}


			// Obsluga tapniecia na ekranie
			//onSingleTapUp
			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				
				String tresc = "";
				String tytul = "";
				int photo1 = 0;
				int photo2 = 0;
				int photo3 = 0;
				int photo4 = 0;
				
				getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
				Float xRel = e.getX() / mDisplayMetrics.widthPixels;
				Float yRel = e.getY() / mDisplayMetrics.heightPixels;

				// Gorne logotypy
				if (yRel < 0.21 && xRel > 0.03 && xRel < 0.2) {
					tytul = getString(R.string.pokl_name);
					tresc = getString(R.string.pokl_text);
					photo1 = R.drawable.photo_pokl;
					wwwAddress = "http://www.kapitalludzki.gov.pl/";
				}
				if (yRel < 0.21 && xRel > 0.81 && xRel < 0.98) {
					tytul = getString(R.string.efs_name);
					tresc = getString(R.string.efs_text);
					photo1 = R.drawable.photo_efs;
					wwwAddress = "http://www.efs.gov.pl";
				}

				// Dolne logotypy
				if (yRel > 0.87 && xRel > 0.25 && xRel < 0.33) {
					tytul = getString(R.string.pg_name);
					tresc = getString(R.string.pg_text);
					photo1 = R.drawable.photo_pg1;
					photo2 = R.drawable.photo_pg2;
					photo3 = R.drawable.photo_pg3;
					wwwAddress = "http://www.pg.gda.pl";
				}
				if (yRel > 0.87 && xRel > 0.36 && xRel < 0.44) {
					tytul = getString(R.string.ftims_name);
					tresc = getString(R.string.ftims_text);
					photo1 = R.drawable.photo_wftims1;
					photo2 = R.drawable.photo_wftims2;
					photo3 = R.drawable.photo_wftims3;
					photo4 = R.drawable.photo_wftims4;
					wwwAddress = "http://www.mif.pg.gda.pl";
				}
				if (yRel > 0.87 && xRel > 0.46 && xRel < 0.59) {
					tytul = getString(R.string.ydp_name);
					tresc = getString(R.string.ydp_text);
					photo1 = R.drawable.photo_ydp1;
					photo2 = R.drawable.photo_ydp2;
					photo3 = R.drawable.photo_ydp3;
					photo4 = R.drawable.photo_ydp4;
					wwwAddress = "http://ydp.com.pl";
				}
				if (yRel > 0.87 && xRel > 0.62 && xRel < 0.77) {
					tytul = getString(R.string.malmberg_name);
					tresc = getString(R.string.malmberg_text);
					photo1 = R.drawable.photo_malmberg1;
					photo2 = R.drawable.photo_malmberg2;
					wwwAddress = "http://www.malmberg.nl";
				}

				if (tresc.isEmpty()) {
					return false;
					// albo startujemy liste e-doswiadczen...
					// startActivity(new Intent(StronaTytulowa.this, ListaED.class));
				} else {
					// to tylko do ew. testów:
					//tresc = Float.toString(e.getX() / viewWidth) + " " + Float.toString(e.getY() / viewHeight);

					// Wyswietlamy popupa w postaci "custom alertDialog"
					AlertDialog.Builder builder;
					AlertDialog alertDialog;

					LayoutInflater inflater = (LayoutInflater) TitlePage.this.getSystemService(LAYOUT_INFLATER_SERVICE);
					View layout = inflater.inflate(R.layout.title_page_popup,
							(ViewGroup) findViewById(R.id.strona_tytulowa_popup));

					TextView text = (TextView) layout.findViewById(R.id.text);
					text.setText(Html.fromHtml(tresc));

					// Marginesy ustawiane programowo, aby nie było luk między "pustymi" obrazkami
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					lp.setMargins(0, 10, 30, 0);
					if (photo1 != 0) {
						ImageView image1 = (ImageView) layout.findViewById(R.id.popup_image1);
						image1.setImageResource(photo1);
					}
					if (photo2 != 0) {
						ImageView image2 = (ImageView) layout.findViewById(R.id.popup_image2);
						image2.setLayoutParams(lp);
						image2.setImageResource(photo2);
					}
					if (photo3 != 0) {
						ImageView image3 = (ImageView) layout.findViewById(R.id.popup_image3);
						image3.setLayoutParams(lp);
						image3.setImageResource(photo3);
					}
					if (photo4 != 0) {
						ImageView image4 = (ImageView) layout.findViewById(R.id.popup_image4);
						image4.setLayoutParams(lp);
						image4.setImageResource(photo4);
					}

					builder = new AlertDialog.Builder(TitlePage.this).setTitle(tytul).
							setNeutralButton(getString(R.string.btn_close),
									new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int which) {
								}
							}).
							setPositiveButton(getString(R.string.btn_view_www),
									new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int which) {
									final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(wwwAddress));
									startActivity(intent);
								}
							});
					builder.setView(layout);
					alertDialog = builder.create();
					alertDialog.show();
				}
				return true;
			}
		
	};

		// Przycisk - Informacje
		Button infoBtn = (Button) findViewById(R.id.buttonInfo);
		infoBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(TitlePage.this, InfoPage.class));
			}
		});

		// Przycisk - Przejście do listy e-doswiadczen
		Button listaBtn = (Button) findViewById(R.id.buttonLista);
		listaBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(TitlePage.this, ListED.class));
			}
		});


		// Przycisk - Zakończ
		Button zakonczBtn = (Button) findViewById(R.id.buttonZakoncz);
		zakonczBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//nanoHTTPD.stop();
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});

	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {

		if (me.getEventTime() > eventTime + 500){
			eventTime = me.getEventTime();
			myGestureScanner.onSingleTapConfirmed(me);	
			return true; 				
		}
		else
			return true; 	
	}

	/*
	 * Utworzenie ActionBar-u.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_action_bar_tytulowa, menu);
		return true;
	}

	/*
	 * Obsługa ActionBar-u.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Obsługa wybranych pozycji z menu
		switch (item.getItemId()) {
		case R.id.menu_pl:
			if (getResources().getConfiguration().locale.getDisplayName().contains("olski")) {
				Toast.makeText(getApplicationContext(), getString(R.string.msg_already_pl), Toast.LENGTH_SHORT).show();
				return true;
			}
			//TODO do poprawy
			Locale localePL = new Locale(PL_FLAG);
			Locale.setDefault(localePL);
			Configuration configPL = new Configuration();
			configPL.locale = localePL;
			getBaseContext().getResources().updateConfiguration(configPL, getBaseContext().getResources().getDisplayMetrics());
			Toast.makeText(getApplicationContext(), getString(R.string.msg_switch_to_pl), Toast.LENGTH_SHORT).show();
			restartFirstActivity();
			return true;

		case R.id.menu_en:
			if (getResources().getConfiguration().locale.getDisplayName().contains("nglish")) {
				Toast.makeText(getApplicationContext(), getString(R.string.msg_already_en), Toast.LENGTH_SHORT).show();
				return true;
			}
			Locale localeEN = new Locale(EN_FLAG);
			Locale.setDefault(localeEN);
			Configuration configEN = new Configuration();
			configEN.locale = localeEN;
			getBaseContext().getResources().updateConfiguration(configEN, getBaseContext().getResources().getDisplayMetrics());
			Toast.makeText(getApplicationContext(), getString(R.string.msg_switch_to_en), Toast.LENGTH_SHORT).show();
			restartFirstActivity();
			return true;

		case R.id.menu_help:
			showHelp();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	
	private void showHelp() {
		// Wyswietlamy pomoc w postaci "custom alertDialog"
		AlertDialog.Builder builder;
		AlertDialog alertDialog;
		
		LayoutInflater inflater = (LayoutInflater)TitlePage.this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.help_popup,
				(ViewGroup) findViewById(R.id.pomoc_popup));

		String title = getString(R.string.txt_title_help);
		String body = getString(R.string.txt_help_tytulowa);

		TextView text = (TextView) layout.findViewById(R.id.text_help_popup);
		text.setText(Html.fromHtml(body));

		builder = new AlertDialog.Builder(TitlePage.this).setTitle(title).
				setNeutralButton(getString(R.string.btn_close),
						new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
					}
				}).setIcon(R.drawable.ic_menu_help);
		builder.setView(layout);
		alertDialog = builder.create();
		alertDialog.show();

	}

	/*
	 * Zamyka wszystkie aktywności i przełącza na startową.
	 *
	 */
	private void restartFirstActivity() {
		Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}

	/**
	 * Sprawdza, czy plik istnieje na zdalnym serwerze.
	 */
	private static boolean fileExistsOnServer(String URLName) {

		try {
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection con =
					(HttpURLConnection) new URL(URLName).openConnection();
			con.setInstanceFollowRedirects(false);
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Test polaczenia z siecia.
	 */
	private boolean isInternetOn() {
		ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		
		
		NetworkInfo tmp0 = connec.getNetworkInfo(0);
		NetworkInfo tmp1 = connec.getNetworkInfo(1);
		NetworkInfo.State  state0 = tmp0.getState();
		NetworkInfo.State  state1 = tmp1.getState();
		
		if (state0 == NetworkInfo.State.CONNECTED || state0 == NetworkInfo.State.CONNECTING){ 
			return true; 
		}
		if (state1 == NetworkInfo.State.CONNECTED || state1 == NetworkInfo.State.CONNECTING){ 
			return true; 
		}	

	return false;
	}

	/**
	 * Sprawdza (w osobnym wątku) czy jest uaktualnienie dango e-doświadczenia.
	 * Bada, czy na serwerze jest plik ZIP o innej dacie modyfikacji niż pobrany . 
	 * Jeżeli tak, to oznacza że jest uaktualnienie.
	 */
	public class CheckForEDUpdates extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... filenames) {

			for (String edName : filenames) {
				long lastModification= 0;
				try {
					if(!isInternetOn()){					
						return null;
					}

					final String fileURL = ED_REMOTE_REPOSITORY + File.separator + edName + ".zip";

					// Sprawdzenie, czy plik ZIP istnieje na sewerze
					if (!fileExistsOnServer(fileURL)) {               	
						break;
					}
					else{
						// Otwiera polaczenie       
						HttpURLConnection conn = (HttpURLConnection) new URL(fileURL).openConnection();
						if(conn.getDoInput()){
							conn.setRequestMethod("HEAD");
							// Pobierz datę ostatniej zmiany zdalnego pliku
							lastModification = conn.getLastModified();
						}
						conn.disconnect();				
						//Zapisz / porównaj ustawienia w Preferencjach
						SharedPreferences edSavedData = getPreferences(MODE_PRIVATE);				
						long edModificationDate = edSavedData.getLong(edName + PREFS_DATE_MODF_SUFFIX, 0);
						
						// Zwróci zero, jak klucza nie ma     
						if (edModificationDate == 0 || edModificationDate < lastModification) {
							// Zapisujemy info o aktualizacji do preferencji
							SharedPreferences.Editor edLocalDataEditor = edSavedData.edit();
							edLocalDataEditor.putBoolean(edName + PREFS_UPDATE_SUFFIX, true);
							edLocalDataEditor.apply();
						}
						else if (edModificationDate > lastModification) {
							// Zapisujemy info o aktualizacji do preferencji
							SharedPreferences.Editor edLocalDataEditor = edSavedData.edit();
							edLocalDataEditor.putBoolean(edName + PREFS_UPDATE_SUFFIX, false);
							edLocalDataEditor.apply();
						}
					}
				}

				catch (Exception e) {
					return null;
				}
			}
			return null;
		}
		
	}

}
