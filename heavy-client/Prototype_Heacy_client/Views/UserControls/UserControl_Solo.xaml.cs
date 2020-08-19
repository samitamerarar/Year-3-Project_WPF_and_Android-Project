using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.ViewModels;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using System.Windows.Controls;

namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_Solo.xaml
    /// </summary>
    public partial class UserControl_Solo : UserControl
    {
        public UserControl_Solo(Group_Model group, HomePage_ViewModel homePageViewModel)
        {
            InitializeComponent();
            this.GroupName.Text = "Veuillez joindre le groupe: <" + group.createGroup.name + ">";
            DataContext = new Solo_ViewModel(this, homePageViewModel, group);
            //DataContext = new TemplateView_ViewModel<UserControl_Solo>(this, homePageViewModel, group);

        }
    }
}
