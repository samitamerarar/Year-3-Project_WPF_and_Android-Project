using Prototype_Heacy_client.ViewModels;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using System.Windows;


namespace Prototype_Heacy_client.Views
{
    /// <summary>
    /// Interaction logic for SerchChanel_Window.xaml
    /// </summary>
    public partial class SerchChanel_Window : Window
    {
        public ConversationsChannel_ViewModel conversationViewModel;
        public SerchChanel_Window(ConversationsChannel_ViewModel ConversationViewModel)
        {
            InitializeComponent();
            this.conversationViewModel = ConversationViewModel;
            DataContext = new SerchChannel_ViewModel(this);
        }

        private void Window_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            this.MyStack.Children.Clear();
        }
    }
}
