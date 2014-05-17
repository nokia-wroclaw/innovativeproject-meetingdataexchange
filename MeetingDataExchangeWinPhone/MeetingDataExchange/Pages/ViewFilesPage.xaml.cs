using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using System.IO;
using System.IO.IsolatedStorage;
using System.Windows.Media.Imaging;
using System.Windows.Media;

namespace MeetingDataExchange
{
    public partial class ViewFilesPage : PhoneApplicationPage
    {
       protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            base.OnNavigatedTo(e);
            StackPanel grid = (LayoutRoot.FindName("PhotoViewer") as StackPanel);

            using (IsolatedStorageFile isStore = IsolatedStorageFile.GetUserStoreForApplication())
            {
                if(isStore.DirectoryExists("files"))
                {
                    string[] fileNames = isStore.GetFileNames("files/*.jpg");
                    foreach (string fileName in fileNames)
                    {
    //                    TextBlock block = new TextBlock();
    //                    block.Text = fileName;
    //                    grid.Children.Add(block);

                        Button button = new Button();
                        button.Content = fileName;
                        button.VerticalContentAlignment = System.Windows.VerticalAlignment.Bottom;
                        ImageBrush brush = new ImageBrush();
                        BitmapImage bi = new BitmapImage();
                        bi.SetSource(isStore.OpenFile("files\\"+fileName, FileMode.Open, FileAccess.Read));
                        brush.ImageSource = bi;
                        brush.Stretch = Stretch.Uniform;
                        button.Height = 300;
                        button.Width = 300;
                        button.Background = brush;

                        grid.Children.Add(button);
                    }
                }
            }
        }
        public ViewFilesPage()
        {
            InitializeComponent();
        }
    }
}