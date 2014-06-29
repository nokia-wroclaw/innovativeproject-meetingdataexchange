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

        private MDEDataContext MDEDB;
        private PhotoCameraLuminanceSource _luminance;
        private QRCodeReader _reader;
        private PhotoCamera _photoCamera;
        private string text;
        private Server server;

        public JoinMeetingPage()
        {
            InitializeComponent();

            MDEDB = new MDEDataContext();
            server = new Server();
            _timer = new DispatcherTimer();
            _timer.Interval = TimeSpan.FromMilliseconds(100);
            _timer.Tick += (o, arg) => ScanPreviewBuffer();
        }

        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            _photoCamera = new PhotoCamera();
            _photoCamera.Initialized += OnPhotoCameraInitialized;
            qrPreviewVideo.SetSource(_photoCamera);

            CameraButtons.ShutterKeyHalfPressed += (o, arg) => _photoCamera.Focus();
            if (server.name != null)
                joinMeeting();
            base.OnNavigatedTo(e);
        }

        private void OnPhotoCameraInitialized(object sender, CameraOperationCompletedEventArgs e)
        {
            try
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
            catch
            {
                _timer.Stop();
            }
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
            if (text != null && (this.text == null || this.text != text) )
            {
                _timer.Stop();
                this.text = text;
                if (text.Split(';')[0] == "mde")
                {
                    string serverUrl = text.Split(';')[1];
                    server.address = serverUrl;
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

        private void joinMeeting()
        {
            JoinMeetingInput input = new JoinMeetingInput();
            server = new ObservableCollection<Server>(from Server s in MDEDB.Servers where s.serverName == server.name select s)[0];
            input.login = server.login;
            input.sid = server.sid;
            input.meetingid = text.Split(';')[2];
            input.accessCode = text.Split(';')[3];

            string url = text.Split(';')[1] + "/api/meeting/adduser";
            new HttpPostRequest<JoinMeetingInput, MeetingOutput>(url, joinMeetingCallback, input);
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
                    server.name = result.servername;
                    var servers = new ObservableCollection<Server>(from Server s in MDEDB.Servers where s.serverName == result.servername select s);
                    if (servers.Count() > 0)
                    {

                        server = servers[0];
                        if (server.sid == null)
                        {
                            MessageBoxResult messageResult =
                                MessageBox.Show("You are not logged in on server. Would you like to log in?",
                                "", MessageBoxButton.OKCancel);

                            if (messageResult == MessageBoxResult.OK)
                            {
                                string url = server.address + "/api/account/login";
                                LoginInput input = new LoginInput(server.login, server.pass);
                                new HttpPostRequest<LoginInput, LoginOutput>(url, loginCallback, input);
                            }
                            else
                            {
                                _timer.Start();
                                text = "";
                            }
                        }
                        else
                        {
                            joinMeeting();
                        }
                    }
                    else
                    {
                        //TODO go to registration page instead telling a user to do it
                        MessageBox.Show("You are not logged in on server. Please register or log in to server.");
                        NavigationService.Navigate(new Uri("/Pages/AddServerPage.xaml?serverAddress=" + server.address + "&serverName=" + server.name, UriKind.Relative));
                    }
                }
            });
        }

        private void loginCallback(LoginOutput output)
        {
            this.Dispatcher.BeginInvoke(delegate()
            {
                if (output.status == "ok")
                {
                    server.sid = output.sid;
                    MDEDB.SubmitChanges();

                    MessageBox.Show("Logged in on server.");
                    joinMeeting();
                }
                else
                {
                    MessageBox.Show("Login or password incorrect.");
                }
            });
        }

        private void joinMeetingCallback(MeetingOutput output)
        {
            this.Dispatcher.BeginInvoke(delegate()
               {
                   if (output.status == "ok")
                   {
                       Meeting meeting = output.getEntity(server);
//                       MDEDB.Meetings.InsertOnSubmit(output.getEntity(server));
                       MDEDB.Meetings.InsertOnSubmit(meeting);
                       MDEDB.SubmitChanges();
                       System.Diagnostics.Debug.WriteLine(meeting.ID);

                       //TODO go to the meeting page instead
                       MessageBox.Show("Succesfully joined the meeting.");
                       NavigationService.Navigate(new Uri("/Pages/MeetingPage.xaml?meetingID=" + meeting.ID + "&removePrevious=" + bool.TrueString, UriKind.Relative));
                   }
                   else
                   {
                       MessageBox.Show("Unable to join meeting.\nServer response:\n" + output.reason);
                       _timer.Start();
                   }
               });
        }


    }
}