﻿using System;
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
using MeetingDataExchange.ServerCommunication;

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

            serverNameBlock.Text = meeting.server.serverName;
            meetingNameBlock.Text = meeting.title;

            base.OnNavigatedTo(e);
        }

        public void addNoteButtonCliked(Object sender, RoutedEventArgs e)
        {
            setControlEnabled(false);
            string url = meeting.server.address + "/api/upload/" + meeting.server.login + "/" + meeting.server.sid + "/" + meeting.serverMeetingID + "/" + titleBox.Text + ".txt";
            System.Diagnostics.Debug.WriteLine(url);
            new HttpPutRequest<StatusReasonOutput>(url, addNoteCallback, noteBox.Text);

        }

        private void addNoteCallback(StatusReasonOutput output)
        {
            this.Dispatcher.BeginInvoke(delegate()
            {

                setControlEnabled(true);
                if (output == null)
                {
                    MessageBox.Show("Couldn't connect to server");
                }
                else if (output.status == "ok")
                {
                    MessageBox.Show("Note added.");
                    NavigationService.GoBack();
                }
                else
                {
                    MessageBox.Show(output.reason);
                }
            });

        }

        void setControlEnabled(bool isEnabled)
        {
            titleBox.IsEnabled = isEnabled;
            noteBox.IsEnabled = isEnabled;
            addNoteButton.IsEnabled = isEnabled;
            progressBar.Visibility = isEnabled ? Visibility.Collapsed : Visibility.Visible;
        }

        public AddNotePage()
        {
            InitializeComponent();
        }
    }
}