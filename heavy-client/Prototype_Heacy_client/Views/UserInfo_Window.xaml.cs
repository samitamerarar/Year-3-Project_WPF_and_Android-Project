using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.Services;
using Prototype_Heacy_client.Views.UserControls;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.IO;
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
using System.Windows.Shapes;

namespace Prototype_Heacy_client.Views
{
    /// <summary>
    /// Interaction logic for UserInfo_Window.xaml
    /// </summary>
    public partial class UserInfo_Window : Window, INotifyPropertyChanged
    {
        private BitmapImage source ;
        public BitmapImage Source { get { return source; } set { source = value; OnPropertyChanged("Source"); } }
        public UserInfo_Window()
        {
            InitializeComponent();
            this.DisplayInfo();
            DataContext = this;
            source = new BitmapImage();
            using (var stream = File.OpenRead(GlobalUser.Avatar))
            {
                var image = new BitmapImage();
                image.BeginInit();
                image.CacheOption = BitmapCacheOption.OnLoad;
                image.StreamSource = stream;
                image.EndInit();
                this.Source = image;
            }
            this.button.IsHitTestVisible = false;
        }

        public async void DisplayInfo()
        {
            var res = await Http.Client.GetAsync(Http.UrlServer + "user/profile/" + GlobalUser.UserName);
            var resp = await res.Content.ReadAsStringAsync();
            JObject json = JObject.Parse(resp);
            if (json["data"]["victoryPercent"].ToString() == "")
            {
                json["data"]["victoryPercent"] = 0;
            }

            Statistics UserFromProfil = JsonConvert.DeserializeObject<Statistics>(json["data"].ToString());

            this.firstName.Content = UserFromProfil.firstName;
            this.lastName.Content = UserFromProfil.lastName;
            this.UserName.Content = UserFromProfil.username;
            this.Matchs.Content = UserFromProfil.matchsPlayed.ToString();
            this.tournamentsWon.Content = UserFromProfil.tournamentsWon.ToString();
            this.Victory.Content = (UserFromProfil.victoryPercent * 100).ToString();
            double a = Math.Round(ConvertMillisecondsToSeconds(UserFromProfil.averageDuration));
            this.Average.Content = a.ToString();
            double b = Math.Round(ConvertMillisecondsToSeconds(UserFromProfil.totalMatchDuration));
            this.TotalDuration.Content = b.ToString();
            this.SoloScore.Content = UserFromProfil.soloHighestScore.ToString();
            this.CoopScore.Content = UserFromProfil.coopHighestScore.ToString();
            foreach(var game in UserFromProfil.gameHistory)
            {
                this.games.Children.Add(new UserControl_GameHistory(game));

            }
            foreach (var history in UserFromProfil.history)
            {
                this.login.Children.Add(new Label {
                    Content = history.login,
                    FontWeight = FontWeights.Bold,
                    Foreground = new SolidColorBrush(Colors.Black),
                    FontSize = 12
                });
                this.logout.Children.Add(new Label
                {
                    Content = history.logout,
                    FontWeight = FontWeights.Bold,
                    Foreground = new SolidColorBrush(Colors.Black),
                    FontSize = 12
                });
            }

        }

        public static double ConvertMillisecondsToSeconds(float milliseconds)
        {
            return TimeSpan.FromMilliseconds(milliseconds).TotalSeconds;
        }

        public event PropertyChangedEventHandler PropertyChanged;

        private void OnPropertyChanged(string propertyName)
        {
            PropertyChangedEventHandler handler = PropertyChanged;

            if (handler != null)
            {
                handler(this, new PropertyChangedEventArgs(propertyName));
            }
        }

    }
}
