package pl.gda.pg.mif.edoswiadczenia;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Wyświetlenie listy e-doświadczeń. Dziedziczy po StronaTytulowa, aby uwspólnić
 * ActionBar.
 *
 * @author sylas
 */
public class ListED extends Activity {

    //private int edFileZIPSize;
    private static volatile boolean downloadingED;
    private final float ONE_GB = 1048576.0F;
    private final String PREFS_UPDATE_SUFFIX = "_update";
    private final String PREFS_SIZE_SUFFIX = "_size";
    private final String PREFS_DIR_SIZE_SUFFIX = "_dirsize";
    private final String PREFS_DATE_MODF_SUFFIX = "_date";
    private final static String ADOBE_FLASH_PACKAGE_NAME = "com.adobe.flashplayer";
    public final static String ED_SDCARD_DIR = "e-doswiadczenia";
    private final String ED_REMOTE_REPOSITORY = "http://e-doswiadczenia.mif.pg.gda.pl/files/ed-android-repo/";
    public static final String ED_BASE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + ED_SDCARD_DIR + File.separator;
    public static final String ED_SERVER_ROOT = "http://127.0.0.1:" + Integer.toString(TitlePage.WWW_SERVER_PORT) + File.separator;
    
    public static Map<String,List<String>> edInformation;
  //TODO poprawka aktualizacji   
    
    public ListED() {
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_ed);
     
        //pobieranie instalacji flasha, jeżeli zachodzi taka potrzeba		
           
        if(!isFlashAvailable(this)){
			askForDownloadingFlash();	
		}

// Wahadlo matematyczne
        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!downloadingED) {         	
                    ED.edFileSWFName = "pendulum.swf";
                    ED.edSubDir = "wahadlo_matematyczne";
                    ED.edName = getString(R.string.ed_name_wahadlo);
                    ED.edInfo = getString(R.string.ed_info_wahadlo);
                    ED.edInfoRun = getString(R.string.ed_cwiczenie_wahadlo);
                    ED.edMovie = R.raw.wahadlo;

                    if (edIsDownloaded()) {
                        startActivity(new Intent(ListED.this, DetailsED.class));
                    }
                }
            }
        });

// Lawa optyczna
        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!downloadingED) {
                    ED.edFileSWFName = "lens.swf";
                    ED.edSubDir = "lawa_optyczna";
                    ED.edName = getString(R.string.ed_name_lawa);
                    ED.edInfo = getString(R.string.ed_info_lawa);
                    ED.edInfoRun = getString(R.string.ed_cwiczenie_lawa);
                    ED.edMovie = R.raw.lawa;

                    if (edIsDownloaded()) {
                        startActivity(new Intent(ListED.this, DetailsED.class));
                    }
                }
            }
        });

// Rownia pochyla
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!downloadingED) {
                    ED.edSubDir = "rownia_pochyla";
                    ED.edFileSWFName = "inclinedplane.swf";
                    ED.edName = getString(R.string.ed_name_rownia);
                    ED.edInfo = getString(R.string.ed_info_rownia);
                    ED.edInfoRun = getString(R.string.ed_cwiczenie_rownia);
                    ED.edMovie = R.raw.rownia;

                    if (edIsDownloaded()) {
                        startActivity(new Intent(ListED.this, DetailsED.class));
                    }
                }
            }
        });

// Zderzenia sprezyste i niesprezyste
        Button button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!downloadingED) {
                    ED.edSubDir = "zderzenia_sprezyste_i_niesprezyste";
                    ED.edFileSWFName = "collisions.swf";
                    ED.edName = getString(R.string.ed_name_zderzenia);
                    ED.edInfo = getString(R.string.ed_info_zderzenia);
                    ED.edInfoRun = getString(R.string.ed_cwiczenie_zderzenia);
                    ED.edMovie = R.raw.zderzenia;

                    if (edIsDownloaded()) {
                        startActivity(new Intent(ListED.this, DetailsED.class));
                    }
                }
            }
        });

