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
    public partial class NewMeetingPage : PhoneApplicationPage
    {
        // Data context for the local database
        private MDEDataContext MDEDB;
        private Server server;
        public NewMeetingPage()
        {
            InitializeComponent();

            MDEDB = new MDEDataContext();
        }

        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            // Define the query to gather all of the to-do items.
            var serversInDB = from Server s in MDEDB.Servers
                              select s;

            string serverName = NavigationContext.QueryString["serverName"];
            System.Diagnostics.Debug.WriteLine(serverName);
            if(serverName!="")server = new ObservableCollection<Server>(from Server s in MDEDB.Servers where s.serverName == serverName select s)[0];
            serverNameBox.Text = serverName;
            /*if (server.sid == null)
            {
                Login.Visibility = System.Windows.Visibility.Visible;
                ChangeAddress.Visibility = System.Windows.Visibility.Visible;
                Logout.Visibility = System.Windows.Visibility.Collapsed;
                Edit.Visibility = System.Windows.Visibility.Collapsed;
            }
            else
            {
                Login.Visibility = System.Windows.Visibility.Collapsed;
                ChangeAddress.Visibility = System.Windows.Visibility.Collapsed;
                Logout.Visibility = System.Windows.Visibility.Visible;
                Edit.Visibility = System.Windows.Visibility.Visible;
            }*/
            
            // Call the base method.
            base.OnNavigatedTo(e);
        }

        void setControlEnabled(bool isEnabled)
        {
            titleBox.IsEnabled = isEnabled;
            topicBox.IsEnabled = isEnabled;
            tglSwitch.IsEnabled = isEnabled;
            createMeetingButton.IsEnabled = isEnabled;
            progressBar.Visibility= isEnabled ? Visibility.Collapsed : Visibility.Visible;
        }


        private void createMeetingCliked(Object sender, RoutedEventArgs e)
        {
            setControlEnabled(false);
            string url = server.address + "/api/general/getname";
            new HttpGetRequest<ServerName>(url, createMeetingCallback);
        }


        private void createMeetingCallback(ServerName output)
        {
            this.Dispatcher.BeginInvoke(delegate()
            {
                if (output == null || output.servername == null)
                {
                    MessageBox.Show("Couldn't connect to server");

                    setControlEnabled(true);
                }
                else if (output.servername != server.serverName)
                {
                    MessageBox.Show("Server with this address has different name than the chosen server name");

                    setControlEnabled(true);
                }
                else
                {
                    string url = server.address + "/api/meeting/new";
                    NewMeetingInput newMeetingInput = new NewMeetingInput();
                    newMeetingInput.login = server.login;
                    newMeetingInput.sid = server.sid;
                    newMeetingInput.title = titleBox.Text;
                    newMeetingInput.topic = topicBox.Text;
                    newMeetingInput.abilityToSendFiles = (bool)tglSwitch.IsChecked ? "true" : "false";//Convert.ToString(tglSwitch.IsChecked); 
                    new HttpPostRequest<NewMeetingInput, MeetingOutput>(url, createMeetingCallback, newMeetingInput);
                }
            });
        }

        private void createMeetingCallback(MeetingOutput output)
        {
            this.Dispatcher.BeginInvoke(delegate()
            {
                if (output.status == "ok")
                {
                    Meeting meeting = new Meeting();
                    meeting.server = server;
                    meeting.serverMeetingID = Convert.ToInt32(output.meetingid);
                    meeting.title = output.title;
                    meeting.topic = output.topic;
                    meeting.adminName = output.hostname;
                    meeting.startTime = output.starttime;
                    meeting.numerOfMembers = 1;
                    meeting.permissions = 2;
                    meeting.code = output.accessCode;
                    MDEDB.Meetings.InsertOnSubmit(meeting);
                    MDEDB.SubmitChanges();

                    MessageBox.Show("Meeting created");
                    //TODO navigation to meeting page, perfectly to QR code 
                    NavigationService.GoBack();
                }
                else
                {
                    MessageBox.Show("Unable to join meeting.\nServer response:\n" + output.reason);

                    setControlEnabled(true);
                }
            });
        }

        private void tglSwitch_Checked(Object sender, RoutedEventArgs e)
        {
            tglSwitch.Content = "Yes";
        }

        private void tglSwitch_Unchecked(Object sender, RoutedEventArgs e)
        {
            tglSwitch.Content = "No";
        }
       /* #region changeAddres
        private void changeAddressClicked(Object sender, RoutedEventArgs e)
        {
            Popup.IsOpen = true;
            ContentPanel.Visibility = System.Windows.Visibility.Collapsed;
        }
        private void changeAddressClicked2(Object sender, RoutedEventArgs e)
        {
            if (!changeAddressBox.Text.StartsWith("http://")) changeAddressBox.Text = "http://" + changeAddressBox.Text;
            string url = changeAddressBox.Text + "/api/general/getname";
            new HttpGetRequest<ServerName>(url, connectCallback);
            changeAddressBox.IsEnabled = false;
            ChangeAddress2.IsEnabled = false;
            connectProgressBar.Visibility = System.Windows.Visibility.Visible;
        }

        private void connectCallback(ServerName result)
        {
            this.Dispatcher.BeginInvoke(delegate()
            {
                changeAddressBox.IsEnabled = true;
                ChangeAddress2.IsEnabled = true;
                connectProgressBar.Visibility = System.Windows.Visibility.Collapsed;
                if (result == null)
                {
                    MessageBox.Show("Couldn't connect to server");
                }
                else if (result.servername == null)
                {
                    MessageBox.Show("Incorrect server response, please contact with administrator or try again later.");
                }
                else
                {
                    if (result.servername != server.serverName)
                        MessageBox.Show("Server with this address is other than chosen one. Please add it as new serwer.");
                    else
                    {
                        MessageBox.Show("Server address changed.");
                        server.address = changeAddressBox.Text;
                        MDEDB.SubmitChanges();
                        Popup.IsOpen = false;
                        ContentPanel.Visibility = System.Windows.Visibility.Visible;
                    }

                }

            });
        }

        #endregion
        */
    }
}