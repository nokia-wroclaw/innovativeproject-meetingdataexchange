using System;
using System.ComponentModel;
using System.Data.Linq;
using System.Data.Linq.Mapping;

namespace MeetingDataExchange.Model
{
    public class MDEDataContext : DataContext 
    {
        public MDEDataContext(string connectionString)
            : base(connectionString)
        { }

        public MDEDataContext()
            : base("isostore:/MDEdb.sdf")
        { }

        public Table<Server> Servers;

        public Table<Meeting> Meetings;

        public Table<File> Files;

        public Table<Comment> Comments;
    }

    [Table]
    public class Server : INotifyPropertyChanged, INotifyPropertyChanging
    {
        public Server()
        {
            _meetings = new EntitySet<Meeting>(
                new Action<Meeting>(this.attach_meeting),
                new Action<Meeting>(this.detach_meeting)
                );
        }

        // Called during an add operation
        private void attach_meeting(Meeting meeting)
        {
            NotifyPropertyChanging("ToDoItem");
            meeting.server = this;
        }

        // Called during a remove operation
        private void detach_meeting(Meeting meeting)
        {
            NotifyPropertyChanging("ToDoItem");
            meeting.server = null;
        }

        #region ID
        private int _ID;

        [Column(IsPrimaryKey = true, IsDbGenerated = true, DbType = "INT NOT NULL Identity", CanBeNull = false, AutoSync = AutoSync.OnInsert)]
        public int ID
        {
            get { return _ID; }
            set
            {
                if (_ID != value)
                {
                    NotifyPropertyChanging("Id");
                    _ID = value;
                    NotifyPropertyChanged("Id");
                }
            }
        }
#endregion
        
        #region address
        private string _address;

        [Column]
        public string address
        {
            get { return _address; }
            set
            {
                if (_address != value)
                {
                    NotifyPropertyChanging("Address");
                    _address = value;
                    NotifyPropertyChanged("Address");
                }
            }
        }
#endregion
        
        #region serverName
        private string _serverName;

        [Column]
        public string serverName
        {
            get { return _serverName; }
            set
            {
                if (_serverName != value)
                {
                    NotifyPropertyChanging("ServerName");
                    _serverName = value;
                    NotifyPropertyChanged("ServerName");
                }
            }
        }
#endregion
        
        #region login
        private string _login;

        [Column]
        public string login
        {
            get { return _login; }
            set
            {
                if (_login != value)
                {
                    NotifyPropertyChanging("Login");
                    _login = value;
                    NotifyPropertyChanged("Login");
                }
            }
        }
#endregion
        
        #region name
        private string _name;

        [Column]
        public string name
        {
            get { return _name; }
            set
            {
                if (_name != value)
                {
                    NotifyPropertyChanging("Name");
                    _name = value;
                    NotifyPropertyChanged("Name");
                }
            }
        }
#endregion
        
        #region email
        private string _email;

        [Column]
        public string email
        {
            get { return _email; }
            set
            {
                if (_email != value)
                {
                    NotifyPropertyChanging("email");
                    _email = value;
                    NotifyPropertyChanged("email");
                }
            }
        }
        #endregion
        
        #region pass
        private string _pass;

        [Column]
        public string pass
        {
            get { return _pass; }
            set
            {
                if (_pass != value)
                {
                    NotifyPropertyChanging("PassMD5");
                    _pass = value;
                    NotifyPropertyChanged("PassMD5");
                }
            }
        }
        #endregion
        
        #region sid
        private string _sid;

        [Column]
        public string sid
        {
            get { return _sid; }
            set
            {
                if ( _sid != value)
                {
                    NotifyPropertyChanging("Sid");
                    _sid = value;
                    NotifyPropertyChanged("Sid");
                }
            }
        }
        #endregion

        #region meetings
        private EntitySet<Meeting> _meetings;

