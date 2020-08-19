using Prototype_Heacy_client.Interfaces;
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
    /// Interaction logic for GameCreationAssisted_Window.xaml
    /// </summary>
    public partial class GameCreationAssisted_Window : Window
    {
        public UserControl_GameCreationAssisted step1;
        public UserControl_GameCreationAssisted_step2 step2;
        public GameCreationAssisted_Window()
        {
            InitializeComponent();
            this.step1 = new UserControl_GameCreationAssisted(this);
            this.Height = 750;
            this.step.Children.Add(this.step1);
        }

        public void createNextStep(GameCreationAssistedS1_ViewModel obj)
        {

            this.step.Children.Remove(this.step1);
            this.Height = 860;
            this.step2 = new UserControl_GameCreationAssisted_step2(this, obj);
            this.step.Children.Add(this.step2);

        }

        public void previousStep()
        {
            this.step.Children.Remove(this.step2);
            this.Height = 750;
            this.step.Children.Add(this.step1);

        }
    }
}
