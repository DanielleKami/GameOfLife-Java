class GoL{
    int width;
    int height;
    int[] world ;

    GoL(int width, int height){
        assert width >= 1 && height >=1;
        this.width = width +2;
        this.height = height +2;
        this.world = new int[this.width*this.height];
    }
    int rules(int center){
        int[] neighbors = {center-1,center+1, center-width, center-width-1,center-width+1,center+width, center+width-1,center+width+1};
        int liveNeighbors = 0;
        for(int pos : neighbors ){
            liveNeighbors += world[pos];

        }
        if(world[center] == 1){
            if(liveNeighbors == 2 || liveNeighbors ==3){
                return 1;
            }
            return 0;
        }else{
            if(liveNeighbors == 3){
                return 1;
            }
            return 0;
        }
        
        
        
    }
    GoL set(int row, int col){
        assert row >= 1 && row < height - 1 ; 
        assert col >= 1 && col < width - 1 ;
        int index = row*width + col;
        world[index] = 1;
        return this;
    }
    
    GoL timestep(){
        int[] newWorld = new int[world.length];
        for (int row = 1; row < height - 1; row++) {
            for (int col = 1; col < width - 1; col++) { 
                int index = row * width + col; 
                newWorld[index] = rules(index);
            }
        }
        world = newWorld;
        return this;
    }
    GoL insert(int row , int col, GoL source){
        assert this.world.length >= source.world.length;
        assert row>=1 && row + source.height -2 <= this.height;
        assert col>=1 && col+source.width-2<=this.width;
        
        for(int i = 0; i<source.height-2; i++){
            for(int j=0; j<source.width-2; j++){
                int sourceIndex = (i+1)*source.width + (j+1);
                int index = (row+i)*this.width + col+j;
                this.world[index] = source.world[sourceIndex];
            }
            
        }
        return this;
    }
    GoL rotate(){
        int[] rotateWorld = new int[world.length] ;
        for(int i = 1; i< this.height-1; i++){
            for(int j =1; j<this.width-1;j++){
                
                int rotateRow = j;
                int rotateCol = height-1 -i;
                int index = i*width + j;
                int rotateIndex = rotateRow*height + rotateCol;
                rotateWorld[rotateIndex] = this.world[index];
            }
        }
        this.world = rotateWorld;
        int var = this.width;
        this.width = this.height;
        this.height = var;


        return this;
    }
    void run(int n){
        assert n>=1;
        for(int i=1;i<=n; i++){
            System.out.println(timestep());
        }
        try {
            Thread.sleep(500);  
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    Turtle t = new Turtle(1000,1000);
    void view(GoL gol) {
        int x = 50, y = 50; 
        t.reset();          
        t.speed(50);        
        for (int row = 1; row < height - 1; row++) {
            t.moveTo(x, y); 
            for (int col = 1; col < width - 1; col++) {
                int pos = (row )* width + col; 
    
                
                t.beginPath(); 
                if (world[pos] == 1) {
                    t.fill("black"); 
                } else {
                    t.fill("white"); 
                }
                t.forward(30).left(90).forward(30).left(90).forward(30).left(90).forward(30).left(90); 
                t.endPath(); 
    
                 if (world[pos] == 1) {
                    t.moveTo(x +5, y - 15).text("🟩 ", Font.SANSSERIF, 25, Font.Align.CENTER);
                }
    
                
                x += 30;
                t.moveTo(x, y);
            }
            
            x = 50;
            y += 30;
        }
        t.left(90).moveTo(400,800).text("GAME OF LIVE",Font.SANSSERIF, 35, Font.Align.CENTER);
    }
    
    

    @Override 
    public String toString() {
        String s = "";
        view(this);
        
        char[] symbols = new char[]{'.', '*'};
        for (int row = 1; row < height - 1; row++) {
            for (int col = 1; col < width - 1; col++) {
                int pos = row * width + col;
                s += symbols[world[pos]];
                
            }
            s += row != height ? "\n" : "";
        }
        return s;
    }
    
}



/* STARTSZENARIO :
 *GoL gosperGliderGun = new GoL(40, 20).set(1, 26).set(2, 24).set(2, 26).set(3, 14).set(3, 15).set(3, 22).set(3, 23).set(3, 36).set(3, 37).set(4, 13).set(4, 17).set(4, 22).set(4, 23).set(4, 36).set(4, 37).set(5, 1).set(5, 2).set(5, 12).set(5, 18).set(5, 22).set(5, 23).set(6, 1).set(6, 2).set(6, 12).set(6, 16).set(6, 18).set(6, 19).set(6, 24).set(6, 26).set(7, 12).set(7, 18).set(7, 26).set(8, 13).set(8, 17).set(9, 14).set(9, 15).set(3, 35).set(4, 35).set(5, 35)
 * 
 * gosperGliderGun.run(100);
 */







 enum Font { 
    ARIAL("Arial"),
    VERDANA("Verdana"),
    TIMES("Times New Roman"),
    COURIER("Courier New"),
    SERIF("serif"),
    SANSSERIF("sans-serif");

    final String fullName; 

    private Font(String fullName) { this.fullName = fullName; }
    
    public String toString() { return fullName;}

    static enum Align {
        CENTER, LEFT, RIGHT;
        public String toString() { return name().toLowerCase(); }    
    }
}

class Turtle implements Clerk {
    final String ID;
    LiveView view;
    final int width, height;
    Font textFont = Font.SANSSERIF;
    double textSize = 10;
    Font.Align textAlign = Font.Align.CENTER;

    Turtle(LiveView view, int width, int height) {
        this.view = view;
        this.width  = Math.max(1, Math.abs(width));  // width is at least of size 1
        this.height = Math.max(1, Math.abs(height)); // height is at least of size 1
        ID = Clerk.getHashID(this);
        Clerk.load(view, "views/Turtle/turtle.js");
        Clerk.write(view, "<canvas id='turtleCanvas" + ID + "' width='" + this.width + "' height='" + this.height + "' style='border:1px solid #000;'></canvas>");
        Clerk.script(view, "const turtle" + ID + " = new Turtle(document.getElementById('turtleCanvas" + ID + "'));");
    }

    Turtle(LiveView view) { this(view, 500, 500); }
    Turtle(int width, int height) { this(Clerk.view(), width, height); }
    Turtle() { this(Clerk.view()); }

    Turtle penDown() {
        Clerk.call(view, "turtle" + ID + ".penDown();");
        return this;
    }

    Turtle penUp() {
        Clerk.call(view, "turtle" + ID + ".penUp();");
        return this;
    }

    Turtle forward(double distance) {
        Clerk.call(view, "turtle" + ID + ".forward(" + distance + ");");
        return this;
    }

    Turtle backward(double distance) {
        Clerk.call(view, "turtle" + ID + ".backward(" + distance + ");");
        return this;
    }

    Turtle left(double degrees) {
        Clerk.call(view, "turtle" + ID + ".left(" + degrees + ");");
        return this;
    }

    Turtle right(double degrees) {
        Clerk.call(view, "turtle" + ID + ".right(" + degrees + ");");
        return this;
    }

    Turtle color(int red, int green, int blue) {
        Clerk.call(view, "turtle" + ID + ".color('rgb(" + (red & 0xFF) + ", " + (green & 0xFF) + ", " + (blue & 0xFF) + ")');");
        return this;
    }

    Turtle color(int rgb) {
        color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
        return this;
    }

    Turtle lineWidth(double width) {
        Clerk.call(view, "turtle" + ID + ".lineWidth('" + width + "');");
        return this;
    }

    Turtle reset() {
        Clerk.call(view, "turtle" + ID + ".reset();");
        return this;
    }

    Turtle text(String text, Font font, double size, Font.Align align) {
        textFont = font;
        textSize = size;
        textAlign = align;
        Clerk.call(view, "turtle" + ID + ".text('" + text + "', '" + "" + size + "px " + font + "', '" + align + "')");
        return this;
    }

    Turtle text(String text) { return text(text, textFont, textSize, textAlign); }

    Turtle moveTo(double x, double y) {
        Clerk.call(view, "turtle" + ID + ".moveTo(" + x + ", " + y + ");");
        return this;
    }

    Turtle lineTo(double x, double y) {
    Clerk.call(view, "turtle" + ID + ".lineTo(" + x + ", " + y + ");");
    return this;
    }
    Turtle fill(String color) {
        
        System.out.println("Turtle fill color set to: " + color);
        return this;
    }

    Turtle beginPath() {
    
         return this;
    }

    Turtle endPath() {
        
         return this;
    }

    private int currentSpeed = 1; 

    public void speed(int speed) {
        if (speed < 1) speed = 1; 
        if (speed > 10) speed = 50; 
        currentSpeed = speed;
        }



}