        [Association(Storage = "_meetings", OtherKey = "_serverID", ThisKey = "ID")]
        public EntitySet<Meeting> meetings
        {
            get { return this._meetings; }
            set {this._meetings.Assign(value); }
        }
        #endregion

        #region INotifyPropertyChanged Members

        public event PropertyChangedEventHandler PropertyChanged;

        // Used to notify that a property changed
        private void NotifyPropertyChanged(string propertyName)
        {
            if (PropertyChanged != null)
            {
                PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
            }
        }

        #endregion
        
        #region INotifyPropertyChanging Members

        public event PropertyChangingEventHandler PropertyChanging;

        // Used to notify that a property is about to change
        private void NotifyPropertyChanging(string propertyName)
        {
            if (PropertyChanging != null)
            {
                PropertyChanging(this, new PropertyChangingEventArgs(propertyName));
            }
        }

        #endregion
    }

    [Table]
    public class Meeting
    {

        #region ID
        private int _ID;

        [Column(IsPrimaryKey = true, IsDbGenerated = true, DbType = "INT NOT NULL Identity", CanBeNull = false, AutoSync = AutoSync.OnInsert)]
        public int ID
        {
            get { return _ID; }
            set
            {
                if (_ID != value)
                {
                    NotifyPropertyChanging("Id");
                    _ID = value;
                    NotifyPropertyChanged("Id");
                }
            }
        }
        #endregion

        #region server

        [Column]
        internal int _serverID;

        private EntityRef<Server> _server;

        [Association(Storage = "_server", ThisKey = "_serverID", OtherKey = "ID", IsForeignKey = true)]
        public Server server
        {
            get { return _server.Entity; }
            set
            {
                NotifyPropertyChanging("Server");
                _server.Entity = value;

                if (value != null)
                {
                    _serverID = value.ID;
                }

                NotifyPropertyChanging("Server");
            }
        }
        #endregion

        #region serverMeetingID
        private int _serverMeetingID;

        [Column]
        public int serverMeetingID
        {
            get { return _serverMeetingID; }
            set
            {
                if (_serverMeetingID != value)
                {
                    NotifyPropertyChanging("serverMeetingID");
                    _serverMeetingID = value;
                    NotifyPropertyChanged("serverMeetingID");
                }
            }
        }
        #endregion

        #region title
        private string _title;

        [Column]
        public string title
        {
            get { return _title; }
            set
            {
                if (_title != value)
                {
                    NotifyPropertyChanging("title");
                    _title = value;
                    NotifyPropertyChanged("title");
                }
            }
        }
        #endregion

        #region topic
        private string _topic;

        [Column]
        public string topic
        {
            get { return _topic; }
            set
            {
                if (_topic != value)
                {
                    NotifyPropertyChanging("topic");
                    _topic = value;
                    NotifyPropertyChanged("topic");
                }
            }
        }
        #endregion

        #region adminName
        private string _adminName;

        [Column]
        public string adminName
        {
            get { return _adminName; }
            set
            {
                if (_adminName != value)
                {
                    NotifyPropertyChanging("adminName");
                    _adminName = value;
                    NotifyPropertyChanged("adminName");
                }
            }
        }
        #endregion

        #region startTime
        private DateTime _startTime;

        [Column]
        public DateTime startTime
        {
            get { return _startTime; }
            set
            {
                if (_startTime != value)
                {
                    NotifyPropertyChanging("startTime");
                    _startTime = value;
                    NotifyPropertyChanged("startTime");
                }
            }
        }
        #endregion

        #region endTime
        private DateTime _endTime;

        [Column]
        public DateTime endTime
        {
            get { return _endTime; }
            set
            {
                if (_endTime != value)
                {
                    NotifyPropertyChanging("endTime");
                    _endTime = value;
                    NotifyPropertyChanged("endTime");
                }
            }
        }
        #endregion

        #region code
        private int _code;

