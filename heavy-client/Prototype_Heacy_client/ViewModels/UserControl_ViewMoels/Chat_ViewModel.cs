using Prototype_Heacy_client.Views.UserControls;
using Prototype_Heacy_client.Models;
using Newtonsoft.Json;
using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.Services;
using System.ComponentModel;

namespace Prototype_Heacy_client.ViewModels.UserControl_ViewMoels
{

  
    class Chat_ViewModel : INotifyPropertyChanged
    {
        public UserControl_Chat chatView;
        private Chat_Model chat;
        private User user;
        private ConversationsChannel_ViewModel conversationViewModel;


        public GeneralCommands<object> SendMessage
        {
            get;
            set;
        }
        public GeneralCommands<object> ReturnToConv
        {
            get;
            set;
        }

        public Chat_ViewModel(UserControl_Chat UCC, User user)
        {
            this.conversationViewModel = UCC.ConversationChannel.conversationViewModel;
            channelName = UCC.ChannelName;
            chat = new Chat_Model();
            chatView = UCC;
            this.user = user;
            SendMessage = new GeneralCommands<object>(SendMessageToServer, CanSend);
            ReturnToConv = new GeneralCommands<object>(Return, CanLogOut);
            History = new GeneralCommands<object>(GetHistory, CanGetHistory);
            InitChat();
          

        }
        public Chat_Model Chat
        {
            get
            {
                return chat;
            }
            set
            {
                chat = value;
            }
        }

        public string ChannelName
        {
            get
            {
               return channelName;
            }
            set
            {
                channelName = value;
                OnPropertyChanged("ChannelName");

            }
        }

        private string channelName = "";



        public bool CanGetHistory(object o)
        {
            if(channelName == "game")
            {
                return false;
            }
            return true;
        }
        public async void GetHistory(object o)
        {
            await this.conversationViewModel.GetChannels();
            this.chatView.stakk.Children.Clear();
            foreach(Channel channel in this.conversationViewModel.channels)
            {
                if(channel.name == channelName)
                {
                    foreach (Message message in channel.chat)
                    {
                        recivedOrSentMessage(message);
                    }
                }
            }

        }
        public void  SendMessageToServer(object o)
        {
            var content = JsonConvert.SerializeObject(new Message(user.user.username, chat.MessageToSend, channelName, ""));
            SocketService.MySocket.Emit("new message", content);
             

        }

        public bool CanSend(object o)
        {
            if (string.IsNullOrWhiteSpace(chat.MessageToSend))
            {
                return false;
            }
            else
            {
                return true;
            }
        }


        public bool CanLogOut(object o)
        {
           
            return true;
        }

        public void Return(object o)
        {
            chatView.SwitchToConv();
        }


        public GeneralCommands<object> History { get; set; }
        void ConnectSocket()
        {
            var content = JsonConvert.SerializeObject(new EnterObject(user.user.username, channelName));
            SocketService.MySocket.Emit("enter channel", content);
        }


        private void ListenMessages()
        {
            SocketService.MySocket.On("new message", (data) =>
            {
                Message message = JsonConvert.DeserializeObject<Message>(data.ToString());
                if(message.content == "La partie à commencer! Bonne chance à tous!")
                {
                    this.chatView.Dispatcher.Invoke(() =>
                    {
                        this.chatView.stakk.Children.Clear();
                    });
                   

                }
                recivedOrSentMessage(message);

            });

        }

        private void recivedOrSentMessage(Message message)
        {
            if (channelName == message.channel)
            {
                if (message.author == user.user.username)
                {
                    chatView.SentMessage(message.content, message.created);
                    chat.MessageToSend = "";
                }
                else
                {
                    chatView.RecivedMessage(message.content, message.author + " At : " + message.created);
                }
            }
        }

        void InitChat()
        {
            ConnectSocket();
            ListenMessages();

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
