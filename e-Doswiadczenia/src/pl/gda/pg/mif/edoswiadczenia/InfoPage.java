package pl.gda.pg.mif.edoswiadczenia;

import android.app.Activity;
import android.content.Intent;
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

/**
 * Info o projekcie. Dziedziczy po StronaTytulowa, aby uwspólnić ActionBar. W
 * przyszłości layout możnaby przepisać z użyciem Fragments.
 *
 * @author sylas
 */
public class InfoPage extends Activity implements OnTouchListener {

    TextView info;
    private final String PROJECT_WWW_ADDRESS = "http://e-doswiadczenia.mif.pg.gda.pl";
    private final String FACEBOOK_WWW_ADDRESS = "http://www.facebook.com/pages/e-Do%C5%9Bwiadczenia-w-fizyce/245605438811390";
    // Stany dla obsługi gestów
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    private float oldDist;
    private float newDist;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.info_page);

        info = (TextView) findViewById(R.id.textViewInfo);
        info.setText(Html.fromHtml(getString(R.string.info_choose)));

        // Scrolling tekstu
        //info.setMovementMethod(new ScrollingMovementMethod());
        // Gryzie się z obsługą gestów, finalnie scrolling jest zrobiony za pomocą <ScrollView> w XML

        // Obsługa gestów
        info.setOnTouchListener(this);


        // Przycisk - O projekcie
        Button aboutProjectBtn = (Button) findViewById(R.id.buttonProjectInfo);
        aboutProjectBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                info.setText(Html.fromHtml(getString(R.string.info_project_text)));
            }
        });

        // Przycisk - O e-doswiadczeniach
        Button aboutEDBtn = (Button) findViewById(R.id.buttonEDInfo);
        aboutEDBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                info.setText(Html.fromHtml(getString(R.string.info_ed_text)));
            }
        });

        // Przycisk - O aplikacji
        Button aboutAppBtn = (Button) findViewById(R.id.buttonAppInfo);
        aboutAppBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                info.setText(Html.fromHtml(getString(R.string.info_app_text)));
            }
        });

        // Przycisk - WWW projektu
        Button wwwBtn = (Button) findViewById(R.id.buttonWWWInfo);
        wwwBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(PROJECT_WWW_ADDRESS));
                startActivity(intent);
            }
        });

        // Przycisk - Facebook
        Button facebookBtn = (Button) findViewById(R.id.buttonFacebookInfo);
        facebookBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(FACEBOOK_WWW_ADDRESS));
                startActivity(intent);
            }
        });

        // Przycisk - Wstecz
        Button backBtn = (Button) findViewById(R.id.buttonBackInfo);
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
