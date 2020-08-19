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
using Prototype_Heacy_client.Models;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using Newtonsoft.Json.Linq;
using Prototype_Heacy_client.Services;
using System.Net;
using Newtonsoft.Json;
using System.Net.Http;

namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_HomePage.xaml
    /// </summary>
    public partial class UserControl_HomePage : UserControl
    {

        public MainWindow _window;
        public User user;
        public HomePage_ViewModel home;

        public UserControl_HomePage(MainWindow window, User user)
        {
            InitializeComponent();
            this.user = user;
            this.button.IsHitTestVisible = false;
            this.home = new HomePage_ViewModel(this);
            DataContext = this.home;
            this._window = window;
        }

        public void Disable()
        {
            this.MyChat.IsEnabled = false;
        }

       
        public void SwitchToLogin()
        {
            _window.SwitchToLogIn();
        }


        private void ButtonClose_Click(object sender, RoutedEventArgs e)
        {
            Application.Current.Shutdown();
        }

        private void ButtonOpenMenu_Click(object sender, RoutedEventArgs e)
        {
            ButtonOpenMenu.Visibility = Visibility.Collapsed;
            ButtonCloseMenu.Visibility = Visibility.Visible;
        }

        private void ButtonCloseMenu_Click(object sender, RoutedEventArgs e)
        {
            ButtonOpenMenu.Visibility = Visibility.Visible;
            ButtonCloseMenu.Visibility = Visibility.Collapsed;
        }
     
        private void StartTutorial_Click(object sender, RoutedEventArgs e)
        {
            System.Windows.MessageBoxResult dialogResult = MessageBox.Show("Voulez vous lancer le tutoriel", "Lancer Tutoriel", System.Windows.MessageBoxButton.YesNo);
            if (dialogResult == System.Windows.MessageBoxResult.Yes)
            {
                this._window.switchTuto();
            }
           
        }

        private async void Button_Click(object sender, RoutedEventArgs e)
        {
            System.Windows.MessageBoxResult dialogResult = MessageBox.Show("Voulez supprimer votre Compte", "Supprimer compte", System.Windows.MessageBoxButton.YesNo);
            if (dialogResult == System.Windows.MessageBoxResult.Yes)
            {
                var response = await Http.Client.PostAsync(Http.UrlServer + "user/delete/" + GlobalUser.User.user.username, new StringContent("", Encoding.UTF8, "application/json"));
                var responseString = await response.Content.ReadAsStringAsync();
                if (((int)response.StatusCode) == 200)
                {
                    GameCreationFeedback_window a = new GameCreationFeedback_window("Votre Compte a ete supprimer", true);
                    a.ShowDialog();
                    object o = new object();
                    this.home.Log_out(o);
                }
                else
                {
                    GameCreationFeedback_window a = new GameCreationFeedback_window("Votre Compte n'a pas ete supprimer, veuillez reesseyer", true);
                    a.ShowDialog();

                }
            }

        }
    }
}
