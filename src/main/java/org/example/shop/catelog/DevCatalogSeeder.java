package org.example.shop.catelog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
@Profile("dev")
class DevCatalogSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DevCatalogSeeder.class);
    private final ProductRepository products;
    private final CategoryRepository categories;

    DevCatalogSeeder(ProductRepository products, CategoryRepository categories) {
        this.products = products;
        this.categories = categories;
    }

    @Override
    @Transactional          // one transaction, so categories stay managed while products link to them
    public void run(String... args) {
        if (products.count() > 0) {
            log.info("Demo catalog already present, skipping seeding");
            return;
        }
        Category bags = categories.save(new Category("Bags"));
        Category stationery = categories.save(new Category("Stationery"));

        Product notebook = new Product("Notebook", "A5, 100 pages", new BigDecimal("4.00"), 100, true);
        Product pen = new Product("Pen", "Blue ink", new BigDecimal("1.50"), 200, false);
        Product backpack = new Product("Blue Backpack", "20 litres", new BigDecimal("45.00"), 5, true);
        Product travelBag = new Product("Travel Bag", "Cabin size", new BigDecimal("60.00"), 2, false);

        notebook.addCategory(stationery);
        pen.addCategory(stationery);
        backpack.addCategory(bags);
        travelBag.addCategory(bags);

        products.saveAll(List.of(notebook, pen, backpack, travelBag));
        log.info("Seeded 2 categories and 4 products");
    }
}
