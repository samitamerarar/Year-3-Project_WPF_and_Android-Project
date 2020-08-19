using Newtonsoft.Json;
using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using Prototype_Heacy_client.Views;
using System.ComponentModel;
using System.Net.Http;
using System.Text;
using Prototype_Heacy_client.Services;


namespace Prototype_Heacy_client.ViewModels
{
    public class ChannelCreation_ViewModel : INotifyPropertyChanged
    {


        public ConversationsChannel_ViewModel ConvVM;
        public ChannelCreationWindow main;

        public ChannelCreation_ViewModel(ChannelCreationWindow main, ConversationsChannel_ViewModel convm)
        {
            this.ConvVM = convm;
            this.main = main;
            channelName = "";
            errorText = "";
            created = "";
            Close = new GeneralCommands<object>(CloseBox, CanClose);
            CreateChannel = new GeneralCommands<object>(CreateTheChannel, CanCreate);
        }



        public GeneralCommands<object> Close { get; set; }

        public void CloseBox(object o)
        {
            this.main.Close();
        }

        public bool CanClose(object o)
        {
            return true;
        }

        public GeneralCommands<object> CreateChannel { get; set; }


        
        public bool CanCreate(object o)
        {
            if (string.IsNullOrWhiteSpace(channelName))
            {
                return false;
            }
            else
            {
                return true;
            }
        }

        public async void CreateTheChannel(object o)
        {
            ErrorText = "";
            Created = "";
            var content = JsonConvert.SerializeObject(new CreationChannel(channelName));
            var response = await Http.Client.PostAsync(Http.UrlServer + "channel", new StringContent(content, Encoding.UTF8, "application/json"));
            var responseString = await response.Content.ReadAsStringAsync();
            CreationFeedBack msg = JsonConvert.DeserializeObject<CreationFeedBack>(responseString);
            if (((int)response.StatusCode) == 200)
            {
                Created = msg.msg;
                this.ConvVM.GetChannels();
               
            }
            else
            {
                ErrorText = msg.msg;
            }

            
        }

        private string channelName { get; set; }
        
        public string ChannelName
        {
            get
            {
                return channelName;
            }
            set
            {
                channelName = value;
                Created = "";
                ErrorText = "";
                OnPropertyChanged("ChannelName");
            }
        }
        private string created { get; set; }

        public string Created
        {
            get
            {
                return created;
            }
            set
            {
                created = value;
                OnPropertyChanged("Created");

            }
        }
        private string errorText { get; set; }

        public string ErrorText
        {
            get
            {
                return errorText;
            }
            set
            {
                errorText = value;
                OnPropertyChanged("ErrorText");
            }
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
