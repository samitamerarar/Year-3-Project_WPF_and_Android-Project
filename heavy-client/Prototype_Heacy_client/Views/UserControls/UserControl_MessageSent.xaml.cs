using System.Windows.Controls;


namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_MessageSent.xaml
    /// </summary>
    public partial class UserControl_MessageSent : UserControl
    {
        public UserControl_MessageSent(string text, string date)
        {
            InitializeComponent();
            this.theMessage.Text = text;
            this.date.Text = date;
        }
    }
}
