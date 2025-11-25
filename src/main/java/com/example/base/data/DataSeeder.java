package com.example.base.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.base.data.models.ContactModel;
import com.example.base.data.models.PartnerModel;
import com.example.base.data.services.PartnerService;

@Component
public class DataSeeder implements CommandLineRunner {

    private final PartnerService partnerService;
    private final Random random;

    public DataSeeder(PartnerService partnerService) {
        this.partnerService = partnerService;
        this.random = new Random();
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if database is already populated
        if (!partnerService.findAll().isEmpty()) {
            System.out.println("Database already contains data. Skipping seeding.");
            return;
        }

        System.out.println("Seeding database with test data...");

        // Create 15 partners
        for (int i = 0; i < 15; i++) {
            PartnerModel partner = createPartner(i);
            partnerService.save(partner);
        }

        System.out.println("Database seeding completed! Created 15 partners with contacts.");
    }

    private PartnerModel createPartner(int index) {
        PartnerModel partner = new PartnerModel();

        // Basic info
        partner.setName(getCompanyName(index) + " d.o.o.");
        partner.setShortName(getShortName(index));
        partner.setAddress(getAddress(index));
        partner.setPostCode(getPostCode(index));
        partner.setTaxNumber(generateTaxNumber());

        // Shipping info
        partner.setShippmentContactPerson(generateFullName());
        partner.setShippmentShortName(getShortName(index) + "-SKL");
        partner.setShippmentPostCode(getPostCode(random.nextInt(12)));
        partner.setShippmentPhone(generatePhone());

        // Create 2-4 contacts
        List<ContactModel> contacts = new ArrayList<>();
        int numContacts = 2 + random.nextInt(3);
        for (int i = 0; i < numContacts; i++) {
            ContactModel contact = createContact(partner);
            contacts.add(contact);
        }
        partner.setContacts(contacts);

        return partner;
    }

    private ContactModel createContact(PartnerModel partner) {
        ContactModel contact = new ContactModel();
        String fullName = generateFullName();
        contact.setName(fullName);
        contact.setPhone(generatePhone());
        contact.setEmail(generateEmail(fullName));
        contact.setPartner(partner);
        return contact;
    }

    private String getCompanyName(int index) {
        String[] names = {
            "Trgovina Mercator", "Kompas Group", "Petrol Ljubljana", "Krka Novo Mesto",
            "Gorenje Group", "Lek Farmacija", "Sava Turizem", "Delo Revije",
            "Trimo Trebnje", "Unior Zreče", "TPV Automotive", "Talum Kidričevo",
            "Alpina Žiri", "Lisca Sevnica", "Terme Čatež"
        };
        return names[index % names.length];
    }

    private String getShortName(int index) {
        String[] names = {
            "MERC", "KOMP", "PTRL", "KRKA", "GORN", "LEK", "SAVA", "DELO",
            "TRIM", "UNIR", "TPV", "TALM", "ALPN", "LISC", "TERM"
        };
        return names[index % names.length];
    }

    private String getAddress(int index) {
        String[] streets = {
            "Dunajska cesta", "Slovenska cesta", "Celovška cesta", "Trg republike",
            "Njegoševa cesta", "Šmartinska cesta", "Tržaška cesta", "Letališka cesta"
        };
        String[] cities = {
            "Ljubljana", "Maribor", "Celje", "Kranj", "Velenje", "Koper",
            "Novo Mesto", "Ptuj", "Kamnik", "Trbovlje", "Jesenice", "Nova Gorica"
        };
        return streets[random.nextInt(streets.length)] + " " + 
               (random.nextInt(100) + 1) + ", " + 
               cities[random.nextInt(cities.length)];
    }

    private String getPostCode(int index) {
        String[] codes = {
            "1000", "2000", "3000", "4000", "3320", "6000",
            "8000", "2250", "1241", "1420", "4270", "5000"
        };
        return codes[index % codes.length];
    }

    private String generateTaxNumber() {
        return String.format("%08d", 10000000 + random.nextInt(90000000));
    }

    private String generateFullName() {
        String[] firstNames = {
            "Janez", "Marko", "Andrej", "Luka", "Matej", "Peter",
            "Ana", "Nina", "Eva", "Maja", "Sara", "Petra"
        };
        String[] lastNames = {
            "Novak", "Horvat", "Kovač", "Krajnc", "Zupan", "Potočnik",
            "Kovačič", "Mlakar", "Kos", "Vidmar", "Golob", "Turk"
        };
        return firstNames[random.nextInt(firstNames.length)] + " " +
               lastNames[random.nextInt(lastNames.length)];
    }

    private String generatePhone() {
        return "+386" + (30 + random.nextInt(20)) + 
               String.format("%06d", random.nextInt(1000000));
    }

    private String generateEmail(String fullName) {
        String[] parts = fullName.toLowerCase().split(" ");
        String domain = random.nextBoolean() ? "gmail.com" : "example.si";
        return parts[0] + "." + parts[1] + "@" + domain;
    }
}
