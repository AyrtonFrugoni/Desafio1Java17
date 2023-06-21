package application;

		
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import entities.Sale;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Entre o caminho do arquivo: ");
        String filePath = scanner.nextLine();
System.out.println();
        try {
            List<Sale> sales = readSalesData(filePath);
            // Obtendo as cinco primeiras vendas de 2016 de maior preço médio
            System.out.println("Cinco primeiras vendas de 2016 de maior preço médio");
            sales.stream()
                    .filter(sale -> sale.getYear() == 2016)
                    .sorted(Comparator.comparing(Sale::getAveragePrice).reversed())
                    .limit(5)
                    .forEach(System.out::println);

            // Obtendo o valor total vendido pelo vendedor Logan nos meses 1 e 7
            int[] months = { 1, 7 };
            double totalSalesByLogan = sales.stream()
                    .filter(sale -> sale.getSeller().equals("Logan"))
                    .filter(sale -> containsMonth(months, sale.getMonth()))
                    .mapToDouble(Sale::getTotal)
                    .sum();

            System.out.println();
            System.out.printf("Valor total vendido pelo vendedor Logan nos meses 1 e 7 = %.2f%n", totalSalesByLogan);
        } catch (FileNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        scanner.close();
    }

    private static List<Sale> readSalesData(String filePath) throws FileNotFoundException {
        List<Sale> sales = new ArrayList<>();
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] fields = line.split(",");

            if (fields.length == 5) {
                int month = Integer.parseInt(fields[0]);
                int year = Integer.parseInt(fields[1]);
                String seller = fields[2];
                int items = Integer.parseInt(fields[3]);
                double total = Double.parseDouble(fields[4]);
                sales.add(new Sale(month, year, seller, items, total));
            }
        }

        scanner.close();
        return sales;
    }

    private static boolean containsMonth(int[] months, int month) {
        for (int m : months) {
            if (m == month) {
                return true;
            }
        }
        return false;
    }
}
