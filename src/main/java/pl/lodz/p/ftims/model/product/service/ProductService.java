package pl.lodz.p.ftims.model.product.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import pl.lodz.p.ftims.model.product.model.Product;
import pl.lodz.p.ftims.model.product.repository.IProductRepository;
import pl.lodz.p.ftims.model.product.repository.ProductRepository;
import pl.lodz.p.ftims.model.storage.service.ProductLineService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ProductService implements IProductService {
    private IProductRepository productRepository;

    public ProductService() {
        this.productRepository = new ProductRepository();
    }

    @Override
    public String addProduct(String name, Double purchasePrice, Double sellPrice, Double discount) {
        Product product = new Product("", name, purchasePrice, sellPrice, discount);
        String id = productRepository.addProduct(product);
        new ProductLineService().addProductLine(0, productRepository.getProduct(id));

        return id;
    }

    @Override
    public void updateProduct(String id, String name, Double purchasePrice, Double sellPrice, Double discount) {
        this.productRepository.updateProduct(new Product(id, name, purchasePrice, sellPrice, discount));
    }

    @Override
    public void removeProduct(String id) {
        this.productRepository.removeProduct(id);
    }

    @Override
    public Product getProduct(String id) {
        return this.productRepository.getProduct(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return this.productRepository.getAll();
    }

    @Override
    public Product getProductByName(String name) {
        return productRepository.getAll().stream().filter(product -> product.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public List<String> getAllProductsName() {
        return productRepository.getAll().stream().map(Product::getName).collect(Collectors.toList());
    }

    @Override
    public void createProductRaport(List<Product> productList) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.newLineAtOffset(25, 725);
            contentStream.setLeading(14.5f);
            for (Product t : productList
            ) {
                contentStream.showText(t.toString());
                contentStream.newLine();
            }
            contentStream.endText();
            contentStream.close();
            StringBuilder sb = new StringBuilder();
            File file = new File("Raports");
            file.mkdir();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss");
            sb.append("./Raports/ProductRaport_");
            sb.append(LocalDateTime.now().format(formatter));
            sb.append(".pdf");
            document.save(new File(sb.toString()));
            document.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
