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

namespace MeetingDataExchange.Pages
{
    public partial class AddNotePage : PhoneApplicationPage
    {
        private MDEDataContext MDEDB;
        private Meeting meeting;

        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            MDEDB = new MDEDataContext();
            int meetingID = int.Parse(NavigationContext.QueryString["meetingID"]);

            meeting = new ObservableCollection<Meeting>(from Meeting m in MDEDB.Meetings where m.ID == meetingID select m)[0];
            //server = meeting.server;
            //files = meeting.files.ToList();

            serverNameBlock.Text = meeting.server.serverName;
            meetingNameBlock.Text = meeting.title;

            base.OnNavigatedTo(e);
        }

        public AddNotePage()
        {
            InitializeComponent();
        }
    }
}