// Rzuty
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!downloadingED) {
                    ED.edSubDir = "rzuty";
                    ED.edFileSWFName = "throws.swf";
                    ED.edName = getString(R.string.ed_name_rzuty);
                    ED.edInfo = getString(R.string.ed_info_rzuty);
                    ED.edInfoRun = getString(R.string.ed_cwiczenie_rzuty);
                    ED.edMovie = R.raw.rzuty;

                    if (edIsDownloaded()) {
                        startActivity(new Intent(ListED.this, DetailsED.class));
                    }
                }
            }
        });

//Ruch cial niebieskich
        Button button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!downloadingED) {
                    ED.edSubDir = "ruch_cial_niebieskich";
                    ED.edFileSWFName = "astro.swf";
                    ED.edName = getString(R.string.ed_name_astro);
                    ED.edInfo = getString(R.string.ed_info_astro);
                    ED.edInfoRun = getString(R.string.ed_cwiczenie_astro);
                    ED.edMovie = R.raw.astro;

                    if (edIsDownloaded()) {
                        startActivity(new Intent(ListED.this, DetailsED.class));
                    }
                }
            }
        });

// Mechanika cieczy
        Button button7 = (Button) findViewById(R.id.button7);
        button7.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!downloadingED) {
                    ED.edSubDir = "mechanika_cieczy";
                    ED.edFileSWFName = "liquid.swf";
                    ED.edName = getString(R.string.ed_name_ciecze);
                    ED.edInfo = getString(R.string.ed_info_ciecze);
                    ED.edInfoRun = getString(R.string.ed_cwiczenie_ciecze);
                    ED.edMovie = R.raw.ciecze;

                    if (edIsDownloaded()) {
                        startActivity(new Intent(ListED.this, DetailsED.class));
                    }
                }
            }
        });

// Bryla sztywna
        Button button8 = (Button) findViewById(R.id.button8);
        button8.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!downloadingED) {
                    ED.edSubDir = "bryla_sztywna";
                    ED.edFileSWFName = "bryla.swf";
                    ED.edName = getString(R.string.ed_name_bryla);
                    ED.edInfo = getString(R.string.ed_info_bryla);
                    ED.edInfoRun = getString(R.string.ed_cwiczenie_bryla);
                    ED.edMovie = R.raw.bryla;

                    if (edIsDownloaded()) {
                        startActivity(new Intent(ListED.this, DetailsED.class));
                    }
                }
            }
        });

/////////////////////////////////wlasciwosci gazów////////////////////////////////////////////
        Button button9 = (Button) findViewById(R.id.button9);
        button9.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (downloadingED) {
                    return;
                }
                showInfoDialog(getString(R.string.msg_title_info), getString(R.string.msg_ed_not_yet_produced), R.drawable.ic_info);
            }
        });

// Drgania mechaniczne
        Button button10 = (Button) findViewById(R.id.button10);
        button10.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!downloadingED) {
                    ED.edSubDir = "drgania_mechaniczne";
                    ED.edFileSWFName = "vibra.swf";
                    ED.edName = getString(R.string.ed_name_drgania);
                    ED.edInfo = getString(R.string.ed_info_drgania);
                    ED.edInfoRun = getString(R.string.ed_cwiczenie_drgania);
                    ED.edMovie = R.raw.drgania;

                    if (edIsDownloaded()) {
                        startActivity(new Intent(ListED.this, DetailsED.class));
                    }
                }
            }
        });

/////////////////////////////////pole elektryczne////////////////////////////////////////////
        Button button11 = (Button) findViewById(R.id.button11);
        button11.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (downloadingED) {
                    return;
                }
                showInfoDialog(getString(R.string.msg_title_info), getString(R.string.msg_ed_not_yet_produced), R.drawable.ic_info);
            }
        });

