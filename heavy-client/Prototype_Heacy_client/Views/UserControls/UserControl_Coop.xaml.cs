using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
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
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_Coop.xaml
    /// </summary>
    public partial class UserControl_Coop : UserControl
    {
        public UserControl_Coop(Group_Model group, HomePage_ViewModel homePageViewModel)
        {
            InitializeComponent();
            this.GroupName.Text = "Joindre le groupe: <" + group.createGroup.name + ">" + " SVP";
            DataContext = new Coop_ViewModel(this, homePageViewModel, group);
            //DataContext = new TemplateView_ViewModel<UserControl_Coop>(this, homePageViewModel, group);
        }
    }
}
