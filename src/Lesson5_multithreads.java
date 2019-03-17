import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;


/**
 * Java. Level 3. Lesson 5.
 * @version 17.03.2019
 */

/*
Все участники должны стартовать одновременно, несмотря на то, что на подготовку у каждого их них уходит разное время.
В тоннель не может заехать одновременно больше половины участников (условность).
Попробуйте все это синхронизировать.
Только после того, как все завершат гонку, нужно выдать объявление об окончании.
Можете корректировать классы (в т.ч. конструктор машин) и добавлять объекты классов из пакета util.concurrent.
*/


public class Lesson5_multithreads {

    public static final int CARS_COUNT = 4;
    public static final int HALF_CARS_COUNT = CARS_COUNT/2;

    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");

/*
       CyclicBarrier выполняет синхронизацию заданного количества потоков в одной точке.
       Как только заданное количество потоков заблокировалось (вызовами метода await()),
       с них одновременно снимается блокировка.
*/

        CyclicBarrier cb = new CyclicBarrier(CARS_COUNT +1);

/*
        CountDownLatch позволяет потоку ожидать завершения операций, выполняющихся в других потоках.
        Режим ожидания запускается методом await(). При создании объекта определяется количество требуемых операций,
        после чего уменьшается при вызове метода countDown(). Как только счетчик доходит до нуля,
         с ожидающего потока снимается блокировка
 */
        CountDownLatch cdl = new CountDownLatch(CARS_COUNT);


        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10),cb, cdl);
        }
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }
        try {
            cb.await();
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
            cb.await();
            cb.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}
