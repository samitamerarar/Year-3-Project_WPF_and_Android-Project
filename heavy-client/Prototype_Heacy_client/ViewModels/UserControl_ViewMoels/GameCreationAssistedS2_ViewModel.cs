using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.Services;
using Prototype_Heacy_client.Views;
using Prototype_Heacy_client.Views.UserControls;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.IO;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Web;

namespace Prototype_Heacy_client.ViewModels.UserControl_ViewMoels
{

   
    public class GameCreationAssistedS2_ViewModel : INotifyPropertyChanged
    {
        /// <summary>
        /// Commands
        /// </summary>
        #region
       
        public GeneralCommands<object> CreateGame { get; set; }
        public GeneralCommands<object> Simulate { get; set; }
        #endregion

        public GameCreationAssistedS2_ViewModel(GameCreationAssistedS1_ViewModel stp1)
        {
            step1_Attribute = stp1;
          
            Hints = new List<string>();
            MyHints = new List<UserControl_Hint>();
            CreateGame = new GeneralCommands<object>(Create, CanCreate);
            Simulate = new GeneralCommands<object>(SimulateGame, CanSimulate);
            Error = "";
        }

        /// <summary>
        /// Attributes
        /// </summary>
        #region
      
        public string file = "";
        private string error = "";
        public string searchGoogle = "";
        private int opt1 = 0;
        private int opt2 = 0;
        private int opt3 = 0;
        private string id = "";
        public GameCreationAssistedS1_ViewModel step1_Attribute;
        public List<string> Hints;
        public List<UserControl_Hint> MyHints;
        #endregion




        /// <summary>
        /// INotify
        /// </summary>
        #region
        public event PropertyChangedEventHandler PropertyChanged;

        private void OnPropertyChanged(string propertyName)
        {
            PropertyChangedEventHandler handler = PropertyChanged;

            if (handler != null)
            {
                handler(this, new PropertyChangedEventArgs(propertyName));
            }
        }
        #endregion

        public byte[] ImageToByteArray(System.Drawing.Image imageIn)
        {
 
            using (var stream = new MemoryStream())
            {
                imageIn.Save(stream, System.Drawing.Imaging.ImageFormat.Png);
                return stream.ToArray();
            }
         
        }
        public async Task uploadImage(bool simulation)
        {
           
            byte[] data = File.ReadAllBytes(this.file);
            string base64String = Convert.ToBase64String(data);
            var content = JsonConvert.SerializeObject(new ImageToServer(base64String));
            StringContent stringContent = new StringContent(content, Encoding.UTF8, "application/json");
            var resp = await Http.Client.PostAsync(this.setQuerry(), stringContent);
            var responseString = await resp.Content.ReadAsStringAsync();
            if ((int)resp.StatusCode == 200)
            {
                this.id = (string)JObject.Parse(responseString)["id"];
                using (var client = new WebClient())
                {
                    client.DownloadFile(Http.UrlServer + "image/" + id, id);
                }
                if (!simulation)
                {
                    this.createGame(this.id);

                }
            }
            else
            {
                var popup = new GameCreationFeedback_window("Image saving error please try again", false);
                popup.ShowDialog();
            }


        }

        public string setQuerry()
        {
            string longurl = Http.UrlServer + "image/convert";
            var uriBuilder = new UriBuilder(longurl);
            var query = HttpUtility.ParseQueryString(uriBuilder.Query);
            query["potrace"] = JsonConvert.SerializeObject(new Portrace(opt1,opt2, option)); 
            uriBuilder.Query = query.ToString();
            longurl = uriBuilder.ToString();
            Console.WriteLine(longurl);
            return longurl;
        }

        /// <summary>
        /// Commands implementation
        /// </summary>
        /// <param name="o"></param>
        #region

        public async void SimulateGame(object o)
        {
            await this.uploadImage(true);
            var sim = new Simulation_Window(this.id, this.step1_Attribute.drawingMode);
            sim.ShowDialog();
        }

        public bool CanSimulate(object o)
        {
            if (string.IsNullOrWhiteSpace(this.file))
            {
                return false;
            }
            return true;
        }
        public async void Create(object o)
        {
            this.Hints = new List<string>();
            Error = "";
            foreach (var Hint in MyHints)
            {
                if (!string.IsNullOrWhiteSpace(Hint.HintContent.Text))
                {
                    this.Hints.Add(Hint.HintContent.Text);
                }
            }
            if (Hints.Count == 0)
            {
                Error = "Please enter at least one hint";
            }
            else
            {
                await this.uploadImage(false);
            }
        }
      


        public async void createGame(string id)
        {
            var gameToServer = JsonConvert.SerializeObject(new GameToServer(step1_Attribute.name, step1_Attribute.expression, Hints, id, step1_Attribute.difficulty, step1_Attribute.drawingMode));
            var responseGame = await Http.Client.PostAsync(Http.UrlServer + "game", new StringContent(gameToServer, Encoding.UTF8, "application/json"));
            var responseStringGame = await responseGame.Content.ReadAsStringAsync();
            dynamic respGame = JObject.Parse(responseStringGame);
            var popup = new GameCreationFeedback_window((string)respGame.msg, (string)respGame.msg == "Game created!");
            popup.ShowDialog();

        }

       
        public bool CanCreate(object o)
        {
            if (string.IsNullOrWhiteSpace(file))
            {
                return false;
            }
            return true;
        }

        public bool CanSetRadio(object o)
        {
            return true;
        }

        #endregion

        /// <summary>
        /// Getters Setters
        /// </summary>
        #region

        public int Opt1
        {
            get
            {
                return opt1;
            }
            set
            {
                opt1 = value;
                OnPropertyChanged("Opt1");
            }
        }
        private double option;
        public double Option { get { return option; } set { option = value; OnPropertyChanged("Option"); } }
        public int Opt2
        {
            get
            {
                return opt2;
            }
            set
            {
                opt2 = value;
                OnPropertyChanged("Opt2");
            }
        }

        public int Opt3
        {
            get
            {
                return opt3;
            }
            set
            {
                opt3 = value;
                option = ((double)opt3) / 10;
                OnPropertyChanged("Option");
                OnPropertyChanged("Opt3");
            }
        }


        public string Error
        {
            get
            {
                return error;
            }
            set
            {
                error = value;
                OnPropertyChanged("Error");
            }
        }

      
        #endregion
    }
}
