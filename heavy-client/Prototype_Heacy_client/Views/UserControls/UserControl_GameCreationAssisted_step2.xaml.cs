using Prototype_Heacy_client.Interfaces;
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
    /// Interaction logic for UserControl_GameCreationAssisted_step2.xaml
    /// </summary>
    public partial class UserControl_GameCreationAssisted_step2 : UserControl
    {
        public GameCreationAssisted_Window mainWind;
        public GameCreationAssistedS2_ViewModel assisted_S2_VM;
        public UserControl_GameCreationAssisted_step2(GameCreationAssisted_Window main, GameCreationAssistedS1_ViewModel obj)
        {
            InitializeComponent();
            this.mainWind = main;
            this.assisted_S2_VM = new GameCreationAssistedS2_ViewModel(obj);
            if(obj.imageOption == "Recherche sur Google")
            {
                this.Image.Children.Clear();
                this.Image.Children.Add(new UserControl_SerchGoogle(this.assisted_S2_VM));
            }
            else
            {
                this.Image.Children.Clear();
                this.Image.Children.Add(new UserControl_UploadImage(this.assisted_S2_VM));
            }
            addHint();

            DataContext = this.assisted_S2_VM;


        }


        private void addHint()
        {
            var hint = new UserControl_Hint(assisted_S2_VM.MyHints.Count + 1);
            this.assisted_S2_VM.MyHints.Add(hint);
            this.HintStack.Children.Add(hint);
        }
        private void Previous_Click(object sender, RoutedEventArgs e)
        {
            this.mainWind.previousStep();
        }

        private void Hint_Click(object sender, RoutedEventArgs e)
        {
            this.addHint();

        }
    }
}
