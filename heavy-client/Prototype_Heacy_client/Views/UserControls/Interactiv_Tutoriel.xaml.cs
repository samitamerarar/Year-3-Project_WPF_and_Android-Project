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
    /// Interaction logic for Interactiv_Tutoriel.xaml
    /// </summary>
    public partial class Interactiv_Tutoriel : UserControl
    {
        public MainWindow main;
        public bool tutorialCancel;
        public Interactiv_Tutoriel(MainWindow main)
        {
            InitializeComponent();
            this.main = main;
            tutorialCancel = false;
        }

        public void switchToGame()
        {
            this.tutorialCancel = true;
            this.main.SwitchToHomePage(GlobalUser.User);
        }

        public UserControl_Write Write;
        public UserControl_Chat chatt;

        public async void step1()
        {
            Write = new UserControl_Write(this);
            this.tuto.Children.Add(Write);
            this.chatt = new UserControl_Chat(this);
            this.chat.Children.Add( this.chatt);
        }
        public void step2()
        {
            this.tuto.Children.Clear();
            this.tuto.Children.Add(new UserControl_Guess(this));
        }

        public void step3()
        {
            this.tuto.Children.Clear();
            this.tuto.Children.Add(new UserControl_Passive(this));
        }

        public void step4(UserControl_Passive pass)
        {
            this.tuto.Children.Remove(pass);
            this.tuto.Children.Add(new UserControl_Guess(pass, this));
        }

        private void UserControl_Loaded(object sender, RoutedEventArgs e)
        {
            this.Dispatcher.BeginInvoke((Action)(() =>
            {
                this.step1();

            }));
        }

        private void Quit_Click(object sender, RoutedEventArgs e)
        {
            this.switchToGame();
        }
    }
}
