using Newtonsoft.Json;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.Services;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using System;
using System.Collections.Generic;
using System.Media;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Threading;

namespace Prototype_Heacy_client.Views.UserControls
{
    /// <summary>
    /// Interaction logic for UserControl_Guess.xaml
    /// </summary>
    public partial class UserControl_Guess : UserControl
    {
        bool firstGuess = false;
        bool thirdGess = false;
        bool activateTuto = false;
        bool passiv = false;
        public UserControl_Passive PassivViewGuess;
        public UserControl_Guess(StateUpdate_Model stateUpdate, HomePage_ViewModel homePageViewModel)
        {
            thirdGess = false;
            this.activateTuto = false;
            InitializeComponent();

            DataContext = new Guess_ViewModel(this);
            this.PassivViewGuess = new UserControl_Passive(stateUpdate, homePageViewModel);
            this.PassiveView.Children.Add(this.PassivViewGuess);
            //if (stateUpdate.attemptLeft == 0)
            //    this.GuessButton.IsEnabled = false;
        }


        public void SwitchToPassive(StateUpdate_Model state)
        {
            this.buttons.Children.Clear();
            this.PassivViewGuess.switchState(state);
        }
       

        public UserControl_Guess(UserControl_Passive p, Interactiv_Tutoriel tuto)
        {
            thirdGess = false;

            this.passiv = true;
            this.activateTuto = true;
            this.tuto = tuto;
            InitializeComponent();
            this.PassiveView.Children.Add(p);
            p.BarTuto.Answer.Content = "1";
            p.BarTuto.Timer.Content = "10";
            this.passTuto = p;

        }

        public Interactiv_Tutoriel tuto;
        public UserControl_Guess(Interactiv_Tutoriel tuto)
        {
            thirdGess = false;

            this.passiv = false;
            this.activateTuto = true;
            InitializeComponent();
            this.tuto = tuto;
        }

        UserControl_Passive passTuto;

        public void step1()
        {
            this.firstGuess = true;
            this.guess.IsEnabled = false;
            passTuto = new UserControl_Passive(true);
            this.PassiveView.Children.Add(passTuto);

            Tutorial_popupWindow a = new Tutorial_popupWindow("Bienvenu dans la vue de devineur, comme vous pouvez le voir votre rôle à changer, et en bas du rôle c'est rendu un chiffre au lieu d'un mot, " +
                "ce chifre indique le nombre d'essais que vous avez pour deviner un dessin, le reste des informations sont les mêmes que dans la vue précédente", "Ok");
            a.ShowDialog();
            a = new Tutorial_popupWindow("Après avoir cliquer sur Ok, vous allez voir un dessin qui se dessine cela va être une simulation de votre coéquipier cela prendra 15 seconde", "Ok");
            a.ShowDialog();
            passTuto.lunchVirualPlayer();
            this.guess.IsEnabled = true;
            this.GuessButton.IsEnabled = false;
            this.GuessWord.IsEnabled = true;
            
            this.indice.IsEnabled = false;
            this.wait();
        }

        public void step2()
        {

            Tutorial_popupWindow a = new Tutorial_popupWindow("Comme vous l'avez surement deviner le mot secret est banane, pour pouvoir répondre il va falloir saisir du texte dans la zone de texte en bas", "Ok");
            a.ShowDialog();
            a = new Tutorial_popupWindow("On commencera par saisir une mauvaise réponse, le mot Pomme va être saisis pour vous\n" +
                "vous devez maintenant cliquer sur le bouton Répondre à droite pour valider votre réponse", "Ok");
            a.ShowDialog();

            this.GuessWord.Text = "Pomme";
            this.GuessWord.IsReadOnly = true;
            this.GuessButton.IsEnabled = true;



        }
        public async void wait()
        {

            await Task.Delay(15000);
            if (!this.tuto.tutorialCancel)
            {
                this.step2();

            }

        }



        public UserControl_Guess(UserControl_Passive passiv)
        {
            this.activateTuto = false;
            InitializeComponent();
            Console.WriteLine("constructor2");
            DataContext = new Guess_ViewModel(this);
            this.PassiveView.Children.Add(passiv);
        }

        private void UserControl_Loaded(object sender, RoutedEventArgs e)
        {
            if (this.activateTuto)
            {
                this.Dispatcher.BeginInvoke((Action)(() =>
                {
                    if (!this.passiv)
                    {
                        this.step1();
                    }
                    else
                    {
                        this.passivStep();
                    }
                }));
            }
           
        }

        public void passivStep()
        {
            

            Tutorial_popupWindow a = new Tutorial_popupWindow("Bienvenu a la vue de droit de riposte, comme vous le constater c'est exactement la même vue que dans l'étape précédente mais cette fois si avec une zone pour pouvoir répondre", "Ok");
            a.ShowDialog();
            a = new Tutorial_popupWindow("La différence dans cette vue cous aller voir que le temps qui est allouer pour cette étape est de 10 secondes et que vous avez droit à une seule tentative", "Ok");
            a.ShowDialog();
            a = new Tutorial_popupWindow("Comme vous l'avez surement deviné la réponse est Lion, on va saisir pour vous cette réponse puis vous devez cliquer sur répondre pour envoyer la réponse.", "Ok");
            a.ShowDialog();
            this.tuto.tuto.IsEnabled = true;
            this.GuessButton.IsEnabled = true;
            this.indice.IsEnabled = false;
            this.GuessWord.Text = "Lion";
            this.GuessWord.IsReadOnly = true;
            thirdGess = true;



        }



