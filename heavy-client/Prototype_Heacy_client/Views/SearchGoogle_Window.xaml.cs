using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Prototype_Heacy_client.Services;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;
using Prototype_Heacy_client.Views.UserControls;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading;
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
    /// Interaction logic for SearchGoogle_Window.xaml
    /// </summary>
    public partial class SearchGoogle_Window : Window
    {
        GameCreationAssistedS2_ViewModel vm;
        ArrayList filesNames = new ArrayList();
        public bool startedReserche = false;
        public Loading_Window animation;
        private List<UserControl_Image> listtoDisplay;

        public SearchGoogle_Window(GameCreationAssistedS2_ViewModel vm)
        {
            InitializeComponent();
            this.vm = vm;
            this.save.IsEnabled = false;
            listtoDisplay = new List<UserControl_Image>();

        }

        private void btn_Click(object sender, RoutedEventArgs e)
        {
            Button b = (Button)sender;
            UserControl_Image p = (UserControl_Image)b.Parent;
            int count = 0;
            foreach (var elem in this.Images.Children)
            {
                if(!(elem is Button))
                {
                    var br = new SolidColorBrush();
                    UserControl_Image c = (UserControl_Image)elem;
                    if (c.Name == p.Name)
                    {
                        br.Opacity = 1;
                        br.Color = System.Windows.Media.Color.FromRgb(255, 255, 255);
                        this.vm.file = System.Environment.CurrentDirectory + "/image" + count + ".jpeg";
                        this.save.IsEnabled = true;
                        c.button.Background = br;
                    }
                    else
                    {
                        br.Opacity = 0;
                        c.button.Background = br;
                    }
                    count++;
                }
              
               
            }
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            if (!string.IsNullOrWhiteSpace(this.Search.Text))
            {
                clearImages();
                this.Images.Children.Clear();
                this.filesNames = new ArrayList();
                this.getImages();
            }
        }

        public void clearImages()
        {
            foreach (var image in listtoDisplay)
            {
            }
            listtoDisplay.Clear();
        }


        private void animationLoading()
        {
            this.Dispatcher.Invoke(() =>
            {
                this.animation = new Loading_Window(this);
                this.animation.ShowDialog();
            });          
        }
        public async void getImages()
        {
            startedReserche = true;
            Thread thr = new Thread(animationLoading);
            thr.Start();
            
            var resp = await Http.Client.GetAsync(Http.UrlServer + "image/search/" + this.Search.Text);
            var responseString = await resp.Content.ReadAsStringAsync();
            if ((int)resp.StatusCode == 200)
            {
                this.SaveImages(responseString);
                this.displayTenImages();
            }

            startedReserche = false;

        }

        void SaveImages(string responseString)
        {
            var ImageBase64 = JsonConvert.DeserializeObject<ArrayList>(responseString);
            int count = 0;
            var sigPath = System.Environment.CurrentDirectory;
            foreach (var elem in ImageBase64)
            {
                string filename = sigPath + "/image" + count + ".jpeg";
                this.filesNames.Add("image" + count);
                string result = Regex.Replace((string)elem, "^data:image/[a-zA-Z]+;base64,", string.Empty);
                byte[] bytes = Convert.FromBase64String(result);
                writeTheFile(filename, bytes);
                count++;
            }
        }

        void writeTheFile(string filename, byte[] bytes)
        {
            using (MemoryStream ms = new MemoryStream(bytes))
            {
                using (var image = System.Drawing.Image.FromStream(ms))
                {
                    
                    image.Save(filename, System.Drawing.Imaging.ImageFormat.Jpeg);
                
                }
            }
        }

        void displayTenImages()
        {
            var sigPath = System.Environment.CurrentDirectory;
            this.Images.Children.Clear();
            clearImages();
            int bound= 0;
            if (this.filesNames.Count < 10)
            {
                bound = this.filesNames.Count;
            }
            else
            {
                bound = 10;
            }

            for (int i = 0; i< bound; i++)
            {
                UserControl_Image c = new UserControl_Image();
                Button b = new Button();
                using (var stream = File.OpenRead(sigPath + "/" + (string)this.filesNames[i] + ".jpeg"))
                {
                    var image = new BitmapImage();
                    image.BeginInit();
                    image.CacheOption = BitmapCacheOption.OnLoad;
                    image.StreamSource = stream;
                    image.EndInit();
                    c.MySource = image;
                }
                c.Name = (string)this.filesNames[i];
                c.image.Height = 400;
                c.image.Width = 600;
                c.button.Click += btn_Click;
                this.listtoDisplay.Add(c);
            }
            foreach(var i in listtoDisplay)
            {
                this.Images.Children.Add(i);
            }

            this.setbutton();
        }

        void setbutton()
        {
            Button button = new Button();
            button.Content = "Load More";
            button.Width = 200;
            var br = new SolidColorBrush();
            br.Color = System.Windows.Media.Color.FromRgb(255, 255, 255);
            button.Foreground = br;
            button.FontSize = 16;
            button.Click += Button1_Click;
            this.Images.Children.Add(button);
        }

        void displayImages()
        {
            var sigPath = System.Environment.CurrentDirectory;
            this.Images.Children.Clear();
            this.vm.file = "";
            this.save.IsEnabled = false;
            clearImages();
            foreach (var elem in filesNames)
            {
                UserControl_Image c = new UserControl_Image();
                Button b = new Button();
                using (var stream = File.OpenRead(sigPath + "/" + (string)elem + ".jpeg"))
                {
                    var image = new BitmapImage();
                    image.BeginInit();
                    image.CacheOption = BitmapCacheOption.OnLoad;
                    image.StreamSource = stream;
                    image.EndInit();
                    c.MySource = image;
                }
                c.Name = (string)elem;
                c.image.Height = 400;
                c.image.Width = 600;
                c.button.Click += btn_Click;
                this.listtoDisplay.Add(c);
            }
            foreach (var i in listtoDisplay)
            {
                this.Images.Children.Add(i);
            }
        }

        private void Button1_Click(object sender, RoutedEventArgs e)
        {
            this.displayImages();
        }

        private void Save_Click(object sender, RoutedEventArgs e)
        {
            this.Images.Children.Clear();
            this.Close();
        }

        private void Close_Click(object sender, RoutedEventArgs e)
        {
            this.Images.Children.Clear();
            this.vm.file = "";
            this.Close();
        }
    }
}
