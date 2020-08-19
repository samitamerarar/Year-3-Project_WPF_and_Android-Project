using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.Services;
using Prototype_Heacy_client.Views;
using Prototype_Heacy_client.Views.UserControls;
using System;
using System.Collections;
using System.Collections.Generic;

namespace Prototype_Heacy_client.ViewModels.UserControl_ViewMoels
{
    public class GamesRoom_ViewModel
    {
        public HomePage_ViewModel _homePageViewModel;
        public UserControl_GamesRoom _gameRoomView;
        public Group_Model _specificGroup = null;

        public List<Group_Model> _listGroups = new List<Group_Model>();
        public List<Player_Model> _listPlayers = new List<Player_Model>();
        public List<ArrayList> _listReady = new List<ArrayList>();

        public GeneralCommands<object> ReturnToHomePage { get; set; }
        public GeneralCommands<object> OpenCreateGroup { get; set; }

        public GamesRoom_ViewModel(UserControl_GamesRoom gameRoomView, HomePage_ViewModel homePageViewModel)
        {
            this._gameRoomView = gameRoomView;
            this._homePageViewModel = homePageViewModel;

            ReturnToHomePage = new GeneralCommands<object>(ReturnToHome, CanReturnToHome);
            OpenCreateGroup = new GeneralCommands<object>(OpenCreate_Group, CanOpenCreate_Group);

            this.ListenToGroups();
            this.ConnectToGroups();
        }
        public void ConnectToGroups()
        {
            var group = JsonConvert.SerializeObject(new ModeDifficulty_Server(GlobalUser.Mode, GlobalUser.Difficulty));
            SocketService.MySocket.Emit("list groups", group);
        }
        public void ListenToGroups()
        {
            SocketService.MySocket.On("list groups", (data) =>
            {
                this._gameRoomView.Dispatcher.Invoke(() =>
                {
                    this._gameRoomView.stakGroups.Children.Clear();
                    this._listGroups.Clear();

                    Console.WriteLine(data);

                    JArray jsonArray = JArray.Parse(data.ToString());
                    foreach (object i in jsonArray)
                    {
                        dynamic g = JObject.Parse(i.ToString());

                        switch ((string)g.mode)
                        {
                            case "CLASSIC":
                                this._specificGroup = this.BuildClassicGroup(g);
                                break;
                            case "SOLO":
                                this._specificGroup = this.BuildSoloGroup(g);
                                break;
                            case "COOP":
                                this._specificGroup = this.BuildCoopGroup(g);
                                break;
                            case "TOURNAMENT":
                                this._specificGroup = this.BuildTournamenGroup(g);
                                break;
                        }

                        this._listGroups.Add(this._specificGroup);
                        this._gameRoomView.stakGroups.Children.Add(new UserControl_Group(this._homePageViewModel, this._specificGroup));
                    }
                });
            });
        }

        public Group_Model BuildTournamenGroup(dynamic g)
        {
            this._listPlayers.Clear();
            if (g.players.Count != 0)
            {
                foreach (dynamic player in g.players)
                {
                    Player_Model p = new Player_Model((string)player.user.username, (string)player.user.avatar, (string)player.type, (string)player.role, "A.A", (bool)player.ready);
                    this._listPlayers.Add(p);
                }
            }

            return new Group_Model((string)g.id, new CreateGroup_Server((string)g.name, (string)g.difficulty, (string)g.mode), this._listPlayers, this._listReady);
        }

        public Group_Model BuildSoloGroup(dynamic g)
        {
            Player_Model pAA = this.CreatePlayer(g.A.A);

            this._listPlayers = new List<Player_Model> { pAA };

            Group_Model group = new Group_Model((string)g.id,
                                                new CreateGroup_Server((string)g.name, (string)g.difficulty, (string)g.mode),
                                                this._listPlayers);
            return group;
        }

        public Group_Model BuildClassicGroup(dynamic g)
        {
            Player_Model pAA = this.CreatePlayer(g.A.A);
            Player_Model pAB = this.CreatePlayer(g.A.B);
            Player_Model pBA = this.CreatePlayer(g.B.A);
            Player_Model pBB = this.CreatePlayer(g.B.B);

            this._listPlayers = new List<Player_Model> { pAA, pAB, pBA, pBB };

            Group_Model group = new Group_Model((string)g.id,
                                                new CreateGroup_Server((string)g.name, (string)g.difficulty, (string)g.mode),
                                                this._listPlayers);
            return group;

        }

        public Group_Model BuildCoopGroup(dynamic g)
        {
            Player_Model pAA = this.CreatePlayer(g.A.A);
            Player_Model pAB = this.CreatePlayer(g.A.B);
            Player_Model pAC = this.CreatePlayer(g.A.C);
            Player_Model pAD = this.CreatePlayer(g.A.D);

            this._listPlayers = new List<Player_Model> { pAA, pAB, pAC, pAD };

            Group_Model group = new Group_Model((string)g.id,
                                                new CreateGroup_Server((string)g.name, (string)g.difficulty, (string)g.mode),
                                                this._listPlayers);
            return group;

        }
        
        public Player_Model CreatePlayer(dynamic player)
        {
            Player_Model p = null;
            if (player is object)
            {
                if((string)player.user.avatar != null)
                    new Player_Model((string)player.user.username, (string)player.user.avatar, (string)player.type, (string)player.role, "A.A", (bool)player.ready);
            }

            return p;
        }

        public void ReturnToHome(object o)
        {
            this._homePageViewModel._homePageView.MiddleView.Children.Clear();
            this._homePageViewModel._homePageView.MiddleView.Children.Add(this._homePageViewModel._gameModeView);
        }
        public bool CanReturnToHome(object o)
        {
            return true;
        }
        public void OpenCreate_Group(object o)
        {
            new GroupCreationWindow().ShowDialog();
        }
        public bool CanOpenCreate_Group(object o)
        {
            return true;
        }
    }
}
