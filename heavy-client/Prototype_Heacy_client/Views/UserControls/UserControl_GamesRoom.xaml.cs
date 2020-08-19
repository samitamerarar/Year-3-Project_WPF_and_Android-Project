using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using System.Windows.Controls;

namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_GamesRoom.xaml
    /// </summary>
    public partial class UserControl_GamesRoom : UserControl
    {
        public UserControl_GamesRoom(HomePage_ViewModel homePageViewModel) 
        {
            InitializeComponent();

            switch(GlobalUser.Difficulty)
            {
                case "EASY": this.GameLevel.Text = "DIFFICULTÉ : FACILE"; break;
                case "INTERMEDIATE": this.GameLevel.Text = "DIFFICULTÉ :INTERMÉDIAIRE"; break;
                case "DIFFICULT": this.GameLevel.Text = "DIFFICULTÉ :DIFFICILE"; break;
            }

            if(GlobalUser.Mode == "CLASSIC")
            {
                this.GameMode.Text = "MODE: CLASSIQUE";
            }
            else
            {
                this.GameMode.Text = "MODE: " + GlobalUser.Mode;
            }

            DataContext = new GamesRoom_ViewModel(this, homePageViewModel);
        }
    }
}
