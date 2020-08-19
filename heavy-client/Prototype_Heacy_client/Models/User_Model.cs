using System.ComponentModel;

namespace Prototype_Heacy_client.Models
{
    
    public class User_Model : INotifyPropertyChanged
    {
        private string username;
        private string password;
        private string error = "";

        public string Error { get { return error; } set { error = value; OnPropertyChanged("Error"); } }

        public string Username
        {
            get
            {
                return username;
            }
            set
            {
                username = value;
                OnPropertyChanged("Username");
            }

        }
        public string Password
        {
            get
            {
                return password;
            }
            set
            {
                password = value;
                OnPropertyChanged("Password");
            }

        }

        public User_Model(string username, string password)
        {
            this.username = username;
            this.password = password;
        }

        public event PropertyChangedEventHandler PropertyChanged;

        private void OnPropertyChanged(string propertyName)
        {
            PropertyChangedEventHandler handler = PropertyChanged;

            if (handler != null)
            {
                handler(this, new PropertyChangedEventArgs(propertyName));
            }
        }
    }
}
