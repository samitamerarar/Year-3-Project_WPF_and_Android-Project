using System.ComponentModel;
using System.Drawing;
using System.Net;
using System.Windows.Controls;
using System.Windows.Media;
using System.Windows.Media.Imaging;

namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_PlayerTournament.xaml
    /// </summary>
    public partial class UserControl_PlayerTournament : UserControl , INotifyPropertyChanged
    {
        private BitmapImage profilePic;
        public BitmapImage ProfilePic { get { return profilePic; } set { profilePic = value; OnPropertyChanged("ProfilePic"); } }
        public UserControl_PlayerTournament(string playerName, string state, string bitmap)
        {
            InitializeComponent();
            DataContext = this;

            this.Player.Text = playerName;

            string imagePath = System.Environment.CurrentDirectory.Replace("\\bin\\Debug", "/Assets/profilePic.jpg");

            if (bitmap != null)
            {
                using (var client = new WebClient())
                {

                    client.DownloadFile(Prototype_Heacy_client.Services.Http.UrlServer + "image/" + bitmap, bitmap);
                }
                imagePath = System.Environment.CurrentDirectory + "/" + bitmap;
            }

            using (var stream = System.IO.File.OpenRead(imagePath))
            {
                var image = new BitmapImage();
                image.BeginInit();
                image.CacheOption = BitmapCacheOption.OnLoad;
                image.StreamSource = stream;
                image.EndInit();

                this.ProfilePic = image;
            }

            switch (state)
            {
                case "WAITING":
                    this.Player.Background = new SolidColorBrush(Colors.Yellow);
                    break;
                case "PLAYING":
                    this.Player.Background = new SolidColorBrush(Colors.Green);
                    break;
                case "WINNER":
                    this.Player.Background = new SolidColorBrush(Colors.DarkGoldenrod);
                    break;
                case "LOSER":
                    this.Player.Background = new SolidColorBrush(Colors.Red);
                    break;
            }
        }
        public UserControl_PlayerTournament()
        {
            InitializeComponent();
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
