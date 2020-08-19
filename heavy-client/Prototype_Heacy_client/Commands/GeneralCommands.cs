using System;
using System.Windows.Input;


namespace Prototype_Heacy_client.Commands
{
    public class GeneralCommands<T> : ICommand
    {

        readonly Action<T> _execute = null;
        readonly Predicate<T> _canExecute = null;


        public GeneralCommands(Action<T> execute, Predicate<T> canExecute)
        {
            _execute = execute;
            _canExecute = canExecute;
        }

        public event EventHandler CanExecuteChanged
        {
            add { CommandManager.RequerySuggested += value; }
            remove { CommandManager.RequerySuggested -= value; }

        }

        public bool CanExecute(object parameter)
        {

            return _canExecute((T)parameter);
        }

        public void Execute(object parameter)
        {

            _execute((T)parameter);
          

        }
    }
}
