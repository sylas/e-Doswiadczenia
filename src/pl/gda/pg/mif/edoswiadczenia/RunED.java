package pl.gda.pg.mif.edoswiadczenia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Uruchomienie wybranego e-doświadczenia przy pomocy WebView. Ponieważ Adobe
 * Flash nie dopuszcza na to, żeby plik SWF uruchamiał inne pliki SWF z innej
 * domeny, stawiany jest serwer WWW - w takim przypadku wszystkie SWF-y są
 * widziane w tej samej domenie "localhost" i nie ma problemów z ich
 * uruchamianiem.
 *
 * @author sylas
 */
public class RunED extends Activity {

    WebView mWebView;
    public static final String MIME_TYPE_PDF = "application/pdf";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.run_ed);

        // Info o uruchamianiu e-d       
        Toast.makeText(getApplicationContext(), getString(R.string.msg_starting_ed), Toast.LENGTH_SHORT).show();

        // Ustawienie elementów pobocznych
        TextView title = (TextView) findViewById(R.id.txtNazwaED);
        title.setText(ED.edName);

        TextView infoRun = (TextView) findViewById(R.id.textEDInfoRun);
        infoRun.setText(Html.fromHtml(ED.edInfoRun));


        // Przycisk - Wyświetl podręcznik
        Button runManualBtn = (Button) findViewById(R.id.buttonRunManual);
        runManualBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (canDisplayPdf(RunED.this)) {
                    File file = new File(DetailsED.pathToManual);
                    if (file.exists()) { // Podrecznik istnieje
                        Intent intent = new Intent();
                        intent.setDataAndType(Uri.parse("file:///" + DetailsED.pathToManual), "application/pdf");
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.msg_manual_not_available), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.msg_no_pdf_reader), Toast.LENGTH_LONG).show();
                }
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(RunED.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        mWebView = (WebView) findViewById(R.id.webview_ed);
        
        // Domyslne powiększenie
        // TODO Dokładnie sprawdzić wahadło, ono jest ciutkę większe
        mWebView.setInitialScale(126);
        mWebView.getSettings().setUseWideViewPort(false);

        // Wlasciwie JS nie musi byc wlaczony, ale jak jest wylaczony to po uruchomieniu e-d
        // pojawia sie brzydki efekt przy pierwszym tapnięciu (tak jakby uaktywnianie Flasha).
        // Efekt uboczny: szara ramka o grubości 5 mm naokoło e-d.
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.getSettings().setLoadWithOverviewMode(true);
        //mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.getSettings().setAllowFileAccess(true); 
        
        // Wyłączenie cache
        //mWebView.getSettings().setAppCacheEnabled(false);
        //mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //mWebView.getSettings().setAppCacheMaxSize(0);
                
        // Wylaczenie ikon zoomu po dlugim tapnieciu - i tak nie dziala :(
        //mWebView.getSettings().setBuiltInZoomControls(false);
        //mWebView.getSettings().setSupportZoom(false);
        //mWebView.setLongClickable(false);
              
        // Pluginy
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);

        // Zaladowanie e-doswiadczenia do WebView
        mWebView.loadUrl(ED.edLink);                
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Zapobiega wyciekom pamięci (kilkadziesiąt MB na pojedyncze uruchomienie e-d!)
        mWebView.destroy();
    }

    /*
     * Sprawdzenie, czy jest aplikacja która obłuży intencję wyświetlenia PDF-a
     */
    private static boolean canDisplayPdf(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType(MIME_TYPE_PDF);
        if (packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
