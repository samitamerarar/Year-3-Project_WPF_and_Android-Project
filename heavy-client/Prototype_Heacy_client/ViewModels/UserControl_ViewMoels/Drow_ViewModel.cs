using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Windows.Ink;
using System.Windows.Media;
using Prototype_Heacy_client.Models;
using Prototype_Heacy_client.Commands;
using Svg;
using Prototype_Heacy_client.Views.UserControls;

namespace Prototype_Heacy_client.ViewModels.UserControl_ViewMoels
{
   public class Drow_ViewModel : INotifyPropertyChanged
    {
          
        public event PropertyChangedEventHandler PropertyChanged;
        private Editeur editeur = new Editeur();
       
        public string SomeText { get; set; }

        // Ensemble d'attributs qui définissent l'apparence d'un trait.
        public DrawingAttributes AttributsDessin { get; set; } = new DrawingAttributes();

        public string OutilSelectionne
        {
            get { return editeur.OutilSelectionne; }
            set { ProprieteModifiee(); }
        }

        public string CouleurSelectionnee
        {
            get { return editeur.CouleurSelectionnee; }
            set { editeur.CouleurSelectionnee = value; }
        }

        public string PointeSelectionnee
        {
            get { return editeur.PointeSelectionnee; }
            set { ProprieteModifiee(); }
        }

        public int TailleTrait
        {
            get { return editeur.TailleTrait; }
            set { editeur.TailleTrait = value; }
        }

        public StrokeCollection Traits { get; set; }

        // Commandes sur lesquels la vue pourra se connecter.
        public RelayCommand_Drow<object> Empiler { get; set; }

        public RelayCommand_Drow<object> Depiler { get; set; }
        public RelayCommand_Drow<string> ChoisirPointe { get; set; }
        public RelayCommand_Drow<string> ChoisirOutil { get; set; }
        public RelayCommand_Drow<object> Reinitialiser { get; set; }

        /// <summary>
        /// Constructeur de VueModele
        /// On récupère certaines données initiales du modèle et on construit les commandes
        /// sur lesquelles la vue se connectera.
        /// </summary>

        public Drow_ViewModel()
        {
            // On écoute pour des changements sur le modèle. Lorsqu'il y en a, EditeurProprieteModifiee est appelée.
            editeur.PropertyChanged += new PropertyChangedEventHandler(EditeurProprieteModifiee);
            SomeText = "";
            // On initialise les attributs de dessin avec les valeurs de départ du modèle.
            AttributsDessin = new DrawingAttributes();
            AttributsDessin.Color = (Color)ColorConverter.ConvertFromString(editeur.CouleurSelectionnee);
            AjusterPointe();

            Traits = editeur.traits;
            // Pour chaque commande, on effectue la liaison avec des méthodes du modèle.            
            Empiler = new RelayCommand_Drow<object>(editeur.Empiler, editeur.PeutEmpiler);
            Depiler = new RelayCommand_Drow<object>(editeur.Depiler, editeur.PeutDepiler);
            // Pour les commandes suivantes, il est toujours possible des les activer.
            // Donc, aucune vérification de type Peut"Action" à faire.
            ChoisirPointe = new RelayCommand_Drow<string>(editeur.ChoisirPointe);
            ChoisirOutil = new RelayCommand_Drow<string>(editeur.ChoisirOutil);
            Reinitialiser = new RelayCommand_Drow<object>(editeur.Reinitialiser);
        }

