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
    /// Interaction logic for UserControl_SerchGoogle.xaml
    /// </summary>
    public partial class UserControl_SerchGoogle : UserControl
    {

        GameCreationAssistedS2_ViewModel vm;
        public UserControl_SerchGoogle(GameCreationAssistedS2_ViewModel vm)
        {
            InitializeComponent();
            this.vm = vm;
            this.elem.Text = this.vm.file;
        }



        private void Search_Click(object sender, RoutedEventArgs e)
        {
            SearchGoogle_Window search = new SearchGoogle_Window(this.vm);
            search.ShowDialog();
            this.elem.Text = this.vm.file;
        }
    }
}
