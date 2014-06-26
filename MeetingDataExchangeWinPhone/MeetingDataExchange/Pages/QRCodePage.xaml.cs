using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using MeetingDataExchange.Model;
using System.Collections.ObjectModel;
using System.Windows.Media.Imaging;
using ZXing;

namespace MeetingDataExchange
{
    public partial class QRCodePage : PhoneApplicationPage
    {
        private MDEDataContext MDEDB;
        private Meeting meeting;
        //private List<File> files;

        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            MDEDB = new MDEDataContext();
            int meetingID = int.Parse(NavigationContext.QueryString["meetingID"]);

            meeting = new ObservableCollection<Meeting>(from Meeting m in MDEDB.Meetings where m.ID == meetingID select m)[0];
            //server = meeting.server;
            //files = meeting.files.ToList();

            serverNameBlock.Text = meeting.server.serverName;
            meetingNameBlock.Text = meeting.title;

            imgQRCode.Source = GenerateQRCode("mde;"+meeting.server.address+";"+meeting.serverMeetingID+";"+meeting.code);
            base.OnNavigatedTo(e);
        }
        public QRCodePage()
        {
            InitializeComponent();
        }

        private static WriteableBitmap GenerateQRCode(string text)
        {
            BarcodeWriter _writer = new BarcodeWriter();

            /*_writer.Renderer = new ZXing.Rendering.WriteableBitmapRenderer()
            {
                Foreground = System.Windows.Media.Color.FromArgb(255, 0, 0, 255) // blue
            };*/

            _writer.Format = BarcodeFormat.QR_CODE;


            _writer.Options.Height = 400;
            _writer.Options.Width = 400;
            _writer.Options.Margin = 10;

            var barcodeImage = _writer.Write(text);

            return barcodeImage;
        }

    }
}