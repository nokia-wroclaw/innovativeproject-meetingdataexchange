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
        public Meeting()
        {
            _files = new EntitySet<File>(
                new Action<File>(this.attach_file),
                new Action<File>(this.detach_file)
                );
        }

        // Called during an add operation
        private void attach_file(File file)
        {
            NotifyPropertyChanging("file");
            file.meeting = this;
        }

        // Called during a remove operation
        private void detach_file(File file)
        {
            NotifyPropertyChanging("file");
            file.meeting = null;
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

        #region files
        private EntitySet<File> _files;

        [Association(Storage = "files", OtherKey = "_meetingID", ThisKey = "ID")]
        public EntitySet<File> files
        {
            get { return this._files; }
            set { this._files.Assign(value); }
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
        public File()
        {
            _comments = new EntitySet<Comment>(
                new Action<Comment>(this.attach_comment),
                new Action<Comment>(this.detach_comment)
                );
        }

        // Called during an add operation
        private void attach_comment(Comment comment)
        {
            NotifyPropertyChanging("comment");
            comment.file = this;
        }

        // Called during a remove operation
        private void detach_comment(Comment comment)
        {
            NotifyPropertyChanging("comment");
            comment.file = null;
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

        #region meeting

        [Column]
        internal int _meetingID;

        private EntityRef<Meeting> _meeting;

        [Association(Storage = "_meeting", ThisKey = "_meetingID", OtherKey = "ID", IsForeignKey = true)]
        public Meeting meeting
        {
            get { return _meeting.Entity; }
            set
            {
                NotifyPropertyChanging("meeting");
                _meeting.Entity = value;

                if (value != null)
                {
                    _meetingID = value.ID;
                }

                NotifyPropertyChanging("meeting");
            }
        }
        #endregion

        #region serverFileID
        private int _serverFileID;

        [Column]
        public int serverFileID
        {
            get { return _serverFileID; }
            set
            {
                if (_serverFileID != value)
                {
                    NotifyPropertyChanging("serverFileID");
                    _serverFileID = value;
                    NotifyPropertyChanged("serverFileID");
                }
            }
        }
        #endregion

        #region fileName
        private string _fileName;

        [Column]
        public string fileName
        {
            get { return _fileName; }
            set
            {
                if (_fileName != value)
                {
                    NotifyPropertyChanging("fileName");
                    _fileName = value;
                    NotifyPropertyChanged("fileName");
                }
            }
        }
        #endregion

        #region authorName
        private string _authorName;

        [Column]
        public string authorName
        {
            get { return _authorName; }
            set
            {
                if (_authorName != value)
                {
                    NotifyPropertyChanging("authorName");
                    _authorName = value;
                    NotifyPropertyChanged("authorName");
                }
            }
        }
        #endregion

        #region addTime
        private DateTime _addTime;

        [Column]
        public DateTime addTime
        {
            get { return _addTime; }
            set
            {
                if (_addTime != value)
                {
                    NotifyPropertyChanging("addTime");
                    _addTime = value;
                    NotifyPropertyChanged("addTime");
                }
            }
        }
        #endregion

        #region hash
        private string _hash;

        [Column]
        public string hash
        {
            get { return _hash; }
            set
            {
                if (_hash != value)
                {
                    NotifyPropertyChanging("hash");
                    _hash = value;
                    NotifyPropertyChanged("hash");
                }
            }
        }
        #endregion

        #region comments
        private EntitySet<Comment> _comments;

        [Association(Storage = "comments", OtherKey = "_fileID", ThisKey = "ID")]
        public EntitySet<Comment> comments
        {
            get { return this._comments; }
            set { this._comments.Assign(value); }
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

        #region file

        [Column]
        internal int _fileID;

        private EntityRef<File> _file;

        [Association(Storage = "_file", ThisKey = "_fileID", OtherKey = "ID", IsForeignKey = true)]
        public File file
        {
            get { return _file.Entity; }
            set
            {
                NotifyPropertyChanging("file");
                _file.Entity = value;

                if (value != null)
                {
                    _fileID = value.ID;
                }

                NotifyPropertyChanging("file");
            }
        }
        #endregion

        #region serverCommentID
        private int _serverCommentID;

        [Column]
        public int serverCommentID
        {
            get { return _serverCommentID; }
            set
            {
                if (_serverCommentID != value)
                {
                    NotifyPropertyChanging("serverCommentID");
                    _serverCommentID = value;
                    NotifyPropertyChanged("serverCommentID");
                }
            }
        }
        #endregion

        #region authorName
        private string _authorName;

        [Column]
        public string authorName
        {
            get { return _authorName; }
            set
            {
                if (_authorName != value)
                {
                    NotifyPropertyChanging("authorName");
                    _authorName = value;
                    NotifyPropertyChanged("authorName");
                }
            }
        }
        #endregion

        #region addTime
        private DateTime _addTime;

        [Column]
        public DateTime addTime
        {
            get { return _addTime; }
            set
            {
                if (_addTime != value)
                {
                    NotifyPropertyChanging("addTime");
                    _addTime = value;
                    NotifyPropertyChanged("addTime");
                }
            }
        }
        #endregion

        #region content
        private string _content;

        [Column]
        public string content
        {
            get { return _content; }
            set
            {
                if (_content != value)
                {
                    NotifyPropertyChanging("content");
                    _content = value;
                    NotifyPropertyChanged("content");
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
