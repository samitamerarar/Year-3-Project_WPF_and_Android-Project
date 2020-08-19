using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.Services;
using Prototype_Heacy_client.Views.UserControls;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Prototype_Heacy_client.ViewModels.UserControl_ViewMoels
{
    public class TemplateView_ViewModel <T>
    {
        private readonly string GLOBAL_USER = GlobalUser.User.user.username;

        private HomePage_ViewModel _homePageViewModel;
        private T _view;
        private Group_Model _group;

        public GeneralCommands<object> ReturnToGroupsList { get; set; }
        public GeneralCommands<object> ReadyToStart { get; set; }
        public GeneralCommands<object> AddPlayerCommand { get; set; }

        public TemplateView_ViewModel(T view, HomePage_ViewModel homePageViewModel, Group_Model group)
        {
            this._view = view;
            this._homePageViewModel = homePageViewModel;
            this._group = group;

            BasicMode basic = new BasicMode(homePageViewModel, group);
            ReturnToGroupsList = new GeneralCommands<object>(basic.RetunToGroups_List, basic.CanRetunToGroups_List);
            ReadyToStart = new GeneralCommands<object>(basic.ReadyTo_Start, basic.CanBeReadyTo_Start);
            AddPlayerCommand = new GeneralCommands<object>(basic.AddPlayer, basic.CanAddPlayer);

            SocketService.MySocket.On("list groups", (data) =>
            {
                JArray jsonArray = JArray.Parse(data.ToString());
                foreach (object i in jsonArray)
                {
                    dynamic p = JObject.Parse(i.ToString());
                    if (p.mode == "COOP")
                    {
                        this._homePageViewModel.HomePageView.Dispatcher.Invoke(() =>
                        {
                            //BasicMode basicMode = new BasicMode(view, p, this._group);
                            //this.CheckGameCoopState(p);
                        });
                    }

                    if (p.mode == "SOLO")
                    {
                        this._homePageViewModel.HomePageView.Dispatcher.Invoke(() =>
                        {
                            //BasicMode basicMode = new BasicMode(view, p, this._group);
                            //this.CheckGameSoloState(p);
                        });
                    }
                }
            });

        }



    }
}

