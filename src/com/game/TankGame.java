package com.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TankGame extends JFrame {

    MyPanel mp;

    public static void main(String args[]) {
        TankGame game = new TankGame();
    }

    TankGame() {
        mp = new MyPanel();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setVisible(true);
        //本来就具有listener属性
        //这个是被监听的，即事件源
        this.addKeyListener(mp);    //添加监听
        this.add(mp);
        Thread s = new Thread(mp);
        s.start();
    }

}

class MyPanel extends JPanel implements KeyListener, Runnable {
    //创建英雄坦克类
    Hero hero = null;
    //创建敌人坦克
    Vector<EnemyTank> ets = new Vector<EnemyTank>();
    GameTimer timer = new GameTimer();
    Thread t = null;
    int enSize = 3;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //画出黑色面板
        g.fillRect(0, 0, 800, 600);
        this.drawTank(hero.getX(), hero.getY(), hero.getDirection(), hero.getColor(), g);
        //这个是画坦克
        for (int i = 0; i < ets.size(); i++) {
            EnemyTank et=ets.get(i);
            if(et.isLive){

                this.drawTank(et.getX(), et.getY(), et.getDirection(), et.getColor(), g);
            }else{
                ets.remove(et);
//                System.out.println("移除一辆地方坦克");
            }
        }
        //画出子弹
        for (int i = 0; i < hero.buls.size(); i++) {
            Bullet bul = hero.buls.get(i);
            if (bul.isLive == true) {
                g.fill3DRect(bul.x, bul.y, 1, 1, true);
            } else {
                hero.buls.remove(bul);
//                System.out.println("移除一个对象");
            }
        }
    }

    MyPanel() {
        hero = new Hero(400, 400);
        //构建对象
        for (int i = 0; i < enSize; i++) {
            EnemyTank et = new EnemyTank(100 * i, 0);
            ets.add(et);
        }
    }

    void drawTank(int x, int y, int direction, int color, Graphics g) {
        switch (color) {
            case Tank.CYAN:
                g.setColor(Color.CYAN);
                break;
            case Tank.YELLOW:
                g.setColor(Color.YELLOW);
                break;
        }
        switch (direction) {
            case Tank.UP:
                g.fill3DRect(x, y, 10, 60, false);
                g.fill3DRect(x + 30, y, 10, 60, false);
                g.fill3DRect(x + 10, y + 10, 20, 40, false);
                g.fillOval(x + 10, y + 20, 20, 20);
                g.drawLine(x + 20, y, x + 20, y + 30);
                break;
            case Tank.LEFT:
                g.fill3DRect(x, y, 60, 10, false);
                g.fill3DRect(x, y + 30, 60, 10, false);
                g.fill3DRect(x + 10, y + 10, 40, 20, false);
                g.fillOval(x + 20, y + 10, 20, 20);
                g.drawLine(x, y + 20, x + 30, y + 20);
                break;
            case Tank.DOWN:
                g.fill3DRect(x, y, 10, 60, false);
                g.fill3DRect(x + 30, y, 10, 60, false);
                g.fill3DRect(x + 10, y + 10, 20, 40, false);
                g.fillOval(x + 10, y + 20, 20, 20);
                g.drawLine(x + 20, y + 60, x + 20, y + 30);
                break;
            case Tank.RIGHT:
                g.fill3DRect(x, y, 60, 10, false);
                g.fill3DRect(x, y + 30, 60, 10, false);
                g.fill3DRect(x + 10, y + 10, 40, 20, false);
                g.fillOval(x + 20, y + 10, 20, 20);
                g.drawLine(x + 60, y + 20, x + 30, y + 20);
                break;
        }
    }

    //按下所有键盘都可以识别
    @Override
    public void keyTyped(KeyEvent e) {

    }

    //功能键不能识别,大概上面那个是输入文本用的
    @Override
    public void keyPressed(KeyEvent e) {
        int key;
        key = e.getKeyCode();
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
            hero.moveUp();
        } else if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            hero.moveLeft();
        } else if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
            hero.moveDown();
        } else if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            hero.moveRight();
        }
        if (key == KeyEvent.VK_J) {

            //如果线程已经死亡，创建一个新线程，控制连发时间
            if (t == null || t.isAlive() == false) {
                hero.shot();
                t = new Thread(timer);
                t.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {
        //重复调用repaint
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            //这块的要判断是否发生击中
            //简单粗暴的遍历
            //先找出所有子弹
            for (int i = 0; i < hero.buls.size(); i++) {
                Bullet bul = hero.buls.get(i);
                for (int j = 0; j < ets.size(); j++) {
                    EnemyTank et = ets.get(j);
                    if (hitTank(bul, et)) {
                        bul.isLive = false;
                        et.isLive = false;
                    }
                }
            }
            this.repaint();
        }
    }

    //判断是否击中
    boolean hitTank(Bullet bul, EnemyTank et) {
        boolean isHit = false;
        switch (et.direction) {
            case Tank.DOWN:
            case Tank.UP:
                if (bul.x > et.x && bul.x < et.x + 40 && bul.y > et.y && bul.y < et.y + 60) {
                    isHit = true;
                }
                break;
            case Tank.LEFT:
            case Tank.RIGHT:
                if (bul.x > et.x && bul.x < et.x + 60 && bul.y > et.y && bul.y < et.y + 40) {
                    isHit = true;
                }
                break;
        }
        return isHit;
    }
}
