using Newtonsoft.Json;
using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.Services;
using Prototype_Heacy_client.Views;
using Prototype_Heacy_client.Views.UserControls;
using System;
using System.Windows;
using System.ComponentModel;
using Newtonsoft.Json.Linq;
using System.Net;
using System.Threading;
using System.Windows.Media.Imaging;
using System.IO;

namespace Prototype_Heacy_client.ViewModels.UserControl_ViewMoels
{
    public class HomePage_ViewModel : INotifyPropertyChanged
    {

        public UserControl_HomePage _homePageView;
        public UserControl_GameMode _gameModeView;
        public UserControl_ChatControl _myChat;
        public bool loggedOut;

        public GeneralCommands<object> Create { get; set; }
        public GeneralCommands<object> LogOut { get; set; }
        public GeneralCommands<object> ExtractChat { get; set; }
        public GeneralCommands<object> CloseChat { get; set; }
        public GeneralCommands<object> CreateAssisted { get; set; }
        public GeneralCommands<object> OpenGalleryCommand { get; set; }
        public GeneralCommands<object> ShowUserProfil { get; set; }
        public GeneralCommands<object> UpdateUserProfil { get; set; }

        public string lastState = "";

        private BitmapImage source;

        public BitmapImage Source {
            get { return source; }
            set { source = value; OnPropertyChanged("Source");}
        }

        public UserControl_HomePage HomePageView
        {
            get { return _homePageView; }
            set { _homePageView = value; }
        }

            public UserControl_Guess guess;
        public HomePage_ViewModel(UserControl_HomePage homePage)
        {
            this._homePageView = homePage;
            this._myChat = new UserControl_ChatControl(this.HomePageView.user);
            this._gameModeView = new UserControl_GameMode(this);

            this.ExtractChat= new GeneralCommands<object>(Extract_Chat, CanExtractChat);
            this.LogOut = new GeneralCommands<object>(Log_out, CanLogOut);
            this.Create = new GeneralCommands<object>(CreateGame, CanCreateGame);
            this.CreateAssisted = new GeneralCommands<object>(CreatGameAssisted, CanCreateGame);
            this.OpenGalleryCommand = new GeneralCommands<object>(OpenGallery, CanOpenGallery);
            this.ShowUserProfil = new GeneralCommands<object>(Show_user_profil, Can_Show_Profil);
            this.UpdateUserProfil = new GeneralCommands<object>(Show_update_user_profil, Can_Show_update_profil);
            this._homePageView.MyChat.Children.Add(_myChat);
            this._homePageView.MiddleView.Children.Add(_gameModeView);
            this.loggedOut = false;
            SocketService.MySocket.Off("disconnect");
            SocketService.MySocket.On("disconnect", () =>
            {

                _homePageView.Dispatcher.Invoke(() =>
                {
                    if (!loggedOut)
                    {
                        GameCreationFeedback_window a = new GameCreationFeedback_window("Connexion perdue", false);
                        a.ShowDialog();
                    }
                    
                    SocketService.MySocket.Close();
                    SocketService.ResetSocket();
                    _homePageView.SwitchToLogin();
                });
                

            });
            getInfo();

        }
        public async void getInfo()
        {
          
            var res = await Http.Client.GetAsync(Http.UrlServer + "user/profile/" + GlobalUser.UserName);
            var resp = await res.Content.ReadAsStringAsync();
            JObject json = JObject.Parse(resp);
            if(json["data"]["avatar"] != null)
            {
                var a = json["data"]["avatar"].ToString();

                using (var client = new WebClient())
                {
                    client.DownloadFile(Http.UrlServer + "image/" + a, a);
                }
                GlobalUser.Avatar = System.Environment.CurrentDirectory + "/" + a;

            }
            else
            {
                GlobalUser.Avatar = System.Environment.CurrentDirectory.Replace("\\bin\\Debug","/Assets/profilePic.jpg");
            }
            using (var stream = File.OpenRead(GlobalUser.Avatar))
            {
                var image = new BitmapImage();
                image.BeginInit();
                image.CacheOption = BitmapCacheOption.OnLoad;
                image.StreamSource = stream;
                image.EndInit();
                this.Source = image;
            }


            this.TournamentStateEvent();
            this.RoundFinishedEvent();
            this.StateUpdateEvent();
            this.GameFinishedEvent();
        }
        public void TournamentStateEvent()
        {
            SocketService.MySocket.Off("tournament state");
            SocketService.MySocket.On("tournament state", (data) =>
            {
                Console.WriteLine("tournement state");
                Console.WriteLine(data);
                TournamentState_Model d = JsonConvert.DeserializeObject<TournamentState_Model>(data.ToString());
                this._homePageView.Dispatcher.Invoke(() =>
                {
                    this._homePageView.MiddleView.Children.Clear();
                    this._homePageView.MiddleView.Children.Add(new UserControl_WaitingView(d.state)); ;
                });
            });
        }
        public void RoundFinishedEvent()
        {
            SocketService.MySocket.Off("round finished");

            SocketService.MySocket.On("round finished", (data) =>
            {
                Console.WriteLine("round finished");
                Console.WriteLine(data);
                Winner d = JsonConvert.DeserializeObject<Winner>(data.ToString());
                if (d.winner)
                {
                    Thread t = new Thread(delegate ()
                    {
                        MessageBox.Show("Tour fini. Vous avez gagné " + GlobalUser.User.user.username + "!");
                    });
                    t.Start();
                   
                }
                else
                {
                    Thread t = new Thread(delegate ()
                    {
                        MessageBox.Show("Tour fini. Vous avez perdu" + GlobalUser.User.user.username + " :(");
                    });
                    t.Start();
                }
            });
        }
        public void StateUpdateEvent() 
        {
            var passiv = new UserControl_Passive();
            var write = new UserControl_Write();
            SocketService.MySocket.Off("state update");
            SocketService.MySocket.On("state update", (data) =>
            {
                Console.WriteLine(data);
                StateUpdate_Model d = JsonConvert.DeserializeObject<StateUpdate_Model>(data.ToString());

                if (d.role == "WRITE" && d.answer != null)
                {
                    this._homePageView.Dispatcher.Invoke(() =>
                    {
                        if (d.clear)
                        {
                            lastState = "WRITE";
                            this._homePageView.MiddleView.Children.Clear();
                            write = new UserControl_Write(d, this);
                            this._homePageView.MiddleView.Children.Add(write);
                        }
                    });
                }   

                else if (d.role == "GUESS" && d.answer == null)
                {
                    this._homePageView.Dispatcher.Invoke(() =>
                    {
                        if (d.clear)
                        {
                            lastState = "GUESS";
                            this._homePageView.MiddleView.Children.Clear();
                            guess = new UserControl_Guess(d, this);
                            this._homePageView.MiddleView.Children.Add(guess);


                        }
                        else
                        {
                            if(lastState == "PASSIVE")
                            {
                                this._homePageView.MiddleView.Children.Remove(passiv);
                                passiv.switchState(d);
                                this._homePageView.MiddleView.Children.Add(new UserControl_Guess(passiv));
                            }
                            else
                            {
                                if (d.attemptsLeft.ToString() == "-1")
                                {
                                    guess.PassivViewGuess.BarInfoPassive.Answer.Content = "Tentatives : " + "Infinies";
                                }
                                else
                                {
                                     guess.PassivViewGuess.BarInfoPassive.Answer.Content = "Tentatives : " + d.attemptsLeft.ToString();

                                }
                            }
                           
                        }
                    });
                }

                else if (d.role == "PASSIVE" && d.answer == null)
                {
                    this._homePageView.Dispatcher.Invoke(() =>
                    {
                        if (d.clear)
                        {
                            lastState = "PASSIVE";

                            this._homePageView.MiddleView.Children.Clear();
                            passiv = new UserControl_Passive(d, this);
                            this._homePageView.MiddleView.Children.Add(passiv);
                        }
                        else
                        {
                           if(lastState == "WRITE")
                            {
                                write.switchState(d);
                               
                            }
                            else if(lastState == "GUESS")
                            {
                                guess.SwitchToPassive(d);
                            }
                            else
                            {
                                passiv.BarInfoPassive.switchtoPassiv(d);
                            }
                        }

                    });
                }

            });
        }

