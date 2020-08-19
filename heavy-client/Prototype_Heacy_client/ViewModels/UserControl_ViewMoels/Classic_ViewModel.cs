using Newtonsoft.Json.Linq;
using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.Services;
using Prototype_Heacy_client.Views.UserControls;
using System.ComponentModel;
using System.Windows.Media.Imaging;

namespace Prototype_Heacy_client.ViewModels.UserControl_ViewMoels
{
    public class Classic_ViewModel : INotifyPropertyChanged
    {
        private readonly string VIRTUAL_AB = "VIRTUAL-AB";
        private readonly string VIRTUAL_BB = "VIRTUAL-BB";
        private readonly string GLOBAL_USER = GlobalUser.User.user.username;

        private UserControl_Classic _classicView;
        private HomePage_ViewModel _homePageViewModel;

        private Group_Model _group;
        private BasicMode _basicMode ;

        private bool VABisOn = false;
        private bool VBBisON = false;

        private BitmapImage profilePicAA;
        private BitmapImage profilePicAB;
        private BitmapImage profilePicBA;
        private BitmapImage profilePicBB;
        public BitmapImage ProfilePicAA { get { return profilePicAA; } set { profilePicAA = value; OnPropertyChanged("ProfilePicAA"); } }
        public BitmapImage ProfilePicAB { get { return profilePicAB; } set { profilePicAB = value; OnPropertyChanged("ProfilePicAB"); } }
        public BitmapImage ProfilePicBA { get { return profilePicBA; } set { profilePicBA = value; OnPropertyChanged("ProfilePicBA"); } }
        public BitmapImage ProfilePicBB { get { return profilePicBB; } set { profilePicBB = value; OnPropertyChanged("ProfilePicBB"); } }

        public GeneralCommands<object> ReturnToGroupsList { get; set; }
        public GeneralCommands<object> ReadyToStart { get; set; }
        public GeneralCommands<object> AddPlayerCommand { get; set; }

