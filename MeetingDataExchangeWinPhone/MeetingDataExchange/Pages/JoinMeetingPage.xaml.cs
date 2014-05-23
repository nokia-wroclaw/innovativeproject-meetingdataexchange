using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using Microsoft.Devices;
using System.Collections.ObjectModel;
using System.IO;
using System.Windows.Threading;
using System.Windows.Media.Imaging;
using ZXing;
using ZXing.QrCode;
using ZXing.Common;
using MeetingDataExchange.ServerCommunication;
using MeetingDataExchange.Model;

namespace MeetingDataExchange
{
    public partial class JoinMeetingPage : PhoneApplicationPage
    {
        private readonly DispatcherTimer _timer;
        //private readonly ObservableCollection<string> _matches;

        private MDEDataContext MDEDB;
        private PhotoCameraLuminanceSource _luminance;
        private QRCodeReader _reader;
        private PhotoCamera _photoCamera;
        private string text;
        private Server server;

        public JoinMeetingPage()
        {
            InitializeComponent();
//            _matches = new ObservableCollection<string>();
//            qrMatchesList.ItemsSource = _matches;

            MDEDB = new MDEDataContext();

            _timer = new DispatcherTimer();
            _timer.Interval = TimeSpan.FromMilliseconds(250);
            _timer.Tick += (o, arg) => ScanPreviewBuffer();
        }

        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            _photoCamera = new PhotoCamera();
            _photoCamera.Initialized += OnPhotoCameraInitialized;
            qrPreviewVideo.SetSource(_photoCamera);

            CameraButtons.ShutterKeyHalfPressed += (o, arg) => _photoCamera.Focus();

            base.OnNavigatedTo(e);
        }

        private void OnPhotoCameraInitialized(object sender, CameraOperationCompletedEventArgs e)
        {
            int width = Convert.ToInt32(_photoCamera.PreviewResolution.Width);
            int height = Convert.ToInt32(_photoCamera.PreviewResolution.Height);
            _luminance = new PhotoCameraLuminanceSource(width, height);
            _reader = new QRCodeReader();

            Dispatcher.BeginInvoke(() =>
            {
                qrPreviewTransform.Rotation = _photoCamera.Orientation;
                _timer.Start();
            });
        }

        private void ScanPreviewBuffer()
        {
            try
            {
                _photoCamera.GetPreviewBufferY(_luminance.PreviewBufferY);
                var binarizer = new HybridBinarizer(_luminance);
                var binBitmap = new BinaryBitmap(binarizer);
                var result = _reader.decode(binBitmap);
                if(result!=null)
                    Dispatcher.BeginInvoke(() => ResultReady(result.Text));
            }
            catch
            {
            }
        }

        private void ResultReady(string text)
        {
            if (text != null)
            {
                _timer.Stop();
                this.text = text;
                if (text.Split(';')[0] == "mde")
                {
                    string serverUrl = text.Split(';')[1];
                    string url = serverUrl + "/api/general/getname";
                    new HttpGetRequest<ServerName>(url, joinMeetingCallback);
                }
                else
                {
                    MessageBox.Show("Incorrect QR code.");
                    _timer.Start();
                }
            }
        }

        private void joinMeetingCallback(ServerName result)
        {
            this.Dispatcher.BeginInvoke(delegate()
               {
                   if (result == null)
                   {
                       MessageBox.Show("Couldn't connect to server");
                       _timer.Start();
                   }
                   else if (result.servername == null)
                   {
                       MessageBox.Show("Incorrect server response, please contact with administrator or try again later.");
                       _timer.Start();
                   }
                   else
                   {
                       var servers = new ObservableCollection<Server>(from Server s in MDEDB.Servers where s.serverName == result.servername select s);
                       if (servers.Count() > 0)
                       {

                           server = servers[0];
                           if (server.sid == null)
                           {
                               MessageBox.Show("You are logged out from server, please log in.");
                               _timer.Start();
                           }
                           else
                           {
                               JoinMeetingInput input = new JoinMeetingInput();
                               input.login = server.login;
                               input.sid = server.sid;
                               input.meetingid = text.Split(';')[2];
                               input.accessCode = text.Split(';')[3];

                               string url = text.Split(';')[1] + "/api/meeting/adduser";
                               new HttpPostRequest<JoinMeetingInput, MeetingOutput>(url, joinMeetingCallback,input);

                           }
                       }
                       else
                       {
                           //TODO go to registration page instead telling a user to do it
                           MessageBox.Show("You don't have account on this server. Please add server on server management tab");
                           _timer.Start();
                       }
                   }
               });
        }
        private void joinMeetingCallback(MeetingOutput output)
        {
            this.Dispatcher.BeginInvoke(delegate()
               {
                   if (output.status == "OK")
                   {
                       server.address = text.Split(';')[1];
                       Meeting meeting=new Meeting();
                       meeting.server = server;
                       meeting.serverMeetingID = Convert.ToInt32(output.meetingid);
                       meeting.title = output.title;
                       meeting.topic = output.title;
                       meeting.adminName = server.name;
                       meeting.startTime = output.starttime;
                       meeting.numerOfMembers = Convert.ToInt32(output.members);
                       meeting.permissions = output.permissions=="memberUpload"? 1 : (output.permissions=="member" ? 0:2);
                       meeting.code = output.accessCode;
                       MDEDB.Meetings.InsertOnSubmit(meeting);
                       MDEDB.SubmitChanges();
                   }
                   else
                   {
                       MessageBox.Show("Unable to join meeting.\nServer response:\n" + output.status);
                       _timer.Start();
                   }
               });
        }


    }
}