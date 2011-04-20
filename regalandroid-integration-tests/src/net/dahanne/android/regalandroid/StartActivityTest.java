
package net.dahanne.android.regalandroid;

import net.dahanne.android.regalandroid.activity.Start;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;


/**
 * Instrumentation tests for poking some buttons
 *
 */

public class StartActivityTest extends ActivityInstrumentationTestCase2 <Start>{
    public boolean setup = false;
    private static final String TAG = "StartTests";
    Start mActivity = null;
    Instrumentation mInst = null;
    
    public StartActivityTest() {
        super("net.dahanne.android.regalandroid", Start.class);
    }
    
//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();
//        
//        mActivity = getActivity();
//        mInst = getInstrumentation();
//    }
//    
//    @Override
//    protected void tearDown() throws Exception {
//        super.tearDown();
//    }
//    
//    
//    /**
//     * The name 'test preconditions' is a convention to signal that if this
//     * test doesn't pass, the test case was not set up properly and it might
//     * explain any and all failures in other tests.  This is not guaranteed
//     * to run before other tests, as junit uses reflection to find the tests.
//     */
//    @SmallTest
//    public void testPreconditions() {
//    }
//
//    /**
//     * Test basic startup/shutdown of Application
//     */
//    @MediumTest
//    public void testSimpleCreate() {
//        createApplication();
//    }
    
//    @LargeTest
//    public void testPressSomeKeys() {
//        Log.v(TAG, "Pressing some keys!");
//        
//        // Make sure that we clear the output
//        press(KeyEvent.KEYCODE_ENTER);
//        press(KeyEvent.KEYCODE_CLEAR);
//        
//        // 3 + 4 * 5 => 23
//        press(KeyEvent.KEYCODE_3);
//        press(KeyEvent.KEYCODE_PLUS);
//        press(KeyEvent.KEYCODE_4);
//        press(KeyEvent.KEYCODE_9 | KeyEvent.META_SHIFT_ON);
//        press(KeyEvent.KEYCODE_5);
//        press(KeyEvent.KEYCODE_ENTER);
//        
//        assertEquals(displayVal(), "23");
//    }
    
//    @LargeTest
//    public void testTapSomeButtons() {
//        Log.v(TAG, "Tapping some buttons!");
//        
//        // Make sure that we clear the output
//        tap(R.id.equal);
//        tap(R.id.del);
//        
//        // 567 / 3 => 189
//        tap(R.id.digit5);
//        tap(R.id.digit6);
//        tap(R.id.digit7);
//        tap(R.id.div);
//        tap(R.id.digit3);
//        tap(R.id.equal);
//        
//        assertEquals(displayVal(), "189");
//        
//        // make sure we can continue calculations also
//        // 189 - 789 => -600
//        tap(R.id.minus);
//        tap(R.id.digit7);
//        tap(R.id.digit8);
//        tap(R.id.digit9);
//        tap(R.id.equal);
//        
//        // Careful: the first digit in the expected value is \u2212, not "-" (a hyphen)
//        assertEquals(displayVal(), mActivity.getString(R.string.minus) + "600");
//    }
//  
//    // helper functions
//    private void press(int keycode) {
//        mInst.sendKeyDownUpSync(keycode);
//    }
//    
//    private boolean tap(int id) {
//        View view = mActivity.findViewById(id);
//        if(view != null) {
//            TouchUtils.clickView(this, view);
//            return true;
//        }
//        return false;
//    }
//  
//    private String displayVal() {
//        CalculatorDisplay display = (CalculatorDisplay) mActivity.findViewById(R.id.display);
//        assertNotNull(display);
//        
//        EditText box = (EditText) display.getCurrentView();
//        assertNotNull(box);
//        
//        return box.getText().toString();
//    }
}

