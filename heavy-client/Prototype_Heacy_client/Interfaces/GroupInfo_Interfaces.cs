using System;
using System.Collections;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Prototype_Heacy_client.Interfaces
{
    public class ModeDifficulty_Server
    {
        public string mode;
        public string difficulty;
        public ModeDifficulty_Server(string mode, string difficulty)
        {
            this.mode = mode;
            this.difficulty = difficulty;
        }
    }
    public class CreateGroup_Server
    {
        public string name;
        public string difficulty;
        public string mode;
        public CreateGroup_Server(string name, string difficulty, string mode)
        {
            this.name = name;
            this.difficulty = difficulty;
            this.mode = mode;
        }
    }

    public class ReadyPlayer_Server
    {
        public string gid;
        public string pos;

        public ReadyPlayer_Server(string gid, string pos)
        {
            this.gid = gid;
            this.pos = pos;
        }
    }

    public class AddPlayer_Server
    {
        public string gid;
        public string pos;
        public string type;
        public string username;
        public string avatar;

        public AddPlayer_Server(string gid, string pos, string type, string username, string avatar)
        {
            this.gid = gid;
            this.pos = pos;
            this.type = type;
            this.username = username;
            this.avatar = avatar;
        }
    }

    public class Player_Model
    {
        public string username;
        public string avatar;
        public string type;
        public string role;
        public string position;
        public bool ready;

        public Player_Model(string username, string type, string role, string position, bool ready)
        {
            this.username = username;
            this.type = type;
            this.role = role;
            this.position = position;
            this.ready = ready;
        }
        public Player_Model(string username, string avatar, string type, string role, string position, bool ready)
        {
            this.username = username;
            this.avatar = avatar;
            this.type = type;
            this.role = role;
            this.position = position;
            this.ready = ready;
        }
    }

    public class UserTournment
    {
        public string username;
        public string avatar;
        public UserTournment(string username, string avatar)
        {
            this.username = username;
            this.avatar = avatar;
        }
    }

    public class GroupTournament_Model
    {
        public string id;
        public CreateGroup_Server createGroup;
        public List<Player_Model> players;
        public List<string> ready;


        public GroupTournament_Model(string id, CreateGroup_Server createGroup, List<Player_Model> players, List<string> ready)
        {
            this.id = id;
            this.createGroup = createGroup;
            this.players = players;
            this.ready = ready;
        }
    }

    public class Group_Model
    {
        public string gid;
        public CreateGroup_Server createGroup;
        public List<Player_Model> listPlayers;
        public List<ArrayList> ready;

        public Group_Model(string gid, CreateGroup_Server createGroup, List<Player_Model> listPlayers)
        {
            this.gid = gid;
            this.createGroup = createGroup;
            this.listPlayers = listPlayers;
        }
        public Group_Model(string gid, CreateGroup_Server createGroup, List<Player_Model> listPlayers, List<ArrayList> ready)
        {
            this.gid = gid;
            this.createGroup = createGroup;
            this.listPlayers = listPlayers;
            this.ready = ready;
        }
    }

    public class Tournament_Model
    {
        public string id;
        public string name;
        public string difficulty;
        public string mode;
        public List<Player_Model> players;
        public List<ArrayList> ready;

        public Tournament_Model(string id, string name, string difficulty, string mode, List<Player_Model> players, List<ArrayList> ready)
        {
            this.id = id;
            this.name = name;
            this.difficulty = difficulty;
            this.mode = mode;
            this.players = players;
            this.ready = ready;
        }
    }
    public class TournState
    {
        public string username;
        public string avatar;
        public string status;
        public TournState(string username, string avatar, string status)
        {
            this.username = username;
            this.avatar = avatar;
            this.status = status;
        }
    }
    public class TournamentState_Model
    {
        public List<List<TournState>> state;
        public int startTimer;
        public TournamentState_Model(List<List<TournState>> state, int startTimer)
        {
            this.state = state;
            this.startTimer = startTimer;
        }

    }

    public class StateUpdate_Model
    {
        public string id;
        public string role;
        public int points;
        public int otherPoints;
        public string answer;
        public bool clear;
        public int currentRound;
        public bool counter;
        public int startTimer;
        public int attemptsLeft;

        public StateUpdate_Model(string role, int points, int otherPoints, string answer, string id, bool clear, int currentRound, bool counter, int startTimer, int attemptsLeft)
        {
            this.id = id;
            this.role = role;
            this.points = points;
            this.otherPoints = otherPoints;
            this.answer = answer;
            this.clear = clear;
            this.currentRound = currentRound;
            this.counter = counter;
            this.startTimer = startTimer;
            this.attemptsLeft = attemptsLeft;
        }
    }


    public class ImagePoint
    {
        public float x;
        public float y;
        public ImagePoint(double x, double y)
        {
            this.x = (float)x;
            this.y = (float)y;
        }
    }

    public class PointParams
    {
        public string pointNature;
        public string color;
        public double width;
        public double height;
        public PointParams(string pointNature, string color, double width, double height)
        {
            this.pointNature = pointNature.ToLower();
            this.color = color;
            this.width = width;
            this.height = height;
        }
    }

    public class DrawPoint_Server
    {
        public string type;
        public string action;
        public int id;
        public PointParams pointParams;
        public ImagePoint point;
        public DrawPoint_Server(string type, string action, int id, PointParams pointParams, ImagePoint point)
        {
            this.type = type;
            this.action = action;
            this.id = id;
            this.pointParams = pointParams;
            this.point = point;
        }
    }

    public class IDrawPoint
    {
        public string type;
        public string action;
        public int id;
        public PointParams pointParams;
        public ImagePoint pointServer = null;
        public IDrawPoint(string type, string action, int id, PointParams pointParams, ImagePoint point)
        {
            this.type = type;
            this.action = action;
            this.id = id;
            this.pointParams = pointParams;
            this.pointServer = point;

        }


    }

    public class PointServer
    {
        public double x;
        public double y;
        public PointServer(string p)
        {
            string[] coords = p.Split(',');
            x = double.Parse(coords[0]);
            y = double.Parse(coords[1]);

        }

    }

    public class Draw_Server
    {
        public string type;
        public DrawPoint_Server draw;

        //public class ListImageSegment_Server
        //{
        //    public List<ImageSegment_Server> list = new List<ImageSegment_Server>();
        //    public ListImageSegment_Server(List<ImageSegment_Server> list)
        //    {
        //        this.list = list;
        //    }
        //}
    }

    public class Winner
    {
        public bool winner;
        public int score;
        public Winner(bool winner, int score)
        {
            this.winner = winner;
            this.score= score;
        }
    }

}
