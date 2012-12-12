/**
 * 
 */
package pl.gda.pg.mif.edoswiadczenia;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import pl.gda.pg.mif.edoswiadczenia.InfoForUser.myDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author maja
 *
 */
public class InfoForUser extends Activity{
	
	private Context mContext = getApplicationContext();
	private Map<Integer, CharSequence> information;
	
	public InfoForUser(){
		information.put(0, "Application doesn't have premission to write on external storage.");
		information.put(1, "External Storage isn't available.");
		information.put(2, "Flash not installed! " +
    		"This application require Adobe Flash Player. " +
    		"Downloading will start automatically.");
		
	}

	//TODO komunikat do usera
	//jezeli coś nie tak z kartą sd, to pokazuje userowi komunikat
	void  pushInfoToUser(int mCase, int duration){		
		Toast toast = Toast.makeText(mContext, information.get(mCase), duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	public class myDialog extends DialogFragment {
		    private EditText mEditText;
		 // Empty constructor required for DialogFragment
		    public myDialog() {
		        
		    }

/*		    @Override
		    public View onCreateView(LayoutInflater inflater, ViewGroup container,
		            Bundle savedInstanceState) {
		        View view = inflater.inflate(R.layout.fragment_edit_name, container);
		        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
		        getDialog().setTitle("Hello");

		        return view;
		    }*/

		@Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
			// 1. Instantiate an AlertDialog.Builder with its constructor
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			// 3. Get the AlertDialog from create()
			return builder.create();
	    }
	}
}

/*new AlertDialog.Builder(ListED.this).setTitle(getString(R.string.msg_title_question)).setMessage(getString(R.string.msg_install_flash_question)).setPositiveButton(getString(R.string.btn_yes),
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
        }).setIcon(R.drawable.ic_question).show();*/
