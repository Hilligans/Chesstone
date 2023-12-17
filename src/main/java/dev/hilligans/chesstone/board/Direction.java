package dev.hilligans.chesstone.board;

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

    public Direction getJoinedInverse() {
      //  System.out.println(directions[mapped(x,y) | mapped(-x,-y)]);
        return directions[mapped(x,y) | mapped(-x,-y)];
    }

    static {
        for(Direction direction : Direction.values()) {
            directions[direction.redstoneShape] = direction;
        }
    }

    public boolean facesTowards(int blockX, int blockY, int sourceX, int sourceY) {
        if(!twoSided) {
            return sourceX - blockX == -x && sourceY - blockY == -y;
        } else {
            int deltaX = sourceX - blockX;
            int deltaY = sourceY - blockY;
            if(deltaX == 0) {
                if(deltaY == -1) {
                    return (redstoneShape & 1) != 0;
                } else {
                    return (redstoneShape & 4) != 0;
                }
            } else if(deltaY == 0) {
                if(deltaX == -1) {
                    return (redstoneShape & 2) != 0;
                } else {
                    return (redstoneShape & 8) != 0;
                }
            }
            return false;
        }
    }



    public static int mapped(int x, int y) {
        int val = 0;
        if(y == 1) {
            val |= 1;
        } else if(y == -1) {
            val |= 4;
        }

        if(x == 1) {
            val |= 2;
        } else if(x == -1) {
            val |= 8;
        }
        return val;
    }

    @Override
    public String toString() {
        return "Direction{" +
                "x=" + x +
                ", y=" + y +
                ", otherX=" + otherX +
                ", otherY=" + otherY +
                ", redstoneShape=" + redstoneShape +
                ", twoSided=" + twoSided +
                '}';
    }
}
