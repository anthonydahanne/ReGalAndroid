/**
 *  ReGalAndroid, a gallery client for Android, supporting G2, G3, etc...
 *  URLs: https://github.com/anthonydahanne/ReGalAndroid , http://blog.dahanne.net
 *  Copyright (c) 2010 Anthony Dahanne
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.dahanne.android.regalandroid.activity;

import net.dahanne.android.regalandroid.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;

//public class FirstTime extends Activity implements OnClickListener {
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.firsttime);
//		Button button = (Button) findViewById(R.id.ok_id);
//		button.setOnClickListener(this);
//
//	}
//
//	public void onClick(View v) {
//
//		FirstTimePreference.setFirstTimeTrue(this);
//		this.finish();
//
//	}
//}

/**
 * THIS CLASS WAS COPIED FROM THE GPL project Astrid
 * https://github.com/todoroo/astrid
 * 
* Displays an EULA ("End User License Agreement") that the user has to accept
* before using the application. Your application should call
* {@link Eula#showEula(android.app.Activity)} in the onCreate() method of the
* first activity. If the user accepts the EULA, it will never be shown again.
* If the user refuses, {@link android.app.Activity#finish()} is invoked on your
* activity.
*/
class FirstTime {
    private static final String PREFERENCE_EULA_ACCEPTED = "eula.accepted"; //$NON-NLS-1$
    private static final String PREFERENCES_EULA = "eula"; //$NON-NLS-1$

    /**
* Displays the EULA if necessary. This method should be called from the
* onCreate() method of your main Activity.
*
* @param activity
* The Activity to finish if the user rejects the EULA
*/
    static void showEula(final Activity activity) {
        final SharedPreferences preferences = activity.getSharedPreferences(
                PREFERENCES_EULA, Activity.MODE_PRIVATE);
        if (preferences.getBoolean(PREFERENCE_EULA_ACCEPTED, false)) {
            return;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.firsttime_title);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.accept,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        accept(activity, preferences);
                    }
                });
        builder.setNegativeButton(R.string.decline,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        refuse(activity);
                    }
                });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                refuse(activity);
            }
        });
        builder.setMessage(R.string.what_is_regalandroid);
        builder.show();
    }

    @SuppressWarnings("unused")
    private static void accept(Activity activity, SharedPreferences preferences) {
        preferences.edit().putBoolean(PREFERENCE_EULA_ACCEPTED, true).commit();
    }

    private static void refuse(Activity activity) {
        activity.finish();
    }

    private FirstTime() {
        // don't construct me
    }
}
