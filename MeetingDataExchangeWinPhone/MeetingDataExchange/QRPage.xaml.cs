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
using System.Windows.Media.Imaging;
namespace MeetingDataExchange
{
    public partial class QRPage : PhoneApplicationPage
    {
        private PhotoCamera camera;
        private bool capturing = false;
        public QRPage()
        {
            InitializeComponent();
        }

/*        protected override void OnNavigatedTo(
           System.Windows.Navigation.NavigationEventArgs e)
        {
            if (null == camera)
            {
                camera = new PhotoCamera();
                // filred when the camera is initialised
                camera.Initialized += camera_Initialised;
                // fired when the button is fully pressed
                CameraButtons.ShutterKeyPressed += camera_ButtonFullPress;
                // fired when an image is available.
                camera.CaptureImageAvailable += camera_CaptureImageAvailable;
                // set the VideoBrush source to the camera output
                videoRotateTransform.CenterX = videoRectangle.Width / 2;
                videoRotateTransform.CenterY = videoRectangle.Height / 2;
                videoRotateTransform.Angle = 90;
                viewfinderBrush.SetSource(camera);
            }
            base.OnNavigatedTo(e);
        }

        // user navigated away from page
        protected override void OnNavigatedFrom(
                   System.Windows.Navigation.NavigationEventArgs e)
        {
            if (camera != null)
            {
                // unhook the event handlers
                CameraButtons.ShutterKeyPressed -= camera_ButtonFullPress;
                camera.CaptureImageAvailable -= camera_CaptureImageAvailable;
                camera.Initialized -= camera_Initialised;
                // dispose of the camera object
                camera.Dispose();
            }
            base.OnNavigatedFrom(e);
        }

        private void camera_Initialised(object sender, CameraOperationCompletedEventArgs e)
        {
            // set the camera resolution
            if (e.Succeeded)
            {
                var res = from resolution in camera.AvailableResolutions
                          where resolution.Width == 640
                          select resolution;
                camera.Resolution = res.First();
            }
        }
        // user has pressed the camera button

        private void camera_ButtonFullPress(object sender, EventArgs e)
        {
            if (capturing) return;
            capturing = true;
            camera.CaptureImage();
        }

        private void camera_CaptureImageAvailable(
            object sender,
            ContentReadyEventArgs e)
        {
           capturing = false;
            
            Stream imageStream = (Stream)e.ImageStream;
            //BarcodeDecoder barcodeDecoder = new BarcodeDecoder();
            QRCodeImageReader qrreader = new QRCodeImageReader();
            Dictionary<DecodeOptions, object> decodingOptions =
                                new Dictionary<DecodeOptions, object>();
            List<BarcodeFormat> possibleFormats = new List<BarcodeFormat>(1);
            Result result;

            Dispatcher.BeginInvoke(() =>
            {
                WriteableBitmap qrImage = new WriteableBitmap(
                                                (int)camera.Resolution.Width,
                                                (int)camera.Resolution.Height);
                imageStream.Position = 0;
                qrImage.LoadJpeg(imageStream);

                possibleFormats.Add(BarcodeFormat.QRCode);
                decodingOptions.Add(
                    DecodeOptions.PossibleFormats,
                    possibleFormats);
                try
                {
                    result = barcodeDecoder.Decode(qrImage, decodingOptions);
                    resultText.Text = result.Text;
                }

                catch (NotFoundException)
                {
                    // this is expected if the image does not contain a valid
                    // code, Or is too distorted to read
                    resultText.Text = "<nothing to display>";
                }

                catch (Exception ex)
                {
                    // something else went wrong, so alert the user
                    MessageBox.Show(
                        ex.Message,
                        "Error Decoding Image",
                        MessageBoxButton.OK);
                }
            });
        }*/
    }
}