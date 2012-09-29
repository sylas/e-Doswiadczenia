package pl.gda.pg.mif.edoswiadczenia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.FloatMath;
import android.util.TypedValue;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import java.io.File;

/**
 * Wyświetlenie szczegółów e-doświadczenia.
 * Dziedziczy po StronaTytulowa, aby uwspólnić ActionBar.
 * Layout i kod bardzo podobne do klasy Info.
 *
 * @author sylas
 */
public class DetailsED extends Activity implements OnTouchListener {

    TextView info;
    public static String pathToManual;
    VideoView videoED;
    private final String MANUAL_CORE_PATH = "/assets/scenarios/";
    private final String MANUAL_NAME_PREFIX = "podrecznik_";
    private final String PDF_FILE_EXTENSION = ".pdf";
    public static final String MIME_TYPE_PDF = "application/pdf";
    // Stany dla obsługi gestów:
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    private float oldDist;
    private float newDist;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.details_ed);


        TextView title = (TextView) findViewById(R.id.txtNazwaED);
        title.setText(ED.edName);

        info = (TextView) findViewById(R.id.textEDInfo);
        info.setText(Html.fromHtml(ED.edInfo));

        videoED = (VideoView) this.findViewById(R.id.video_view);
        String uri = "android.resource://" + getPackageName() + File.separator + ED.edMovie;
        videoED.setVideoURI(Uri.parse(uri));

        pathToManual = ListED.ED_BASE_DIR + ED.edSubDir + MANUAL_CORE_PATH
                + MANUAL_NAME_PREFIX + ED.edSubDir + PDF_FILE_EXTENSION;



        // Obsługa gestów
        info.setOnTouchListener(this);


        // Przycisk - Uruchom ED
        Button aboutEDBtn = (Button) findViewById(R.id.buttonRunED);
        aboutEDBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailsED.this, RunED.class));
            }
        });

        // Przycisk - Wyświetl podręcznik
        Button runManualBtn = (Button) findViewById(R.id.buttonRunManual);
        runManualBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (canDisplayPdf(DetailsED.this)) {
                    File file = new File(pathToManual);
                    if (file.exists()) { // Podrecznik istnieje
                        Intent intent = new Intent();
                        intent.setDataAndType(Uri.parse("file:///" + pathToManual), MIME_TYPE_PDF);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.msg_manual_not_available), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(DetailsED.this, getString(R.string.msg_no_pdf_reader), Toast.LENGTH_LONG).show();
                }
            }
        });


        // Przycisk - Wstecz
        Button backBtn = (Button) findViewById(R.id.buttonBackSzczegoly);
        backBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    public boolean onTouch(View v, MotionEvent event) {
        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    mode = ZOOM;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == ZOOM) {
                    newDist = spacing(event);

                    if (newDist > 10f) {
                        float textSize = info.getTextSize();
                        if (newDist > oldDist) {
                            if (textSize <= 40) {
                                textSize++;
                            }
                            info.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        } else {
                            if (textSize >= 15) {
                                textSize--;
                            }
                            info.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        }
                    }
                }
                break;
        }

        return true;

    }

    private float spacing(MotionEvent event) {
        if (event.getPointerCount() == 2) { // jeżeli dwa punkty, to oblicz odległość
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return FloatMath.sqrt(x * x + y * y);
        }
        return 0;
    }

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

    @Override
    public void onResume() {
            super.onResume();
            videoED.start();
    }
    
    @Override
    public void onPause() {
            super.onResume();
            videoED.stopPlayback();
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
