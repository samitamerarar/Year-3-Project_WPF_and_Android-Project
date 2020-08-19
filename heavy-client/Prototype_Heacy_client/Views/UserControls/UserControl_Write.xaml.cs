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
    /// Interaction logic for UserControl_Write.xaml
    /// </summary>
    public partial class UserControl_Write : UserControl
    {
        public UserControl_Drow drawView = new UserControl_Drow();
        public Interactiv_Tutoriel tuto;
        public UserControl_GameInfoBar gameInfoBar;
        public UserControl_Write(StateUpdate_Model stateUpdate, HomePage_ViewModel homePageViewModel)
        {
            InitializeComponent();
            gameInfoBar = new UserControl_GameInfoBar(stateUpdate, homePageViewModel);
            this.InfoBar.Children.Add(gameInfoBar);

            this.DrawingArea.Children.Add(drawView);

        }
        public UserControl_Write()
        {
            InitializeComponent();
        }

        public void switchState(StateUpdate_Model d)
        {
            this.gameInfoBar.switchtoPassiv(d);
            drawView.Dispatcher.Invoke(() =>
            {
                InkCanvas b = new InkCanvas();
                UserControl_WriteToPassif temp = new UserControl_WriteToPassif();
                foreach (var s in drawView.surfaceDessin.Strokes)
                {
                    b.Strokes.Add(s);
                }
                
                this.DrawingArea.Children.Clear();
                temp.doc.Children.Add(b);
                this.DrawingArea.Children.Add(temp);
                
            });
        }
        public UserControl_Write(Interactiv_Tutoriel tuto)
        {
            InitializeComponent();
            this.DrawingArea.Children.Add(drawView);
            drawView.surfaceDessin.Background = Brushes.LightGray;
            this.InfoBar.Children.Add(new UserControl_GameInfoBar("WRITE"));

            this.tuto = tuto;

        }
    }

     
}
