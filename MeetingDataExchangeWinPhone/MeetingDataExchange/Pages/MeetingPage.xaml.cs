using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using Microsoft.Phone.Controls;
using MeetingDataExchange.Model;
using System.Collections.ObjectModel;
using System.ComponentModel;
using MeetingDataExchange.ServerCommunication;
using System.Windows.Threading;
using Microsoft.Phone.Tasks;

namespace MeetingDataExchange.Pages
{
    public partial class MeetingPage : PhoneApplicationPage, INotifyPropertyChanged
    {
        private MDEDataContext MDEDB;
        private Server server;
        private Meeting meeting;

        private readonly DispatcherTimer _timer;

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

        public PhotoChooserTask photoChooserTask;
        private ObservableCollection<File> _files;
        public ObservableCollection<File> files
        {
            get
            {
                return _files;
            }
            set
            {
                if (_files != value)
                {
                    _files = value;
                    NotifyPropertyChanged("files");
                }
            }
        }

        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            #region Actualising navigation stack
            bool remove = false;

            if (NavigationContext.QueryString.ContainsKey("removePrevious"))
            {
                remove = ((string)NavigationContext.QueryString["removePrevious"]).Equals(bool.TrueString);
                NavigationContext.QueryString.Remove("removePrevious");
            }

            if (remove)
            {
                NavigationService.RemoveBackEntry();
            }
            #endregion

            MDEDB = new MDEDataContext();
            int meetingID = int.Parse( NavigationContext.QueryString["meetingID"] );

            meeting = new ObservableCollection<Meeting>(from Meeting m in MDEDB.Meetings where m.ID == meetingID select m)[0];
            server = meeting.server;
            files = new ObservableCollection<File>(meeting.files);

            PivotRoot.Title = server.serverName;

            #region Details
            titleBlock.Text = meeting.title;
            topicBlock.Text = meeting.topic;
            organisatorBlock.Text = meeting.adminName;
            if (meeting.endTime == null)
            {
                timeBlock.Text = meeting.startTime;
                stateBlock.Text = "Trwa (liczba użytkowników: " + meeting.numerOfMembers + " )";
            }
            else
            {
                timeBlock.Text = meeting.startTime + " - " + meeting.endTime;
                stateBlock.Text = "Zakończone (l. użytkowników: " + meeting.numerOfMembers + " )";
            }
            //permision = 0 - member
            //1 - member upload
            //2 - admin
            if (meeting.permissions == 2)
            {
                permissionBlock.Text = "Admin";
                settingsButton.Visibility = System.Windows.Visibility.Visible;
            }
            else
            {
                settingsButton.Visibility = System.Windows.Visibility.Collapsed;
                if(meeting.permissions == 1)
                    permissionBlock.Text = "Member with upload permission";
                else
                    permissionBlock.Text = "Member without upload permission";
            }
            #endregion

            refresh();
            _timer.Start();

            base.OnNavigatedTo(e);
        }

        protected override void OnNavigatedFrom(System.Windows.Navigation.NavigationEventArgs e)
        {
            _timer.Stop();
            base.OnNavigatedFrom(e);
        }

        public MeetingPage()
        {
            InitializeComponent();

            _timer = new DispatcherTimer();
            _timer.Interval = TimeSpan.FromMilliseconds(10000);
            _timer.Tick += (o, arg) => refresh();

            photoChooserTask = new PhotoChooserTask();
            photoChooserTask.Completed += new EventHandler<PhotoResult>(photoChooserTask_Completed);

            // Data context and observable collection are children of the main page.
            this.DataContext = this;
        }

        private void setControlEnabled(bool isEnabled)
        {
            progressBar.Visibility = isEnabled ? Visibility.Collapsed : Visibility.Visible;
        }

        public void refresh()
        {
            setControlEnabled(false);
            string url = server.address + "/api/files/list/" + meeting.serverMeetingID + "/" + server.login + "/" + server.sid;// +"?cache=" + Guid.NewGuid().ToString();
            System.Diagnostics.Debug.WriteLine(url);
            new HttpGetRequest<FilesListOutput>(url, filesListCallback);
        }

