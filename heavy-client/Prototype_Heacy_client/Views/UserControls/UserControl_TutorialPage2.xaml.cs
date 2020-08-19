using Prototype_Heacy_client.ViewModels;
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
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_TutorialPage2.xaml
    /// </summary>
    public partial class UserControl_TutorialPage2 : UserControl
    {
        public UserControl_TutorialPage2()
        {
            InitializeComponent();
            //DataContext = new Tutorial_ViewModel(this);
        }
    }
}
