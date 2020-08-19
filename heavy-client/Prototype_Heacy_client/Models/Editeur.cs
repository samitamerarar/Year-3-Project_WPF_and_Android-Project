using Prototype_Heacy_client.Interfaces;
using System;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Windows.Ink;

namespace Prototype_Heacy_client.Models
{
    class Editeur : INotifyPropertyChanged
    {
        public event PropertyChangedEventHandler PropertyChanged;
        public StrokeCollection traits = new StrokeCollection();
        private StrokeCollection traitsRetires = new StrokeCollection();

        // Outil actif dans l'éditeur
        private string outilSelectionne = "crayon";
        public string OutilSelectionne
        {
            get { return outilSelectionne; }
            set { outilSelectionne = "crayon"; ProprieteModifiee(); }
        }

        // Forme de la pointe du crayon
        private string pointeSelectionnee = "ronde";
        public string PointeSelectionnee
        {
            get { return pointeSelectionnee; }
            set
            {
                OutilSelectionne = "crayon";
                GlobalUser.OutilSelectionnee = "crayon";
                pointeSelectionnee = value;
                ProprieteModifiee();
            }
        }

        // Couleur des traits tracés par le crayon.
        private string couleurSelectionnee = "Black";
        public string CouleurSelectionnee
        {
            get { return couleurSelectionnee; }
            // Lorsqu'on sélectionne une couleur c'est généralement pour ensuite dessiner un trait.
            // C'est pourquoi lorsque la couleur est changée, l'outil est automatiquement changé pour le crayon.
            set
            {
                couleurSelectionnee = value;
                ProprieteModifiee();
            }
        }

        // Grosseur des traits tracés par le crayon.
        private int tailleTrait = 11;
        public int TailleTrait
        {
            get { return tailleTrait; }
            // Lorsqu'on sélectionne une taille de trait c'est généralement pour ensuite dessiner un trait.
            // C'est pourquoi lorsque la taille est changée, l'outil est automatiquement changé pour le crayon.
            set
            {
                tailleTrait = value;
                OutilSelectionne = "crayon";
                CouleurSelectionnee = "#FF000000";
                GlobalUser.OutilSelectionnee = "crayon";
                ProprieteModifiee();
            }
        }

        /// <summary>
        /// Appelee lorsqu'une propriété d'Editeur est modifiée.
        /// Un évènement indiquant qu'une propriété a été modifiée est alors émis à partir d'Editeur.
        /// L'évènement qui contient le nom de la propriété modifiée sera attrapé par VueModele qui pourra
        /// alors prendre action en conséquence.
        /// </summary>
        /// <param name="propertyName">Nom de la propriété modifiée.</param>
        protected void ProprieteModifiee([CallerMemberName] string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }

        // S'il y a au moins 1 trait sur la surface, il est possible d'exécuter Empiler.
        public bool PeutEmpiler(object o) => (traits.Count > 0);
        // On retire le trait le plus récent de la surface de dessin et on le place sur une pile.
        public void Empiler(object o)
        {
            try
            {


                Stroke trait = traits.Last();
                traitsRetires.Add(trait);
                traits.Remove(trait);
            }
            catch { }

        }

        // S'il y a au moins 1 trait sur la pile de traits retirés, il est possible d'exécuter Depiler.
        public bool PeutDepiler(object o) => (traitsRetires.Count > 0);
        // On retire le trait du dessus de la pile de traits retirés et on le place sur la surface de dessin.
        public void Depiler(object o)
        {
            try
            {
                Stroke trait = traitsRetires.Last();
                traits.Add(trait);
                traitsRetires.Remove(trait);
            }
            catch { }
        }

        // On assigne une nouvelle forme de pointe passée en paramètre.
        public void ChoisirPointe(string pointe)
        { PointeSelectionnee = pointe; CouleurSelectionnee = "#FF000000";
            OutilSelectionne = "crayon";
            GlobalUser.OutilSelectionnee = "crayon";
        }

        // L'outil actif devient celui passé en paramètre.
        //public void ChoisirOutil(string outil) => OutilSelectionne = outil;
        public void ChoisirOutil(string outil)
        {
            //Console.WriteLine(outil);

            GlobalUser.OutilSelectionnee = outil;

            OutilSelectionne = outil;
            if(outil == "efface_segment")
            {
                CouleurSelectionnee = "#FFFFFFFF";

            }
            if (outil == "efface_trait")
            {
                CouleurSelectionnee = "#00000000";
            }
            if(outil == "crayon")
            {
                CouleurSelectionnee = "#FF000000";
            }
        }
        
        

        // On vide la surface de dessin de tous ses traits.
        public void Reinitialiser(object o) => traits.Clear();
    }
}
