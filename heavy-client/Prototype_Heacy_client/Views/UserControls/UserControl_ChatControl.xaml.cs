using Prototype_Heacy_client.Interfaces;
using System.Windows.Controls;


namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_ChatControl.xaml
    /// </summary>
    public partial class UserControl_ChatControl : UserControl
    {
        public User user;
        public UserControl_ChatControl(User user)
        {
            InitializeComponent();
            this.user = user;
            DataContext = new UserControl_Conversations_Channel(this);
        }
    }
}
