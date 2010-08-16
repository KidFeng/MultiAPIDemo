package net.kid.android.multiapi;

import android.test.ActivityInstrumentationTestCase2;
/**
 * try to startup Home Activity
 * @author Kid Feng <Kid.Stargazer@gmail.com>
 * create@ Aug 14, 2010 11:09:19 PM
 */
public class HomeTest extends ActivityInstrumentationTestCase2<Home> {
	public HomeTest() {
		super("net.kid.android.multiapi", Home.class);
	}
	
	public void testActivity() {
		assertNotNull("activity should be launched successfully", getActivity());
	}
}
