using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;

namespace MeetingDataExchange.ServerCommunication
{
    class HttpPostRequest<U, T>
    {
        public delegate void Delegate(T t);
        Delegate del;
        U entity;
        public HttpPostRequest(string url, Delegate del, U entity)
        {
            this.entity = entity;
            this.del = del;
            var request = HttpWebRequest.Create(url);
            request.ContentType = "application/json";
            request.Method = "POST";
            request.BeginGetRequestStream(new AsyncCallback(RequestCallback), request);
        }

        void RequestCallback(IAsyncResult result)
        {
            HttpWebRequest request = result.AsyncState as HttpWebRequest;

            Stream postStream = request.EndGetRequestStream(result);

            string postData = JsonConvert.SerializeObject(entity);
            // Convert the string into a byte array.
            byte[] byteArray = Encoding.UTF8.GetBytes(postData);
            postStream.Write(byteArray, 0, postData.Length);
            postStream.Close();
            System.Diagnostics.Debug.WriteLine("Json sent:\n"+postData);
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
