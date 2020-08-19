using Prototype_Heacy_client.ViewModels;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using System.Windows;
using System.Windows.Input;

namespace Prototype_Heacy_client.Views
{
    /// <summary>
    /// Interaction logic for GroupeCreationWindow.xaml
    /// </summary>
    public partial class GroupCreationWindow : Window
    {
        public GroupCreationWindow()
        {
            InitializeComponent();
            DataContext = new GroupCreation_ViewModel(this);
            this.GroupNameBox.Focus();
        }

        private void TitleBar_MouseDown(object sender, MouseButtonEventArgs e)
        {
            try
            {
                DragMove();
            }
            catch
            {

            }
        }
    }
}
