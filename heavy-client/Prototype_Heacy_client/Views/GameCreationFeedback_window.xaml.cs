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
    /// Interaction logic for GameCreationFeedback_window.xaml
    /// </summary>
    public partial class GameCreationFeedback_window : Window
    {
        public GameCreationFeedback_window(string text, bool succes)
        {

            InitializeComponent();
            this.text.Text = text;

            if (succes)
            {
                this.text.Foreground = new SolidColorBrush(Colors.Green);
            }
            else
            {
                this.text.Foreground = new SolidColorBrush(Colors.Red);
            }
        }
    }
}
