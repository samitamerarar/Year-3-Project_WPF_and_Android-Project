using GalaSoft.MvvmLight.Command;
using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using Prototype_Heacy_client.Views;
using Prototype_Heacy_client.Views.UserControls;
using System;
using System.ComponentModel;
using System.Windows;
using System.Windows.Media.Animation;

namespace Prototype_Heacy_client.ViewModels
{
    public class GameDifficulties_ViewModel
    {
        public GameDifficultiesWindow _gameDiffWindow;
        public HomePage_ViewModel _homePageViewModel;

        public RelayCommand<CancelEventArgs> _onClosingWindow;
        public DoubleAnimation anim;

        public GameDifficulties_ViewModel(GameDifficultiesWindow gameDiffWindow, HomePage_ViewModel homePageViewModel)
        {
            this._gameDiffWindow = gameDiffWindow;
            this._homePageViewModel = homePageViewModel;

            CloseWindowCommand = new GeneralCommands<object>(CloseWindow, CanCloseWindow);
            _onClosingWindow = new RelayCommand<CancelEventArgs>(OnClosing_Window);

            ChooseLevelCommand = new GeneralCommands<object>(ChooseLevel, CanChooseLevel);

        }
        public GeneralCommands<object> ChooseLevelCommand
        {
            get;
            set;
        }

        public void ChooseLevel(object level)
        {
            this.GoToGamesRoom((string)level);
        }

        public bool CanChooseLevel(object o)
        {
            return true;
        }

        public void GoToGamesRoom(string level)
        {
            this._gameDiffWindow.Close();
            GlobalUser.Difficulty = level;

            this._homePageViewModel._homePageView.MiddleView.Children.Clear();
            this._homePageViewModel._homePageView.MiddleView.Children.Add(new UserControl_GamesRoom(this._homePageViewModel));
        }
        public GeneralCommands<object>CloseWindowCommand
        {
            get;
            set;
        }

        public RelayCommand<CancelEventArgs> OnClosingWindow
        {
            get
            {
                return _onClosingWindow;
            }
        }

        public void OnClosing_Window(CancelEventArgs e)
        {
            e.Cancel = true;
            anim = new DoubleAnimation(0, (Duration)TimeSpan.FromSeconds(0.5));
            anim.Completed += (s, _) => this._gameDiffWindow.Close();
            this._gameDiffWindow.BeginAnimation(UIElement.OpacityProperty, anim);

        }
        public bool CanCloseWindow(object o)
        {
            return true;
        }
        public void CloseWindow(object o)
        {
            this._gameDiffWindow.Close();
            //this._homePageViewModel.HomePageView.IsEnabled = true;

        }

    }
}
