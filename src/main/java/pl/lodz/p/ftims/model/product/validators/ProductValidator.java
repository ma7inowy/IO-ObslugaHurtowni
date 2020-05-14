package pl.lodz.p.ftims.model.product.validators;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.math.NumberUtils;

public class ProductValidator {
    public static void validate(String productName, String productPurchasePrice, String productSellPrice, String productDiscount) throws Exception {
        validateProductName(productName);
        validateProductPurchasePrice(productPurchasePrice);
        validateProductSellPrice(productSellPrice);
        validateProductDiscount(productDiscount);
    }

    private static void validateProductName(String productName) throws Exception {
        if (productName.isEmpty()) {
            throw new Exception("Name is empty!");
        }
        if (NumberUtils.isCreatable(productName)) {
            throw new Exception("Name has wrong format!");
        }

    }

    private static void validateProductPurchasePrice(String productPurchasePrice) throws Exception {
        if (productPurchasePrice.isEmpty()) {
            throw new Exception("Purchase Price is empty!");
        }
        if (!NumberUtils.isCreatable(productPurchasePrice)) {
            throw new Exception("Purchase Price has wrong format!");
        }
        if (Double.parseDouble(productPurchasePrice) < 0) {
            throw new Exception("Purchase Price must be greater than zero!");
        }
    }

    private static void validateProductSellPrice(String productSellPrice) throws Exception {
        if (productSellPrice.isEmpty()) {
            throw new Exception("Sell Price is empty!");
        }
        if (!NumberUtils.isCreatable(productSellPrice)) {
            throw new Exception("Sell Price has wrong format!");
        }
        if (Double.parseDouble(productSellPrice) < 0) {
            throw new Exception("Purchase Price must be greater than zero!");
        }
    }

    private static void validateProductDiscount(String productDiscount) throws Exception {
        if (productDiscount.isEmpty()) {
            throw new Exception("Discount is empty!");
        }
        if (!NumberUtils.isCreatable(productDiscount)) {
            throw new Exception("Discount wrong format!");
        }
        Range<Double> range = Range.between(0.5, 1.0);
        if (!range.contains(Double.parseDouble(productDiscount))) {
            throw new Exception("Discount value is not between <0.5, 1.0>!");
        }
    }


}
