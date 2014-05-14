package serverCommunicator;

import org.json.JSONException;
import org.json.JSONObject;

public class GetPersonalDataHelper 
{
	public String[] parseJSONRespondGetServerName(String input) throws JSONException,NullPointerException
	{
		String[] result= new String[2];
		String nick=null;
		String email=null;
        JSONObject obj= new JSONObject(input);
        nick=(String) obj.get("name");
        email=(String) obj.get("email");
        if(nick==null||email==null)throw new NullPointerException();
        result[0]=nick;
        result[1]=email;
        return result;
	}

}
