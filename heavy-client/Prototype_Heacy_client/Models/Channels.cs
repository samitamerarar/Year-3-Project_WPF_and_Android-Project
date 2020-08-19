using System.Collections.Generic;
using Prototype_Heacy_client.Interfaces;

namespace Prototype_Heacy_client.Models
{
    class Channels
    {
        private List<Channel> myChannels;
        public List<Channel> MyChannels
        {
            get { return myChannels; }
            set { myChannels = value; }
        }

        public Channels(List<Channel> channels)
        {
            myChannels = channels;
        }

    }
}
