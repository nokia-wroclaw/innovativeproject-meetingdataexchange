using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using Microsoft.Phone.Controls;
using MeetingDataExchange.Model;
using System.Collections.ObjectModel;
using System.ComponentModel;

namespace MeetingDataExchange.Pages
{
    public partial class MeetingPage : PhoneApplicationPage, INotifyPropertyChanged
    {
        private MDEDataContext MDEDB;
        private Server server;
        private Meeting meeting;
        private List<File> files;

        public event PropertyChangedEventHandler PropertyChanged;

        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            MDEDB = new MDEDataContext();
            int meetingID = int.Parse( NavigationContext.QueryString["meetingID"] );

            meeting = new ObservableCollection<Meeting>(from Meeting m in MDEDB.Meetings where m.ID == meetingID select m)[0];
            server = meeting.server;
            files = meeting.files.ToList();

            PivotRoot.Title = server.serverName;

            #region Details
            topicBlock.Text = meeting.topic;
            organisatorBlock.Text = meeting.adminName;
            if (meeting.endTime != "")
            {
                timeBlock.Text = meeting.startTime;
                stateBlock.Text = "Trwa (liczba użytkowników: " + meeting.numerOfMembers + " )";
            }
            else
            {
                timeBlock.Text = meeting.startTime + " - " + meeting.endTime;
                stateBlock.Text = "Zakończone (liczba użytkowników: " + meeting.numerOfMembers + " )";
            }
            //permision = 0 - member
            //1 - member upload
            //2 - admin
            if (meeting.permissions == 2)
            {
                permissionBlock.Text = "Admin";
                settingsButton.Visibility = System.Windows.Visibility.Visible;
            }
            else
            {
                settingsButton.Visibility = System.Windows.Visibility.Collapsed;
                if(meeting.permissions == 1)
                    permissionBlock.Text = "Member with upload permission";
                else
                    permissionBlock.Text = "Member without upload permission";
            }
            #endregion

            #region Files

            #endregion

            #region Add
            #endregion
            // Call the base method.
            base.OnNavigatedTo(e);
        }

        public void settingsButtonClicked(Object sender, RoutedEventArgs e)
        {

        }
        public void fileClicked(Object sender, RoutedEventArgs e)
        {

        }
        public void fileHolded(Object sender, RoutedEventArgs e)
        {

        }
        public void addPhotoButtonClicked(Object sender, RoutedEventArgs e)
        {

        }
        public void addFileButtonClicked(Object sender, RoutedEventArgs e)
        {

        }
        public void addNoteButtonClicked(Object sender, RoutedEventArgs e)
        {

        }
        public void addUserButtonClicked(Object sender, RoutedEventArgs e)
        {
            NavigationService.Navigate(new Uri("/Pages/QRCodePage.xaml?meetingID=" + meeting.ID, UriKind.Relative));
        }

        public MeetingPage()
        {
            InitializeComponent();
        }
    }
}