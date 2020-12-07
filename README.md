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

In a nutshell, the program should work as follows:
 1. Create a new canvas
 2. Start drawing on the canvas by issuing various commands
 3. Quit

## Command Description

|  Command | Description  |
| ------------ | ------------ |
|  C w h   |  Should create a new canvas of width w and height h. |
| L x1 y1 x2 y2  | Should create a new line from (x1,y1) to (x2,y2). Currently only horizontal or vertical lines are supported. Horizontal and vertical lines will be drawn using the "x" character |
|R x1 y1 x2 y2|Should create a new rectangle, whose upper left corner is (x1,y1) and lower right corner is (x2,y2). Horizontal and vertical lines will be drawn using the 'x' character|
|B x y c|Should fill the entire area connected to (x,y) with "colour" c. The behavior of this is the same as that of the "bucket fill" tool in paint programs
|Q|Should quit the program

## Validations and rejections:
1. User must create canvas before drawing
2. User must provide positive width and height when creating canvas
3. All out of bound coordinates will be rejected
4. Drawing rectangle must at least has 1 width and 1 height (abs(x1 - x2) > 0 && abs(y1 - y2) > 0
5. BucketFiller cannot fill with x


## Limitations:
Cannot draw diagonal lines
