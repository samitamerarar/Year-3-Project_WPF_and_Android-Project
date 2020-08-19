using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using System.Windows.Controls;


namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_GroupCompostion.xaml
    /// </summary>
    public partial class UserControl_Classic : UserControl
    {
        public UserControl_Classic(Group_Model group, HomePage_ViewModel homePageViewModel)
        {
            InitializeComponent();
            this.GroupName.Text = "Veuillez choisir une place dans le groupe: <" + group.createGroup.name + ">";
            DataContext = new Classic_ViewModel(this, homePageViewModel, group);
        }
    }
}
