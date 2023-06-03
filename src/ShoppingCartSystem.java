import java.util.Objects;
import java.util.Scanner;

public class ShoppingCartSystem {
    static final int MAX_PRODUCTS = 100; // 最大商品数量
    private static final int MAX_CUSTOMERS = 100; // 最大顾客数量
    private static final int MAX_CARDS = 100; // 最大会员卡数量

    private static Product[] products = new Product[MAX_PRODUCTS]; // 商品列表
    private static int numProducts = 0; // 商品数量

    private static Customer[] customers = new Customer[MAX_CUSTOMERS]; // 顾客列表
    private static int numCustomers = 0; // 顾客数量

    private static Card[] cards = new Card[MAX_CARDS]; // 会员卡列表
    private static int numCards = 0; // 会员卡数量

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("请选择功能：");
            System.out.println("1. 商品信息录入");
            System.out.println("2. 信息查询");
            System.out.println("3. 结账");
            System.out.println("4. 购物卡管理");
            System.out.println("5. 退出");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addProduct(scanner);
                    break;
                case 2:
                    searchProduct(scanner);
                    break;
                case 3:
                    checkout(scanner);
                    break;
                case 4:
                    manageCard(scanner);
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("无效的选项，请重新选择。");
                    break;
            }
        }
    }

    private static void addProduct(Scanner scanner) {
        System.out.println("请输入商品信息：");

        System.out.print("货号：");
        String id = scanner.next();

        // 检查是否已存在相同货号的商品
        for (int i = 0; i < numProducts; i++) {
            if (products[i].getId().equals(id)) {
                System.out.println("已存在相同货号的商品，请重新输入。");
                return;
            }
        }

        System.out.print("名称：");
        String name = scanner.next();

        System.out.print("价格：");
        double price = scanner.nextDouble();

        products[numProducts++] = new Product(id, name, price);

        System.out.println("商品信息录入成功。");
    }

    private static void searchProduct(Scanner scanner) {
        System.out.println("请选择查询方式：");
        System.out.println("1. 按货号查询");
        System.out.println("2. 按商品名称查询");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.print("请输入货号：");
                String id = scanner.next();

                for (int i = 0; i < numProducts; i++) {
                    if (products[i].getId().equals(id)) {
                        System.out.println(products[i]);
                        return;
                    }
                }

                System.out.println("未找到相应的商品。");
                break;
            case 2:
                System.out.print("请输入商品名称：");
                String name = scanner.next();

                for (int i = 0; i < numProducts; i++) {
                    if (products[i].getName().equals(name)) {
                        System.out.println(products[i]);
                        return;
                    }
                }

                System.out.println("未找到相应的商品。");
                break;
            default:
                System.out.println("无效的选项，请重新选择。");
                break;
        }
    }

    private static void checkout(Scanner scanner) {
        System.out.println("请输入顾客信息：");

        System.out.print("是否持卡（y/n）：");
        boolean hasCard = scanner.next().equals("y");

        Customer customer;

        if (hasCard) {
            System.out.print("请输入会员卡号：");
            String cardId = scanner.next();

            customer = findCustomerByCard(cardId);

            if (customer == null) {
                System.out.println("未找到相应的顾客。");
                return;
            }
        } else {
            customer = new Customer();
        }

        boolean exit = false;
        double total = 0;

        while (!exit) {
            System.out.print("请输入商品货号（输入0结束结账）：");
            String id = scanner.next();

            if (id.equals("0")) {
                exit = true;
                continue;
            }

            Product product = findProductById(id);

            if (product == null) {
                System.out.println("未找到相应的商品。");
                continue;
            }

            System.out.print("请输入数量：");
            int quantity = scanner.nextInt();

            if (quantity <= 0) {
                System.out.println("数量不能小于等于0，请重新输入。");
                continue;
            }

            total += product.getPrice() * quantity;

            if (hasCard) {
                customer.addPurchase(product, quantity);
            }
        }

        if (hasCard) {
            System.out.println("消费情况已记录。");
        } else if (total >= 200) {
            System.out.println("本次购物满200元，可为您发放一张会员卡，以后购物可享受9折优惠。");
            addCard(customer);
        }

        System.out.println("总金额为：" + total);
    }

    private static void manageCard(Scanner scanner) {
        System.out.println("请选择操作：");
        System.out.println("1. 发放会员卡");
        System.out.println("2. 查询会员卡信息");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("请输入顾客信息：");

                System.out.print("姓名：");
                String name = scanner.next();

                System.out.print("电话：");
                String phone = scanner.next();

                Customer customer = new Customer(name, phone);
                addCard(customer);

                System.out.println("会员卡已发放。");
                break;
            case 2:
                System.out.print("请输入会员卡号：");
                String cardId = scanner.next();

                Card card = findCardById(cardId);

                if (card == null) {
                    System.out.println("未找到相应的会员卡。");
                } else {
                    System.out.println(card);
                }

                break;
            default:
                System.out.println("无效的选项，请重新选择。");
                break;
        }
    }

    private static void addCard(Customer customer) {
        cards[numCards++] = new Card(customer);
    }

    private static Customer findCustomerByCard(String cardId) {
        for (int i = 0; i < numCards; i++) {
            if (Objects.equals(cards[i].getId(), cardId)) {
                return cards[i].getCustomer();
            }
        }

        return null;
    }

    private static Product findProductById(String id) {
        for (int i = 0; i < numProducts; i++) {
            if (products[i].getId().equals(id)) {
                return products[i];
            }
        }

        return null;
    }

    private static Card findCardById(String id) {
        for (int i = 0; i < numCards; i++) {
            if (Objects.equals(cards[i].getId(), id)) {
                return cards[i];
            }
        }

        return null;
    }
}

class Product {
    private String id;
    private String name;
    private double price;

    public Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "货号：" + id + "，名称：" + name + "，价格：" + price;
    }
}

class Customer {
    private String name;
    private String phone;
    private Purchase[] purchases = new Purchase[ShoppingCartSystem.MAX_PRODUCTS];
    private int numPurchases = 0;

    public Customer() {}

    public Customer(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public void addPurchase(Product product, int quantity) {
        purchases[numPurchases++] = new Purchase(product, quantity);
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Purchase[] getPurchases() {
        return purchases;
    }

    public int getNumPurchases() {
        return numPurchases;
    }
}

class Purchase {
    private Product product;
    private int quantity;

    public Purchase(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}

class
Card {
    private static int nextId = 1;

    private int id;
    private Customer customer;

    public Card(Customer customer) {
        this.id = nextId++;
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return "会员卡号：" + id + "，姓名：" + customer.getName() + "，电话：" + customer.getPhone();
    }
}
