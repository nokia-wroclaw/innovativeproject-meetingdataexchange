package meeting_options;

public class RefreshMeetingProgressListView implements Runnable
{
	private MeetingProgress frag;
	public RefreshMeetingProgressListView(MeetingProgress frag)
	{
		this.frag=frag;
	}
	@Override
	public void run() 
	{
		
		frag.refreshList();
	}

}
