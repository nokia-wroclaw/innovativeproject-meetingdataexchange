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
    public partial class EditMeetingSettingsPage : PhoneApplicationPage
    {
        private MDEDataContext MDEDB;
        private Meeting meeting;

        public EditMeetingSettingsPage()
        {
            InitializeComponent();

            MDEDB = new MDEDataContext();
        }


        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            MDEDB = new MDEDataContext();
            
            int meetingID = int.Parse(NavigationContext.QueryString["meetingID"]);
            meeting = new ObservableCollection<Meeting>(from Meeting m in MDEDB.Meetings where m.ID == meetingID select m)[0];


            base.OnNavigatedTo(e);
        }

        private void button_Click(object sender, RoutedEventArgs e)
        {
            button.IsEnabled = false;
            buttonProgressBar.Visibility = System.Windows.Visibility.Visible;

            string url = meeting.server.address + "/api/meeting/stop";
            StopInput input;

            input = new StopInput(meeting.server.login, meeting.server.sid, meeting.serverMeetingID.ToString());

            new HttpPostRequest<StopInput, StatusReasonOutput>(url, editCallback, input);
        }

        private void editCallback(StatusReasonOutput output)
        {
            this.Dispatcher.BeginInvoke(delegate()
            {
                buttonProgressBar.Visibility = System.Windows.Visibility.Collapsed;
                button.IsEnabled = true;
                if (output == null)
                {
                    MessageBox.Show("Couldn't connect to server");
                }
                else if (output.status == "ok")
                {
                    MessageBox.Show("Meeting stopped.");
                    NavigationService.GoBack();
                }
                else
                {
                    MessageBox.Show(output.reason);
                }
            });

        }
    }
}