using System.Windows;
using Prototype_Heacy_client.Views.UserControls;
using Prototype_Heacy_client.Models;
using Prototype_Heacy_client.Interfaces;
using System.Windows.Ink;
using System.Windows.Input;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;

namespace Prototype_Heacy_client.Views
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();

            //DataContext = new UserControl_WaitingView();
            DataContext = new UserControl_LogIn(this);
        }

        public void switchTuto()
        {
            DataContext = new Interactiv_Tutoriel(this);
        }


        public void SwitchToHomePage(User user)
        {
            DataContext = new UserControl_HomePage(this, user);
        }

        public void SwitchToLogIn()

        {

            DataContext = new UserControl_LogIn(this);
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
