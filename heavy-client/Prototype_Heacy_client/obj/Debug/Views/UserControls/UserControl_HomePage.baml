   M S B A M L   `   `   `  ����Q  MPrototype_Heacy_client, Version=1.0.0.0, Culture=neutral, PublicKeyToken=nulle7clr-namespace:Prototype_Heacy_client.Views.UserControls)Prototype_Heacy_client.Views.UserControls  5      e7clr-namespace:Prototype_Heacy_client.Views.UserControls)Prototype_Heacy_client.Views.UserControls  5      D    >Prototype_Heacy_client.Views.UserControls.UserControl_HomePage   5      R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    5      R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5      Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5      @d2http://schemas.microsoft.com/expression/blend/2008     5      Ilocal7clr-namespace:Prototype_Heacy_client.Views.UserControls     5      TUserControlViews7clr-namespace:Prototype_Heacy_client.Views.UserControls     5      U QMaterialDesignThemes.Wpf, Version=2.6.0.325, Culture=neutral, PublicKeyToken=nullFmaterialDesign1http://materialdesigninxaml.net/winfx/xaml/themes  5        � 	Resources  5         OpenMenu �� TargetProperty �� 
TargetName9  3System.Windows.Media.Animation.EasingDoubleKeyFrame O� KeyTime O� Value  	CloseMenu��%  &	        &	 B
    �� 5   
   .��5      U� $U O(UIElement.RenderTransform).(TransformGroup.Children)[3].(TranslateTransform.X)�6,   $ GridMain��6�   .\�5        $ 0��6'   $ 0Q�63     5      $ 0:0:0.5��6'   $	 250Q�69   5      U� 5      $U O(UIElement.RenderTransform).(TransformGroup.Children)[3].(TranslateTransform.Y)�6,   $ GridMain��6�   .\�5        $ 0��6'   $ 0Q�63     5      $ 0:0:0.5��6'   $ 50Q�69   5      U� 5      $U O(UIElement.RenderTransform).(TransformGroup.Children)[3].(TranslateTransform.X)�6,   $ StackPanelMenu��6�   .\�5        $ 0��6'   $ 0Q�63     5      $ 0:0:0.5��6'   $	 250Q�69   5      U� 5      $U O(UIElement.RenderTransform).(TransformGroup.Children)[3].(TranslateTransform.X)�6,   $ 
ManualGame��6�   .\�5        $ 0��6'   $
 -250Q�63     5      $ 0:0:0.5��6'   $
 -250Q�69     5      $ 0:0:0.7��6'   $ 0Q�69   5      U� 5       $U O(UIElement.RenderTransform).(TransformGroup.Children)[3].(TranslateTransform.X)�6,   $ AssistedGame��6�   .\�5!        $ 0��6'   $
 -250Q�63     5"      $ 0:0:0.5��6'   $
 -250Q�69     5#      $ 0:0:0.9��6'   $ 0Q�69   5$      U� 5%      $U O(UIElement.RenderTransform).(TransformGroup.Children)[3].(TranslateTransform.X)�6,   $ Tutorial��6�   .\�5&        $ 0��6'   $
 -250Q�63     5'      $ 0:0:0.5��6'   $
 -250Q�69     5(      $ 0:0:1.1��6'   $ 0Q�69   5)      U� 5*      $U O(UIElement.RenderTransform).(TransformGroup.Children)[3].(TranslateTransform.X)�6,   $ Logout��6�   .\�5+        $ 0��6'   $
 -250Q�63     5,      $ 0:0:0.5��6'   $
 -250Q�69     5-      $ 0:0:1.1��6'   $ 0Q�69   5.      U� 5/      $U O(UIElement.RenderTransform).(TransformGroup.Children)[3].(TranslateTransform.X)�6,   $ DeleteAccount��6�   .\�50        $ 0��6'   $
 -250Q�63     51      $ 0:0:0.5��6'   $
 -250Q�69     52      $ 0:0:1.3��6'   $ 0Q�69   53      U� 54      $V P(UIElement.RenderTransform).(TransformGroup.Children)[0].(ScaleTransform.ScaleX)�6,   $ button��6�   .\�55        $ 0��6'   $ 0Q�63     56      $ 0:0:0.5��6'   $ 0Q�69     57      $ 0:0:1.5��6'   $ 1Q�69   58      U� 59      $V P(UIElement.RenderTransform).(TransformGroup.Children)[0].(ScaleTransform.ScaleY)�6,   $ button��6�   .\�5:        $ 0��6'   $ 0Q�63     5;      $ 0:0:0.5��6'   $ 0Q�69     5<      $ 0:0:1.5��6'   $ 1Q�69   5=      5>      �� 5?   
   .��5@      U� $U O(UIElement.RenderTransform).(TransformGroup.Children)[3].(TranslateTransform.X)�6,   $ GridMain��6�   .\�5A        $ 0��6'   $	 250Q�63     5B      $ 0:0:0.5��6'   $ 0Q�69   5C      U� 5D      $U O(UIElement.RenderTransform).(TransformGroup.Children)[3].(TranslateTransform.Y)�6,   $ GridMain��6�   .\�5E        $ 0��6'   $ 50Q�63     5F      $ 0:0:0.5��6'   $ 0Q�69   5G      U� 5H      $U O(UIElement.RenderTransform).(TransformGroup.Children)[3].(TranslateTransform.X)�6,   $ StackPanelMenu��6�   .\�5I        $ 0��6'   $	 250Q�63     5J      $ 0:0:0.5��6'   $ 0Q�69   5K      5L      5M       � Triggers 5N      :� 5O   
    :� RoutedEvent$ ButtonBase.Click��6    :� 
SourceName$ ButtonOpenMenu��66   .[�5P      �� #��[  6   5Q      :� 5R   
   $ ButtonBase.Click��6   $ ButtonCloseMenu��66   .[�5S      �� -   5         ��Name$ CloseMenu_BeginStoryboard��5S      #��[ 6A   5T      .��5X      � ���	#FF3580BF.+�5Z   
   �� -   5        	 ��Name$	 StackPanelMenu��5Z      $	��250��6-   $
��Left=�69   $��
-250 0 0 0q�6T   
 J� RenderTransformOrigin$
 0.5,0.5&�6h   z�5[      V� 5\      .~�5]      �� �� 5^      �� 5_      T� 5`      5a      .+�5c      ��  �� Orientation$ 
