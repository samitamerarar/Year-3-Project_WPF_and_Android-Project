using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Views;
using System.ComponentModel;
using System.Windows.Forms;
using System.Windows.Media;
using Prototype_Heacy_client.Interfaces;

namespace Prototype_Heacy_client.ViewModels
{
    public class Gallery_ViewModel : INotifyPropertyChanged
    {
        private UserProfil_ViewModel _updateUserProfil;
        private Gallery_Window _galleryWindow;
        public GeneralCommands<object> ChooseProfileCommand { get; set; }
        public GeneralCommands<object> BrowseProfile { get; set; }
        public Gallery_ViewModel(UserProfil_ViewModel userProfile, Gallery_Window galer )
        {
            this._galleryWindow = galer;
            this._updateUserProfil = userProfile;
            ChooseProfileCommand = new GeneralCommands<object>(ChooseProfile, CanChooseProfile);
            BrowseProfile = new GeneralCommands<object>(Update_Profil_Avatar, CanUpdate_Profil_Avatar);
        }

        public void ChooseProfile(object parameter)
        {
            string s = (string)parameter;
            switch(s)
            {
                case "Pic1": this.ChangePhoto(this._galleryWindow.Pic1.ImageSource); break;
                case "Pic2": this.ChangePhoto(this._galleryWindow.Pic2.ImageSource); break;
                case "Pic3": this.ChangePhoto(this._galleryWindow.Pic3.ImageSource); break;
                case "Pic4": this.ChangePhoto(this._galleryWindow.Pic4.ImageSource); break;
                case "Pic5": this.ChangePhoto(this._galleryWindow.Pic5.ImageSource); break;
                case "Pic6": this.ChangePhoto(this._galleryWindow.Pic6.ImageSource); break;
                case "Pic7": this.ChangePhoto(this._galleryWindow.Pic7.ImageSource); break;
                case "Pic8": this.ChangePhoto(this._galleryWindow.Pic8.ImageSource); break;
            }
        }
        public bool CanChooseProfile(object o)
        {
            return true;
        }

        public bool CanUpdate_Profil_Avatar(object o) {
            return true;
        }
        public void ChangePhoto(ImageSource source)
        {           
            var i = source.ToString().Split(',');
            this._updateUserProfil.File = System.Environment.CurrentDirectory.Replace("\\bin\\Debug", i[i.Length - 1]);

            this._galleryWindow.Close();
        }

        public void Update_Profil_Avatar(object o)
        {
            OpenFileDialog op = new OpenFileDialog();
            op.Filter = "All supported graphics|*.jpg;*.jpeg;*.png|" +
            "JPEG (*.jpg;*.jpeg)|*.jpg;*.jpeg|" +
            "Portable Network Graphic (*.png)|*.png";

            if (op.ShowDialog() == System.Windows.Forms.DialogResult.OK)
            {
                GlobalUser.Avatar = op.FileName;
                this._updateUserProfil.File = op.FileName;
                this._galleryWindow.Close();
            }
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
