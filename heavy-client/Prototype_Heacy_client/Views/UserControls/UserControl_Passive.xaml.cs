using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.Services;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Media;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_Passive.xaml
    /// </summary>
    public partial class UserControl_Passive : UserControl
    {
        List<StylusPoint> listPoints = new List<StylusPoint>();
        public VirtualPlayer virtualP;
        public Interactiv_Tutoriel tuto;
        public bool activatetuto = false;
        public bool passive = false;

        public UserControl_Passive()
        {
            this.activatetuto = false;
            InitializeComponent();
           
        }

        public UserControl_Passive(Interactiv_Tutoriel tuto)
        {
            InitializeComponent();
            this.activatetuto = true;
            this.tuto = tuto;
            this.passive = true;
            this.BarTuto = new UserControl_GameInfoBar("PASSIVE");
            this.InfoBar.Children.Add(BarTuto);

        }

        public UserControl_GameInfoBar BarTuto;
        public UserControl_GameInfoBar BarInfoPassive;
        public UserControl_Passive(bool tuto)
        {
            InitializeComponent();
            this.activatetuto = true;
            this.passive = false;
            this.BarTuto = new UserControl_GameInfoBar("GUESS");
            this.InfoBar.Children.Add(BarTuto);
            
        }

        
        public async void lunchVirualPlayer()
        {
            virtualP = new VirtualPlayer(this.PassiveCanvas, this.PassiveCanvasPath);
            virtualP.startSimulation("image_16udqop2ck3ax7frl.json", "NORMAL");
        }

        public UserControl_Passive(StateUpdate_Model stateUpdate, HomePage_ViewModel homePageViewModel)
        {
            this.activatetuto = false;

           
            this.BarInfoPassive = new UserControl_GameInfoBar(stateUpdate, homePageViewModel);
            InitializeComponent();
            this.virtualP = new VirtualPlayer(this.PassiveCanvas, this.PassiveCanvasPath);
            this.InfoBar.Children.Add(this.BarInfoPassive);
        }

        public void switchState(StateUpdate_Model state)
        {
            this.BarInfoPassive.switchtoPassiv(state);
        }

        public void passiveStep1()
        {
            Tutorial_popupWindow a = new Tutorial_popupWindow("Bienvenu(e) dans la vue passive, vous allez avoir accès à cette vue quand c'est à l'équipe adverse de jouer.\n" +
                "Vous pouvez voir que votre rôle est devenu passif et que la couleur des barres est verte.\n" +
                "Dans cette vue, vous allez avoir accès à ce que le devineur de l'équipe adverse voit.", "Ok");
            a.ShowDialog();
            a = new Tutorial_popupWindow("Le rôle de passif consiste à regarder la partie qui se joue de l'équipe adverse.  Une fois le temps écouler, si l'équipe adverse ne devine pas le mot, vous aller avoir le droit à une riposte pour essayer de deviner le mot.", "Ok");
            a.ShowDialog();
            a = new Tutorial_popupWindow("Après avoir cliqué sur \"ok\", vous aller voir une simulation d'une partie qui durera 15 secondes, où l'autre équipe ne réussira pas à deviner le mot. Cliquer sur \"ok\" pour commencer.", "Ok");
            a.ShowDialog();
            virtualP = new VirtualPlayer(this.PassiveCanvas, this.PassiveCanvasPath);
            virtualP.startSimulation("svg_16udqo3c4k3c6ptda.svg", "PANORAMIC_U");
            this.wait();
           

        }
        public void passiveStep2()
        {
            Tutorial_popupWindow a = new Tutorial_popupWindow("Si l'équipe adverse avait deviner le mot vous aller voir leur point augmenter de 1, et vous serai transférer vers un autre rôle dépendamment de la manche, ou vers la fin du jeux," +
                "mais comme vous pouvez le constater le temps a écouler et l'équipe adverse n'a pas deviner, du coup vous aller avoir un droit de riposte cliquer sur ok pour continuer", "Ok");
            a.ShowDialog();
            this.activatetuto = false;
            this.tuto.step4(this);

        }

        public async void wait()
        {

            await Task.Delay(20000);
            if (!this.tuto.tutorialCancel)
            {
                this.passiveStep2();
            }
           

        }
        private void UserControl_Loaded(object sender, RoutedEventArgs e)
        {
            if (activatetuto)
            {
                if (passive)
                {
                    this.Dispatcher.BeginInvoke((Action)(() =>
                    {
                        this.passiveStep1();
                    }));
                }
            }
         

        }
    }
}
