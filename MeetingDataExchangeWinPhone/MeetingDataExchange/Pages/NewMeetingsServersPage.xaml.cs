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
    public partial class NewMeetingsServersPage : PhoneApplicationPage, INotifyPropertyChanged
    {
        // Data context for the local database
        private MDEDataContext MDEDB;


        // Define an observable collection property that controls can bind to.
        private ObservableCollection<Server> _servers;
        public ObservableCollection<Server> servers
        {
            get
            {
                return _servers;
            }
            set
            {
                if (_servers != value)
                {
                    _servers = value;
                    NotifyPropertyChanged("servers");
                }
            }
        }

        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            MDEDB = new MDEDataContext();

            // Define the query to gather all of the to-do items.
            var serversInDB = from Server s in MDEDB.Servers
                                select s;

            // Execute the query and place the results into a collection.
            servers = new ObservableCollection<Server>(serversInDB);

            // Call the base method.
            base.OnNavigatedTo(e);
        }

        public NewMeetingsServersPage()
        {
            InitializeComponent();

            // Data context and observable collection are children of the main page.
            this.DataContext = this;
        }
        
        public void serverClicked(Object sender, RoutedEventArgs e)
        {
            NavigationService.Navigate(new Uri("/Pages/NewMeetingPage.xaml?serverName=" + ((Button)sender).Tag, UriKind.Relative));//
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