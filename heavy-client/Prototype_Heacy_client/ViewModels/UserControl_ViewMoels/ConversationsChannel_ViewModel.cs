using System.Collections.Generic;
using System.Threading.Tasks;
using Prototype_Heacy_client.Interfaces;
using Newtonsoft.Json;
using Prototype_Heacy_client.Views.UserControls;
using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Views;
using Prototype_Heacy_client.Services;

namespace Prototype_Heacy_client.ViewModels.UserControl_ViewMoels
{

    public class ConversationsChannel_ViewModel
    {
        public List<Channel> channels { get; set; }
        public UserControl_Conversations_Channel convChannelVM;
        public ConversationsChannel_ViewModel(UserControl_Conversations_Channel convChannelVM)
        {
            this.convChannelVM = convChannelVM;
            Serch = new GeneralCommands<object>(SerchChannel, CanSerch);
            Create = new GeneralCommands<object>(CreateChannel, canCreate);
            Init();
        }

        public bool CanSerch(object o)
        {
            return true;
        }

        public GeneralCommands<object> Create { get; set; }

        public void CreateChannel(object o)
        {
            ChannelCreationWindow popup = new ChannelCreationWindow(this);
            popup.ShowDialog();
        }

        public bool canCreate(object o)
        {
            return true;
        }

        public async void Init()
        {
           await GetChannels();
        }

        public GeneralCommands<object> Serch { get; set; }

        public void  SerchChannel(object o)
        {

            SerchChanel_Window popup = new SerchChanel_Window(this);
            popup.ShowDialog();
        }
        public async Task GetChannels()
        {
            var responses = await Http.Client.GetAsync(Http.UrlServer + "channels");
            var responseStrings = await responses.Content.ReadAsStringAsync();
            this.channels = new List<Channel>();
            channels.Add(new Channel("game", new List<Message>()));

            channels.AddRange(JsonConvert.DeserializeObject<List<Channel>>(responseStrings));
            
            DisplayChannels();

        }
        public void RefrechViews()
        {
            this.convChannelVM.RefreshView();
        }
        private void DisplayChannels()
        {
            channels.ForEach(delegate (Channel channel)
            {
                this.convChannelVM.addChannel(channel.name);
            });
        }
    }
}
