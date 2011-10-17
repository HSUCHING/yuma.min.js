# TODOs

* Image: the annotation layer sometimes seems to be shifted upwards a varying number of pixels. Seems
  to happen more often in Firefox than in any other browser, but I had it in Chrome as well once.
* Web tests with a tool like Selenium!!
* Seajax: bboxes are currently encoded in screen coordinates, not image 
  coordinates -> change!
* Seajax bbox encoding leads to wrong bbox size when editing after zooming
  in/out
* Seajax: when the mouse moves over a DetailPopup (even a hidden one) this currently
  triggers a mouseout event and fades the bboxes -> fix!
* CSS style for button active/focus
* Seadragon: Reposition/z-index should also work when switching between 
  Seadragon normal/fullscreen mode
* Investigate the timing issue that causes issue #1, as reported by Klokan
