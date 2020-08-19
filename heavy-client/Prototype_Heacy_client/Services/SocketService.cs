using Prototype_Heacy_client.Interfaces;
using Quobject.SocketIoClientDotNet.Client;
using System;
using System.Web;

namespace Prototype_Heacy_client.Services
{
    public class SocketService
    {
        public static Socket MySocket
        {
            get
            {
                if ( socket == null)
                {
                    socket = IO.Socket(Http.UrlServer);
                }
                return socket;

            }
          
        }
 
        private static Socket socket = null;

        public static void ResetSocket()
        {
            socket = null;
        }

        public static string setQuerry()
        {
            string longurl = Http.UrlServer;
            var uriBuilder = new UriBuilder(longurl);
            var query = HttpUtility.ParseQueryString(uriBuilder.Query);
            query["username"] = GlobalUser.User.user.username;
            uriBuilder.Query = query.ToString();
            longurl = uriBuilder.ToString();
            Console.WriteLine(longurl);
            return longurl;
        }


    }
}
   