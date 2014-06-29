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
using System.Windows.Threading;

namespace MeetingDataExchange.Pages
{
    public partial class MeetingsPage : PhoneApplicationPage, INotifyPropertyChanged
    {
        private MDEDataContext MDEDB;
        private Server server;

        private readonly DispatcherTimer _timer;


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

            server = new ObservableCollection<Server>(from Server s in MDEDB.Servers where s.serverName == serverName select s)[0];

            serverNameTextBox.Text = serverName;

            meetings = new ObservableCollection<Meeting>(server.meetings);
            refresh();
            _timer.Start();

            base.OnNavigatedTo(e);
        }


        protected override void OnNavigatedFrom(System.Windows.Navigation.NavigationEventArgs e)
        {
            _timer.Stop();
            base.OnNavigatedFrom(e);
        }

        public MeetingsPage()
        {
            InitializeComponent();

            this.DataContext = this;
            
            _timer = new DispatcherTimer();
            _timer.Interval = TimeSpan.FromMilliseconds(10000);
            _timer.Tick += (o, arg) => refresh();
        }
        
        public void meetingClicked(Object sender, RoutedEventArgs e)
        {
            NavigationService.Navigate(new Uri("/Pages/MeetingPage.xaml?meetingID=" + ((Button)sender).Tag, UriKind.Relative));//
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

        private void setControlEnabled(bool isEnabled)
        {
            progressBar.Visibility = isEnabled ? Visibility.Collapsed : Visibility.Visible;
        }

        public void refresh()
        {
            setControlEnabled(false);
            string url = server.address + "/api/meeting/list/" + server.login + "/" + server.sid;
            new HttpGetRequest<MeetingsListOutput>(url, meetingListCallback);
        }

        private void meetingListCallback(MeetingsListOutput output)
        {
            this.Dispatcher.BeginInvoke(delegate()
                {
                    if (output == null)
                    {
                        //MessageBox.Show("Error communicating with server. Check your internet connection and try again.");
                        connectionFailureTextBlock.Visibility = System.Windows.Visibility.Visible;
                    }
                    else if (output.status == "ok")
                    {
                        connectionFailureTextBlock.Visibility = System.Windows.Visibility.Collapsed;
                        foreach (MeetingOutput meetingOutput in output.meetings)
                        {
                            var meeting = (from Meeting m in MDEDB.Meetings where m.serverMeetingID == meetingOutput.meetingid select m);
                            if (meeting.Count() == 0)
                            {
                                MDEDB.Meetings.InsertOnSubmit(meetingOutput.getEntity(server));
                            }
                            else
                            {
                                Meeting m1 = meeting.ToList()[0];
                                Meeting m2 = meetingOutput.getEntity(server);
                                m1.adminName = m2.adminName;
                                m1.topic = m2.topic;
                                m1.title = m1.title;
                                m1.numerOfMembers = m2.numerOfMembers;
                                m1.permissions = m2.permissions;
                                m1.startTime = m2.startTime;
                                m1.endTime = m2.endTime;
                            }
                        }
                        MDEDB.SubmitChanges();
                        meetings = new ObservableCollection<Meeting>(from Meeting m in MDEDB.Meetings
                                                                     where m.server == server
                                                                     select m);
                    }
                    else
                    {
                        MessageBox.Show("Unable to refresh.\nServer response:\n" + output.reason);
                    }
                    setControlEnabled(true);
                });
        }

    }
}