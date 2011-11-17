# TODOs

* AJAX load indicators needed inside the edit form when saving/updating/deleting
* JavaDoc
* Consistent naming of CSS classes throughout the project
* The whole project could benefit from a thorough code review
* GUI components/functionality for OpenID login
* Architecture/class model needs to be documented in a graphic/UML diagram -> some revisions are needed to make the
  whole UI class structure more consistent/readable
* Image: the annotation layer sometimes seems to be shifted upwards a varying number of pixels. Seems
  to happen more often in Firefox than in any other browser, but I had it in Chrome as well once.
* GUI testing: http://dojotoolkit.org/reference-guide/util/dohrobot.html may be a possible option
* Seajax is totally incomplete
  * bboxes are currently encoded in screen coordinates, not image coordinates -> change!
  * When the mouse moves over a DetailPopup (even a hidden one) this currently triggers a 
    mouseout event and fades the bboxes -> fix!
  * Reposition/z-index should also work when switching between Seadragon
    normal/fullscreen mode
* Investigate the timing issue that causes issue #1, as reported by Klokan
