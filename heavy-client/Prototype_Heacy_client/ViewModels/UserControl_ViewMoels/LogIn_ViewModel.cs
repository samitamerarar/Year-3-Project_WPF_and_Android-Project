using System.Text;
using System.Net.Http;
using Newtonsoft.Json;
using System.ComponentModel;
using Newtonsoft.Json.Linq;
using System.Text.RegularExpressions;
using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Models;
using Prototype_Heacy_client.Views.UserControls;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.Services;
using System.Windows.Media.Animation;
using System.Linq;

namespace Prototype_Heacy_client.ViewModels.UserControl_ViewMoels
{
 

   public  class LogIn_ViewModel : INotifyPropertyChanged

    {
        private User_Model user;
        public UserControl_LogIn logInView;



        public LogIn_ViewModel(UserControl_LogIn logInView)
        {
            this.logInView = logInView;
            LogIn = new GeneralCommands<object>(LogInOrRegister, CanLogIn);
            user = new User_Model("", "");
        }
       


        public User_Model User
        {
            get
            {
                return user;
            }
            set
            {
                user = value;
            }
        }

        public GeneralCommands<object>LogIn
        {
            get;
            private set;
        }


        public bool CanLogIn(object o)
        {
            return true;
        }


        public async void LogInOrRegister(object o)
        {
            bool UserNameStatus = VerifyUserNameStatus();

            if (UserNameStatus)
            {
                var content = JsonConvert.SerializeObject( new UserToServer(user.Username, this.logInView.Password.Password));
                var response = await Http.Client.PostAsync(Http.UrlServer+"auth", new StringContent(content, Encoding.UTF8, "application/json"));
                var responseString = await response.Content.ReadAsStringAsync();
                User userFromServer = JsonConvert.DeserializeObject<User>(responseString);


                if (((int)response.StatusCode) != 200)
                {
                    user.Error = "Veuillez vérifier vos informations";
                    this.ShakeText();

                }
                else
                {
                    user.Error = "";
                    GlobalUser.User = userFromServer;
                    GlobalUser.PassWord = this.logInView.Password.Password;
                    GlobalUser.UserName = User.Username;
                    SocketService.MySocket.Emit("username", User.Username);
                    JObject json = JObject.Parse(responseString);
                    if (userFromServer.msg == "Account Created!")
                    {
                        logInView.SwitchTuto();

                    }
                    else
                    {
                        logInView.SwitchToHomePage(userFromServer);
                    }
                    
                }
            }
        }
        public void ShakeText()
        {
            Storyboard myStoryboard = (Storyboard)this.logInView.ErrorText.Resources["TestStoryboard"];
            Storyboard.SetTarget(myStoryboard.Children.ElementAt(0) as DoubleAnimationUsingKeyFrames, this.logInView.ErrorText);
            myStoryboard.Begin();
        }
        public bool VerifyUserNameStatus()
        {
            if (!string.IsNullOrWhiteSpace(user.Username))
            {
                User.Error = "";
                return VerifyPassWordStatus();
            }
            else
            {
                User.Error = "Le pseudonyme est requis";
                this.ShakeText();
                return false;
            }
        }

        public bool VerifyPassWordStatus()
        {
            /*Requires 1 upper letter, 1 lower, 1 number, and more then 4 characters*/
            Regex regex = new Regex(@"^(?=.*[A-Z])(?=.{4,15})(?=.*[0-9])[a-zA-Z0-9]+$");
            if (regex.Match(this.logInView.Password.Password).Success)
            {
                User.Error = "";
                return true;
            }
            else
            {
                User.Error = "Le mot de passe doit avoir au minimum 4 caractères et contenir\r\n" +
                              "    au moins 1 lettre majuscule, 1 lettre minuscule et 1 chiffre";
                this.ShakeText();
                return false;
                
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


