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
using System.IO;
using System.IO.IsolatedStorage;
using MeetingDataExchange.ServerCommunication;
using MeetingDataExchange.Model;
using System.Collections.ObjectModel;

namespace MeetingDataExchange
{
    public partial class AddPhotoPage : PhoneApplicationPage
    {
        private int savedCounter = 0;
        PhotoCamera cam;

        private MDEDataContext MDEDB;
        private Meeting meeting;

        public AddPhotoPage()
        {
            InitializeComponent();
        }

        // Update the UI if initialization succeeds.
        void cam_Initialized(object sender, Microsoft.Devices.CameraOperationCompletedEventArgs e)
        {
            if (e.Succeeded)
            {
                this.Dispatcher.BeginInvoke(delegate()
                {
                    ShutterButton.Visibility = System.Windows.Visibility.Visible;
                });
            }
        }

        //Code for initialization, capture completed, image availability events; also setting the source for the viewfinder.
        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            MDEDB = new MDEDataContext();
            int meetingID = int.Parse(NavigationContext.QueryString["meetingID"]);

            meeting = new ObservableCollection<Meeting>(from Meeting m in MDEDB.Meetings where m.ID == meetingID select m)[0];

            // Check to see if the camera is available on the phone.
            if ((PhotoCamera.IsCameraTypeSupported(CameraType.Primary) == true) ||
                 (PhotoCamera.IsCameraTypeSupported(CameraType.FrontFacing) == true))
            {
                // Initialize the camera, when available.
                if (PhotoCamera.IsCameraTypeSupported(CameraType.FrontFacing))
                {
                    // Use front-facing camera if available.
                    cam = new Microsoft.Devices.PhotoCamera(CameraType.FrontFacing);
                }
                else
                {
                    // Otherwise, use standard camera on back of phone.
                    cam = new Microsoft.Devices.PhotoCamera(CameraType.Primary);
                }

                // Event is fired when the PhotoCamera object has been initialized.
                cam.Initialized += new EventHandler<Microsoft.Devices.CameraOperationCompletedEventArgs>(cam_Initialized);

                // Event is fired when the capture sequence is complete and an image is available.
                cam.CaptureImageAvailable += new EventHandler<Microsoft.Devices.ContentReadyEventArgs>(cam_CaptureImageAvailable);

                //Set the VideoBrush source to the camera.
                viewfinderBrush.SetSource(cam);
            }
            else
            {
                // The camera is not supported on the phone.
                this.Dispatcher.BeginInvoke(delegate()
                {
                    // Write message.
                    txtDebug.Text = "A Camera is not available on this phone.";
                    // Disable UI.
                    ShutterButton.IsEnabled = false;
                });

            }
        }
        protected override void OnNavigatingFrom(System.Windows.Navigation.NavigatingCancelEventArgs e)
        {
            if (cam != null)
            {
                // Dispose camera to minimize power consumption and to expedite shutdown.
                cam.Dispose();

                // Release memory, ensure garbage collection.
                cam.Initialized -= cam_Initialized;
                cam.CaptureImageAvailable -= cam_CaptureImageAvailable;
            }
        }


        // Informs when full resolution photo has been taken, saves to local media library and the local folder.
        void cam_CaptureImageAvailable(object sender, Microsoft.Devices.ContentReadyEventArgs e)
        {

            try
            {
               // e.ImageStream.Seek(0, SeekOrigin.Begin);

                byte[] image = new byte[e.ImageStream.Length];
                e.ImageStream.Read(image, 0, image.Length);
                
                this.Dispatcher.BeginInvoke(delegate()
                {
                    ShutterButton.IsEnabled = false;
                });
                string url = meeting.server.address + "/api/upload/" + meeting.server.login + "/" + meeting.server.sid + "/" + meeting.serverMeetingID + "/" + DateTime.Now.ToString("yyMMddHHmmss") + ".jpg";
                System.Diagnostics.Debug.WriteLine(url);
                new HttpPutRequest<StatusReasonOutput>(url, addPhotoCallback, image, (int)e.ImageStream.Length);
            }
            finally
            {
                e.ImageStream.Close();
            }

        }

        private void addPhotoCallback(StatusReasonOutput output)
        {
            this.Dispatcher.BeginInvoke(delegate()
            {
                ShutterButton.IsEnabled = true;
                if (output == null)
                {
                    MessageBox.Show("Couldn't connect to server");
                }
                else if (output.status == "ok")
                {
                    MessageBox.Show("Photo added.");
                    NavigationService.GoBack();
                }
                else
                {
                    MessageBox.Show(output.reason);
                }
            });

        }

        private void ShutterButton_Click(object sender, RoutedEventArgs e)
        {
            System.Diagnostics.Debug.WriteLine("ShutterButton_Click");
            if (cam != null)
            {
                try
                {
                    // Start image capture.
                    cam.CaptureImage();
                }
                catch (Exception ex)
                {
                    this.Dispatcher.BeginInvoke(delegate()
                    {
                        // Cannot capture an image until the previous capture has completed.
                        txtDebug.Text = ex.Message;
                    });
                }
            }
        }
    }
}