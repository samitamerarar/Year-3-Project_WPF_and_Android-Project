using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Prototype_Heacy_client.Interfaces;
using System.Collections.Generic;
using System.Windows.Controls;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Shapes;

namespace Prototype_Heacy_client.Services
{
    public class VirtualPlayer
    {
        private InkCanvas inkCAnvas;
        private Path path;
        private Dictionary<int, int> indexes;
        private string pathdata = "";


        public VirtualPlayer(InkCanvas a, Path p)
        {
            inkCAnvas = a;
            this.path = p;
            SocketService.MySocket.Off("draw");
            indexes = new Dictionary<int, int>();
            pathdata = "";
            this.DrawGame();
        }

        private void DrawGame()
        {
            indexes = new Dictionary<int, int>();
            int lastLine = 0;
            List<StylusPoint> list = new List<StylusPoint>();
           
            bool firstPoint = true;

            SocketService.MySocket.On("draw", (data) =>
            {
                dynamic d = JArray.Parse(data.ToString());
                if(d[0]["type"] == "DRAW")
                {
                    lastLine = this.Draw(data.ToString(), list, lastLine, firstPoint );
                    firstPoint = false;
                }
                else
                {
                    pathdata += this.DrawSvg(data.ToString());
                    Geometry a = Geometry.Parse(pathdata);
                    this.path.Dispatcher.Invoke(() =>
                    {
                        this.path.Data = a;
                    });

                }
            });
        }

        private string DrawSvg(string data)
        {
           string path = "";
         
           List<ISvgCommand> svg = JsonConvert.DeserializeObject<List<ISvgCommand>>(data);
           foreach(ISvgCommand c in svg)
            {
                path +=c.command + " ";
                foreach (var elem in c.args)
                {
                    path += elem + " ";
                }
            }

            return path;
        }
        private int Draw(string data, List<StylusPoint>list, int lastLine, bool firstPoint)
        {
           
            List<IDrawPoint> pointServer = JsonConvert.DeserializeObject<List<IDrawPoint>>(data);
            foreach(IDrawPoint p in pointServer)
            {
                if(p.action == "ADD")
                {
                    lastLine = ADD(list, lastLine, firstPoint, p);
                    firstPoint = false;
                }
                else
                {
                    this.inkCAnvas.Dispatcher.Invoke(() =>
                    {
                        var listt = new List<StylusPoint>();
                        listt.Add(new StylusPoint(10000, 10000));
                        this.inkCAnvas.Strokes[p.id] = new Stroke(new StylusPointCollection(listt));
                        this.inkCAnvas.Strokes[p.id].DrawingAttributes.Color = System.Windows.Media.Color.FromArgb(255,255,255,255);
                    });
                }

            }
           
            return lastLine;
        }

        private int ADD(List<StylusPoint> list, int lastLine, bool firstPoint, IDrawPoint p)
        {
            StylusPoint stylusPointServer = new StylusPoint(p.pointServer.x, p.pointServer.y, (float)0.5);
            list.Add(stylusPointServer);
            DrawingAttributes drAtt = this.AddAttributes(p);
            StylusPointCollection toAdd = new StylusPointCollection(list);
            bool size = true;
            this.inkCAnvas.Dispatcher.Invoke(() =>
            {
                size = p.id > this.inkCAnvas.Strokes.Count;
            });
            if (lastLine != p.id || firstPoint || (size && lastLine != p.id))
            {
                lastLine = p.id;
                this.inkCAnvas.Dispatcher.Invoke(() =>
                {
                    indexes[p.id] = this.inkCAnvas.Strokes.Count;
                });
                Stroke a = new Stroke(toAdd, drAtt);
                this.inkCAnvas.Dispatcher.Invoke(() =>
                {
                    this.inkCAnvas.Strokes.Add(a);
                });
            }
            else
            {
                this.inkCAnvas.Dispatcher.Invoke(() =>
                {
                    if (lastLine >= 0)
                        this.inkCAnvas.Strokes[indexes[lastLine]].StylusPoints.Add(toAdd);
                });
            }
            list.Clear();
            return lastLine;
        }

        public void startSimulation(string image, string drawingMode)
        {
             var IStart = JsonConvert.SerializeObject(new IStartVirtualPlayer(image, drawingMode));
            SocketService.MySocket.Emit("start virtual", IStart);

        }
        private DrawingAttributes AddAttributes(IDrawPoint pointServer)
        {
            DrawingAttributes drAtt = new DrawingAttributes();
            drAtt.Width = pointServer.pointParams.width;
            drAtt.Height = pointServer.pointParams.width;

            drAtt.StylusTip = (pointServer.pointParams.pointNature == "ellipse") ? StylusTip.Ellipse : StylusTip.Rectangle;
            System.Windows.Media.Color colorFromServer = (System.Windows.Media.Color)System.Windows.Media.ColorConverter.ConvertFromString("#FF000000");
            if (pointServer.pointParams.color != null)
            {

               colorFromServer = (System.Windows.Media.Color)System.Windows.Media.ColorConverter.ConvertFromString(pointServer.pointParams.color);
            }
           
            drAtt.Color = colorFromServer;

            return drAtt;
        }
    }
}
