package meeting_options;

import android.app.ActionBar;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class MeetDetOnPageListener implements OnPageChangeListener
{
	private ActionBar actionBar;
	public MeetDetOnPageListener(ActionBar actionBar)
	{
		this.actionBar=actionBar;
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
	public void onPageSelected(int position) {
		
        actionBar.setSelectedNavigationItem(position);
		
	}

}
