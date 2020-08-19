using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Forms;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_UploadImage.xaml
    /// </summary>
    public partial class UserControl_UploadImage : System.Windows.Controls.UserControl
    {

        public GameCreationAssistedS2_ViewModel vm;

        public UserControl_UploadImage(GameCreationAssistedS2_ViewModel vm)
        {
            InitializeComponent();
            this.vm = vm;
        }


     

        private void Upload_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog test = new OpenFileDialog();
            test.Filter = "Image Files|*.jpg;*.jpeg;*.png;";
            test.InitialDirectory = @"C:\";
            test.Title = "Please select an image.";
            if (test.ShowDialog() == System.Windows.Forms.DialogResult.OK)
            {
                file.Text = test.FileName;
                this.vm.file = test.FileName;
            }
        }
    }
}
