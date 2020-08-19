using System;
using System.Windows.Controls;
using System.Windows.Data;

namespace Prototype_Heacy_client.Convertisseur
{
     public class ConvertisseurBordure : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            return (value.ToString() == parameter.ToString()) ? "#FF58BDFA" : "#00000000";
        }
        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture) => System.Windows.DependencyProperty.UnsetValue;
    }

    /// Permet de générer une couleur en fonction de la chaine passée en paramètre.
    /// Par exemple, pour chaque bouton d'un groupe d'option on compare son nom avec l'élément actif (sélectionné) du groupe.
    /// S'il y a correspondance, la couleur de fond du bouton aura une teinte bleue, sinon elle sera transparente.
    /// Cela permet de mettre l'option sélectionnée dans un groupe d'options en évidence.
    public class ConvertisseurCouleurFond : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            // Console.WriteLine(value.ToString());
            // Console.WriteLine((string)value);
            if (value.ToString() == parameter.ToString())
            {
                // Console.WriteLine("kkjdsjd");
                return "#3F58BDFA";
            }
            else
            {
                return  "#00000000";
            }
               
        }
        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture) => System.Windows.DependencyProperty.UnsetValue;
    }

    /// <summary>
    /// Permet au InkCanvas de définir son mode d'édition en fonction de l'outil sélectionné.
    /// </summary>
   public  class ConvertisseurModeEdition : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            switch (value)
            {
                case "lasso":
                    return InkCanvasEditingMode.Select;
                case "efface_segment":
                    return InkCanvasEditingMode.Ink;
                case "efface_trait":
                    return InkCanvasEditingMode.Ink;
                default:
                    return InkCanvasEditingMode.Ink;
            }
        }
        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture) => System.Windows.DependencyProperty.UnsetValue;
    }
}
