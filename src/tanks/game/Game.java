package tanks.game;

import tanks.IO.Input;
import tanks.display.Display;
import tanks.graphics.Sprite;
import tanks.graphics.SpriteSheet;
import tanks.graphics.TextureAtlas;
import tanks.utils.Time;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Game implements Runnable {
    // паралельный  компонент для создания  нового процесса
    /*==================================================*/
    // конструктор игры
    //  int width, int height, String title, int _clearColor, int numBuffers
    // параметры для создания  окна
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String TITLE = "Tanks";
    public static final int CLEAR_COLOR = 0xff000000;
    public static final int NUM_BUFFERS = 3;


    // дополнительные
    public static final float UPDATE_RATE = 60.0f; //значения  для  обновления
    public static final float UPDATE_INTERVAL = Time.SECOND / UPDATE_RATE; // интервал определенного на обновления UPDATE
    public static final long IDLE_TIME = 1; // каждый фред создаваемый давал на обработку других процесссов

    public static final String ATLAS_FILE_NAME = "texture_atlas.png";

    // поля  игры
    private boolean running;
    private Thread gameThread;
    private Graphics2D graphics2D;
    private Input input;
    private TextureAtlas atlas;
    private SpriteSheet sheet;
    private Sprite sprite;

    // temp
    float x = 350;
    float y = 250;
    float delta = 0;
    float rad = 50;
    float speed = 3;


    //temp
    public Game() {
        running = false;
        Display.create(WIDTH, HEIGHT, TITLE, CLEAR_COLOR, NUM_BUFFERS);
        graphics2D = Display.getGraphics2D();
        input = new Input();
        Display.addInputListener(input);
        atlas = new TextureAtlas(ATLAS_FILE_NAME);
        sheet = new SpriteSheet(atlas.cut(8 * 16, 5 * 16, 16 * 2, 16), 2, 16);
        sprite = new Sprite(sheet, 1);
    }

    /*==================================================*/
    // для  старта игры  synchronized для не
    // одновременного вызова 2 разных фреда не могли начинать
    public synchronized void start() {

        if (running) // проверяем запущена игра или нет
            return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start(); // запускает игру
    }

    /*==================================================*/
    // для  остановки игры  synchronized для не
    // одновременного останавливания 2 разных фреда не могли остановить
    public synchronized void stop() {
        if (!running) // проверяем запущена игра или нет
            return;
        running = false;
        // прекратить все
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();// распечатать на какой строчке произошел аксепшен
        }
        cleanUp();// очищение
    }

    /*==================================================*/
    // считает все расчеты
    private void update() {
        if (input.getKey(KeyEvent.VK_UP))
            y -= speed;
        if (input.getKey(KeyEvent.VK_DOWN))
            y += speed;
        if (input.getKey(KeyEvent.VK_LEFT))
            x -= speed;
        if (input.getKey(KeyEvent.VK_RIGHT))
            x += speed;
    }

    /*==================================================*/
    // после подсчета  рендерим обьектов танков пуль выводим отрисовку
    private void render() {
        Display.clear();
        sprite.render(graphics2D, x, y);
        Display.swapBuffers();
    }

    /*==================================================*/
    // главная  функции Loop опред кол
    public void run() {

        int fps = 0;
        int upd = 0;
        int updL = 0;

        // вывод на экран FPS UPD UPDL;
        long count = 0;

        float delta = 0;

        long lastTime = Time.get();
        while (running) { // пока тру будет бежать по циклу
            long now = Time.get();
            long elapsedTime = now - lastTime; // кол времени сколько прошло от прошлого раза
            lastTime = now; // приравняем к текущему

            count += elapsedTime;

            boolean render = false;
            delta += (elapsedTime / UPDATE_INTERVAL); // кол времени до того как должен проходить апдейт
            while (delta > 1) { // увеличиваем постоянно на 1
                // следит за количеством Update чтоб постоянно было 60
                update();
                upd++;
                delta--;
                if (render) {
                    updL++;
                } else {
                    render = true;
                }
            }
            if (render) {
                render();
                fps++;
            } else {
                try {
                    Thread.sleep(IDLE_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (count >= Time.SECOND) {
                Display.setTitle(TITLE + " || Fps: "
                        + fps + " >> Upd: " + upd + " >> UpdL: " + updL);
                upd = 0;
                fps = 0;
                updL = 0;
                count = 0;
            }

        }

    }

    /*==================================================*/
    //  ресурсы  которые  желатьльно закрывать
    private void cleanUp() {
        Display.destroy(); // удаления окна при закрытии игры
    }
}
