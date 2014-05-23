using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using System.ComponentModel;
using MeetingDataExchange.Model;
using System.Collections.ObjectModel;
using MeetingDataExchange.ServerCommunication;

namespace MeetingDataExchange.Pages
{
    public partial class MeetingsPage : PhoneApplicationPage, INotifyPropertyChanged
    {
        // Data context for the local database
        private MDEDataContext MDEDB;


        // Define an observable collection property that controls can bind to.
        private ObservableCollection<Meeting> _meetings;
        public ObservableCollection<Meeting> meetings
        {
            get
            {
                return _meetings;
            }
            set
            {
                if (_meetings != value)
                {
                    _meetings = value;
                    NotifyPropertyChanged("meetings");
                }
            }
        }

        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            MDEDB = new MDEDataContext();
            string serverName = NavigationContext.QueryString["serverName"];


            /*Meeting meeting = new Meeting();
            Server server = new ObservableCollection<Server>(from Server s in MDEDB.Servers where s.serverName == serverName select s)[0];
            meeting.server = server;
            server.meetings.Add(meeting);
            meeting.topic = "LOL";

            MDEDB.Meetings.InsertOnSubmit(meeting);
            MDEDB.SubmitChanges();
            */

            serverNameTextBox.Text = serverName;
            // Define the query to gather all of the to-do items.
            var meetingsInDB = from Meeting m in MDEDB.Meetings
                                where m.server.serverName==serverName select m;


            // Execute the query and place the results into a collection.
            meetings= new ObservableCollection<Meeting>(meetingsInDB);
            // Call the base method.
            base.OnNavigatedTo(e);
        }

        public MeetingsPage()
        {
            InitializeComponent();

            // Data context and observable collection are children of the main page.
            this.DataContext = this;
        }
        
        public void meetingClicked(Object sender, RoutedEventArgs e)
        {
            //TODO
            //NavigationService.Navigate(new Uri("/Pages/MeetingsPage.xaml?serverName=" + ((Button)sender).Tag, UriKind.Relative));//
        }
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

    }
}