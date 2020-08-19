using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using System.Windows.Controls;

namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_Tournament.xaml
    /// </summary>
    public partial class UserControl_Tournament : UserControl
    {
        public UserControl_Tournament(Group_Model group, HomePage_ViewModel homePageViewModel)
        {
            InitializeComponent();
            DataContext = new Tournament_ViewModel(this, homePageViewModel, group);
            this.GroupTitle.Text = "Veuillez joindre le tournoi : <" + group.createGroup.name + ">";
        }
    }
}
