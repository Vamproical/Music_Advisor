package advisor;

import java.util.List;

public class View {
    static int elem;
    static int page;
    static List<String> data;
    static int pagesCount;

    public static void print(List<String> data) {
        elem = -Utils.PAGE;
        page = 0;
        View.data = data;
        pagesCount = data.size() / Utils.PAGE;
        pagesCount += data.size() % Utils.PAGE != 0 ? 1 : 0;

        printNextPage();
    }

    public static void printNextPage() {
        if (page >= pagesCount) {
            System.out.println("No more pages.");
        } else {
            elem += Utils.PAGE;
            page++;
            print();
        }
    }

    public static void printPrevPage() {
        if (page == 1) {
            System.out.println("No more pages.");
        } else {
            elem -= Utils.PAGE;
            page--;
            print();
        }
    }

    public static void print() {
        data.stream()
                .skip(elem)
                .limit(Utils.PAGE)
                .forEach(System.out::print);
        System.out.printf("---PAGE %d OF %d---\n", page, pagesCount);
    }
}
