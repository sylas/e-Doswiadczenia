/**
 * 
 */
package pl.gda.pg.mif.edoswiadczenia;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
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
}


