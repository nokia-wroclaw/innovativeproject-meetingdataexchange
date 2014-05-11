using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;

namespace MeetingDataExchange.Server
{
    public class ServerName
    {
        public string servername { get; set; }
    }

    public class LoginInput
    {
        private string p1;
        private string p2;

        public LoginInput(string login, string password)
        {
            this.login = login;
            this.password = password;
        }
        public string login { get; set; }
        public string password { get; set; }
    }

    public class LoginOutput
    {
        public string status { get; set; }
        public string sid { get; set; }
        public string reason { get; set; }
    }

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
