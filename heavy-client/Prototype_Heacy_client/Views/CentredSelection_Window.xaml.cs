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
    /// Interaction logic for CentredSelection_Window.xaml
    /// </summary>
    /// 
  
    public partial class CentredSelection_Window : Window
    {

        public GameCreationManual_1_ViewModel ManualVM = null;
        public GameCreationAssistedS1_ViewModel AssistedVM = null;
        public CentredSelection_Window(GameCreationAssistedS1_ViewModel vm)
        {
            AssistedVM = vm;
            InitializeComponent();
        }

        public CentredSelection_Window(GameCreationManual_1_ViewModel vm)
        {
            ManualVM = vm;
            InitializeComponent();
        }

        private void Opt1_Checked(object sender, RoutedEventArgs e)
        {
   
            if (AssistedVM is null)
            {
                ManualVM.drawingMode = "CENTERED_IN";
            }
            else
            {
                AssistedVM.drawingMode = "CENTERED_IN";
            }
          
        }

        private void Opt2_Checked(object sender, RoutedEventArgs e)
        {
            if (AssistedVM is null)
            {
                ManualVM.drawingMode = "CENTERED_OUT";
            }
            else
            {
                AssistedVM.drawingMode = "CENTERED_OUT";
            }
      
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
    }
}
