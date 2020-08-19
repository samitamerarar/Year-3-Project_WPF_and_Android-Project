using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using Prototype_Heacy_client.Views.UserControls;
using Svg;
using System.Collections;
using System.Collections.Generic;
using System.Windows;


namespace Prototype_Heacy_client.Views
{
    /// <summary>
    /// Interaction logic for GameCreation_Window.xaml
    /// </summary>
    public partial class GameCreation_Window : Window
    {
        public Drow_ViewModel draw_ViewModel;
        UserControl_Drow DrowInterface;
        public ArrayList drawingPoint = new ArrayList();
        public UserControl_GameCreationManual_1 GameCreationManual1;
        public GameCreation_Window()
        {
            InitializeComponent();
            draw_ViewModel = new Drow_ViewModel();
            this.DrowInterface = new UserControl_Drow(this.draw_ViewModel);
            this.Draw_Interface.Children.Add(DrowInterface);
            this.GameCreationManual1 = new UserControl_GameCreationManual_1(this);
            this.Game_Creation.Children.Add(this.GameCreationManual1);
        }

      

        private async void Simulation_Click(object sender, RoutedEventArgs e)
        {
            if (this.draw_ViewModel.Traits.Count > 0)
            {
                await this.GameCreationManual1.GameCVM.createGame(true);
                Simulation_Window SimulationWindow = new Simulation_Window(this.GameCreationManual1.GameCVM.idImage, this.GameCreationManual1.GameCVM.drawingMode);
                SimulationWindow.ShowDialog();
            }
            else
            {
                GameCreationFeedback_window a = new GameCreationFeedback_window("Please draw something to simulate", false);
                a.ShowDialog();
            }
           
        }

        public void BuildDrowingPoints()
        {
            this.drawingPoint = new ArrayList();
            foreach(var stroke in this.draw_ViewModel.Traits)
            {

                var arrayPoint = new ArrayList();
               
                arrayPoint.Add(stroke.DrawingAttributes.StylusTip.ToString());
                arrayPoint.Add(stroke.DrawingAttributes.Height);
                arrayPoint.Add(stroke.DrawingAttributes.Width);
                arrayPoint.Add(stroke.DrawingAttributes.Color.ToString());
                int count = 0;
                foreach (var point in stroke.GetBezierStylusPoints())
                {
                    count++;
                    arrayPoint.Add(new Point(point.X, point.Y));
                }
                this.drawingPoint.Add(arrayPoint);
            }
        }




    }
}
