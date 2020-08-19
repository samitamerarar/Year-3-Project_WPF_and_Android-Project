
using System.Windows.Controls;
using Prototype_Heacy_client.Models;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_LogIn.xaml
    /// </summary>
    public partial class UserControl_LogIn : UserControl
    {
        public MainWindow main;
        public User_Model _user;
        public UserControl_LogIn(MainWindow main)
        {
            InitializeComponent();
            this.main = main;
            DataContext = new LogIn_ViewModel(this);
            this.Pseudonyme.Focus();
            this.Pseudonyme.MaxLength = 15;
            this.Password.MaxLength = 15;

        }

        public void SwitchTuto()
        {
            this.main.switchTuto();
        }

        public void SwitchToHomePage(User user)
        {
            this.main.SwitchToHomePage(user);
        }

       
    }
}
