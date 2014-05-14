using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;

namespace MeetingDataExchange.ServerCommunication
{
    class ServerSession
    {
        public ServerSession(IPEndPoint ipEndPoint)
        {
            string url = "http://" + ipEndPoint.ToString() + "/api/general/getname";

            new HttpGetRequest<ServerName>(url, next);

            url = "http://" + ipEndPoint.ToString() + "/api/account/login";
            
            new HttpPostRequest<LoginInput,LoginOutput>(url, next2,new LoginInput("123","placek"));

        }
        void next(ServerName result)
        {
            if (result != null)
                System.Diagnostics.Debug.WriteLine(result.servername);
            else
                System.Diagnostics.Debug.WriteLine("Unable to connect to server");
        }

        void next2(LoginOutput result)
        {
            if (result != null)
                System.Diagnostics.Debug.WriteLine(result.status+" "+result.sid+" "+result.sid);
            else
                System.Diagnostics.Debug.WriteLine("Unable to connect to server");
        }
    }
}
