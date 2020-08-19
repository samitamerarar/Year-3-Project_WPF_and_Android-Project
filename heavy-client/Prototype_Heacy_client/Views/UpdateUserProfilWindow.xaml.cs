using Prototype_Heacy_client.ViewModels;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using Prototype_Heacy_client.Views.UserControls;
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
    /// Interaction logic for UpdateUserProfilWindow.xaml
    /// </summary>
    public partial class UpdateUserProfilWindow : Window
    {
        public UpdateUserProfilWindow(HomePage_ViewModel vm)
        {
            InitializeComponent();
            DataContext = new UserProfil_ViewModel(vm);
        }

     
    }
}
