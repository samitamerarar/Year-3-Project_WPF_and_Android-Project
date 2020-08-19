using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.Services;
using Prototype_Heacy_client.Views;
using Prototype_Heacy_client.Views.UserControls;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

namespace Prototype_Heacy_client.ViewModels.UserControl_ViewMoels
{
    public class GameCreationManual_1_ViewModel : INotifyPropertyChanged
    {

        public GeneralCommands<object> Radio { get; set; }
        public GeneralCommands<object> RadioMode { get; set; }
        public List<UserControl_Hint> MyHints;
        public string idImage;
        public GeneralCommands<object> CreateGame { get; set; }


        private string difficulty = "EASY";
        public string drawingMode = "NORMAL";
        private string error = "";
        private string name = "";
        private string expression;

        public List<string> Hints;
        public GameCreation_Window GameWind;

        public string Name
        {
            get { return name; }
            set { name = value; OnPropertyChanged("Name"); }
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

        public string Expression
        {
            get
            {
                return expression;
            }
            set
            {
                expression = value;
                OnPropertyChanged("Expression");
            }
        }



        public GameCreationManual_1_ViewModel(GameCreation_Window gamwWind)
        {
            Radio = new GeneralCommands<object>(SetRadio, CanSetRadio);
            RadioMode = new GeneralCommands<object>(SetRadioMode, CanSetRadio);

            Hints = new List<string>();
            MyHints = new List<UserControl_Hint>();
            CreateGame = new GeneralCommands<object>(Create, CanCreate);
            this.GameWind = gamwWind;
            Error = "";
        }


        public async void Create(object o)
        {

            Error = "";
            foreach(var Hint in MyHints)
            {
                if (!string.IsNullOrWhiteSpace(Hint.HintContent.Text))
                {
                    this.Hints.Add(Hint.HintContent.Text);
                }
            }
            if(Hints.Count == 0)
            {
                Error = "Please enter at least ine hint";
            }
            else
            {
                await this.createGame(false);
             
            }
        }

        public async Task createGame(bool simulation)
        {
            this.GameWind.BuildDrowingPoints();
            await this.CreateDrowingPointToServer(simulation);
        }


        public async Task CreateDrowingPointToServer(bool simulation)
        {
            ArrayList segmts = new ArrayList();
            foreach(ArrayList elem in this.GameWind.drawingPoint)
            {
                string pn = (string)elem[0];
                double h = (double)elem[1];
                double w = (double)elem[2];
                string c = (string)elem[3];
                elem.RemoveRange(0, 4);
                Segment seg = new Segment(pn, h, w,c, elem);
                segmts.Add(seg);
            }
            await RequestPostImageData(segmts, simulation);

        }

        public async Task RequestPostImageData(ArrayList a, bool simulation)
        {
            var pointToServer = JsonConvert.SerializeObject(new DrowPoints(a));
            var response = await Http.Client.PostAsync(Http.UrlServer + "image/data", new StringContent(pointToServer, Encoding.UTF8, "application/json"));
            var responseString = await response.Content.ReadAsStringAsync();
          
            if ((int)response.StatusCode == 200)
            {
                string id = (string)JObject.Parse(responseString)["id"];
                this.idImage = id;
                if(!simulation)
                    this.createGame(id);
            }
            else
            {
                var popup = new GameCreationFeedback_window("Image saving error please try again", false);
                popup.ShowDialog();
            }
            
        }

        public async void createGame(string id)
        {
            var gameToServer = JsonConvert.SerializeObject(new GameToServer(name, expression, Hints, id, difficulty, drawingMode));
            var responseGame = await Http.Client.PostAsync(Http.UrlServer + "game", new StringContent(gameToServer, Encoding.UTF8, "application/json"));
            var responseStringGame = await responseGame.Content.ReadAsStringAsync();
            dynamic respGame = JObject.Parse(responseStringGame);
           
            var popup = new GameCreationFeedback_window((string)respGame.msg, (string)respGame.msg == "Game created!");
            popup.ShowDialog();
          
        }
        public void SetRadio(object o)
        {

            difficulty = (string)o;
            if (difficulty == "DIFFICILE")
            {
                difficulty = "DIFFICULT";
                return;
            }
            if (difficulty == "INTERMÉDIAIRE")
            {
                difficulty = "INTERMEDIATE";
                return;
            }
            if (difficulty == "FACILE")
            {
                difficulty = "EASY";
                return;
            }

        }
        public void SetRadioMode(object o)
        {
           
            drawingMode = (string)o;
            if(drawingMode == "PANORAMIQUE")
            {
                PanoramicSelection_Window a = new PanoramicSelection_Window(this);
                a.ShowDialog();
                return;
            }
            if(drawingMode == "CENTRÉ")
            {
                CentredSelection_Window a = new CentredSelection_Window(this);
                a.ShowDialog();
                return;
            }
            if(drawingMode == "ALÉATOIRE")
            {
                drawingMode = "RANDOM";
            }
           
        }

        public bool CanCreate(object o)
        {
            if (string.IsNullOrWhiteSpace(name) || string.IsNullOrWhiteSpace(expression) || this.GameWind.draw_ViewModel.Traits.Count == 0)
            {
                return false;
            }
            return true;
        }

        public bool CanSetRadio(object o)
        {
            return true;
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
