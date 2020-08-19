using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.Services;
using Prototype_Heacy_client.Views.UserControls;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;

namespace Prototype_Heacy_client.ViewModels.UserControl_ViewMoels
{
    public class Guess_ViewModel : INotifyPropertyChanged
    {
        public UserControl_Guess _guessView;
        private string _wordToGuess;

        public GeneralCommands<object> GuessCommand { get; set; }
        public GeneralCommands<object> ClueCommand { get; set; }
        public Guess_ViewModel(UserControl_Guess guessView)
        {
            GuessCommand = new GeneralCommands<object>(GuessDrawing, CanGuessDrawing);
            ClueCommand = new GeneralCommands<object>(GetClue, CanGetClue);
            // this._guessView.GuessWord.Text = "";

        }

        public void GetClue(object o)
        {
            SocketService.MySocket.Emit("clue");
        }

        public bool CanGetClue(object o)
        {
            return true;
        }

        public void GuessDrawing(object p)
        {
            SocketService.MySocket.Emit("guess", WordToGuess);
        }
        public bool CanGuessDrawing(object p)
        {
            return true;
        }

        public string WordToGuess
        {
            get { return _wordToGuess; }
            set
            {
                _wordToGuess = value;
                OnPropertyChanged("GuessWord");
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
