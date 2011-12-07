# GUI-Test Checklist

This is the checklist for performing GUI integration tests. We REALLY need to automate these tests!
The [doh robot] (http://dojotoolkit.org/reference-guide/util/dohrobot.html) seems to be a suitable
candidate, as it captures mouse movement as well. (Critical - but not supported by e.g. Selenium).

# Image Annotation - no replies

Repeat tests 2 times: (i) in serverless mode and (ii) with annotation server.

1. Create an annotation and click CANCEL. Confirm the annotation is removed from the GUI.
2. Create an annotation and click OK. Delete the annotation again, and confirm it works as intended.
3. Create an annotation (and click OK to save it). DO NOT use default bounding box size and location!
   * Edit the annotation: change text AND bounding box size/location -> Click CANCEL and confirm it works as intended
   * Edit the annotation: change text AND bounding box size/location -> Click OK and confirm it works as intended
4. Create a second annotation that covers the first. Make sure the first remains clickable. 
   
Compile the project and repeat all tests outside of hosted mode.

# Image Annotation - replies

1. Create an annotation and click CANCEL. Confirm the annotation is removed from the GUI.

# OpenLayers Annotation - no replies

Repeat the same steps as for image annotation - no replies
