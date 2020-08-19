
using System.Net.Http;


namespace Prototype_Heacy_client.Services
{
    public class Http
    {
        public static HttpClient Client {
            get
            {
                if (client == null)
                {
                    client = new HttpClient();
                }
                return client;
            }
        }

        private static  HttpClient client = null;

        public static string UrlServer = "https://projet-03-equipe-06.herokuapp.com/"; //10.200.12.71 syphax 10.200.13.83 amine
    }
}
