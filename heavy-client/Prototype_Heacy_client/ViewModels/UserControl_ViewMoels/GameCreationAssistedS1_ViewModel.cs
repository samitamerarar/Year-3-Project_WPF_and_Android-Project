using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Views;
using System.ComponentModel;

namespace Prototype_Heacy_client.ViewModels.UserControl_ViewMoels
{
    public class GameCreationAssistedS1_ViewModel : INotifyPropertyChanged
    {
        /// <summary>
        /// attributes
        /// </summary>
        #region
        public string name;
        public string imageOption;
        public string difficulty = "EASY";
        public string drawingMode = "RANDOM";

        public string expression;

        public GameCreationAssisted_Window main;
        #endregion

        /// <summary>
        /// Commands
        /// </summary>
        #region
        public GeneralCommands<object> Radio1 { get; set; }
        public GeneralCommands<object> RadioMode { get; set; }
        public GeneralCommands<object> Radio { get; set; }
        public GeneralCommands<object> Next { get; set; }
        #endregion


        public GameCreationAssistedS1_ViewModel(GameCreationAssisted_Window main)
        {
          
            name = "";
            imageOption = "Recherche sur Google";
            Radio1 = new GeneralCommands<object>(SetRadio1, CanSetRadio);
            RadioMode = new GeneralCommands<object>(SetRadioMode, CanSetRadio);
            Radio = new GeneralCommands<object>(SetRadio, CanSetRadio);
            Next = new GeneralCommands<object>(NextStep, CanGoNext);
            this.main = main;
        }

        /// <summary>
        /// Commands implementation
        /// </summary>
        /// <param name="o"></param>
        #region
        public void SetRadio(object o)
        {
            imageOption = (string)o;
        }
        public void SetRadio1(object o)
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
            if (drawingMode == "PANORAMIQUE")
            {
                PanoramicSelection_Window a = new PanoramicSelection_Window(this);
                a.ShowDialog();
                return;
            }
            if (drawingMode == "CENTRÉ")
            {
                CentredSelection_Window a = new CentredSelection_Window(this);
                a.ShowDialog();
                return;
            }
            if (drawingMode == "ALÉATOIRE")
            {
                drawingMode = "RANDOM";
            }
        }
        public bool CanSetRadio(object o)
        {
            return true;
        }


        public void NextStep(object o)
        {
            this.main.createNextStep(this);
        }

        public bool CanGoNext(object o)
        {
            if (!(string.IsNullOrWhiteSpace(name) || (string.IsNullOrWhiteSpace(expression))))
                return true;
            return false;
        }
        #endregion

        /// <summary>
        /// getters setters
        /// </summary>
        #region


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
      
        public string Name
        {
            get
            {
                return name;
            }
            set
            {
                name = value;
                OnPropertyChanged("Name");

            }

        }
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
    }
}