        public Drow_ViewModel(UserControl_Drow userDrow)
        {
            // On écoute pour des changements sur le modèle. Lorsqu'il y en a, EditeurProprieteModifiee est appelée.
            editeur.PropertyChanged += new PropertyChangedEventHandler(EditeurProprieteModifiee);
            SomeText = "";
            // On initialise les attributs de dessin avec les valeurs de départ du modèle.
            AttributsDessin = new DrawingAttributes();
            AttributsDessin.Color = (Color)ColorConverter.ConvertFromString(editeur.CouleurSelectionnee);
            AjusterPointe();

            Traits = editeur.traits;
            // Pour chaque commande, on effectue la liaison avec des méthodes du modèle.            
            Empiler = new RelayCommand_Drow<object>(editeur.Empiler, editeur.PeutEmpiler);
            Depiler = new RelayCommand_Drow<object>(editeur.Depiler, editeur.PeutDepiler);
            // Pour les commandes suivantes, il est toujours possible des les activer.
            // Donc, aucune vérification de type Peut"Action" à faire.
            ChoisirPointe = new RelayCommand_Drow<string>(editeur.ChoisirPointe);
            ChoisirOutil = new RelayCommand_Drow<string>(editeur.ChoisirOutil);
            Reinitialiser = new RelayCommand_Drow<object>(editeur.Reinitialiser);

        }

        public void SavveAsSvg()
        {
            SvgDocument svg = new SvgDocument();

            var group = new SvgGroup();
            foreach (var stroke in Traits)
            {
                var geometry = stroke.GetGeometry();
                SvgPath a = new SvgPath
                {
                    PathData = SvgPathBuilder.Parse(geometry.ToString()),
                    StrokeWidth = new SvgUnit((float)AttributsDessin.Width),
                };
                group.Children.Add(a);
                svg.Children.Add(group);
            }
            svg.Write("Game.svg");

        }
    

        /// <summary>
        /// Appelee lorsqu'une propriété de VueModele est modifiée.
        /// Un évènement indiquant qu'une propriété a été modifiée est alors émis à partir de VueModèle.
        /// L'évènement qui contient le nom de la propriété modifiée sera attrapé par la vue qui pourra
        /// alors mettre à jour les composants concernés.
        /// </summary>
        /// <param name="propertyName">Nom de la propriété modifiée.</param>
        protected virtual void ProprieteModifiee([CallerMemberName] string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }

        /// <summary>
        /// Traite les évènements de modifications de propriétés qui ont été lancés à partir
        /// du modèle.
        /// </summary>
        /// <param name="sender">L'émetteur de l'évènement (le modèle)</param>
        /// <param name="e">Les paramètres de l'évènement. PropertyName est celui qui nous intéresse. 
        /// Il indique quelle propriété a été modifiée dans le modèle.</param>
        private void EditeurProprieteModifiee(object sender, PropertyChangedEventArgs e)
        {
            if (e.PropertyName == "CouleurSelectionnee")
            {
                AttributsDessin.Color = (Color)ColorConverter.ConvertFromString(editeur.CouleurSelectionnee);
            }
            else if (e.PropertyName == "OutilSelectionne")
            {
                OutilSelectionne = editeur.OutilSelectionne;
            }
            else if (e.PropertyName == "PointeSelectionnee")
            {
                PointeSelectionnee = editeur.PointeSelectionnee;
                AjusterPointe();
            }
            else // e.PropertyName == "TailleTrait"
            {
                AjusterPointe();
            }
        }

        /// <summary>
        /// C'est ici qu'est défini la forme de la pointe, mais aussi sa taille (TailleTrait).
        /// Pourquoi deux caractéristiques se retrouvent définies dans une même méthode? Parce que pour créer une pointe 
        /// horizontale ou verticale, on utilise une pointe carrée et on joue avec les tailles pour avoir l'effet désiré.
        /// </summary>
        private void AjusterPointe()
        {
            AttributsDessin.StylusTip = (editeur.PointeSelectionnee == "ronde") ? StylusTip.Ellipse : StylusTip.Rectangle;
            AttributsDessin.Width = (editeur.PointeSelectionnee == "verticale") ? 1 : editeur.TailleTrait;
            AttributsDessin.Height = (editeur.PointeSelectionnee == "horizontale") ? 1 : editeur.TailleTrait;
        }
    }
}
