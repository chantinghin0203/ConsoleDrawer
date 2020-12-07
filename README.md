# Drawing program

A console application for drawing lines/rectangles and filling colors

## Technical stack
  - Java 8
  - Srpingboot
  
  
## To run the project
  ```
  mvn clean install
  ```
  
  ```
  mvn exec:java
  ```
  
__Description__

You're given the task of writing a simple console version of a drawing program. 
At this time, the functionality of the program is quite limited but this might change in the future. 
In a nutshell, the program should work as follows:
 1. Create a new canvas
 2. Start drawing on the canvas by issuing various commands
 3. Quit


Command 		Description
C w h           Should create a new canvas of width w and height h.
L x1 y1 x2 y2   Should create a new line from (x1,y1) to (x2,y2). Currently only
                horizontal or vertical lines are supported. Horizontal and vertical lines
                will be drawn using the 'x' character.
R x1 y1 x2 y2   Should create a new rectangle, whose upper left corner is (x1,y1) and
                lower right corner is (x2,y2). Horizontal and vertical lines will be drawn
                using the 'x' character.
B x y c         Should fill the entire area connected to (x,y) with "colour" c. The
                behavior of this is the same as that of the "bucket fill" tool in paint
                programs.
Q               Should quit the program.


Validations and rejections:
User must create canvas before 
Creating Canvas must provide positive numbers
All points with out of bound coordinate will be rejected
Drawing rectandle must provide at least 1 width and 1 height (For example x1=1,y1=2,x2=1,y2=3 will be rejected - width is zero). Otherwise you should draw a line instead


Limitation:
Cannot draw diagonal lines
