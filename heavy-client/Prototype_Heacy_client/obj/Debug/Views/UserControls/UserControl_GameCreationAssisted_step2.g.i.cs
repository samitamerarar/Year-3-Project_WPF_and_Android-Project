﻿#pragma checksum "..\..\..\..\Views\UserControls\UserControl_GameCreationAssisted_step2.xaml" "{8829d00f-11b8-4213-878b-770e8597ac16}" "33F0FD57035406EE2185CDBF19FD515EFAFBDAF01C6109359B83C5AD37BDEAA4"
//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Runtime Version:4.0.30319.42000
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

using Prototype_Heacy_client.Views.UserControls;
using System;
using System.Diagnostics;
using System.Windows;
using System.Windows.Automation;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Markup;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Media.Effects;
using System.Windows.Media.Imaging;
using System.Windows.Media.Media3D;
using System.Windows.Media.TextFormatting;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Shell;


namespace Prototype_Heacy_client.Views.UserControls {
    
    
    /// <summary>
    /// UserControl_GameCreationAssisted_step2
    /// </summary>
    public partial class UserControl_GameCreationAssisted_step2 : System.Windows.Controls.UserControl, System.Windows.Markup.IComponentConnector {
        
        
        #line 14 "..\..\..\..\Views\UserControls\UserControl_GameCreationAssisted_step2.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.Grid Image;
        
        #line default
        #line hidden
        
        
        #line 72 "..\..\..\..\Views\UserControls\UserControl_GameCreationAssisted_step2.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.Button hint;
        
        #line default
        #line hidden
        
        
        #line 78 "..\..\..\..\Views\UserControls\UserControl_GameCreationAssisted_step2.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.StackPanel HintStack;
        
        #line default
        #line hidden
        
        
        #line 104 "..\..\..\..\Views\UserControls\UserControl_GameCreationAssisted_step2.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.Button previous;
        
        #line default
        #line hidden
        
        private bool _contentLoaded;
        
        /// <summary>
        /// InitializeComponent
        /// </summary>
        [System.Diagnostics.DebuggerNonUserCodeAttribute()]
        [System.CodeDom.Compiler.GeneratedCodeAttribute("PresentationBuildTasks", "4.0.0.0")]
        public void InitializeComponent() {
            if (_contentLoaded) {
                return;
            }
            _contentLoaded = true;
            System.Uri resourceLocater = new System.Uri("/Prototype_Heacy_client;component/views/usercontrols/usercontrol_gamecreationassi" +
                    "sted_step2.xaml", System.UriKind.Relative);
            
            #line 1 "..\..\..\..\Views\UserControls\UserControl_GameCreationAssisted_step2.xaml"
            System.Windows.Application.LoadComponent(this, resourceLocater);
            
            #line default
            #line hidden
        }
        
        [System.Diagnostics.DebuggerNonUserCodeAttribute()]
        [System.CodeDom.Compiler.GeneratedCodeAttribute("PresentationBuildTasks", "4.0.0.0")]
        [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Never)]
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Design", "CA1033:InterfaceMethodsShouldBeCallableByChildTypes")]
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Maintainability", "CA1502:AvoidExcessiveComplexity")]
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1800:DoNotCastUnnecessarily")]
        void System.Windows.Markup.IComponentConnector.Connect(int connectionId, object target) {
            switch (connectionId)
            {
            case 1:
            this.Image = ((System.Windows.Controls.Grid)(target));
            return;
            case 2:
            this.hint = ((System.Windows.Controls.Button)(target));
            
            #line 72 "..\..\..\..\Views\UserControls\UserControl_GameCreationAssisted_step2.xaml"
            this.hint.Click += new System.Windows.RoutedEventHandler(this.Hint_Click);
            
            #line default
            #line hidden
            return;
            case 3:
            this.HintStack = ((System.Windows.Controls.StackPanel)(target));
            return;
            case 4:
            this.previous = ((System.Windows.Controls.Button)(target));
            
            #line 104 "..\..\..\..\Views\UserControls\UserControl_GameCreationAssisted_step2.xaml"
            this.previous.Click += new System.Windows.RoutedEventHandler(this.Previous_Click);
            
            #line default
            #line hidden
            return;
            }
            this._contentLoaded = true;
        }
    }
}

