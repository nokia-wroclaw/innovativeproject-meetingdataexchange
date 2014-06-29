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
        private MDEDataContext MDEDB;
        private Server server;
        public ServerOptionsPage()
        {
            InitializeComponent();

            MDEDB = new MDEDataContext();
        }

        protected override void OnBackKeyPress(System.ComponentModel.CancelEventArgs e)
        {
            if (Popup.IsOpen)
            {
                Popup.IsOpen = false;
                ContentPanel.Visibility = System.Windows.Visibility.Visible;
                e.Cancel = true;
            }

            base.OnBackKeyPress(e);
        }
        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            var serversInDB = from Server s in MDEDB.Servers
                              select s;

            string serverName = NavigationContext.QueryString["serverName"];
            System.Diagnostics.Debug.WriteLine(serverName);
            if(serverName!="")server = new ObservableCollection<Server>(from Server s in MDEDB.Servers where s.serverName == serverName select s)[0];

            if (server.sid == null)
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
            }
            
            base.OnNavigatedTo(e);
        }

        #region login
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
        #endregion

        #region logout
        private void logoutClicked(Object sender, RoutedEventArgs e)
        {
            server.sid = null;
            MDEDB.SubmitChanges();

            MessageBox.Show("Logged out from server.");
            NavigationService.GoBack();
        }
/*        private void logoutClicked(Object sender, RoutedEventArgs e)
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
 */
        #endregion

        #region delete
        private void deleteClicked(Object sender, RoutedEventArgs e)
        {
            this.Dispatcher.BeginInvoke(delegate()
            {
                var meetings = server.meetings;
                foreach (Meeting meeting in new ObservableCollection<Meeting>(meetings))
                {
                    var files = meeting.files;
                    System.Diagnostics.Debug.WriteLine(meeting.title);
                    MDEDB.Files.DeleteAllOnSubmit(files);
                }
                MDEDB.Meetings.DeleteAllOnSubmit(meetings);
                MDEDB.Servers.DeleteOnSubmit(server);
                MDEDB.SubmitChanges();

                MessageBox.Show("Server deleted.");
                NavigationService.GoBack();
            });
        }
        #endregion

        #region changeAddres
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

        private void editClicked(Object sender, RoutedEventArgs e)
        {
            NavigationService.Navigate(new Uri("/Pages/EditPersonalDataPage.xaml?serverName=" + server.serverName, UriKind.Relative));
        }
        private void cancelClicked(Object sender, RoutedEventArgs e)
        {
            NavigationService.GoBack();
        }
    }
}