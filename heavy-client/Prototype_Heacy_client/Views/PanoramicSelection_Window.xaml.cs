using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace Prototype_Heacy_client.Views
{
    /// <summary>
    /// Interaction logic for PanoramicSelection_Window.xaml
    /// </summary>
    public partial class PanoramicSelection_Window : Window
    {
        public GameCreationManual_1_ViewModel ManualVM = null;
        public GameCreationAssistedS1_ViewModel AssistedVM = null;
        public PanoramicSelection_Window(GameCreationManual_1_ViewModel vm)
        {
            ManualVM = vm;
            InitializeComponent();
         
        }
        public PanoramicSelection_Window(GameCreationAssistedS1_ViewModel vm)
        {
            AssistedVM = vm;
            InitializeComponent();

        }

        private void Opt1_Checked(object sender, RoutedEventArgs e)
        {

            if (AssistedVM is null)
            {
                ManualVM.drawingMode = "PANORAMIC_U";
            }
            else
            {
                AssistedVM.drawingMode = "PANORAMIC_U";
            }
          
        }

        private void Opt2_Checked(object sender, RoutedEventArgs e)
        {

            if (AssistedVM is null)
            {
                ManualVM.drawingMode = "PANORAMIC_D";
            }
            else
            {
                AssistedVM.drawingMode = "PANORAMIC_D";
            }
        }

        private void Opt3_Checked(object sender, RoutedEventArgs e)
        {

            if (AssistedVM is null)
            {
                ManualVM.drawingMode = "PANORAMIC_L";
            }
            else
            {
                AssistedVM.drawingMode = "PANORAMIC_L";
            }
            
        }

        private void Opt4_Checked(object sender, RoutedEventArgs e)
        {
            if (AssistedVM is null)
            {
                ManualVM.drawingMode = "PANORAMIC_R";
            }
            else
            {
                AssistedVM.drawingMode = "PANORAMIC_R";
            }
            
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
    }
}
