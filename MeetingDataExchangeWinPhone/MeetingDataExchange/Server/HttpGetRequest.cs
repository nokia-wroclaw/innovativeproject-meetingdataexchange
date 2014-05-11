using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;

namespace MeetingDataExchange.Server
{
    class HttpGetRequest<T>
    {
        public delegate void Delegate(T t);
        Delegate del;
        public HttpGetRequest (string url,Delegate del)
        {
            this.del = del;
            var httpWebRequest = HttpWebRequest.Create(url);
            httpWebRequest.Method = "GET";
            httpWebRequest.BeginGetResponse(ResponseCallback, httpWebRequest);
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
                       del(JsonConvert.DeserializeObject<T>(responseText));
                    }
                }
                catch (WebException e)
                {
                    del(JsonConvert.DeserializeObject<T>(""));
                }

            }
        }
    }
}
