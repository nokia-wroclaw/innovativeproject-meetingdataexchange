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
                string[] fileNames = isStore.GetFileNames();
                foreach (string fileName in fileNames)
                {
//                    TextBlock block = new TextBlock();
//                    block.Text = fileName;
//                    grid.Children.Add(block);

                    Button button = new Button();
                    button.Content = fileName;
                    ImageBrush brush = new ImageBrush();
                    BitmapImage bi = new BitmapImage();
                    bi.SetSource(isStore.OpenFile(fileName, FileMode.Open, FileAccess.Read));
                    brush.ImageSource = bi;
                    brush.Stretch = Stretch.Uniform;
                    button.Height = 300;
                    button.Width = 300;
                    button.Background = brush;

                    grid.Children.Add(button);
                }
            }
        }
        public ViewFilesPage()
        {
            InitializeComponent();
        }
    }
}