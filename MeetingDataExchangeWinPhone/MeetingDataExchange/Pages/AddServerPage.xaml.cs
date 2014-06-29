using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Controls.Primitives;
using Microsoft.Phone.Shell;
using MeetingDataExchange.ServerCommunication;
using MeetingDataExchange.Model;
using System.Windows.Controls.Primitives;
using System.Collections.ObjectModel;

namespace MeetingDataExchange.Pages
{
    public partial class AddServerPage : PhoneApplicationPage
    {
        private string serverUrl;
        private MDEDataContext MDEDB;

        public AddServerPage()
        {
            InitializeComponent();

            MDEDB = new MDEDataContext();

            this.DataContext = this;
        }

        private void connect(Object sender, RoutedEventArgs e)
        {
            if (!serverAddressBox.Text.StartsWith("http://")) serverAddressBox.Text = "http://" + serverAddressBox.Text;
            serverUrl = serverAddressBox.Text;
            string url = serverUrl + "/api/general/getname";
            new HttpGetRequest<ServerName>(url, connectCallback);
            serverAddressBox.IsEnabled = false;
            connectButton.IsEnabled = false;
            connectProgressBar.Visibility = System.Windows.Visibility.Visible;
        }

        private void connectCallback(ServerName result)
        {
            this.Dispatcher.BeginInvoke(delegate()
                {
                    serverAddressBox.IsEnabled = true;
                    connectButton.IsEnabled = true;
                    connectProgressBar.Visibility = System.Windows.Visibility.Collapsed;
                    if (result == null)
                    {
                        connectedPanel.Visibility = System.Windows.Visibility.Collapsed;
                        MessageBox.Show("Couldn't connect to server");
                    }
                    else if (result.servername == null)
                    {
                        connectedPanel.Visibility = System.Windows.Visibility.Collapsed;
                        MessageBox.Show("Incorrect server response, please contact with administrator or try again later.");
                    }
                    else
                    {
                        serverNameBox.Text = result.servername;
                        var servers = new ObservableCollection<Server>(from Server s in MDEDB.Servers where s.serverName == serverNameBox.Text select s);
                        if (servers.Count() > 0)
                        {
                            Server server = servers[0];
                            if (server.address == serverAddressBox.Text)
                                MessageBox.Show("You already have account on this server. \n If you want to change to another one, please remove server from application. " +
                            "All data on the device will be lost. Data on the server will remain unchanged.");
                            else
                            {
                                server.address = serverAddressBox.Text;
                                MDEDB.SubmitChanges();
                                MessageBox.Show("You already have account on this server, under other address. Address in application had been automatically changed. \n If you want to change to another account, please remove server from application. " +
                            "All data on the device will be lost. Data on the server will remain unchanged.");
                            }
                            connectedPanel.Visibility = System.Windows.Visibility.Collapsed;
                        }
                        else
                        {
                            connectedPanel.Visibility = System.Windows.Visibility.Visible;
                        }
                    }

                }); 
        }

        private void tglSwitch_Checked(Object sender, RoutedEventArgs e)
        {
            tglSwitch.Content = "Yes";
            namePanel.Visibility = System.Windows.Visibility.Collapsed;
            mailPanel.Visibility = System.Windows.Visibility.Collapsed;
            repPasswordPanel.Visibility = System.Windows.Visibility.Collapsed;
            button.Content = "Log in";
        }

        private void tglSwitch_Unchecked(Object sender, RoutedEventArgs e)
        {
            tglSwitch.Content = "No";
            namePanel.Visibility = System.Windows.Visibility.Visible;
            mailPanel.Visibility = System.Windows.Visibility.Visible;
            repPasswordPanel.Visibility = System.Windows.Visibility.Visible;
            button.Content = "Register";
        }

        private void button_Click(Object sender, RoutedEventArgs e)
        {
            setControlEnabled(false);
            if ((bool)(tglSwitch.IsChecked))
            {
                login();
            }
            else
            {
                register();
            }
            
        }

        private void login()
        {
            string url = serverUrl + "/api/account/login";
            LoginInput input = new LoginInput(loginBox.Text, passwordBox.Password);
            new HttpPostRequest<LoginInput, LoginOutput>(url, loginCallback, input);
        }

