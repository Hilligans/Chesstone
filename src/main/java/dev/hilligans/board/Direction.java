package dev.hilligans.board;

public enum Direction {

    ALL(1,1, -1,-1),
    NONE(0,0),
    PX(1,0),
    PY(0,1),
    NX(-1,0),
    NY(0,-1),

    PX_PY_NY(1,1,0,-1),
    PX_PY_NX(1,1,-1,0),
    NX_PY_NY(-1,1,0,-1),
    NX_PY_NX(1,-1,-1,0),

    PX_PY(1,1,0,0),
    PX_NY(1,-1,0,0),
    NX_PY(-1,1,0,0),
    NX_NY(-1,-1,0,0),

    X(1,0, -1,0),
    Y(0,1, 0,-1);

    public static Direction[] directions = new Direction[16];


    public int x,y;
    public int otherX,otherY;

    public int redstoneShape;

    public boolean twoSided;
    Direction(int x, int y) {
        this.x = x;
        this.y = y;
        redstoneShape = mapped(x,y);
    }

    Direction(int x, int y, int otherX, int otherY) {
        this.x = x;
        this.y = y;
        this.otherX = otherX;
        this.otherY = otherY;
        redstoneShape = mapped(x,y) | mapped(otherX,otherY);
        this.twoSided = true;
    }

    static {
        for(Direction direction : Direction.values()) {
            directions[direction.redstoneShape] = direction;
        }
    }

    //FIX THIS
    public boolean facesTowards(int blockX, int blockY, int sourceX, int sourceY) {
        if(!twoSided) {
            return blockX == sourceX + x && blockY == sourceY + y;
        } else {
            return blockX == sourceX + x && blockY == sourceY + y || blockX == sourceX + otherX && blockY == sourceY + y || blockX == sourceX + x && blockY == sourceY + otherY || blockX == sourceX + otherX && blockY == sourceY + otherY;
        }
    }

    static int mapped(int x, int y) {
        int val = 0;
        if(y == 1) {
            val |= 8;
        } else if(y == -1) {
            val |= 2;
        }

        if(x == 1) {
            val |= 4;
        } else if(x == -1) {
            val |= 1;
        }
        return val;
    }
}
