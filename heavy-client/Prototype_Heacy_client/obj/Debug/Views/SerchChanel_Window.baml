   M S B A M L   `   `   `  ����Q  MPrototype_Heacy_client, Version=1.0.0.0, Culture=neutral, PublicKeyToken=nullK*clr-namespace:Prototype_Heacy_client.ViewsPrototype_Heacy_client.Views  5   	   5    /Prototype_Heacy_client.Views.SerchChanel_Window   5   	   R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    5   	   R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5   	   @d2http://schemas.microsoft.com/expression/blend/2008     5   	   Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5   	   <local*clr-namespace:Prototype_Heacy_client.Views     5   	   -   5          � WindowStartupLocation$  CenterScreen=�5   	    � Title$ SerchChanel_Window��5	   	   $	��530��6$   $	��800��61   .��5      �  � RowDefinitions 5   
   �� 5      $	��140 �6   �� 5      $��* �6   �� 5      $��50 �6   .+�5   
   �� $��30q�6   $��0��6!   .+�5      �� $��18(�6   $��Saisir le nom du canal:: ��6"   
�������6F   � 5       � ColumnDefinitions 5      �� 5      $��5* �6'   �� 5      $��1* �6'   �� 5      $��2* �6'   .+�5!      �� 
�������$
��0,10q�61   $��1q�6?   $��0��6S   ._�5"      �� $
��10,0q�6   $��Champagne & Limousines)�6|   $��18(�6�   ��6,   �� *ChannelName+ �� UpdateSourceTrigger$ PropertyChanged=� �� Mode$ TwoWay=�.��6�    J� InputBindings 5#      �� 5$      
 �� Key$ Enter��6)    �� Command 65   �� *SerchChannel+$ PropertyChanged=�.��5%   1   5&      5'      �� 5(      $��Chercher��6   $��10,10,159,11q�6N   
�������$��1��6w   $��2��6�   ��6,   �� *SerchChannel+6�   �� 5)      $��Choisir un canal��6   $��29,10,21,11q�6W   
�������$��2��6   ��64   �� *SelectChannel+6   5+      5-      � 5.   
   $��1��6   .+�5/      �� $
��20 0q�6   $��18(�6"   
���  ����60   �� *	ErrorText+$ PropertyChanged=�$ TwoWay=�6�   �� 51      $
��Auto=�6   $��20q�6A   .��53      �� -   5        	 ��Name$	 MyStack��53      55      56      58      �� 59   
   $��2��6   $��Fermer��6   $��0 0 40 0q�6I   $��Right=�6[   
���  ��
���  ��
�������$��1��6�   ��6/   �� *Close+6�   5<      5?      