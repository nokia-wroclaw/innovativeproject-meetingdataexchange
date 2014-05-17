
namespace MeetingDataExchange.ServerCommunication
{
    public class ServerName
    {
        public string servername { get; set; }
    }

    public class RegistrationInput
    {
        public RegistrationInput(string login, string name, string email, string password)
        {
            this.login = login;
            this.name = name;
            this.email = email;
            this.password = password;
        }
        public string login { get; set; }
        public string name { get; set; }
        public string email { get; set; }
        public string password { get; set; }
    }

    public class RegistrationOutput
    {
        public string status { get; set; }
        public string reason { get; set; }
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

    public class LoginOutput
    {
        public string status { get; set; }
        public string sid { get; set; }
        public string reason { get; set; }
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

}