using System;
using System.ComponentModel;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.Services;
using Prototype_Heacy_client.Views;
using Newtonsoft.Json.Linq;
using Prototype_Heacy_client.ViewModels.UserControl_ViewMoels;

namespace Prototype_Heacy_client.ViewModels
{
    public class UserProfil_ViewModel : INotifyPropertyChanged
    {
      

        private string _updatedInformation;
        private string _updatedInformation2;

        private string _errorInUpdate;
        private string _erroInUpdate2;
        private string _errorInUpdate3;

        private string file = "";

        public string File { get { return file; } set { file = value; OnPropertyChanged("File"); } }
        public string id = "";
        public HomePage_ViewModel HomeVm;
        public UserProfil_ViewModel(HomePage_ViewModel vm)
        {
            HomeVm = vm;
            this.File = GlobalUser.Avatar;
            UpdateProfil = new GeneralCommands<object>(Update_Profil, CanUpdate_Profil);
            ChoosePicture = new GeneralCommands<object>(ChoosePic, CanChoose);
           

        }


        public string UpdateFirstName { get { return _updatedInformation; } set { _updatedInformation = value; OnPropertyChanged("UpdateFirstName"); } }
        public string UpdateLastName { get { return _updatedInformation2; } set { _updatedInformation2 = value; OnPropertyChanged("UpdateLastName"); } }
        public string ErrorInFirstName { get { return _errorInUpdate; } set { _errorInUpdate = value; OnPropertyChanged("ErrorInFirstName"); } }
        public string ErrorInLastName { get { return _erroInUpdate2; } set { _erroInUpdate2 = value; OnPropertyChanged("ErrorInLastName"); } }
        public string ErrorInUpdate { get { return _errorInUpdate3; } set { _errorInUpdate3 = value; OnPropertyChanged("ErrorInUpdate"); } }
        public string Success { get { return _success; } set { _success = value; OnPropertyChanged("Success"); } }
        private string _success = "";
        public GeneralCommands<object> ChoosePicture { get; set; }


        public bool CanUpdate_Profil(object o) { return true; }


        public GeneralCommands<object> UpdateProfil { get; set; }

        public void Update_Profil(object o)
        {
            this.Success = "";
            this.ErrorInUpdate = "";
            if (Verify_FirstName() && Verify_LastName())
            {
                Update_Profil_Request();
            }
            else
            {
                this.ErrorInUpdate = "Cannot update profil";
            }
        }

        public async void Update_Profil_Request()
        {
            await UploadPhotoToServerAsync();
            var content = JsonConvert.SerializeObject(new UpdateUserProfilToServer(UpdateFirstName, UpdateLastName, this.id));
            var response = await Http.Client.PostAsync(Http.UrlServer + "user/profile/" + GlobalUser.UserName, new StringContent(content, Encoding.UTF8, "application/json"));
            var responseString = await response.Content.ReadAsStringAsync();

            if (((int)response.StatusCode) != 200)
            {
                this.ErrorInUpdate = "Impossible de mettre à jour le profil";
            }
            else
            {
                
                this.Success = "Votre profil a été mis a jour";
                this.HomeVm.getInfo();

            }
        }
        public void ChoosePic(object o)
        {
            new Gallery_Window(this).ShowDialog();
        }

        public bool CanChoose(object o)
        {
            return true;
        }

        public async Task UploadPhotoToServerAsync()
        {
            byte[] data = System.IO.File.ReadAllBytes(this.file);
            string base64String = Convert.ToBase64String(data);
            var content = JsonConvert.SerializeObject(new ImageToServer(base64String));
            StringContent stringContent = new StringContent(content, Encoding.UTF8, "application/json");
            var resp = await Http.Client.PostAsync(Http.UrlServer + "image/upload", stringContent);
            var responseString = await resp.Content.ReadAsStringAsync();

            if ((int)resp.StatusCode != 200)
            {
                this.ErrorInUpdate = "Photo uploading error";
            }
            else
            {
                this.id = (string)JObject.Parse(responseString)["id"];
            }
        }

        public bool Verify_FirstName()
        {
            if (!string.IsNullOrWhiteSpace(UpdateFirstName))
            {
                this.ErrorInFirstName = "";
                return true;
            }
            else
            {
                this.ErrorInFirstName = "Le prénom est requis";
                return false;
            }
        }


        public bool Verify_LastName()
        {
            if (!string.IsNullOrWhiteSpace(UpdateLastName))
            {
                this.ErrorInLastName = "";
                return true;
            }
            else
            {
                this.ErrorInLastName = "Le nom est requis";
                return false;
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
