using Newtonsoft.Json;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using Prototype_Heacy_client.Views.UserControls;
using System;
using System.IO;
using System.Net;
using System.Windows;
using System.Windows.Media.Imaging;

namespace Prototype_Heacy_client.Services
{
    public class BasicMode
    {
        private readonly string GLOBAL_USER = GlobalUser.User.user.username;
        
        private HomePage_ViewModel _homePageViewModel;
        private Group_Model _group;

        #region COMMANDS
        public BasicMode(HomePage_ViewModel homePageViewModel, Group_Model group)
        {
            this._homePageViewModel = homePageViewModel;
            this._group = group;
        }

        public void EmitGroup()
        {
            var group = JsonConvert.SerializeObject(new ModeDifficulty_Server(GlobalUser.Mode, GlobalUser.Difficulty));
            SocketService.MySocket.Emit("list groups", group);
        }

        public void AddPlayer(object playerPos)
        {
            if ((string)playerPos == "A.A")
                this.CanSendPlayer("A.A", "REAL", GLOBAL_USER);

            if ((string)playerPos == "A.B")
                this.CanSendPlayer("A.B", "REAL", GLOBAL_USER);

            if ((string)playerPos == "A.C")
                this.CanSendPlayer("A.C", "REAL", GLOBAL_USER);

            if ((string)playerPos == "A.D")
                this.CanSendPlayer("A.D", "REAL", GLOBAL_USER);
        }
        public bool CanAddPlayer(object o)
        {
            return true;
        }
        public void CanSendPlayer(string position, string type, string username)
        {
            if (!this.CheckPlayerExistance())
                this.SendPlayer(position, type, username);
            else
                MessageBox.Show("Vous etes deja en position!");
        }
        public void SendPlayer(string position, string type, string username)
        {
            string avatar = GlobalUser.Avatar.ToString();
            string avatarId = avatar.Substring(avatar.IndexOf("/") + 1);
            string IdToSend = null;

            if (avatarId != "Assets/profilePic.jpg")
                IdToSend = avatarId;

            Console.WriteLine(IdToSend);
            AddPlayer_Server addPlayerForm = new AddPlayer_Server(this._group.gid, position, type, username, IdToSend);
            var PlayerForm = JsonConvert.SerializeObject(addPlayerForm);
            SocketService.MySocket.Emit("add player", PlayerForm);
        }

        public bool CheckPlayerExistance()
        {
            bool playerExist = false;

            this._group.listPlayers.ForEach(delegate (Player_Model p)
            {
                if (p is Player_Model)
                {
                    if (p.username == GLOBAL_USER)
                        playerExist = true;
                }
            });

            return playerExist;
        }
        public string GetPosition()
        {
            string pos = "";

            this._group.listPlayers.ForEach(delegate (Player_Model p)
            {
                if (p is Player_Model)
                {
                    if (p.username == GlobalUser.User.user.username)
                        pos = p.position;
                }
            });

            return pos;
        }

        public void ReadyTo_Start(object o)
        {
            Console.WriteLine(GetPosition());
            if (this.GetPosition() == "") { MessageBox.Show("Veuillez d'abord choisir une place."); return; }

            var playerReady = JsonConvert.SerializeObject(new ReadyPlayer_Server(this._group.gid, this.GetPosition()));
            SocketService.MySocket.Emit("player ready", playerReady);
        }
        public bool CanBeReadyTo_Start(object o)
        {
            return true;
        }
        public void RetunToGroups_List(object o)
        {
            if (!CheckPlayerExistance())
            {
                this._homePageViewModel._homePageView.MiddleView.Children.Clear();
                this._homePageViewModel._homePageView.MiddleView.Children.Add(new UserControl_GamesRoom(this._homePageViewModel));
            }
            else
                MessageBox.Show("Vous etes en position!. Déconnecter d'abord SVP.");
        }

        public bool CanRetunToGroups_List(object o)
        {
            return true;
        }

        public string SaveAvatarPhoto(dynamic player)
        {
            string imagePath = System.Environment.CurrentDirectory.Replace("\\bin\\Debug", "/Assets/profilePic.jpg");

            if (player.user.avatar != null)
            {
                using (var client = new WebClient())
                {

                    client.DownloadFile(Http.UrlServer + "image/" + (string)player.user.avatar, (string)player.user.avatar);
                }
                imagePath = System.Environment.CurrentDirectory + "/" + (string)player.user.avatar;
            }
            return imagePath;
        }
        public BitmapImage DisplayAvatar(string path)
        {
            using (var stream = File.OpenRead(path))
            {
                var image = new BitmapImage();
                image.BeginInit();
                image.CacheOption = BitmapCacheOption.OnLoad;
                image.StreamSource = stream;
                image.EndInit();

                return image;
            }
        }

        #endregion

        public void pAA(dynamic view, dynamic p, Group_Model group)
        {
            view.AA.Content = (string)p.A.A.user.username;
            Player_Model player = new Player_Model((string)p.A.A.user.username, (string)p.A.A.type, (string)p.A.A.role, "A.A", (bool)p.A.A.ready);
            group.listPlayers[0] = player;

        }

        public void pAB(dynamic view, dynamic p, Group_Model group)
        {
            view.AB.Content = (string)p.A.B.user.username;
            Player_Model player = new Player_Model((string)p.A.B.user.username, (string)p.A.B.type, (string)p.A.B.role, "A.B", (bool)p.A.B.ready);
            group.listPlayers[1] = player;
        }

        public void DisableReadyButton(dynamic view, dynamic p)
        {
            if (p is object)
                if (p.user.username == GLOBAL_USER && p.ready == true & p.type == "REAL")
                    view.ReadyButton.IsEnabled = false;
        }

        //public void GoToGameViews()
        //{
        //    var passiv = new UserControl_Passive();

        //    SocketService.MySocket.On("state update", (data) =>
        //    {
        //        Console.WriteLine(data);
        //        StateUpdate_Model d = JsonConvert.DeserializeObject<StateUpdate_Model>(data.ToString());

        //        if (d.role == "WRITE" && d.answer != null)
        //        {
        //            this._homePageViewModel.HomePageView.Dispatcher.Invoke(() =>
        //            {
        //                this._homePageViewModel.HomePageView.MiddleView.Children.Clear();
        //                this._homePageViewModel.HomePageView.MiddleView.Children.Add(new UserControl_Write(d, this._homePageViewModel)); ;
        //            });
        //        }

        //        else if (d.role == "GUESS" && d.answer == null)
        //        {
        //            this._homePageViewModel.HomePageView.Dispatcher.Invoke(() =>
        //            {
        //                if (d.clear)
        //                {
        //                    this._homePageViewModel.HomePageView.MiddleView.Children.Clear();
        //                    this._homePageViewModel.HomePageView.MiddleView.Children.Add(new UserControl_Guess(d, this._homePageViewModel));
        //                }
        //                else
        //                {
        //                    this._homePageViewModel.HomePageView.MiddleView.Children.Remove(passiv);
        //                    this._homePageViewModel.HomePageView.MiddleView.Children.Add(new UserControl_Guess(passiv));
        //                }
        //            });
        //        }

        //        else if (d.role == "PASSIVE" && d.answer == null)
        //        {
        //            this._homePageViewModel.HomePageView.Dispatcher.Invoke(() =>
        //            {
        //                this._homePageViewModel.HomePageView.MiddleView.Children.Clear();
        //                passiv = new UserControl_Passive(d, this._homePageViewModel);
        //                this._homePageViewModel.HomePageView.MiddleView.Children.Add(passiv);
        //            });
        //        }

        //    });
        //}

    }
}
