/*
 * This is an example test project created in Eclipse to test NotePad which is a sample 
 * project located in AndroidSDK/samples/android-11/NotePad
 * Just click on File --> New --> Project --> Android Project --> Create Project from existing source and
 * select NotePad.
 * 
 * Then you can run these test cases either on the emulator or on device. You right click
 * the test project and select Run As --> Run As Android JUnit Test
 * 
 * @author Renas Reda, renas.reda@jayway.com
 * 
 */

package net.dahanne.android.regalandroid.its;

import junit.framework.AssertionFailedError;
import net.dahanne.android.regalandroid.R;
import net.dahanne.android.regalandroid.activity.Start;

import com.jayway.android.robotium.solo.Solo;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.Smoke;


public class ReGalAndroidTest extends ActivityInstrumentationTestCase2<Start>{

	private Solo solo;

	public ReGalAndroidTest() {
		super("net.dahanne.android.regalandroid", Start.class);

	}
	@Override
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}


	@Smoke
	public void testEnterSettingsAndConfigurePiwigoGallery() throws Exception {
		try{
			solo.clickOnButton(this.getActivity().getString(R.string.accept));
		}catch (AssertionFailedError e){
			//OK, this is not the first time this activity is run
		}
		solo.clickOnMenuItem(this.getActivity().getString(R.string.settings_label));
		
		//Assert that Settings activity is opened
		solo.assertCurrentActivity("Expected ReGalAndroid activity", "Settings");
		
		//Choose Piwigo Gallery as gallerytype
		solo.clickOnText(this.getActivity().getString(R.string.gallery_connection_type_title));
		solo.clickOnText(this.getActivity().getString(R.string.gallery_connection_type_piwigo));
		
		//set user as username
		solo.clickOnText(this.getActivity().getString(R.string.username_title));
		solo.clearEditText(0);
		solo.enterText(0,"user");
		solo.clickOnButton(this.getActivity().getString(R.string.ok));
		
		//set password as password
		solo.clickOnText(this.getActivity().getString(R.string.password_title));
		solo.clearEditText(0);
		solo.enterText(0,"password");
		solo.clickOnButton(this.getActivity().getString(R.string.ok));
		

	}

	@Override
	public void tearDown() throws Exception {
		//Robotium will finish all the activities that have been opened
		solo.finishOpenedActivities();
	}
}
