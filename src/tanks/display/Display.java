package tanks.display;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

// отрисовка холста и окна
public abstract class Display {

    private static boolean created = false; // проверка на создание окна или нет
    private static JFrame window; // создание  рамки
    private static Canvas content; // добавления холста на котором будет отрисовываться

    //buffer накладное  изображение
    private static BufferedImage buffer;
    private static int[] bufferData;
    private static Graphics bufferGraphics;
    private static int clearColor;

    private static BufferStrategy bufferStrategy;

    public static void create(int width, int height, String title, int _clearColor, int numBuffers) { // метод создания
        if (created) // проверка на создан клас или нет
            return;
        window = new JFrame(title); // создание рамки
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // при закрытии программы прекращала работать сама програма
        // , без этого работает дальше но нет окна
        content = new Canvas();

        Dimension size = new Dimension(width, height); // предаем размеры листа в Canvas
        content.setPreferredSize(size);
//        content.setBackground(Color.BLACK); // задали цвет холста

        window.setResizable(false); // отмена  изменения  окна Юзером
        window.getContentPane().add(content); // добавили лист
        window.pack(); // изменяет под размер нашего контента
        window.setLocationRelativeTo(null); // задает  позицию окна от компонента
        window.setVisible(true);  // видимость окна

        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);// создаем IMG
        bufferData = ((DataBufferInt) buffer.getRaster().getDataBuffer()).getData(); // дастаем информацию  массива вернет
        bufferGraphics = buffer.getGraphics();
        ((Graphics2D) bufferGraphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // сглаживание на обьект
        clearColor = _clearColor; //инициализировали свойтво

        content.createBufferStrategy(numBuffers);
        bufferStrategy = content.getBufferStrategy();

        created = true;

    }

    public static void clear() { // метод очистки
        Arrays.fill(bufferData, clearColor);   ///???? заменяет for Loop заполняем массив цветом
    }

    public static void swapBuffers() {
        Graphics g = bufferStrategy.getDrawGraphics(); // вернет грический обьект
        g.drawImage(buffer, 0, 0, null); // рисуем по каким координатам высоты  и ширины
        bufferStrategy.show();
    }

    public static Graphics2D getGraphics2D() { // возвращает обект с графики
        return (Graphics2D) bufferGraphics;
    }

    public static void destroy() { //уничтожает окно
        if (!created)
            return;

        window.dispose();
    }

    public static void setTitle(String title) {
        window.setTitle(title);
    }

}
