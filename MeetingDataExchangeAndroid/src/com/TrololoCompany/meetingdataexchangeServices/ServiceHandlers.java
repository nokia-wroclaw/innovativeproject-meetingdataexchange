package com.TrololoCompany.meetingdataexchangeServices;

import java.util.HashMap;

import android.app.Service;

public class ServiceHandlers {
	 
    private boolean communicationServerService;
    private boolean refreshMeetingDetails;
    private boolean refreshMeetingProgress;
    
    private ServiceHandlers() {}
 
    private static class SingletonHolder 
    { 
        private final static ServiceHandlers instance = new ServiceHandlers();
    }
 
    public static ServiceHandlers getInstance() 
    {
        return SingletonHolder.instance;
    }

	public boolean isCommunicationServerService() {
		return communicationServerService;
	}

	public void setCommunicationServerService(boolean communicationServerService) {
		this.communicationServerService = communicationServerService;
	}

	public boolean isRefreshMeetingDetails() {
		return refreshMeetingDetails;
	}

	public void setRefreshMeetingDetails(boolean refreshMeetingDetails) {
		this.refreshMeetingDetails = refreshMeetingDetails;
	}

	public boolean isRefreshMeetingProgress() {
		return refreshMeetingProgress;
	}

	public void setRefreshMeetingProgress(boolean refreshMeetingProgress) {
		this.refreshMeetingProgress = refreshMeetingProgress;
	}
    
    
}
