package meeting_options;

import android.app.ActionBar;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class MyOnPageChangeListener implements OnPageChangeListener
{

private ActionBar bar;
public MyOnPageChangeListener(ActionBar bar)
{
	this.bar=bar;
}

@Override
public void onPageScrollStateChanged(int arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void onPageScrolled(int arg0, float arg1, int arg2) {
	// TODO Auto-generated method stub
	
}

@Override
public void onPageSelected(int arg0) {
	  bar.setSelectedNavigationItem(arg0);
	
}
}
