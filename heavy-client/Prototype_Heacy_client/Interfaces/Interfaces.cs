using System;
using System.Collections;
using System.Collections.Generic;
using System.Drawing;

namespace Prototype_Heacy_client.Interfaces
{

    public class IQUerySocket
    {
        public string query;
        public IQUerySocket(string username)
        {
            query = "username=$" + username;
        }
    }


    public class Message
    {
        public string author;
        public string content;
        public string channel;
        public string created;
        public Message(string author, string content, string channel, string created)
        {
            this.author = author;
            this.content = content;
            this.channel = channel;
            this.created = created;

        }

    }

    public class IStartVirtualPlayer
    {
        public string image;
        public int time;
        public string drawMode;

        public IStartVirtualPlayer(string i, string d)
        {
            image = i;
            time = 10000;
            drawMode = d;
        }
    }

    public class ISvgCommand
    {
        public string type;
        public string command;
        public List<double> args;
        public ISvgCommand(string type, string command, List<double> args)
        {
            this.type = type;
            this.command = command;
            this.args = args;
        }
    }
    public class Portrace
    {
        public int turdSize;
        public int alphaMax;
        public double optTolerance;
        public Portrace( int t, int a, double o)
        {
            turdSize = t;
            alphaMax = a;
            optTolerance = o;
        }
    }

    public class ImageToServer
    {
        public string data;
        public ImageToServer(string s)
        {
            data = s;
        }
    }
  
    
    public class Segment
    {
        public string pointNature { get; set; }
        public double height { get; set; }
        public double width { get; set; }
        public string color { get; set; }
        public List<ImagePoint> points { get; set; }

        public Segment(string pn, double h, double w,string c, ArrayList pts)
        {
            pointNature = pn;
            height = h;
            width = w;
            points = new List<ImagePoint>();
            this.color = c;
            foreach(var a in pts)
            {
                string[] p = a.ToString().Split(',');
               
                points.Add(new ImagePoint(double.Parse(p[0]), double.Parse(p[1])));
            }
        }

    }

    public class DrowPoints
    {
        public ArrayList segments { get; set; }
        public DrowPoints(ArrayList seg)
        {
            segments = seg;
        }
    }
    public class UserToServer
    {
        public string username;
        public string password;
        public UserToServer(string username, string password)
        {
            this.username = username;
            this.password = password;
        }

    }

    public class GameToServer
    {
        public string name;
        public string answer;
        public List<string> clues;
        public string image;
        public string difficulty;
        public string drawingMode;

        public GameToServer(string n, string a, List<string> c, string i, string di , string dr)
        {
            this.name = n;
            this.answer = a;
            this.clues = c;
            this.image = i;
            this.difficulty = di;
            this.drawingMode = dr;
        }
    }
    

    public class Channel
    {
        public string name;
        public List<Message> chat;

        public Channel(string name, List<Message> chat)
        {
            this.name = name;
            this.chat = chat;
        }
    }

    public class UserComponent {
        public string username;
        public string password;
        public List<string> channels;
        public string created;
        public UserComponent(string username, string password, List<string> channels, string created)
        {
            this.username = username;
            this.password = password;
            this.channels = channels;
            this.created = created;
        }
    }
    public class User
    {

        public string msg;
        public UserComponent user;
        
        public User(string msg, UserComponent user )
        {
            this.user = user;
            this.msg = msg;
          
        }
    }

    public class JoinObject
    {
        public string username;
        public string names;
        public JoinObject(string username, string names)
        {
            this.username = username;
            this.names = names;
        }
    }
    public class EnterObject
    {
        public string username;
        public string channel;
        public EnterObject(string username, string channel)
        {
            this.username = username;
            this.channel = channel;
        }
    }

    public class CreationChannel
    {
        public string name;
        public CreationChannel(string name)
        {
            this.name = name;
        }
    }

    public class CreationFeedBack
    {
        public string msg;
        public CreationFeedBack(string msg)
        {
            this.msg = msg;
        }
    }

    public class UpdateUserProfilToServer
    {
        public string firstName;
        public string lastName;
        public string avatar;

        public UpdateUserProfilToServer(string FirstName, string LastName, string Avatar)
        {
            this.firstName = FirstName;
            this.lastName = LastName;
            this.avatar = Avatar;
        }
    }

    public class History {
        public string login;
        public string logout;

        public History(string login, string logout) {
            this.login = login;
            this.logout = logout;
        }
    }

    public class ResultClassic {
        public string[] w = null;
        public string[] l= null;

        public ResultClassic(string[] win, string[] loss) {
            this.w = win;
            this.l = loss;
        }
    }

    public class GameHistory
    {
        public float start;
        public float end;
        public float duration;
        public string[] participants = null;
        public string mode;
        public ResultClassic resultClassic;
        public float resultCoop;
        public string localstart;
        public string localend;

        public GameHistory(float start, float end, float duration, string[] participants, string mode,
                           ResultClassic resultClassic, float resultCoop, string localstart, string localend)
        {
            this.start = start;
            this.end = end;
            this.duration = duration;
            this.participants = participants;
            this.mode = mode;
            this.resultClassic = resultClassic;
            this.resultCoop = resultCoop;
            this.localstart = localstart;
            this.localend = localend;
        }
    }

    public class Statistics : UpdateUserProfilToServer
    {
        public string username;
        public float matchsPlayed;
        public float victoryPercent;
        public float averageDuration;
        public float totalMatchDuration;
        public float soloHighestScore;
        public float coopHighestScore;
        public History[] history;
        public GameHistory[] gameHistory;
        public float tournamentsWon;
        public Statistics(string firstname, string lastname, string avatar ,string username, float matchsplayed, float averageMatchDuration,
            float totalMatchDuration, float soloHighestScore, float coopHighestScore, History[] history, GameHistory[] gameHistory, float tournamentsWon): base(firstname, lastname,avatar)
        {
            this.username = username;
            this.matchsPlayed = matchsplayed;
            this.victoryPercent = 0 ;
            this.averageDuration = averageMatchDuration;
            this.totalMatchDuration = totalMatchDuration;
            this.soloHighestScore = soloHighestScore;
            this.coopHighestScore = coopHighestScore;
            this.history = history;
            this.gameHistory = gameHistory;
            this.tournamentsWon = tournamentsWon;
        }
    }

}