        private void filesListCallback(FilesListOutput output)
        {
            this.Dispatcher.BeginInvoke(delegate()
            {
                if (output == null)
                {
                    //MessageBox.Show("Error communicating with server. Check your internet connection and try again.");
                    connectionFailureTextBlock.Visibility = System.Windows.Visibility.Visible;
                }
                else if (output.status == "ok")
                {
                    connectionFailureTextBlock.Visibility = System.Windows.Visibility.Collapsed;
                    foreach (FileOutput fileOutput in output.files)
                    {
                        var file = (from File f in MDEDB.Files where f.serverFileID == fileOutput.fileid select f);
                        if (file.Count() == 0)
                        {
                            MDEDB.Files.InsertOnSubmit(fileOutput.getEntity(meeting));
                        }
                        else
                        {
                            File f1 = file.ToList()[0];
                            File f2 = fileOutput.getEntity(meeting);
                            f1.addTime = f2.addTime;
                            f1.authorName = f2.authorName;
                            f1.comments = f2.comments;
                            f1.fileName = f2.fileName;
                        }
                    }
                    MDEDB.SubmitChanges();
                    files = new ObservableCollection<File>(from File f in MDEDB.Files
                                                                 where f.meeting == meeting
                                                                 select f);
                }
                else
                {
                    MessageBox.Show("Unable to refresh.\nServer response:\n" + output.reason);
                }
                setControlEnabled(true);
            });
        }


        public void settingsButtonClicked(Object sender, RoutedEventArgs e)
        {
            NavigationService.Navigate(new Uri("/Pages/EditMeetingSettingsPage.xaml?meetingID=" + meeting.ID, UriKind.Relative));
        }
        public void fileClicked(Object sender, RoutedEventArgs e)
        {
            NavigationService.Navigate(new Uri("/Pages/FilePage.xaml?fileID=" + ((Button)sender).Tag, UriKind.Relative));
        }
        public void fileHolded(Object sender, RoutedEventArgs e)
        {

        }
        public void addPhotoButtonClicked(Object sender, RoutedEventArgs e)
        {
            NavigationService.Navigate(new Uri("/Pages/AddPhotoPage.xaml?meetingID=" + meeting.ID, UriKind.Relative));
        }
        public void addFileButtonClicked(Object sender, RoutedEventArgs e)
        {
            System.Diagnostics.Debug.WriteLine("Add File Button Clicked");
            photoChooserTask.Show();
        }
        public void addNoteButtonClicked(Object sender, RoutedEventArgs e)
        {
            NavigationService.Navigate(new Uri("/Pages/AddNotePage.xaml?meetingID=" + meeting.ID, UriKind.Relative));
        }
        public void addUserButtonClicked(Object sender, RoutedEventArgs e)
        {
            NavigationService.Navigate(new Uri("/Pages/QRCodePage.xaml?meetingID=" + meeting.ID, UriKind.Relative));
        }


        void photoChooserTask_Completed(object sender, PhotoResult e)
        {
            if (e.TaskResult == TaskResult.OK)
            {
                //e.ChosenPhoto.Seek(0, SeekOrigin.Begin);

                byte[] image = new byte[e.ChosenPhoto.Length];
                e.ChosenPhoto.Read(image, 0, image.Length);

                string url = meeting.server.address + "/api/upload/" + meeting.server.login + "/" + meeting.server.sid + "/" + meeting.serverMeetingID + "/" + DateTime.Now.ToString("yymmddhhmmss") + ".jpg";
                System.Diagnostics.Debug.WriteLine(url);
                new HttpPutRequest<StatusReasonOutput>(url, addPhotoCallback, image, (int)e.ChosenPhoto.Length);
            }
        }

        private void addPhotoCallback(StatusReasonOutput output)
        {
            this.Dispatcher.BeginInvoke(delegate()
            {
                if (output == null)
                {
                    MessageBox.Show("Couldn't connect to server");
                }
                else if (output.status == "ok")
                {
                    MessageBox.Show("Photo added.");
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