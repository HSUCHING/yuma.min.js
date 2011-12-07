# GUI-Test Checklist

This is the checklist for performing GUI integration tests. We REALLY need to automate these tests!
The [doh robot] (http://dojotoolkit.org/reference-guide/util/dohrobot.html) seems to be a suitable
candidate, as it captures mouse movement as well. (Critical - but not supported by e.g. Selenium).

# Image Annotation - no replies

Repeat tests 2 times: (i) in serverless mode and (ii) with annotation server. In server mode,
refresh the page after each test, to confirm whether client and server remain in sync. 

1. Create an annotation and click CANCEL. Confirm the annotation is removed from the GUI.
2. Create an annotation and click OK. Delete the annotation again, and confirm it works as intended.
3. Create an annotation (and click OK to save it). DO NOT use default bounding box size and location!
   a) Edit the annotation: change text AND bounding box size/location. Click CANCEL and confirm it 
      works as intended.
   b) Edit the annotation: change text AND bounding box size/location. Click OK and confirm it
      works as intended.
4. Create a second annotation that covers the first. Make sure the first remains clickable. 
   
Compile the project and repeat all tests outside of hosted mode.

# OpenLayers Annotation - no replies

Repeat the same steps as for image annotation - no replies. Each time an annotation was created or
edited, pan and zoom the map to ensure the bounding boxes stay correctly aligned in OpenLayers.

# Image Annotation - replies

Repeat the tests 2 times: (i) in serverless mode and (ii) with annotation server.

1. Create an annotation and click CANCEL. Confirm the annotation is removed from the GUI.
2. Create an annotation and click OK. DO NOT use default bounding box size and location!
   a) Confirm the mouseover behavior works as intended.
   b) Edit the annotation (change text AND move/change bounding box). Click CANCEL and confirm 
      it works as intended (including mouseover behavior).
   c) Edit the annotation (change text AND move/change bounding box). Click OK and confirm 
      it works as intended (including mouseover behavior).
   d) Delete the annotation again and confirm it works as intended.
3. Create another annotation.
   a) Add two replies to the annotation and confirm the mouseover behavior works as intended.
   b) Edit the top-most annotation: change text AND bounding box. Click CANCEL and confirm it works as
      intended.
   c) Edit the top-most annotation: change text AND bounding box. Click OK and confirm it works as
      intended.
   d) Repeat steps 3b and 3c for the middle annotation.
   e) Delete the middle annotation and confirm it works as expected. 


