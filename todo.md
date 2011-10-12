# TODOs

* Some CSS properties (text colors etc.) are not yet defined in the stylesheet, and are inherited from
  the CSS hierarchy - can lead to issues (e.g. if default text color is white, annotation popups will be
  white on white background) -> fix this
* Annotations created later always cover annotations created earlier -> fix, so that smaller annotations
  are never covered by larger ones!
* Image: the annotation layer sometimes seems to be shifted upwards a varying number of pixels. Seems
  to happen more often in Firefox than in any other browser, but I had it in Chrome as well once.
* Seajax: bboxes are currently encoded in screen coordinates, not image 
  coordinates -> change!
* Seajax bbox encoding leads to wrong bbox size when editing after zooming
  in/out
* Seajax: when the mouse moves over a DetailPopup (even a hidden one) this currently
  triggers a mouseout event and fades the bboxes -> fix!
* There's quite a bit of code overlap between ImageAnnotationLayer and SeajaxAnnotationLayer (and
  potentially OpenLayersAnnotationLayer) -> eliminate this by defining a "Annotatable2D" base class
* CSS style for button active/focus
* Seadragon: Reposition/z-index should also work when switching between 
  Seadragon normal/fullscreen mode
* Investigate the timing issue that causes issue #1, as reported by Klokan
