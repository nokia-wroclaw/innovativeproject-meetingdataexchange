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
using MeetingDataExchange.ServerCommunication;

namespace MeetingDataExchange.Pages
{
    public partial class MeetingPage : PhoneApplicationPage, INotifyPropertyChanged
    {
        private MDEDataContext MDEDB;
        private Server server;
        private Meeting meeting;

        #region INotifyPropertyChanged Members

        public event PropertyChangedEventHandler PropertyChanged;

        // Used to notify the app that a property has changed.
        private void NotifyPropertyChanged(string propertyName)
        {
            if (PropertyChanged != null)
            {
                PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
            }
        }
        #endregion

        private ObservableCollection<File> _files;
        public ObservableCollection<File> files
        {
            get
            {
                return _files;
            }
            set
            {
                if (_files != value)
                {
                    _files = value;
                    NotifyPropertyChanged("files");
                }
            }
        }

        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            MDEDB = new MDEDataContext();
            int meetingID = int.Parse( NavigationContext.QueryString["meetingID"] );

            meeting = new ObservableCollection<Meeting>(from Meeting m in MDEDB.Meetings where m.ID == meetingID select m)[0];
            server = meeting.server;
            files = new ObservableCollection<File>(meeting.files);
            System.Diagnostics.Debug.WriteLine("Count1 = " + files.Count());

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
            refresh();
            #endregion

            #region Add
            #endregion
            // Call the base method.
            base.OnNavigatedTo(e);
        }

        public MeetingPage()
        {
            InitializeComponent();

            // Data context and observable collection are children of the main page.
            this.DataContext = this;
        }

        public void refresh()
        {
            string url = server.address + "/api/files/list/" + meeting.serverMeetingID + "/" + server.login + "/" + server.sid;
            new HttpGetRequest<FilesListOutput>(url, filesListCallback);
        }

        private void filesListCallback(FilesListOutput output)
        {
            this.Dispatcher.BeginInvoke(delegate()
            {
                if (output == null)
                {
                    MessageBox.Show("Error communicating with server. Check your internet connection and try again.");
                }
                else if (output.status == "ok")
                {
                    foreach (FileOutput fileOutput in output.files)
                    {
                        var file = (from File f in MDEDB.Files where f.serverFileID == fileOutput.fileid select f);
                        System.Diagnostics.Debug.WriteLine("Count = " + file.Count());
                        if (file.Count() == 0)
                        {
                            MDEDB.Files.InsertOnSubmit(fileOutput.getEntity(meeting));
                        }
                    }
                    MDEDB.SubmitChanges();
                    /*files = 
                        new ObservableCollection<Meeting>(from Meeting m in MDEDB.Meetings
                                                                 where m.server == server
                                                                 select m);*/
                }
                else
                {
                    MessageBox.Show("Unable to refresh.\nServer response:\n" + output.reason);
                }
            });
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
            NavigationService.Navigate(new Uri("/Pages/AddNotePage.xaml?meetingID=" + meeting.ID, UriKind.Relative));
        }
        public void addUserButtonClicked(Object sender, RoutedEventArgs e)
        {
            NavigationService.Navigate(new Uri("/Pages/QRCodePage.xaml?meetingID=" + meeting.ID, UriKind.Relative));
        }
 
    }
}