package pl.gda.pg.mif.edoswiadczenia;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.*;
import android.widget.*;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class PortRandomization {

	
	private int id;
	private Context mContext;
	private ConnectivityManager connection;
	private WifiManager wifi;
		
	
	public void prepareSystem(){
		connection = (ConnectivityManager)mContext.getSystemService("connection");
		wifi = (WifiManager)mContext.getSystemService("wifi"); 

		
	}
	
}
