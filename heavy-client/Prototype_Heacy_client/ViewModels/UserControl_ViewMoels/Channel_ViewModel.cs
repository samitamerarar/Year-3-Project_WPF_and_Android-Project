using System;
using System.Text;
using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Views.UserControls;
using System.ComponentModel;
using Newtonsoft.Json;
using Prototype_Heacy_client.Interfaces;
using System.Net.Http;
using Prototype_Heacy_client.Services;

namespace Prototype_Heacy_client.ViewModels.UserControl_ViewMoels
{
    class Channel_ViewModel : INotifyPropertyChanged
    {
        public UserControl_Channel channelView;
        private string nameButton;
        public string NameButton
        {
            get
            {
                return nameButton;
            }
            set
            {
                nameButton = value;
                OnPropertyChanged("NameButton");

            }
        }


        public GeneralCommands<object> JoinOrEnter
        {
            get;
            set;
        }
        public Channel_ViewModel(UserControl_Channel channelView, string buttonContent)
        {
            JoinOrEnter = new GeneralCommands<object>(joinOrEnter, canjoin);

            this.channelView = channelView;
            nameButton = buttonContent;
        }
        public async void joinOrEnter(object o)
        {
            if(NameButton == "Join")
            {
                var content = JsonConvert.SerializeObject(new JoinObject(this.channelView.user.user.username, this.channelView.channelName.Content.ToString()));
                var response = await Http.Client.PostAsync(Http.UrlServer + "channel/join", new StringContent(content, Encoding.UTF8, "application/json"));
                var responseString = await response.Content.ReadAsStringAsync();

                if (((int)response.StatusCode) == 200)
                {
                    AddJoinedChat();
                }
   
            }
            else
            {
                this.channelView.UserControlConversation.SwitchToChat(this.channelView.channelName.Content.ToString());
            }
           
           
        }
        public void AddJoinedChat()
        {
            Tuple<string, UserControl_Channel> toDelete = null;
            foreach (Tuple<string, UserControl_Channel> item in this.channelView.UserControlConversation.listChatToJoin)
            {
                if(item.Item1 == this.channelView.channelName.Content.ToString())
                {
                    toDelete = item;
                }

            }
            if(toDelete!=null)
            {
                this.channelView.UserControlConversation.listChatToJoin.Remove(toDelete);
            }
            this.channelView.UserControlConversation.listChat.Add(
                                              new Tuple<string, UserControl_Chat>(this.channelView.channelName.Content.ToString(),
                                              new UserControl_Chat(this.channelView.UserControlConversation.UserControlChatControl,
                                              this.channelView.UserControlConversation, this.channelView.channelName.Content.ToString(),
                                              this.channelView.user)));
            NameButton = "Enter";
            this.channelView.UserControlConversation.RefreshView();
        }


        public bool canjoin(object o)
        {
            return true;
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
