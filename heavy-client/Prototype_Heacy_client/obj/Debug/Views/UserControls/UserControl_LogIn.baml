   M S B A M L   `   `   `  ����Q  MPrototype_Heacy_client, Version=1.0.0.0, Culture=neutral, PublicKeyToken=nulle7clr-namespace:Prototype_Heacy_client.Views.UserControls)Prototype_Heacy_client.Views.UserControls  5      A    ;Prototype_Heacy_client.Views.UserControls.UserControl_LogIn   5      R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    5      R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5      Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5      @d2http://schemas.microsoft.com/expression/blend/2008     5      Ilocal7clr-namespace:Prototype_Heacy_client.Views.UserControls     5      U QMaterialDesignThemes.Wpf, Version=2.6.0.325, Culture=neutral, PublicKeyToken=nullFmaterialDesign1http://materialdesigninxaml.net/winfx/xaml/themes  5      .��5      � .+�5   
   �� $	��280��6   $	��Top=�6!   ��5      �� 5        �� EndPoint$  0.5,1&�6&    �� 
StartPoint$ 0.5,0&�67   .��5      �  � Color$ 	#FF3580BF��6#   � 5      $ 	#FF3580BF��6#    � Offset$ 1Q�65   � 5      $ 	#FF3580BF��6#   $ 0.546Q�65   5      5      �� 5   
   $	��380��6   $	��320��6    $��Bottom=�6-   $��0,100q�6H   
������� J� Effect 5      3  -System.Windows.Media.Effects.DropShadowEffect  5         
BlurRadius$ 15Q�6#      	Direction$ 0Q�63      RenderingBias$ Quality=�6A      ShadowDepth$ 1Q�6Y   	   Color$	 	#FFBBBBBB��6i   5      � 5   
   $	��380��6   $��Bottom=�6   $��35,85q�67   $	��300��6F   .+�5      �� $!��S'AUTHENTIFIER/SE CONNECTER��6   $��Center=�6:   $	��Top=�6W   $��5q�6o   
�������$��18(�6�   �� 5      $��Center=�6   $��15q�64   .+�5      �� $��Pseudonyme :��6   $
��left=�6/   $	��Top=�6J   
�������$��12(�6u   �� 5       
�������$
��0,10q�61   $��1q�6?   ._�5!      �� -   5        
 ��Name$
 
Pseudonyme��5!      |�. $
��10,0q�6C    %  !MaterialDesignFloatingHintTextBox#��[  6�   $��Champagne & Limousines)�6�   $��18(�6�   ��6Q   �� *User.Username+ �� UpdateSourceTrigger$ PropertyChanged=�.��6   J� InputBindings 5"      �� 5#      
 �� Key$ Enter��6)    �� Command 65   �� *LogIn+$ PropertyChanged=�.��5$   1   5%      5&      �� 5(      $��Mot de passe :��6   $
��left=�61   $	��Top=�6L   
�������$��12(�6v   �� 5)      $
��0,10q�6   
�������$��1q�6@   ._�5*      G� -   5         G�Name$ Password��5*   "   $
��10,0q�64    ) %MaterialDesignFloatingHintPasswordBox#��[ 6B   $��Champagne & Limousines)�6�   $��18(�6�    5+      �� 5,      $ Enter��6)    65   �� *LogIn+$ PropertyChanged=�5.      5/      �� 51      -   5         ��Name$ 	ErrorText��51       J� RenderTransformOrigin$ 0.5,0.5&�6/   $��Center=�6�   $	��Top=�6�   $��5q�6�   
���  ��$��12(�6�   ��6O   �� *
User.Error+$ PropertyChanged=� �� Mode$ TwoWay=� � 	Resources 52        TestStoryboard �� TargetProperty g� RepeatBehavior9  3System.Windows.Media.Animation.EasingDoubleKeyFrame O� KeyTime O� Value��%i  &	       �� 53      .��54      U� $, &RenderTransform.(TranslateTransform.Y)�6<   $ 5x��6   .\�55   "     $ 0:0:0.05��67   $ 0Q�6J     56   "   $ 0:0:0.1��67   $ 2Q�6I     57   "   $ 0:0:0.15��67   $ 0Q�6J     58   "   $ 0:0:0.20��67   $ -2Q�6J     59   "   $ 0:0:0.25��67   $ 0Q�6J   5:      5;      5<      z�5=      T� 5>       T� X$ 0Q�6-    T� Y$ 0Q�63   5@      5B      5C      �� 5D   
   -   5         ��Name$ LoginButton��5D      $	��250��6&   $��Center=�6L   $��Bottom=�6i   $
��0,85q�6�   $!��S'AUTHENTIFIER/SE CONNECTER��6�   ���	#FF3580BF��62   �� *LogIn+5E      5F      5H      