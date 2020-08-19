using Newtonsoft.Json;
using Prototype_Heacy_client.Commands;
using Prototype_Heacy_client.Interfaces;
using Prototype_Heacy_client.Services;
using Prototype_Heacy_client.Views;
using System;
using System.ComponentModel;

namespace Prototype_Heacy_client.ViewModels
{
    public class GroupCreation_ViewModel : INotifyPropertyChanged
    {
        GroupCreationWindow _groupCreationWindow;
        private string _groupName;
        private string _created;

        public GeneralCommands<object> CloseWindow { get; set; }
        public GeneralCommands<object> CreateGroup { get; set; }

        public GroupCreation_ViewModel(GroupCreationWindow groupCreationWindow)
        {
            this._groupCreationWindow = groupCreationWindow;

            _created = "";
            _groupName = "";

            CloseWindow = new GeneralCommands<object>(Close_CreateGroupWindow, CanClose_CreateGroupWindow);
            CreateGroup = new GeneralCommands<object>(Create_Group, CanCreate_Group);
        }
        public void Close_CreateGroupWindow(object o)
        {
            this._groupCreationWindow.Close();
        }
        public bool CanClose_CreateGroupWindow(object o)
        {
            return true;
        }
        public void Create_Group(object o)
        {
            var createGroup = JsonConvert.SerializeObject(new CreateGroup_Server(GroupName, GlobalUser.Difficulty, GlobalUser.Mode));
            SocketService.MySocket.Emit("new group", createGroup);

            Created = "Le groupe " + this.GroupName + " est crée!";
            this._groupCreationWindow.GroupNameBox.Text = "";
            this._groupCreationWindow.Close();
        }
        public bool CanCreate_Group(object o)
        {
            return !string.IsNullOrEmpty(this.GroupName);
        }

        public string GroupName
        {
            get { return _groupName; }
            set { _groupName = value; }
        }
        public String Created
        {
            get { return _created; }
            set
            {
                _created = value;
                OnPropertyChanged("Created");
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
