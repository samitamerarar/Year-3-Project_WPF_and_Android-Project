   M S B A M L   `   `   `  ����Q  MPrototype_Heacy_client, Version=1.0.0.0, Culture=neutral, PublicKeyToken=nulle7clr-namespace:Prototype_Heacy_client.Views.UserControls)Prototype_Heacy_client.Views.UserControls  5      V    PPrototype_Heacy_client.Views.UserControls.UserControl_GameCreationAssisted_step2   5      R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    5      R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5      Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5      @d2http://schemas.microsoft.com/expression/blend/2008     5      Ilocal7clr-namespace:Prototype_Heacy_client.Views.UserControls     5      .��5
      � .+�5   
   �� .+�5      � .+�5      �� 
�������$
��10 6q�61   $��1q�6@   ._�5      � -   5          �Name$  Image��5   '   $��10q�6   6'   5      5      � 5      .+�5      �� 
�������$��
10 12 10 6q�61   $��1q�6F   ._�5      �� $	��top=�6!   $��15q�69   .+�5      �� $��0��6    $��Options de potrace��60   $
��Bold"�6M   $��Center=�6_   $	��Top=�6|   
�������$��16(�6�   �� 5      $��0��6    $'��!Taille de suppression de tache : ��60   $
��Bold"�6\   $
��left=�6n   $	��Top=�6�   
�������$��16(�6�   � 5      $��5q�6    � ColumnDefinitions 5      �� 5   "   $	��10* �63   �� 5   "   $��* �63   .+�5      �� $��0��6&    �� IsSnapToTickEnabled . $��100 10q�6�    � Maximum$ 10Q�6�    �� TickPlacement$ BottomRight=�6�    �� TickFrequency$ 1Q�6�    � Value 66   �� *Opt1+ �� Mode$ TwoWay=� �� UpdateSourceTrigger$ PropertyChanged=�6�   �� 5      
�������$
��Bold"�67   $��1��6I   ��6Y   �� *Opt1+$ TwoWay=�$ PropertyChanged=�5       �� 5!      $��0��6    $��Seuil d'angle : ��60   $
��Bold"�6K   $
��left=�6]   $	��Top=�6x   
�������$��16(�6�   � 5"      $��5q�6    5#      �� 5$   "   $	��10* �63   �� 5%   "   $��* �63   .+�5'      �� $��0��6&    . $��100 10q�6�   $ 10Q�6�   $ BottomRight=�6�   $ 1Q�6�    66   �� *Opt2+$ TwoWay=�$ PropertyChanged=�6�   �� 5)      
�������$
��Bold"�67   $��1��6I   ��6Y   �� *Opt2+$ TwoWay=�$ PropertyChanged=�5+      �� 5,      $��0��6    $"��Optimisation de la courbe : ��60   $
��Bold"�6W   $
��left=�6i   $	��Top=�6�   
�������$��16(�6�   � 5-      $��5q�6    5.      �� 5/   "   $	��10* �63   �� 50   "   $��* �63   .+�52      �� $��0��6&    . $��100 10q�6�   $ 10Q�6�   $ BottomRight=�6�   $ 1Q�6�    66   �� *Opt3+$ TwoWay=�$ PropertyChanged=�6�   �� 54      
�������$
��Bold"�67   $��1��6I   ��6Y   �� *Option+$ TwoWay=�$ PropertyChanged=�56      57      58      59      � 5=      .+�5>      �� 
�������$
��10 6q�61   $��1q�6@   ._�5@      �� $��15q�6!   .+�5A      �  5B      �� 5C   "   $��3* �63   �� 5D   "   $��* �63   .+�5F      �� $�� 0q�6$   $��0��60   $
��Bold"�6@   $��
Astuces : ��6R   $
��left=�6g   $	��Top=�6�   
�������$��16(�6�   �� 5H      -   5        $
��hint��5H   h   $��1��6%   $
��Bold"�67   
�������$��+��6\   $��Right=�6�   $��20(�6�   $
��10 0q�6�   6�   5J      � 5L      $	��200��6   .+�5M      �� $
��Auto=�6,   $��20q�6Q   .��5N   "   �� -   5        	 ��Name$	 	HintStack��5N   -   5P   #   5Q      5R      5T      5V      5X      5Z      �� 5[   
   $��Bottom=�6   .+�5\      � .+�5]      �� $��18(�6   $��Center=�6&   $
��Bold"�6�   
���  ����6C   �� *Error+$ PropertyChanged=�$ TwoWay=�
 J� Effect
 5^      -  'System.Windows.Media.Effects.BlurEffect  5_         Radius$	 4.0Q�5`         
KernelType$	 Box=�5a      5c      �� 5d      $��18(�6   $��Center=�6&   $
��Bold"�6�   
���������6C   �� *Error+$ PropertyChanged=�$ TwoWay=�6�   5f      �� 5g       �� Orientation$ 
Horizontal=�6   $��Right=�62   .+�5h      �� -   5        $��previous��5h   b   $��10q�6   $��Précédent��6%   $��16(�69   $��Bottom=�6G   $
��Bold"�6�   
�������
�����_�6�   �� 5i      $��10q�6   $��Voir la simulation��6B   $��16(�6_   $��Bottom=�6m   $
��Bold"�6�   
���������6%   �� *
Simulate+6�   �� 5k      $��10q�6   $��Créer un jeu��6D   $��16(�6[   $��Bottom=�6i   $
��Bold"�6�   
���������6%   �� *
CreateGame+6�   5m      5n      5o      5p      