   M S B A M L   `   `   `  ����Q  MPrototype_Heacy_client, Version=1.0.0.0, Culture=neutral, PublicKeyToken=nulle7clr-namespace:Prototype_Heacy_client.Views.UserControls)Prototype_Heacy_client.Views.UserControls  5      �Eclr-namespace:Prototype_Heacy_client.ViewModels.UserControl_ViewMoels7Prototype_Heacy_client.ViewModels.UserControl_ViewMoels  5      P    JPrototype_Heacy_client.Views.UserControls.UserControl_GameCreationManual_1   5      R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    5      R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5      Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5      @d2http://schemas.microsoft.com/expression/blend/2008     5      Ilocal7clr-namespace:Prototype_Heacy_client.Views.UserControls     5      [	viewModelEclr-namespace:Prototype_Heacy_client.ViewModels.UserControl_ViewMoels     5      .��5      � .+�5   
   �� .+�5      � .+�5      �� 
�������$��
10 12 10 6q�61   $��1q�6F   ._�5      �� $	��top=�6!   $��15q�69   .+�5      �� $��0��6    $��Nom du jeu : ��60   $
��Bold"�6H   $
��left=�6Z   $	��Top=�6u   
�������$��16(�6�   �� 5      $��1��6!   
�������$
��0,10q�6I   $��1q�6W   ._�5      �� $
��10,0q�6&    %  !MaterialDesignFloatingHintTextBox#��[  6~   
�������$��Champagne & Limousines)�6�   $��16(�6�   ��65   �� *Name+  �� Mode$  TwoWay=� �� UpdateSourceTrigger$ PropertyChanged=�6�   5      �� 5      $��0��6    $��Expression : ��60   $
��Bold"�6H   $
��left=�6Z   $	��Top=�6u   
�������$��16(�6�   �� 5      $��1��6!   
�������$
��0,10q�6I   $��1q�6W   ._�5      �� $
��10,0q�6&   #��[  6�   
�������$��Champagne & Limousines)�6�   $��16(�6�   ��65   �� *
Expression+$  TwoWay=�$ PropertyChanged=�6�   5      5      5      5      � 5!      .+�5"      �� 
�������$
��10 6q�61   $��1q�6@   ._�5#      �� $��15q�6!   .+�5$      � .+�5%      �� $��15q�6)   .+�5&   "   �� $�� 0q�6(   $��Dificulté : ��64   $
��Bold"�6K   $
��left=�6]   $	��Top=�6x   
�������$��16(�6�   �� 5(   "    �� Orientation$ 
Horizontal=�6.   $��Center=�6G   .+�5)   &   	� -   5        $��	Easy_name��5.   !    d� 	IsChecked$
 trueY�5*   !   $��16(�5+   !   
�������$
��20 0q�5-   !   $��FACILE��5/   !   ��50   !   �� *Radio+��51   !   ��  �� ElementName$ 	Easy_name�� �� Path$ Content�6d   	� 52   &   -   5        $��Intermediate_name��55   !   $��16(�52   3   
�������$
��20 0q�54   !   $��INTERMÉDIAIRE��56   !   ��57   !   �� *Radio+��58   "   �� $ Intermediate_name��$ Content�6m   	� 59   &   -   5        $��	Hard_name��5<   !   $��16(�59   3   
�������$
��20 0q�5;   !   $��	DIFFICILE��5=   !   ��5>   !   �� *Radio+��5?   "   �� $ 	Hard_name��$ Content�6e   5@   #   5A      5C      � 5D      .+�5E      �� $��15q�6)   .+�5F   "   �� $�� 0q�6(   $��Mode : ��64   $
��Bold"�6F   $
��left=�6X   $	��Top=�6s   
�������$��16(�6�   �� 5H   "   $ 
Horizontal=�6.   $��Center=�6G   .+�5I   &   	� -   5        $��normal��5N   !   $
 trueY�5J   !   $��16(�5K   !   
�������$
��10 0q�5M   !   $��NORMAL��5O   !   ��5P   !   �� *	RadioMode+��5Q   !   �� $ normal��$ Content�6a   	� 5R   &   -   5        $��random��5U   !   $��16(�5R   3   
�������$
��10 0q�5T   !   $��
ALÉATOIRE��5V   !   ��5W   !   �� *	RadioMode+��5X   "   �� $ random��$ Content�6b   	� 5Y   &   -   5        $��	panoramic��5\   !   $��16(�5Y   3   
�������$
��10 0q�5[   !   $��PANORAMIQUE��5]   !   ��5^   !   �� *	RadioMode+��5_   "   �� $ 	panoramic��$ Content�6e   	� 5`   &   -   5        $��centred��5c   !   $��16(�5`   3   
�������$
��10 0q�5b   !   $��CENTRÉ��5d   !   ��5e   !   �� *	RadioMode+��5f   "   �� $ centred��$ Content�6c   5g   #   5h      5j      5k      5m      5n      � 5p      .+�5q      �� 
�������$
��10 6q�61   $��1q�6@   ._�5s      �� $��15q�6!   .+�5t      �  � ColumnDefinitions 5u      �� 5v   "   $��3* �63   �� 5w   "   $��* �63   .+�5y      �� $�� 0q�6$   $��0��60   $
��Bold"�6@   $��
Astuces : ��6R   $
��left=�6g   $	��Top=�6�   
�������$��16(�6�   �� 5{      -   5        $��1��5{   %   $
��Bold"�6K   
�������$��+��6p   $��Right=�6}   $��20(�6�   $
��10 0q�6�   6�   5}      � 5�      $	��300��6   .+�5�      �� $
��Auto=�6,   $��20q�6Q   .��5�   "   �� -	   5         ��Name$ 	HintStack��5�   -   5�   #   5�      5�      5�      5�      5�      5�      �� 5�   
   $��Bottom=�6   .+�5�      � .+�5�      �� $��18(�6   $��Center=�6&   $
��Bold"�6�   
���  ����6C   �� *Error+$ PropertyChanged=�$  TwoWay=� J� Effect 5�      -  'System.Windows.Media.Effects.BlurEffect  5�      	   Radius$		 4.0Q�5�      
   
KernelType$	
 Box=�5�      5�      �� 5�      $��18(�6   $��Center=�6&   $
��Bold"�6�   
���������6C   �� *Error+$ PropertyChanged=�$  TwoWay=�6�   5�      �� 5�      $��
20 7 20 14q�6   $��Créer un jeu��6H   $��16(�6_   $��Bottom=�6m   $
��Bold"�6�   
���������6)   �� *
CreateGame+6�   5�      5�      5�      