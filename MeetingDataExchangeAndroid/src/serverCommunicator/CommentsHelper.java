package serverCommunicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.TrololoCompany.meetingdataexchangedataBase.CommentEntity;
import com.TrololoCompany.meetingdataexchangedataBase.FileEntity;
import com.TrololoCompany.meetingdataexchangedataBase.MeetingEntity;

public class CommentsHelper 
{
	
public CommentEntity makeComment(JSONObject json,FileEntity file) throws JSONException
{
		CommentEntity comment= new CommentEntity();
	    comment.setServerCommentID(json.getLong("commentid"));
	    comment.setAuthorName(json.getString("author"));
	    comment.setAddTime(json.getString("addtime"));
	    comment.setContent(json.getString("content"));
	    comment.setFileID(file.getID());
	    return comment;
		
	}

}
