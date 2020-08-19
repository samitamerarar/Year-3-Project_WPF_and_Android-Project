using Prototype_Heacy_client.Interfaces;
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
    /// Interaction logic for UserControl_GameHistory.xaml
    /// </summary>
    public partial class UserControl_GameHistory : UserControl
    {
        public UserControl_GameHistory( GameHistory game )
        {
            InitializeComponent();

            this.mode.Content = TraductionEnglishFrench(game.mode);
            double durationInSeconds = Math.Round(ConvertMillisecondsToSeconds(game.duration));
            this.duration.Content = durationInSeconds.ToString();
            this.localStart.Content = game.localstart;
            this.localEnd.Content = game.localend;
            if (game.resultCoop.ToString() == "0")
            {
                this.result.Visibility = Visibility.Collapsed;
            }
            else {
                this.resultCoop.Content = game.resultCoop.ToString();
            }

            
            if (game.resultClassic != null)
            {
                foreach (string w in game.resultClassic.w)
                {
                    this.winners.Content += w + " ";
                }
            }
            else
            {
                this.gagnants.Visibility = Visibility.Collapsed;
            }

            if (game.resultClassic != null)
            {
                foreach (string l in game.resultClassic.l)
                {
                    this.loosers.Content += l + ", ";
                }
                string loosers = this.loosers.Content.ToString();
                string loosers1 = loosers.Substring(0, loosers.Length - 2);
                this.loosers.Content = loosers1;
            }
            else {
                this.perdants.Visibility = Visibility.Collapsed;
            }
            if (game.participants != null)
            {
                if (game.mode == "")
                {

                }
                else
                {
                    foreach (string p in game.participants)
                    {
                        this.participants.Content += p + ", ";
                    }
                }
                string participants = this.participants.Content.ToString();
                string participants1 = participants.Substring(0, participants.Length - 2);
                this.participants.Content = participants1;
            }
        }
        public static string TraductionEnglishFrench(string mode) {
            if (mode == "CLASSIC")
            {
                return "Classique";
            }
            else if (mode == "SOLO")
            {
                return "Sprint solo";
            }
            else if (mode == "COOP")
            {
                return "Sprint Coopératif";
            }
            else if (mode == "TOURNAMENT")
            {
                return "Tournoi";
            }
            else {
                return "Tournoi (1 match)";
            }
        }
        public static double ConvertMillisecondsToSeconds(float milliseconds)
        {
            return TimeSpan.FromMilliseconds(milliseconds).TotalSeconds;
        }
    }
}
