using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using Prototype_Heacy_client.Views.UserControls;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace Prototype_Heacy_client.Views
{
    /// <summary>
    /// Interaction logic for TestDrawing.xaml
    /// </summary>
    public partial class TestDrawing : Window
    {

        public TestDrawing(StrokeCollection sc)
        {
            InitializeComponent();
            this.MyContent.Strokes = sc;
            this.Show();
            //DataContext = new UserControl_Drow();
        }

        public TestDrawing()
        {
            InitializeComponent();
            this.MyContent.Strokes = new StrokeCollection();
            this.Show();

        }

        public void AddToStroke(StrokeCollection sc)
        {
            this.MyContent.Strokes = sc;

        }


        // Pour la gestion de l'affichage de position du pointeur.
        //private void surfaceDessin_MouseLeave(object sender, MouseEventArgs e) => textBlockPosition.Text = "";
        //private void surfaceDessin_MouseMove(object sender, MouseEventArgs e)
        //{
        //    Point p = e.GetPosition(surfaceDessin);
        //    textBlockPosition.Text = Math.Round(p.X) + ", " + Math.Round(p.Y) + "px";

        //    Debug.Write("");
        //}
    }
}
