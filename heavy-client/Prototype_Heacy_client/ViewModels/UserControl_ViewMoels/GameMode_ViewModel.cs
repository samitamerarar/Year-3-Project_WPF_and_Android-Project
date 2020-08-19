using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.Views;
using Prototype_Heacy_client.Views.UserControls;
using System;

namespace Prototype_Heacy_client.ViewModels.UserControl_ViewMoels
{
    public class GameMode_ViewModel
    {
        public HomePage_ViewModel _homePageViewModel;

        public GameMode_ViewModel(HomePage_ViewModel homePageViewModel)
        {
            this._homePageViewModel = homePageViewModel;
            ChooseGameMode = new GeneralCommands<object>(ShowModeDiff, CanShowModeDiff);
        }
        public GeneralCommands<object> ChooseGameMode
        {
            get;
            private set;
        }
        public void ShowModeDiff(object mode)
        {
            GlobalUser.Mode = (string)mode;
            new GameDifficultiesWindow(this._homePageViewModel).ShowDialog();

        }
        public bool CanShowModeDiff(object o)
        {
            return true;
        }
    }
}
