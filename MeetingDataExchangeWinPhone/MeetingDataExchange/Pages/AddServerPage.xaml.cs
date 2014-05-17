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

        private void connect(object sender, RoutedEventArgs e)
        {
            if (!serverAddressBox.Text.StartsWith("http://")) serverAddressBox.Text = "http://" + serverAddressBox.Text;
            serverUrl = serverAddressBox.Text;
            string url = serverUrl + "/api/general/getname";
            new HttpGetRequest<ServerName>(url, connectCallback);
            serverAddressBox.IsEnabled = false;
            connectProgressBar.Visibility = System.Windows.Visibility.Visible;
        }

        private void connectCallback(ServerName result)
        {
            this.Dispatcher.BeginInvoke(delegate()
                {
                    serverAddressBox.IsEnabled = true;
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
                            MessageBox.Show("You already have account on this server. If you want to change to another one, please remove server from application. "+
                            "All data on the device will be lost. Data on the server will remain unchanged.");
                            connectedPanel.Visibility = System.Windows.Visibility.Collapsed;
                        }
                        else
                        {
                            connectedPanel.Visibility = System.Windows.Visibility.Visible;
                        }
                    }

                }); 
        }

        private void tglSwitch_Checked(object sender, RoutedEventArgs e)
        {
            tglSwitch.Content = "Yes";
            namePanel.Visibility = System.Windows.Visibility.Collapsed;
            mailPanel.Visibility = System.Windows.Visibility.Collapsed;
            repPasswordPanel.Visibility = System.Windows.Visibility.Collapsed;
            button.Content = "Log in";
        }

        private void tglSwitch_Unchecked(object sender, RoutedEventArgs e)
        {
            tglSwitch.Content = "No";
            namePanel.Visibility = System.Windows.Visibility.Visible;
            mailPanel.Visibility = System.Windows.Visibility.Visible;
            repPasswordPanel.Visibility = System.Windows.Visibility.Visible;
            button.Content = "Register";
        }

        private void button_Click(object sender, RoutedEventArgs e)
        {
            button.IsEnabled = false;
            buttonProgressBar.Visibility = System.Windows.Visibility.Visible;
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

        private void loginCallback(LoginOutput output)
        {
            this.Dispatcher.BeginInvoke(delegate()
            {
                buttonProgressBar.Visibility = System.Windows.Visibility.Collapsed;
                if (output.status == "ok")
                {
                    var servers = new ObservableCollection<Server>(from Server s in MDEDB.Servers where s.serverName == serverNameBox.Text select s);
                    Server server;
                    if (servers.Count() > 0)
                    {
                        MessageBox.Show("Wow, you shouldn't be here, how have you done that?!");
                    }
                    else
                    {
                        server = new Server();
                        MDEDB.Servers.InsertOnSubmit(server);
                        server.serverName = serverNameBox.Text;
                        server.address = serverAddressBox.Text;
                        server.login = loginBox.Text;
                        server.name = nameBox.Text;
                        server.pass = passwordBox.Password;
                        server.sid = output.sid;
                        System.Diagnostics.Debug.WriteLine(server.address + "\n" + server.login + "\n" + server.name);
                        MDEDB.SubmitChanges();

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


        private void register()
        {
            if (passwordBox.Password != repPasswordBox.Password)
            {
                this.Dispatcher.BeginInvoke(delegate()
                {
                    MessageBox.Show("These passwords are different.");
                });
                passwordBox.Password = repPasswordBox.Password = "";
                button.IsEnabled = true;
            }
            else if(loginBox.Text==""||nameBox.Text==""||mailBox.Text==""||passwordBox.Password=="")
            {
                this.Dispatcher.BeginInvoke(delegate()
                {
                    MessageBox.Show("All fields must not be empty.");
                });
                passwordBox.Password = repPasswordBox.Password = "";
                button.IsEnabled = true;
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
                        button.IsEnabled = true;
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
                        MessageBox.Show("Login or password incorrect.");
                        passwordBox.Password = repPasswordBox.Password = "";
                        button.IsEnabled = true;
                    }
                });
        }

    }
}