/////////////////////////////////prąd stały////////////////////////////////////////////
        Button button12 = (Button) findViewById(R.id.button12);
        button12.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (downloadingED) {
                    return;
                }
                showInfoDialog(getString(R.string.msg_title_info), getString(R.string.msg_ed_not_yet_produced), R.drawable.ic_info);
            }
        });

/////////////////////////////////laboratorium dzwieku////////////////////////////////////////////
        Button button13 = (Button) findViewById(R.id.button13);
        button13.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (downloadingED) {
                    return;
                }
                showInfoDialog(getString(R.string.msg_title_info), getString(R.string.msg_ed_not_yet_produced), R.drawable.ic_info);
            }
        });

/////////////////////////////////kalorymetria////////////////////////////////////////////
        Button button14 = (Button) findViewById(R.id.button14);
        button14.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (downloadingED) {
                    return;
                }
                showInfoDialog(getString(R.string.msg_title_info), getString(R.string.msg_ed_not_yet_produced), R.drawable.ic_info);
            }
        });

/////////////////////////////////kondensatory////////////////////////////////////////////
        Button button15 = (Button) findViewById(R.id.button15);
        button15.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (downloadingED) {
                    return;
                }
                showInfoDialog(getString(R.string.msg_title_info), getString(R.string.msg_ed_not_yet_produced), R.drawable.ic_info);
            }
        });

/////////////////////////////////pole magnetyczne////////////////////////////////////////////
        Button button16 = (Button) findViewById(R.id.button16);
        button16.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (downloadingED) {
                    return;
                }
                showInfoDialog(getString(R.string.msg_title_info), getString(R.string.msg_ed_not_yet_produced), R.drawable.ic_info);
            }
        });

/////////////////////////////////cewki i indukcja
        Button button17 = (Button) findViewById(R.id.button17);
        button17.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (downloadingED) {
                    return;
                }
                showInfoDialog(getString(R.string.msg_title_info), getString(R.string.msg_ed_not_yet_produced), R.drawable.ic_info);
            }
        });

/////////////////////////////////zjawisko polaryzacji i zalamania swiatla//////
        Button button18 = (Button) findViewById(R.id.button18);
        button18.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (downloadingED) {
                    return;
                }
                showInfoDialog(getString(R.string.msg_title_info), getString(R.string.msg_ed_not_yet_produced), R.drawable.ic_info);
            }
        });

/////////////////////////////////uklady RLC/////////////////////////////
        Button button19 = (Button) findViewById(R.id.button19);
        button19.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (downloadingED) {
                    return;
                }
                showInfoDialog(getString(R.string.msg_title_info), getString(R.string.msg_ed_not_yet_produced), R.drawable.ic_info);
            }
        });

/////////////////////////////////korpuskularna natura swiatła i materii/////
        Button button20 = (Button) findViewById(R.id.button20);
        button20.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (downloadingED) {
                    return;
                }
                showInfoDialog(getString(R.string.msg_title_info), getString(R.string.msg_ed_not_yet_produced), R.drawable.ic_info);
            }
        });

/////////////////////////////////interferencja i dyfrakcja////////////////////
        Button button21 = (Button) findViewById(R.id.button21);
        button21.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (downloadingED) {
                    return;
                }
                showInfoDialog(getString(R.string.msg_title_info), getString(R.string.msg_ed_not_yet_produced), R.drawable.ic_info);
            }
        });

/////////////////////////////////spektroskopia////////////////////////////////////////////
        Button button22 = (Button) findViewById(R.id.button22);
        button22.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (downloadingED) {
                    return;
                }
                showInfoDialog(getString(R.string.msg_title_info), getString(R.string.msg_ed_not_yet_produced), R.drawable.ic_info);
            }
        });

