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
    /// Interaction logic for Tutorial_popupWindow.xaml
    /// </summary>
    public partial class Tutorial_popupWindow : Window
    {
        public Tutorial_popupWindow(string content, string b)
        {
            InitializeComponent();
            this.Mytext.Text = content;
            this.button.Content = b;
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
    }
}
