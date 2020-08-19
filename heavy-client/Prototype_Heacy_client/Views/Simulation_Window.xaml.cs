using Newtonsoft.Json;
using Prototype_Heacy_client.Services;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using Prototype_Heacy_client.Views.UserControls;
using Svg;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;

namespace Prototype_Heacy_client.Views
{
    /// <summary>
    /// Interaction logic for Simulation_Window.xaml
    /// </summary>
    /// 


 

    public partial class Simulation_Window : Window
    {
        public Drow_ViewModel DrowVM;
        public UserControl_Drow drowInterface;
        public VirtualPlayer virtualPlayer;

        public Simulation_Window(string image, string DMode)
        {
            this.InitializeComponent();
            virtualPlayer = new VirtualPlayer(this.InkCanvas, this.path);
            virtualPlayer.startSimulation(image, DMode);
            Console.WriteLine(image);
        }
        
        private void Window_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            SocketService.MySocket.Off("draw");
        }
    }
}

