   M S B A M L   `   `   `  ����Q  MPrototype_Heacy_client, Version=1.0.0.0, Culture=neutral, PublicKeyToken=nullK*clr-namespace:Prototype_Heacy_client.ViewsPrototype_Heacy_client.Views  5   	   6    0Prototype_Heacy_client.Views.GroupCreationWindow   5   	   R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    5   	   R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5   	   @d2http://schemas.microsoft.com/expression/blend/2008     5   	   Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5   	   <local*clr-namespace:Prototype_Heacy_client.Views     5   	     � WindowStartupLocation$  CenterScreen=�5   	    � Title$ GroupeCreationWindow��5	   	   $	��250��6&   $	��800��63    � AllowsTransparency .  � WindowStyle$
 None=�6Y   $��1q�6m   
���   �.��5      �� $��30q�6   .+�5   
   � .+�5      �� -   5        ���	#FFD1D1D1$��-30,-30,-30,0q�5   L    J� RenderTransformOrigin$ 0.5,0.5&�6c   z�5      V� 5      .~�5      ��  �� ScaleY$ -1Q�6)   �� 5      �� 5      T� 5      5      5      5      �� 5   
   $��18(�6   $��Enter the group name: ��6   
�������6?   �� 5   
   
�������$
��0,10q�6)   $��1q�67   ._�5      �� -   5         ��Name$ GroupNameBox��5      $
��10,0q�6,   $��Champagne & Limousines)�6�   $��18(�6�    �� 	MaxLength$ 20��6�   ��6:   �� *	GroupName+ �� UpdateSourceTrigger$ PropertyChanged=�	 �� Mode$	 TwoWay=�.��6�   
 J� InputBindings
 5      �� 5      
 �� Key$ Enter��6!    �� Command 6-   �� *CreateGroup+$ PropertyChanged=�.��5    )   5!      5"      �� 5$   
   $��18(�6   
���  ����6   �� *	ErrorText+$ PropertyChanged=�$	 TwoWay=�6n   �� 5%   
   $��18(�6   
��� � ���6   �� *	Created+$ PropertyChanged=�$	 TwoWay=�6l   �� 5&   
    �� Orientation$ 
Horizontal=�6   $��Right=�6.   .+�5'      �� $��Fermer��6   $��Right=�6F   
���  ��
���  ��$��10q�6�   
�������$��1��6�   ��6&   �� *CloseWindow+6�   �� 5)      $��Créer��6   $��Right=�6E   $��10q�6a   
�������$��1��6�   ��6%   �� *CreateGroup+6�   5+      5,      5-      