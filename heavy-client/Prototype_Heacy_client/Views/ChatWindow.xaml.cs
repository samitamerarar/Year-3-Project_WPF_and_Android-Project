using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using Prototype_Heacy_client.Views.UserControls;
using System.Windows;


namespace Prototype_Heacy_client.Views
{
    /// <summary>
    /// Interaction logic for ChatWindow.xaml
    /// </summary>
    public partial class ChatWindow : Window
    {
 

        public UserControl_ChatControl _myChat;
        public HomePage_ViewModel _home;

        public ChatWindow(HomePage_ViewModel home, UserControl_ChatControl chat)
        {
            InitializeComponent();
            this._home = home;
            this._myChat = chat;
            this._home._homePageView.MyChat.Children.Remove(chat);         
            this.MyChatWindow.Children.Add(chat);
            this.Show();
           
        }

        private void Window_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            this.MyChatWindow.Children.Remove(this._myChat);
            this._home._homePageView.MyChat.Children.Add(this._myChat);
        }
    }
}