Horizontal=�6   $	��Top=�62   $	��100��6J   $��Center=�6W   .+�5d      ��  0 ,MaterialDesignFloatingActionMiniAccentButton#��[ 6>   $��0q�6�   $��50��6�   $��50��6�   $��10q�6�   ��6   �� *UpdateUserProfil+��6�   Z� ��6�   Z� .��5e      '  !MaterialDesignThemes.Wpf.PackIcon  �  �ControlzEx.PackIconBase`1[[MaterialDesignThemes.Wpf.PackIconKind, MaterialDesignThemes.Wpf, Version=2.6.0.325, Culture=neutral, PublicKeyToken=null]]   Kind$ Settings=�6.   
�������$��40��6Q   $��40��6\   5f      �� 5g      -   5         ��Name$ button��5g      #��[ 6)   $��0q�6�   $��80��6�   $��80��6�   $��10q�6�   $
 0.5,0.5&�6�   ��6o   Z� z�5h      V� 5i      .~�5j      �� �� 5k      �� 5l      T� 5m      5n      ��5p      �� 5q       j� Stretch$ UniformToFill=�6w    �� ImageSource 6&   �� *Source+ �� Mode$ OneWay=� �� UpdateSourceTrigger$ PropertyChanged=�6w   5s      �� 5t      #��[ 6<   $��0q�6�   $��50��6�   $��50��6�   $��10q�6�   ��6   �� *ShowUserProfil+��6�   Z� ��6�   Z� .��5u        $ InformationOutline=�6.   