        [Column]
        public int code
        {
            get { return _code; }
            set
            {
                if (_code != value)
                {
                    NotifyPropertyChanging("code");
                    _code = value;
                    NotifyPropertyChanged("code");
                }
            }
        }
        #endregion

        #region numerOfMembers
        private int _numerOfMembers;

        [Column]
        public int numerOfMembers
        {
            get { return _numerOfMembers; }
            set
            {
                if (_numerOfMembers != value)
                {
                    NotifyPropertyChanging("numerOfMembers");
                    _numerOfMembers = value;
                    NotifyPropertyChanged("numerOfMembers");
                }
            }
        }
        #endregion

        #region permissions
        private int _permissions;

        [Column]
        public int permissions
        {
            get { return _permissions; }
            set
            {
                if (_permissions != value)
                {
                    NotifyPropertyChanging("permissions");
                    _permissions = value;
                    NotifyPropertyChanged("permissions");
                }
            }
        }
        #endregion

        #region INotifyPropertyChanged Members

        public event PropertyChangedEventHandler PropertyChanged;

        // Used to notify that a property changed
        private void NotifyPropertyChanged(string propertyName)
        {
            if (PropertyChanged != null)
            {
                PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
            }
        }

        #endregion

        #region INotifyPropertyChanging Members

        public event PropertyChangingEventHandler PropertyChanging;

        // Used to notify that a property is about to change
        private void NotifyPropertyChanging(string propertyName)
        {
            if (PropertyChanging != null)
            {
                PropertyChanging(this, new PropertyChangingEventArgs(propertyName));
            }
        }

        #endregion
     }

    [Table]
    public class File
    {
        #region ID
        private int _ID;

        [Column(IsPrimaryKey = true, IsDbGenerated = true, DbType = "INT NOT NULL Identity", CanBeNull = false, AutoSync = AutoSync.OnInsert)]
        public int ID
        {
            get { return _ID; }
            set
            {
                if (_ID != value)
                {
                    NotifyPropertyChanging("Id");
                    _ID = value;
                    NotifyPropertyChanged("Id");
                }
            }
        }
        #endregion
        
        #region INotifyPropertyChanged Members

        public event PropertyChangedEventHandler PropertyChanged;

        // Used to notify that a property changed
        private void NotifyPropertyChanged(string propertyName)
        {
            if (PropertyChanged != null)
            {
                PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
            }
        }

        #endregion

        #region INotifyPropertyChanging Members

        public event PropertyChangingEventHandler PropertyChanging;

        // Used to notify that a property is about to change
        private void NotifyPropertyChanging(string propertyName)
        {
            if (PropertyChanging != null)
            {
                PropertyChanging(this, new PropertyChangingEventArgs(propertyName));
            }
        }

        #endregion
    }

    [Table]
    public class Comment
    {
        #region ID
        private int _ID;

        [Column(IsPrimaryKey = true, IsDbGenerated = true, DbType = "INT NOT NULL Identity", CanBeNull = false, AutoSync = AutoSync.OnInsert)]
        public int ID
        {
            get { return _ID; }
            set
            {
                if (_ID != value)
                {
                    NotifyPropertyChanging("Id");
                    _ID = value;
                    NotifyPropertyChanged("Id");
                }
            }
        }
        #endregion
        
        #region INotifyPropertyChanged Members

        public event PropertyChangedEventHandler PropertyChanged;

        // Used to notify that a property changed
        private void NotifyPropertyChanged(string propertyName)
        {
            if (PropertyChanged != null)
            {
                PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
            }
        }

        #endregion

        #region INotifyPropertyChanging Members

        public event PropertyChangingEventHandler PropertyChanging;

        // Used to notify that a property is about to change
        private void NotifyPropertyChanging(string propertyName)
        {
            if (PropertyChanging != null)
            {
                PropertyChanging(this, new PropertyChangingEventArgs(propertyName));
            }
        }

        #endregion
    }
}
