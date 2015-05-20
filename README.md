# swing-drawing-app
This is an old app I wrote in 2005-2006 as a research project.  
It uses a few borrowed components, I kept all the license information in every case.
Many classes still sit there, waiting to be used as utilities.  

The application allows the user to draw simple lines and composite lines (Lines made out of other lines). 
Both kinds, can be manipulated in size using handles, and they can also be moved arround, clicking and dragging on them.
It has a working snap-on grid and the zoom in/out functionallity.
There is a also a working properties inspector that lets the user modify some basic properties on the drawing
(There's a small bug that forces you to switch document tab in order to see it working)
Also it has an implemetnation of the command pattern to acomplish undo redo.

Probaby the most interesting part here is the  eficient  paint strategy based on "clipping" and QuadTrees 

Have fun.


 

