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
    /// Interaction logic for Loading_Window.xaml
    /// </summary>
    public partial class Loading_Window : Window
    {
        SearchGoogle_Window s;
        public Loading_Window(SearchGoogle_Window s)
        {
            InitializeComponent();
            this.s = s;
            this.animation();
        }

        public async void animation()
        {
            while (this.s.startedReserche)
            {
                this.myLabel.Content = "Image Loading ";
                await Task.Delay(800);
                if (this.s.startedReserche)
                {
                    this.myLabel.Content = "Image Loading .";
                    await Task.Delay(800);
                }
                if (this.s.startedReserche)
                {
                    this.myLabel.Content = "Image Loading ..";
                    await Task.Delay(800);
                }
                if (this.s.startedReserche)
                {
                    this.myLabel.Content = "Image Loading ...";
                    await Task.Delay(800);
                }

               

            }

            this.Close();
        }
    }
}
