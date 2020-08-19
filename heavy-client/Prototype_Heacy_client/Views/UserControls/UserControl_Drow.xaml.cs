using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Controls.Primitives;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using System.Diagnostics;
using System.IO;
using System.Windows.Ink;
using System.Timers;
using System.Threading;
using System.Windows.Markup;
using System.Collections.Generic;
using Newtonsoft.Json;
using Prototype_Heacy_client.Services;
using Newtonsoft.Json.Linq;
using Prototype_Heacy_client.Interfaces;
using System.Threading.Tasks;

namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_Drow.xaml
    /// </summary>
    public partial class UserControl_Drow
    {
        public bool clicked = false;
        public int counter = 0;
        bool activateTuto = false;
        public List<List<Point>> traits;
        public List<Point> trait;
        public Interactiv_Tutoriel tuto;
        public bool notClicked = true;

        public UserControl_Drow()
        {
            this.activateTuto = false;
            InitializeComponent();
            GlobalUser.OutilSelectionnee = "crayon";
            DataContext = new Drow_ViewModel(); ;
            traits = new List<List<Point>>();
            trait = new List<Point>();
            crayon.Background = Brushes.LightBlue;
            efface_segment.Background = Brushes.Transparent;
            efface_trait.Background = Brushes.Transparent;
            this.surfaceDessin.UseCustomCursor = true;
            this.surfaceDessin.Cursor = Cursors.Hand;
        }

        public void step1(Interactiv_Tutoriel tuto)
        {
            this.activateTuto = true;
            this.tuto = tuto;
            this.tuto.tuto.IsEnabled = true;
            this.surfaceDessin.IsEnabled = false;
            this.surfaceDessin.Background = Brushes.LightGray;
            Tutorial_popupWindow a = new Tutorial_popupWindow("On va commencer par la présentation de la barre bleu si haut de gauche à droite", "Ok");
            a.ShowDialog();
            a = new Tutorial_popupWindow("A gauche de la barre vous avez les points qui son dédiés a votre équipe, en dessous vous avez le numéro de la manche que vous jouez", "Ok");
            a.ShowDialog();
            a = new Tutorial_popupWindow("Au milieu de la barre vous avez le rôle qui vous ai dédié, dans cette manche vous êtes Auteur, juste en bas vous avez le mot à dessiner et à faire deviner à votre coéquipier", "Ok");
            a.ShowDialog();
            a = new Tutorial_popupWindow("Finalement à gauche de la barre vous avez les points de votre équipe adverse, et juste en bas vous avez un conteur en secondes qui indique le temps que vous disposez pour finir votre dessin", "Ok");
            a.ShowDialog();
            this.notClicked = true;
            a = new Tutorial_popupWindow("Pour commencer à dessiner vous devez choisir un des outils présent a votre droite\n vous pouvez y'allez maintenant !!!", "Ok");
            a.ShowDialog();
       

        }


        public async void wait()
        {
           
            await Task.Delay(20000);
            this.step2();
               
        }

        public void step2()
        {
            Tutorial_popupWindow a = new Tutorial_popupWindow("Maintenant que vous avez fini le dessin, votre rôle d’Auteur est fini, et c'est au devineur de répondre", "Ok");
            a.ShowDialog();
            a = new Tutorial_popupWindow("Vous allez être transféré à la vue de devineur pour continuer le tutoriel", "Ok");
            a.ShowDialog();
            this.tuto.step2();

        }
        private void StackPanel_LostMouseCapture(object sender, MouseEventArgs e)
        {
            if (this.activateTuto && notClicked)
            {
                this.surfaceDessin.Background = Brushes.White;

                notClicked = false;

                Tutorial_popupWindow a = new Tutorial_popupWindow("Vous devez maintenant dessiner le mot qui vous ai proposer (Banane) sur la feuille de dessin blanche au milieu \n Vous pouvez commencer vous avez 20 seconde", "Ok");
                a.ShowDialog();
                this.surfaceDessin.IsEnabled = true;


                this.wait();
            }
            
        }
        public UserControl_Drow(Drow_ViewModel vm)
        {
            traits = new List<List<Point>>();
            trait = new List<Point>();
            GlobalUser.OutilSelectionnee = "crayon";
            InitializeComponent();
            DataContext = vm;
           
        }
        // Pour gérer les points de contrôles.
        private void GlisserCommence(object sender, DragStartedEventArgs e) => (sender as Thumb).Background = Brushes.Black;
        private void GlisserTermine(object sender, DragCompletedEventArgs e) => (sender as Thumb).Background = Brushes.White;
        //private void GlisserMouvementRecu(object sender, DragDeltaEventArgs e)
        //{
        //    String nom = (sender as Thumb).Name;
        //    if (nom == "horizontal" || nom == "diagonal") colonne.Width = new GridLength(Math.Max(32, colonne.Width.Value + e.HorizontalChange));
        //    if (nom == "vertical" || nom == "diagonal") ligne.Height = new GridLength(Math.Max(32, ligne.Height.Value + e.VerticalChange));

        //}

        // Pour la gestion de l'affichage de position du pointeur.
        private void surfaceDessin_MouseLeave(object sender, MouseEventArgs e) => textBlockPosition.Text = "";
        private void surfaceDessin_MouseMove(object sender, MouseEventArgs e)
        {
          
                Point p = e.GetPosition(surfaceDessin);
                textBlockPosition.Text = Math.Round(p.X) + ", " + Math.Round(p.Y) + "px";

                if (clicked)
                {
                    DrawingAttributes attributes = new DrawingAttributes();

                    // get attributes
                    StylusPoint stylusPoint = new StylusPoint(p.X, p.Y, (float)0.5);
                    StylusTip stylusTip = this.surfaceDessin.DefaultDrawingAttributes.StylusTip;
                    Color color = this.surfaceDessin.DefaultDrawingAttributes.Color;
                    double width = this.surfaceDessin.DefaultDrawingAttributes.Width;
                    double height = this.surfaceDessin.DefaultDrawingAttributes.Height;

                  

                    // send to server
                    ImagePoint pCoord = new ImagePoint(p.X, p.Y);

                    string action = "";
                    DrawPoint_Server segment = null;
                    if ( GlobalUser.OutilSelectionnee == "crayon" || GlobalUser.OutilSelectionnee == "efface_segment")
                    {
                        action = "ADD";
                        this.trait.Add(p);
                        segment = new DrawPoint_Server("DRAW", action, this.traits.Count, new PointParams(stylusTip.ToString(), color.ToString(), width, height), pCoord);
                    }

                    else
                    {
                        action = "DEL";
                        int index = 0;
                        foreach (var elem in traits)
                        {
                            foreach (var po in elem)
                            {
                                if (p.X >= po.X - 6 && p.X <= po.X + 6 && p.Y >= po.Y - 6 && p.Y <= po.Y + 6)
                                {
                                    segment = new DrawPoint_Server("DRAW", action, index, new PointParams(stylusTip.ToString(), color.ToString(), width, height), pCoord);
                                    this.surfaceDessin.Strokes[index].DrawingAttributes.Color = System.Windows.Media.Color.FromArgb(255, 255, 255, 255);

                                    break;
                                }
                            }
                            index++;
                        }
                    }
                    if (segment != null && !activateTuto)
                    {
                        var image = JsonConvert.SerializeObject(segment);
                        SocketService.MySocket.Emit("draw", image);

                    }
                }

            
           
        }

        private void surfaceDessin_GotMouseCapture(object sender, MouseEventArgs e)
        {
            clicked = true;
            this.surfaceDessin.UseCustomCursor = true;
            this.surfaceDessin.Cursor = Cursors.Hand;
        }
        private void surfaceDessin_LostMouseCapture(object sender, MouseEventArgs e)
        {
            clicked = false;
            if(GlobalUser.OutilSelectionnee == "crayon" || GlobalUser.OutilSelectionnee == "efface_segment")
            {
                traits.Add(trait);
            }
            else {
                this.surfaceDessin.Strokes.Remove(this.surfaceDessin.Strokes[this.surfaceDessin.Strokes.Count - 1]);
            }
            this.trait = new List<Point>();
           
        }
        private void DupliquerSelection(object sender, RoutedEventArgs e)
        {
            surfaceDessin.CopySelection();
            surfaceDessin.Paste();
        }

        private void SupprimerSelection(object sender, RoutedEventArgs e) => surfaceDessin.CutSelection();

        private void Crayon_Click(object sender, RoutedEventArgs e)
        {
            this.Couleur.IsEnabled = true;

            crayon.Background = Brushes.LightBlue;
            efface_segment.Background = Brushes.Transparent;
            efface_trait.Background = Brushes.Transparent;

        }

        private void Efface_segment_Click(object sender, RoutedEventArgs e)
        {
            this.Couleur.IsEnabled = false;
            efface_segment.Background = Brushes.LightBlue;
            crayon.Background = Brushes.Transparent;
            efface_trait.Background = Brushes.Transparent;
        }

        private void Efface_trait_Click(object sender, RoutedEventArgs e)
        {
            this.Couleur.IsEnabled = false;
            efface_trait.Background = Brushes.LightBlue;
            crayon.Background = Brushes.Transparent;
            efface_segment.Background = Brushes.Transparent;
        }

        private void PointeRonde_Click(object sender, RoutedEventArgs e)
        {
            this.Couleur.IsEnabled = true;

            crayon.Background = Brushes.LightBlue;
            efface_segment.Background = Brushes.Transparent;
            efface_trait.Background = Brushes.Transparent;
        }

        private void PointeCarree_Click(object sender, RoutedEventArgs e)
        {
            this.Couleur.IsEnabled = true;

            crayon.Background = Brushes.LightBlue;
            efface_segment.Background = Brushes.Transparent;
            efface_trait.Background = Brushes.Transparent;
        }

        private void Gridcouleur_GotMouseCapture(object sender, MouseEventArgs e)
        {
            this.Couleur.IsEnabled = true;

            crayon.Background = Brushes.LightBlue;
            efface_segment.Background = Brushes.Transparent;
            efface_trait.Background = Brushes.Transparent;
        }

        private void Couleur_GotMouseCapture(object sender, MouseEventArgs e)
        {
            crayon.Background = Brushes.LightBlue;
            efface_segment.Background = Brushes.Transparent;
            efface_trait.Background = Brushes.Transparent;
        }

        private void Taille_GotMouseCapture(object sender, MouseEventArgs e)
        {
            this.Couleur.IsEnabled = true;

            crayon.Background = Brushes.LightBlue;
            efface_segment.Background = Brushes.Transparent;
            efface_trait.Background = Brushes.Transparent;
        }
    }
}
