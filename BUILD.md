# Build Umbrella

## Requirements

- Android Studio, fairly recent (version 2.2+, because of Gradle wrapper)
- SDK24 which we build against
- Android device with SDK 10 (IceCream Sandwich, 4.0.3) or above

## Building the Source Code

1\) Download and install [Android Studio](http://developer.android.com/sdk/index.html).

2\) Launch Android Studio and select **Get from Version Control** from the Welcome Screen.

![](/building/02.png)

3\) Another screen pops up. Ensure that **Version control** is set to **Git**. Enter **https://github.com/securityfirst/Umbrella_android.git** as the **URL**, then click **Clone**.

![](/building/04.png)

This process may take a bit, and you should see a dialog similar to this.

![](/building/04-2.png)

4\) When the source code has downloaded, components will be loaded and the project will open in an Android Studio workspace.

5\) Wait for Gradle to sync. 

You'll need to be connected to the internet for this. If the process fails at any point, then select **Sync project with Gradle Files** from the **File** menu to restart the sync process. 

![](/building/06.png)

It takes several minutes to download, install, verify and configure the required packages and tools. 

6\) If you want the development build, navigate to **VCS** > **Git** > **Branches**

![](/building/06-1.png)

Select the `develop` branch and **Checkout**

![](/building/06-2.png)

7\) When Gradle sync is complete, select **Build** > **Build Bundle(s)/APK(s)** > **Build APK(s)** from the Android Studio menu.

![](/building/7.png)

8\) When the build process finishes, click **locate** from the Event Log to open the folder containing the APK file, **app-debug.apk**. You can [sideload](http://www.digitalcitizen.life/how-sideload-apps-using-apk-files-android-devices) this file onto any Android device.

![](/building/8.png)

## Updating

9\) Occasionally, you should check for updates. To get the latest version of the source code, select **VCS** > **Update Project**.

![](/building/09.png)

Alternatively, you can also use the **Update Project** button on the toolbar:

![](/building/09-2.png)

10\) Choose whether to "Merge incoming changes into the current branch" or to "Rebase the current branch on top of incoming changes" and click **OK** to proceed.

![](/building/10.png)

11\) Repeat steps 7 and 8 to build and install the updated app.
