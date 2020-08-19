using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.Models;
using Prototype_Heacy_client.Services;
using Prototype_Heacy_client.Views.UserControls;
using System;

namespace Prototype_Heacy_client.ViewModels.UserControl_ViewMoels
{
    public class Group_ViewModel
    {
        private HomePage_ViewModel _homePageViewModel;
        private Group_Model _group;

        public GeneralCommands<object> EnterGroup
        {
            get;
            set;
        }
        public Group_ViewModel(HomePage_ViewModel homePageViewModel, Group_Model group)
        {
            this._homePageViewModel = homePageViewModel;
            this._group = group;
            EnterGroup = new GeneralCommands<object>(Enter_Group, CanEnter_Group);
        }

        public void Enter_Group(object o)
        {

            if (this._group.createGroup.mode == "CLASSIC")
            {
                this._homePageViewModel._homePageView.MiddleView.Children.Clear();
                this._homePageViewModel._homePageView.MiddleView.Children.Add(new UserControl_Classic(this._group, this._homePageViewModel));
            }
            else if(this._group.createGroup.mode == "SOLO")
            {
                this._homePageViewModel._homePageView.MiddleView.Children.Clear();
                this._homePageViewModel._homePageView.MiddleView.Children.Add(new UserControl_Solo(this._group, this._homePageViewModel));
            }
            else if(this._group.createGroup.mode == "COOP")
            {
                this._homePageViewModel._homePageView.MiddleView.Children.Clear();
                this._homePageViewModel._homePageView.MiddleView.Children.Add(new UserControl_Coop(this._group, this._homePageViewModel));
            }
            else if (this._group.createGroup.mode == "TOURNAMENT")
            {
                this._homePageViewModel._homePageView.MiddleView.Children.Clear();
                this._homePageViewModel._homePageView.MiddleView.Children.Add(new UserControl_Tournament(this._group, this._homePageViewModel));
            }
        }

        public bool CanEnter_Group(object o)
        {
            return true;
        }
    }
}
