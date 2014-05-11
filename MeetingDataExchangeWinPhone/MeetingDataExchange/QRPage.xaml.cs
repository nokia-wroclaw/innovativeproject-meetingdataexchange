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

namespace MeetingDataExchange
{
    public partial class QRPage : PhoneApplicationPage
    {
        private readonly DispatcherTimer _timer;
        private readonly ObservableCollection<string> _matches;

        private PhotoCameraLuminanceSource _luminance;
        private QRCodeReader _reader;
        private PhotoCamera _photoCamera;
        public QRPage()
        {
            InitializeComponent();
            _matches = new ObservableCollection<string>();
            qrMatchesList.ItemsSource = _matches;

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
                    Dispatcher.BeginInvoke(() => DisplayResult(result.Text));
            }
            catch
            {
            }
        }

        private void DisplayResult(string text)
        {
//            if(text!= null)
            if (!_matches.Contains(text))
                _matches.Add(text);
        }
    }
}