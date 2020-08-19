using System.Windows.Controls;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using Prototype_Heacy_client.Interfaces;
using System.Windows;
using System;

namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_Chat.xaml
    /// </summary>
    public partial class UserControl_Chat : UserControl
    {
        public UserControl_ChatControl UserControlChatControl;
        public UserControl_Conversations_Channel ConversationChannel;
        public string ChannelName;
        Interactiv_Tutoriel tuto;
        bool tutorial;
        public UserControl_Chat(UserControl_ChatControl UserControlChatControl, UserControl_Conversations_Channel ConversationChannel, string channelName, User user)
        {
            this.tutorial = false;
            this.ConversationChannel = ConversationChannel;
            InitializeComponent();
            this.ChannelName = channelName;
            DataContext = new Chat_ViewModel(this, user);
            this.UserControlChatControl = UserControlChatControl;
        }

        public UserControl_Chat(Interactiv_Tutoriel tuto)
        {
            
            InitializeComponent();
            this.tutorial = true;
            this.tuto = tuto;
            this.ChannelName = "Game";
            nameChannel.Text = "Game";
        }
         async void  presentationChat()
        {
            tutorial = true;
            this.tuto.chat.IsEnabled = false;
            this.tuto.tuto.IsEnabled = false;
            Tutorial_popupWindow a = new Tutorial_popupWindow("Bienvenu(e) dans notre tutoriel interactif, vous pouvez à tout moment terminer ce tutoriel en cliquant sur le bouton \"Terminer tutoriel\" en bas de la page..", "Ok");
            a.ShowDialog();
            this.tuto.chat.IsEnabled = true;
            this.msgSent.Text = "Salut";
            this.msgSent.IsReadOnly = true;
            a = new Tutorial_popupWindow("Pour débuter, on vous présentera le chat, on va commencer par envoyer un message. Le mot \"Salut\" est saisi pour vous,\n " +
                "veuillez cliquer sur le bouton rond à droite du message pour l'envoyer.", "Ok");
            a.ShowDialog();
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            if (tutorial)
            {
                this.SentMessage("Salut", "now");
                this.msgSent.Text = "";
                Tutorial_popupWindow a = new Tutorial_popupWindow("Comme vous pouvez le voir, le message a été envoyé, maintenant vous allez recevoir un message du système", "Ok");
                a.ShowDialog();
                this.RecivedMessage("Salut cv bien !!", "System: now");
                a = new Tutorial_popupWindow("Maintenant, on passera à une autre étape du tutoriel. Dans cette étape, on jouera à un jeu classique.", "ok");
                a.ShowDialog();
                this.tuto.chat.IsEnabled = false;
                this.tuto.Write.drawView.step1(this.tuto);

            }
        }

        public void SwitchToConv()
        {
            UserControlChatControl.DataContext = this.ConversationChannel;
        }
        public void RecivedMessage(string message, string date)
        {
            this.Dispatcher.Invoke(() =>
            {
                UserControl_MessageRecieved messageDisplay = new UserControl_MessageRecieved(message, date);
                stakk.Children.Add(messageDisplay);
                ScrollView.ScrollToEnd();
            });

        }

      

        public void SentMessage(string message, string date)
        {
            this.Dispatcher.Invoke(() =>
            {

                UserControl_MessageSent messageDisplay = new UserControl_MessageSent(message, date);
                stakk.Children.Add(messageDisplay);
                ScrollView.ScrollToEnd();
            });


        }

        private void UserControl_Loaded(object sender, RoutedEventArgs e)
        {
            if (this.tutorial)
            {
                this.Dispatcher.BeginInvoke((Action)(() =>
                {
                    this.presentationChat();

                }));

            }
           
        }

      
    }
}
