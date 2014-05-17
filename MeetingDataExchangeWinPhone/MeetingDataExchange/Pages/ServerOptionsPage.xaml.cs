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
    public partial class ServerOptionsPage : PhoneApplicationPage
    {
        // Data context for the local database
        private MDEDataContext MDEDB;
        private Server server;
        public ServerOptionsPage()
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
            server = new ObservableCollection<Server>(from Server s in MDEDB.Servers where s.serverName == serverName select s)[0];

            if (server.sid == null)
            {
                Login.Visibility = System.Windows.Visibility.Visible;
                Logout.Visibility = System.Windows.Visibility.Collapsed;
                Edit.Visibility = System.Windows.Visibility.Collapsed;
            }
            else
            {
                Login.Visibility = System.Windows.Visibility.Collapsed;
                Logout.Visibility = System.Windows.Visibility.Visible;
                Edit.Visibility = System.Windows.Visibility.Visible;
            }
            
            // Call the base method.
            base.OnNavigatedTo(e);
        }

        private void loginClicked(Object sender, RoutedEventArgs e)
        {
            //TODO block buttons
            string url = server.address + "/api/general/getname";
            new HttpGetRequest<ServerName>(url, loginCallback);
        }
        private void loginCallback(ServerName output)
        {
            this.Dispatcher.BeginInvoke(delegate()
            {
                if (output == null || output.servername == null)
                {
                    MessageBox.Show("Couldn't connect to server");
                }
                else if (output.servername != server.serverName)
                {
                    MessageBox.Show("Server with this address has different name than the chosen server name");

                }
                else
                {
                    string url = server.address + "/api/account/login";
                    LoginInput input = new LoginInput(server.login, server.pass);
                    new HttpPostRequest<LoginInput, LoginOutput>(url, loginCallback, input);
                }
            });
        }

        private void loginCallback(LoginOutput output)
        {
            this.Dispatcher.BeginInvoke(delegate()
            {
                if (output.status == "ok")
                {
                    server.sid = output.sid;
                    MDEDB.SubmitChanges();

                    MessageBox.Show("Logged in on server.");
                    NavigationService.GoBack();
                }
                else
                {
                    MessageBox.Show("Login or password incorrect.");
                }
            });
        }


        private void logoutClicked(Object sender, RoutedEventArgs e)
        {
            //TODO block buttons
            string url = server.address + "/api/general/getname";
            new HttpGetRequest<ServerName>(url, logoutCallback);
        }
        private void logoutCallback(ServerName output)
        {
            this.Dispatcher.BeginInvoke(delegate()
            {
                if (output == null || output.servername == null)
                {
                    MessageBox.Show("Couldn't connect to server");
                }
                else if (output.servername != server.serverName)
                {
                    MessageBox.Show("Server with this address has different name than the chosen server name");
                }
                else
                {
                    string url = server.address + "/api/account/login";
                    LogoutInput input = new LogoutInput(server.login, server.sid);
                    new HttpPostRequest<LogoutInput, LoginOutput>(url, logoutCallback, input);
                }
            });
        }
        private void logoutCallback(LoginOutput output)
        {
            this.Dispatcher.BeginInvoke(delegate()
            {

                server.sid = null;
                MDEDB.SubmitChanges();

                MessageBox.Show("Logged out from server.");
                NavigationService.GoBack();
            });
        }
        private void editClicked(Object sender, RoutedEventArgs e)
        {
            NavigationService.GoBack();
        }
        private void deleteClicked(Object sender, RoutedEventArgs e)
        {
            this.Dispatcher.BeginInvoke(delegate()
            {
                var servers = from Server s in MDEDB.Servers where s.serverName == server.serverName select s;
                MDEDB.Servers.DeleteAllOnSubmit(servers);
                //var server = new ObservableCollection<Server>(servers)[0];
                var meetings = from Meeting m in MDEDB.Meetings where m.server.name == server.serverName select m;
                MDEDB.Meetings.DeleteAllOnSubmit(meetings);
                foreach (Meeting meeting in new ObservableCollection<Meeting>(meetings))
                {
                    var files = from File f in MDEDB.Files where f.meeting == meeting select f;
                    MDEDB.Meetings.DeleteAllOnSubmit(meetings);
                    //TODO - delete files
                }
                //MDEDB.SubmitChanges(); //They will be submited after logout succeed
                logoutClicked(sender, e);
            });
        }
        private void cancelClicked(Object sender, RoutedEventArgs e)
        {
            NavigationService.GoBack();
        }

        /*private void serverClicked(Object sender, RoutedEventArgs e)
        {
            serverButtons.IsOpen = true;
            serverButtons.Tag = ((Button)sender).Tag;
            ContentPanel.Visibility = System.Windows.Visibility.Collapsed;
            var server = new ObservableCollection<Server>(from Server s in MDEDB.Servers where s.serverName == (string)serverButtons.Tag select s)[0];
            if (server.sid == null)
            {
                Login.Visibility = System.Windows.Visibility.Visible;
                Logout.Visibility = System.Windows.Visibility.Collapsed;
                Edit.Visibility = System.Windows.Visibility.Collapsed;
            }
            else
            {
                Login.Visibility = System.Windows.Visibility.Collapsed;
                Logout.Visibility = System.Windows.Visibility.Visible;
                Edit.Visibility = System.Windows.Visibility.Visible;
            }
        }*/

    }
}