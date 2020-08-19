using System;
using System.ComponentModel;
using System.Windows.Media.Imaging;

namespace Prototype_Heacy_client.Interfaces
{
    public class GlobalUser 
    {
        private static User _user;

        public static User User
        {
            get { return _user; }
            set { _user = value; }
        }

        // Group Info mode - difficulty
        private static string mode;
        private static string difficulty;

        public static string Mode
        {
            get { return mode; }
            set { mode = value; }
        }
        
        public static string Difficulty
        {
            get { return difficulty; }
            set { difficulty = value; }
        }

        private static bool gameIsActive;
        public static bool GameIsActive
        {
            get { return true; }
            set { gameIsActive = value;}
        }

        private static string outilSelectionnee = "crayon";
        public static string OutilSelectionnee
        {
            get { return outilSelectionnee; }
            set { outilSelectionnee = value; }
        }

        private static string _username;
        private static string _password;

        public static String UserName
        {
            get { return _username; }
            set { _username = value; }
        }

        public static String PassWord
        {
            get { return _password; }
            set { _password = value; }
        }

        private static string _firstName;
        private static string _lastName;
        private static string _avatar = "";
        public static String FirstName
        {
            get { return _firstName; }
            set { _firstName = value; }
        }

        

        public static String LastName
        {
            get { return _lastName; }
            set { _lastName = value; }
        }

        public static String Avatar
        {
            get { return _avatar; }
            set { _avatar = value; }
        }

       

    }
}
