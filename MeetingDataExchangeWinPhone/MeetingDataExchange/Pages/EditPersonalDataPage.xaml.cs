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
    public partial class EditPersonalDataPage : PhoneApplicationPage
    {
        private MDEDataContext MDEDB;
        private Server server;

        public EditPersonalDataPage()
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
            server = new ObservableCollection<Server>(from Server s in MDEDB.Servers where s.serverName == serverName select s)[0];
            nameBox.Text = server.name;
            mailBox.Text = server.email;
            // Call the base method.
            base.OnNavigatedTo(e);
        }

        private void button_Click(object sender, RoutedEventArgs e)
        {
            button.IsEnabled = false;
            buttonProgressBar.Visibility = System.Windows.Visibility.Visible;

            string url = server.address + "/api/account/setdata";
            EditInput input;
            if (newPasswordBox.Password == repNewPasswordBox.Password)
            {
                if (newPasswordBox.Password == "")
                    input = new EditInput(server.login, server.sid, nameBox.Text, mailBox.Text, null);
                else
                    input = new EditInput(server.login, server.sid, nameBox.Text, mailBox.Text, newPasswordBox.Password);

                new HttpPostRequest<EditInput, EditOutput>(url, editCallback, input);
            }
            else
            {
                MessageBox.Show("These passwords are different.");
                newPasswordBox.Password = repNewPasswordBox.Password = "";
                button.IsEnabled = true;
            }
        }

        private void editCallback(EditOutput output)
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
                    var servers = new ObservableCollection<Server>(from Server s in MDEDB.Servers where s.serverName == server.serverName select s);
                    server.name = nameBox.Text;
                    server.pass = newPasswordBox.Password;
                    MDEDB.SubmitChanges();

                    MessageBox.Show("Logged in on server.");
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