
using MeetingDataExchange.Model;
using System.Collections.Generic;
namespace MeetingDataExchange.ServerCommunication
{
    public class ServerName
    {
        public string servername { get; set; }
    }

    public class StatusReasonOutput
    {
        public string status { get; set; }
        public string reason { get; set; }
    }

    public class RegistrationInput:LoginInput
    {
        public RegistrationInput(string login, string name, string email, string password):
            base(login,password)
        {
            this.name = name;
            this.email = email;
        }
        public string name { get; set; }
        public string email { get; set; }
    }

    public class RegistrationOutput:StatusReasonOutput
    {
    }

    public class LoginInput
    {
        public LoginInput(string login, string password)
        {
            this.login = login;
            this.password = password;
        }
        public string login { get; set; }
        public string password { get; set; }
    }

    public class LoginOutput:RegistrationOutput
    {
        public string sid { get; set; }
    }

    public class LogoutInput
    {
        public LogoutInput(string login, string sid)
        {
            this.login = login;
            this.sid = sid;
        }
        public string login { get; set; }
        public string sid { get; set; }
    }

    public class EditInput:RegistrationInput
    {
        public EditInput(string login,string sid, string name, string email, string password):
            base(login, name, email, password)
        {
            this.sid = sid;
        }
        public string sid { get; set; }
    }
        
    public class EditOutput : StatusReasonOutput
    {
    }

    public class PersonalDataOutput : StatusReasonOutput
    {
        public string name { get; set; }
        public string email { get; set; }
    }
    public class NewMeetingInput
    {
        public string login { get; set; }
        public string sid { get; set; }
        public string title { get; set; }
        public string topic { get; set; }
        public string abilityToSendFiles { get; set; }

    }

    public class MeetingOutput : StatusReasonOutput
    {
        public int meetingid { get; set; }
        public string title { get; set; }
        public string topic { get; set; }
        public string hostname { get; set; }
        public string starttime { get; set; }
        public string endtime { get; set; }
        public int members { get; set; }
        public string permissions { get; set; }
        public string accessCode { get; set; }

        public Meeting getEntity(Server server)
        {
            Meeting meeting = new Meeting();
            meeting.server = server;
            meeting.serverMeetingID = meetingid;
            meeting.title = title;
            meeting.topic = topic;
            meeting.adminName = hostname;
            meeting.startTime = starttime;
            meeting.endTime = endtime;
            meeting.numerOfMembers = members;
            meeting.permissions = permissions == "memberUpload" ? 1 : (permissions == "member" ? 0 : 2);
            meeting.code = accessCode;
            return meeting;
        }
    }

    public class JoinMeetingInput
    {
        public string login { get; set; }
        public string sid { get; set; }
        public int meetingid { get; set; }
        public string accessCode { get; set; }

    }

    public class MeetingsListOutput : StatusReasonOutput
    {
        public MeetingOutput[] meetings { get; set; }
    }

    public class CommentOutput
    {
        public int commentid { get; set; }
        public string author { get; set; }
        public string addtime { get; set; }
        public string content { get; set; }

        public Comment getEntity(File file)
        {
            Comment comment = new Comment();
            comment.file = file;
            comment.serverCommentID = commentid;
            comment.authorName = author;
            comment.content = content;
            return comment;
        }
    }

    public class FileOutput : StatusReasonOutput
    {
        public int fileid { get; set; }
        public string filename { get; set; }
        public string author { get; set; }
        public string addtime { get; set; }
        public string hash { get; set; }
        public string sizeKB { get; set; }
        public CommentOutput[] comments { get; set; }

        public File getEntity(Meeting meeting)
        {
            File file = new File();
            file.meeting = meeting;
            file.serverFileID = fileid;
            file.fileName = filename;
            file.authorName = author;
            file.hash = hash;
            List<Comment> list = new List<Comment>();
            foreach(CommentOutput comment in comments)
                list.Add(comment.getEntity(file) );
            file.comments.AddRange(list);
            return file;
        }
    }


    public class FilesListOutput : StatusReasonOutput
    {
        public FileOutput[] files { get; set; }
    }

}