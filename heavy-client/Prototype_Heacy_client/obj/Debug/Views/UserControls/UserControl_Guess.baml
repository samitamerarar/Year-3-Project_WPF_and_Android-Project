   M S B A M L   `   `   `  ����Q  MPrototype_Heacy_client, Version=1.0.0.0, Culture=neutral, PublicKeyToken=nulle7clr-namespace:Prototype_Heacy_client.Views.UserControls)Prototype_Heacy_client.Views.UserControls  5      A    ;Prototype_Heacy_client.Views.UserControls.UserControl_Guess   5      R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    5      R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5      Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5      @d2http://schemas.microsoft.com/expression/blend/2008     5      Ilocal7clr-namespace:Prototype_Heacy_client.Views.UserControls     5      -   5        .��5	      � -   5          �Name$  guess��5	       � RowDefinitions 5   
   �� 5      $	��13* �6   �� 5      $��* �6   .+�5   
   � -   5        $��PassiveView��5      $��0��6   61   �� 5   
   $��1��6   $��5 5 5 5q�6   ._�5      � -   5        $��buttons��5       � ColumnDefinitions 5      �� 5      $��2* �6'   �� 5      $��7* �6'   �� 5      $��2* �6'   .+�5      �� -   5        $��indice��5      $��0��6<   $��Demande indice��6l   $
��Auto��6�   
����������	#FF2E7FC3���	#FF2E7FC3$��16(�6�   ��6L   �� *ClueCommand+6�   �� 5      $��1��6   
�������$��1q�6<   $��2 2 2 2q�6P    �� CornerRadius$ 2 2 2 2��6a   ._�5      �� -   5         ��Name$ 	GuessWord��5      $��16(�6�   $��10.2,-0.8,9.6,-0.8q�6�    �� 
CaretBrush �	#FF207E83
���������61   �� *WordToGuess+ �� UpdateSourceTrigger$ PropertyChanged=� �� Mode$ TwoWay=�.��6�    J� InputBindings 5      �� 5      
	 �� Key$	 Enter��6)   
 �� Command
 65   �� *GuessCommand+$ PropertyChanged=�.��5   1   5      5       �� 5!      -   5         ��Name$ GuessButton��5!   C   $��2��6   $��	Répondre��6y   
����������	#FF2E7FC3���	#FF2E7FC3$
��Auto��6�   $��16(�6�   ��6X   �� *GuessCommand+6�   5"      5#      5$      5%      