using System.ComponentModel;


namespace Prototype_Heacy_client.Models
{
    class Chat_Model : INotifyPropertyChanged
    {
        private string messageToSend;
        public string MessageToSend
        {
            get
            {
                return messageToSend;
            }
            set
            {
                messageToSend = value;
                OnPropertyChanged("MessageToSend");

            }
        }

        public Chat_Model()
        {
            messageToSend = "";
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
