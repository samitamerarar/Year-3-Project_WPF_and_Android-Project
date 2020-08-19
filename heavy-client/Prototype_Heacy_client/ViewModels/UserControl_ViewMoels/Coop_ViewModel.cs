using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.Services;
using Prototype_Heacy_client.Views.UserControls;
using System.ComponentModel;
using System.Windows.Media.Imaging;

namespace Prototype_Heacy_client.ViewModels.UserControl_ViewMoels
{
    public class Coop_ViewModel : INotifyPropertyChanged
    {
        private readonly string GLOBAL_USER = GlobalUser.User.user.username;

        private HomePage_ViewModel _homePageViewModel;
        private UserControl_Coop _coopView;
        private Group_Model _group;
        private BasicMode _basicMode;
        private BitmapImage profilePicAA;
        private BitmapImage profilePicAB;
        private BitmapImage profilePicAC;
        private BitmapImage profilePicAD;
        public BitmapImage ProfilePicAA { get { return profilePicAA; } set { profilePicAA = value; OnPropertyChanged("ProfilePicAA"); }   }
        public BitmapImage ProfilePicAB { get { return profilePicAB; } set { profilePicAB = value; OnPropertyChanged("ProfilePicAB"); }   }
        public BitmapImage ProfilePicAC { get { return profilePicAC; } set { profilePicAC = value; OnPropertyChanged("ProfilePicAC"); }   }
        public BitmapImage ProfilePicAD { get { return profilePicAD; } set { profilePicAD = value; OnPropertyChanged("ProfilePicAD"); }   }

        public GeneralCommands<object> ReturnToGroupsList { get; set; }
        public GeneralCommands<object> ReadyToStart { get; set; }
        public GeneralCommands<object> AddPlayerCommand { get; set; }
        public Coop_ViewModel(UserControl_Coop coopView, HomePage_ViewModel homePageViewModel, Group_Model group)
        {
            this._coopView = coopView;
            this._homePageViewModel = homePageViewModel;
            this._group = group;

            this._basicMode = new BasicMode(homePageViewModel, group);
            ReturnToGroupsList = new GeneralCommands<object>(this._basicMode.RetunToGroups_List, this._basicMode.CanRetunToGroups_List);
            ReadyToStart = new GeneralCommands<object>(this._basicMode.ReadyTo_Start, this._basicMode.CanBeReadyTo_Start);
            AddPlayerCommand = new GeneralCommands<object>(this._basicMode.AddPlayer, this._basicMode.CanAddPlayer);

            this._basicMode.EmitGroup();

            SocketService.MySocket.On("list groups", (data) =>
            {
                JArray jsonArray = JArray.Parse(data.ToString());
                foreach (object i in jsonArray)
                {
                    dynamic p = JObject.Parse(i.ToString());
                    if (p.mode == "COOP" && p.name == group.createGroup.name)
                    {
                        this._homePageViewModel.HomePageView.Dispatcher.Invoke(() =>
                        {
                            this.DisplayPlayerName(p);
                            this.DisableReadyButtons(p);
                        });
                    }
                }
            });
        }


        public void DisplayPlayerName(dynamic p)
        {

            if (p.A.A is object)
            {
                this.ProfilePicAA = this._basicMode.DisplayAvatar(this._basicMode.SaveAvatarPhoto((object)p.A.A));
                this._basicMode.pAA(this._coopView, p, this._group);
            }
            else
            {
                this._coopView.AA.Content = "+ Joueur";
                this.ProfilePicAA = null;
                this._group.listPlayers[0] = null;
            }

            if (p.A.B is object)
            {
                this.ProfilePicAB = this._basicMode.DisplayAvatar(this._basicMode.SaveAvatarPhoto((object)p.A.B));
                this._basicMode.pAB(this._coopView, p, this._group);
            }
            else
            {
                this._coopView.AB.Content = "+ Joueur";
                this.ProfilePicAB = null;
                this._group.listPlayers[1] = null;
            }

            if (p.A.C is object)
            {
                this._coopView.AC.Content = (string)p.A.C.user.username;
                this.ProfilePicAC = this._basicMode.DisplayAvatar(this._basicMode.SaveAvatarPhoto((object)p.A.C));
                Player_Model player = new Player_Model((string)p.A.C.user.username, (string)p.A.C.type, (string)p.A.C.role, "A.C", (bool)p.A.C.ready);
                this._group.listPlayers[2] = player;
            }
            else
            {
                this._coopView.AC.Content = "+ Joueur";
                this.ProfilePicAC = null;
                this._group.listPlayers[2] = null;
            }

            if (p.A.D is object)
            {
                this._coopView.AD.Content = (string)p.A.D.user.username;
                this.ProfilePicAD = this._basicMode.DisplayAvatar(this._basicMode.SaveAvatarPhoto((object)p.A.D));
                Player_Model player = new Player_Model((string)p.A.D.user.username, (string)p.A.D.type, (string)p.A.D.role, "A.D", (bool)p.A.D.ready);
                this._group.listPlayers[3] = player;
            }
            else
            {
                this._coopView.AD.Content = "+ Joueur";
                this.ProfilePicAD = null;
                this._group.listPlayers[3] = null;
            }

        }
        public void DisableReadyButtons(dynamic p)
        {
            this._basicMode.DisableReadyButton(this._coopView, p.A.A);
            this._basicMode.DisableReadyButton(this._coopView, p.A.B);
            this._basicMode.DisableReadyButton(this._coopView, p.A.C);
            this._basicMode.DisableReadyButton(this._coopView, p.A.D);
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
