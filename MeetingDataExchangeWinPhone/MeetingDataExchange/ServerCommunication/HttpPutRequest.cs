using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;


namespace MeetingDataExchange.ServerCommunication
{

    /**
     * Put contrary to post is not using json parser to put data - it just send given stream
     */
    class HttpPutRequest<T>
    {
        public delegate void Delegate(T t);
        Delegate del;
        //string entity;
        byte[] byteArray;
        int length;


        public HttpPutRequest(string url, Delegate del, string entity)
        {
            byteArray = Encoding.UTF8.GetBytes(entity);
            this.del = del;
            var request = HttpWebRequest.Create(url);
            request.ContentType = "application/binary";
            request.Method = "PUT";
            request.BeginGetRequestStream(new AsyncCallback(RequestCallback), request);
        }

        public HttpPutRequest(string url, Delegate del, byte[] byteArray, int length)
        {
            this.byteArray = byteArray;
            this.length = length;
            this.del = del;
            var request = HttpWebRequest.Create(url);
            request.ContentType = "application/binary";
            request.Method = "PUT";
            request.BeginGetRequestStream(new AsyncCallback(RequestCallback), request);
        }

        void RequestCallback(IAsyncResult result)
        {
            HttpWebRequest request = result.AsyncState as HttpWebRequest;

            Stream postStream = request.EndGetRequestStream(result);

            //string postData = JsonConvert.SerializeObject(entity);
            // Convert the string into a byte array.
            postStream.Write(byteArray, 0, length);
            postStream.Close();
            System.Diagnostics.Debug.WriteLine("Data sent");
            request.BeginGetResponse(new AsyncCallback(ResponseCallback), request);
        }

        void ResponseCallback(IAsyncResult result)
        {
            HttpWebRequest request = (HttpWebRequest)result.AsyncState;
            try
            {
                HttpWebResponse response = (HttpWebResponse)request.EndGetResponse(result);
                Stream streamResponse = response.GetResponseStream();
                StreamReader streamRead = new StreamReader(streamResponse);
                string responseText = streamRead.ReadToEnd();
                System.Diagnostics.Debug.WriteLine("JSON received:\n"+responseText);
                del(JsonConvert.DeserializeObject<T>(responseText));
            }
            catch (WebException e)
            {
                System.Diagnostics.Debug.WriteLine(e.Message);
                System.Diagnostics.Debug.WriteLine("WebException!");
                del(JsonConvert.DeserializeObject<T>(""));
            }
        }
    }
}