        private void setControlEnabled(bool isEnabled)
        {
            button.IsEnabled = isEnabled;
            buttonProgressBar.Visibility = isEnabled ? Visibility.Collapsed : Visibility.Visible;
        }

        private void loginCallback(LoginOutput output)
        {
            this.Dispatcher.BeginInvoke(delegate()
            {
                setControlEnabled(true);
                if (output.status == "ok")
                {
                    var servers = new ObservableCollection<Server>(from Server s in MDEDB.Servers where s.serverName == serverNameBox.Text select s);
                    Server server;
                    if (servers.Count() > 0)
                    {
                        //We are here right after registration. We should only add sid.
                        server = servers[0];
                        server.sid = output.sid;
                        MDEDB.SubmitChanges();
                        NavigationService.GoBack();
                    }
                    else
                    {
                        server = new Server();
                        MDEDB.Servers.InsertOnSubmit(server);
                        server.serverName = serverNameBox.Text;
                        server.address = serverAddressBox.Text;
                        server.login = loginBox.Text;
                        server.pass = passwordBox.Password;
                        server.sid = output.sid;

                        MDEDB.SubmitChanges();

                        string url = serverUrl + "/api/account/getdata/" + loginBox.Text + "/" + output.sid;
                        new HttpGetRequest<PersonalDataOutput>(url, personalDataCallback);

                        MessageBox.Show("Logged in on server.");
                        NavigationService.GoBack();
                    }
                }
                else
                {
                    MessageBox.Show("Login or password incorrect.");
                }
            });

        }

        private void personalDataCallback(PersonalDataOutput output)
        {
            if (output.status == "ok")
            {
                this.Dispatcher.BeginInvoke(delegate()
                {
                    var servers = new ObservableCollection<Server>(from Server s in MDEDB.Servers where s.serverName == serverNameBox.Text select s);
                    Server server = servers[0];
                    server.name = output.name;
                    server.email = output.email;

                    MDEDB.SubmitChanges();
                });
            }
        }


        private void register()
        {
            if (passwordBox.Password != repPasswordBox.Password)
            {
                this.Dispatcher.BeginInvoke(delegate()
                {
                    MessageBox.Show("These passwords are different.");
                });
                passwordBox.Password = repPasswordBox.Password = "";
                setControlEnabled(true);
            }
            else if(loginBox.Text==""||nameBox.Text==""||mailBox.Text==""||passwordBox.Password=="")
            {
                this.Dispatcher.BeginInvoke(delegate()
                {
                    MessageBox.Show("All fields must not be empty.");
                });
                passwordBox.Password = repPasswordBox.Password = "";
                setControlEnabled(true);
            }
            else
            {
                string url = serverUrl + "/api/account/register";
                RegistrationInput input = new RegistrationInput(loginBox.Text, nameBox.Text, mailBox.Text, passwordBox.Password);
                new HttpPostRequest<RegistrationInput, RegistrationOutput>(url, registerCallback, input);
            }
        }

        private void registerCallback(RegistrationOutput output)
        {
            this.Dispatcher.BeginInvoke(delegate()
                {
                    if (output == null)
                    {
                        MessageBox.Show("Error communicating with server. Check your internet connection and try again.");
                        passwordBox.Password = repPasswordBox.Password = "";
                        setControlEnabled(true);
                    }
                    else if (output.status == "ok")
                    {

                        MessageBox.Show("Registerred on server.");
                        var servers = from Server s in MDEDB.Servers where s.serverName == serverNameBox.Text select s;
                        Server server;
                        if (servers.Count() > 0)
                        {
                            MessageBox.Show("Wow, you shouldn't be here, how have you done that?!");
                        }
                        else
                        {
                            server=new Server();
                            server.address = serverUrl;
                            server.serverName = serverNameBox.Text;
                            server.login = loginBox.Text;
                            server.name = nameBox.Text;
                            server.email = mailBox.Text;
                            server.pass = passwordBox.Password;
                            MDEDB.Servers.InsertOnSubmit(server);
                            MDEDB.SubmitChanges();
                            login();
                        }

                    }
                    else
                    {
                        MessageBox.Show("Error: " + output.reason);
                        passwordBox.Password = repPasswordBox.Password = "";
                        setControlEnabled(true);
                    }
                });
        }

    }
}