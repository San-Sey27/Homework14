package hw14;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class TripEuropeanUnion {

    static void periodOfStay(String fileName) {
        ArrayList<String> datesList = new ArrayList<>();
        datesList = readFromFile(fileName);

        if (isValid(datesList)) {
            int sum = 0;
            LocalDate startDate = LocalDate.now().minusDays(180);
            for (int i = datesList.size() - 1; i >= 0; i--) {
                LocalDate ld1 = LocalDate.parse(datesList.get(i).substring(0, 10), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                LocalDate ld2 = getLd2(datesList, i);
                if (ld1.isAfter(startDate)) {
                    sum = sum + getDays(ld1, ld2);
                }
                if (startDate.isBefore(ld2) && startDate.isAfter(ld1)) {
                    sum = sum + getDays(startDate, ld2);
                }
            }
            System.out.println("Today - 180 days = date: " + startDate);
            if (sum <= 90) {
                System.out.printf("Человек находится легально в еврозоне, до истечения у него осталось %s д. ", (90 - sum));
            } else {
                System.out.printf("Человек находится не легально в еврозоне, на %s д. просрочен", (sum - 90));
            }

        } else {
            System.out.println("Исправте даты!");
        }
    }

    private static boolean isValid(ArrayList<String> list) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        boolean valid = true;

        for (int i = 0; i < list.size(); i++) {
            String value = list.get(i).substring(0, 10);
            try {
                formatter.parse(value);
            } catch (ParseException e) {
                System.out.println("Не верный формат даты: " + list.get(i).substring(0, 10));
                valid = false;
            }
            if (list.get(i).contains("-")) {
                String value2 = list.get(i).substring(13);
                try {
                    formatter.parse(value2);
                } catch (ParseException e) {
                    System.out.println("Не верный формат даты: " + list.get(i).substring(13));
                    valid = false;
                }
            }
        }
        return valid;
    }

    private static int getDays(LocalDate ld1, LocalDate ld2) {
        int days = (int) ChronoUnit.DAYS.between(ld1, ld2);
        return days;
    }

    private static LocalDate getLd2(ArrayList<String> list, int i) {
        if (list.get(i).contains("-")) {
            return LocalDate.parse(list.get(i).substring(13), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } else {
            return LocalDate.now();
        }
    }

   private static ArrayList readFromFile(String fileName) {
        ArrayList<String> datesString = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                datesString.add(currentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datesString;
    }
}
