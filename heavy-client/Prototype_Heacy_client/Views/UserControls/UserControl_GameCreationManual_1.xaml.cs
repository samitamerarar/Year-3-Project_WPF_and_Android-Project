using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using System;
using System.Collections;
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
    /// Interaction logic for UserControl_GameCreationManual_1.xaml
    /// </summary>
    public partial class UserControl_GameCreationManual_1 : UserControl
    {

        public GameCreationManual_1_ViewModel GameCVM;
        public UserControl_GameCreationManual_1(GameCreation_Window gameWind)
        {
            InitializeComponent();

            GameCVM = new GameCreationManual_1_ViewModel(gameWind);
            addHint();
            DataContext = GameCVM;
        }

       

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            this.addHint();
        }

        private void addHint()
        {
            var hint = new UserControl_Hint(GameCVM.MyHints.Count + 1);
            this.GameCVM.MyHints.Add(hint);
            this.HintStack.Children.Add(hint);
        }
    }
}
