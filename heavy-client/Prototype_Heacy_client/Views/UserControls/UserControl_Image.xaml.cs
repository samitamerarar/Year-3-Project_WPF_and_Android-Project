using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media.Imaging;

namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_Image.xaml
    /// </summary>
    public partial class UserControl_Image : UserControl, INotifyPropertyChanged
    {

        BitmapImage source { get; set; }
        public BitmapImage MySource { get { return source; } set { source = value; OnPropertyChanged("MySource"); } }
        public UserControl_Image()
        {
            source = new BitmapImage();
            InitializeComponent();
            DataContext = this;
            
        }

        public event PropertyChangedEventHandler PropertyChanged;

        private void OnPropertyChanged(string propertyName)
        {
            PropertyChangedEventHandler handler = PropertyChanged;

            if (handler != null)
            {
                handler(this, new PropertyChangedEventArgs(propertyName));
            }
        }

    }
}
