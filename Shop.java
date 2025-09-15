import java.util.ArrayList;
import java.util.Scanner;

// Encapsulated Product class
class Product {
    private String name;
    private double price;
    private int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    // Total price for this product (price * quantity)
    public double getTotalPrice() {
        return price * quantity;
    }
}

// Discount base class (polymorphism)
abstract class Discount {
    public abstract double applyDiscount(ArrayList<Product> products);
}

// Festive discount: 10% off total
class FestiveDiscount extends Discount {
    @Override
    public double applyDiscount(ArrayList<Product> products) {
        double total = 0;
        for (Product p : products) {
            total += p.getTotalPrice();
        }
        return total * 0.9; // 10% off
    }
}

// Bulk discount: 20% off if quantity > 5 for any product
class BulkDiscount extends Discount {
    @Override
    public double applyDiscount(ArrayList<Product> products) {
        double total = 0;
        for (Product p : products) {
            double productTotal = p.getTotalPrice();
            if (p.getQuantity() > 5) {
                productTotal *= 0.8; // 20% off on this product
            }
            total += productTotal;
        }
        return total;
    }
}

// Payment interface
interface Payment {
    void pay(double amount);
}

// Payment implementations for different methods
class CashPayment implements Payment {
    @Override
    public void pay(double amount) {
        System.out.printf("Payment Method: Cash\nTotal Amount Payable: %.2f\n", amount);
    }
}

class CardPayment implements Payment {
    @Override
    public void pay(double amount) {
        System.out.printf("Payment Method: Card\nTotal Amount Payable: %.2f\n", amount);
    }
}

class UpiPayment implements Payment {
    @Override
    public void pay(double amount) {
        System.out.printf("Payment Method: UPI\nTotal Amount Payable: %.2f\n", amount);
    }
}

public class Shop {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("------------Welcome to My Shop ------------");

        System.out.print("Enter number of products: ");
        int n = Integer.parseInt(sc.nextLine());

        ArrayList<Product> cart = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.printf("Enter details of product %d (Name Price Quantity): ", i + 1);
            String line = sc.nextLine();
            String[] parts = line.split(" ");
            if(parts.length < 3) {
                System.out.println("Invalid input. Please enter Name Price Quantity separated by space.");
                i--;
                continue;
            }
            String name = parts[0];
            double price;
            int quantity;
            try {
                price = Double.parseDouble(parts[1]);
                quantity = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid price or quantity. Please enter numeric values.");
                i--;
                continue;
            }
            cart.add(new Product(name, price, quantity));
        }

        System.out.print("Choose discount type (festive/bulk): ");
        String discountType = sc.nextLine().trim().toLowerCase();

        Discount discount;
        if (discountType.equals("festive")) {
            discount = new FestiveDiscount();
        } else if (discountType.equals("bulk")) {
            discount = new BulkDiscount();
        } else {
            System.out.println("Unknown discount type. No discount will be applied.");
            discount = new Discount() {
                @Override
                public double applyDiscount(ArrayList<Product> products) {
                    double total = 0;
                    for (Product p : products) {
                        total += p.getTotalPrice();
                    }
                    return total;
                }
            };
        }

        System.out.print("Choose payment method (cash/card/upi): ");
        String paymentMethod = sc.nextLine().trim().toLowerCase();

        Payment payment;
        switch (paymentMethod) {
            case "cash":
                payment = new CashPayment();
                break;
            case "card":
                payment = new CardPayment();
                break;
            case "upi":
                payment = new UpiPayment();
                break;
            default:
                System.out.println("Unknown payment method. Defaulting to Cash.");
                payment = new CashPayment();
        }
        System.out.println("-------------------TRANSACTION SUMMARY-------------------");

        // Print product details
        for (Product p : cart) {
            System.out.printf("Product: %s, Price: %.2f, Quantity: %d\n",
                    p.getName(), p.getPrice(), p.getQuantity());
        }

        double finalAmount = discount.applyDiscount(cart);

        payment.pay(finalAmount);
        System.out.println("Payment Successful");
        System.out.println("Thank You For Shopping With Us...");

        sc.close();
    }
}
