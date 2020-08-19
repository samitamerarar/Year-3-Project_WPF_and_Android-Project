using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Views;
using Prototype_Heacy_client.Views.UserControls;
using System;
using System.ComponentModel;


namespace Prototype_Heacy_client.ViewModels
{
    public class SerchChannel_ViewModel : INotifyPropertyChanged
    {
        public SerchChanel_Window SerchWind;
        public UserControl_Conversations_Channel conversationView;
        private string channelName { get; set; }
        public SerchChannel_ViewModel(SerchChanel_Window serchWind)
        {
           
            this.SerchWind = serchWind;
            conversationView = this.SerchWind.conversationViewModel.convChannelVM;
            this.errorText = "";
            SerchChannel = new GeneralCommands<object>(serchChannel, CanSerch);
            SelectChannel = new GeneralCommands<object>(selectChannel, canSelect);
            Close = new GeneralCommands<object>(CloseDialog, CanClose);
            
        }

 
        public GeneralCommands<object> Close { get; set; }
        public GeneralCommands<object> SerchChannel { get; set; }

        public GeneralCommands<object> SelectChannel { get; set; }

       
        public void CloseDialog(object o)
        {
            this.SerchWind.MyStack.Children.Clear();
            this.SerchWind.Close();
        }

        public bool CanClose(object o)
        {
            return true;
        }
        public async void selectChannel(object o)
        {
            this.ErrorText = "";
            bool channelFound = false;
            await this.SerchWind.conversationViewModel.GetChannels();

            this.SerchWind.MyStack.Children.Clear();
            foreach (Tuple<string, UserControl_Channel> elem in this.conversationView.listChatToJoin)
            {
                channelFound = true;
                this.SerchWind.MyStack.Children.Add(elem.Item2);
               
            }
            if (!channelFound)
            {
                this.ErrorText = "There is no new channels to join";
            }
        }

        public bool canSelect(object o)
        {
            return true;
        }
        public async void serchChannel(object o)
        {
            await this.SerchWind.conversationViewModel.GetChannels();
            this.ErrorText = "";
            bool channelFound = false;
            this.SerchWind.MyStack.Children.Clear();
            foreach(Tuple<string,UserControl_Channel> elem in this.conversationView.listChatToJoin)
            {
                if(elem.Item1 == channelName)
                {
                    this.SerchWind.MyStack.Children.Add(elem.Item2);
                    channelFound = true;
                }
            }
            if (!channelFound)
            {
                this.ErrorText = "Channel not found";
                foreach(Tuple<string, UserControl_Chat> elem in this.conversationView.listChat)
                {
                    if(elem.Item1 == channelName)
                    {
                        this.ErrorText = "Channel already joined";
                    }
                }
                
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




        public bool CanSerch(object o)
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


        public string ChannelName
        {
            get
            {
                return channelName;
            }
            set
            {
                channelName = value;
                ErrorText = "";
                OnPropertyChanged("ChannelName");
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