        public void GameFinishedEvent()
        {
            SocketService.MySocket.Off("game finished");

            SocketService.MySocket.On("game finished", (data) =>
            {
             
                Winner d = JsonConvert.DeserializeObject<Winner>(data.ToString());
               

                if (d.winner)
                {
                   
                        MessageBox.Show("Jeu fini. Vous avez gagné "+ GlobalUser.User.user.username +" !");
                   
                }
                else
                {
                   
                        MessageBox.Show("Jeu fini. Vous avez perdu " + GlobalUser.User.user.username + " :(");
                   
  
                }
                _homePageView.Dispatcher.Invoke(() =>
                {
                    _homePageView.MiddleView.Children.Clear();
                    _homePageView.MiddleView.Children.Add(new UserControl_GameMode(this));
                });

            });
        }
        public void OpenGallery(object o)
        {
            //Gallery_Window creation = new Gallery_Window();
            //creation.ShowDialog();
        }
        public bool CanOpenGallery(object o)
        {
            return true;
        }
        public void CreatGameAssisted(object o)
        {
            GameCreationAssisted_Window creation = new GameCreationAssisted_Window();
            creation.ShowDialog();
        }

        public void CreateGame(object o)
        {
            GameCreation_Window test = new GameCreation_Window();
            test.ShowDialog();
        }

        public bool CanCreateGame(object o)
        {
            return true;
        }

        public void Log_out(object o)
        {
            loggedOut = true;
            SocketService.MySocket.Disconnect();

            _homePageView.SwitchToLogin();
        }
        public bool CanLogOut(object o)
        {
            return true;
        }

        public void Extract_Chat(object parameter)
        {
            ChatWindow _newChatWindow = new ChatWindow(this, _myChat);
        }

        public bool CanExtractChat(object parameter)
        {
            if(this.HomePageView.MyChat.Children.Count > 0)
            {
                return true;
            }
            return false;
        }

        public bool Can_Show_Profil(object o)
        {
            return true;
        }

        public bool Can_Show_update_profil(object o) {
            return true;
        }

        public void Show_user_profil(object o)
        {
            UserInfo_Window _newUserWindow = new UserInfo_Window();
            _newUserWindow.ShowDialog();
        }

        public void Show_update_user_profil(object o) {
            UpdateUserProfilWindow _newUpdateUserWindow = new UpdateUserProfilWindow(this);
            _newUpdateUserWindow.ShowDialog();
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
