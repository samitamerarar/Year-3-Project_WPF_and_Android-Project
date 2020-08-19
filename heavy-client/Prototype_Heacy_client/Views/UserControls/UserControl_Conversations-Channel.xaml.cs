using System;
using System.Collections.Generic;
using System.Windows.Controls;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;

namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_Conversations_Channel.xaml
    /// </summary>
    public partial class UserControl_Conversations_Channel : UserControl
    {
        public UserControl_ChatControl UserControlChatControl;
        public ConversationsChannel_ViewModel conversationViewModel;
        public List<Tuple<string, UserControl_Chat>> listChat = new List<Tuple<string, UserControl_Chat>>();
        public List<string> channelDisplayed = new List<string>();
        public List<Tuple<string, UserControl_Channel>> listChatToJoin = new List<Tuple<string, UserControl_Channel>>();


        public UserControl_Conversations_Channel(UserControl_ChatControl UserControlChatControl)
        {
            InitializeComponent();
            this.conversationViewModel = new ConversationsChannel_ViewModel(this);
            DataContext = this.conversationViewModel;
            this.UserControlChatControl = UserControlChatControl;
        }

        public void addChannel(string name)
        {
            if(!this.channelDisplayed.Exists(elem => elem == name))
            {
                this.channelDisplayed.Add(name);

                if (this.UserControlChatControl.user.user.channels.Exists(element => element == name))
                {
                    stakChannels.Children.Add(new UserControl_Channel(this, name, this.UserControlChatControl.user, "Enter"));
                    this.listChat.Add(new Tuple<string, UserControl_Chat>(name,
                                                   new UserControl_Chat(this.UserControlChatControl,
                                                   this, name,
                                                   this.UserControlChatControl.user)));
                    
                    
                }
                else
                {
                   this.listChatToJoin.Add(new Tuple<string, UserControl_Channel>(name, new UserControl_Channel(this, name, this.UserControlChatControl.user, "Join")));
                }

            }
           
        }

        public void RefreshView()
        {
            stakChannels.Children.Clear();
            foreach (Tuple<string, UserControl_Chat> elem in this.listChat)
            {
                stakChannels.Children.Add(new UserControl_Channel(this, elem.Item1, this.UserControlChatControl.user, "Enter"));
            }
           
        }

        public void SwitchToChat(string channelName)
        {
      
            foreach(Tuple<string, UserControl_Chat> elem in listChat)
            {
                if(elem.Item1 == channelName)
                {
                    this.UserControlChatControl.DataContext = elem.Item2;
                }
            }
          
        }



    }
}
