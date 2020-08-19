using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;


namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_Channel.xaml
    /// </summary>
    public partial class UserControl_Channel : UserControl
    {
        public UserControl_Conversations_Channel UserControlConversation;
        public User user;
        public UserControl_Channel(UserControl_Conversations_Channel convVM, string name, User user, string buttonContent)
        {
            InitializeComponent();
            this.user = user;
            this.UserControlConversation = convVM;
            DataContext = new Channel_ViewModel(this, buttonContent);
            channelName.Content = name;
        }
    }
}
