using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;

namespace MeetingDataExchange.ServerCommunication
{
    class HttpGetRequest<T>
    {
        public delegate void Delegate(T t);
        Delegate delegat;
        public HttpGetRequest (string url,Delegate delegat)
        {
            try
            {
                this.delegat = delegat;
                var httpWebRequest = HttpWebRequest.Create(url);
                httpWebRequest.Method = "GET";
                httpWebRequest.BeginGetResponse(ResponseCallback, httpWebRequest);
            }
            catch(Exception)
            {
                delegat(JsonConvert.DeserializeObject<T>(""));
            }
        }

        void ResponseCallback(IAsyncResult result)
        {
           HttpWebRequest request = result.AsyncState as HttpWebRequest;
            if (request != null)
            {
                try
                {
                    WebResponse response = request.EndGetResponse(result);
                    using (var streamReader = new StreamReader(response.GetResponseStream()))
                    {
                        var responseText = streamReader.ReadToEnd();
                       delegat(JsonConvert.DeserializeObject<T>(responseText));
                    }
                }
                catch (WebException)
                {
                    delegat(JsonConvert.DeserializeObject<T>(""));
                }

            }
        }
    }
}