// Eksperymenty myslowe Einsteina
        Button button23 = (Button) findViewById(R.id.button23);
        button23.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!downloadingED) {
                    ED.edSubDir = "eksperymenty_myslowe_einsteina";
                    ED.edFileSWFName = "einstein.swf";
                    ED.edName = getString(R.string.ed_name_einstein);
                    ED.edInfo = getString(R.string.ed_info_einstein);
                    ED.edInfoRun = getString(R.string.ed_cwiczenie_einstein);
                    ED.edMovie = R.raw.einstein;

                    if (edIsDownloaded()) {
                        startActivity(new Intent(ListED.this, DetailsED.class));
                    }
                }
            }
        });

// Powrót do strony tytulowej
        Button button24 = (Button) findViewById(R.id.button24);
        button24.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!downloadingED) {
                    finish();
                }
            }
        });

    }

    private void showInfoDialog(String title, String body, int type) {

        new AlertDialog.Builder(ListED.this).setTitle(title).setMessage(body).setNeutralButton(getString(R.string.btn_ok),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog,
                            int which) {
                    }
                }).setIcon(type).show();

    }

    private void askForDownloadingED(final String edRemoteZipFileName) {

        new AlertDialog.Builder(ListED.this).setTitle(getString(R.string.msg_title_question)).setMessage(getString(R.string.msg_download_ed_question)).setPositiveButton(getString(R.string.btn_yes),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog,
                            int which) {
                        // Start pobierania / rozpakowywania
                        new DownloadED().execute(edRemoteZipFileName);
                    }
                }).setNegativeButton(getString(R.string.btn_no),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog,
                            int which) {
                    }
                }).setIcon(R.drawable.ic_question).show();
    }

    private void askForDownloadingEDUpdate(final String edRemoteZipFileName) {

        new AlertDialog.Builder(ListED.this).setTitle(getString(R.string.msg_title_question)).setMessage(getString(R.string.msg_download_ed_update_question)).setPositiveButton(getString(R.string.btn_yes),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog,
                            int which) {
                        // Start pobierania / rozpakowywania
                        new DownloadED().execute(edRemoteZipFileName);
                    }
                }).setNegativeButton(getString(R.string.btn_no),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog,
                            int which) {
                        // Uzytkownik nie chce pobrac uaktualnienia - uruchamiamy stare e-d
                        startActivity(new Intent(ListED.this, DetailsED.class));
                    }
                }).setIcon(R.drawable.ic_question).show();
    }

    private void askForDownloadingFlash() {

        new AlertDialog.Builder(ListED.this).setTitle(getString(R.string.msg_title_question)).setMessage(getString(R.string.msg_install_flash_question)).setPositiveButton(getString(R.string.btn_yes),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog,
                            int which) {
                        
                    	Downloading flash = new Downloading(getApplicationContext());
                    	flash.downloadFlash();
                    	// Przekierowanie do marketu
                        //Uri marketUri = Uri.parse(ADOBE_FLASH_MARKET_URL);
                        // Można też tak (bezposrednio do marketu)...:
                        //Uri marketUri = Uri.parse("market://details?id=com.adobe.flashplayer");
                        // ... ale np. emulator nie rozpoznaje protokulu "market"
                        //Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                        //startActivity(marketIntent);
                    }
                }).setNegativeButton(getString(R.string.btn_no),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog,
                            int which) {
                    }
                }).setIcon(R.drawable.ic_question).show();
    }

    private boolean edIsDownloaded() {

        // Tworzenie pelnych nazw plikow i URL-i
        final String edRemoteZipFileName = ED.edSubDir + ".zip"; // Nazwa pliku ZIP z e-doswiadczeniem
        ED.edLink = ED_SERVER_ROOT + ED.edSubDir + File.separator + ED.edFileSWFName; // URL dla e-doswiadczenia

        // Sprawdzenie, czy jest zainstalowany Adobe Flash Player
        if (!isFlashAvailable(ListED.this)) {
            askForDownloadingFlash();
            return false;
        }

        if (isEdIntegrant()) {  // E-d juz pobrane i nie naruszone

            // Sprawdzamy, czy jest aktualizacja
            new CheckForEDUpdates().execute(edRemoteZipFileName);
            // Uwaga! Sprawdzenie jest w osobnym wątku, więc warunek poniżej zapewne nie będzie prawdziwy
            // nawet gdy będzie znaleziona aktualizacja. Ale będzie prawdziwy podczas kolejnego uruchomienia e-d!
            // TODO Sprawdzic, czy nie da sie zastosowac semafora.

            // Sprawdzenie, czy jest ustawiona flaga "update" w preferencjach danego e-d
            SharedPreferences edLocalData = getPreferences(MODE_PRIVATE);
            boolean isUpdateAvialable = edLocalData.getBoolean(ED.edSubDir + PREFS_UPDATE_SUFFIX, false);

            if (isUpdateAvialable && isInternetOn()) {
                // Jest nowsza wersja e-d, pytanie o pobranie - pytanie, czy pobrac
                // Dialogi sa asynchroniczne, wiec samo wywolanie pobierania jest przy obsludze dialogu
                askForDownloadingEDUpdate(edRemoteZipFileName);
                return false;
            }
            return true;

        } else { // Proba pobrania e-d z Internetu

            // Sprawdzenie, czy istnieje katalog bazowy i ew. jego utworzenie
            File dir = new File(ED_BASE_DIR);
            if (!dir.exists() || !dir.isDirectory()) {
                if (!dir.mkdirs()) {
                    showInfoDialog(getString(R.string.msg_title_error), getString(R.string.msg_cant_make_dir), R.drawable.ic_error);
                    return false;
                }
            }

            // Sprawdzenie, czy na karcie jest wystarczająco dużo miejsca
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            double sdAvailSize = (double) stat.getAvailableBlocks() * (double) stat.getBlockSize();
            double mBytesSdAvailSize = sdAvailSize / 1048576;
            if (mBytesSdAvailSize < 100) {
                showInfoDialog(getString(R.string.msg_title_error), getString(R.string.msg_to_less_space), R.drawable.ic_error);
                return false;
            }

            if (isInternetOn()) {
                // Zapytanie, czy pobrac e-d
                // Dialogi sa asynchroniczne, wiec samo wywolanie pobierania jest przy obsludze dialogu
                askForDownloadingED(edRemoteZipFileName);
                return false;
            } else {
                // brak sieci
                showInfoDialog(getString(R.string.msg_title_info), getString(R.string.msg_no_network), R.drawable.ic_error);
                return false;
            }

        }
    }

    /**
     * Test polaczenia z siecia.
     */
    private boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
                || connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
                || connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
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
     * Sprawdza, czy zainstalowano Adobe Flash Playera. Jeżeli nie, wyrzuca
     * odpowiedni wyjątek.
     *
     * @param context
     * @return
     */
    private static boolean isFlashAvailable(Context context) {
        try {
            String mVersion = context.getPackageManager().getPackageInfo(
                    ADOBE_FLASH_PACKAGE_NAME, 0).versionName;
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /*
     * Sprawdza, czy pobrane na kartę SD e-d nie jest naruszone (np. przez
     * skasowanie jakiegoś pliku).
     */
    private boolean isEdIntegrant() {
        String edFileSWFPath = ED_BASE_DIR + ED.edSubDir + File.separator + ED.edFileSWFName; // Pelna sciezka do SWF-a
        File fileSWF = new File(edFileSWFPath);
        if (fileSWF.exists()) {
            // Plik główny istnieje, ale sprawdzamy czy całość jest OK

            File edFolder = new File(ED_BASE_DIR + ED.edSubDir); // Folder z e-d
            int edDirSize = getFolderSize(edFolder);

            SharedPreferences edLocalData = getPreferences(MODE_PRIVATE);
            // Wczytujemy zapisaną w preferencjach wartosc rozmiaru katalogu danego e-d
            int edSavedDirSize = edLocalData.getInt(ED.edSubDir + PREFS_DIR_SIZE_SUFFIX, 0);

            if (edSavedDirSize == edDirSize) {
                // Wszystko OK
                return true;
            } else {
                // Dane naruszone
                return false;
            }

        } else {
            return false;
        }

    }

    /*
     * Rekurencyjne obliczenie sumy rozmiarów wszystkich plików w katalogu.
     * Użwane do sprawdzenia integralności struktury pobranego e-doświadczenia
     * (np. czy ktoś ręcznie nie pousuwał plików).
     */
    public int getFolderSize(File folder) {

        int foldersize = 0;

        File[] filelist = folder.listFiles();
        for (int i = 0; i < filelist.length; i++) {
            if (filelist[i].isDirectory()) {
                foldersize += getFolderSize(filelist[i]);
            } else {
                foldersize += filelist[i].length();
            }
        }
        return foldersize;
    }

    /**
     * Pobiera (w osobnym wątku) dane e-doświadczenie, wyświetlając progress
     * dialog.
     */
    private class DownloadED extends AsyncTask<String, Integer, Integer> {

        private ProgressDialog dialog = new ProgressDialog(ListED.this);
        private String dlgDownloading;

        @Override
        protected void onPreExecute() {
            ListED.downloadingED = true; // flaga zabezpieczajaca przed klikaniem innych elementow podczas pobierania
            showProgressDialog(dlgDownloading);
        }

        @Override
        protected Integer doInBackground(String... edRemoteZipFileName) {
            try {

                String fileURL = edRemoteZipFileName[0];
                String fileName = edRemoteZipFileName[0];

                final int BUFFER_SIZE = 1024;

                // Sprawdzenie, czy plik istnieje na serwerze
                if (!fileExistsOnServer(ED_REMOTE_REPOSITORY + fileURL)) {
                    return 0;
                }

                URL url = new URL(ED_REMOTE_REPOSITORY + fileURL); // plik do pobrania

                File file = new File(ED_BASE_DIR + fileName); // Plik lokalny

                // Otwiera polaczenie
                URLConnection ucon = url.openConnection();

                // Pobierz wielkosc zdalnego pliku
                ED.edFileZIPSize = ucon.getContentLength();
                dlgDownloading = String.format(getString(R.string.dlg_downloading), (int) (ED.edFileZIPSize / ONE_GB));

                // Wczytanie preferencji
                SharedPreferences edLocalData = getPreferences(MODE_PRIVATE);

                // Wczytujemy zapisaną w preferencjach wartosc rozmiaru pliku danego e-d
                // Zwróci zero, jak klucza jeszcze nie ma - pierwsze pobranie e-d
                int edSavedFileSize = edLocalData.getInt(ED.edSubDir + PREFS_SIZE_SUFFIX, 0);
                long edSavedModifiedDate = edLocalData.getLong(ED.edName + PREFS_DATE_MODF_SUFFIX,0);
              
                if (edSavedFileSize == 0 || edSavedFileSize != ED.edFileZIPSize) {
                    // Stworzenie nowego klucza bądź uaktualnienie starego
                    SharedPreferences.Editor edLocalDataEditor = edLocalData.edit();
                    edLocalDataEditor.putInt(ED.edSubDir + PREFS_SIZE_SUFFIX, ED.edFileZIPSize);
                    edLocalDataEditor.apply();
                }
                
                //TODO moj jest ten kawałek podłogi ;)
                   
                else if (edSavedModifiedDate == 0 || edSavedModifiedDate != ED.edLastModification) {
                    // Stworzenie nowego klucza bądź uaktualnienie starego
                    SharedPreferences.Editor edLocalDataEditor = edLocalData.edit();
                    edLocalDataEditor.putLong(ED.edName+PREFS_DATE_MODF_SUFFIX,ED.edLastModification);
                    edLocalDataEditor.apply();
                }
                //

                // Definiuje InputStreams do odczytu z URLConnection
                InputStream is = ucon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is, BUFFER_SIZE);

                FileOutputStream fos = new FileOutputStream(file);

                byte[] baf = new byte[BUFFER_SIZE];

                int current = 0;
                int iterCount = 0;
                int progressInPercents;

                while (current != -1) {
                    if (isCancelled()) {
                        publishProgress(-1); // Anulowano pobieranie
                        return (null);
                    }
                    fos.write(baf, 0, current);
                    current = bis.read(baf, 0, BUFFER_SIZE);
                    if (iterCount % 100 == 0) {
                        // Dzielimy numer iteracji przez wielkość pliku w megabajtach i mnożymy przez 100
                        progressInPercents = (int) (iterCount / (float) ED.edFileZIPSize * 102400);
                        publishProgress(progressInPercents);
                    }
                    iterCount++;
                }

                fos.close();
                publishProgress(100);

                // Od tego momentu nie można anulować operacji
                dialog.setCancelable(false);

                // Skasowanie starych plikow
                deleteDirRecursive(new File(ED_BASE_DIR + ED.edSubDir));

                // Rozpakowanie archiwum a nastepnie jego usuniecie
                Decompress archive = new Decompress(ED_BASE_DIR + fileName, ED_BASE_DIR);
                archive.unzip();
                file.delete();

            } catch (IOException e) {
                return 0;
            }

            // Normalny powrót
            return 1;

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            if (progress[0] == -1) { // Anulowano pobieranie
                hideProgressDialog();
            }
            if (progress[0] >= 100) {
                updateProgressDialog(getString(R.string.dlg_unpacking));
            } else {
                updateProgressDialog(dlgDownloading + " " + progress[0] + "%");
            }
        }

        @Override
        protected void onPostExecute(Integer result) {

            hideProgressDialog();

            if (result == 1) { // Wszystko OK, starujemy e-doswiadczenie

                // Wyczyszczenie flagi "update" w preferencjach
                SharedPreferences edLocalData = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor edLocalDataEditor = edLocalData.edit();
                edLocalDataEditor.putBoolean(ED.edSubDir + PREFS_UPDATE_SUFFIX, false);

                // Obliczenie i zapisanie w preferencjach rozmiaru katalogu z e-d
                File edFolder = new File(ED_BASE_DIR + ED.edSubDir);
                int edDirSize = getFolderSize(edFolder);
                edLocalDataEditor.putInt(ED.edSubDir + PREFS_DIR_SIZE_SUFFIX, edDirSize);

                edLocalDataEditor.apply();


                startActivity(new Intent(ListED.this, DetailsED.class));
            } else {
                showInfoDialog(getString(R.string.msg_title_error), getString(R.string.msg_no_file_on_server), R.drawable.ic_error);
            }
            ListED.downloadingED = false;
        }

        @Override
        protected void onCancelled() {
            ListED.downloadingED = false;
            hideProgressDialog();
            Toast.makeText(getApplicationContext(), getString(R.string.msg_download_cancelled), Toast.LENGTH_LONG).show();

            // Usunięcie plikow z katalogu e-doświadczeń (tylko najwyższy poziom, gdzie są przechowywane ZIP-y
            File path = new File(ED_BASE_DIR);
            File[] filesInDir = path.listFiles();
            if (filesInDir == null) {
                return;
            }
            for (int i = 0; i < filesInDir.length; i++) {
                if (filesInDir[i].isFile()) {
                    filesInDir[i].delete();
                }

            }

        }

        void showProgressDialog(String string) {
            dialog.setMessage(string);
            dialog.setCancelable(true);

            dialog.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(true);
                }
            });

            dialog.show();
        }

        void updateProgressDialog(String string) {
            dialog.show();
            dialog.setMessage(string);
        }

        void hideProgressDialog() {
            dialog.dismiss();
        }

        void deleteDirRecursive(File dir) {
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    File temp = new File(dir, children[i]);
                    if (temp.isDirectory()) {
                        deleteDirRecursive(temp);
                    } else {
                        temp.delete();
                    }
                }
                dir.delete();
            }
        }
    }

    /**
     * Sprawdza (w osobnym wątku) czy jest uaktualnienie dango e-doświadczenia.
     * Bada, czy na serwerze jest plik ZIP o innej długości niż pobrany przy
     * poprzedniej instalacji. Jeżeli tak, to oznacza że jest uaktualnienie.
     * TODO  Można zastąpić datą pliku, ale nie wiem jak ją wydobyć.
     */
    private class CheckForEDUpdates extends AsyncTask<String, Void, Void> {

		@Override
        protected Void doInBackground(String... edRemoteZipFileName) {
            try {
            	String fileURL = edRemoteZipFileName[0];
                // Sprawdzenie, czy plik ZIP istnieje na sewerze
                if (!fileExistsOnServer(ED_REMOTE_REPOSITORY + fileURL)) {
                    return null;
                }
                // plik do sprawdzenia
                URL url = new URL(ED_REMOTE_REPOSITORY + fileURL); 
                // Otwiera polaczenie
                URLConnection ucon = url.openConnection(); 
                
                // Pobierz dlugosc zdalnego pliku
                ED.edFileZIPSize = ucon.getContentLength();
                
                // Pobierz dlugosc pliku zapisana w preferencjach
                SharedPreferences edLocalData = getPreferences(MODE_PRIVATE);
                int edSavedFileSize = edLocalData.getInt(ED.edSubDir + PREFS_SIZE_SUFFIX, 0);
                // Zwróci zero, jak klucza nie ma 
                // (zero powinno wystąpić wyłącznie wtedy, jeżeli użytkownik skasował dane aplikacji)

            	/* kawałek mojego testowego kodu */
                //pobieranie daty ostatniej modyfikacji;
                ED.edLastModification = ucon.getLastModified();
                               
                // Pobierz datę pliku zapisaną w preferencjach
                //SharedPreferences edLocalModificationDate = getPreferences(MODE_PRIVATE);
                long edSavedModificationDate = edLocalData.getLong(ED.edName+PREFS_DATE_MODF_SUFFIX, 0);
                // Zwróci zero, jak klucza nie ma 
                // (zero powinno wystąpić wyłącznie wtedy, jeżeli użytkownik skasował dane aplikacji)
               
                if (edSavedFileSize != ED.edFileZIPSize && edSavedModificationDate != ED.edLastModification) {
                    // Jest aktualizacja, albo użytkownik wykasował dane aplikacji i nie można stwierdzić
                    // Zapisujemy info o aktualizacji do preferencji
                    SharedPreferences.Editor edLocalDataEditor = edLocalData.edit();
                    edLocalDataEditor.putBoolean(ED.edSubDir + PREFS_UPDATE_SUFFIX, true);
                    edLocalDataEditor.apply();
                }          
                else if (edSavedFileSize == ED.edFileZIPSize && edSavedModificationDate != ED.edLastModification) {
                    // Jest aktualizacja, albo użytkownik wykasował dane aplikacji i nie można stwierdzić
                    // Zapisujemy info o aktualizacji do preferencji
                    SharedPreferences.Editor edLocalDataEditor = edLocalData.edit();
                    edLocalDataEditor.putBoolean(ED.edSubDir + PREFS_UPDATE_SUFFIX, true);
                    edLocalDataEditor.apply();
                }
                
                
            } catch (IOException e) {
                return null;
            }
            return null;
        }
    }

    /*
     * Utworzenie ActionBar-u.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action_bar, menu);
        return true;
    }

    /*
     * Obsługa ActionBar-u.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Obsługa wybranych pozycji z menu
        switch (item.getItemId()) {
            case R.id.menu_help:
                Toast.makeText(getApplicationContext(), "Tu będzie pomoc", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
