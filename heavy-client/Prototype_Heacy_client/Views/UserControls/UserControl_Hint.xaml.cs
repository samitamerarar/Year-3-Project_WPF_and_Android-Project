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
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_Hint.xaml
    /// </summary>
    public partial class UserControl_Hint : UserControl
    {

        public GameCreationManual_1_ViewModel GameCVM;
        public UserControl_Hint(int number)
        {
            InitializeComponent();
            this.hint.Content = "Hint " + number.ToString();

        }
    }
}