        private void GuessButton_Click(object sender, RoutedEventArgs e)
        {
            if (this.activateTuto)
            {
                if (!thirdGess)
                {
                    if (firstGuess)
                    {
                        this.firstGuessTry();
                    }
                    else
                    {
                        this.SecondeGuessTry();
                    }
                }
                else
                {
                    this.thirdTry();
                }
               
            }

        }

        public void thirdTry()
        {
            this.Dispatcher.Invoke(() =>
            {
                this.passTuto.BarTuto.MyPoints.Content = "Mon équipe: 1";
                this.tuto.chatt.RecivedMessage("BRAVOO !!! vous avez eu la bonne reponse", "jour virtuel: now");
            });
            this.tuto.chatt.IsEnabled = true;
            Tutorial_popupWindow a = new Tutorial_popupWindow("Une fois qu'on a répondu vrai on peut voir que le nombre de points de votre équipe a augmenté de 1 et que nous avons reçu un message de félicitation du joueur virtuel ", "Ok");
            a.ShowDialog();
            this.tuto.tuto.IsEnabled = false;
            this.tuto.chatt.send.IsEnabled = false;
            a = new Tutorial_popupWindow("Félicitation vous êtes arrivé à la fin du tutoriel cliquer sur Fin pour accéder au jeu, merci d'avoir suivi", "Fin");
            a.ShowDialog();
            this.tuto.switchToGame();

        }

        public void SecondeGuessTry()
        {
            this.Dispatcher.Invoke(() =>
            {
                this.passTuto.BarTuto.MyPoints.Content = "Mon équipe: 1";
                this.tuto.chatt.RecivedMessage("BRAVOO !!! vous avez eu la bonne reponse", "jour virtuel: now");
            });
            Tutorial_popupWindow a = new Tutorial_popupWindow("Une fois qu'on a répondu vrai on peut voir que le nombre de points de votre équipe a augmenter de 1 et que nous avons reçu un message de félicitation du joueur virtuel", "Ok");
            a.ShowDialog();
            this.tuto.tuto.IsEnabled = false;
            this.tuto.chatt.IsEnabled = false;
            a = new Tutorial_popupWindow("Ceci résume le rôle de devineur dans le jeu, Maintenant on va passer au rôle de passif, se rôle vous sera attribuer quand l'équipe adverse est entant de jouer" +
                "cliquer sur ok pour continuer", "Ok");
            a.ShowDialog();
            this.tuto.step3();
            

        }

        public void firstGuessTry()
        {
            this.Dispatcher.Invoke(() =>
            {
                this.passTuto.BarTuto.Answer.Content = "14";
                this.tuto.chatt.RecivedMessage("oopps mauvaise reponse, Ne lache pas t'es capable", "jour virtuel: now");
            });
            this.tuto.chat.IsEnabled = true;
            this.tuto.chatt.send.IsEnabled = false;
            this.tuto.chatt.msgSent.IsReadOnly = true;
            this.GuessButton.IsEnabled = false;
            this.indice.IsEnabled = true;
            this.GuessButton.IsEnabled = false;
            Tutorial_popupWindow a = new Tutorial_popupWindow("Une fois qu'on a répondu faux on peut voir que le nombre de tentative allouées à baisser de 1 et que vous avez reçu un message d'encouragement du joueur virtuel", "Ok");
            a.ShowDialog();
            this.GuessWord.Text = "";
            a = new Tutorial_popupWindow("Après cette mauvaise réponse on va demander au joueur virtuel de nous donner un indice, cliquez sur le bouton Indice a gauche de la zone de texte pour demander l'indice", "Ok");
            a.ShowDialog();
            this.indice.IsEnabled = true;
            this.firstGuess = false;
        }

        private void Indice_Click(object sender, RoutedEventArgs e)
        {
            if (this.activateTuto)
            {
                
                this.Dispatcher.Invoke(() =>
                {
                    this.passTuto.BarTuto.Timer.Content = "20";
                    this.tuto.chatt.RecivedMessage("Indice: Fruit", "jour virtuel: now");
                });
                this.indice.IsEnabled = false;
                Tutorial_popupWindow a = new Tutorial_popupWindow("Une fois que vous avez a demandé un indice on peut voir dans la zone de message à droite que vous avez reçu comme indice Fruit de la part du joueur virtuel", "Ok");
                a.ShowDialog();
                a = new Tutorial_popupWindow("Après avoir demandé l'indice on va saisir la bonne réponse pour voir ce qui va se passer, le mot banane a était saisi pour vous cliquer sur répondre pour envoyer la réponse.", "Ok");
                a.ShowDialog();
                this.GuessWord.Text = "banane";
                this.GuessButton.IsEnabled = true;
            }
    
        }
    }
}
