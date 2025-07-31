-- 테이블 init
DELETE FROM product_catalog;
DELETE FROM brand;
ALTER TABLE product_catalog AUTO_INCREMENT = 1;
ALTER TABLE brand AUTO_INCREMENT = 1;

INSERT INTO product_catalog (ref_brand_id, product_name, base_price, image_url, description, published_at, created_at, updated_at) VALUES (999, 'Product with No Brand', 110.00, 'https://example.com/puma_wildrider.png', 'Urban-inspired street style.', NOW(), NOW(), NOW());