using Prototype_Heacy_client.Models;
using Prototype_Heacy_client.ViewModels;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using Prototype_Heacy_client.Views.UserControls;
using System.Windows;
using System.Windows.Input;


namespace Prototype_Heacy_client.Views
{
    /// <summary>
    /// Interaction logic for GameDifficultiesWindow.xaml
    /// </summary>
    public partial class GameDifficultiesWindow : Window
    {
        public GameDifficultiesWindow(HomePage_ViewModel homePageViewModel)
        {
            InitializeComponent();
            DataContext = new GameDifficulties_ViewModel(this, homePageViewModel);
        }

        public GameDifficultiesWindow()
        {
            //InitializeComponent();
        }
        private void TitleBar_MouseDown(object sender, MouseButtonEventArgs e)
        {
            try
            {
                DragMove();
            }
            catch
            {

            }
        }
    }
}
