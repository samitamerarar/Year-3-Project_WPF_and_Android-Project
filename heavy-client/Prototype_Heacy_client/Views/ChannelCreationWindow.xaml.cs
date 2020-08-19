using Prototype_Heacy_client.ViewModels;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using System.Windows;


namespace Prototype_Heacy_client.Views
{
    /// <summary>
    /// Interaction logic for ChannelCreationWindow.xaml
    /// </summary>
    public partial class ChannelCreationWindow : Window
    {
        
        public ChannelCreationWindow(ConversationsChannel_ViewModel conVM )
        {
            InitializeComponent();
            DataContext = new ChannelCreation_ViewModel(this, conVM);
        }
    }
}
