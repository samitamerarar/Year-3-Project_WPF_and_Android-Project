using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.Models;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using System;
using System.Windows.Controls;

namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_Group.xaml
    /// </summary>
    public partial class UserControl_Group : UserControl
    {
        public UserControl_Group(HomePage_ViewModel homePageViewModel, Group_Model group)
        {
            InitializeComponent();
            this.GroupName.Content = "Le nom du groupe: " + group.createGroup.name;
            DataContext = new Group_ViewModel(homePageViewModel, group);
        }

        public UserControl_Group()
        {
            InitializeComponent();

        }
    }
}
