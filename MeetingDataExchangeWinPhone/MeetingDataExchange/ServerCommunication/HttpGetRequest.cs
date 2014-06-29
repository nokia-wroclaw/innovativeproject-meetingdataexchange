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
                if (httpWebRequest.Headers == null)
                    httpWebRequest.Headers = new WebHeaderCollection();
                httpWebRequest.Headers[HttpRequestHeader.IfModifiedSince] = DateTime.UtcNow.ToString();
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
                        System.Diagnostics.Debug.WriteLine("JSON received:\n" + responseText);
                       delegat(JsonConvert.DeserializeObject<T>(responseText));
                    }
                }
                catch (WebException)
                {
                    System.Diagnostics.Debug.WriteLine("WebException!");
                    delegat(JsonConvert.DeserializeObject<T>(""));
                }

            }
        }
    }
}