        public Classic_ViewModel(UserControl_Classic classicView, HomePage_ViewModel homePageViewModel, Group_Model group)
        {
            this._classicView = classicView;
            this._homePageViewModel = homePageViewModel;
            this._group = group;

            this._basicMode = new BasicMode(homePageViewModel, group);
            ReturnToGroupsList = new GeneralCommands<object>(this._basicMode.RetunToGroups_List, this._basicMode.CanRetunToGroups_List);
            ReadyToStart = new GeneralCommands<object>(this._basicMode.ReadyTo_Start, this._basicMode.CanBeReadyTo_Start);

            AddPlayerCommand = new GeneralCommands<object>(AddPlayer, CanAddPlayer);

            this._basicMode.EmitGroup();
            SocketService.MySocket.Off("list groups");
            SocketService.MySocket.On("list groups", (data) =>
            {
                this._classicView.Dispatcher.Invoke(() =>
                {
                    JArray jsonArray = JArray.Parse(data.ToString());
                    foreach (object i in jsonArray)
                    {
                        dynamic p = JObject.Parse(i.ToString());
                        #region CLASSIC
                        if (p.mode == "CLASSIC" && p.name == group.createGroup.name)
                        {
                            if (p.A.A is object)
                            {
                                this._classicView.AA.Content = (string)p.A.A.user.username;
                                this.ProfilePicAA = this._basicMode.DisplayAvatar(this._basicMode.SaveAvatarPhoto((object)p.A.A));
                                Player_Model player = new Player_Model((string)p.A.A.user.username, (string)p.A.A.type, (string)p.A.A.role, "A.A", (bool)p.A.A.ready);
                                this._group.listPlayers[0] = player;

                                if ((string)p.A.A.user.username == GLOBAL_USER && p.A.B == null)
                                {
                                    this._classicView.AB.IsEnabled = true;
                                    this._classicView.AB.Content = "Ajouter un joueur virtuel AB";
                                    this.VABisOn = true;
                                    this._classicView.BB.IsEnabled = true;

                                }
                            }
                            else
                            {
                                this._classicView.AA.Content = "+ Joueur: Devineur";
                                this.ProfilePicAA = null;
                                this._group.listPlayers[0] = null;
                            }

                            if (p.A.B is object)
                            {
                                Player_Model player = new Player_Model((string)p.A.B.user.username, (string)p.A.B.type, (string)p.A.B.role, "A.B", (bool)p.A.B.ready);
                                this._group.listPlayers[1] = player;
                                if (p.A.B.type == "REAL")
                                {
                                    this._classicView.AB.Content = (string)p.A.B.user.username;
                                    this.ProfilePicAB = this._basicMode.DisplayAvatar(this._basicMode.SaveAvatarPhoto((object)p.A.B));
                                }
                                else
                                {
                                    this._classicView.AB.Content = VIRTUAL_AB;
                                   
                                    this._classicView.AvatarAB.ImageSource = this._basicMode.DisplayAvatar(System.Environment.CurrentDirectory.Replace("\\bin\\Debug", "/Assets/profile_virtual_player.png"));
                                    this._classicView.AB.IsEnabled = true;
                                    this._classicView.BB.IsEnabled = true;
                                }

                            }

                            else if (p.A.B == null && this.VABisOn == false)
                            {
                                this._classicView.AB.Content = "+ Joueur: Auteur";
                                this.ProfilePicAB = null;
                                this._group.listPlayers[1] = null;
                            }

                            if (p.B.A is object)
                            {
                                this._classicView.BA.Content = (string)p.B.A.user.username;
                                this.ProfilePicBA = this._basicMode.DisplayAvatar(this._basicMode.SaveAvatarPhoto((object)p.B.A));
                                Player_Model player = new Player_Model((string)p.B.A.user.username, (string)p.B.A.type, (string)p.B.A.role, "B.A", (bool)p.B.A.ready);
                                this._group.listPlayers[2] = player;

                                if ((string)p.B.A.user.username == GLOBAL_USER && p.B.B == null)
                                {
                                    this._classicView.BB.IsEnabled = true;
                                    this._classicView.BB.Content = "Ajouter un joueur virtuel BB";
                                    this.VBBisON= true;
                                    this._classicView.AB.IsEnabled = true;
                                }
                            }

                            else
                            {
                                this._classicView.BA.Content = "+ Joueur: Devineur";
                                this.ProfilePicBA = null;
                                this._group.listPlayers[2] = null;
                            }

                            if (p.B.B is object)
                            {
                                Player_Model player = new Player_Model((string)p.B.B.user.username, (string)p.B.B.type, (string)p.B.B.role, "B.B", (bool)p.B.B.ready);
                                this._group.listPlayers[3] = player;
                                if (p.B.B.type == "REAL")
                                {
                                    this._classicView.BB.Content = (string)p.B.B.user.username;
                                    this.ProfilePicBB = this._basicMode.DisplayAvatar(this._basicMode.SaveAvatarPhoto((object)p.B.B));
                                }
                                else
                                {
                                    this._classicView.BB.Content = VIRTUAL_BB;
                                    this._classicView.BB.IsEnabled = true;
                                    this._classicView.AvatarBB.ImageSource = this._basicMode.DisplayAvatar(System.Environment.CurrentDirectory.Replace("\\bin\\Debug", "/Assets/profile_virtual_player.png"));

                                    this._classicView.AB.IsEnabled = true;
                                }

                            }
                            else if (p.B.B == null && this.VBBisON == false)
                            {
                                this._classicView.BB.Content = "+ Joueur: Auteur";
                                this._group.listPlayers[3] = null;
                                this.ProfilePicBB = null;
                            }

                            this.EnablePlayersButtons(p);
                            this.DisableReadyButton(p);
                        }
                        #endregion
                    }
                });
            });
        }
 
        public void AddPlayer(object playerPos)
        {
            if ((string)playerPos == "A.A")
                this._basicMode.CanSendPlayer("A.A", "REAL", GLOBAL_USER);
            
            if ((string)playerPos == "B.A")
                this._basicMode.CanSendPlayer("B.A", "REAL", GLOBAL_USER);

            if((string)playerPos == "A.B")
            {
                if (this.VABisOn)
                    this._basicMode.SendPlayer("A.B", "VIRTUAL", VIRTUAL_AB);
                else
                    this._basicMode.CanSendPlayer("A.B", "REAL", GLOBAL_USER);
            }

            if ((string)playerPos == "B.B")
            {
                if (this.VBBisON)
                    this._basicMode.SendPlayer("B.B", "VIRTUAL", VIRTUAL_BB);
                else
                    this._basicMode.CanSendPlayer("B.B", "REAL", GLOBAL_USER);
            }

        }
        public bool CanAddPlayer(object o)
        {
            return true;
        }

        public void EnablePlayersButtons(dynamic p)
        {
            if (p.A.A is object && p.B.A is object)
            {
                if (((string)p.A.A.user.username != GLOBAL_USER) && ((string)p.B.A.user.username != GLOBAL_USER))
                {
                    this._classicView.AB.IsEnabled = true;
                    this._classicView.BB.IsEnabled = true;
                }
            }
        }

        public void DisableReadyButton(dynamic p)
        {
            this._basicMode.DisableReadyButton(this._classicView, p.A.A);
            this._basicMode.DisableReadyButton(this._classicView, p.A.B);
            this._basicMode.DisableReadyButton(this._classicView, p.B.A);
            this._basicMode.DisableReadyButton(this._classicView, p.B.B);
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
