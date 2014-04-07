package meeting_options;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
public class MeetingTabPagerAdapter extends FragmentStatePagerAdapter 
{
    public MeetingTabPagerAdapter(FragmentManager fm) 
    {
    	super(fm);
    
    }
  @Override
  public Fragment getItem(int i) {
    switch (i) {
        case 0:
            return new MeetingProgress();
        case 1:
            return new MeetingDescription();
        case 2:
            return new MeetingAddItems();
        }
    return null;
  }
  @Override
  public int getCount() 
  {
   
    return 3; 
  }
}