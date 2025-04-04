import test.EpicTest;
import test.SubTaskTest;
import test.TaskManagerTest;
import test.TaskTest;

public class main {
    public static void main(String[] args) {
        System.out.println("-----------Тест Таска-----------");
        TaskTest.main(args);
        System.out.println("-----------Тест Эпика-----------");
        EpicTest.main(args);
        System.out.println("-----------Тест Подзадачи-----------");
        SubTaskTest.main(args);
        System.out.println("-----------Тест Менеджера-----------");
        TaskManagerTest.main(args);
    }
}
