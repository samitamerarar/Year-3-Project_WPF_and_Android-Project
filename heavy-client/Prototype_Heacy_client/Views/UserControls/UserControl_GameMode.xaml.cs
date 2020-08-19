using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using System.Windows.Controls;


namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_GameMode.xaml
    /// </summary>
    public partial class UserControl_GameMode : UserControl
    {
        public HomePage_ViewModel _homePageViewModel;
        public UserControl_GameMode(HomePage_ViewModel homePageViewModel)
        {
            InitializeComponent();
            DataContext = new GameMode_ViewModel(homePageViewModel);
        }
    }
}
