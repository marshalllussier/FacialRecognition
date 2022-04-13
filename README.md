# Facial Recognition 

Spring 2022 semester group roster:
<ul>
  <li>Marshall Lussier</li>
  <li>Kamal Allouzi</li>
  <li>Dianne Pham</li>
  <li>Nick Gavin</li>
  <li>Hong Shi</li>
</ul>

## Progress made:
As of 4/13/2022, the app currently opens and requests necessary permissions. Once permissions are granted, it will then begin scanning the frame of the camera for any detected human faces. If any faces are detected, it will draw a box or contour points on the face, depending on the developer settings. It also gets the image into the proper format to be passed into TensorFlow Lite to be scanned for facial recognition.

### Known bugs:
<ul>
  <li>"Contact Support" button crashes on AutoZone device, but not on other Android devices.</li>
  <li>"Logout" button works but the layout needs to be refreshed in order to make the menu items reappear.</li>
</ul>

### What's left:
<ul>
  <li>Setup a page to create a profile (This should only be accessible once the user has logged in with credentials AND if they don't have an existing profile)</li>
  <li>Setup a page to edit/delete a profile (This should only be accessible once the user is logged in AND if they do have an existing profile</li>
  
  **Note**: These both can be created on the fragment_settings page and have different display behaviour based on the conditions.
  <li>Successfully pass detected faces into TFLite model to retrieve the highest confidence score and make a guess on who the detected user is</li>
</ul>


### How to build project: 
1)	Download and install Android Studio. [Link](https://developer.android.com/studio)
![image](https://user-images.githubusercontent.com/77768342/163279738-621ef59d-96e5-45d1-adae-38f5fa4f4c9b.png)

2) After installing, click "Project from Version Control".
![image](https://user-images.githubusercontent.com/77768342/163279785-c461972e-1df3-4353-84da-eb62bf1ef32b.png)

3) Insert link below into "URL:__" and click "Clone" https://github.com/marshalllussier/FacialRecognition.git
![image](https://user-images.githubusercontent.com/77768342/163279817-694179a5-1041-4f45-a24c-3c4df58c4061.png)

4) Afterwards, a notification will appear like this. Click "Trust Project"
![image](https://user-images.githubusercontent.com/77768342/163279865-e0c80657-e980-4d41-8ffa-28a63c826843.png)

5) On the bottom right, there will be a progress bar. Use this to keep track of the cloning and wait for it to finish. 
![image](https://user-images.githubusercontent.com/77768342/163279896-16fe1847-47f1-4612-a056-00ed7e4461cb.png)

6) Once the cloning is complete, go to the upper left corner and hover over the "Build" tab. Click on "Make Project".
![image](https://user-images.githubusercontent.com/77768342/163279935-54c947a0-0646-4bce-966e-7d4119feaf8b.png)

7)	The build terminal can be seen by clicking on "Build" near the bottom left. This can be used to determine when the project is done building.
![image](https://user-images.githubusercontent.com/77768342/163279968-37566d27-686e-4ade-ad98-991898272650.png)


### How to demo project:
Set up a virtual device (NOTE: Virtualization must be enabled on your BIOS for this option to work)
OR
Connect a physical android device.

1) Go to the Settings section of an Android phone and click on "About Phone" to display the "Build Number" as shown below.
![image](https://user-images.githubusercontent.com/77768342/163280132-f8525517-3743-483e-acf3-c10f4fa81f87.png)

2) Keep clicking build number until this notification shows.
![image](https://user-images.githubusercontent.com/77768342/163280156-0c2bed57-14c0-4fdd-9ff2-9563aad4f1e2.png)

3) Go back to Settings, then go to the "System" section and click on "Developer Options".
![image](https://user-images.githubusercontent.com/77768342/163280212-6954e9ef-9a01-4916-85d1-dd2dd7979e40.png)

4)	Scroll down until you see USB debugging, turn it on and plug Android into PC then click "Always allow from this computer" and OK
![image](https://user-images.githubusercontent.com/77768342/163280246-9296d9c3-72c1-40af-9149-76ab1e9c9aca.png)
![image](https://user-images.githubusercontent.com/77768342/163280260-dbad7c67-dc42-4429-8746-1ed577507abc.png)

5)	Go back to Android Studio and you should see the device on the top right displayed like the picture below. Click the play/run button.
![image](https://user-images.githubusercontent.com/77768342/163280292-fde7005f-be2e-47c9-a487-13d5a90276a4.png)

6)	The app will run on the Android Device.
