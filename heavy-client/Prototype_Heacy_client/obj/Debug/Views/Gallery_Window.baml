   M S B A M L   `   `   `  ����Q  MPrototype_Heacy_client, Version=1.0.0.0, Culture=neutral, PublicKeyToken=nullK*clr-namespace:Prototype_Heacy_client.ViewsPrototype_Heacy_client.Views  5   	   1    +Prototype_Heacy_client.Views.Gallery_Window   5   	   R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    5   	   R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5   	   @d2http://schemas.microsoft.com/expression/blend/2008     5   	   Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5   	   <local*clr-namespace:Prototype_Heacy_client.Views     5   	     � Title$  Gallery_Window��5   	   $	��450��6    $	��400��6-    � WindowStartupLocation$ CenterScreen=�69   .��5	      �  � RowDefinitions 5   
   �� 5      $��* �6   .+�5   
   � $��1��6    5      �� 5      $��* �6    �� 5      $��* �6    �� 5      $��* �6     � ColumnDefinitions 5      �� 5      $��* �6#   �� 5      $��* �6#   �� 5      $��* �6#   .+�5      �� $��0��6   $��0��6"   $
��Pic1��6[   $
��Auto��6�   $��3q�6�   ��62   �� *ChooseProfileCommand+��6s   Z� ��5      �� 5      -   5        Pic1�� �� ImageSource$ /Assets/profile_1.jpg��5   /   5!      �� 5#      $��0��6   $��1��6"   $
��Pic2��6[   $��0q�6�   $
��Auto��6�   $��3q�6�   ��62   �� *ChooseProfileCommand+��6s   Z� ��5$      �� 5%      -   5        Pic2��$ /Assets/profile_2.jpg��5%   /   5'      �� 5)      $��0��6   $��2��6"   $
��Pic3��6[   $��0q�6�   $
��Auto��6�   $��3q�6�   ��62   �� *ChooseProfileCommand+��6s   Z� ��5*      �� 5+      -   5        Pic3��$ /Assets/profile_3.jpg��5+   /   5-      �� 5/      $��1��6   $��0��6"   $
��Pic4��6[   $��0q�6�   $
��Auto��6�   $��3q�6�   ��62   �� *ChooseProfileCommand+��6s   Z� ��50      �� 51      -   5        Pic4��$ /Assets/profile_4.jpg��51   /   53      �� 55      $��1��6   $��1��6"   $
��Pic5��6[   $��0q�6�   $
��Auto��6�   $��3q�6�   ��62   �� *ChooseProfileCommand+��6s   Z� ��56      �� 57      -   5        Pic5��$ /Assets/profile_5.jpg��57   /   59      �� 5;      $��1��6   $��2��6"   $
��Pic6��6[   $��0q�6�   $
��Auto��6�   $��3q�6�   ��62   �� *ChooseProfileCommand+��6s   Z� ��5<      �� 5=      -   5        Pic6��$ /Assets/profile_6.jpg��5=   /   5?      �� 5A      $��2��6   $��0��6"   $
��Pic7��6\   $��0q�6�   $
��Auto��6�   $��3q�6�   ��62   �� *ChooseProfileCommand+��6t   Z� ��5B      �� 5C      -   5        Pic7��$ /Assets/profile_7.jpg��5C   /   5E      �� 5G      $��2��6   $��1��6"   $
��Pic8��6\   $��0q�6�   $
��Auto��6�   $��3q�6�   ��62   �� *ChooseProfileCommand+��6t   Z� ��5H      �� 5I      -   5        Pic8��$ /Assets/profile_8.jpg��5I   /   5K      �� 5L      $��2��6   $��2��6"   $��	Parcourir��6T   $
��Auto��6h   $��3q�6v   ��62   �� *BrowseProfile+6v   5M      5N      5^      