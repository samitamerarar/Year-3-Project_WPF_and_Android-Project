   M S B A M L   `   `   `  ����Q  MPrototype_Heacy_client, Version=1.0.0.0, Culture=neutral, PublicKeyToken=nullK*clr-namespace:Prototype_Heacy_client.ViewsPrototype_Heacy_client.Views  5   	   8    2Prototype_Heacy_client.Views.ChannelCreationWindow   5   	   R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    5   	   R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5   	   @d2http://schemas.microsoft.com/expression/blend/2008     5   	   Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5   	   <local*clr-namespace:Prototype_Heacy_client.Views     5   	     � WindowStartupLocation$  CenterScreen=�5   	    � Title$ ChannelCreationWindow��5	   	   $	��300��6'   $	��500��64   .��5      �� $��30q�6   .+�5   
   �� $��18(�6   $��Saisir le nom du canal: ��6   
�������6A   �� 5      
�������$
��0,10q�6-   $��1q�6;   ._�5      �� $
��10,0q�6   $��Champagne & Limousines)�6t   $��18(�6�   ��6$   �� *ChannelName+ �� UpdateSourceTrigger$ PropertyChanged=� �� Mode$ TwoWay=�.��6�    J� InputBindings 5      �� 5      
 �� Key$ Enter��6!    �� Command 6-   �� *CreateChannel+$ PropertyChanged=�.��5   )   5      5      �� 5   
   $��18(�6   
���  ����6   �� *	ErrorText+$ PropertyChanged=�$ TwoWay=�6n   �� 5   
   $��18(�6   
��� � ���6   �� *	Created+$ PropertyChanged=�$ TwoWay=�6l   �� 5   
    �� Orientation$ 
Horizontal=�6   $��Right=�6.   .+�5      �� $��Fermer��6   $��Right=�6@   
���  ��$��10q�6m   
�������$��1��6�   ��6&   �� *Close+6�   �� 5      $��Créer��6   $��Right=�6G   $��10q�6c   
�������$��1��6�   ��6%   �� *CreateChannel+6�   5!      5"      5#      