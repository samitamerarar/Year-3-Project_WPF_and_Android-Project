using Prototype_Heacy_client.Interfaces;
using System;
using System.Collections;
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
    /// Interaction logic for UserControl_WaitingView.xaml
    /// </summary>
    public partial class UserControl_WaitingView : UserControl
    {
        public UserControl_WaitingView(List<List<TournState>> state)
        {
            InitializeComponent();
            this.FillTree();

            foreach (var arr in state)
            {
                switch (arr.Count)
                {
                    case 16:
                        this.PlayerA1.Children.Add(new UserControl_PlayerTournament((string)arr[0].username, (string)arr[0].status, (string)arr[0].avatar));
                        this.PlayerA2.Children.Add(new UserControl_PlayerTournament((string)arr[1].username, (string)arr[1].status, (string)arr[1].avatar));
                        this.PlayerA3.Children.Add(new UserControl_PlayerTournament((string)arr[2].username, (string)arr[2].status, (string)arr[2].avatar));
                        this.PlayerA4.Children.Add(new UserControl_PlayerTournament((string)arr[3].username, (string)arr[3].status, (string)arr[3].avatar));
                        this.PlayerA5.Children.Add(new UserControl_PlayerTournament((string)arr[4].username, (string)arr[4].status, (string)arr[4].avatar));
                        this.PlayerA6.Children.Add(new UserControl_PlayerTournament((string)arr[5].username, (string)arr[5].status, (string)arr[5].avatar));
                        this.PlayerA7.Children.Add(new UserControl_PlayerTournament((string)arr[6].username, (string)arr[6].status, (string)arr[6].avatar));
                        this.PlayerA8.Children.Add(new UserControl_PlayerTournament((string)arr[7].username, (string)arr[7].status, (string)arr[7].avatar));

                        this.PlayerE1.Children.Add(new UserControl_PlayerTournament((string)arr[8].username, (string)arr[8].status, (string)arr[8].avatar));
                        this.PlayerE2.Children.Add(new UserControl_PlayerTournament((string)arr[9].username, (string)arr[9].status, (string)arr[9].avatar));
                        this.PlayerE3.Children.Add(new UserControl_PlayerTournament((string)arr[10].username, (string)arr[10].status, (string)arr[10].avatar));
                        this.PlayerE4.Children.Add(new UserControl_PlayerTournament((string)arr[11].username, (string)arr[11].status, (string)arr[11].avatar));
                        this.PlayerE5.Children.Add(new UserControl_PlayerTournament((string)arr[12].username, (string)arr[12].status, (string)arr[12].avatar));
                        this.PlayerE6.Children.Add(new UserControl_PlayerTournament((string)arr[13].username, (string)arr[13].status, (string)arr[13].avatar));
                        this.PlayerE7.Children.Add(new UserControl_PlayerTournament((string)arr[14].username, (string)arr[14].status, (string)arr[14].avatar));
                        this.PlayerE8.Children.Add(new UserControl_PlayerTournament((string)arr[15].username, (string)arr[15].status, (string)arr[15].avatar));
                        break;
                    case 8:
                        this.PlayerB1.Children.Add(new UserControl_PlayerTournament((string)arr[0].username, (string)arr[0].status, (string)arr[0].avatar));
                        this.PlayerB2.Children.Add(new UserControl_PlayerTournament((string)arr[1].username, (string)arr[1].status, (string)arr[1].avatar));
                        this.PlayerB3.Children.Add(new UserControl_PlayerTournament((string)arr[2].username, (string)arr[2].status, (string)arr[2].avatar));
                        this.PlayerB4.Children.Add(new UserControl_PlayerTournament((string)arr[3].username, (string)arr[3].status, (string)arr[3].avatar));
                                                                                                                         
                        this.PlayerF1.Children.Add(new UserControl_PlayerTournament((string)arr[4].username, (string)arr[4].status, (string)arr[4].avatar));
                        this.PlayerF2.Children.Add(new UserControl_PlayerTournament((string)arr[5].username, (string)arr[5].status, (string)arr[5].avatar));
                        this.PlayerF3.Children.Add(new UserControl_PlayerTournament((string)arr[6].username, (string)arr[6].status, (string)arr[6].avatar));
                        this.PlayerF4.Children.Add(new UserControl_PlayerTournament((string)arr[7].username, (string)arr[7].status, (string)arr[7].avatar));

                        break;
                    case 4:
                        this.PlayerC1.Children.Add(new UserControl_PlayerTournament((string)arr[0].username, (string)arr[0].status, (string)arr[0].avatar));
                        this.PlayerC2.Children.Add(new UserControl_PlayerTournament((string)arr[1].username, (string)arr[1].status, (string)arr[1].avatar));

                        this.PlayerG1.Children.Add(new UserControl_PlayerTournament((string)arr[2].username, (string)arr[2].status, (string)arr[2].avatar));
                        this.PlayerG2.Children.Add(new UserControl_PlayerTournament((string)arr[3].username, (string)arr[3].status, (string)arr[3].avatar));
                        break;
                    case 2:
                        this.PlayerD1.Children.Add(new UserControl_PlayerTournament((string)arr[0].username, (string)arr[0].status, (string)arr[0].avatar));
                        this.PlayerH1.Children.Add(new UserControl_PlayerTournament((string)arr[1].username, (string)arr[1].status, (string)arr[1].avatar));
                        break;
                    case 1:
                        break;
                }
            }

        }

        public void FillTree()
        {

            this.PlayerD1.Children.Add(new UserControl_PlayerTournament());
            this.PlayerH1.Children.Add(new UserControl_PlayerTournament());


            this.PlayerC1.Children.Add(new UserControl_PlayerTournament());
            this.PlayerC2.Children.Add(new UserControl_PlayerTournament());

            this.PlayerG1.Children.Add(new UserControl_PlayerTournament());
            this.PlayerG2.Children.Add(new UserControl_PlayerTournament());

            this.PlayerB1.Children.Add(new UserControl_PlayerTournament());
            this.PlayerB2.Children.Add(new UserControl_PlayerTournament());
            this.PlayerB3.Children.Add(new UserControl_PlayerTournament());
            this.PlayerB4.Children.Add(new UserControl_PlayerTournament());

            this.PlayerF1.Children.Add(new UserControl_PlayerTournament());
            this.PlayerF2.Children.Add(new UserControl_PlayerTournament());
            this.PlayerF3.Children.Add(new UserControl_PlayerTournament());
            this.PlayerF4.Children.Add(new UserControl_PlayerTournament());



            this.PlayerA1.Children.Add(new UserControl_PlayerTournament());
            this.PlayerA2.Children.Add(new UserControl_PlayerTournament());
            this.PlayerA3.Children.Add(new UserControl_PlayerTournament());
            this.PlayerA4.Children.Add(new UserControl_PlayerTournament());
            this.PlayerA5.Children.Add(new UserControl_PlayerTournament());
            this.PlayerA6.Children.Add(new UserControl_PlayerTournament());
            this.PlayerA7.Children.Add(new UserControl_PlayerTournament());
            this.PlayerA8.Children.Add(new UserControl_PlayerTournament());

            this.PlayerE1.Children.Add(new UserControl_PlayerTournament());
            this.PlayerE2.Children.Add(new UserControl_PlayerTournament());
            this.PlayerE3.Children.Add(new UserControl_PlayerTournament());
            this.PlayerE4.Children.Add(new UserControl_PlayerTournament());
            this.PlayerE5.Children.Add(new UserControl_PlayerTournament());
            this.PlayerE6.Children.Add(new UserControl_PlayerTournament());
            this.PlayerE7.Children.Add(new UserControl_PlayerTournament());
            this.PlayerE8.Children.Add(new UserControl_PlayerTournament());
        }
    }
}
