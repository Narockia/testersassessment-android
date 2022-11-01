package com.abnamro.apps.referenceandroid.nancy.test;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
/**This Class contains testcases for testing ReferenceAndroid App.
 * package(App) name : com.abnamro.apps.referenceandroid
 * Variable used for UiDevice and Launch timeout.
 */
public class testReferenceAndroidApp {
    private static final String BASIC_SAMPLE_PACKAGE
            = "com.abnamro.apps.referenceandroid";

    private static final int LAUNCH_TIMEOUT = 5000;

    private UiDevice mDevice;

    /** Before condition is usually set to launch the device app and clear previous open instances,
     * before the actual UiTesting starts.
     * This method starts from home screen and opens the app and wait for the Home screen to appear
     */
    @Before
    public void startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // Launch the Reference Android app
        Context context = getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    /** Checking preconditions like app is not empty
     */
    @Test
    public void checkPreconditions() {

        assertThat(mDevice, notNullValue());
    }

    /** Test to verify the Hello World message is displayed. contentMessage string variable has
     * text content displayed in the app and assert the string variable has the same message
     */

    @Test
    public void verifyHelloWorldIsDisplayed() {
        // Verify Hello World! text is displayed
        final String contentMessage = mDevice.findObject(By.text("Hello World!")).getText();
        Assert.assertEquals(contentMessage,("Hello World!"));
    }

    /** Test to verify the ReferenceAndroid title in the toolbar. toolBarTitle string variable has
     * title content from toolbar and assert the title is same as ReferenceAndroid
     */

    @Test
    public void verifyReferenceAndroidTitleIsDisplayed() {
        // Verify Reference Android title is Visible in the tool bar
        assertThat("mDevice.findObject(By.res(\"com.abnamro.apps.referenceandroid\",\"toolbar\"))",notNullValue());
        final String toolBarTitle = mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE,"toolbar")).getChildren().get(0).getText();
        Assert.assertEquals(toolBarTitle,("ReferenceAndroid"));
    }

    /**Test to click the mailIcon and verify the message displayed. Clicking mail icon shows a
     * snack-bar and message is displayed. verifying the message displayed in snack-bar using assert
     * equals.
     */
    @Test
    public void testMailIconAndSnackBar(){
        // click mail icon and verify snack-bar and message is displayed
        assertThat("mDevice.findObject(By.res(\"com.abnamro.apps.referenceandroid\",\"fab\"))",notNullValue());
        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE,"fab")).click();
        mDevice.wait(Until.findObject(By.res(BASIC_SAMPLE_PACKAGE,"snackbar_text")),500);
        final String snackBarMessage = mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE,"snackbar_text")).getText();
        Assert.assertEquals(snackBarMessage, ("Replace with your own action"));
    }

    /**
     * Uses package manager to find the package name of the device launcher. This is a generic solution which
     * works on all platforms.`
    */
    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = getApplicationContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }

}
