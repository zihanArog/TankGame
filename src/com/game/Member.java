package com.game;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

class Bullet implements Runnable {
    //一个线程只能启动一次
    //线程安全会有问题

    int x;
    int y;
    int speed;
    int direction;
    boolean isLive;

    //颜色属性对照
    public static final int YELLOW = 0;
    public static final int CYAN = 1;

    //方向设定
    static final int UP = 0;
    static final int DOWN = 1;
    static final int LEFT = 2;
    static final int RIGHT = 3;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    Bullet(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = 2;
        this.isLive = true;
    }

    @Override
    public void run() {
        while (true) {
            //限制时间
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            System.out.println("子弹位置：\nx=" + this.x + ",y=" + this.y);
            switch (this.direction) {
                case Bullet.UP:
                    this.y -= this.speed;
                    break;
                case Bullet.DOWN:
                    this.y += this.speed;
                    break;
                case Bullet.LEFT:
                    this.x -= this.speed;
                    break;
                case Bullet.RIGHT:
                    this.x += this.speed;
                    break;
            }

            // 控制线程结束
            if (this.x <= 0 || this.x >= 800 || this.y <= 0 || this.y >= 600) {
                isLive = false;
                break;
            }
        }
    }
}

class Tank {

    int x;
    int y;
    int direction;
    int color;
    int speed;
    //    Bullet bul = null;
    Vector<Bullet> buls = new Vector<Bullet>();

    void shot() {
        //射击
//        System.out.println("正在射击");
        Bullet bul = null;
        switch (direction) {
            case Tank.DOWN:
                bul = new Bullet(this.x + 20, this.y + 60, this.direction);
                break;
            case Tank.UP:
                bul = new Bullet(this.x + 20, this.y, this.direction);
                break;
            case Tank.LEFT:
                bul = new Bullet(this.x, this.y + 20, this.direction);
                break;
            case Tank.RIGHT:
                bul = new Bullet(this.x + 60, this.y + 20, this.direction);
        }

        //添加bul进向量
        buls.add(bul);
        //启动线程
        Thread t = new Thread(bul);
        t.start();
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    //颜色属性对照
    public static final int YELLOW = 0;
    public static final int CYAN = 1;

    //方向设定
    static final int UP = 0;
    static final int DOWN = 1;
    static final int LEFT = 2;
    static final int RIGHT = 3;


    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }


    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    Tank(int x, int y) {
        this.x = x;
        this.y = y;
        this.direction = Tank.DOWN;
        this.color = Tank.YELLOW;
        this.speed = 1;
    }

    void moveUp() {
        this.direction = Tank.UP;
        this.y -= this.speed;
    }

    void moveDown() {
        this.direction = Tank.DOWN;
        this.y += this.speed;
    }

    void moveRight() {
        this.direction = Tank.RIGHT;
        this.x += this.speed;
    }

    void moveLeft() {
        this.direction = Tank.LEFT;
        this.x -= this.speed;
    }

}

//英雄
class Hero extends Tank {
    Hero(int x, int y) {
        super(x, y);
        this.color = Tank.CYAN;
        this.direction = Tank.UP;
    }
}


//敌人坦克
class EnemyTank extends Tank {
    boolean isLive = true;

    EnemyTank(int x, int y) {
        super(x, y);
        this.color = Tank.YELLOW;
        this.direction = Tank.DOWN;
    }
}

class GameTimer implements Runnable {

    @Override
    public void run() {
        //这是个计时器，倒计时300ms
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
