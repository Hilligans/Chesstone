package dev.hilligans.board;

public enum Direction {

    ALL(1,1, -1,-1),
    NONE(0,0),
    PX(1,0),
    PY(0,1),
    NX(-1,0),
    NY(-1,0),

    PX_PY_NY(1,1,0,-1),
    PX_PY_NX(1,1,-1,0),
    NX_PY_NY(-1,1,0,-1),
    NX_PY_NX(-1,1,-1,0),

    PX_PY(1,1,0,0),
    PX_NY(1,-1,0,0),
    NX_PY(-1,1,0,0),
    NX_NY(-1,-1,0,0),

    X(1,0, -1,0),
    Y(0,1, 0,-1);


    public int x,y;
    public int otherX,otherY;

    public boolean twoSided;
    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Direction(int x, int y, int otherX, int otherY) {
        this.x = x;
        this.y = y;
        this.otherX = otherX;
        this.otherY = otherY;
        this.twoSided = true;
    }

    public boolean facesTowards(int blockX, int blockY, int sourceX, int sourceY) {
        if(!twoSided) {
            return blockX == sourceX + x && blockY == sourceY + y;
        } else {
            return blockX == sourceX + x && blockY == sourceY + y || blockX == sourceX + otherX && blockY == sourceY + y || blockX == sourceX + x && blockY == sourceY + otherY || blockX == sourceX + otherX && blockY == sourceY + otherY;
        }
    }
}
