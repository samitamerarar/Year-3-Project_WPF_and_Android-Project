using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.Services;
using Prototype_Heacy_client.Views.UserControls;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Prototype_Heacy_client.ViewModels.UserControl_ViewMoels
{
    public class Tournament_ViewModel
    {

        private readonly string GLOBAL_USER = GlobalUser.User.user.username;

        private HomePage_ViewModel _homePageViewModel;
        private UserControl_Tournament _tournamentView;
        private Group_Model _group;
        private BasicMode _basicMode;

        public GeneralCommands<object> ReturnToGroupsList { get; set; }
        public GeneralCommands<object> ReadyToStart { get; set; }
        public GeneralCommands<object> AddPlayerCommand { get; set; }
        public Tournament_ViewModel(UserControl_Tournament tournamentView, HomePage_ViewModel homePageViewModel, Group_Model group)
        {
            this._tournamentView = tournamentView;
            this._homePageViewModel = homePageViewModel;
            this._group = group;

            this._basicMode = new BasicMode(homePageViewModel, group);
            ReturnToGroupsList = new GeneralCommands<object>(this._basicMode.RetunToGroups_List, this._basicMode.CanRetunToGroups_List);
            ReadyToStart = new GeneralCommands<object>(this._basicMode.ReadyTo_Start, this._basicMode.CanBeReadyTo_Start);
            AddPlayerCommand = new GeneralCommands<object>(this._basicMode.AddPlayer, this._basicMode.CanAddPlayer);

            //this.AddExistingPlayers();

            var groupp = JsonConvert.SerializeObject(new ModeDifficulty_Server(GlobalUser.Mode, GlobalUser.Difficulty));
            SocketService.MySocket.Emit("list groups", groupp);

            SocketService.MySocket.On("list groups", (data) =>
            {
                tournamentView.Dispatcher.Invoke(() =>
                {
                    JArray jsonArray = JArray.Parse(data.ToString());
                    foreach (object i in jsonArray)
                    {
                        dynamic p = JObject.Parse(i.ToString());
                        if (p.mode == "TOURNAMENT" && p.name == group.createGroup.name)
                        {
                            this._homePageViewModel.HomePageView.Dispatcher.Invoke(() =>
                            {
                                this.ClearStacks();
                                this.DisplayerPlayerName(p);
                                this.DisplayStacks();

                            });
                        }
                    }
                });
            });
        }
        public void ClearStacks()
        {
            this._tournamentView.stakTournament1.Children.Clear();
            this._tournamentView.stakTournament2.Children.Clear();
            this._tournamentView.stakTournament3.Children.Clear();
            this._tournamentView.stakTournament4.Children.Clear();
        }
        public void DisplayerPlayerName(dynamic p)
        {
            if (p.players.Count != 0)
            {
                foreach (dynamic player in p.players)
                {
                    if ((string)player.user.username == GLOBAL_USER)
                        this._tournamentView.AA.Content = (string)player.user.username;
                }
            }
        }
        public void DisplayStacks()
        {
            if (this._group.listPlayers.Count != 0)
            {
                foreach (Player_Model player in this._group.listPlayers)
                {
                    if (this._tournamentView.stakTournament1.Children.Count < 4)
                        this._tournamentView.stakTournament1.Children.Add(new UserControl_PlayerTournament(player.username, (string)"", (string)player.avatar));
                    else if (this._tournamentView.stakTournament1.Children.Count >= 4 && this._tournamentView.stakTournament2.Children.Count < 4)
                        this._tournamentView.stakTournament2.Children.Add(new UserControl_PlayerTournament(player.username, (string)"", (string)player.avatar));
                    else if (this._tournamentView.stakTournament2.Children.Count >= 4 && this._tournamentView.stakTournament3.Children.Count < 4)
                        this._tournamentView.stakTournament3.Children.Add(new UserControl_PlayerTournament(player.username, (string)"", (string)player.avatar));
                    else
                        this._tournamentView.stakTournament4.Children.Add(new UserControl_PlayerTournament(player.username, (string)"", (string)player.avatar));
                }
            }
        }
    }
}
