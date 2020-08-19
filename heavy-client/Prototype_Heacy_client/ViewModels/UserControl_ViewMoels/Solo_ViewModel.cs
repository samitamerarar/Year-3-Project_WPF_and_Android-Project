using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.Services;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using Prototype_Heacy_client.Views.UserControls;
using System;
using System.ComponentModel;
using System.Windows.Media.Imaging;

namespace Prototype_Heacy_client.ViewModels
{
    public class Solo_ViewModel : INotifyPropertyChanged
    {
        private readonly string GLOBAL_USER = GlobalUser.User.user.username;
        private UserControl_Solo _soloView;
        private HomePage_ViewModel _homePageViewModel;
        private BasicMode _basicMode;
        private Group_Model _group;

        private BitmapImage profilePicAA;
        public BitmapImage ProfilePicAA { get { return profilePicAA; } set { profilePicAA = value; OnPropertyChanged("ProfilePicAA"); } }

        public GeneralCommands<object> ReturnToGroupsList { get; set; }
        public GeneralCommands<object> ReadyToStart { get; set; }
        public GeneralCommands<object> AddPlayerCommand { get; set; }

        public Solo_ViewModel(UserControl_Solo soloView, HomePage_ViewModel homePageViewModel, Group_Model group)
        {
            this._soloView = soloView;
            this._homePageViewModel = homePageViewModel;
            this._group = group;
            this._basicMode = new BasicMode(homePageViewModel, group);

            ReturnToGroupsList = new GeneralCommands<object>(this._basicMode.RetunToGroups_List, this._basicMode.CanRetunToGroups_List);
            ReadyToStart = new GeneralCommands<object>(this._basicMode.ReadyTo_Start, this._basicMode.CanBeReadyTo_Start);
            AddPlayerCommand = new GeneralCommands<object>(this._basicMode.AddPlayer, this._basicMode.CanAddPlayer);

            this._basicMode.EmitGroup();
            this.ListenListGroups();
        }

        public void ListenListGroups()
        {
            SocketService.MySocket.On("list groups", (data) =>
            {
                JArray jsonArray = JArray.Parse(data.ToString());
                foreach (object i in jsonArray)
                {
                    dynamic p = JObject.Parse(i.ToString());
                    if (p.mode == "SOLO" && p.name == this._group.createGroup.name)
                    {
                        this._homePageViewModel.HomePageView.Dispatcher.Invoke(() =>
                        {
                            this.DisplayPlayerName(p);
                            this._basicMode.DisableReadyButton(this._soloView, p.A.A);
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
                this._basicMode.pAA(this._soloView, p, this._group);
            }
            else if (p.A.A == null)
            {
                this._soloView.AA.Content = "+ Joueur";
                this.ProfilePicAA = null;
                this._group.listPlayers[0] = null;
            }
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
