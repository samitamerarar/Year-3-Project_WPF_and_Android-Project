using Newtonsoft.Json;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.Services;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Media;
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
    /// Interaction logic for GameInfoBar.xaml
    /// </summary>
    public partial class UserControl_GameInfoBar : UserControl
    {
        public UserControl_GameInfoBar(string Role)
        {
            InitializeComponent();

            switch (Role)
            {
                case "WRITE":
                    this.Role.Content = "Auteur";
                    this.Answer.Content = "Banane";

                    break;
                case "GUESS":
                    this.Role.Content = "Devineur";
                    this.Answer.Content = "15";
                    break;
                case "PASSIVE":
                    this.Role.Content = "Passive";
                    this.Bar1.Background = (Brush)new BrushConverter().ConvertFrom("#FF2EC3B6");
                    this.Bar2.Background = (Brush)new BrushConverter().ConvertFrom("#FF2EC3B6");
                    break;
            }
            this.MyPoints.Content = "Mon équipe: 0";
            this.Timer.Content = 30;
            this.Round.Content = "Manche: 0 ";
            this.OtherPoints.Content = "Autre équipe: 0";



        }

        public void switchtoPassiv(StateUpdate_Model stateUpdate)
        {
            if (stateUpdate.answer != null)
                this.Answer.Content = "Réponse: " + stateUpdate.answer;
            else
            {
                if (stateUpdate.attemptsLeft >= 0)
                    this.Answer.Content = "Tentatives : " + stateUpdate.attemptsLeft;
                else
                    this.Answer.Content = "Tentative: infini";
            }

            this.MyPoints.Content = "Mon équipe: " + stateUpdate.points;

            if (stateUpdate.otherPoints >= 0)
                this.OtherPoints.Content = "Autre équipe: " + stateUpdate.otherPoints;

            this.Round.Content = "Manche: " + stateUpdate.currentRound;

            switch (stateUpdate.role)
            {
                case "WRITE":
                    this.Role.Content = "Auteur";
                    break;
                case "GUESS":
                    this.Role.Content = "Devineur";
                    break;
                case "PASSIVE":
                    this.Role.Content = "Passive";
                    this.Bar1.Background = (Brush)new BrushConverter().ConvertFrom("#FF2EC3B6");
                    this.Bar2.Background = (Brush)new BrushConverter().ConvertFrom("#FF2EC3B6");
                    break;
            }

            this.Countdown(stateUpdate.startTimer / 1000, TimeSpan.FromSeconds(1), cur => this.Timer.Content = cur.ToString());
        }
        public UserControl_GameInfoBar(StateUpdate_Model stateUpdate, HomePage_ViewModel homePageViewModel)
        {
            InitializeComponent();

            if (stateUpdate.answer != null)
                this.Answer.Content = "Réponse: " + stateUpdate.answer;
            else
            {
                if (stateUpdate.attemptsLeft >= 0)
                    this.Answer.Content = "Tentatives : " + stateUpdate.attemptsLeft;
                else
                    this.Answer.Content = "Tentative: infini";
            }

            this.MyPoints.Content = "Mon équipe: " + stateUpdate.points;

            if (stateUpdate.otherPoints >= 0)
                this.OtherPoints.Content = "Autre équipe: " + stateUpdate.otherPoints;

            this.Round.Content = "Manche: " + stateUpdate.currentRound;
            
            switch (stateUpdate.role)
            {
                case "WRITE" :
                    this.Role.Content = "Auteur";
                    break;
                case "GUESS" : 
                    this.Role.Content = "Devineur";
                    break;
                case "PASSIVE" : 
                    this.Role.Content = "Passive"; 
                    this.Bar1.Background = (Brush)new BrushConverter().ConvertFrom("#FF2EC3B6"); 
                    this.Bar2.Background = (Brush)new BrushConverter().ConvertFrom("#FF2EC3B6"); 
                    break;
            }

            this.Countdown(stateUpdate.startTimer / 1000, TimeSpan.FromSeconds(1), cur => this.Timer.Content = cur.ToString());
        }


        void Countdown(int count, TimeSpan interval, Action<int> ts)
        {
            SoundPlayer player = new SoundPlayer("C:/Users/bassa/Downloads/beep-07.wav");
            SoundPlayer player2 = new SoundPlayer("C:/Users/bassa/Downloads/beep-09.wav");

            var dt = new System.Windows.Threading.DispatcherTimer();
            dt.Interval = interval;
            dt.Tick += (_, a) =>
            {
                if (count-- == 0)
                {
                    dt.Stop();
                    //player2.Play();
                }
                else
                {
                    //if (count == 5 || count == 4 || count == 3 || count == 2 || count == 1)
                        //player.Play();
                    ts(count);
                }
            };
            ts(count);
            dt.Start();
        }

    }
}
