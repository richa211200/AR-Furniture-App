# AR-Furniture-App
Using Android ARCore - For best user experience of online furniture shopping !

# What's Sceneview library?
Built on top of sceneform and we don't need to add openGL to render images while using sceneview

## What's Sceneform library?
  - This lib provides essential functionality to build augmented reality scenes in Android apps
  - Includes features like - rendering 3D models, handling user interactions, managing AR session
  - Uses - Create and manipulate 3D objects, apply materials and textures, handle gestrues and touch input
  - lib contains in-build components associated with 3D graphics and low-level graphics programming

# Setup enironment of AR Android project

## 1. Add ARCore libraries
Add these dependencies to your app's `build.gradle` file:

```gradle
  - ARCore library for augmented reality functionality
      implementation("com.google.ar.core::1.33.0")
    
  - Sceneform core library for building AR scenes
      implementation("com.google.ar.sceneform:core:1.17.1")
    
  - Sceneform UX library for additional user experience features in AR scenes
      implementation("com.google.ar.sceneform.ux:sceneform-ux:1.17.1")

  - Sceneform animation library for adding animations to AR scenes
      implementation("com.google.ar.sceneform:animation:1.17.1")
```

## 2. Andriod Manifest permission & features
  - Add camera permission
    ```gradle
       <uses-feature
        android:name="android.hardware.camera"
        android:required="true" />
       <uses-permission android:name="android.permission.CAMERA"/>
    ```
  - Add OpenGL version
    ```gradle
       <uses-feature android:glEsVersion="0x00030000" android:required="true"/>
    ```
  - Add hardware ar feature
    ```gradle
       <uses-feature android:name="android.hardware.camera.ar"/>
    ```

## 3. Add AR related meta data for application
  - Add the following metadata tag in the `<application>` section of your AndroidManifest.xml file:
  - ```manifest.xml
    <meta-data android:name="com.google.ar.core" android:value="required" />
    ```

## 4. layout
  - make sure to add tag of ARCore sceneform ux
  - Ex:
    ```xml
    android:name="com.google.ar.sceneform.ux.ArFragment"
    ```