�������$��40��6[   $��40��6f   5v      5w      �� 5x      .@�5y      �� -   5         ��Name$ 
ManualGame��5y      $��60��63   $
 0.5,0.5&�6?   z�5z      V� 5{      .~�5|      �� �� 5}      �� 5~      T� 5      5�      .��5�      �� $��0 0 0 0q�68   $	��230��6I   $
��Auto��6U   
������ 
������ ��6   �� *Create+.��5�      �� $ 
Horizontal=�6%   $��	-25 0 0 0q�6>   .+�5�        $ Create=�66   $��30��6D   $��30��6O   $
��Left=�6[   $��Center=�6v   $��5q�6�   �� 5�      $��Créer un jeu manuel��6(   $��10q�6C   $��Center=�6O   5�      5�      5�      �� 5�      -   5        $ AssistedGame��5�      $��60��65   $
 0.5,0.5&�6A   z�5�      V� 5�      .~�5�      �� �� 5�      �� 5�      T� 5�      5�      .��5�      �� $��0 0 0 0q�6@   $	��230��6Q   $
��Auto��6]   
������ 
������ ��6   �� *CreateAssisted+.��5�      �� $ 
Horizontal=�6%   $��	-30 0 0 0q�6>   .+�5�        $ CreateOutline=�66   $��30��6K   $��30��6V   $
��Left=�6b   $��Center=�6}   $��5q�6�   �� 5�      $��Créer un jeu assisté��6(   $��10q�6D   $��Center=�6P   5�      5�      5�      �� 5�      -   5        $ Tutorial��5�      $��60��61   $
 0.5,0.5&�6=   z�5�      V� 5�      .~�5�      �� �� 5�      �� 5�      T� 5�      5�      .��5�      �� -   5        $��0 0 0 0q�5�      $	��230��6J   $
��Auto��6V   
������ 
������ .��5�      �� $ 
Horizontal=�6%   $��
-105 0 0 0q�6>   .+�5�        $ 
PhotoAlbum=�66   $��30��6H   $��30��6S   $
��Left=�6_   $��Center=�6z   $��5q�6�   �� 5�      $��Tutorial��6(   $��10q�68   $��Center=�6D   5�      5�      5�      �� 5�      -   5        $ Logout��5�      $��60��6/   $
 0.5,0.5&�6;   $	��217��6[   z�5�      V� 5�      .~�5�      �� �� 5�      �� 5�      T� 5�      5�      .��5�      �� $��0 0 0 0q�68   $	��230��6I   $
��Auto��6U   
������ 
������ ��6   �� *LogOut+.��5�      �� $ 
Horizontal=�6%   $��
-105 0 0 0q�6>   .+�5�        $ LogoutVariant=�66   $��30��6K   $��30��6V   $
��Left=�6b   $��Center=�6}   $��5q�6�   �� 5�      $��Déconnecter��6(   $��10q�6;   $��Center=�6G   5�      5�      5�      �� 5�      -	   5        $ DeleteAccount��5�      $��60��66   $
 0.5,0.5&�6B   z�5�      V� 5�      .~�5�      �� �� 5�      �� 5�      T� 5�      5�      .��5�      �� -
   5        $��0 0 0 0q�5�   2   $	��230��6C   $
��Auto��6O   
������ 
������ .��5�      �� $ 
Horizontal=�6%   $��	-35 0 0 0q�6>   .+�5�        $ Trash=�66   $��30��6C   $��30��6N   $
��Left=�6Z   $��Center=�6u   $��5q�6�   �� 5�      $��Supprimer compte��6(   $��10q�6@   $��Center=�6L   5�      5�      5�      5�      5�      � 5�   
   -   5         �Name$ GridMain��5�   #   
�������$
��Auto��65   $
 0.5,0.5&�6C   z�5�      V� 5�      .~�5�      �� �� 5�      �� 5�      T� 5�      5�       � ColumnDefinitions 5�      �� 5�      $��2* �6#   �� 5�      $��* �6#   .+�5�      � $��1��6    � RowDefinitions 5�      �� 5�      $��* �6$   �� 5�      $��20 �6$   .+�5�      � -   5        $ MyChat��5�   $   $��0��6   6$   � 5�      $��1��6   
������ .+�5�      �� $��Dissocier Chat��6=   $��8(�6V   $
��Auto��6c   $
��Auto��6q   ���	#FF2B7EBF��6   �� *ExtractChat+��5�      �� 5�       �� EndPoint$ 0.5,1&�62    �� 
StartPoint$ 0.5,0&�6C   .��5�   "   �  � Color$ 	#FF1B3F5C��6/    � Offset$ 0Q�6A   � 5�   "   $ 	#FF4688BC��6/   $ 1Q�6A   5�      5�      5�      5�      � 5�      $��0��6    5�      �� 5�      $��50 �6$   �� 5�      $��* �6$   �� 5�      $��20 �6$   .+�5�      �� -   5        $ ButtonCloseMenu��5�      #��[ 62   $��30��6x   $��30��6�   $��0q�6�   $��Center=�6�   $
��Left=�6�   $��5q�6�   $y�	Collapsed=�6'  ��6�   Z� ��6�   Z� .��5�        $
 Menu=�6.   ���	#FF3580BF6:   5�      �� 5�      -   5        $ ButtonOpenMenu��5�      #��[ 61   $��30��6w   $��30��6�   $��0q�6�   $��Center=�6�   $
��Left=�6�   $��5q�6�   ��6�   Z� ��6�   Z� .��5�        $
 Menu=�6.   ���	#FF3580BF6:   5�      � 5     -   5        $ 
MiddleView��5  $   $��1��6   6$   � 5     $��2��6   
�������6$   �� 5     $ 
Horizontal=�6   $	��Top=�66   $��40��6N   $��Right=�6Z   $��10q�6v   .+�5     �� -   5        $ ButtonClose��5     #��[ 62   ���	#FF3580BF$��0 0 -5 0q�6�   $��30��6�   $��30��6�   $��0q�6�   ��6�   Z� ��6�   Z� .��5       $ Power=�62   5     5     5	     5
     5     5     