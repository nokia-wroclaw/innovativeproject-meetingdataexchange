﻿
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
        public string meetingid { get; set; }
        public string title { get; set; }
        public string topic { get; set; }
        public string hostname { get; set; }
        public string starttime { get; set; }
        public string endtime { get; set; }
        public string members { get; set; }
        public string permissions { get; set; }
        public string accessCode { get; set; }
    }
    public class JoinMeetingInput
    {
        public string login { get; set; }
        public string sid { get; set; }
        public string meetingid { get; set; }
        public string accessCode { get; set; }

    }

}