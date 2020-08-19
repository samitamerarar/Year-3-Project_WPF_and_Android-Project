using System.Windows.Controls;


namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_MessageRecieved.xaml
    /// </summary>
    public partial class UserControl_MessageRecieved : UserControl
    {
        public UserControl_MessageRecieved(string text, string date)
        {
            InitializeComponent();
            this.theMessage.Text = text;
            this.date.Text = date;
        }
    }
}
